package com.notedgeek.kcmachine.build.compile.backend.model

sealed interface Type {
    val size: Int
}

data object IntType : Type {
    override val size = 1
}

sealed interface WrapperType : Type {
    val wrappedType: Type
}

data class PointerType(override val wrappedType: Type) : WrapperType {
    override val size = wrappedType.size
    val targetType = wrappedType
}

data class FunctionType(override val wrappedType: Type, val parameterTypes: List<Type> = emptyList() ) : WrapperType {
    override val size = wrappedType.size
    val returnType = wrappedType
}

data class ArrayType(override val wrappedType: Type) : WrapperType {
    override val size = wrappedType.size
    val elementType = wrappedType
}

data class NameAndType(val name: String, val type: Type)
data class QualifiedType(var type: Type)

data class QualifiedNameAndType(val name: String, val type: QualifiedType) {
    constructor(nameAndType: NameAndType) : this(nameAndType.name, QualifiedType(nameAndType.type))
}