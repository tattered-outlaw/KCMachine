package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGCompoundStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGReturnStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGStatement

fun parseStatement(tokenBuffer: TokenBuffer): CGStatement {
    val nextLexeme = tokenBuffer.nextLexeme()
    return when (nextLexeme) {
        "{" -> ::parseCompoundStatement
        "return" -> ::parseReturnStatement
        else -> TODO()
    }.invoke(tokenBuffer)
}

fun parseCompoundStatement(tokenBuffer: TokenBuffer): CGCompoundStatement {
    val start = tokenBuffer.index
    tokenBuffer.consume("{")
    val statements = sequence {
        while (tokenBuffer.nextLexeme() != "}") {
           yield(parseStatement(tokenBuffer))
        }
    }.toList()
    tokenBuffer.consume("}")
    return CGCompoundStatement(start, tokenBuffer.index, statements)
}

fun parseReturnStatement(tokenBuffer: TokenBuffer): CGReturnStatement {
    val start = tokenBuffer.index
    tokenBuffer.consume("return")
    val expression = parseExpression(tokenBuffer)
    tokenBuffer.consume(";")
    return CGReturnStatement(start, tokenBuffer.index, expression)
}