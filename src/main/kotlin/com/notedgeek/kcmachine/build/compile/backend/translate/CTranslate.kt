package com.notedgeek.kcmachine.build.compile.backend.translate

import com.notedgeek.kcmachine.build.compile.backend.model.*

fun translateC(compilationUnit: CompilationUnit, translationContext: TranslationContext) {
    for (compilationItem in compilationUnit.items) {
        if (compilationItem is FunctionDefinition) {
            translateFunctionDefinition(compilationItem, translationContext)
        } else {
            TODO()
        }
    }
}

private fun translateFunctionDefinition(functionDefinition: FunctionDefinition, translationContext: TranslationContext) {
    with(translationContext) {
        emit("${functionDefinition.name}:")
        emit(PUSH_BP())
        emit(SP_TO_BP())
    }
    functionDefinition.statement.statements.forEach { translateStatement(it, translationContext) }
}
