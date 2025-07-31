package com.notedgeek.kcmachine.build.compile.backend.analyze

import com.notedgeek.kcmachine.build.compile.backend.model.*
import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

fun typeFromDeclarator(declarator: CGDeclarator, type: Type) = when (declarator) {
    is CGConcreteDeclarator -> nameAndTypeFromDeclarator(declarator, type).type
    is CGAbstractDeclarator -> typeFromAbstractDeclarator(declarator, type)
}


fun nameAndTypeFromDeclarator(declarator: CGConcreteDeclarator, type: Type): NameAndType = when (declarator) {
    is CGIdentifierDeclarator -> NameAndType(declarator.identifier.value, type)
    is CGPointerDeclarator -> {
        val type = PointerType(type)
        nameAndTypeFromDeclarator(declarator.declarator, type)
    }

    is CGFunctionDeclarator -> {
        val parameterTypes = when (declarator) {
            is CGParameterFunctionDeclarator -> declarator.parameterTypeList.map {
                typeFromDeclarator(
                    it.declarator,
                    typeFromDeclarationSpecifiers(it.declarationSpecifiers)
                )
            }.toList()
            else -> emptyList()
        }
        val type = FunctionType(type, parameterTypes)
        nameAndTypeFromDeclarator(declarator.declarator, type)
    }

    is CGArrayDeclarator -> {
        val type = ArrayType(type)
        nameAndTypeFromDeclarator(declarator.declarator, type)
    }

    else -> throw AnalyzeException("Unexpected declarator: $declarator")
}

fun typeFromAbstractDeclarator(declarator: CGAbstractDeclarator, type: Type): Type = when (declarator) {
    is CGAbstractDeclaratorPlaceholder -> type
    is CGAbstractPointerDeclarator -> {
        val type = PointerType(type)
        typeFromAbstractDeclarator(declarator.declarator, type)
    }

    is CGAbstractFunctionDeclarator -> {
        val parameterTypes = when (declarator) {
            is CGAbstractParameterFunctionDeclarator -> declarator.parameterTypeList.map {
                typeFromDeclarator(
                    it.declarator,
                    typeFromDeclarationSpecifiers(it.declarationSpecifiers)
                )
            }.toList()
            else -> emptyList()
        }
        val type = FunctionType(type, parameterTypes)
        typeFromAbstractDeclarator(declarator.declarator, type)
    }

    is CGAbstractArrayDeclarator -> {
        val type = ArrayType(type)
        typeFromAbstractDeclarator(declarator.declarator, type)
    }

    else -> throw AnalyzeException("Unexpected declarator: $declarator")
}
