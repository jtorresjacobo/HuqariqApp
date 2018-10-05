package com.lolisapp.gametraductor.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.lolisapp.gametraductor.R
import com.lolisapp.gametraductor.asynctask.SyncronizeData
import com.lolisapp.gametraductor.bean.Ubigeo
import com.lolisapp.gametraductor.bean.User
import com.lolisapp.gametraductor.database.DataBaseService
import com.lolisapp.gametraductor.util.Constants
import com.lolisapp.gametraductor.util.Util
import kotlinx.android.synthetic.main.activity_register.*
import java.text.ParseException
import android.content.Intent
import com.lolisapp.gametraductor.asynctask.InternetCheck
import com.lolisapp.gametraductor.client.LoginClient
import com.lolisapp.gametraductor.client.RegistrarUsuarioClient
import com.lolisapp.gametraductor.util.session.SessionManager


class RegisterActivity : AppCompatActivity() {

    var listaDepartamento: ArrayList<Ubigeo>? = null
    var departamento: String? = null
    var provincia: String? = null
    var distrito: String? = null
    var ubigeoSelected:Ubigeo?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupToolbar()
        val dataBaseService = DataBaseService.getInstance(this)

        try {
            dataBaseService.getListDepartamento()
            listaDepartamento=dataBaseService.getListDepartamento()
        } catch (e:Exception) {
            e.printStackTrace()
        }

        spDepartamento.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelectedDepartamento(position)
            }
        }
        spProvincia.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelectProvincia(position)
            }
        }

        spDistrito.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelectDistrito(position)
            }
        }
        val departamentoAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaDepartamento)
        spDepartamento.setAdapter(departamentoAdapter)

    }


    fun spinnerItemSelectedDepartamento(position: Int) {
        val dataBaseService = DataBaseService.getInstance(this)

        val ubigeo = spDepartamento.getItemAtPosition(position) as Ubigeo
        departamento = ubigeo.nombre
        var listaProvincia: ArrayList<Ubigeo> = ArrayList()
        try {
            listaProvincia = dataBaseService.getListProvincia(ubigeo.idDepartamento)
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        val provinciaAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaProvincia)
        spProvincia.setAdapter(provinciaAdapter)

    }

    fun spinnerItemSelectProvincia( position: Int) {

        val dataBaseService = DataBaseService.getInstance(this)

        val ubigeo = spProvincia.getItemAtPosition(position) as Ubigeo
        provincia = ubigeo.nombre
        var listaDistrito: ArrayList<Ubigeo> = ArrayList()
        try {
            listaDistrito = dataBaseService.getListDistrito(ubigeo.idDepartamento, ubigeo.idProvincia)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val distritoAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaDistrito)
        spDistrito.setAdapter(distritoAdapter)
        btnSignup.setOnClickListener { registrarUser() }

    }


    fun spinnerItemSelectDistrito( position: Int) {
        val ubigeo = spProvincia.getItemAtPosition(position) as Ubigeo
        distrito = departamento + "/" + provincia + "/" + ubigeo.nombre
        ubigeoSelected=ubigeo


    }


    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    fun registrarUser() {
        if (etEmail.getText().toString().compareTo(Constants.VACIO) == 0) {
            showError(etEmail,getString(R.string.registro_message_error_ingrese_correo_electronico))
            return
        }
        if (!Util.validarCorreo(etEmail.getText().toString())) {
            showError(etEmail,getString(R.string.registro_message_error_correo_electronico_invalido))
            return
        }

        if (etPassword.getText().toString().compareTo(Constants.VACIO) == 0) {
            showError(etPassword,getString(R.string.registro_message_error_ingrese_contrasena_usuario))
            return
        }

        if (etPaterno.getText().toString().compareTo(Constants.VACIO) == 0) {
            showError(etPaterno,getString(R.string.registro_message_error_ingrese_apellido_paterno))
            return
        }
/*
        if (indidcePaisSeleccionado === 0) {
            Toast.makeText(applicationContext, getString(R.string.seleccione_tipo_doc), Toast.LENGTH_LONG).show()
            return
        }
*/
        if (etName.getText().toString().compareTo(Constants.VACIO) == 0) {
            showError(etName,getString(R.string.registro_message_error_ingrese_nombres))
            return
        }

        if (etDni.getText().toString().compareTo(Constants.VACIO) == 0||etDni.text.toString().length!=8) {
            showError(etName,getString(R.string.registro_message_error_formato_dni))
            return
        }

        if (etTelefono.getText().toString().compareTo(Constants.VACIO) == 0) {
            showError(etName,getString(R.string.registro_message_error_formato_phone))
            return
        }

        registrar()
    }




    private fun showError(editText:EditText,message:String){
        Toast.makeText(applicationContext, message , Toast.LENGTH_LONG).show()
        editText.setError(message)
        editText.setFocusable(true)
        editText.requestFocus()
    }


    fun registrar() {
        val usuario = User()
        usuario.setFirstName(etName.text.toString())
        usuario.setLastName(etPaterno.text.toString())
        usuario.setEmail(etEmail.text.toString())
        usuario.setPassword(etPassword.text.toString())
        usuario.setPhone(etTelefono.getText().toString())
        usuario.setDni(etDni.text.toString())
        usuario.codeDepartamento=ubigeoSelected?.idDepartamento
        usuario.codeProvincia=ubigeoSelected?.idProvincia
        usuario.codeDistrito=ubigeoSelected?.idDistrito
        usuario.avance=0


        InternetCheck({ internet -> if (internet) registerByServiceWeb(usuario) else verifyEmailLocal(usuario) })

    }

    private fun registerByServiceWeb(user:User){
       // Toast.makeText(this,"registro via web service",Toast.LENGTH_LONG).show()


        val progress=Util.createProgressDialog(this,"Cargando");
        progress.show();
        val register=RegistrarUsuarioClient(this,object:RegistrarUsuarioClient.RegistrarUsuarioCorreoListener{
            override fun onSuccess(user: User?) {

                SessionManager.getInstance(baseContext).createUserSession(user)

                user!!.userExternId=0
                var database=DataBaseService.getInstance(baseContext)
                database.insertUser(user)
                setResult(Activity.RESULT_OK, null)
                finish()
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)

                progress.dismiss();
                finish()


            }

            override fun onError(message: String?) {
                progress.dismiss();
                Toast.makeText(baseContext,message,Toast.LENGTH_LONG).show();


            }


        })
        register.insertarusuarioPorCorreo(user);

    }

    private fun registerLocal(user:User){
        SessionManager.getInstance(getApplicationContext()).createUserSession(user);

        user.userExternId=0
        var database=DataBaseService.getInstance(this)
        database.insertUser(user)
        setResult(Activity.RESULT_OK, null)
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    private fun verifyEmailLocal(user:User){

        val database=DataBaseService.getInstance(this)
        if (database.getUser(user.email)!=null) {
            Toast.makeText(this,R.string.registro_message_error_correo_anterior,Toast.LENGTH_LONG).show()
            return
        }
        registerLocal(user)


    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }



}
