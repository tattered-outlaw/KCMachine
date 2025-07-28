package com.notedgeek.kcmachine.build

import kotlin.test.Test
import kotlin.test.assertEquals

class BuildKtTest {

    @Test
    fun testBuildSkeleton() {
        val result = build("int main() { return 10; }", mapOf("\$STACK_TOP" to 2048))
        assertEquals(12, result.size)
    }
}