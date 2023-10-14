package com.example.spinalfreshstart


import akka.http.scaladsl.server.RejectionHandler
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spinalfreshstart.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Transaction
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.DataPointInterface
import com.jjoe64.graphview.series.LineGraphSeries
import android.icu.util.DateInterval
import kotlin.system.exitProcess

class homePage : Fragment() {
    //homeBranch created
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private lateinit var mobSend: MobileSender

    private lateinit var lineGraphView: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        lineGraphView = view.findViewById(R.id.graphModel)

      //  val timerOne = view.findViewById<Chronometer>(R.id.chronometerOne)
      //  val timerTwo = view.findViewById<Chronometer>(R.id.chronometerTwo)

//        val startTimerOne = view.findViewById<Button>(R.id.startbuttonTimerOne)
//        val stopTimerOne = view.findViewById<Button>(R.id.stopbuttonTimerOne)
//        val startTimerTwo = view.findViewById<Button>(R.id.startbuttonTimerTwo)
    //    val timer = view.findViewById<Button>(R.id.chronometerTwo)
//
//
//        startTimerOne.setOnClickListener {
//            timerOne.base = SystemClock.elapsedRealtime()
//            timerOne.start()
//        }
//        stopTimerOne.setOnClickListener {
//            timerOne.stop()
//        }
//        startTimerTwo.setOnClickListener {
//            timerTwo.base = SystemClock.elapsedRealtime()
//            timerTwo.start()
//        }
//        stopTimerTwo.setOnClickListener {
//            timerTwo.stop()
//        }

        val modelActivity = ModelActivity()
        mobSend = modelActivity.mobSend
        val series1: LineGraphSeries<DataPoint> = LineGraphSeries()
        var dataIndex = 0
        val handler = Handler(Looper.getMainLooper())
        var time = 0.0
        var maxLimit = modelActivity.sampleAngles2.size - 1

        fun harmfulAngleFun() {
            val max = series1.highestValueY;
            val min = series1.lowestValueY;
            var harmfulAngle = (max-min)*(0.8)+min;
            mobSend.sendMessage("/harmfulAngle",harmfulAngle.toFloat().toString().toByteArray())
            var harmfulAngleMyRef: DatabaseReference = modelActivity.database.getReference("harmfulAngle")
            harmfulAngleMyRef.setValue(harmfulAngle)

            //harmful angle
            val harmfulAngleTextView1 = view?.findViewById<TextView>(R.id.harmfulAngleNew)
            val floatHarmfulAngle = String.format("%.2f", harmfulAngle.toFloat()).toFloat()
            harmfulAngleTextView1?.text= floatHarmfulAngle.toString()

            //max angle
            val maxAngleTextView = view?.findViewById<TextView>(R.id.maxAngleValue)
            val floatMaxAngle = String.format("%.2f", max.toFloat()).toFloat()
            maxAngleTextView?.text= floatMaxAngle.toString()

            //min angle
            val minAngleTextView = view?.findViewById<TextView>(R.id.minAngleValue)
            val floatMinAngle = String.format("%.2f", min.toFloat()).toFloat()
            minAngleTextView?.text= floatMinAngle.toString()




            val values = series1.getValues(min,max)
            var count1 = 0.0
            var totalCount= 0.0
            for (dataPoint in  values) {
                val yValue = dataPoint.y // Get the y-value of the data point
                if(yValue>harmfulAngle){
                    println("y-Value is $yValue")
                    count1 += 1.0
                    totalCount++
                } else {
                    totalCount++
                    continue
                }
            }

            var ratio = count1/totalCount
            var harmfulAngleDuration = series1.highestValueX * ratio
            println("Harmful angle: ${harmfulAngle.toFloat()}")
            println("harmfulAngleCount is $count1")
            println("totalCount is $totalCount")
            println("ratio is $ratio")
            println("Harmful angle duration: "+ harmfulAngleDuration +" out of "+ series1.highestValueX)
            val floatHarmfulAngleDuration = String.format("%.2f", harmfulAngleDuration.toFloat()).toFloat()
            val angleTextView = view?.findViewById<TextView>(R.id.textHarmfulAngle)
            angleTextView?.text= floatHarmfulAngleDuration.toString()
        }

        val newRun = object : Runnable {
            override fun run(){
                if(dataIndex != modelActivity.sampleAngles2.size) {
                    val angleData = modelActivity.sampleAngles2[dataIndex]
                    val dataPoint = DataPoint(time, angleData.toDouble())
                    series1.appendData(dataPoint, true, maxLimit)
                    println("working!")
                    lineGraphView.addSeries(series1)

                    harmfulAngleFun()
                    dataIndex += 1
                    time += 0.2
                    handler.postDelayed(this, 50)
                } else {

                    val maximum = series1.highestValueY;
                    val minimum = series1.lowestValueY;
                    val harmfulAngle = (maximum-minimum)*(0.8)+minimum;

                    fun drawStraightLineFromYAxisValue(graphView: GraphView, angle: Float) {
                        val lineGraphSeries = LineGraphSeries<DataPoint>()
                        val harmfulAngleDouble = angle.toDouble()
                        val startPoint = DataPoint(series1.lowestValueX,  harmfulAngleDouble)
                        val endPoint = DataPoint(series1.highestValueX,  harmfulAngleDouble)
                        lineGraphSeries.color = Color.RED
                        lineGraphSeries.appendData(startPoint, true, 2)
                        lineGraphSeries.appendData(endPoint, true, 2)
                        graphView.addSeries(lineGraphSeries)
                    }

                    drawStraightLineFromYAxisValue(lineGraphView, harmfulAngle.toFloat())

                }
            }
        }

        handler.post(newRun)
        lineGraphView.animate()
        series1.color = R.color.purple_200
        lineGraphView.addSeries(series1)

        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment homePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, param2: String?): homePage {
            val fragment = homePage()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

