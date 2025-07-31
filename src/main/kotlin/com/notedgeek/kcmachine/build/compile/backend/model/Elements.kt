package com.notedgeek.kcmachine.build.compile.backend.model

sealed interface Element {
    val start: Int
    val end: Int
}

data class CompilationUnit(override val start: Int, override val end: Int, val items: List<CompilationItem>) : Element

interface CompilationItem : Element

data class FunctionDefinition(
    override val start: Int,
    override val end: Int,
    val name: String,
    val returnType: QualifiedType,
    val parameters: List<NameAndType>,
    val statement: CompoundStatement
) : CompilationItem
