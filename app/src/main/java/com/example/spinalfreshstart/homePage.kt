package com.example.spinalfreshstart

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spinalfreshstart.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.DataPointInterface
import com.jjoe64.graphview.series.LineGraphSeries

/**
 * A simple [Fragment] subclass.
 * Use the [homePage.newInstance] factory method to
 * create an instance of this fragment.
 */

class homePage : Fragment() {
    //homeBranch created
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

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

        val timerOne = view.findViewById<Chronometer>(R.id.chronometerOne)
        val timerTwo = view.findViewById<Chronometer>(R.id.chronometerTwo)

        val startTimerOne = view.findViewById<Button>(R.id.startbuttonTimerOne)
        val stopTimerOne = view.findViewById<Button>(R.id.stopbuttonTimerOne)
        val startTimerTwo = view.findViewById<Button>(R.id.startbuttonTimerTwo)
        val stopTimerTwo = view.findViewById<Button>(R.id.stopbuttonTimerTwo)


        startTimerOne.setOnClickListener {

            timerOne.base = SystemClock.elapsedRealtime()
            timerOne.start()
        }
        stopTimerOne.setOnClickListener {
            timerOne.stop()
        }
        startTimerTwo.setOnClickListener {
            timerTwo.base = SystemClock.elapsedRealtime()
            timerTwo.start()
        }
        stopTimerTwo.setOnClickListener {
            timerTwo.stop()
        }

