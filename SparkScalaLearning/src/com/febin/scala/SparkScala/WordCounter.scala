package com.febin.scala.SparkScala

import org.apache.log4j._
import org.apache.spark.SparkContext

object WordCounter {
   
  def main(args:Array[String])
  {
    Logger.getLogger("org").setLevel(Level.ERROR);
    
    val sc=new SparkContext("local[*]","WordCounter")
    
    val lines=sc.textFile("../book.txt")
    
    val word=lines.flatMap(x=>x.split(" "))
    
    val words=word.countByValue()
    
    words.foreach(println)
    
  }
  
}