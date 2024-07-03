package com.example.movieratings

import com.example.movieratings.utils.CsvUtils.writeToFile
import com.example.movieratings.service.MovieReportGeneratorService.generateReport

import java.io.File


object ReportGenerator {

  private val ERROR_ARG_MESSAGE: String = "Please make sure your arguments are: " +
    "MOVIE_TITLES_PATH, MOVIE_TXT_FILES_DATA, PROCESSING_OUTPUT"

  def main(args: Array[String]): Unit = {

    if (args.length != 3) {
      println(ERROR_ARG_MESSAGE)
      sys.exit(1)
    }

    val reports = generateReport(args(0), args(1))

    writeToFile(reports, new File(args(2)))

  }
}
