package com.notedgeek.kcmachine.build.compile.backend.model

sealed interface Type {
    val size: Int
}

data object IntType : Type {
    override val size = 1
}

interface WrapperType : Type {
    val wrappedType: Type
}

data class PointerType(override val wrappedType: Type) : WrapperType {
    override val size = wrappedType.size
}

data class FunctionType(override val wrappedType: Type) : WrapperType {
    override val size = wrappedType.size
}

data class ArrayType(override val wrappedType: Type) : WrapperType {
    override val size = wrappedType.size
}
