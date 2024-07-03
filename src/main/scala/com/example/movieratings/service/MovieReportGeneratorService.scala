package com.example.movieratings.service

import com.example.movieratings.mapper.BaseMapper.stringArrayToHashMap
import com.example.movieratings.utils.TxtUtils.{getTxtFilesList, readTxtFileAsArray}

import java.io.File
import java.math.{MathContext, RoundingMode}
import java.util
import scala.collection.mutable.ArrayBuffer
import scala.math.BigDecimal.double2bigDecimal
import scala.util.Try

case object MovieReportGeneratorService {

  def generateReport(moviesFilesPath: String, moviesDataPath: String): Iterable[List[Any]] = {
    val movieTitles = readTxtFileAsArray(new File(moviesFilesPath))
    val movieMap = stringArrayToHashMap(movieTitles)

    val reports = processMovieData(movieMap, moviesDataPath)

    val sortedReports = reports.sortBy(r => (-r._3, r._1)).map {
      case (title, year, rating, reviews) => List[Any](title, year, rating, reviews)
    }

    sortedReports
  }


  private def processMovieData(
                                movieMap: util.HashMap[Int, (Int, String)], moviesDataPath: String
                              ): ArrayBuffer[(String, Int, Double, Int)] = {

    val reports = ArrayBuffer.empty[(String, Int, Double, Int)]
    val filesNames = getTxtFilesList(moviesDataPath)
    for (f <- filesNames) {
      val fileData = readTxtFileAsArray(f)
      val movieId = extractMovieId(fileData(0))
      if (fileData.length > 1000 && movieMap.keySet().contains(movieId)) {
        val movie = movieMap.get(movieId)
        val report = (
          movie._2,
          movie._1,
          calculateRating(fileData.tail),
          fileData.length - 1
        )
        reports += report
      }
    }
    reports
  }


  private def calculateRating(strings: Array[String]): Double = {
    val ratings = strings.flatMap { str =>
      str.split(",") match {
        case Array(_, rating, _) => Try(rating.toDouble).toOption
        case _ => None
      }
    }

    if (ratings.nonEmpty) {
      val mc = new MathContext(5, RoundingMode.HALF_UP)
      (ratings.sum / ratings.length.toDouble).rounded(mc).toDouble
    } else {
      0.0
    }
  }


  private def extractMovieId(value: String): Int = {
    value.split(":")(0).toInt
  }

}
