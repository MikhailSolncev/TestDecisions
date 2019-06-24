package com.debugg3r.android.testdecisions

class TestOdd() {
    public fun printOdd(array: Array<Int>) {
        array.toList().filter { it % 2 == 1 }.forEach { println("$it ") }
    }
}