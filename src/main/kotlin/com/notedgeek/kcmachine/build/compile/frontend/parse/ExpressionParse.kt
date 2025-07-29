package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.CGBinaryOperationExpression
import com.notedgeek.kcmachine.build.compile.CGExpression
import com.notedgeek.kcmachine.build.compile.CGIntegerConstant
import com.notedgeek.kcmachine.build.compile.frontend.IntegralLiteral
import com.notedgeek.kcmachine.build.compile.frontend.Symbol

private val binaryExpressionParserStack = listOf(
    listOf("*", "/"),
    listOf("+", "-")
).fold(::parsePrimaryExpression, ::addParserToStack)

fun parseExpression(tokenBuffer: TokenBuffer): CGExpression {
    return binaryExpressionParserStack.invoke(tokenBuffer)
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

    else -> TODO()
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
    val expression = parseExpression(tokenBuffer)
    tokenBuffer.consume(")")
    expression.start = start
    expression.end = tokenBuffer.index
    return expression
}

private fun addParserToStack(previousTopParser: (TokenBuffer) -> CGExpression, operators: List<String>): (TokenBuffer) -> CGExpression {
    return fun(tokenBuffer: TokenBuffer): CGExpression {
        var expression = previousTopParser.invoke(tokenBuffer)
        var nextLexeme = tokenBuffer.nextLexeme()
        while (operators.contains(nextLexeme)) {
            tokenBuffer.consume()
            val rhs = previousTopParser.invoke(tokenBuffer)
            expression = CGBinaryOperationExpression(expression.start, tokenBuffer.index, expression, nextLexeme, rhs)
            nextLexeme = tokenBuffer.nextLexeme()
        }
        return expression
    }
}

