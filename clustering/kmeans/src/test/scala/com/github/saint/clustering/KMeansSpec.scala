package com.github.saint.clustering

import com.github.saint.UnitTest
import com.github.saint.clustering.internal.Clusters.ClusterId
import com.github.saint.clustering.internal.{Cluster, Clusters}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}


object TestData {
  type Point = Seq[Double]
  implicit val vectorSize = 2

  /**           |
    *           |
    *    C      | D
    *  B        |   E
    * ----------|----------
    *    A      |
    *           |
    *           |
    *           |
    */
  val points = Seq[Point](
    Seq(-4.0, -1.0), // A
    Seq(-5.0, 1.0),  // B
    Seq(-4.0, 2.0),  // C
    Seq(1.0, 2.0),   // D
    Seq(2.0, 1.0)    // E
  )

}

class KMeansSpec extends WordSpec with Matchers with PrivateMethodTester {

  import TestData._

  // mirror to private/protected methods
  val initializeMirror: PrivateMethod[(Clusters[Point], Seq[Int])] = PrivateMethod[(Clusters[Point], Seq[Int])]('initialize)
  val assignToClusterMirror: PrivateMethod[Seq[Int]] = PrivateMethod[Seq[Int]]('assignToClusters)


  // Sample class for testing
  object TestKMeans extends KMeans[Point] {
    override def toVector(element: Point): Seq[Double] = element
  }

  // KMeans taking (A, C, E) (B, D) as the initial state
  object TestKMeansWithFixedInitialState extends KMeansBase[Seq[Double]] {
    override def toVector(element: Point): Seq[Double] = element
    override def initialize(elements: Seq[Point], k: Int): (Clusters[Point], Seq[ClusterId]) = {
      val assignments: Seq[ClusterId] = elements.indices.map(_ % 2)
      Clusters(points, assignments) -> assignments
    }
  }


  "initialize method" should {
    "create random assignment in the range of 0 to k" taggedAs UnitTest in {
      val actual = TestKMeans invokePrivate initializeMirror(points, 2)
      Some(actual._1.clusters.length) should contain oneOf (1, 2)
      all (actual._2) should (be >= 0 and be <=1)
    }
  }

  "assignToClusters method" should {
    "assign given elements to the cluster that has the nearest mean" taggedAs UnitTest in {
      val means = Seq(
        Seq(-4.5, 0.0),
        Seq(1.5, 1.5)
      )
      val actual = TestKMeans invokePrivate assignToClusterMirror(points, means)
      actual should equal (Seq(0, 0, 0, 1, 1))
    }
  }

  "clustering method" should {
    "result in clusters (A, B, C) (D, E)" when {
      "given k = 2" taggedAs UnitTest in {
        val actual = TestKMeansWithFixedInitialState.clustering(points, 2)
        actual.clusters should contain theSameElementsAs Seq(
          Cluster(points.take(3)),
          Cluster(points.takeRight(2))
        )
      }
    }
  }

}
