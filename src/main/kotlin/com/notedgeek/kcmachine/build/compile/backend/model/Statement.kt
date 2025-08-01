package com.notedgeek.kcmachine.build.compile.backend.model

sealed interface Statement : Element

data class CompoundStatement(override val start: Int, override val end: Int, val statements: List<Statement>) : Statement

data class ReturnStatement(override val start: Int, override val end: Int, val expression: Expression) : Statement

data class IfStatement(override val start: Int, override val end: Int, val condition: Expression, val trueStatement: Statement, val falseStatement: Statement?) : Statement
