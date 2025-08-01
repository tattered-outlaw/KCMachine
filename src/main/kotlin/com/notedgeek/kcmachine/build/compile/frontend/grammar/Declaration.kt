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

val declarationSpecifierLexemeMap = sequence {
    yieldAll(CGTypeSpecifier.lexemeMap.entries)
    yieldAll(CGTypeQualifier.lexemeMap.entries)
    yieldAll(CGStorageClassSpecifier.lexemeMap.entries)
}.map { it.key to it.value }.toMap()

data class SpecifiedDeclarator(
    override var start: Int,
    override var end: Int,
    val declarationSpecifiers: List<CGDeclarationSpecifier>,
    val declarator: CGDeclarator
) : CGElement

typealias CGParameterDeclaration = SpecifiedDeclarator