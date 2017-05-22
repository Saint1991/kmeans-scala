package com.github.saint1991.clustering

case class ClusteringResult[T](clusters: Clusters[T]) extends AnyVal {
  override def toString: String = clusters.zipWithIndex.foldLeft(new StringBuilder()) { case (builder, (cluster, index)) =>
    builder.append(s"Cluster[$index] => ")
    builder.append(cluster.map(_.self.toString).mkString(", "))
    builder.append("\n")
  }.toString()
  def show(): Unit = println(toString)
}
