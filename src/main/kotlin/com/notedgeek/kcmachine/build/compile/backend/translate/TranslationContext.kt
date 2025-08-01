package com.notedgeek.kcmachine.build.compile.backend.translate

class TranslationContext(name: String = "main") {

    var labelIndex = 0
    val code = ArrayList<String>()

    fun labelHead(string: String) = "${string}_${labelIndex++}"

    fun emit(line: String) {
        code.add(line)
    }

    fun label(string: String) {
        code.add("$string:")
    }

}