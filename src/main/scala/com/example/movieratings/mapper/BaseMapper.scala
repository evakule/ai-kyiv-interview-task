package com.example.movieratings.mapper

import java.util

/** Simple mapper to map base DS between each other.
 *
 */
object BaseMapper {

  def stringArrayToHashMap(movies: Array[String]): util.HashMap[Int, (Int, String)] = {
    val hashMap = new util.HashMap[Int, (Int, String)]()
    for (mvt <- movies) {
      val row = mvt.split(",")
      if (isYearInRequiredRange(row(1))) {
        hashMap.put(row(0).toInt, (row(1).toInt, row(2)))
      }
    }
    hashMap
  }

  private def isYearInRequiredRange(value: String): Boolean = {
    try {
      val year = value.toInt
      year >= 1970 && year <= 1990
    } catch {
      case _: NumberFormatException => false
    }
  }

}
