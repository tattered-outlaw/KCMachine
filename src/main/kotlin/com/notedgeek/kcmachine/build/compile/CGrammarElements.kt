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

sealed interface CGDeclarator : CGElement

interface CGConcreteDeclarator : CGDeclarator

data class CGPointerDeclarator(
    override var start: Int,
    override var end: Int,
    val pointer: CGPointer,
    val declarator: CGConcreteDeclarator
) :
    CGConcreteDeclarator

interface CGDirectDeclarator : CGConcreteDeclarator

data class CGIdentifierDeclarator(override var start: Int, override var end: Int, val identifier: CGIdentifierExpression) :
    CGConcreteDeclarator

interface CGFunctionDeclarator : CGDirectDeclarator

data class CGEmptyFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGConcreteDeclarator
) : CGFunctionDeclarator

data class CGParameterFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGConcreteDeclarator,
    val parameterTypeList: List<CSParameterDeclaration>
) : CGFunctionDeclarator

data class CGIdentifierFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGConcreteDeclarator,
    val parameterTypeList: List<CGIdentifierDeclarator>
) : CGFunctionDeclarator

data class CGArrayDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGConcreteDeclarator,
    val parameterTypeList: CGConstantValueExpression? = null
) : CGConcreteDeclarator


interface CGAbstractDeclarator : CGDeclarator

data class CGAbstractPointerDeclarator(
    override var start: Int,
    override var end: Int,
    val pointer: CGPointer,
    val declarator: CGAbstractDeclarator
) :
    CGAbstractDeclarator

interface CGDirectAbstractDeclarator : CGAbstractDeclarator

interface CGAbstractFunctionDeclarator : CGDirectAbstractDeclarator

data class CGAbstractEmptyFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGAbstractDeclarator
) : CGAbstractFunctionDeclarator

data class CGAbstractParameterFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGAbstractDeclarator,
    val parameterTypeList: List<CSAbstractParameterDeclaration>
) : CGAbstractFunctionDeclarator

data class CGAbstractArrayDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGAbstractDeclarator,
    val parameterTypeList: CGConstantValueExpression? = null
) : CGAbstractDeclarator

data class CGAbstractDeclaratorPlaceholder(override var start: Int, override var end: Int) : CGAbstractDeclarator


data class CGPointer(override var start: Int, override var end: Int, val typeQualifiers: List<CGTypeQualifier>) : CGElement

interface CGDeclarationSpecifier

enum class CGTypeSpecifier(val lexeme: String) : CGDeclarationSpecifier {
    INT("int");

    companion object {
        val lexemeMap = entries.associateBy { it.lexeme }
    }
}

enum class CGTypeQualifier(val lexeme: String) : CGDeclarationSpecifier {
    CONST("const"), VOLATILE("volatile");

    companion object {
        val lexemeMap = entries.associateBy { it.lexeme }
    }
}

enum class CGStorageClassSpecifier(val lexeme: String) : CGDeclarationSpecifier {
    STATIC("static");

    companion object {
        val lexemeMap = entries.associateBy { it.lexeme }
    }
}

val declarationSpecifierMap = sequence {
    yieldAll(CGTypeSpecifier.entries)
    yieldAll(CGTypeQualifier.entries)
    yieldAll(CGStorageClassSpecifier.entries)
}.toList()

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

data class CSParameterDeclaration(
    override var start: Int,
    override var end: Int,
    val declarationSpecifiers: List<CGDeclarationSpecifier>,
    val declarator: CGConcreteDeclarator
) : CGElement

data class CSAbstractParameterDeclaration(
    override var start: Int,
    override var end: Int,
    val declarationSpecifiers: List<CGDeclarationSpecifier>,
    val declarator: CGAbstractDeclarator
) :
    CGElement