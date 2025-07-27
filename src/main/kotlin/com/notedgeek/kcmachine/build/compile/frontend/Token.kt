package com.notedgeek.kcmachine.build.compile.frontend

sealed interface Token {
    val lexeme: String
}

data class Identifier(override val lexeme: String) : Token

data class Keyword(override val lexeme: String) : Token

data class Symbol(override val lexeme: String) : Token

data class Whitespace(override val lexeme: String) : Token

sealed interface NumericLiteral : Token

sealed interface IntegralLiteral : NumericLiteral {
    val value: Long
}

data class DecimalIntegralLiteral(override val lexeme: String) : IntegralLiteral {
    override val value = lexeme.toLong()
}
