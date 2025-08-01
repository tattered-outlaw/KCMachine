package com.notedgeek.kcmachine

import kotlin.test.Test

class BasicLanguageTests {

    @Test
    fun `test run skeleton`() = runCodeForResult(
        """
            int main() { 
                return 10; 
            }
            """, 10
    )

    @Test
    fun `test binary addition`() = runCodeForResult(
        """
            int main() { 
                return 1+2; 
            }
            """, 3
    )

    @Test
    fun `test binary subtraction positive`() = runCodeForResult(
        """
            int main() { 
                return 5 - 3; 
            }
            """, 2
    )

    @Test
    fun `test binary subtraction negative`() = runCodeForResult(
        """
            int main() { 
                return 3 - 5; 
            }
            """, -2
    )

    @Test
    fun `test associativity`() = runCodeForResult(
        """
            int main() { 
                return 5 - 3 - 1; 
            }
            """, 1
    )

    @Test
    fun `test precedence`() = runCodeForResult(
        """
            int main() { 
                return 1 + 2 * 3 + 4 / 2; 
            }
            """, 9
    )

    @Test
    fun `test bracketing`() = runCodeForResult(
        """
            int main() { 
                return (1 + 2) * (3 + 4); 
            }
            """, 21
    )

    @Test
    fun `test nested bracketing`() = runCodeForResult(
        """
            int main() { 
                return (((1 + 2)) * (3 + 4)); 
            }
            """, 21
    )

    @Test
    fun `test function call no args`() = runCodeForResult(
        """
            int main() { 
                return getNum(); 
            }
            
            int getNum() {
                return 5;
            }
            """, 5
    )

    @Test
    fun `test function call no args two levels`() = runCodeForResult(
        """
            int main() { 
                return getNum(); 
            }
            
            int getNum() {
                return getNumNested();
            }

            int getNumNested() {
                return 99;
            }
            """, 99
    )
}