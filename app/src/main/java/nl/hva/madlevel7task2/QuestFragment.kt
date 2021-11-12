package nl.hva.madlevel7task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import nl.hva.madlevel7task2.databinding.FragmentQuestBinding

class QuestFragment : Fragment() {
    private var _binding: FragmentQuestBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuestViewModel by viewModels()
    private val quizIndex: Int = 0
    private val quest: ArrayList<Quiz> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuest()
        observeQuest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeQuest() {
        viewModel.quest.observe(viewLifecycleOwner, {
            quest.clear()
            quest.addAll(it)
            setQuestion(0)
        })
    }

    private fun setQuestion(index: Int) {
        val quiz = quest[index]
        binding.tvIndex.text = getString(R.string.tvIndex, index + 1, quest.size)
        binding.tvQuestion.text = quiz.question
        binding.rbtnOptionOne.text = quiz.optionOne
        binding.rbtnOptionTwo.text = quiz.optionTwo
        binding.rbtnOptionThree.text = quiz.optionThree
        quiz.imgStorageReference.downloadUrl.addOnSuccessListener {
            print(it)
        }
        GlideApp.with(this)
            .load(quiz.imgStorageReference)
            .into(binding.ivBuilding)
    }
}