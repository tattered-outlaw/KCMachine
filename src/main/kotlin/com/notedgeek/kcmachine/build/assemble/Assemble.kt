package com.notedgeek.kcmachine.build.assemble

fun assemble(source: List<String>, externalValues: Map<String, Int> = emptyMap()): List<Long> {
    return secondPass(source, firstPass(source, externalValues))
}

private val spaceRegex = "\\s+".toRegex()

fun firstPass(lines: List<String>, externalValues: Map<String, Int>): Map<String, Int> {
    val result = HashMap<String, Int>()
    result.putAll(externalValues)
    var address = 0
    lines.forEachIndexed { index, line ->
        val words = line.split(spaceRegex)
        val firstWord = words[0]
        if (firstWord.endsWith(':')) {
            val label = firstWord.substring(0, firstWord.length - 1)
            if(result.containsKey(label)) {
                throw AssembleException("Duplicate label '$label' on line ${index + 1}.")
            } else {
                result[label] = address
            }
        } else {
            opcodeMap[firstWord]?.let { _ -> address++ } ?: throw AssembleException("Unknown opcode: $firstWord.")
        }
    }
    return result
}

fun secondPass(lines: List<String>, labels: Map<String, Int>): List<Long> {
    val labelsByAddress = HashMap<Int, MutableList<String>>()
    labels.forEach {(label, address) ->
        labelsByAddress.putIfAbsent(address, ArrayList())
        labelsByAddress[address]?.add("$label:")
    }
    val result = ArrayList<Long>()
    var address = 0
    lines.forEachIndexed { index, line ->
        val words = line.split(spaceRegex)
        opcodeMap[words[0]]?.let { opcode ->
            labelsByAddress[address]?.let { println(it.joinToString()) }
            var instruction = opcode.toLong() shl 32
            var operand = 0
            var hasOperand = false
            if(words.size > 1) {
                hasOperand = true
                val secondWord  = words[1]
                operand = if(secondWord.all { it.isDigit() }) {
                    secondWord.toInt()
                } else {
                    labels[secondWord] ?: throw AssembleException("Unknown label '$secondWord' at line ${index + 1}.")
                }
                instruction += operand
            }
            println("$address     $line - $opcode${if (hasOperand) ",$operand" else ""} ${java.lang.Long.toHexString(instruction)}")
            result.add(instruction)
            address++
        }
    }
    labelsByAddress[address]?.let { println(it.joinToString()) }
    return result
}

