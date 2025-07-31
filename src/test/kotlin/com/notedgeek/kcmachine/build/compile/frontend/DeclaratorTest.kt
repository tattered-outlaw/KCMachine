package com.notedgeek.kcmachine.build.compile.frontend

import com.notedgeek.kcmachine.build.compile.backend.analyze.nameAndTypeFromDeclarator
import com.notedgeek.kcmachine.build.compile.backend.analyze.typeFromAbstractDeclarator
import com.notedgeek.kcmachine.build.compile.backend.model.*
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGAbstractDeclarator
import com.notedgeek.kcmachine.build.compile.frontend.grammar.CGConcreteDeclarator
import com.notedgeek.kcmachine.build.compile.frontend.parse.parseDeclarator
import com.notedgeek.kcmachine.build.compile.frontend.lex.lexC
import com.notedgeek.kcmachine.build.compile.frontend.parse.TokenBuffer
import org.junit.jupiter.api.Test

class DeclaratorTest {
    @Test
    fun testDeclarator() {
        listOf(
            "a", "",
            "*a", "*",
            "a()", "()",
            "a[]()", "[]()",
            "**a[]()", "**[]()",
            "(**a)[]()", "(**)[]()",
        ).forEach(::process)
    }

    private fun process(source: String) {
        val declarator = parseDeclarator(TokenBuffer(lexC("$source;")))
        val name: String
        var type: Type? = null
        var nameAndType: NameAndType? = null
        when (declarator) {
            is CGConcreteDeclarator -> nameAndType = nameAndTypeFromDeclarator(declarator, IntType)
            is CGAbstractDeclarator -> type = typeFromAbstractDeclarator(declarator, IntType)
        }

        type = nameAndType?.type ?: type
        name = nameAndType?.name ?: ""

        var string = ""
        while (type is WrapperType) {
            string += when (type) {
                is PointerType -> "p"
                is FunctionType -> "f"
                is ArrayType -> "a"
            }
            type = type.wrappedType
        }
        println("$source ${name.ifEmpty { "{abstract}" }} : $string")
    }


}