package com.gumgum.source

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._
import org.apache.spark.sql.SQLContext

object ReadAssets {

  def main(args: Array[String]) {

    //Reading the Files
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "ReadAssets")
    val linesAssets = sc.textFile("../assets.gz")
    val linesAd_events = sc.textFile("../ad-events.gz")

    //Reading the required JSON Content
    val assetsJSON = linesAssets.map(x => x.toString().split("},")(1))
    val ad_eventsJSON = linesAd_events.map(x => x.toString().split("},")(1))

    //Asset Impression Count
    val assetsJSONPVTemp = assetsJSON.map(x => x.toString().substring(x.indexOf("\"pv\":\"") + 6))
    val assetsJSONPV = assetsJSONPVTemp.map(x => x.toString().substring(0, x.indexOf("\"")))
    val resultAssets = assetsJSONPV.countByValue()

    //View Count
    val viewCount = ad_eventsJSON.filter(x => x.toString().contains("\"e\":\"view\""))
    val viewsJSONPVTemp = viewCount.map(x => x.toString().substring(x.indexOf("\"pv\":\"") + 6))
    val viewsJSONPV = viewsJSONPVTemp.map(x => x.toString().substring(0, x.indexOf("\"")))
    val resultViews = viewsJSONPV.countByValue()

    //ClickCount
    val clickCount = ad_eventsJSON.filter(x => x.toString().contains("\"e\":\"click\""))
    val clicksJSONPVTemp = clickCount.map(x => x.toString().substring(x.indexOf("\"pv\":\"") + 6))
    val clicksJSONPV = clicksJSONPVTemp.map(x => x.toString().substring(0, x.indexOf("\"")))
    val resultClicks = clicksJSONPV.countByValue()

    //RDDs to be joined for getting the result
    val resultAssetRdd = sc.parallelize(resultAssets.toSeq)
    val resultViewsRdd = sc.parallelize(resultViews.toSeq)
    val resultClickRdd = sc.parallelize(resultClicks.toSeq)
    
    val resultTemp=resultAssetRdd.leftOuterJoin(resultViewsRdd)
    val resultFinal=resultTemp.leftOuterJoin(resultClickRdd)
    //resultFinal.foreach(println)
    //println(resultFinal.count())
    
    

  }

}