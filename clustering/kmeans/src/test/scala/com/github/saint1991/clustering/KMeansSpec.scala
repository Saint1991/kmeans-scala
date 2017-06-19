package com.github.saint1991.clustering

import com.github.saint1991.vector.{EuclideanSpace, UnitTest, VectorRepr}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

import scala.language.implicitConversions

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

  val initializeMirror: PrivateMethod[Seq[ClusterIndex]] = PrivateMethod[Seq[ClusterIndex]]('initialize)
  object TestRandomInitialization extends RandomInitialization[Point]

  // KMeans taking (A, C, E) (B, D) as the initial state
  trait FixedInit[Point] extends Initialization[Point] {
    override def initialize(elements: Seq[VectorRepr[Point]], k: ClusterIndex): Seq[ClusterIndex] =
      elements.indices.map(_ % 2)
  }

  implicit def mapToVector(x: Point): Seq[Double] = identity(x)
  implicit val space = EuclideanSpace

  "RandomInitialization" should {
    "assign the number in range of 0 to k to each element" taggedAs UnitTest in {
      TestRandomInitialization
      val actual = TestRandomInitialization invokePrivate initializeMirror(points, 2)
      all (actual) should (be >= 0 and be <=1)
    }
  }

  "clustering method" should {
    "result in clusters (A, B, C) (D, E)" when {
      "given k = 2" taggedAs UnitTest in {
        val testSubject = new KMeans[Point] with FixedInit[Point]
        val actual = testSubject.clustering(points, 2)
        actual.show()
        actual.clusters.map(cluster => cluster.map(_.self)) should contain theSameElementsAs Seq(
          points.take(3),
          points.takeRight(2)
        )
      }
    }
  }

}
