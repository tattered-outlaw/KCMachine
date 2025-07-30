package com.notedgeek.kcmachine.build.compile.backend.analyze

import com.notedgeek.kcmachine.build.compile.*
import com.notedgeek.kcmachine.build.compile.backend.model.*

fun analyzeC(cgCompilationUnit: CGCompilationUnit): CompilationUnit {
    val compilationItems = sequence {
        for (externalDeclaration in cgCompilationUnit.externalDeclarations) {
            yield(
                when (externalDeclaration) {
                    is CGFunctionDefinition -> analyzeFunctionDefinition(externalDeclaration)
                    else -> TODO()
                }
            )
        }
    }.toList()
    return CompilationUnit(cgCompilationUnit.start, cgCompilationUnit.end, compilationItems)
}

private fun analyzeFunctionDefinition(cgFunctionDefinition: CGFunctionDefinition): FunctionDefinition {
    if (cgFunctionDefinition.declarationSpecifiers.isEmpty()) {
        throw AnalyzeException("Declaration specifiers list is empty.")
    }

    val typeSpecifier = cgFunctionDefinition.declarationSpecifiers.first()

    if (typeSpecifier !is CGTypeSpecifier) {
        TODO()
    }

    if (typeSpecifier != CGTypeSpecifier.INT) {
        TODO()
    }

    if (cgFunctionDefinition.declarator !is CGEmptyFunctionDeclarator) {
        TODO()
    }

    if (cgFunctionDefinition.declarator.declarator !is CGIdentifierDeclarator) {
        TODO()
    }

    val identifier = cgFunctionDefinition.declarator.declarator.identifier

    val compoundStatement = analyzeCompoundStatement(cgFunctionDefinition.compoundStatement)

    return FunctionDefinition(
        cgFunctionDefinition.start,
        cgFunctionDefinition.end,
        identifier.value,
        QualifiedType(IntType),
        emptyList(),
        compoundStatement
    )
}

fun nameAndTypeFromDeclarator(declarator: CGConcreteDeclarator): NameAndType {
    fun recurse(declarator: CGConcreteDeclarator, stack: MutableList<(Type) -> WrapperType>): String {
        when (declarator) {
            is CGIdentifierDeclarator -> return declarator.identifier.value
            is CGPointerDeclarator -> {
                stack.add(::PointerType)
                return recurse(declarator.declarator, stack)
            }
            is CGEmptyFunctionDeclarator -> {
                stack.add(::FunctionType)
                return recurse(declarator.declarator, stack)
            }
            is CGArrayDeclarator -> {
                stack.add(::ArrayType)
                return recurse(declarator.declarator, stack)
            }
            else -> TODO()
        }
    }

    val stack = ArrayList<(Type) -> WrapperType>()
    val name = recurse(declarator, stack)
    val type = stack.fold(IntType) { acc: Type, next -> next.invoke(acc) }
    return NameAndType(name, type)

}

fun typeFromDeclarator(declarator: CGAbstractDeclarator): Type {
    fun recurse(declarator: CGAbstractDeclarator, stack: MutableList<(Type) -> WrapperType>) {
        when (declarator) {
            is CGAbstractDeclaratorPlaceholder -> return
            is CGAbstractPointerDeclarator -> {
                stack.add(::PointerType)
                return recurse(declarator.declarator, stack)
            }
            is CGAbstractEmptyFunctionDeclarator -> {
                stack.add(::FunctionType)
                return recurse(declarator.declarator, stack)
            }
            is CGAbstractArrayDeclarator -> {
                stack.add(::ArrayType)
                return recurse(declarator.declarator, stack)
            }
            else -> TODO()
        }
    }
    val stack = ArrayList<(Type) -> WrapperType>()
    recurse(declarator, stack)
    return stack.fold(IntType) { acc: Type, next -> next.invoke(acc) }
}

private fun analyzeStatement(cgStatement: CGStatement): Statement {
    return when (cgStatement) {
        is CGCompoundStatement -> analyzeCompoundStatement(cgStatement)
        is CGReturnStatement -> analyzeReturnStatement(cgStatement)
        else -> TODO()
    }
}

private fun analyzeCompoundStatement(cgCompoundStatement: CGCompoundStatement): CompoundStatement {
    val statements = cgCompoundStatement.statements.map { analyzeStatement(it) }
    return CompoundStatement(cgCompoundStatement.start, cgCompoundStatement.end, statements)
}

private fun analyzeReturnStatement(cgReturnStatement: CGReturnStatement): ReturnStatement {
    return ReturnStatement(cgReturnStatement.start, cgReturnStatement.end, analyzeExpression(cgReturnStatement.expression))
}

private fun analyzeExpression(cgExpression: CGExpression): Expression {
    return when (cgExpression) {
        is CGIntegerConstant -> analyzeIntExpression(cgExpression)
        is CGBinaryOperationExpression -> analyzeBinaryOperatorExpression(cgExpression)
        is CGFunctionCallExpression -> analyzeFunctionCallExpression(cgExpression)
        else -> TODO()
    }
}

private fun analyzeBinaryOperatorExpression(expr: CGBinaryOperationExpression): BinaryOperatorExpression {
    return BinaryOperatorExpression(expr.start, expr.end, analyzeExpression(expr.left), expr.operator, analyzeExpression(expr.right))
}

private fun analyzeFunctionCallExpression(expr: CGFunctionCallExpression): FunctionCallExpression {
    if(expr.expression !is CGIdentifierExpression) {
        throw AnalyzeException("Function call expression type ${expr.expression} not supported")
    }
    return FunctionCallExpression(expr.start, expr.end, IntType, expr.expression.value, expr.arguments.map { analyzeExpression(it) })
}

private fun analyzeIntExpression(cgIntegerConstant: CGIntegerConstant): IntExpression {
    return IntExpression(cgIntegerConstant.start, cgIntegerConstant.end, cgIntegerConstant.value)
}
