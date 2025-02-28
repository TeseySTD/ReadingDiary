package com.example.readingdiary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readingdiary.R
import com.example.readingdiary.models.ReadingPlan
import com.example.readingdiary.repo.ReadingPlanRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlansFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlansAdapter
    private val plansRepository = ReadingPlanRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.plansRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PlansAdapter(
            plans = emptyList(),
            onCompletePlan = { note -> plansRepository.completeReadingPlan(note) },
        )

        recyclerView.adapter = adapter

        plansRepository.getReadingPlansLiveData().observe(viewLifecycleOwner, Observer { notes ->
            recyclerView.post {
                adapter.updatePlans(notes)
            }
        })
    }
}

class PlansAdapter(
    private var plans: List<ReadingPlan>,
    private val onCompletePlan: (ReadingPlan) -> Unit
): RecyclerView.Adapter<PlansAdapter.PlansViewHolder>(){

    class PlansViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.planTitle)
        val planBooks: TextView = itemView.findViewById(R.id.planBooks)
        val readingTime: TextView = itemView.findViewById(R.id.readingTime)
        val completeButton: FloatingActionButton = itemView.findViewById(R.id.completePlanButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan, parent, false)
        return PlansViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlansViewHolder, position: Int) {
        val plan = plans[position]
        holder.titleText.text = plan.title
        holder.planBooks.text = "List of books:\n ${plan.getFormatedBooks().joinToString (separator = "\n")}"
        holder.readingTime.text = "Reading time: ${plan.calculateReadingTime}"

        holder.completeButton.setOnClickListener {
            onCompletePlan(plan)
        }
    }

    override fun getItemCount() = plans .size

    fun updatePlans(newPlans: List<ReadingPlan>) {
        plans = newPlans
        notifyDataSetChanged()
    }

}