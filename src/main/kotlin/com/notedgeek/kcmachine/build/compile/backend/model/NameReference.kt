package com.notedgeek.kcmachine.build.compile.backend.model

sealed interface NameReference

data class ArgumentReference(val index: Int) : NameReference