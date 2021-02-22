package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.MainActivityBinding
import com.example.myapplication.ui.MainNavigationFragment
import com.example.myapplication.ui.NavigationHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationHost {

    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(R.id.mainFragment)
    }

    private val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    private lateinit var navFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        navFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navFragment.navController
    }

    private fun getCurrentFragment(): MainNavigationFragment? = navFragment
        .childFragmentManager
        .primaryNavigationFragment as? MainNavigationFragment

    override fun onUserInteraction() {
        super.onUserInteraction()
        getCurrentFragment()?.onUserInteraction()
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}