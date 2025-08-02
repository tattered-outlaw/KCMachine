package com.notedgeek.kcmachine

import kotlin.test.Test

class BasicLanguageTests {

    @Test
    fun `run skeleton`() = runCodeForResult(
        """
            int main() { 
                return 10; 
            }
            """, 10
    )

    @Test
    fun `binary addition`() = runCodeForResult(
        """
            int main() { 
                return 1+2; 
            }
            """, 3
    )

    @Test
    fun `binary subtraction positive`() = runCodeForResult(
        """
            int main() { 
                return 5 - 3; 
            }
            """, 2
    )

    @Test
    fun `binary subtraction negative`() = runCodeForResult(
        """
            int main() { 
                return 3 - 5; 
            }
            """, -2
    )

    @Test
    fun associativity() = runCodeForResult(
        """
            int main() { 
                return 5 - 3 - 1; 
            }
            """, 1
    )

    @Test
    fun precedence() = runCodeForResult(
        """
            int main() { 
                return 1 + 2 * 3 + 4 / 2; 
            }
            """, 9
    )

    @Test
    fun bracketing() = runCodeForResult(
        """
            int main() { 
                return (1 + 2) * (3 + 4); 
            }
            """, 21
    )

    @Test
    fun `nested bracketing`() = runCodeForResult(
        """
            int main() { 
                return (((1 + 2)) * (3 + 4)); 
            }
            """, 21
    )

    @Test
    fun `function call no args`() = runCodeForResult(
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
    fun `function call no args two levels`() = runCodeForResult(
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

    @Test
    fun `function call with argument`() = runCodeForResult(
        """
            int main() {
                return triple(3);
            }
            
            int triple(int n) {
                return n * 3;
            }
        """, 9
    )

    @Test
    fun `function call with two arguments`() = runCodeForResult(
        """
            int main() {
                return sub(100, 1);
            }
            
            int sub(int a, int b) {
                return a - b;
            }
        """, 99
    )

    @Test
    fun `function call with two arguments with caller clearing stack`() = runCodeForResult(
        """
            int main() {
                return sub(100, 1) + sub(8, 3);
            }
            
            int sub(int a, int b) {
                return a - b;
            }
        """, 104
    )

    @Test
    fun `if statement and recursion`() = runCodeForResult(
        """
            int main() {
                return factorial(6);
            }
            
            int factorial(int n) {
                if(n == 1) {
                    return 1;
                } else {
                    return n * factorial(n - 1); 
                }
            }
        """, 720
    )

    @Test
    fun `if statement without else`() = runCodeForResult(
        """
            int main() {
                if(1 == 2) {
                    return 2;
                }
                return 1;
            }
        """, 1
    )

    @Test
    fun fibonacci() = runCodeForResult(
        """
            int main() {
                return fib(9);
            }
            
            int fib(int n) {
                if(n == 1 || n == 2) {
                    return 1;
                } else {
                    return fib(n - 1) + fib(n - 2);
                }
            }
        """, 34
    )

    @Test
    fun gcd() = runCodeForResult(
        """
            int main() {
                return gcd(1071, 462);
            }
            
            int gcd(int a, int b) {
                if (b == 0) return a;
                return gcd(b, a % b);
            }        
        """, 21
    )
}