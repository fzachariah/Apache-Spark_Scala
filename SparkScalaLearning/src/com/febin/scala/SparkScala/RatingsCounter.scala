package com.febin.scala.SparkScala

import org.apache.spark.SparkContext
import org.apache.log4j.Logger
import org.apache.log4j.Level

/*import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._*/

/** Count up how many of each star rating exists in the MovieLens 100K data set. */
object RatingsCounter {
 
  /** Our main function where the action happens */
  def main(args: Array[String]) {
    
    Logger.getLogger("org").setLevel(Level.ERROR)
   
    val sc=new SparkContext("local[*]","RatingsCounter")
    
    val lines=sc.textFile("../ml-100k/u.data")
    
    val ratings=lines.map(x=>x.toString().split("\t")(2))
    
    val result=ratings.countByValue();
    
    println(result)
    
    val sortedResults = result.toSeq.sortBy(_._1)
    
    // Print each result on its own line.
    sortedResults.foreach(println)
    
  }
}
