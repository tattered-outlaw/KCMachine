package com.notedgeek.kcmachine.build.compile

sealed interface CGElement {
    val start: Int
    val end: Int
}

data class CGCompilationUnit(override val start: Int, override val end: Int, val externalDeclarations: List<CGExternalDeclaration>) : CGElement

interface CGExternalDeclaration : CGElement

data class CGFunctionDefinition(
    override val start: Int,
    override val end: Int,
    val declarationSpecifiers: List<CGDeclarationSpecifier>,
    val declarator: CGFunctionDeclarator,
    val compoundStatement: CGCompoundStatement
) : CGExternalDeclaration

interface CGStatement : CGElement

data class CGCompoundStatement(override val start: Int, override val end: Int, val statements: List<CGStatement>) : CGStatement

data class CGReturnStatement(override val start: Int, override val end: Int, val expression: CGExpression) : CGStatement

interface CGExpression : CGElement

interface CGPrimaryExpression : CGExpression

interface CGConstantExpression : CGPrimaryExpression

data class CGIntegerConstant(override val start: Int, override val end: Int, val value: Long) : CGConstantExpression

interface CGDeclarator : CGElement

interface CGDirectDeclarator : CGDeclarator

data class CGIdentifierDeclarator(override val start: Int, override val end: Int, val identifier: String) : CGDirectDeclarator

interface CGFunctionDeclarator : CGDirectDeclarator

data class CGEmptyFunctionDeclarator(override val start: Int, override val end: Int, val declarator: CGDeclarator) : CGFunctionDeclarator

interface CGDeclarationSpecifier : CGElement

enum class CGType(val lexeme: String) {
    INT("int")

    ;

    companion object {
        val lexemeMap = entries.associateBy { it.lexeme }
    }
}


data class CGTypeSpecifier(override val start: Int, override val end: Int, val type: CGType) : CGDeclarationSpecifier

