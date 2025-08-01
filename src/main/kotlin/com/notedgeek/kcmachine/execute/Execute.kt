package com.notedgeek.kcmachine.execute

import com.notedgeek.kcmachine.build.assemble.opcodeList
import kotlin.math.abs

private const val MASK = (1L shl 32) - 1

private const val HALT = 0
private const val PUSH_CONST = 1
private const val LBP = 2
private const val LSP = 3
private const val PUSH_BP = 4
private const val POP_BP = 5
private const val SP_TO_BP = 6
private const val BP_TO_SP = 7
private const val SAVE_A = 8
private const val CALL = 9
private const val RETURN = 10
private const val ADD = 11
private const val SUB = 12
private const val MUL = 13
private const val DIV = 14
private const val PUSH_A = 15

private const val DEC_STACK = 16

private const val PUSH_STACK_OFFSET = 17

fun execute(code: List<Long>, ramSize: Int) = ExecutionEngine(ramSize).executeCode(code)

class ExecutionEngine(ramSize: Int) {

    private val ram = Array(ramSize) { 12321L }
    private var ip: Int = 0
    private var sp: Int = 0
    private var bp: Int = 0

    private var running = false
    private val debug = true

    fun executeCode(code: List<Long>): Long {
        code.forEachIndexed { i, v -> ram[i] = v }
        running = true
        while (running) {
            executeNextInstruction()
        }
        return ram[++sp]
    }

    private fun executeNextInstruction() {
        val instruction = ram[ip++]
        val operand = (instruction and MASK).toInt()
        val opcode = (instruction shr 32).toInt()
        if(debug) {
            println("ip:${ip -1} sp:$sp stack size:${ram.size - sp - 1} bp:$bp")
            val maxStack = 150
            if(sp != 0 && abs(ram.size - sp) > maxStack) {
                throw ExecuteException("Max stack!")
            }
            if(sp != 0) {
                val stackDisplay = ArrayList<Long>()
                for (si in sp + 1..<ram.size) {
                    stackDisplay.add(ram[si])
                }
                println(stackDisplay.joinToString(" "))
            } else {
                println("zero stack")
            }
            println("${opcodeList[opcode]} $operand")
        }
        when (opcode) {
            HALT -> HALT()
            PUSH_CONST -> PUSH_CONST(operand)
            LBP -> LBP(operand)
            LSP -> LSP(operand)
            PUSH_BP -> PUSH_BP()
            POP_BP -> POP_BP()
            SP_TO_BP -> SP_TO_BP()
            BP_TO_SP -> BP_TO_SP()
            SAVE_A -> SAVE_A(operand)
            CALL -> CALL(operand)
            RETURN -> RETURN()
            ADD -> ADD()
            SUB -> SUB()
            MUL -> MUL()
            DIV -> DIV()
            PUSH_A -> PUSH_A(operand)
            DEC_STACK -> DEC_STACK(operand)
            PUSH_STACK_OFFSET -> PUSH_STACK_OFFSET(operand)
            else -> throw ExecuteException("Unrecognized opcode $opcode.")
        }
    }

    private fun HALT() {
        running = false
    }

    private fun PUSH_CONST(constant: Int) {
        ram[sp--] = constant.toLong()
    }

    private fun LBP(constant: Int) {
        bp = constant
    }

    private fun LSP(constant: Int) {
        sp = constant
    }

    private fun PUSH_BP() {
        ram[sp--] = bp.toLong()
    }

    private fun POP_BP() {
        bp = ram[++sp].toInt()
    }

    private fun SP_TO_BP() {
        bp = sp
    }

    private fun BP_TO_SP() {
        sp = bp
    }

    private fun SAVE_A(index: Int) {
        ram[bp + 3 + index] = ram[++sp]
    }

    private fun PUSH_A(index: Int) {
        ram[sp--] = ram[bp + 3 + index]
    }

    private fun CALL(address: Int) {
        ram[sp--] = ip.toLong()
        ip = address
    }

    private fun RETURN() {
        ip = ram[++sp].toInt()
    }

    private fun ADD() {
        val rhs = ram[++sp]
        val lhs = ram[++sp]
        ram[sp--] = lhs + rhs
    }

    private fun SUB() {
        val rhs = ram[++sp]
        val lhs = ram[++sp]
        ram[sp--] = lhs - rhs
    }

    private fun MUL() {
        val rhs = ram[++sp]
        val lhs = ram[++sp]
        ram[sp--] = lhs * rhs
    }

    private fun DIV() {
        val rhs = ram[++sp]
        val lhs = ram[++sp]
        ram[sp--] = lhs / rhs
    }

    private fun DEC_STACK(size: Int) {
        sp += size
    }

    private fun PUSH_STACK_OFFSET(offset: Int) {
        ram[sp--] = ram[sp - offset + 1]
    }

}