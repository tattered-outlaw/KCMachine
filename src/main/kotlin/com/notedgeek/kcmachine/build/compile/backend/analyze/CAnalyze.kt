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

    val lhsDeclarator = cgFunctionDefinition.declarator.declarator

    if (lhsDeclarator !is CGIdentifierDeclarator) {
        TODO()
    }

    val identifier = lhsDeclarator.identifier

    val parameters = ArrayList<NameAndType>()

    if (cgFunctionDefinition.declarator is CGParameterFunctionDeclarator) {
        parameters.addAll(cgFunctionDefinition.declarator.parameterTypeList.map {
            val parameterDeclarator = it.declarator
            if(parameterDeclarator !is CGConcreteDeclarator) {
                throw AnalyzeException("Unexpected abstract declarator for parameter: $it")
            }
            nameAndTypeFromDeclarator(
                parameterDeclarator,
                typeFromDeclarationSpecifiers(it.declarationSpecifiers)
            )
        })
    }

    val compoundStatement = analyzeCompoundStatement(cgFunctionDefinition.compoundStatement)

    return FunctionDefinition(
        cgFunctionDefinition.start,
        cgFunctionDefinition.end,
        identifier.value,
        QualifiedType(IntType),
        parameters,
        compoundStatement
    )

}

fun typeFromDeclarationSpecifiers(declarationSpecifiers: List<CGDeclarationSpecifier>): Type {
    return IntType
}


