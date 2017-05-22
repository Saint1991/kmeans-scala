package com.github.saint1991.clustering

import com.github.saint1991.vector.{VectorRepr, VectorSpace}

import scala.annotation.tailrec
import scala.language.postfixOps


trait KMeans[T] extends Initialization[T] {

  protected def initialize(elements: Seq[VectorRepr[T]], k: Int): Seq[ClusterIndex]

  def clustering(elements: Seq[T], k: Int, maxIteration: Int = 1000, conversionThreshold: Double = 0.01)
                (implicit mapToVector: T => Seq[Double], space: VectorSpace): ClusteringResult[T] = {

    val vectors: Seq[VectorRepr[T]] = elements.map(VectorRepr(_)(mapToVector, space.distance))
    val assignments: Seq[ClusterIndex] = initialize(vectors, k)
    require(vectors.length == assignments.length, "all elements should be assigned to a cluster")
    require(assignments.forall(idx => 0 <= idx && idx < k), s"all elements should be assigned to a cluster within a range of 0 to $k")

    val clusters: Map[ClusterIndex, Seq[VectorRepr[T]]] = groupByAssignments(vectors, assignments)

    @tailrec
    def iteration(acc: Map[ClusterIndex, Seq[VectorRepr[T]]], currentIteration: Int): Map[ClusterIndex, Seq[VectorRepr[T]]] = {
      val means: Map[ClusterIndex, Seq[Double]] = acc.par.map(x => x._1 -> space.mean(x._2.map(_.vector):_*)).seq
      val nextAssignments = vectors.par.map(vector => means.minBy(clusterMean => vector distanceFrom clusterMean._2)._1).seq
      val nextClusters = groupByAssignments(vectors, nextAssignments)

      val objective = nextClusters.map { case (idx, cluster) =>
        cluster.map(x => Math.sqrt(x distanceFrom means(idx))).sum
      }.sum

      if (currentIteration + 1 > maxIteration || objective <= conversionThreshold) nextClusters
      else iteration(nextClusters, currentIteration + 1)
    }

    ClusteringResult(iteration(clusters, 0).values.toSeq)
  }
}
