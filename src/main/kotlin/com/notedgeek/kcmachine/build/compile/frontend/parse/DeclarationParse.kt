package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

// used initially to collect parsed tokens while trying to discriminate between
// function_definition and declaration
private data class ExternalDeclarationHead(
    val start: Int,
    val end: Int,
    val declarationSpecifiers: List<CGDeclarationSpecifier>,
    val declarator: CGDeclarator
)

fun parseExternalDeclaration(tokenBuffer: TokenBuffer): CGExternalDeclaration {
    val start = tokenBuffer.index
    val externalDeclarationHead = parseExternalDeclarationHead(tokenBuffer)
    val declarator = externalDeclarationHead.declarator
    if (tokenBuffer.nextLexeme() == "{" && declarator is CGFunctionDeclarator) {
        val compoundStatement = parseCompoundStatement(tokenBuffer)
        return CGFunctionDefinition(
            start,
            tokenBuffer.index,
            externalDeclarationHead.declarationSpecifiers,
            declarator,
            compoundStatement
        )
    } else {
        TODO()
    }
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

private fun parseExternalDeclarationHead(tokenBuffer: TokenBuffer): ExternalDeclarationHead {
    val start = tokenBuffer.index
    val declarationSpecifiers = parseDeclarationSpecifiers(tokenBuffer)
    val declarator = parseDeclarator(tokenBuffer)
    return ExternalDeclarationHead(start, tokenBuffer.index, declarationSpecifiers, declarator)
}
