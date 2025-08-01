package com.notedgeek.kcmachine.build.compile.backend.analyze

import com.notedgeek.kcmachine.build.compile.backend.model.*
import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

fun analyzeExpression(cgExpression: CGExpression, analysisContext: AnalysisContext): Expression {
    return when (cgExpression) {
        is CGIntegerConstant -> analyzeIntExpression(cgExpression)
        is CGBinaryOperationExpression -> analyzeBinaryOperatorExpression(cgExpression, analysisContext)
        is CGFunctionCallExpression -> analyzeFunctionCallExpression(cgExpression, analysisContext)
        is CGIdentifierExpression -> analyzeIdentifierExpression(cgExpression, analysisContext)
        else -> TODO()
    }
}

private fun analyzeBinaryOperatorExpression(expr: CGBinaryOperationExpression, analysisContext: AnalysisContext): BinaryOperatorExpression {
    return BinaryOperatorExpression(
        expr.start,
        expr.end,
        analyzeExpression(expr.left, analysisContext),
        expr.operator,
        analyzeExpression(expr.right, analysisContext)
    )
}

private fun analyzeFunctionCallExpression(expr: CGFunctionCallExpression, analysisContext: AnalysisContext): FunctionCallExpression {
    if (expr.expression !is CGIdentifierExpression) {
        throw AnalyzeException("Function call expression type ${expr.expression} not supported")
    }
    return FunctionCallExpression(expr.start, expr.end, IntType, expr.expression.value, expr.arguments.map { analyzeExpression(it, analysisContext) })
}

private fun analyzeIntExpression(expr: CGIntegerConstant): IntExpression {
    return IntExpression(expr.start, expr.end, expr.value)
}

private fun analyzeIdentifierExpression(expr: CGIdentifierExpression, analysisContext: AnalysisContext): IdentifierExpression {
    return IdentifierExpression(expr.start, expr.end, expr.value, analysisContext.getReference(expr.value))
}
