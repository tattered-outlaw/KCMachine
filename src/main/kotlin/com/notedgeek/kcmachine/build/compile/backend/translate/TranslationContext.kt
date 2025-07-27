package com.notedgeek.kcmachine.build.compile.backend.translate

class TranslationContext() {

    val code = ArrayList<String>()

    fun emit(line: String) {
        code.add(line)
    }
}