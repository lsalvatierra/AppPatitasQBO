package qboinstitute.com.apppatitasqbo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONObject
import qboinstitute.com.apppatitasqbo.R
import java.util.regex.Pattern


class RegistroActivity : AppCompatActivity() {

    //Definimos la cola de peticiones
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        //Realizamos la instancia de la cola de peticiones
        queue = Volley.newRequestQueue(this)
        btnirlogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        btnregistrarme.setOnClickListener {
            btnregistrarme.isEnabled = false
            if(validarFormulario(it)){
                RegistrarUsuarioWS(it)
            }else{
                btnregistrarme.isEnabled = true
            }
        }


    }
    fun RegistrarUsuarioWS(vista: View){
        val urlwslogin = "http://www.kreapps.biz/patitas/persona.php"
        val parameters = JSONObject()
        parameters.put("nombres", etnomusuario.text.toString())
        parameters.put("apellidos", etapeusuario.text.toString())
        parameters.put("email", etemailusuario.text.toString())
        parameters.put("celular", etcelusuario.text.toString())
        parameters.put("usuario", etusureg.text.toString())
        parameters.put("password", etpassreg.text.toString())
        val request = JsonObjectRequest(
            Request.Method.PUT,
            urlwslogin, parameters, { response ->
                if (response.getBoolean("rpta")) {
                    setearControles()
                }
                mostrarMensaje(vista, response.getString("mensaje"))
                btnregistrarme.isEnabled = true
            }, {
                Log.i("REGISTRO", it.toString())
                btnregistrarme.isEnabled = true
            })
        queue.add(request)
    }
    //3. Método que setea los controles del formulario
    private fun setearControles() {
        etnomusuario.setText("")
        etapeusuario.setText("")
        etemailusuario.setText("")
        etcelusuario.setText("")
        etusureg.setText("")
        etpassreg.setText("")
        etrepassreg.setText("")
    }

    //2. Método que valida el formulario.
    fun validarFormulario(vista: View):Boolean{
        var respuesta = true
        when {
            etnomusuario.text.toString().trim().isEmpty() -> {
                etnomusuario.isFocusableInTouchMode = true
                etnomusuario.requestFocus()
                mostrarMensaje(vista, "Ingrese su nombre")
                respuesta = false
            }
            etapeusuario.text.toString().trim().isEmpty() -> {
                etapeusuario.isFocusable = true
                etapeusuario.isFocusableInTouchMode = true
                etapeusuario.requestFocus()
                mostrarMensaje(vista, "Ingrese su apellido")
                respuesta = false
            }
            etemailusuario.text.toString().trim().isEmpty() -> {
                etemailusuario.isFocusable = true
                etemailusuario.isFocusableInTouchMode = true
                etemailusuario.requestFocus()
                mostrarMensaje(vista, "Ingrese su email")
                respuesta = false
            }
            /*etemailusuario.text.toString().trim().isNotEmpty() -> {
                val pattern: Pattern = Patterns.EMAIL_ADDRESS
                if(!pattern.matcher(etemailusuario.text.toString().trim()).matches())
                {
                    etemailusuario.isFocusable = true
                    etemailusuario.isFocusableInTouchMode = true
                    etemailusuario.requestFocus()
                    mostrarMensaje(vista, "Ingrese un email válido")
                    respuesta = false
                }
            }*/
            etcelusuario.text.toString().trim().isEmpty() -> {
                etcelusuario.isFocusable = true
                etcelusuario.isFocusableInTouchMode = true
                etcelusuario.requestFocus()
                mostrarMensaje(vista, "Ingrese su celular")
                respuesta = false
            }

            etusureg.text.toString().trim().isEmpty() -> {
                etusureg.isFocusable = true
                etusureg.isFocusableInTouchMode = true
                etusureg.requestFocus()
                mostrarMensaje(vista, "Ingrese su usuario")
                respuesta = false
            }
            etpassreg.text.toString().trim().isEmpty() -> {
                etpassreg.isFocusable = true
                etpassreg.isFocusableInTouchMode = true
                etpassreg.requestFocus()
                mostrarMensaje(vista, "Ingrese su password")
                respuesta = false
            }
            etpassreg.text.toString().trim().isNotEmpty() -> {
                if(etpassreg.text.toString() != etrepassreg.text.toString()){
                    etrepassreg.isFocusable = true
                    etrepassreg.isFocusableInTouchMode = true
                    etrepassreg.requestFocus()
                    mostrarMensaje(vista, "Su password no coincide")
                    respuesta = false
                }
            }
        }
        return respuesta
    }


    //1. Creamos método para enviar nuestros mensajes
    fun mostrarMensaje(vista: View, mensaje: String){
        Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG).show()
    }

}