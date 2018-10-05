package com.lolisapp.gametraductor.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.lolisapp.gametraductor.R
import com.lolisapp.gametraductor.fragment.RecordAudioFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.R.string.cancel
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.lolisapp.gametraductor.fragment.ConfigFragment
import com.lolisapp.gametraductor.util.session.SessionManager


class MainActivity : AppCompatActivity() {

    private var idCurrentFragment: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        defaultFragment()
        //disableShiftMode(bottomNavigation)
        var fragment: Fragment? = null
        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            fragment = when (item.itemId) {
                idCurrentFragment -> null
                R.id.record_option -> {
                    item.setChecked(true)
                    idCurrentFragment = R.id.record_option
                    supportFragmentManager.findFragmentByTag("record")
                    RecordAudioFragment()

                }

                R.id.config_option -> {
                    idCurrentFragment = R.id.config_option
                    ConfigFragment()
                }

                R.id.close_option->{
                    showDialogCloseSession()
                    null
                }



                else -> null
            }
            fragment.toString()

            transitionFragment(fragment)
        }

    }





    private fun defaultFragment() {
        val fragmentToView = RecordAudioFragment()
        transitionFragment(fragmentToView)
        bottomNavigation.selectedItemId = 0

    }


    private fun transitionFragment(fragment: Fragment?): Boolean {
        if (fragment == null) return false
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentTransition, fragment, "Fragment")
        transaction.addToBackStack(fragment.tag)
        transaction.commit()
        return true
    }

    @SuppressLint("RestrictedApi")
    private fun disableShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShiftingMode(false)
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) { }
        catch (e: IllegalAccessException) { }

    }

    private fun changeIdOption(id: Int) {
        idCurrentFragment = id

    }


    private fun showDialogCloseSession(){

        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage("¿Desea cerrar la sesión?")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
                "Si",
                DialogInterface.OnClickListener { dialog, id ->
                    finish()
                    SessionManager.getInstance(this).logoutApp()
                    dialog.cancel() })

        builder1.setNegativeButton(
                "No",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11 = builder1.create()
        alert11.show()
    }




}



