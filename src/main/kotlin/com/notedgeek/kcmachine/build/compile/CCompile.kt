package com.notedgeek.kcmachine.build.compile

import com.notedgeek.kcmachine.build.compile.backend.analyze.analyzeC
import com.notedgeek.kcmachine.build.compile.backend.translate.*
import com.notedgeek.kcmachine.build.compile.frontend.parse.parseC

fun compile(source: String): List<String> {
    val translationContext = TranslationContext()
    with(translationContext) {
        emit(LSP("\$STACK_TOP"))
        emit(LBP("0"))
        emit(PUSH_CONST("0"))
        emit(CALL("main"))
        emit(HALT)
    }
    translateC(analyzeC(parseC(source)), translationContext)
    return translationContext.code
}