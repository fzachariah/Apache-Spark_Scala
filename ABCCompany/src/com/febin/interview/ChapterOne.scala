package com.febin.interview

object ChapterOne {
  def main(args: Array[String]) {
    var i = 0
    println(i)
    i = 12
    println(i)

    val k: Int = 10;
    println(k)

    //i="Hello"
    println(factorial(5))

    println(sum(id, 1, 10))
    println(sum(sqr, 2, 5))
    println(sum(cube, 1, 4))
  }

  def sum(f: Int => Int, a: Int, b: Int): Int = {
    f(a) + a + b
  }

  def id(i: Int) = i
  def sqr(i: Int) = math.pow(i, 2).toInt
  def cube(i: Int) = math.pow(i, 3).toInt

  def factorial(i: Int) = {
    var result = 1;
    for (i <- 2 to i) {
      result = result * i
    }
    result
  }
}