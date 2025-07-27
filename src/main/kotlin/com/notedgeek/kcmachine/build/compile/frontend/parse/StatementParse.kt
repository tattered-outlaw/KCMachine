package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.CGCompoundStatement
import com.notedgeek.kcmachine.build.compile.CGReturnStatement
import com.notedgeek.kcmachine.build.compile.CGStatement

fun parseStatement(tokenBuffer: TokenBuffer) : CGStatement {
    val nextLexeme = tokenBuffer.nextLexeme()
    return when(nextLexeme) {
        "{" -> parseCompoundStatement(tokenBuffer)
        "return" -> parseReturnStatement(tokenBuffer)
        else -> TODO()
    }
}

fun parseCompoundStatement (tokenBuffer: TokenBuffer) : CGCompoundStatement {
    val start = tokenBuffer.index
    tokenBuffer.consume("{")
    val statements = ArrayList<CGStatement>()
    while (tokenBuffer.nextLexeme() != "}") {
        statements.add(parseStatement(tokenBuffer))
    }
    tokenBuffer.consume("}")
    return CGCompoundStatement(start, tokenBuffer.index, statements)
}

fun parseReturnStatement(tokenBuffer: TokenBuffer) : CGReturnStatement {
    val start = tokenBuffer.index
    tokenBuffer.consume("return")
    val expression = parseExpression(tokenBuffer)
    tokenBuffer.consume(";")
    return CGReturnStatement(start, tokenBuffer.index, expression)
}