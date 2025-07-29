package com.notedgeek.kcmachine

import org.junit.jupiter.api.Assertions.assertEquals

fun runCodeForResult(code: String, expectedResult: Long) {
    val result = runCode(code, 1024 * 1024)
    assertEquals(expectedResult, result)
}