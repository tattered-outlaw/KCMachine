package com.notedgeek.kcmachine.build.compile.backend.translate

import com.notedgeek.kcmachine.build.compile.backend.model.*

fun translateC(compilationUnit: CompilationUnit, translationContext: TranslationContext) {
    for (compilationItem in compilationUnit.items) {
        if (compilationItem is FunctionDefinition) {
            translateFunctionDefinition(compilationItem, translationContext)
        } else {
            TODO()
        }
    }
}

private fun translateFunctionDefinition(functionDefinition: FunctionDefinition, translationContext: TranslationContext) {
    with(translationContext) {
        emit("${functionDefinition.name}:")
        emit(Inst.PUSH_BP())
        emit(Inst.SP_TO_BP())
    }
    functionDefinition.statement.statements.forEach { translateStatement(it, translationContext) }
}

private fun translateStatement(statement: Statement, translationContext: TranslationContext) {
    when (statement) {
        is CompoundStatement -> translateCompoundStatement(statement, translationContext)
        is ReturnStatement -> translateReturnStatement(statement, translationContext)
    }
}

private fun translateCompoundStatement(compoundStatement: CompoundStatement, translationContext: TranslationContext) {
    compoundStatement.statements.forEach { translateStatement(it, translationContext) }
}

private fun translateReturnStatement(returnStatement: ReturnStatement, translationContext: TranslationContext) {
    translateExpression(returnStatement.expression, translationContext)
    with(translationContext) {
        emit(Inst.SAVE_A("0"))
        emit(Inst.BP_TO_SP())
        emit(Inst.POP_BP())
        emit(Inst.RETURN())
    }
}

private fun translateExpression(expression: Expression, translationContext: TranslationContext) {
    when (expression) {
        is IntExpression -> translateIntExpression(expression, translationContext)
        else -> TODO()
    }
}

private fun translateIntExpression(intExpression: IntExpression, translationContext: TranslationContext) {
    translationContext.emit(Inst.PUSH_CONST(intExpression.value.toString()))
}