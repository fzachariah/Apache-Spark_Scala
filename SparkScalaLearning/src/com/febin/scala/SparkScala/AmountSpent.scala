package com.febin.scala.SparkScala

import org.apache.log4j._
import org.apache.spark.SparkContext

object AmountSpent {
  def main(args: Array[String]) {
    
    Logger.getLogger("org").setLevel(Level.ERROR);
    
    val sc=new SparkContext("local[*]","CustomerOrder")
    
    val lines=sc.textFile("../customer-orders.csv")
    
    val rdd=lines.map(parseLine)
    
    val result=rdd.reduceByKey((x,y)=>(x+y))
    
    val sorted=result.map(x=>(x._2,x._1)).sortByKey()
    
    val finalRsult=sorted.collect()
    
    finalRsult.foreach(println)
       

  }
  
  def parseLine(line:String)={
    
    val fields=line.split(",")
    val userId=fields(0)
    val amount=fields(2).toFloat
    
    (userId,amount)
    
  }
}