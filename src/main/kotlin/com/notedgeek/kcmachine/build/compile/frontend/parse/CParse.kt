package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.CGCompilationUnit
import com.notedgeek.kcmachine.build.compile.CGExternalDeclaration
import com.notedgeek.kcmachine.build.compile.frontend.lex.lexC

fun parse(source: String): CGCompilationUnit {
    return parseCompilationUnit(TokenBuffer(lexC(source)))
}

fun parseCompilationUnit(tokenBuffer: TokenBuffer): CGCompilationUnit {
    val externalDeclarations = ArrayList<CGExternalDeclaration>()
    val start = tokenBuffer.index
    while (tokenBuffer.hasMoreTokens()) {
        externalDeclarations.add(parseExternalDeclaration(tokenBuffer))
    }
    return CGCompilationUnit(start, tokenBuffer.index, externalDeclarations)
}
