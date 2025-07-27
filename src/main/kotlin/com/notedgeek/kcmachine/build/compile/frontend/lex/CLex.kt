package com.notedgeek.kcmachine.build.compile.frontend.lex

import com.notedgeek.kcmachine.build.compile.frontend.*

private const val IDENTIFIER_PATTERN = "[_a-zA-Z][_a-zA-Z0-9]*"
private const val WHITESPACE_PATTERN = "\\s+"
private const val DECIMAL_INTEGRAL_PATTERN = "[0-9]+"

private val SYMBOL_PATTERNS = listOf("\\(", "\\)", "\\{", "\\}", ";")
private val KEYWORDS = setOf("int", "return")

private val tokenSpecs = run {
    val result = mutableListOf(
        TokenSpec(IDENTIFIER_PATTERN, ::Identifier), TokenSpec(WHITESPACE_PATTERN, ::Whitespace),
        TokenSpec(DECIMAL_INTEGRAL_PATTERN, ::DecimalIntegralLiteral)
    )
    result.addAll(SYMBOL_PATTERNS.map { TokenSpec(it, ::Symbol) })
    result
}

private val lexer = Lexer(tokenSpecs)

fun lexC(source: String): List<Token> {
    val result = ArrayList<Token>()

    for (token in lexer.lex(source)) {
        if (token is Identifier && KEYWORDS.contains(token.lexeme)) {
            result.add(Keyword(token.lexeme))
        } else if (token !is Whitespace) {
            result.add(token)
        }
    }

    return result
}
