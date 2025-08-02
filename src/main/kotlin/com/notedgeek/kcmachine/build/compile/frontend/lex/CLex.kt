package com.notedgeek.kcmachine.build.compile.frontend.lex

import com.notedgeek.kcmachine.build.compile.frontend.*

private val patternMap = mapOf(
    "[_a-zA-Z][_a-zA-Z0-9]*" to ::Identifier,
    "\\s+" to ::Whitespace,
    "[0-9]+" to ::DecimalIntegralLiteral
)

private val symbolList = listOf("\\(", "\\)", "\\{", "\\}", "\\[", "\\]", "\\==", "\\+", "-", "\\*", "/", ";", ",", "\\|\\|")

private val tokenSpecs = sequence {
    yieldAll(patternMap.entries.map(::TokenSpec))
    yieldAll(symbolList.map { TokenSpec(it, ::Symbol) })
}.toList()

private val KEYWORDS = setOf("int", "return", "if", "else")

fun lexC(source: String): List<Token> = sequence {
    for (token in lex(tokenSpecs, source)) {
        if (token is Identifier && KEYWORDS.contains(token.lexeme)) {
            yield(Keyword(token.lexeme))
        } else if (token !is Whitespace) {
            yield(token)
        }
    }
}.toList()
