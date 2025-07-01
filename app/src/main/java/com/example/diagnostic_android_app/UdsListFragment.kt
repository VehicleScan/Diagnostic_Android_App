package com.example.diagnostic_android_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView

class UdsListFragment : Fragment() {
    private val items = listOf(
        UdsItem(1, "Speed Sensor", "Monitors speed", R.drawable.carspeed1),
        UdsItem(2, "Oil Temp Sensor", "Tracks oil temp", R.drawable.thermometer1),
        UdsItem(3, "MAF Sensor", "Measures airflow", R.drawable.airflow1)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_uds_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.uds_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = UdsAdapter(items) { itemId ->
            findNavController().navigate(
                R.id.action_uds_list_to_uds_detail,
                Bundle().apply { putInt("itemId", itemId) }
            )
        }
    }
}

class UdsAdapter(
    private val items: List<UdsItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<UdsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val name: TextView = view.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_uds, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconRes)
        holder.name.text = item.name
        holder.itemView.setOnClickListener { onItemClick(item.id) }
    }

    override fun getItemCount(): Int = items.size
}