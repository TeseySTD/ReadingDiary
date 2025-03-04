package com.example.readingdiary

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.readingdiary.ui.AddBookDialog
import com.example.readingdiary.ui.AddNoteDialog
import com.example.readingdiary.ui.BooksFragment
import com.example.readingdiary.ui.NotesFragment
import com.example.readingdiary.ui.PlansFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        fabAdd = findViewById(R.id.fabAdd)
        viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Read books"
                1 -> "Notes"
                else -> "Plans of future reading"
            }
        }.attach()

        findViewById<View>(R.id.fabAdd).setOnClickListener {
            when (viewPager.currentItem) {
                0 -> showAddBookDialog()
                1 -> showAddNoteDialog()
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                fabAdd.visibility = if (position == 2) View.GONE else View.VISIBLE
            }
        })
    }

    private fun showAddBookDialog() {
        AddBookDialog().show(supportFragmentManager, "AddBookDialog")
    }

    private fun showAddNoteDialog() {
        AddNoteDialog().show(supportFragmentManager, "AddNoteDialog")
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