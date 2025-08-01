package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

fun parseExternalDeclaration(tokenBuffer: TokenBuffer): CGExternalDeclaration {
    val start = tokenBuffer.index
    val declarationHead = parseSpecifiedDeclarator(tokenBuffer)
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

fun parseSpecifiedDeclarator(tokenBuffer: TokenBuffer): SpecifiedDeclarator {
    val start = tokenBuffer.index
    val declarationSpecifiers = parseDeclarationSpecifiers(tokenBuffer)
    val declarator = parseDeclarator(tokenBuffer)
    return SpecifiedDeclarator(start, tokenBuffer.index, declarationSpecifiers, declarator)
}
