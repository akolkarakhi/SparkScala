import PlotTemps.lines
import org.apache.spark.{SparkConf, SparkContext}
import scalafx.application.JFXApp
import swiftvis2.plotting.Plot
import swiftvis2.plotting._
import swiftvis2.plotting.renderer.FXRenderer
import swiftvis2.spark._
import swiftvis2.plotting.{ColorGradient, Plot}

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scalafx.application.JFXApp
import swiftvis2.plotting._
import swiftvis2.plotting.renderer.FXRenderer
import swiftvis2.spark._


object RDDTempData extends JFXApp{
    val conf = new SparkConf().setAppName("FirstExample").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val lines= sc.textFile("MN212142_9392.csv").filter(!_.contains("Day"))

    val data= lines.flatMap{line=>
      val p= line.split(",")
      if(p(7)=="." ||p(8)=="."||p(9)==".") Seq.empty else
        Seq(TempData(p(0).toInt,p(1).toInt,p(2).toInt,p(4).toInt,
          TempData.toDoubleOrNeg(p(5)),TempData.toDoubleOrNeg(p(6)),p(7).toDouble,p(8).toDouble,p(9).toDouble))
    }.cache()

    println(data.max()(Ordering.by(_.tmax)))

    println(data.reduce((td1,td2)=>if(td1.tmax>=td2.tmax) td1 else td2))

    val rainyCount = data.filter(_.precip >= 1.0).count()
    println(s"There are $rainyCount rainy days. There is ${rainyCount * 100.0/data.count()} percent ")

    val (rainySum, rainyCount2) = data.aggregate(0.0 -> 0)({
      case ((sum,cnt),td)=>
        if(td.precip < 1.0) (sum,cnt) else (sum+td.tmax,cnt+1)
    },{
      case((s1,c1),(s2,c2))=>
        (s1+s2,c1+c2)
    })
    println(rainySum)
    println(rainyCount2)
    println(s"Average Rainy temp is ${rainySum/rainyCount2}")

    //FlatMap method
    val rainyTemps = data.flatMap(td => if(td.precip <1.0) Seq.empty else Seq(td.tmax))
    println(s"Average Rainy temp is ${rainyTemps.sum/rainyTemps.count}")

    val monthGroup = data.groupBy(_.month)
    val monthlyHighTemp = monthGroup.map {
      case (m, days) => {
        m -> (days.foldLeft(0.0)((sum, td) => sum + td.tmax) / days.size,0xffff0000,5)


      }
    }

  val monthlyLowTemp = monthGroup.map {
    case (m, days) =>
      m -> (days.foldLeft(0.0)((sum, td) => sum + td.tmin) / days.size,0xff00ff00,5)
  }
    monthlyHighTemp.collect().sortBy(_._1) foreach println

   /* val plot = {
      Plot.scatterPlots(Seq(high1, high2, RedARGB, 5), "Temps", "Month", "Temperature")
    }
*/
  val tup1 = (doubles(monthlyHighTemp)(_._1),doubles(monthlyHighTemp)(_._2._1),ints(monthlyHighTemp)(_._2._2),doubles(monthlyHighTemp)(_._2._3))
  val tup2 = (doubles(monthlyLowTemp)(_._1),doubles(monthlyLowTemp)(_._2._1),ints(monthlyLowTemp)(_._2._2),doubles(monthlyLowTemp)(_._2._3))
  val plot = Plot.scatterPlots(Seq(tup1,tup2),"Temps","Month","Temperature")

  FXRenderer(plot, 1200, 1200)
}
