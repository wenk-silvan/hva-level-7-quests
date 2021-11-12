package nl.hva.madlevel7task2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class QuestRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var questCollection = firestore.collection("quizzes")

    private val _quest: MutableLiveData<ArrayList<Quiz>> = MutableLiveData()
    val quest: LiveData<ArrayList<Quiz>> get() = _quest

    suspend fun getQuest() {
        try {
            withTimeout(5_000) {
                val data = questCollection.get().await()
                val tempList = ArrayList<Quiz>()
                data.documents.forEach {
                    tempList.add(
                        Quiz(
                            it.getString("question").toString(),
                            it.getString("optionOne").toString(),
                            it.getString("optionTwo").toString(),
                            it.getString("optionThree").toString(),
                            it.getString("result").toString()
                        )
                    )
                }
                _quest.value = tempList
            }
        } catch (e: Exception) {
            throw QuestRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    class QuestRetrievalError(message: String) : Exception(message)
}