        val series1: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                DataPoint(0.0, 10.3),
                DataPoint(0.2, 9.7),
                DataPoint(0.4, 9.0),
                DataPoint(0.6, 8.3),
                DataPoint(0.8, 7.6),
                DataPoint(1.0, 7.0),
                DataPoint(1.2, 6.4),
                DataPoint(1.4, 5.9),
                DataPoint(1.6, 5.6),
                DataPoint(1.8, 5.1),
                DataPoint(2.0, 4.4),
                DataPoint(2.2, 3.6),
                DataPoint(2.4, 2.9),
                DataPoint(2.6, 2.4),
                DataPoint(2.8, 2.2),
                DataPoint(3.0, 2.1),
                DataPoint(3.2, 2.0),
                DataPoint(3.4, 1.9),
                DataPoint(3.6, 2.2),
                DataPoint(3.8, 3.0),
                DataPoint(4.0, 4.4),
                DataPoint(4.2, 5.8),
                DataPoint(4.4, 7.1),
                DataPoint(4.6, 8.3),
                DataPoint(4.8, 9.4),
                DataPoint(5.0, 10.3),
                DataPoint(5.2, 0.0),
                DataPoint(5.4, 15.3),
                DataPoint(5.6, 16.3),
                DataPoint(5.8, 17.0),
                DataPoint(6.0, 17.5),
                DataPoint(6.2, 18.5),
                DataPoint(6.4, 19.1),
                DataPoint(6.6, 20.3),
                DataPoint(6.8, 21.4),
                DataPoint(7.0, 22.4),
                DataPoint(7.2, 23.6),
                DataPoint(7.4, 24.7),
                DataPoint(7.6, 26.1),
                DataPoint(7.8, 27.4),
                DataPoint(8.0, 28.8),
                DataPoint(8.2, 30.1),
                DataPoint(8.4, 31.5),
                DataPoint(8.6, 33.1),
                DataPoint(8.8, 34.5),
                DataPoint(9.0, 35.7),
                DataPoint(9.2, 37.1),
                DataPoint(9.4, 38.4),
                DataPoint(9.6, 39.8),
                DataPoint(9.8, 41.2),
                DataPoint(10.0, 42.8),
                DataPoint(10.2, 44.4),
                DataPoint(10.4, 45.9),
                DataPoint(10.6, 47.4),
                DataPoint(10.8, 48.8),
                DataPoint(11.0, 50.3),
                DataPoint(11.2, 51.9),
                DataPoint(11.4, 53.4),
                DataPoint(11.6, 54.6),
                DataPoint(11.8, 55.8),
                DataPoint(12.0, 56.8),
                DataPoint(12.2, 57.8),
                DataPoint(12.4, 58.7),
                DataPoint(12.6, 59.5),
                DataPoint(12.8, 60.2),
                DataPoint(13.0, 60.8),
                DataPoint(13.2, 61.2),
                DataPoint(13.4, 61.7),
                DataPoint(13.6, 62.0),
                DataPoint(13.8, 62.2),
                DataPoint(14.0, 62.5),
                DataPoint(14.2, 62.9),
                DataPoint(14.4, 63.0),
                DataPoint(14.6, 62.8),
                DataPoint(14.8, 62.6),
                DataPoint(15.0, 62.3),
                DataPoint(15.2, 61.9),
                DataPoint(15.4, 61.3),
                DataPoint(15.6, 60.4),
                DataPoint(15.8, 59.1),
                DataPoint(16.0, 57.6),
                DataPoint(16.2, 55.5),
                DataPoint(16.4, 53.5),
                DataPoint(16.6, 51.4),
                DataPoint(16.8, 49.0),
                DataPoint(17.0, 46.6),
                DataPoint(17.2, 44.0),
                DataPoint(17.4, 41.4),
                DataPoint(17.6, 38.8),
                DataPoint(17.8, 36.3),
                DataPoint(18.0, 34.0),
                DataPoint(18.2, 32.0),
                DataPoint(18.4, 29.9),
                DataPoint(18.6, 27.9),
                DataPoint(18.8, 26.0),
                DataPoint(19.0, 24.1),
                DataPoint(19.2, 22.1),
                DataPoint(19.4, 19.9),
                DataPoint(19.6, 17.7),
                DataPoint(19.8, 15.5),
                DataPoint(20.0, 13.3),
                DataPoint(20.2, 10.9),
                DataPoint(20.4, 8.7),
                DataPoint(20.6, 6.4),
                DataPoint(20.8, 4.4),
                DataPoint(21.0, 2.5),
                DataPoint(21.2, 1.3),
                DataPoint(21.4, 2.0),
                DataPoint(21.6, 3.8),
                DataPoint(21.8, 5.5),
                DataPoint(22.0, 7.2),
                DataPoint(22.2, 8.8),
                DataPoint(22.4, 10.5),
                DataPoint(22.6, 12.1),
                DataPoint(22.8, 13.8),
                DataPoint(23.0, 15.2),
                DataPoint(23.2, 16.4),
                DataPoint(23.4, 17.3),
                DataPoint(23.6, 18.0),
                DataPoint(23.8, 18.4),
                DataPoint(24.0, 18.3),
                DataPoint(24.2, 18.4),
                DataPoint(24.4, 18.4),
                DataPoint(24.6, 18.5),
                DataPoint(24.8, 18.5),
                DataPoint(25.0, 18.5)
            )
        )

        val max = series1.highestValueY;
        val min = series1.lowestValueY;
        val harmfulAngle = (max-min)*(0.8)+min;

        fun drawStraightLineFromYAxisValue(graphView: GraphView, angle: Float) {
            val lineGraphSeries = LineGraphSeries<DataPoint>()
            val harmfulAngleDouble = angle.toDouble()
            val startPoint = DataPoint(series1.lowestValueX,  harmfulAngleDouble)
            val endPoint = DataPoint(series1.highestValueX,  harmfulAngleDouble)
            lineGraphSeries.appendData(startPoint, true, 2)
            lineGraphSeries.appendData(endPoint, true, 2)
            graphView.addSeries(lineGraphSeries)
        }

        drawStraightLineFromYAxisValue(lineGraphView, harmfulAngle.toFloat())

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

        println("harmfulAngleCount is $count1")
        println("totalCount is $totalCount")
        println("ratio is $ratio")
        println("Harmful angle duration: "+ harmfulAngleDuration +" out of "+ series1.highestValueX)

        val angleTextView = view?.findViewById<TextView>(R.id.textHarmfulAngle)
        angleTextView?.text= harmfulAngleDuration.toString()


        lineGraphView.animate()
        lineGraphView.viewport.isScrollable = true
        lineGraphView.viewport.isScalable = true
        lineGraphView.viewport.setScalableY(true)
        lineGraphView.viewport.setScrollableY(true)
        series1.color = R.color.purple_200
        lineGraphView.addSeries(series1)

        // Inflate the layout for this fragment
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