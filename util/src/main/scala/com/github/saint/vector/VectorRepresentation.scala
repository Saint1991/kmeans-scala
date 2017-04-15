package com.github.saint.vector

case class VectorRepresentation(vector: Seq[Double]) extends AnyVal {

  // Basic arithmetic operations for vector
  def +(another: VectorRepresentation): Seq[Double] = checkComparisonAndThen(another) {
    (vector zip another.vector).map(x => x._1 + x._2)
  }
  def -(another: VectorRepresentation): Seq[Double] = checkComparisonAndThen(another) {
    (vector zip another.vector).map(x => x._1 - x._2)
  }
  def *(factor: Double): Seq[Double] = vector.map(_ * factor)
  def /(factor: Double): Seq[Double] = vector.map(_ / factor)

  // inner product
  def *(another: VectorRepresentation): Double = checkComparisonAndThen(another) {
    (vector zip another.vector).foldLeft(0.0) {
      (acc, dimElements) => acc + dimElements._1 * dimElements._2
    }
  }
  def length: Int = vector.length
  def distanceTo(that: VectorRepresentation): Double = Math.sqrt( (this - that).map(Math.pow(_, 2.0)).sum )

  private def checkComparisonAndThen[T](comparison: VectorRepresentation)(f:  => T): T = {
    require(vector.length == comparison.vector.length, "two vectors must have the same degree")
    f
  }

}