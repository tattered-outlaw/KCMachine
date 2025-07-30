package com.notedgeek.kcmachine.build.compile.backend.model

sealed interface Element {
    val start: Int
    val end: Int
}

data class CompilationUnit(override val start: Int, override val end: Int, val items: List<CompilationItem>) : Element

interface CompilationItem : Element

data class FunctionDefinition(
    override val start: Int,
    override val end: Int,
    val name: String,
    val returnType: QualifiedType,
    val parameters: List<NameAndType>,
    val statement: CompoundStatement
) : CompilationItem

interface Statement : Element

data class CompoundStatement(override val start: Int, override val end: Int, val statements: List<Statement>) : Statement

data class ReturnStatement(override val start: Int, override val end: Int, val expression: Expression) : Statement

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

data class QualifiedType(var type: Type)

data class NameAndType(var type: QualifiedType)