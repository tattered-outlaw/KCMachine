package com.notedgeek.kcmachine

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class RunCodeKtTest {

    @Test
    fun testRunSkeleton() {
        val result = runCode("int main() { return 10; }", 20)
        assertEquals(10, result)
    }
}