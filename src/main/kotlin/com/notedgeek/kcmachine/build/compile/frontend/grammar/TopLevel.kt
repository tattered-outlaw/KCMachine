package com.notedgeek.kcmachine.build.compile.frontend.grammar

sealed interface CGElement {
    var start: Int
    var end: Int
}

data class CGCompilationUnit(override var start: Int, override var end: Int, val externalDeclarations: List<CGExternalDeclaration>) :
    CGElement

