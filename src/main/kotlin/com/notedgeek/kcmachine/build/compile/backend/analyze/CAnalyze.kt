package com.notedgeek.kcmachine.build.compile.backend.analyze

import com.notedgeek.kcmachine.build.compile.backend.model.*
import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

fun analyzeC(cgCompilationUnit: CGCompilationUnit): CompilationUnit {
    val compilationItems = sequence {
        for (externalDeclaration in cgCompilationUnit.externalDeclarations) {
            yield(
                when (externalDeclaration) {
                    is CGFunctionDefinition -> analyzeFunctionDefinition(externalDeclaration)
                    else -> TODO()
                }
            )
        }
    }.toList()
    return CompilationUnit(cgCompilationUnit.start, cgCompilationUnit.end, compilationItems)
}

private fun analyzeFunctionDefinition(cgFunctionDefinition: CGFunctionDefinition): FunctionDefinition {
    if (cgFunctionDefinition.declarationSpecifiers.isEmpty()) {
        throw AnalyzeException("Declaration specifiers list is empty.")
    }

    val typeSpecifier = cgFunctionDefinition.declarationSpecifiers.first()

    if (typeSpecifier !is CGTypeSpecifier) {
        TODO()
    }

    if (typeSpecifier != CGTypeSpecifier.INT) {
        TODO()
    }

    if (cgFunctionDefinition.declarator !is CGEmptyFunctionDeclarator) {
        TODO()
    }

    if (cgFunctionDefinition.declarator.declarator !is CGIdentifierDeclarator) {
        TODO()
    }

    val identifier = cgFunctionDefinition.declarator.declarator.identifier

    val compoundStatement = analyzeCompoundStatement(cgFunctionDefinition.compoundStatement)

    return FunctionDefinition(
        cgFunctionDefinition.start,
        cgFunctionDefinition.end,
        identifier.value,
        QualifiedType(IntType),
        emptyList(),
        compoundStatement
    )
}



