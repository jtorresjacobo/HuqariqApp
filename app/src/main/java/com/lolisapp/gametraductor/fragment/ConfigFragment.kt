package com.lolisapp.gametraductor.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import com.lolisapp.gametraductor.R
import com.lolisapp.gametraductor.bean.User
import com.lolisapp.gametraductor.database.DataBaseService
import com.lolisapp.gametraductor.util.Constants
import com.lolisapp.gametraductor.util.session.SessionManager
import kotlinx.android.synthetic.main.fragment_config.*
import kotlinx.android.synthetic.main.fragment_config.view.*


class ConfigFragment : Fragment() {


    private lateinit var user:User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_config, container, false)
        user=DataBaseService.getInstance(activity).getUser(SessionManager.getInstance(activity).userLogged.email)
        view.etRegion.setText(user.region)
        view.etInstitution.setText(user.institution)
        view.cbEnabled.setOnCheckedChangeListener { compoundButton, b -> updateEditText(view,b) }
        view.btSaveCongiguration.setOnClickListener({saveCongiguration()})
        if (user.isMember==Constants.IS_MEMBER){view.cbEnabled.isChecked=true
            view.tvTextChek.setText("HABILITADO")
        }
        return view


    }

    private fun saveCongiguration() {
        if (cbEnabled.isChecked){
            if (etRegion.text.toString()==null||etRegion.text.toString()==""){
                showError(etRegion,"Dato inválido")
                return
            }
            if (etInstitution.text.toString()==null||etInstitution.text.toString()==""){
                showError(etInstitution,"Dato inválido")
                return
            }
            DataBaseService.getInstance(activity).editMember(etInstitution.text.toString(),etRegion.text.toString(),
                    Constants.IS_MEMBER,user.email)
            Toast.makeText(activity,"DATOS GUARDADOS",Toast.LENGTH_LONG).show()

        }else{
            DataBaseService.getInstance(activity).editMember(etInstitution.text.toString(),etRegion.text.toString(),
                    0,user.email)
            Toast.makeText(activity,"DATOS GUARDADOS",Toast.LENGTH_LONG).show()

        }

    }


    private fun showError(editText: EditText, message:String){
        Toast.makeText(context, message , Toast.LENGTH_LONG).show()
        editText.setError(message)
        editText.setFocusable(true)
        editText.requestFocus()
    }

    private fun updateEditText(view:View,condition:Boolean){
        if (condition)view.tvTextChek.setText("HABILITADO") else view.tvTextChek.setText("DESHABILITADO")
        view.etInstitution.isEnabled=condition
        view. etRegion.isEnabled=condition
    }







}
