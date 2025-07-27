package com.notedgeek.kcmachine.build.compile

import com.notedgeek.kcmachine.build.compile.backend.analyze.analyzeC
import com.notedgeek.kcmachine.build.compile.backend.translate.Inst
import com.notedgeek.kcmachine.build.compile.backend.translate.TranslationContext
import com.notedgeek.kcmachine.build.compile.backend.translate.translateC
import com.notedgeek.kcmachine.build.compile.frontend.parse.parseC

fun compile(source: String): List<String> {
    val translationContext = TranslationContext()
    with(translationContext) {
        emit(Inst.LSP("\$MEM_TOP"))
        emit(Inst.LBP("0"))
        emit(Inst.PUSH_CONST("0"))
        emit(Inst.CALL("main"))
        emit(Inst.HALT())
    }
    translateC(analyzeC(parseC(source)), translationContext)
    return translationContext.code
}