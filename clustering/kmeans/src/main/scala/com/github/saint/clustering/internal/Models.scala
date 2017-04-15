package com.github.saint.clustering.internal

import com.github.saint.vector.dsl._

private [saint] case class Cluster[T](elements: Seq[T])
  (implicit toVector: T => Seq[Double], vectorSize: Int) {

  lazy val mean: Seq[Double] =
    elements
      .map(toVector)
      .foldLeft(Seq.fill(vectorSize)(0.0)) {
        (acc, vector) => acc + vector
      } / elements.length
}

object Clusters {
  type ClusterId = Int
  def empty[T]: Clusters[T] = Clusters(Seq.empty[Cluster[T]])
  def apply[T](elements: Seq[T], assignments: Seq[ClusterId])(implicit toVector: T => Seq[Double], vectorSize: Int): Clusters[T] = Clusters(
    assignments.zipWithIndex
      .groupBy{ case (clusterId, elementId) => clusterId }
      .map ( x => x._1 -> x._2.map { case (_, elementId) => elements(elementId) } ).toSeq
      .sortBy( x => x._1 )
      .map { case (clusterId, members) => Cluster(members) }
  )
}

private [saint] case class Clusters[T](clusters: Seq[Cluster[T]]) {
  lazy val means: Seq[Seq[Double]] = clusters.map(_.mean)
}
