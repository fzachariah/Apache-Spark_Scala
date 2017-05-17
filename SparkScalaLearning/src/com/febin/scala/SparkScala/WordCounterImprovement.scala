package com.febin.scala.SparkScala

import org.apache.log4j._
import org.apache.spark.SparkContext
object WordCounterImprovement {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR);

    val sc = new SparkContext("local[*]", "WordCounter")

    val lines = sc.textFile("../book.txt")

    val word = lines.flatMap(x => x.split("\\W+"))

    val lowerCase = word.map(x => x.toLowerCase())

   // val words = lowerCase.countByValue()
    
    val words=lowerCase.map(x=>(x,1)).reduceByKey((x,y)=>x+y)
    
    val sorted=words.map(x=>(x._2,x._1)).sortByKey()

    sorted.foreach(println)
  }

}