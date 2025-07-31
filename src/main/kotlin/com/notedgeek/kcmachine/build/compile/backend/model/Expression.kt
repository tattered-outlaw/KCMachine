package com.notedgeek.kcmachine.build.compile.backend.model

interface Expression : Element {
    val type: Type
}

data class BinaryOperatorExpression(
    override val start: Int,
    override val end: Int,
    val left: Expression,
    val operator: String,
    val right: Expression
) : Expression {
    override val type = IntType
}

interface PostfixExpression : Expression

data class FunctionCallExpression(
    override val start: Int,
    override val end: Int,
    override val type: Type,
    val name: String,
    val arguments: List<Expression>
) : PostfixExpression

data class IntExpression(override val start: Int, override val end: Int, val value: Long) : Expression {
    override val type = IntType
}
data class IdentifierExpression(override val start: Int, override val end: Int, val value: String) : Expression {
    override val type = IntType
}
