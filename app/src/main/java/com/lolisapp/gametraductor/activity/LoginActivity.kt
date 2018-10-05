package com.lolisapp.gametraductor.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.lolisapp.gametraductor.R
import com.lolisapp.gametraductor.asynctask.InternetCheck
import com.lolisapp.gametraductor.bean.User
import com.lolisapp.gametraductor.client.GetUserByCorreoClient
import com.lolisapp.gametraductor.client.LoginClient
import com.lolisapp.gametraductor.database.DataBaseService
import com.lolisapp.gametraductor.util.Util
import com.lolisapp.gametraductor.util.session.SessionManager
import kotlinx.android.synthetic.main.activity_login.*
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*


class LoginActivity : AppCompatActivity() {
    private val REQUEST_SIGNUP = 0
    private val REQUEST_LOGIN = 1

    var callbackManager: CallbackManager? = null
    var mGoogleApiClient: GoogleApiClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configureFacebook()
        congifureGmail()

        btnRegistro.setOnClickListener({
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
        })


        btnLogin.setOnClickListener({ if (validate()) InternetCheck({ internet -> if (internet) verifyLoginExtern() else verifyLocal() }) })
        sign_in_button.setOnClickListener({ logueoGmail() })


        if (SessionManager.getInstance(this).isLogged) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyLoginExtern() {


        // Toast.makeText(this,"login con servicios web",Toast.LENGTH_LONG).show();


        val progress = Util.createProgressDialog(this, "Cargando");
        progress.show();
        val loginclient = LoginClient(this, object : LoginClient.VerificarUsuarioListener {
            override fun onSuccess(user: User?) {

                var database = DataBaseService.getInstance(baseContext)
                val logued = database.getUser(user!!.email)
                if(user.avance>0) database.editAvance(user.avance,user.email);
                if (logued === null) {
                    val rand = Random()

                    user!!.avance = 0
                    user.codeDepartamento = 1
                    user.codeDistrito = 1
                    user.codeProvincia = 1
                    database.insertUser(user)


                }
                val newuser = database.getUser(user.email)




                progress.dismiss();
                SessionManager.getInstance(baseContext).createUserSession(newuser)
                finish()
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)

            }

            override fun onError(message: String?) {
                progress.dismiss();
                Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show();


            }


        })
        loginclient.verificarUser(etEmail.text.toString(), etPass.text.toString());


    }

    private fun verifyLocal() {
        val dabaseService = DataBaseService.getInstance(this)
        val user = dabaseService.getUserByPassEmail(etEmail.text.toString(), etPass.text.toString())
        if (user != null) {
            SessionManager.getInstance(this).createUserSession(user)


            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "correo o contraseña incorrecta", Toast.LENGTH_LONG).show()
        }

    }


    private fun handleSignInResult(result: GoogleSignInResult) {

        if (result.isSuccess) {

            val progressDialog = Util.createProgressDialog(this, "Cargando")
            progressDialog.show();

            val emailGmail = GetUserByCorreoClient(baseContext, object : GetUserByCorreoClient.VerificarUsuarioListener {
                override fun onSuccess(user: User?) {
                    progressDialog.dismiss();
                    var database = DataBaseService.getInstance(baseContext)
                    val logued = database.getUser(user!!.email)
                    if (logued == null) {

                        user!!.avance = 0
                        user.codeDepartamento = 1
                        user.codeDistrito = 1
                        user.codeProvincia = 1
                        database.insertUser(user)


                    }
                    val newuser = database.getUser(user.email)




                    SessionManager.getInstance(baseContext).createUserSession(newuser)
                    finish()
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)

                }

                override fun notExist() {
                    finish()
                    val intent = Intent(baseContext, RegisterExternActivity::class.java)
                    intent.putExtra("email", result.signInAccount!!.email)
                    startActivity(intent)
                }


                override fun onError(message: String?) {
                    progressDialog.dismiss();
                }
            })

            emailGmail.userByEmail(result.getSignInAccount()!!.getEmail());
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.d("result ","result "+resultCode+" request "+requestCode)


        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                this.finish()
            }
        }



        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result)
                setResult(RESULT_OK, null)
                this.finish()
            }
        }

        callbackManager!!.onActivityResult(requestCode, resultCode, data);

    }

    fun validate(): Boolean {
        var valid = true

        val email = etEmail.getText().toString()
        val password = etPass.getText().toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(resources.getString(R.string.registro_message_error_correo_electronico_invalido))
            valid = false
        } else {
            etEmail.setError(null)
        }

        if (password.isEmpty()) {
            etPass.setError(resources.getString(R.string.registro_message_error_ingrese_contrasena_usuario))
            valid = false
        } else {
            etPass.setError(null)
        }

        return valid
    }


    private fun configureFacebook() {
        callbackManager = CallbackManager.Factory.create()
       // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
     //   btbLoginFace.setReadPermissions("email")
/*
        btbLoginFace.registerCallback( callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("face ","face result succes");
                if (AccessToken.getCurrentAccessToken() != null) {
                    verifyExistUser();
                }
            }

            override fun onCancel() {
                Log.d("face ","face result cancel");

                //  Log.d(FragmentActivity.TAG, "Login Facebook Cancel")
            }

            override fun onError(exception: FacebookException) {
                Log.d("face ","face result error");

                if (exception is FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut()
                    }
                }
                val sw = StringWriter()
                exception.printStackTrace(PrintWriter(sw))

            }
        })
*/
        LoginManager.getInstance().registerCallback(

                callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                Log.d("face ","face result succes");
                if (AccessToken.getCurrentAccessToken() != null) {
                    verifyExistUser();
                }
            }

            override fun onCancel() {
                Log.d("face ","face result cancel");

                //  Log.d(FragmentActivity.TAG, "Login Facebook Cancel")
            }

            override fun onError(exception: FacebookException) {
                Log.d("face ","face result error");

                if (exception is FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut()
                    }
                }
                val sw = StringWriter()
                exception.printStackTrace(PrintWriter(sw))

            }
        })

        btbLoginFace.setOnClickListener(

        {

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))





        })

    }


    private fun congifureGmail() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, object : GoogleApiClient.OnConnectionFailedListener {
                    override fun onConnectionFailed(connectionResult: ConnectionResult) {
                        Log.d("INICIOACTIVITY", connectionResult.getErrorMessage())
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()


    }

    fun logueoGmail() {
        if (mGoogleApiClient!!.isConnected()) Auth.GoogleSignInApi.signOut(mGoogleApiClient)
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, 1)
    }


    fun verifyExistUser() {


        val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), GraphRequest.GraphJSONObjectCallback({ json, response
            ->

            val facebookjson = response.getJSONObject();
            if (facebookjson.has("email")) {
                val f_email = facebookjson.getString("email");
                Log.d("email face,","email face "+f_email);

                verificarEmail(f_email);
            }


        }))
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,picture")
        request.parameters = parameters
        request.executeAsync()

    }


    private fun verificarEmail(f_email: String) {
       // val progressDialog = Util.createProgressDialog(applicationContext, "Cargando")
       // progressDialog.show();

        GetUserByCorreoClient(this, object : GetUserByCorreoClient.VerificarUsuarioListener {
            override fun notExist() {
                //progressDialog.dismiss()

                val f_name = ""

                val intent = Intent(this@LoginActivity, RegisterExternActivity::class.java)
                intent.putExtra("email", f_email)
                intent.putExtra("pname", f_name)
                startActivityForResult(intent, 2)
            }

            override fun onSuccess(client: User?) {

                if (client != null) {
                    var database = DataBaseService.getInstance(baseContext)
                    val logued = database.getUser(client!!.email)
                    if(client.avance>0) database.editAvance(client.avance,client.email);

                    if (logued === null) {
                        val rand = Random()

                        client!!.avance = 0
                        client.codeDepartamento = 1
                        client.codeDistrito = 1
                        client.codeProvincia = 1

                        database.insertUser(client)


                    }
                    val newuser = database.getUser(client.email)

                    SessionManager.getInstance(applicationContext).createUserSession(newuser)
                  //  progressDialog.dismiss()
                    setResult(Activity.RESULT_OK, null)
                    this@LoginActivity.finish()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)


                } else {


                }

            }

            override fun onError(message: String) {


                //   progressDialog.dismiss()
            }
        }).userByEmail(f_email)


    }

}
