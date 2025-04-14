package com.example.readingdiary

import android.os.Bundle
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
import com.example.readingdiary.ui.compose.LoginFragment
import com.example.readingdiary.ui.compose.RegistrationFragment
import com.example.readingdiary.ui.compose.HomeFragment
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
                FragmentPositions.HomeFragment.ordinal -> "Home"
                FragmentPositions.BooksFragment.ordinal -> "Read books"
                FragmentPositions.NotesFragment.ordinal -> "Notes"
                FragmentPositions.PlansFragment.ordinal -> "Plans"
                FragmentPositions.LoginFragment.ordinal -> "Login"
                FragmentPositions.RegistrationFragment.ordinal -> "Register"
                else -> ""
            }
        }.attach()

        findViewById<View>(R.id.fabAdd).setOnClickListener {
            when (viewPager.currentItem) {
                FragmentPositions.BooksFragment.ordinal -> showAddBookDialog()
                FragmentPositions.NotesFragment.ordinal -> showAddNoteDialog()
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                fabAdd.visibility = if (position in
                    FragmentPositions.BooksFragment.ordinal .. FragmentPositions.NotesFragment.ordinal)
                    View.VISIBLE else View.GONE
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

enum class FragmentPositions{
    HomeFragment,
    BooksFragment,
    NotesFragment,
    PlansFragment,
    LoginFragment,
    RegistrationFragment,
}

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FragmentPositions.HomeFragment.ordinal -> HomeFragment()
            FragmentPositions.BooksFragment.ordinal -> BooksFragment()
            FragmentPositions.NotesFragment.ordinal -> NotesFragment()
            FragmentPositions.PlansFragment.ordinal -> PlansFragment()
            FragmentPositions.LoginFragment.ordinal -> LoginFragment()
            FragmentPositions.RegistrationFragment.ordinal -> RegistrationFragment()
            else -> HomeFragment()
        }
    }
}