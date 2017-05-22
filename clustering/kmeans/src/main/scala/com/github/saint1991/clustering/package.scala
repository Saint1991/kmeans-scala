package com.github.saint1991

import com.github.saint1991.vector.VectorRepr

package object clustering {
  type ClusterIndex = Int
  type Cluster[T] = Seq[VectorRepr[T]]
  type Clusters[T] = Seq[Cluster[T]]

  def groupByAssignments[T](vectors: Seq[VectorRepr[T]], assignments: Seq[ClusterIndex]): Map[ClusterIndex, Seq[VectorRepr[T]]] =
    (vectors zip assignments).groupBy(_._2).map(x => x._1 -> x._2.map(_._1))
}
