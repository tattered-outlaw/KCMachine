package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.*
import com.notedgeek.kcmachine.build.compile.frontend.Identifier

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

fun parseDeclarator(tokenBuffer: TokenBuffer): CGDeclarator {
    return parseDirectDeclarator(tokenBuffer)
}

fun parseDirectDeclarator(tokenBuffer: TokenBuffer): CGDeclarator {
    val start = tokenBuffer.index
    val token = tokenBuffer.nextToken()
    if (token !is Identifier) {
        TODO()
    }
    val directDeclarator = CGIdentifierDeclarator(start, tokenBuffer.index, parseIdentifierExpression(tokenBuffer))
    tokenBuffer.consume("(")
    tokenBuffer.consume(")")
    return CGEmptyFunctionDeclarator(start, tokenBuffer.index, directDeclarator)
}

fun parseDeclarationSpecifiers(tokenBuffer: TokenBuffer): List<CGDeclarationSpecifier> = sequence {
    while (true) {
        val lexeme = tokenBuffer.nextLexeme()
        val start = tokenBuffer.index

        val type = CGType.lexemeMap[lexeme]
        if (type != null) {
            tokenBuffer.consume()
            yield(CGTypeSpecifier(start, tokenBuffer.index, type))
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
