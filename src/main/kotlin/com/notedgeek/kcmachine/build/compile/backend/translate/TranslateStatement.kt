package com.notedgeek.kcmachine.build.compile.backend.translate

import com.notedgeek.kcmachine.build.compile.backend.model.CompoundStatement
import com.notedgeek.kcmachine.build.compile.backend.model.ReturnStatement
import com.notedgeek.kcmachine.build.compile.backend.model.Statement

fun translateStatement(statement: Statement, translationContext: TranslationContext) {
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
        emit(BP_TO_SP)
        emit(POP_BP)
        emit(RETURN)
    }
}
