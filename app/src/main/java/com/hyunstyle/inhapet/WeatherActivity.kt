package com.hyunstyle.inhapet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherViewSensorEventListener
import kotlinx.android.synthetic.main.content_weather.*
import org.jetbrains.anko.AnkoLogger

/**
 * Created by sh on 2018-03-04.
 */

// maybe not need to use
class WeatherActivity : AppCompatActivity(), AnkoLogger {

    lateinit var weatherSensor: WeatherViewSensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_weather)

        weatherSensor = WeatherViewSensorEventListener(this, weather_view)

        //weather_view.fadeOutPercent = progress.toFloat() / seekBar.max.toFloat()
        //weather_view.angle = progress - 90

        weather_view.speed = 1000
        weather_view.emissionRate = 50f
        weather_view.fadeOutPercent = 1f
        weather_view.angle = 0
        weather_view.setWeatherData(PrecipType.RAIN)
    }

    override fun onResume() {
        super.onResume()
        weatherSensor.onResume()
    }

    override fun onPause() {
        super.onPause()
        weatherSensor.onPause()
    }


//                    1 -> setWeatherData(PrecipType.SNOW)
//                    2 -> setWeatherData(PrecipType.CLEAR)



//        weather_label.setFactory {
//            TextView(this).apply {
//                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
//                gravity = Gravity.CENTER
//                setTextSize(TypedValue.COMPLEX_UNIT_SP, 64f)
//                typeface = Typeface.create("sans-serif-thin", Typeface.NORMAL)
//            }
//        }
//
//        fade_out_percent_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                if (fromUser)
//                    weather_view.fadeOutPercent = progress.toFloat() / seekBar.max.toFloat()
//            }
//        })
//
//        angle_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                if (fromUser)
//                    weather_view.angle = progress - 90
//            }
//        })
//
//        speed_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                if (fromUser)
//                    weather_view.speed = progress
//            }
//        })
//
//        emission_rate_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                if (fromUser)
//                    weather_view.emissionRate = progress.toFloat()
//            }
//        })
//
////        orientation_switch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
////            when (isChecked) {
////                true -> weatherSensor.start()
////                false -> weatherSensor.stop()
////            }
////        }
//
//        weather_condition_spinner.adapter = ArrayAdapter.createFromResource(this, R.array.weather_name_list, android.R.layout.simple_spinner_item)
//                .apply {
//                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                }
//        weather_condition_spinner.onItemSelectedListener = object : ReducedOnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                when (position) {
//                    0 -> setWeatherData(PrecipType.RAIN) // Runs at start
//                    1 -> setWeatherData(PrecipType.SNOW)
//                    2 -> setWeatherData(PrecipType.CLEAR)
//                    else -> throw IllegalStateException("Invalid spinner position!")
//                }
//            }
//        }
//
//        github_icon.setOnClickListener {
//            browse("https://github.com/MatteoBattilana/WeatherView")
//        }
//
//        weather_view.fadeOutPercent = 1f
//        weather_view.angle = 0
//

//
//    private fun setWeatherData(weatherData: WeatherData): Unit {
//        weather_view.setWeatherData(weatherData)
//        speed_seekbar.setProgressCompat(weatherData.speed, true)
//        emission_rate_seekbar.setProgressCompat(weatherData.emissionRate.toInt(), true)
//
//        weather_label.setText(getString(
//                when (weatherData.precipType) {
//                    PrecipType.CLEAR -> R.string.sun
//                    PrecipType.RAIN -> R.string.rain
//                    PrecipType.SNOW -> R.string.snow
//                }
//        ))
//    }


}
