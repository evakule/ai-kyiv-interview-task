package com.example.movieratings.utils

import java.io.File
import scala.io.{Codec, Source}

/** Additional methods to work with TXT files.
 *
 */
object TxtUtils {

  def readTxtFileAsArray(file: File): Array[String] = {
    var source: Source = null
    try {
      source = Source.fromFile(file)(Codec.ISO8859)
      source.mkString.split("\n")
    } catch {
      case e: Exception =>
        e.printStackTrace()
        Array.empty[String]
    } finally {
      if (source != null) {
        source.close()
      }
    }
  }

  def getTxtFilesList(folderPath: String): Array[File] = {
    new File(folderPath).listFiles.filter(_.getName.endsWith(".txt"))
  }

}
