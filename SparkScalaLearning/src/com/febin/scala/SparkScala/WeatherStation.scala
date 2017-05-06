package com.febin.scala.SparkScala

import org.apache.log4j._
import org.apache.spark.SparkContext

import scala.math.min

object WeatherStation {
  
  def main(args:Array[String])
  {
    Logger.getLogger("org").setLevel(Level.ERROR);
    
    val sc =new SparkContext("local[*]","minTemp")
    
    val lines=sc.textFile("../1800.csv")
    
    val parsedResult=lines.map(parseLines)
    
    val filteredData=parsedResult.filter(x=>x._2=="TMIN")
    
    val stationTemp=filteredData.map(x=>(x._1,x._3.toFloat))
    
    val minTempStation=stationTemp.reduceByKey((x,y)=>min(x,y))
    
    val results = minTempStation.collect()
    
    for(result<-results.sorted)
    {
      val station = result._1
       val temp = result._2
       val formattedTemp = f"$temp%.2f F"
       println(s"$station minimum temperature: $formattedTemp")
    }
    
    
    
    
  }
  
  def parseLines(lines:String)={
    
    val fields=lines.split(",")
    val stationId=fields(0)
    val entryType=fields(2)
    val temperature=fields(3)
    
    (stationId,entryType,temperature)

  }
  
}