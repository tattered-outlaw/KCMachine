package com.notedgeek.kcmachine.build.compile.backend.analyze

import com.notedgeek.kcmachine.build.compile.backend.model.*
import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

fun analyzeExpression(cgExpression: CGExpression): Expression {
    return when (cgExpression) {
        is CGIntegerConstant -> analyzeIntExpression(cgExpression)
        is CGBinaryOperationExpression -> analyzeBinaryOperatorExpression(cgExpression)
        is CGFunctionCallExpression -> analyzeFunctionCallExpression(cgExpression)
        else -> TODO()
    }
}

private fun analyzeBinaryOperatorExpression(expr: CGBinaryOperationExpression): BinaryOperatorExpression {
    return BinaryOperatorExpression(expr.start, expr.end, analyzeExpression(expr.left), expr.operator, analyzeExpression(expr.right))
}

private fun analyzeFunctionCallExpression(expr: CGFunctionCallExpression): FunctionCallExpression {
    if (expr.expression !is CGIdentifierExpression) {
        throw AnalyzeException("Function call expression type ${expr.expression} not supported")
    }
    return FunctionCallExpression(expr.start, expr.end, IntType, expr.expression.value, expr.arguments.map { analyzeExpression(it) })
}

private fun analyzeIntExpression(cgIntegerConstant: CGIntegerConstant): IntExpression {
    return IntExpression(cgIntegerConstant.start, cgIntegerConstant.end, cgIntegerConstant.value)
}
