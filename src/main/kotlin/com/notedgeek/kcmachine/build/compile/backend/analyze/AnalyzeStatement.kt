package com.notedgeek.kcmachine.build.compile.backend.analyze

import com.notedgeek.kcmachine.build.compile.backend.model.CompoundStatement
import com.notedgeek.kcmachine.build.compile.backend.model.ReturnStatement
import com.notedgeek.kcmachine.build.compile.backend.model.Statement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGCompoundStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGReturnStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGStatement

fun analyzeStatement(cgStatement: CGStatement, analysisContext: AnalysisContext): Statement {
    return when (cgStatement) {
        is CGCompoundStatement -> analyzeCompoundStatement(cgStatement, analysisContext)
        is CGReturnStatement -> analyzeReturnStatement(cgStatement, analysisContext)
        else -> TODO()
    }
}

fun analyzeCompoundStatement(cgCompoundStatement: CGCompoundStatement, analysisContext: AnalysisContext): CompoundStatement {
    val statements = cgCompoundStatement.statements.map { analyzeStatement(it, analysisContext) }
    return CompoundStatement(cgCompoundStatement.start, cgCompoundStatement.end, statements)
}

private fun analyzeReturnStatement(cgReturnStatement: CGReturnStatement, analysisContext: AnalysisContext): ReturnStatement {
    return ReturnStatement(cgReturnStatement.start, cgReturnStatement.end, analyzeExpression(cgReturnStatement.expression, analysisContext))
}
