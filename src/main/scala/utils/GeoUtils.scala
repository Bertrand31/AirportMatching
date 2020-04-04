package utils

import airportmatching.Point

object GeoUtils {

  private val EarthRadius = 6371

  /** Calculates the distance between two points of the Earth
    */
  def distance(pointA: Point, pointB: Point): Double = {
    val latA = Math.toRadians(pointA.lat)
    val latB = Math.toRadians(pointB.lat)
    val lonA = Math.toRadians(pointA.lon)
    val lonB = Math.toRadians(pointB.lon)

    // Haversine formula
    val dLat = latB - latA
    val dLon = lonB - lonA
    val a =
      Math.pow(Math.sin(dLat / 2d), 2d) +
      Math.cos(latA) * Math.cos(latB) *
      Math.pow(Math.sin(dLon / 2d), 2d)

    2d * Math.asin(Math.sqrt(a)) * EarthRadius
  }
}
