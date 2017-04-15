package com.github.saint.vector

package object dsl {
  implicit def toVectorRepr[T](x: T)(implicit conversion: T => Seq[Double]): VectorRepresentation =
    VectorRepresentation(x)
  implicit def toVectorRepr(vector: Seq[Double]): VectorRepresentation =
    VectorRepresentation(vector)
}
