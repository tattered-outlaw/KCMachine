package com.notedgeek.kcmachine.build.compile.backend.analyze

import com.notedgeek.kcmachine.build.compile.backend.model.ArgumentReference

class AnalysisContext {

    var argumentIndex = 0
    val arguments = HashMap<String, ArgumentReference>()

    fun addArgument(name: String) {
        arguments[name] = ArgumentReference(argumentIndex++)
    }

    fun getReference(name: String) = arguments[name] ?: throw AnalyzeException("name $name not found")
}