package com.notedgeek.kcmachine.build.compile.frontend.parse

import com.notedgeek.kcmachine.build.compile.CGCompilationUnit
import kotlin.test.Test
import kotlin.test.assertIs

class CParseKtTest {

    @Test
    fun testParseSkeleton() {
        val result = parseC("int main() { return 10; }")
        assertIs<CGCompilationUnit>(result)
    }
}