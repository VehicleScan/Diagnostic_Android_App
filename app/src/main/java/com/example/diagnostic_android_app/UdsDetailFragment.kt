package com.example.diagnostic_android_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class UdsDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_uds_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemId = arguments?.getInt("itemId") ?: 1
        val item = listOf(
            UdsItem(1, "Speed Sensor", "Monitors speed", R.drawable.carspeed1),
            UdsItem(2, "Oil Temp Sensor", "Tracks oil temp", R.drawable.thermometer1),
            UdsItem(3, "MAF Sensor", "Measures airflow", R.drawable.airflow1)
        ).first { it.id == itemId }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.details).text = item.details
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            findNavController().navigate(R.id.action_uds_detail_to_uds_list)
        }
    }
}