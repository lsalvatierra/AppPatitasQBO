package qboinstitute.com.apppatitasqbo.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import qboinstitute.com.apppatitasqbo.R
import qboinstitute.com.apppatitasqbo.viewmodel.PersonaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    //Definir estas dos variables.
    private lateinit var navView: NavigationView
    private lateinit var personaViewModel: PersonaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_lista_mascota, R.id.nav_voluntario
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        mostrarInfoAutenticacion()
    }

    fun mostrarInfoAutenticacion() {
        //Colocar IDs a los controles TextView al
        // archivo layout->nav_header_main
        val tvnomusuario : TextView = navView.getHeaderView(0)
            .findViewById(R.id.tvnomusuario)
        val tvemailusuario : TextView = navView.getHeaderView(0)
            .findViewById(R.id.tvemailusuario)
        personaViewModel = ViewModelProvider(this).get(PersonaViewModel::class.java)
        personaViewModel.obtener()
            .observe(this, Observer { persona ->
                // Update the cached copy of the words in the adapter.
                persona?.let {
                    tvemailusuario.text = persona.email
                    tvnomusuario.text = persona.nombres
                }
            })

    }
    //Sobrescribir el método
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Cambiar el id y el título de la opción en el
        // archivo menu->main.xml
        val idItem = item.itemId
        if(idItem == R.id.action_cerrar){
            startActivity(
                Intent(this,
                LoginActivity::class.java)
            )
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}