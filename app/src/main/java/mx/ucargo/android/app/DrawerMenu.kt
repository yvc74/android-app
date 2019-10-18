package mx.ucargo.android.app

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.driver_profile_activity.*
import kotlinx.android.synthetic.main.nav_header_menu.view.*
import kotlinx.android.synthetic.main.nav_view.*
import kotlinx.android.synthetic.main.toolbar.*
import mx.ucargo.android.R
import mx.ucargo.android.editprofile.EditProfileActivity
import mx.ucargo.android.editprofile.Profile
import mx.ucargo.android.orderlist.OrderListActivity
import mx.ucargo.android.signin.SignInActivity

fun AppCompatActivity.onNavigationItemSelectedListener(drawerLayout: DrawerLayout) = NavigationView.OnNavigationItemSelectedListener {
    when (it.itemId) {
        R.id.nav_cotizaciones -> {
            finish()
            navView.setCheckedItem(it.itemId)
            startActivity(OrderListActivity.newIntent(this,0))
        }
        R.id.nav_asignados -> {
            finish()
            navView.setCheckedItem(it.itemId)
            startActivity(OrderListActivity.newIntent(this,1))
        }
        R.id.nav_historial -> {
            finish()
            navView.setCheckedItem(it.itemId)
            startActivity(OrderListActivity.newIntent(this,2))
        }
        R.id.nav_logout -> {
            finish()
            val intent = SignInActivity.newIntent(this, signOut = true)
            startActivity(intent)
        }
    }
    drawerLayout.closeDrawer(GravityCompat.START)
    true
}


fun AppCompatActivity.setUpDrawer(drawerLayout: DrawerLayout,position :Int) {
    val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_closed)
    drawerLayout.addDrawerListener(toggle)
    toggle.syncState()
    if (position != 5){
        navView.getMenu().getItem(position).setEnabled(false)
        navView.getMenu().getItem(position).setChecked(true);
    }
    // var navViewHeader = navView.findViewById(R.id.navViewHeader) as View
    navView.inflateHeaderView(R.layout.nav_header_menu)

    navView.setNavigationItemSelectedListener(onNavigationItemSelectedListener(drawerLayout))
}

fun AppCompatActivity.setUpMenuHeader(profile: Profile){
    val headerLayout = navView.getHeaderView(0)

    headerLayout.menuNameTextView.text = profile.name
    headerLayout.menuUsernameTextView.text = profile.username
    val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_usuario_default_image_profile)
            .circleCrop()

    Glide.with(this)
            .load(profile.picture)
            .apply(requestOptions)
            .into(headerLayout.menuDriverProfileImageView);
    headerLayout.menuDriverRatingBar.rating = 5.0F
    headerLayout.menuDriverProfileImageView.setOnClickListener {
        finish()
        val intent = EditProfileActivity.newIntent(this)
        startActivity(intent)
    }
}


fun AppCompatActivity.drawerMenuOnBackPressed(callback: (() -> Unit)? = null) {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
        drawerLayout.closeDrawer(GravityCompat.START)
    } else {
        callback?.invoke()
    }
}
