package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.CGExpression
import com.notedgeek.kcmachine.build.compile.CGIntegerConstant
import com.notedgeek.kcmachine.build.compile.frontend.IntegralLiteral

fun parseExpression(tokenBuffer: TokenBuffer) : CGExpression {
    return parseIntegerConstant(tokenBuffer)
}

fun parseIntegerConstant(tokenBuffer: TokenBuffer) : CGIntegerConstant {
    val start = tokenBuffer.index
    val token = tokenBuffer.nextToken()
    if(token !is IntegralLiteral) {
        throw ParseException("Could not parse integer constant, expected integer literal, got $token.")
    }
    tokenBuffer.consume()
    return CGIntegerConstant(start, tokenBuffer.index, token.value)
}