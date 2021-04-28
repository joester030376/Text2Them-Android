package com.app.text2them.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.text2them.R
import com.app.text2them.fragment.*
import com.app.text2them.utils.MySharedPreferences
import com.bmd.mybmd.api.UrlConstant
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.drawer_menu.*
import java.util.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment.javaClass.simpleName)
            .commit()
        txtTitle.text = getString(R.string.home)

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, R.string.Open, R.string.Close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val header: View = navigationView.getHeaderView(0)

        val ivProfile = header.findViewById<CircleImageView>(R.id.ivProfile)
        val ivClose = header.findViewById<ImageView>(R.id.ivClose)
        val txtName = header.findViewById<TextView>(R.id.txtName)
        val txtCity = header.findViewById<TextView>(R.id.txtCity)

        Glide.with(this@HomeActivity).load(MySharedPreferences.getMySharedPreferences()!!.userImage)
            .into(ivProfile)
        txtName.text=MySharedPreferences.getMySharedPreferences()!!.userName

        ivDrawer.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        ivClose.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
        }

        llChangePassword.setOnClickListener {
            val fragment = ChangePasswordFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
            txtTitle.text = getString(R.string.changePassword)
            drawer.closeDrawer(GravityCompat.START)
        }

        llPrivacy.setOnClickListener {
            val fragment = PrivacyFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
            txtTitle.text = getString(R.string.privacyPolicy)
            drawer.closeDrawer(GravityCompat.START)
        }
        llLogout.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            dialogLogout()
        }

    }
    private fun dialogLogout() {
        AlertDialog.Builder(Objects.requireNonNull(this))
            .setMessage(getString(R.string.are_logout))
            .setPositiveButton(getString(R.string.yes)) { dialogInterface, i ->
                dialogInterface.dismiss()

                val mySharedPreferences = MySharedPreferences.getMySharedPreferences()
                mySharedPreferences!!.isLogin = false
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .setNegativeButton(getString(R.string.no)) { dialogInterface, i -> dialogInterface.dismiss() }
            .show()
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navHome -> {
                    val fragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    txtTitle.text = getString(R.string.home)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navUsers -> {
                    val fragment = UsersFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    txtTitle.text = getString(R.string.users)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navMessage -> {

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navProfile -> {
                    val fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    txtTitle.text = getString(R.string.profile)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}