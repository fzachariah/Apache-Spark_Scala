package com.febin.scala.SparkScala

import org.apache.log4j._
import org.apache.spark.SparkContext

object FriendsAge {
  
  
  def main(args: Array[String])
  {
    Logger.getLogger("org").setLevel(Level.ERROR);
    
    val sc=new SparkContext("local[*]","FriendsAge")
    
    val lines=sc.textFile("../fakeFriends.csv")
    
    val rdd=lines.map(parseLine)
    
    val temp=rdd.mapValues(x=>(x,1))
    
    val totalByAge=temp.reduceByKey((x,y)=>(x._1+y._1,x._2+y._2))
    
    
    val average=totalByAge.mapValues(x=>(x._1/x._2))
    
    val result=average.collect()
    
    result.sorted.foreach(println)
    
  }
  
  def parseLine(line:String)={
    
    val fields=line.split(",")
    val age=fields(2).toInt
    val count=fields(3).toInt
    
    (age,count)
  }
}