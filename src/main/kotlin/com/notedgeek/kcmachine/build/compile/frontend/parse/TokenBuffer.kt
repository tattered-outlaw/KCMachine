package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.frontend.Token

class TokenBuffer(private val tokens: List<Token>) {

    var index = 0

    fun nextToken(): Token {
        ensureHasNextToken()
        return tokens[index]
    }

    fun nextLexeme(lookahead: Int = 0): String {
        ensureHasNextToken(lookahead)
        return tokens[index + lookahead].lexeme
    }

    fun consume(): String {
        val result= nextLexeme()
        index++
        return result
    }

    fun consume(expectedLexeme: String) {
        ensureHasNextToken()
        val lexeme = tokens[index++].lexeme
        if (lexeme != expectedLexeme) {
            throw ParseException("Expected '$expectedLexeme', but got '$lexeme'.")
        }
    }

    fun hasMoreTokens(lookahead: Int = 0) = index + lookahead < tokens.size

    private fun ensureHasNextToken(lookahead: Int = 0) {
        if (!hasMoreTokens(lookahead)) {
            throw ParseException("Tokens exhausted.")
        }
    }
}