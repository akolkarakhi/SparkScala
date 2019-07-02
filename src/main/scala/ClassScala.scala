import org.apache.spark.sql.SparkSession


object ClassScala  {

   def main(args: Array[String]): Unit={

     val collection = List(1,2,3,4,5)
     val sum = collection.foldLeft(5)(_+_)
     println(sum)






  }
}
