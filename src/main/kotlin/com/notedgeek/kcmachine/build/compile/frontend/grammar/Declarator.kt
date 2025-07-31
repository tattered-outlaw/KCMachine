package com.notedgeek.kcmachine.build.compile.frontend.grammar

interface CGDeclarator : CGElement

interface CGConcreteDeclarator : CGDeclarator

data class CGPointerDeclarator(
    override var start: Int,
    override var end: Int,
    val pointer: CGPointer,
    val declarator: CGConcreteDeclarator
) :
    CGConcreteDeclarator

interface CGDirectDeclarator : CGConcreteDeclarator

data class CGIdentifierDeclarator(override var start: Int, override var end: Int, val identifier: CGIdentifierExpression) :
    CGConcreteDeclarator

interface CGFunctionDeclarator : CGDirectDeclarator

data class CGEmptyFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGConcreteDeclarator
) : CGFunctionDeclarator

data class CGParameterFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGConcreteDeclarator,
    val parameterTypeList: List<CSParameterDeclaration>
) : CGFunctionDeclarator

data class CGIdentifierFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGConcreteDeclarator,
    val parameterTypeList: List<CGIdentifierDeclarator>
) : CGFunctionDeclarator

data class CGArrayDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGConcreteDeclarator,
    val parameterTypeList: CGConstantValueExpression? = null
) : CGConcreteDeclarator

interface CGAbstractDeclarator : CGDeclarator

data class CGAbstractPointerDeclarator(
    override var start: Int,
    override var end: Int,
    val pointer: CGPointer,
    val declarator: CGAbstractDeclarator
) :
    CGAbstractDeclarator

interface CGDirectAbstractDeclarator : CGAbstractDeclarator

interface CGAbstractFunctionDeclarator : CGDirectAbstractDeclarator

data class CGAbstractEmptyFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGAbstractDeclarator
) : CGAbstractFunctionDeclarator

data class CGAbstractParameterFunctionDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGAbstractDeclarator,
    val parameterTypeList: List<CSAbstractParameterDeclaration>
) : CGAbstractFunctionDeclarator

data class CGAbstractArrayDeclarator(
    override var start: Int,
    override var end: Int,
    val declarator: CGAbstractDeclarator,
    val parameterTypeList: CGConstantValueExpression? = null
) : CGAbstractDeclarator

data class CGAbstractDeclaratorPlaceholder(override var start: Int, override var end: Int) : CGAbstractDeclarator


data class CGPointer(override var start: Int, override var end: Int, val typeQualifiers: List<CGTypeQualifier>) : CGElement
