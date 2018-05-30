package mx.ucargo.android.app

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.driver_profile_activity.*
import kotlinx.android.synthetic.main.nav_view.*
import kotlinx.android.synthetic.main.toolbar.*
import mx.ucargo.android.R
import mx.ucargo.android.orderlist.OrderListActivity
import mx.ucargo.android.signin.SignInActivity

fun AppCompatActivity.onNavigationItemSelectedListener(drawerLayout: DrawerLayout) = NavigationView.OnNavigationItemSelectedListener {
    when (it.itemId) {
        R.id.nav_home -> {
            startActivity(OrderListActivity.newIntent(this))
        }
        R.id.nav_images -> Toast.makeText(this, "Images", Toast.LENGTH_SHORT).show()
        R.id.nav_videos -> Toast.makeText(this, "Videos", Toast.LENGTH_SHORT).show()
        R.id.nav_tools -> {
            finish()
            val intent = SignInActivity.newIntent(this, signOut = true)
            startActivity(intent)
        }
    }
    drawerLayout.closeDrawer(GravityCompat.START)
    true
}


fun AppCompatActivity.setUpDrawer(drawerLayout: DrawerLayout) {
    val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_closed)
    drawerLayout.addDrawerListener(toggle)
    toggle.syncState()
    navView.setNavigationItemSelectedListener(onNavigationItemSelectedListener(drawerLayout))
}


fun AppCompatActivity.drawerMenuOnBackPressed(callback: (() -> Unit)? = null) {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
        drawerLayout.closeDrawer(GravityCompat.START)
    } else {
        callback?.invoke()
    }
}
