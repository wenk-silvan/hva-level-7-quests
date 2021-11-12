package nl.hva.madlevel7task2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuestViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FIRESTORE"
    private val questRepository = QuestRepository()
    val quest: LiveData<ArrayList<Quiz>> = questRepository.quest

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getQuest() {
        viewModelScope.launch {
            try {
                questRepository.getQuest()
            } catch (ex: QuestRepository.QuestRetrievalError) {
                val errorMsg = "Something went wrong while retrieving quest"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }
}