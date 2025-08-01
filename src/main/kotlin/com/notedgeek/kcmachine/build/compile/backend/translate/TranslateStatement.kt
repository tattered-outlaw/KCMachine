package com.notedgeek.kcmachine.build.compile.backend.translate

import com.notedgeek.kcmachine.build.compile.backend.model.CompoundStatement
import com.notedgeek.kcmachine.build.compile.backend.model.IfStatement
import com.notedgeek.kcmachine.build.compile.backend.model.ReturnStatement
import com.notedgeek.kcmachine.build.compile.backend.model.Statement

fun translateStatement(statement: Statement, translationContext: TranslationContext) {
    when (statement) {
        is CompoundStatement -> translateCompoundStatement(statement, translationContext)
        is ReturnStatement -> translateReturnStatement(statement, translationContext)
        is IfStatement -> translateIfStatement(statement, translationContext)
        else -> TODO()
    }
}

private fun translateCompoundStatement(compoundStatement: CompoundStatement, translationContext: TranslationContext) {
    compoundStatement.statements.forEach { translateStatement(it, translationContext) }
}

private fun translateReturnStatement(returnStatement: ReturnStatement, translationContext: TranslationContext) {
    translateExpression(returnStatement.expression, translationContext)
    with(translationContext) {
        emit(SAVE_A(0))
        emit(BP_TO_SP)
        emit(POP_BP)
        emit(RETURN)
    }
}

private fun translateIfStatement(stmt: IfStatement, translationContext: TranslationContext) {
    val labelHead = translationContext.labelHead("if")
    with(translationContext) {
        translateExpression(stmt.condition, translationContext)
        emit(JMP_Z(if(stmt.falseStatement == null) labelHead + "_end" else labelHead + "_else"))
        translateStatement(stmt.trueStatement, translationContext)
        emit(JMP(labelHead + "_end"))
        if(stmt.falseStatement != null) {
            label(labelHead + "_else")
            translateStatement(stmt.falseStatement, translationContext)
        }
        label(labelHead + "_end")
    }
}
