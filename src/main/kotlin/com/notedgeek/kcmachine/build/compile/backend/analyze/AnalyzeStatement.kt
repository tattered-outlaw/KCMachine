package com.notedgeek.kcmachine.build.compile.backend.analyze

import com.notedgeek.kcmachine.build.compile.backend.model.CompoundStatement
import com.notedgeek.kcmachine.build.compile.backend.model.IfStatement
import com.notedgeek.kcmachine.build.compile.backend.model.ReturnStatement
import com.notedgeek.kcmachine.build.compile.backend.model.Statement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGCompoundStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGIfStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGReturnStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGStatement

fun analyzeStatement(stmt: CGStatement, analysisContext: AnalysisContext): Statement {
    return when (stmt) {
        is CGCompoundStatement -> analyzeCompoundStatement(stmt, analysisContext)
        is CGReturnStatement -> analyzeReturnStatement(stmt, analysisContext)
        is CGIfStatement -> analyzeIfStatement(stmt, analysisContext)
        else -> TODO()
    }
}

fun analyzeCompoundStatement(stmt: CGCompoundStatement, analysisContext: AnalysisContext): CompoundStatement {
    val statements = stmt.statements.map { analyzeStatement(it, analysisContext) }
    return CompoundStatement(stmt.start, stmt.end, statements)
}

private fun analyzeReturnStatement(stmt: CGReturnStatement, analysisContext: AnalysisContext): ReturnStatement {
    return ReturnStatement(stmt.start, stmt.end, analyzeExpression(stmt.expression, analysisContext))
}

private fun analyzeIfStatement(stmt: CGIfStatement, analysisContext: AnalysisContext): IfStatement {
    return IfStatement(
        stmt.start, stmt.end, analyzeExpression(stmt.condition, analysisContext), analyzeStatement(stmt.trueStatement, analysisContext),
        stmt.falseStatement?.let { analyzeStatement(it, analysisContext) } ?: null)
}