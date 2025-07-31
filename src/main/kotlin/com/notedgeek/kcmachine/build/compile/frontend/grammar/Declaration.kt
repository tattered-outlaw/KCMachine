package com.notedgeek.kcmachine.build.compile.frontend.grammar

interface CGExternalDeclaration : CGElement

data class CGFunctionDefinition(
    override var start: Int,
    override var end: Int,
    val declarationSpecifiers: List<CGDeclarationSpecifier>,
    val declarator: CGFunctionDeclarator,
    val compoundStatement: CGCompoundStatement
) : CGExternalDeclaration

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