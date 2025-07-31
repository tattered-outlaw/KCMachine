package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.frontend.Identifier
import com.notedgeek.kcmachine.build.compile.frontend.IntegralLiteral
import com.notedgeek.kcmachine.build.compile.frontend.Symbol
import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

private val binaryExpressionParserStack = listOf(
    listOf("*", "/"),
    listOf("+", "-")
).fold(::parsePostfixExpression, ::addParserToStack)

fun parseExpression(tokenBuffer: TokenBuffer): CGExpression {
    return parseConditionalExpression(tokenBuffer)
}

fun parseConstantExpression(tokenBuffer: TokenBuffer): CGConstantValueExpression {
    val start = tokenBuffer.index
    val expression = parseConditionalExpression(tokenBuffer)
    return CGConstantValueExpression(start, tokenBuffer.index, expression)
}

fun parseConditionalExpression(tokenBuffer: TokenBuffer) = binaryExpressionParserStack.invoke(tokenBuffer)

fun parsePostfixExpression(tokenBuffer: TokenBuffer): CGExpression {
    val start = tokenBuffer.index
    var expr = parsePrimaryExpression(tokenBuffer)
    while(true) {
        val lexeme = tokenBuffer.nextLexeme()
        when(lexeme) {
            "(" -> {
                tokenBuffer.consume()
                tokenBuffer.consume(")")
                expr = CGFunctionCallExpression(start, tokenBuffer.index, expr, emptyList())
            }
            else -> break
        }
    }
    return expr
}

fun parsePrimaryExpression(tokenBuffer: TokenBuffer) = when (val token = tokenBuffer.nextToken()) {
    is IntegralLiteral -> parseIntegerConstant(tokenBuffer)
    is Symbol -> {
        if (token.lexeme == "(") {
            parseBracketedExpression(tokenBuffer)
        } else {
            throw ParseException("Unexpected symbol token ${token.lexeme}")
        }
    }
    is Identifier -> parseIdentifierExpression(tokenBuffer)

    else -> TODO()
}

fun parseIdentifierExpression(tokenBuffer: TokenBuffer): CGIdentifierExpression {
    val start = tokenBuffer.index
    val identifier = tokenBuffer.consume()
    return CGIdentifierExpression(start, tokenBuffer.index, identifier)
}

fun parseIntegerConstant(tokenBuffer: TokenBuffer): CGIntegerConstant {
    val start = tokenBuffer.index
    val token = tokenBuffer.nextToken()
    if (token !is IntegralLiteral) {
        throw ParseException("Could not parse integer constant, expected integer literal, got $token.")
    }
    tokenBuffer.consume()
    return CGIntegerConstant(start, tokenBuffer.index, token.value)
}

fun parseBracketedExpression(tokenBuffer: TokenBuffer): CGExpression {
    val start = tokenBuffer.index
    tokenBuffer.consume("(")
    val expr = parseExpression(tokenBuffer)
    tokenBuffer.consume(")")
    expr.start = start
    expr.end = tokenBuffer.index
    return expr
}

private fun addParserToStack(previousTopParser: (TokenBuffer) -> CGExpression, operators: List<String>): (TokenBuffer) -> CGExpression {
    return fun(tokenBuffer: TokenBuffer): CGExpression {
        var expr = previousTopParser.invoke(tokenBuffer)
        var nextLexeme = tokenBuffer.nextLexeme()
        while (operators.contains(nextLexeme)) {
            tokenBuffer.consume()
            val rhs = previousTopParser.invoke(tokenBuffer)
            expr = CGBinaryOperationExpression(expr.start, tokenBuffer.index, expr, nextLexeme, rhs)
            nextLexeme = tokenBuffer.nextLexeme()
        }
        return expr
    }
}

