package com.notedgeek.kcmachine

import com.notedgeek.kcmachine.build.build
import com.notedgeek.kcmachine.execute.execute

fun runCode(code: String, ramSize: Int) = execute(build(code, mapOf("\$STACK_TOP" to ramSize - 1)), ramSize).toInt()
