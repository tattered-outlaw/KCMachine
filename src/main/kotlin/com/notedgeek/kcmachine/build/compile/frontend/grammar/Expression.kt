package com.notedgeek.kcmachine.build.compile.frontend.grammar

interface CGExpression : CGElement

class CGConstantValueExpression(override var start: Int, override var end: Int, val expression: CGExpression) : CGElement

class CGBinaryOperationExpression(
    override var start: Int, override var end: Int, val left: CGExpression, val operator: String,
    val right: CGExpression
) : CGExpression

interface CGPostfixExpression : CGExpression

data class CGFunctionCallExpression(
    override var start: Int,
    override var end: Int,
    val expression: CGExpression,
    val arguments: List<CGExpression>
) : CGPostfixExpression

interface CGPrimaryExpression : CGExpression

data class CGIdentifierExpression(override var start: Int, override var end: Int, val value: String) : CGExpression

interface CGConstantLiteralExpression : CGPrimaryExpression

data class CGIntegerConstant(override var start: Int, override var end: Int, val value: Long) : CGConstantLiteralExpression
