package com.notedgeek.kcmachine.build.compile.backend.translate

import com.notedgeek.kcmachine.build.compile.backend.model.BinaryOperatorExpression
import com.notedgeek.kcmachine.build.compile.backend.model.Expression
import com.notedgeek.kcmachine.build.compile.backend.model.FunctionCallExpression
import com.notedgeek.kcmachine.build.compile.backend.model.IntExpression

fun translateExpression(expr: Expression, translationContext: TranslationContext) {
    when (expr) {
        is IntExpression -> translateIntExpression(expr, translationContext)
        is BinaryOperatorExpression -> translateBinaryOperatorInstruction(expr, translationContext)
        is FunctionCallExpression -> translateFunctionCallExpression(expr, translationContext)
        else -> TODO()
    }
}

private val binaryOperationInstructionMap = mapOf(
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
        emit(PUSH_CONST("0"))
        emit(CALL(functionCallExpression.name))
    }
}

private fun translateIntExpression(expr: IntExpression, translationContext: TranslationContext) {
    translationContext.emit(PUSH_CONST(expr.value.toString()))
}