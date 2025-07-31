package com.notedgeek.kcmachine.build.compile.backend.analyze

import com.notedgeek.kcmachine.build.compile.backend.model.*
import com.notedgeek.kcmachine.build.compile.frontend.grammar.*

fun nameAndTypeFromDeclarator(declarator: CGConcreteDeclarator): NameAndType {
    fun recurse(declarator: CGConcreteDeclarator, stack: MutableList<(Type) -> WrapperType>): String {
        when (declarator) {
            is CGIdentifierDeclarator -> return declarator.identifier.value
            is CGPointerDeclarator -> {
                stack.add(::PointerType)
                return recurse(declarator.declarator, stack)
            }

            is CGEmptyFunctionDeclarator -> {
                stack.add(::FunctionType)
                return recurse(declarator.declarator, stack)
            }

            is CGArrayDeclarator -> {
                stack.add(::ArrayType)
                return recurse(declarator.declarator, stack)
            }

            else -> TODO()
        }
    }

    val stack = ArrayList<(Type) -> WrapperType>()
    val name = recurse(declarator, stack)
    val type = stack.fold(IntType) { acc: Type, next -> next.invoke(acc) }
    return NameAndType(name, type)

}

fun typeFromDeclarator(declarator: CGAbstractDeclarator): Type {
    fun recurse(declarator: CGAbstractDeclarator, stack: MutableList<(Type) -> WrapperType>) {
        when (declarator) {
            is CGAbstractDeclaratorPlaceholder -> return
            is CGAbstractPointerDeclarator -> {
                stack.add(::PointerType)
                return recurse(declarator.declarator, stack)
            }

            is CGAbstractEmptyFunctionDeclarator -> {
                stack.add(::FunctionType)
                return recurse(declarator.declarator, stack)
            }

            is CGAbstractArrayDeclarator -> {
                stack.add(::ArrayType)
                return recurse(declarator.declarator, stack)
            }

            else -> TODO()
        }
    }

    val stack = ArrayList<(Type) -> WrapperType>()
    recurse(declarator, stack)
    return stack.fold(IntType) { acc: Type, next -> next.invoke(acc) }
}
