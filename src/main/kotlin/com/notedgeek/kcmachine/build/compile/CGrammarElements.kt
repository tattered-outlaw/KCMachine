package com.notedgeek.kcmachine.build.compile

sealed interface CGElement {
    var start: Int
    var end: Int
}

data class CGCompilationUnit(override var start: Int, override var end: Int, val externalDeclarations: List<CGExternalDeclaration>) :
    CGElement

interface CGExternalDeclaration : CGElement

data class CGFunctionDefinition(
    override var start: Int,
    override var end: Int,
    val declarationSpecifiers: List<CGDeclarationSpecifier>,
    val declarator: CGFunctionDeclarator,
    val compoundStatement: CGCompoundStatement
) : CGExternalDeclaration

interface CGStatement : CGElement

data class CGCompoundStatement(override var start: Int, override var end: Int, val statements: List<CGStatement>) : CGStatement

data class CGReturnStatement(override var start: Int, override var end: Int, val expression: CGExpression) : CGStatement

interface CGDeclarator : CGElement

interface CGDirectDeclarator : CGDeclarator

data class CGIdentifierDeclarator(override var start: Int, override var end: Int, val identifier: String) : CGDirectDeclarator

interface CGFunctionDeclarator : CGDirectDeclarator

data class CGEmptyFunctionDeclarator(override var start: Int, override var end: Int, val declarator: CGDeclarator) : CGFunctionDeclarator

interface CGDeclarationSpecifier : CGElement

enum class CGType(val lexeme: String) {
    INT("int")

    ;

    companion object {
        val lexemeMap = entries.associateBy { it.lexeme }
    }
}

data class CGTypeSpecifier(override var start: Int, override var end: Int, val type: CGType) : CGDeclarationSpecifier

interface CGExpression : CGElement

class CGBinaryOperationExpression(
    override var start: Int, override var end: Int, val left: CGExpression, val operator: String,
    val right: CGExpression
) : CGExpression

interface CGPrimaryExpression : CGExpression

interface CGConstantExpression : CGPrimaryExpression

data class CGIntegerConstant(override var start: Int, override var end: Int, val value: Long) : CGConstantExpression

