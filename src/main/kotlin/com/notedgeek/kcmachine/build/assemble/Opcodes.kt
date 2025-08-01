package com.notedgeek.kcmachine.build.assemble

val opcodeList = listOf (
    "HALT",
    "PUSH_CONST",
    "LBP",
    "LSP",
    "PUSH_BP",
    "POP_BP",
    "SP_TO_BP",
    "BP_TO_SP",
    "SAVE_A",
    "CALL",
    "RETURN",
    "ADD",
    "SUB",
    "MUL",
    "DIV",
    "PUSH_A",
    "DEC_STACK",
    "PUSH_STACK_OFFSET",
    "EQ",
    "JMP",
    "JMP_Z"
)

val opcodeMap = opcodeList.mapIndexed { index, opcode -> (opcode to index) }.toMap()