package com.notedgeek.kcmachine.build.assemble

val opCodesMap = mapOf(
    "HALT" to 0,
    "PUSH_CONST" to 1,
    "LBP" to 2,
    "LSP" to 3,
    "PUSH_BP" to 4,
    "POP_BP" to 5,
    "SP_TO_BP" to 6,
    "BP_TO_SP" to 7,
    "SAVE_A" to 8,
    "CALL" to 9,
    "RETURN" to 10
)