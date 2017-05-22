package com.github.saint1991.clustering

import com.github.saint1991.vector.VectorRepr

import scala.util.Random

trait Initialization[T] {
  protected def initialize(elements: Seq[VectorRepr[T]], k: Int): Seq[ClusterIndex]
}

trait RandomInitialization[T] {
  protected def initialize(elements: Seq[T], k: Int): Seq[ClusterIndex] = {
    val generator = new Random(System.currentTimeMillis())
    elements.map(_ => generator.nextInt(k))
  }
}
