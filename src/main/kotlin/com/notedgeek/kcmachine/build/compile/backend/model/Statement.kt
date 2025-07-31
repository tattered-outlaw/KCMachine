package com.notedgeek.kcmachine.build.compile.backend.model

interface Statement : Element

data class CompoundStatement(override val start: Int, override val end: Int, val statements: List<Statement>) : Statement

data class ReturnStatement(override val start: Int, override val end: Int, val expression: Expression) : Statement
