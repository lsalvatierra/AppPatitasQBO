package qboinstitute.com.apppatitasqbo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_registro.*
import qboinstitute.com.apppatitasqbo.R
import qboinstitute.com.apppatitasqbo.db.entity.PersonaEntity
import qboinstitute.com.apppatitasqbo.viewmodel.PersonaViewModel

class RegistroActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

    }


}