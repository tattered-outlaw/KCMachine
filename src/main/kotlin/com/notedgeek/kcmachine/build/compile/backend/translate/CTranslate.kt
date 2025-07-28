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
        emit(PUSH_BP())
        emit(SP_TO_BP())
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
        emit(SAVE_A("0"))
        emit(BP_TO_SP())
        emit(POP_BP())
        emit(RETURN())
    }
}

private fun translateExpression(expression: Expression, translationContext: TranslationContext) {
    when (expression) {
        is IntExpression -> translateIntExpression(expression, translationContext)
        else -> TODO()
    }
}

private fun translateIntExpression(intExpression: IntExpression, translationContext: TranslationContext) {
    translationContext.emit(PUSH_CONST(intExpression.value.toString()))
}