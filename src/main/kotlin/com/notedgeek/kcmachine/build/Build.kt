package com.notedgeek.kcmachine.build

import com.notedgeek.kcmachine.build.assemble.assemble
import com.notedgeek.kcmachine.build.compile.compile

fun build(source: String,  externalValues: Map<String, Int> = emptyMap()) = assemble(compile(source), externalValues)
