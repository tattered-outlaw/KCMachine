package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.frontend.Token

class TokenBuffer(private val tokens: List<Token>) {

    var index = 0
        private set

    fun nextToken(): Token {
        ensureHasNextToken()
        return tokens[index]
    }

    fun nextLexeme(): String {
        ensureHasNextToken()
        return tokens[index].lexeme
    }

    fun consume() {
        ensureHasNextToken()
        index++
    }

    fun consume(expectedLexeme: String) {
        ensureHasNextToken()
        val lexeme = tokens[index++].lexeme
        if (lexeme != expectedLexeme) {
            throw ParseException("Expected '$expectedLexeme', but got '$lexeme'.")
        }
    }

    fun hasMoreTokens() = index < tokens.size

    private fun ensureHasNextToken() {
        if (!hasMoreTokens()) {
            throw ParseException("Tokens exhausted.")
        }
    }
}