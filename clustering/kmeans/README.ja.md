
## k-meansクラスタリング

下記の目的関数を最小化するように、各要素をk個のクラスタに割り振るアルゴリズム．

*$\{x_i \in X\}$* : クラスタリング対象の要素  
*$\{c_i \in C\}$* : クラスタの平均位置

** 目的関数 **  
$argmin_C \sum^X min_j |x_i - c_j|^2$

### アルゴリズム
1. 初期状態: 各要素を適当なクラスタに割り当て
2. 各クラスタの平均位置を計算
3. 各要素を平均位置との距離が最も近いクラスタに再割り当て
4. 目的関数の変化が$d$以下になるまで2, 3を繰り返し


### 使い方
```

// Xはクラスタリング対象の要素の型
val kmeans = new KMeans[X] with RandomInitialization[X]

// 要素Xをベクトル表現に変換する関数を規定する
implicit def mapToVector(x: X): Seq[Double] = ???
// 2要素の距離の定義、多要素の平均位置の算出方法を規定
// ここでは座標空間にユークリッド空間を使用し、距離にユークリッド距離、平均にベクトルの算術平均を利用する．
implicit val space = EuclideanSpace

// クラスタリング対象の要素集合
val xs = ???
val result = kmeans.clustering(xs, k)
```

詳細は[テストコード](./test/scala/com/saint/github/clustering/KMeansSpec.scala)を参照．

### Reference
https://en.wikipedia.org/wiki/K-means_clustering