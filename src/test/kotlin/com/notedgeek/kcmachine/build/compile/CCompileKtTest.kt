package com.notedgeek.kcmachine.build.compile

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs

class CCompileKtTest {

    @Test
    fun testCompileSkeleton() {
        val result = compile("int main() { return 10; }")
        assertIs<List<String>>(result)
        result.forEach(::println)
        assertFalse(result.isEmpty())
    }

}