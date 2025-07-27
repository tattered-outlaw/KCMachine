package com.notedgeek.kcmachine.build.compile

import com.notedgeek.kcmachine.build.compile.backend.model.CompilationUnit
import kotlin.test.Test
import kotlin.test.assertIs

class CCompileKtTest {

    @Test
    fun testCompileSkeleton() {
        var result = compile("int main() { return 10; }")
        assertIs<CompilationUnit>(result)
    }

}