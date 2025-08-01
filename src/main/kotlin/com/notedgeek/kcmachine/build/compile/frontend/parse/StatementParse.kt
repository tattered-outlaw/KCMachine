package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGCompoundStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGIfStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGReturnStatement
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGStatement

fun parseStatement(tokenBuffer: TokenBuffer): CGStatement {
    val nextLexeme = tokenBuffer.nextLexeme()
    return when (nextLexeme) {
        "{" -> ::parseCompoundStatement
        "return" -> ::parseReturnStatement
        "if" -> ::parseIfStatement
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

fun parseIfStatement(tokenBuffer: TokenBuffer): CGIfStatement {
    val start = tokenBuffer.index
    tokenBuffer.consume("if")
    tokenBuffer.consume("(")
    val condition = parseExpression(tokenBuffer)
    tokenBuffer.consume(")")
    val trueStatement = parseStatement(tokenBuffer)
    var falseStatement: CGStatement? = null
    if (tokenBuffer.nextLexeme() == "else") {
        tokenBuffer.consume("else")
        falseStatement = parseStatement(tokenBuffer)
    }
    return CGIfStatement(start, tokenBuffer.index, condition, trueStatement, falseStatement)
}