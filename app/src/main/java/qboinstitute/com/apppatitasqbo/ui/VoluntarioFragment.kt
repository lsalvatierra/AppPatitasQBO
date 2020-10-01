package qboinstitute.com.apppatitasqbo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.fragment_voluntario.*
import org.json.JSONObject
import qboinstitute.com.apppatitasqbo.R
import qboinstitute.com.apppatitasqbo.db.entity.PersonaEntity
import qboinstitute.com.apppatitasqbo.viewmodel.PersonaViewModel


class VoluntarioFragment : Fragment() {

    //Definimos la cola de peticiones
    private lateinit var queue: RequestQueue
    //Definimos el viewmodel
    private lateinit var personaViewModel: PersonaViewModel
    //Definimos el objeto PersonaEntity
    private lateinit var personaEntity: PersonaEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //Realizamos la instancia de la cola de peticiones
        queue = Volley.newRequestQueue(context)
        //Realizamos la instancia de ViewModelProvider
        val vista = inflater.inflate(R.layout.fragment_voluntario, container, false)
        personaViewModel = ViewModelProvider(this).get(PersonaViewModel::class.java)
        personaViewModel.obtener()
            .observe(viewLifecycleOwner, Observer { persona ->
                // Update the cached copy of the words in the adapter.
                persona?.let {
                    if(persona.esvoluntario == "1"){
                       actualizarFormulario()
                    }else{
                        personaEntity = persona
                    }
                }
            })
        val btnregvoluntario : Button = vista.findViewById(R.id.btnregvoluntario)
        btnregvoluntario.setOnClickListener {
            if(chkaceptocondiciones.isChecked){
                btnregvoluntario.isEnabled = false
                registrarUsuarioWS(it)
            }else{
                mostrarMensaje(it, "Acepte los términos y condiciones para ser voluntario")
            }
        }
        return vista
    }

    fun registrarUsuarioWS(vista: View){
        val urlwslogin = "http://www.kreapps.biz/patitas/personavoluntaria.php"
        val parameters = JSONObject()
        parameters.put("idpersona", personaEntity.id)
        val request = JsonObjectRequest(
            Request.Method.POST,
            urlwslogin, parameters, { response ->
                if (response.getBoolean("rpta")) {
                    val nuevaPersonaEntity = PersonaEntity(
                        personaEntity.id,
                        personaEntity.nombres,
                        personaEntity.apellidos,
                        personaEntity.email,
                        personaEntity.celular,
                        personaEntity.usuario,
                        personaEntity.password,
                        "1"
                    )
                    personaViewModel.actualizar(nuevaPersonaEntity)
                    actualizarFormulario()
                }
                mostrarMensaje(vista, response.getString("mensaje"))
                btnregvoluntario.isEnabled = true
            }, {
                Log.i("REGISTRO", it.toString())
                btnregvoluntario.isEnabled = true
            })
        queue.add(request)
    }
    //2. Actualizar controles del formulario
    private fun actualizarFormulario() {
        tvtextovoluntario.visibility = View.GONE
        btnregvoluntario.visibility = View.GONE
        chkaceptocondiciones.visibility = View.GONE
        tvtituvoluntario.text = getString(R.string.valtvesuvoluntario)

    }

    //1. Creamos método para enviar nuestros mensajes
    fun mostrarMensaje(vista: View, mensaje: String){
        Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG).show()
    }

}