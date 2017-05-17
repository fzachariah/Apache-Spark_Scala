package com.febin.scala.SparkScala

import org.apache.spark.SparkContext
import org.apache.log4j.Logger
import org.apache.log4j.Level
import scala.io.Codec
import java.nio.charset.CodingErrorAction
import scala.io.Source

object PopularHero {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val sc = new SparkContext("local[*]", "PopularHero")
    
    val names = sc.textFile("../marvel-names.txt")
    val namesRdd = names.flatMap(parseNames)
    
    val lines = sc.textFile("../marvel-graph.txt")
    
    val pair=lines.map(countCoOccurences)
    
    val totalFriends=pair.reduceByKey((x,y)=>x+y)
    
    val flipped = totalFriends.map( x => (x._2, x._1) )
    
    val max=flipped.max()
    
    val mostPopularName = namesRdd.lookup(max._2)(0)
    
    // Print out our answer!
    println(s"$mostPopularName is the most popular superhero with ${max._1} co-appearances.")
  }
  
  def countCoOccurences(line: String) = {
    var elements = line.split("\\s+")
    ( elements(0).toInt, elements.length - 1 )
  }
  
  def parseNames(line: String) : Option[(Int, String)] = {
    var fields = line.split('\"')
    if (fields.length > 1) {
      return Some(fields(0).trim().toInt, fields(1))
    } else {
      return None // flatmap will just discard None results, and extract data from Some results.
    }
  }

}