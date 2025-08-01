package com.notedgeek.kcmachine.build.compile.backend.translate

import com.notedgeek.kcmachine.build.compile.backend.model.*

fun translateExpression(expr: Expression, translationContext: TranslationContext) {
    when (expr) {
        is IntExpression -> translateIntExpression(expr, translationContext)
        is BinaryOperatorExpression -> translateBinaryOperatorInstruction(expr, translationContext)
        is FunctionCallExpression -> translateFunctionCallExpression(expr, translationContext)
        is IdentifierExpression -> translateIdentifierExpression(expr, translationContext)
        else -> TODO()
    }
}

private val binaryOperationInstructionMap = mapOf(
    "==" to "EQ",
    "+" to ADD,
    "-" to SUB,
    "*" to MUL,
    "/" to DIV
)

private fun translateBinaryOperatorInstruction(expr: BinaryOperatorExpression, translationContext: TranslationContext) {
    val instruction = binaryOperationInstructionMap[expr.operator] ?: throw TranslateException("Unknown operator ${expr.operator}.")
    translateExpression(expr.left, translationContext)
    translateExpression(expr.right, translationContext)
    translationContext.emit(instruction)
}

private fun translateFunctionCallExpression(functionCallExpression: FunctionCallExpression, translationContext: TranslationContext) {
    with(translationContext) {
        val arguments = functionCallExpression.arguments.reversed()
        if(arguments.isNotEmpty()) {
            arguments.forEach { translateExpression(it, translationContext) }
        } else {
            emit(PUSH_CONST("0"))
        }
        emit(CALL(functionCallExpression.name))
        val argumentsSize = arguments.size
        if(argumentsSize > 1) {
            emit(DEC_STACK(argumentsSize))
            emit(PUSH_STACK_OFFSET(argumentsSize - 1))
        }
    }
}

private fun translateIntExpression(expr: IntExpression, translationContext: TranslationContext) {
    translationContext.emit(PUSH_CONST(expr.value.toString()))
}

private fun translateIdentifierExpression(expr: IdentifierExpression, translationContext: TranslationContext) {
    when (val nameReference = expr.nameReference) {
        is ArgumentReference -> translationContext.emit(PUSH_A(nameReference.index))
    }
}