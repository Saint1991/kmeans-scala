package com.github.saint1991.vector

import com.github.saint1991._
import org.scalatest.{Matchers, WordSpec}

class VectorReprSpec extends WordSpec with Matchers {

  final val Precision = 1.0E-5
  implicit def mapToVector(x: Seq[Double]): Seq[Double] = identity(x)
  implicit val measureDistance: (Seq[Double], Seq[Double]) => Double = EuclideanSpace.distance
  implicit def toVectorRepr(x: Seq[Double]): VectorRepr[Seq[Double]] = VectorRepr[Seq[Double]](x)

  "+ operator" should {
    "result a new vector that is the sum of 2 vectors" when {
      "2 vectors have the same degree" taggedAs UnitTest in {
        val x1 = Seq(1.1 , 2.1)
        val x2 = Seq(3.2, -4.0)
        val actual = x1 + x2
        actual should equal (Seq(4.3, -1.9))
      }
    }
    "assume non exist dimension to be 0.0" when {
      "the degree of 2 vectors are different" taggedAs UnitTest in {
        val x1 = Seq(1.1 , 2.1)
        val x2 = Seq(3.2, -4.0, 5.5)
        val actual = x1 + x2
        actual should equal (Seq(4.3, -1.9, 5.5))
      }
    }
  }


  "- operator" should {
    "result a new vector that is the difference of 2 vectors" when {
      "2 vectors have the same degree" taggedAs UnitTest in {
        val x1 = Seq(1.1 , 2.1)
        val x2 = Seq(3.2, -4.0)
        val actual = x1 - x2
        actual should equal ( Seq(-2.1, 6.1)  )
      }
    }
    "assume non exist dimension to be 0.0" when {
      "the degree of 2 vectors are different" taggedAs UnitTest in {
        val x1 = Seq(1.1 , 2.1)
        val x2 = Seq(3.2, -4.0, 5.5)
        val actual = x1 - x2
        actual should equal (Seq(-2.1, 6.1, -5.5))
      }
    }
  }


  "* operator" should {
    "result a multiplied vector" when {
      "a constant is given as an operand" taggedAs UnitTest in {
        val x1 = Seq(1.1, 2.1)
        val actual = x1 * 4.0
        actual should equal ( Seq(4.4, 8.4) )
      }
    }
    "result a inner product" when {
      "another vector is given as an operand" taggedAs UnitTest in {
        val x1 = Seq(1.1 , 2.1)
        val x2 = Seq(3.2, -4.0)
        val actual = x1 * x2
        actual should equal (-4.88)
      }
    }
  }


  "/ operator" should {
    "result a divided vector by a given operand" taggedAs UnitTest in {
      val x1 = Seq(1.4, 2.2)
      val actual = x1 / 2.0
      actual should equal ( Seq(0.7, 1.1) )
    }
  }


  "distanceFrom method" should {
    "result a distance from another vector" when {
      "2 vectors have the same degree" taggedAs UnitTest in {
        val x1 = Seq(1.0, 0.0)
        val x2 = Seq(0.0, 1.0)
        val actual = x1 distanceFrom x2
        actual should equal(Math.sqrt(2.0))
      }
    }
    "assume non exist dimension to be 0.0" when {
      "the degree of 2 vectors are different" taggedAs UnitTest in {
        val x1 = Seq(1.0 , 0.0)
        val x2 = Seq(0.0, 2.0, 2.0)
        val actual =  x1 distanceFrom x2
        actual should equal (3.0)
      }
    }
  }

}
