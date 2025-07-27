package com.notedgeek.kcmachine.build.compile.frontend.lex

import com.notedgeek.kcmachine.build.compile.frontend.Token
import java.nio.CharBuffer
import java.util.regex.Pattern

class TokenSpec(patternString: String, val tokenConstructor: (String) -> Token) {
    val pattern: Pattern = Pattern.compile("^$patternString")
}

class Lexer(private val tokenSpecs: List<TokenSpec>) {

    fun lex(source: String): List<Token> {
        val tokens: MutableList<Token> = ArrayList()

        // Using CharBuffer avoids re-creating a new String each time we advance through input having
        // consumed a token (we can't just use matcher.find(index) and change the value of index,
        // since ^ anchor pattern won't match properly).
        val chars = CharBuffer.wrap(source)
        var index = 0
        while (index < chars.length) {
            val remainingInput = chars.subSequence(index, chars.length)
            var found = false
            for (tokenSpec in tokenSpecs) {
                val matcher = tokenSpec.pattern.matcher(remainingInput)
                found = matcher.find()
                if (found) {
                    val lexeme = matcher.group()
                    tokens.add(tokenSpec.tokenConstructor.invoke(lexeme))
                    index += lexeme.length
                    break
                }
            }
            if (!found) {
                throw LexException("No match found at '${source.substring(index)}'.")
            }
        }
        return tokens
    }

}
