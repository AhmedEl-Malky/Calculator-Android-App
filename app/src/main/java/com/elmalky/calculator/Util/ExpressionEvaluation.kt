package com.elmalky.calculator.Util

import java.util.Stack

/**
 * @param op which is an operator e.g.(+,-,*,/)
 * @return boolean value true if this parameter is operator or false if it is not
 */
fun isOperator(op: Char): Boolean = op == '+' || op == '–' || op == '÷' || op == '×' || op == '%'

/**
 * @param op is an operand which is an any real number e.g.(9,99,999,....)
 * @return boolean value true if this parameter is operand or false if it is not
 */
fun isOperand(op: Char): Boolean = op in '0'..'9'

// region conversion from infix expression to postfix expression functions
var precedences = mapOf(
    '%' to 2,
    '×' to 2,
    '÷' to 2,
    '+' to 1,
    '–' to 1
) // a map that map precedence for its operator

/**
 * @param op1 is the first operator e.g.(+)
 * @param op2 is the second operator e.g.(/)
 * @return return boolean value true if op1 >= op2 and false if not
 */
fun hasHigherPrecedence(op1: Char, op2: Char): Boolean = precedences[op1]!! >= precedences[op2]!!

/**
 * @param infixExpression is normal(infix) expression e.g.(4*5-6+10/2)
 * @return postfix expression for this infix expression e.g.(4 5 * 6 - 10 2 / +)
 * @see isOperand
 */
fun infixToPostfix(infixExpression: String?): String {
    var posfixExpression = ""
    var conversionStack: Stack<Char> = Stack<Char>()
    if (infixExpression != null) {
        for (i in infixExpression) {
            if (i == ' ' || i == ',')
                continue
            else if (isOperand(i) || i == '.')
                posfixExpression += i
            else {
                posfixExpression += ' '
                while (!conversionStack.isEmpty() && hasHigherPrecedence(
                        conversionStack.peek(),
                        i
                    )
                ) {
                    posfixExpression += conversionStack.peek()
                    posfixExpression += ' '
                    conversionStack.pop()
                }
                conversionStack.push(i)
            }
        }
        while (!conversionStack.isEmpty()) {
            posfixExpression += ' '
            posfixExpression += conversionStack.peek()
            conversionStack.pop()
        }
    }
    return posfixExpression
}
// endregion

// region expression evaluation functions
/**
 * @param op1 which is the first operand e.g.(10)
 * @param op2 which is the second operand e.g.(2)
 * @param op which is the operator that will be applied on the two operands
 * @return double value of result of operation e.g.(10+2 , 10/2 , 10-2 , 10*2 )
 */
fun performOperation(op1: Double, op2: Double, op: Char): Double =
    when (op) {
        '+' -> op1 + op2
        '–' -> op1 - op2
        '÷' -> op1 / op2
        '×' -> op1 * op2
        '%' -> op1 % op2
        else -> 0.0
    }

/**
 * @param postfixExpression which is a result from conversion of infix expression e.g.(4 5 * 6 - 10 2 / +)
 * @return double value which is the final result of your expression e.g.(result = 19.0)
 */
fun evaluateExpression(postfixExpression: String?): Double? {
    var buffer = ""
    var evaluationStack: Stack<Double> = Stack<Double>()
    if (postfixExpression != null) {
        for (i in postfixExpression) {
            if (i in '0'..'9' || i == '.')
                buffer += i
            else if (i == ' ') {
                if (!buffer.isEmpty())
                    evaluationStack.push(buffer.toDouble())
                buffer = ""
            } else if (isOperator(i)) {
                var op1: Double = evaluationStack.peek()
                evaluationStack.pop()
                var op2: Double = evaluationStack.peek()
                evaluationStack.pop()
                evaluationStack.push(performOperation(op2, op1, i))
            }
        }
        if (evaluationStack.isEmpty())
            return buffer.toDouble()
        return evaluationStack.peek()
    }
    return null
}
// endregion
//fun main(){
//    println("125.0".contains('.'))
//}