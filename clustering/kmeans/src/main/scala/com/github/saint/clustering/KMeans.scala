package com.github.saint.clustering

import com.github.saint.clustering.internal.Clusters.ClusterId
import com.github.saint.clustering.internal.Clusters
import com.github.saint.vector.dsl._

import scala.annotation.tailrec
import scala.language.postfixOps
import scala.util.Random

trait KMeansBase[T] {

  protected implicit def toVector(element: T): Seq[Double]

  protected def initialize(elements: Seq[T], k: Int): (Clusters[T], Seq[ClusterId])

  def clustering(elements: Seq[T], k: Int, maxIteration: Int = 1000): Clusters[T] = {

    @tailrec
    def iteration(accClusters: Clusters[T], accAssignments: Seq[ClusterId], accIteration: Int)(implicit vectorSize: Int): (Clusters[T], Seq[ClusterId], Int) =
      if (vectorSize == 0 || accIteration > maxIteration) (accClusters, accAssignments, accIteration)
      else {
        val clusterMeans: Seq[Seq[Double]] = accClusters.means
        val nextAssignments: Seq[ClusterId] = assignToClusters(elements, clusterMeans)
        val clusters = Clusters[T](elements, nextAssignments)

        if (accAssignments == nextAssignments) (clusters, nextAssignments, accIteration)
        else iteration(clusters, nextAssignments, accIteration + 1)
      }

    elements.headOption match {
      case None => Clusters.empty
      case Some(head) =>
        implicit val vectorSize = toVector(head).length
        val (clusters, assignments) = initialize(elements, k)
        iteration(accClusters = clusters, accAssignments = assignments, 0)._1
    }
  }

  // assign each element to the nearest cluster
  protected def assignToClusters(elements: Seq[T], clusterMeans: Seq[Seq[Double]]): Seq[ClusterId] = elements map { element =>
    clusterMeans.zipWithIndex.map { case (mean, clusterId) =>
      clusterId -> (element distanceTo mean)
    }.minBy(_._2)._1
  }

}

trait KMeans[T] extends KMeansBase[T] {
  override protected def initialize(elements: Seq[T], k: Int): (Clusters[T], Seq[ClusterId]) = elements.headOption match {
    case None => Clusters.empty[T] -> Seq.empty[ClusterId]
    case Some(head) =>
      implicit val vectorSize = toVector(head).length
      val generator = new Random(System.currentTimeMillis())
      val assignments: Seq[ClusterId] = elements.map(_ => generator.nextInt(k))
      Clusters(elements, assignments) -> assignments
  }
}
