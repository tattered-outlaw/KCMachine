package com.notedgeek.kcmachine.build.compile.backend.model

sealed interface Type {
    val szie: Int
}

data object IntType : Type {
    override val szie = 1
}
