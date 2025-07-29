package com.notedgeek.kcmachine

import kotlin.test.Test

class ArithmeticTest {

    @Test
    fun `test run skeleton`() {
        runCodeForResult(
            """
            int main() { 
                return 10; 
            }
            """.trimIndent(), 10
        )
    }

    @Test
    fun `test binary addition`() {
        runCodeForResult(
            """
            int main() { 
                return 1+2; 
            }
            """.trimIndent(), 3
        )
    }

    @Test
    fun `test binary subtraction positive`() {
        runCodeForResult(
            """
            int main() { 
                return 5 - 3; 
            }
            """.trimIndent(), 2
        )
    }

    @Test
    fun `test binary subtraction negative`() {
        runCodeForResult(
            """
            int main() { 
                return 3 - 5; 
            }
            """.trimIndent(), -2
        )
    }

    @Test
    fun `test associativity`() {
        runCodeForResult(
            """
            int main() { 
                return 5 - 3 - 1; 
            }
            """.trimIndent(), 1
        )
    }

    @Test
    fun `test precedence`() {
        runCodeForResult(
            """
            int main() { 
                return 1 + 2 * 3 + 4 / 2; 
            }
            """.trimIndent(), 9
        )
    }

    @Test
    fun `test bracketing`() {
        runCodeForResult(
            """
            int main() { 
                return (1 + 2) * (3 + 4); 
            }
            """.trimIndent(), 21
        )
    }

    @Test
    fun `test nested bracketing`() {
        runCodeForResult(
            """
            int main() { 
                return (((1 + 2)) * (3 + 4)); 
            }
            """.trimIndent(), 21
        )
    }

}