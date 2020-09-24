package qboinstitute.com.apppatitasqbo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import qboinstitute.com.apppatitasqbo.repository.PersonaRepository
import qboinstitute.com.apppatitasqbo.db.PatitasRoomDatabase
import qboinstitute.com.apppatitasqbo.db.entity.PersonaEntity

class PersonaViewModel(application: Application)
    : AndroidViewModel(application)
{
    private val repository: PersonaRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    init {
        val wordsDao = PatitasRoomDatabase.getDatabase(application).personaDao()
        repository = PersonaRepository(wordsDao)
    }
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertar(personaEntity: PersonaEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertar(personaEntity)
    }
    fun actualizar(personaEntity: PersonaEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.actualizar(personaEntity)
    }

    fun eliminartodo() = viewModelScope.launch(Dispatchers.IO) {
        repository.eliminartodo()
    }

    fun obtener() : LiveData<PersonaEntity> {
        return repository.obtener()
    }
}