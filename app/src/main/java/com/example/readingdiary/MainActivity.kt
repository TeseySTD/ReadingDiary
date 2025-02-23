package com.example.readingdiary

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.readingdiary.ui.BooksFragment
import com.example.readingdiary.ui.NotesFragment
import com.example.readingdiary.ui.PlansFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "My books"
                1 -> "Notes"
                else -> "Plans of future reading"
            }
        }.attach()

        findViewById<View>(R.id.fabAdd).setOnClickListener {
            when (viewPager.currentItem) {
                0 -> showAddBookDialog()
                1 -> showAddNoteDialog()
                2 -> showAddPlanDialog()
            }
        }
    }

    private fun showAddBookDialog() {
        throw NotImplementedError()
    }

    private fun showAddNoteDialog() {
        throw NotImplementedError()
    }

    private fun showAddPlanDialog() {
        throw NotImplementedError()
    }
}

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BooksFragment()
            1 -> NotesFragment()
            else -> PlansFragment()
        }
    }
}