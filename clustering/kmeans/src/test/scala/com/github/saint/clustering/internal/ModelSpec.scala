package com.github.saint.clustering.internal

import com.github.saint._
import com.github.saint.UnitTest
import com.github.saint.clustering.internal.Clusters.ClusterId
import org.scalatest.{Matchers, WordSpec}

import scala.language.implicitConversions

object ModelSpec {
  type Element = Seq[Double]
  implicit def toVector(x: Element): Element = x
  implicit val vectorSize = 2
  val members1 = Seq(
    Seq(1.0, 2.0),
    Seq(3.0, 2.0),
    Seq(-1.0, 8.0)
  )
  val members2 = Seq(
    Seq(3.0, 4.5),
    Seq(-1.0, 2.5)
  )
  val assignment: Seq[ClusterId] =
    members1.map(_ => 0) ++ members2.map(_ => 1)
}


class ClusterSpec extends WordSpec with Matchers {
  import ModelSpec._

  "mean method" should {
    "calculate the mean of members" when {
      "all elements have the same degree" taggedAs UnitTest in {
        val cluster = Cluster[Element](members1)
        val actual = cluster.mean
        actual should equal ( Seq(1.0, 4.0) )
      }
    }
    "throw an IllegalArgumentException" when {
      "members have different degree" taggedAs UnitTest in {
        val cluster = Cluster[Element](Seq(
          Seq(1.0, 2.0),
          Seq(3.0, 4.0),
          Seq(-1.0)
        ))
        an [IllegalArgumentException] should be thrownBy cluster.mean
      }
    }
  }

}


class ClustersSpec extends WordSpec with Matchers {
  import ModelSpec._

  "Clusters class" should {
    "be constructed with elements and assignments" taggedAs UnitTest in {
      val actual = Clusters(members1 ++ members2, assignment)
      actual.clusters.length should equal (2)
      actual.clusters(0).elements should contain theSameElementsAs members1
      actual.clusters(1).elements should contain theSameElementsAs members2
    }
  }

  "empty method" should {
    "create empty clusters" taggedAs UnitTest in {
      val actual = Clusters.empty[Seq[Double]]
      actual.clusters shouldBe empty
    }
  }

  "means method" should {
    "calculates the mean of each cluster" taggedAs UnitTest in {
      val actual = Clusters(members1 ++ members2, assignment).means
      actual should equal (Seq(
        Seq(1.0, 4.0),
        Seq(1.0, 3.5)
      ))
    }
  }

}
