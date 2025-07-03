package com.example.diagnostic_android_app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class UdsDetailFragment : Fragment(R.layout.fragment_uds_detail) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemId = arguments?.getInt("itemId") ?: 1
        val item = UdsData.items.first { it.id == itemId }
        view.findViewById<TextView>(R.id.name)?.text = item.name
        view.findViewById<TextView>(R.id.details)?.text = item.details
        view.findViewById<Button>(R.id.back_button)
            ?.setOnClickListener { findNavController().navigate(R.id.action_uds_detail_to_uds_list) }
    }
}