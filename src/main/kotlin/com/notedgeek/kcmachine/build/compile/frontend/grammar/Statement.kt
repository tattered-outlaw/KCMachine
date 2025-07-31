package com.notedgeek.kcmachine.build.compile.frontend.grammar

interface CGStatement : CGElement

data class CGCompoundStatement(override var start: Int, override var end: Int, val statements: List<CGStatement>) : CGStatement

data class CGReturnStatement(override var start: Int, override var end: Int, val expression: CGExpression) : CGStatement

