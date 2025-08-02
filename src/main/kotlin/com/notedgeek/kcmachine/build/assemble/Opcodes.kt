package com.notedgeek.kcmachine.build.assemble

val opcodeList = listOf (
    "HALT",
    "PUSH_CONST",
    "LBP",
    "LSP",
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
    "JMP_Z",
    "LOR",
    "ENTER"
)

val opcodeMap = opcodeList.mapIndexed { index, opcode -> (opcode to index) }.toMap()