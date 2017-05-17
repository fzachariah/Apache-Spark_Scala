package com.febin.scala.SparkScala

import org.apache.spark.SparkContext
import org.apache.log4j.Logger
import org.apache.log4j.Level
import scala.io.Codec
import java.nio.charset.CodingErrorAction
import scala.io.Source

object PopularMovie {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "PopularMovie")

    var nameDict = sc.broadcast(loadMovieNames)

    val lines = sc.textFile("../ml-100k/u.data")

    val movies = lines.map(x => (x.split("\t")(1).toInt, 1))

    val movieCounts = movies.reduceByKey((x, y) => x + y)

    val flipped = movieCounts.map(x => (x._2, x._1))

    val sortedMovies = flipped.sortByKey()

    val sortedMoviesWithNames = sortedMovies.map(x => (nameDict.value(x._2), x._1))

    // Collect and print results
    val results = sortedMoviesWithNames.collect()

    results.foreach(println)
  }

  /** Load up a Map of movie IDs to movie names. */
  def loadMovieNames(): Map[Int, String] = {

    // Handle character encoding issues:
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    // Create a Map of Ints to Strings, and populate it from u.item.
    var movieNames: Map[Int, String] = Map()

    val lines = Source.fromFile("../u.item").getLines()
    for (line <- lines) {
      var fields = line.split('|')
      if (fields.length > 1) {
        movieNames += (fields(0).toInt -> fields(1))
      }
    }

    return movieNames
  }

}