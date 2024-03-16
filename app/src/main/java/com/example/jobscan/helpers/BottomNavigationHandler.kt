package com.example.jobscan.helpers
import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.example.jobscan.CandidateActivity
import com.example.jobscan.DetailActivity
import com.example.jobscan.MainActivity
import com.example.jobscan.ProfileActivity
import com.example.jobscan.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationHandler(private val context: Context) {
    fun onNavigationItemSelected(item: Int): Boolean {
        val currentActivity = context.javaClass
        when (item) {
            R.id.navigation_home -> {
                if (currentActivity != MainActivity::class.java) {
                    context.startActivity(Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                return true
            }
            R.id.navigation_connections -> {
                if (currentActivity != CandidateActivity::class.java) {
                    context.startActivity(Intent(context, CandidateActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                return true
            }
            R.id.navigation_profile -> {
                if (currentActivity != ProfileActivity::class.java) {
                    context.startActivity(Intent(context, ProfileActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                return true
            }
        }
        return false
    }


    fun selectBottomNavigationItem(bottomNavigationView: BottomNavigationView, @IdRes itemId: Int) {
        bottomNavigationView.selectedItemId = itemId
    }
}