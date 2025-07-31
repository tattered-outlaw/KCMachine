package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

// used initially to collect parsed tokens while trying to discriminate between
// function_definition and declaration
private data class DeclarationHead(
    val start: Int,
    val end: Int,
    val declarationSpecifiers: List<CGDeclarationSpecifier>,
    val declarator: CGDeclarator
)

fun parseExternalDeclaration(tokenBuffer: TokenBuffer): CGExternalDeclaration {
    val start = tokenBuffer.index
    val declarationHead = parseExternalDeclarationHead(tokenBuffer)
    val declarator = declarationHead.declarator
    if (tokenBuffer.nextLexeme() == "{" && declarator is CGFunctionDeclarator) {
        return parseFunctionDefinition(tokenBuffer, start, declarationHead.declarationSpecifiers, declarator)
    } else {
        TODO()
    }
}

fun parseFunctionDefinition(
    tokenBuffer: TokenBuffer,
    start: Int,
    declarationSpecifiers: List<CGDeclarationSpecifier>,
    declarator: CGFunctionDeclarator
): CGFunctionDefinition {
    val compoundStatement = parseCompoundStatement(tokenBuffer)
    return CGFunctionDefinition(
        start,
        tokenBuffer.index,
        declarationSpecifiers,
        declarator,
        compoundStatement
    )
}


fun parseDeclarationSpecifiers(tokenBuffer: TokenBuffer): List<CGDeclarationSpecifier> = sequence {
    while (true) {
        val lexeme = tokenBuffer.nextLexeme()

        val typeSpecifier = CGTypeSpecifier.lexemeMap[lexeme]
        if (typeSpecifier != null) {
            tokenBuffer.consume()
            yield(typeSpecifier)
            continue
        }

        val typeQualifier = CGTypeQualifier.lexemeMap[lexeme]
        if (typeQualifier != null) {
            tokenBuffer.consume()
            yield(typeQualifier)
            continue
        }

        break
    }
}.toList()

fun parseParameterDeclaration(tokenBuffer: TokenBuffer): CGParameterDeclaration {
    val start = tokenBuffer.index
    val declarationSpecifiers = parseDeclarationSpecifiers(tokenBuffer)
    val declarator = parseDeclarator(tokenBuffer)
    return CGParameterDeclaration(start, tokenBuffer.index, declarationSpecifiers, declarator)
}
private fun parseExternalDeclarationHead(tokenBuffer: TokenBuffer): DeclarationHead {
    val start = tokenBuffer.index
    val declarationSpecifiers = parseDeclarationSpecifiers(tokenBuffer)
    val declarator = parseConcreteDeclarator(tokenBuffer)
    return DeclarationHead(start, tokenBuffer.index, declarationSpecifiers, declarator)
}
