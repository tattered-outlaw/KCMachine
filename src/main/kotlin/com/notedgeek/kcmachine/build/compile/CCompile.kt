package com.notedgeek.kcmachine.build.compile

import com.notedgeek.kcmachine.build.compile.backend.analyze.analyzeC
import com.notedgeek.kcmachine.build.compile.backend.model.CompilationUnit
import com.notedgeek.kcmachine.build.compile.frontend.parse.parseC

fun compile(source: String): CompilationUnit {
    return analyzeC(parseC(source))
}