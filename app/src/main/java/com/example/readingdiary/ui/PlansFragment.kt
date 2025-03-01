package com.example.readingdiary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readingdiary.R
import com.example.readingdiary.models.ReadingPlan
import com.example.readingdiary.repo.ReadingPlanRepository
import com.example.readingdiary.ui.compose.ComposePlanItem
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
): RecyclerView.Adapter<PlansAdapter.ComposePlansViewHolder>(){

    inner class ComposePlansViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComposePlansViewHolder {
        val composeView = ComposeView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ComposePlansViewHolder(composeView)
    }

    override fun onBindViewHolder(holder: ComposePlansViewHolder, position: Int) {
        val plan = plans[position]
        holder.composeView.setContent {
            ComposePlanItem(
                plan = plan,
                onCompletePlan = onCompletePlan
            )
        }
    }

    override fun getItemCount() = plans .size

    fun updatePlans(newPlans: List<ReadingPlan>) {
        plans = newPlans
        notifyDataSetChanged()
    }

}