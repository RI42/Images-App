package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.MainActivityBinding
import com.example.myapplication.ui.MainNavigationFragment
import com.example.myapplication.ui.NavigationHost
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationHost {

    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(R.id.catFragment, R.id.dogFragment)
    }

    private val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)

    private var currentNavController: LiveData<NavController>? = null

    private lateinit var binding: MainActivityBinding
    private var navFragment: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
//        binding.bottomNav.applySystemWindowInsetsToPadding(bottom = true)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds = listOf(R.navigation.cats, R.navigation.dogs)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this) { navController ->
            navFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
            findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)
        }
        currentNavController = controller
    }

    private fun getCurrentFragment(): MainNavigationFragment? = navFragment
        ?.childFragmentManager
        ?.primaryNavigationFragment as? MainNavigationFragment

    override fun onUserInteraction() {
        super.onUserInteraction()
        getCurrentFragment()?.onUserInteraction()
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        currentNavController?.value?.let {
            toolbar.setupWithNavController(it, appBarConfiguration)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp(appBarConfiguration) ?: false
    }
}