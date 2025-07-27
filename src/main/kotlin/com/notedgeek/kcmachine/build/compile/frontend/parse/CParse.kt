package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.CGCompilationUnit
import com.notedgeek.kcmachine.build.compile.frontend.lex.lexC

fun parseC(source: String): CGCompilationUnit {
    val tokenBuffer = TokenBuffer(lexC(source))
    val start = tokenBuffer.index

    val externalDeclarations = sequence {
        while (tokenBuffer.hasMoreTokens()) {
            yield(parseExternalDeclaration(tokenBuffer))
        }
    }.toList()

    return CGCompilationUnit(start, tokenBuffer.index, externalDeclarations)
}
