package com.example.movieratings.service

import org.scalatest.flatspec.AnyFlatSpec

import java.io.File
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfterEach


class MovieReportGeneratorServiceSpec extends AnyFlatSpec with Matchers with BeforeAndAfterEach {

  val service: MovieReportGeneratorService.type = MovieReportGeneratorService

  var movieTitlesFile: File = _
  var trainingSetFolder: File = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    movieTitlesFile = File.createTempFile("movie_titles", ".txt")
    trainingSetFolder = new File(System.getProperty("java.io.tmpdir"), "training_set")
    trainingSetFolder.mkdir()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    movieTitlesFile.delete()
    trainingSetFolder.listFiles().foreach(_.delete())
    trainingSetFolder.delete()
  }

  it should "handle missing movie data files" in {
    val movieTitlesData = "1,1980,Movie A\n2,1985,Movie B\n3,1990,Movie C"
    writeFile(movieTitlesFile, movieTitlesData)

    val result = service.generateReport(movieTitlesFile.getPath, trainingSetFolder.getPath)
    result shouldBe empty
  }

  it should "ignore movies with less than 1000 reviews" in {
    val movieTitlesData = "1,1980,Movie A"
    val movie1Data = "1:\n1,5.0,2020-01-01"

    writeFile(movieTitlesFile, movieTitlesData)
    writeFile(new File(trainingSetFolder, "mv_0001.txt"), movie1Data)

    val result = service.generateReport(movieTitlesFile.getPath, trainingSetFolder.getPath)
    result shouldBe empty
  }

  private def writeFile(file: File, data: String): Unit = {
    import java.io.PrintWriter
    val writer = new PrintWriter(file)
    try {
      writer.write(data)
    } finally {
      writer.close()
    }
  }

}
