package qboinstitute.com.apppatitasqbo.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONObject
import qboinstitute.com.apppatitasqbo.R
import qboinstitute.com.apppatitasqbo.db.entity.PersonaEntity
import qboinstitute.com.apppatitasqbo.viewmodel.PersonaViewModel

class LoginActivity : AppCompatActivity() {
    //Definimos el viewmodel
    private lateinit var personaViewModel: PersonaViewModel
    //Variable de preferencias
    private lateinit var preferencias: SharedPreferences
    //Definimos la cola de peticiones
    private lateinit var queue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Realizamos la instancia a las preferencias del aplicación
        preferencias = getSharedPreferences("appPatitas", MODE_PRIVATE)
        //Realizamos la instancia de ViewModelProvider
        personaViewModel = ViewModelProvider(this).get(PersonaViewModel::class.java)
        //Validamos que exista la preferencia recordardatos
        if(verificarValorSharedPreferences()){
            //activar checkbox recordar
            chkrecordar.isChecked = true
            etusuario.isEnabled = false
            etpassword.isEnabled = false
            chkrecordar.text = "Quitar el chek para ingresar con otro usuario"
            //obtiene los datos de SQLite y los muestra en los edittext
            personaViewModel.obtener()
                .observe(this, Observer { persona ->
                    // Update the cached copy of the words in the adapter.
                    persona?.let {
                        etusuario.setText(persona.usuario)
                        etpassword.setText(persona.password)
                    }

                })
        }else{
            personaViewModel.eliminartodo()
        }
        //Realizamos la instancia de la cola de peticiones
        queue = Volley.newRequestQueue(this)
        chkrecordar.setOnClickListener {
            setearValoresDeRecordar(it)
        }
        btnlogin.setOnClickListener {
            btnlogin.isEnabled = false
            if(validarUsuarioPassword()){
                AutenticarUsuarioWS(it, etusuario.text.toString(),
                    etpassword.text.toString())
            }else{
                btnlogin.isEnabled = true
                mostrarMensaje(it, getString(R.string.msguspassword))
            }
        }
        //Ir al registro de usuario
        btnregistrar.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
        }
    }

    private fun setearValoresDeRecordar(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            when (view.id) {
                R.id.chkrecordar -> {
                    if(!checked){
                        if(verificarValorSharedPreferences()){
                            preferencias.edit().remove("recordardatos")
                                .apply()
                            personaViewModel.eliminartodo()
                            etusuario.isEnabled = true
                            etpassword.isEnabled = true
                            chkrecordar.text = getString(R.string.valchkguardardatos)
                        }
                    }
                }
            }
        }
    }

    fun AutenticarUsuarioWS(vista: View, usuario: String, password: String){
        val urlwslogin = "http://www.kreapps.biz/patitas/login.php"
        val parameters = JSONObject()
        parameters.put("usuario", usuario)
        parameters.put("password", password)
        val request = JsonObjectRequest(Request.Method.POST,
            urlwslogin, parameters, { response ->
                if (response.getBoolean("rpta")) {
                    val personaEntity = PersonaEntity(
                        response.getString("idpersona").toInt(),
                        response.getString("nombres"),
                        response.getString("apellidos"),
                        response.getString("email"),
                        response.getString("celular"),
                        response.getString("usuario"),
                        response.getString("password"),
                        response.getString("esvoluntario")
                    )
                    if(verificarValorSharedPreferences()){
                        personaViewModel.actualizar(personaEntity)
                    }else{
                        personaViewModel.insertar(personaEntity)
                        if(chkrecordar.isChecked){
                            preferencias.edit().putBoolean("recordardatos", true)
                                .apply()
                        }
                    }
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )
                    finish()
                }else{
                    mostrarMensaje(vista, response.getString("mensaje"))
                }
                btnlogin.isEnabled = true
            }, {
                Log.i("LOGIN", it.toString())
                btnlogin.isEnabled = true
            })
        queue.add(request)
    }

    fun verificarValorSharedPreferences(): Boolean{
        return preferencias.getBoolean("recordardatos", false)
    }

    //2. Método que valida el ingreso de usuario y password.
    fun validarUsuarioPassword():Boolean{
        var respuesta = true
        if(etusuario.text.toString().trim().isEmpty()){
            etusuario.isFocusableInTouchMode = true
            etusuario.requestFocus()
            respuesta = false

        } else if(etpassword.text.toString().trim().isEmpty()){
            etpassword.isFocusable = true
            etpassword.isFocusableInTouchMode = true
            etpassword.requestFocus()
            respuesta = false
        }
        return respuesta
    }

    //1. Creamos método para enviar nuestros mensajes
    fun mostrarMensaje(vista: View, mensaje : String){
        Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG).show()
    }
}