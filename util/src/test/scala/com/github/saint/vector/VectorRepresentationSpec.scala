package com.github.saint.vector

import com.github.saint._
import com.github.saint.UnitTest
import org.scalatest.{Matchers, WordSpec}

class VectorRepresentationSpec extends WordSpec with Matchers {

  import com.github.saint.vector.dsl._
  final val Precision = 1.0E-5

  "+ operator" should {
    "result a new vector that is the sum of 2 vectors" when {
      "2 vectors have the same degree" taggedAs UnitTest in {
        val x1 = Seq(1.1 , 2.1)
        val x2 = Seq(3.2, -4.0)
        val actual = x1 + x2
        actual should equal (Seq(4.3, -1.9))
      }
    }
    "throws IllegalArgumentException" when {
      "the degree of 2 vectors are different" taggedAs UnitTest in {
        val x1 = Seq(1.1 , 2.1)
        val x2 = Seq(3.2, -4.0, 5.5)
        an [IllegalArgumentException] should be thrownBy x1 + x2
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
    "throws IllegalArgumentException" when {
      "the degree of 2 vectors are different" taggedAs UnitTest in {
        val x1 = Seq(1.1 , 2.1)
        val x2 = Seq(3.2, -4.0, 5.5)
        an [IllegalArgumentException] should be thrownBy x1 - x2
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


  "distanceTo method" should {
    "result a distance to another vector" when {
      "2 vectors have the same degree" taggedAs UnitTest in {
        val x1 = Seq(1.0 , 0.0)
        val x2 = Seq(0.0, 1.0)
        val actual = x1 distanceTo x2
        actual should equal ( Math.sqrt(2.0)  )
      }
      "the degree of 2 vectors are different" taggedAs UnitTest in {
        val x1 = Seq(1.0 , 0.0)
        val x2 = Seq(0.0, 1.0, 5.5)
        an [IllegalArgumentException] should be thrownBy (x1 distanceTo x2)
      }
    }
  }

}
