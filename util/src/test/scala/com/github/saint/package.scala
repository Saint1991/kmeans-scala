package com.github

import org.scalactic.{Equality, TolerantNumerics}

package object saint {
  final val DefaultPrecision = 1.0E-7
  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(DefaultPrecision)
  implicit val doubleSeqEq: Equality[Seq[Double]] = new Equality[Seq[Double]] {
    override def areEqual(a: Seq[Double], b: Any): Boolean =  b match {
      case x: Seq[_] =>
        val matched =  x.collect {
          case element: Double => element
        }
        matched.length == a.length && (matched zip a).forall{ case (x1, x2) => Math.abs(x1 - x2) <= DefaultPrecision }
      case _ => false
    }
  }
}
