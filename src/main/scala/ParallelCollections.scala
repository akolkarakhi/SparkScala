object ParallelCollections extends App {

  val a = Array(4,2,7,3,9,1)
  println(a.aggregate(0)(_+_,_+_))

}
