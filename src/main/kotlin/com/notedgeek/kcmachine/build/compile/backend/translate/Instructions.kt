package com.notedgeek.kcmachine.build.compile.backend.translate

object Inst {
    fun HALT() = "HALT"
    fun PUSH_CONST(operand: String) = "PUSH_CONST $operand"
    fun LBP(operand: String) = "LBP $operand"
    fun LSP(operand: String) = "LSP $operand"
    fun PUSH_BP() = "PUSH_BP"
    fun POP_BP() = "POP_BP"
    fun SP_TO_BP() = "SP_TO_BP"
    fun BP_TO_SP() = "BP_TO_SP"
    fun SAVE_A(operand: String) = "SAVE_A $operand"
    fun CALL(operand: String) = "CALL $operand"
    fun RETURN() = "RETURN"
}