package com.github.saint1991.vector

trait VectorSpace {
  def distance(x1: Seq[Double], x2: Seq[Double]): Double
  def mean(xs: Seq[Double]*): Seq[Double]
}

object EuclideanSpace extends VectorSpace {
  override def distance(x1: Seq[Double], x2: Seq[Double]): Double = Math.sqrt(
    x1.zipAll(x2, 0.0, 0.0).map(x => Math.pow(x._1 - x._2, 2.0)).sum
  )
  override def mean(xs: Seq[Double]*): Seq[Double] = xs.foldLeft(Seq.empty[Double]) { (x, acc) =>
    x.zipAll(acc, 0.0, 0.0).map(x => x._1 + x._2)
  } map(_ / xs.length)
}

case class VectorRepr[T](self: T)(implicit mapToVector: T => Seq[Double], measureDistance: (Seq[Double], Seq[Double]) => Double) {

  lazy val vector: Seq[Double] = mapToVector(self)
  lazy val length: Int = vector.length

  def +(another: Seq[Double]): Seq[Double] = vector.zipAll(another, 0.0, 0.0).map(x => x._1 + x._2)
  def +(another: VectorRepr[T]): Seq[Double] = this + another.vector
  def -(another: Seq[Double]): Seq[Double] = vector.zipAll(another, 0.0, 0.0).map(x => x._1 - x._2)
  def -(another: VectorRepr[T]): Seq[Double] = this - another.vector
  def *(factor: Double): Seq[Double] = vector.map(_ * factor)
  def *(another: Seq[Double]): Double = vector.zipAll(another, 0.0, 0.0).map(x => x._1 * x._2).sum // inner product
  def /(factor: Double): Seq[Double] = vector.map(_ / factor)

  def distanceFrom(another: Seq[Double]): Double = measureDistance(vector, another)
  def distanceFrom(another: VectorRepr[T]): Double = distanceFrom(another.vector)
}
