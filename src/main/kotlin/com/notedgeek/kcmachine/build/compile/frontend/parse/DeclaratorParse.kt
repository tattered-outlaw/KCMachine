package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.frontend.Identifier
import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

fun parseDeclaratorOfEitherType(tokenBuffer: TokenBuffer): CGDeclarator {
    val start = tokenBuffer.index
    try {
        return parseDeclarator(tokenBuffer)
    } catch (e: ConcreteDeclaratorFailure) {
        tokenBuffer.index = start
        return parseAbstractDeclarator(tokenBuffer)
    }
}

fun parseDeclarator(tokenBuffer: TokenBuffer): CGConcreteDeclarator {
    val start = tokenBuffer.index
    return if (tokenBuffer.nextLexeme() == "*") {
        val pointer = parsePointer(tokenBuffer)
        val declarator = parseDeclarator(tokenBuffer)
        CGPointerDeclarator(start, tokenBuffer.index, pointer, declarator)
    } else {
        parseDirectDeclarator(tokenBuffer)
    }
}

fun parseDirectDeclarator(tokenBuffer: TokenBuffer): CGConcreteDeclarator {
    val start = tokenBuffer.index
    var declarator: CGConcreteDeclarator
    if (tokenBuffer.nextToken() is Identifier) {
        declarator = CGIdentifierDeclarator(start, tokenBuffer.index, parseIdentifierExpression(tokenBuffer))
    } else if (tokenBuffer.nextLexeme() == "(") {
        tokenBuffer.consume("(")
        declarator = parseDeclarator(tokenBuffer)
        tokenBuffer.consume(")")
    } else {
        throw ConcreteDeclaratorFailure()
    }
    while (true) {
        val lexeme = tokenBuffer.nextLexeme()
        declarator = if (lexeme == "(") {
            parseFunctionDeclarator(tokenBuffer, declarator)
        } else if (lexeme == "[") {
            parseArrayDeclarator(tokenBuffer, declarator)
        } else {
            break
        }
    }
    return declarator
}

fun parseFunctionDeclarator(tokenBuffer: TokenBuffer, declarator: CGConcreteDeclarator): CGFunctionDeclarator {
    val start = tokenBuffer.index
    tokenBuffer.consume("(")
    tokenBuffer.consume(")")
    return CGEmptyFunctionDeclarator(start, tokenBuffer.index, declarator)
}

fun parseArrayDeclarator(tokenBuffer: TokenBuffer, declarator: CGConcreteDeclarator): CGArrayDeclarator {
    val start = tokenBuffer.index
    tokenBuffer.consume("[")
    tokenBuffer.consume("]")
    return CGArrayDeclarator(start, tokenBuffer.index, declarator)
}

fun parseAbstractDeclarator(tokenBuffer: TokenBuffer): CGAbstractDeclarator {
    val start = tokenBuffer.index
    return if (tokenBuffer.nextLexeme() == "*") {
        val pointer = parsePointer(tokenBuffer)
        val declarator = parseAbstractDeclarator(tokenBuffer)
        CGAbstractPointerDeclarator(start, tokenBuffer.index, pointer, declarator)
    } else {
        parseDirectAbstractDeclarator(tokenBuffer)
    }
}

fun parseDirectAbstractDeclarator(tokenBuffer: TokenBuffer): CGAbstractDeclarator {
    val start = tokenBuffer.index
    var declarator: CGAbstractDeclarator
    if (tokenBuffer.nextLexeme() == "(" && tokenBuffer.nextLexeme(1) != ")") {
        tokenBuffer.consume("(")
        declarator = parseAbstractDeclarator(tokenBuffer)
        tokenBuffer.consume(")")
    } else {
        declarator = CGAbstractDeclaratorPlaceholder(start, tokenBuffer.index)
    }
    while (true) {
        val lexeme = tokenBuffer.nextLexeme()
        declarator = if (lexeme == "(") {
            parseAbstractFunctionDeclarator(tokenBuffer, declarator)
        } else if (lexeme == "[") {
            parseAbstractArrayDeclarator(tokenBuffer, declarator)
        } else {
            break
        }
    }
    return declarator
}

fun parseAbstractFunctionDeclarator(tokenBuffer: TokenBuffer, declarator: CGAbstractDeclarator): CGAbstractFunctionDeclarator {
    val start = tokenBuffer.index
    tokenBuffer.consume("(")
    tokenBuffer.consume(")")
    return CGAbstractEmptyFunctionDeclarator(start, tokenBuffer.index, declarator)
}

fun parseAbstractArrayDeclarator(tokenBuffer: TokenBuffer, declarator: CGAbstractDeclarator): CGAbstractArrayDeclarator {
    val start = tokenBuffer.index
    tokenBuffer.consume("[")
    tokenBuffer.consume("]")
    return CGAbstractArrayDeclarator(start, tokenBuffer.index, declarator)
}


fun parsePointer(tokenBuffer: TokenBuffer): CGPointer {
    val start = tokenBuffer.index
    tokenBuffer.consume("*")
    return CGPointer(start, tokenBuffer.index, emptyList())
}

private class ConcreteDeclaratorFailure : Exception()
