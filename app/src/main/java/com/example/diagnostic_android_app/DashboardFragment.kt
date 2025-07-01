package com.example.diagnostic_android_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.anastr.speedviewlib.AwesomeSpeedometer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DashboardFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val speedometer1 = view.findViewById<AwesomeSpeedometer>(R.id.speedometer1)
        val speedometer2 = view.findViewById<AwesomeSpeedometer>(R.id.speedometer2)

        speedometer1.post {
            speedometer1.apply {
                minSpeed = MainActivity.config1.minSpeed
                maxSpeed = MainActivity.config1.maxSpeed
                setSpeedometerColor(MainActivity.config1.color)
                trianglesColor = android.graphics.Color.parseColor("#333333")
                indicator.width = 15f
                indicator.color = android.graphics.Color.WHITE
                speedTo(MainActivity.speed1Flow.value, 0)
                withTremble = false
            }
        }

        speedometer2.post {
            speedometer2.apply {
                minSpeed = MainActivity.config2.minSpeed
                maxSpeed = MainActivity.config2.maxSpeed
                setSpeedometerColor(MainActivity.config2.color)
                trianglesColor = android.graphics.Color.parseColor("#333333")
                indicator.width = 15f
                indicator.color = android.graphics.Color.WHITE
                speedTo(MainActivity.speed2Flow.value, 0)
                withTremble = false
            }
        }

        // Collect Flow values and update speedometers in real-time
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                launch {
                    MainActivity.speed1Flow.collect { newSpeed ->
                        speedometer1.speedTo(newSpeed)
                    }
                }
                launch {
                    MainActivity.speed2Flow.collect { newSpeed ->
                        speedometer2.speedTo(newSpeed)
                    }
                }
            }
        }
    }
}