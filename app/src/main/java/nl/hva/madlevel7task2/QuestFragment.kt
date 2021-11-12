package nl.hva.madlevel7task2

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import nl.hva.madlevel7task2.databinding.FragmentQuestBinding
import java.lang.Exception

class QuestFragment : Fragment() {
    private var _binding: FragmentQuestBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuestViewModel by viewModels()
    private val quest: ArrayList<Quiz> = arrayListOf()
    private var quizIndex: Int = 0

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
        setOnBtnConfirmClickListener()
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

    private fun setOnBtnConfirmClickListener() {
        binding.btnConfirm.setOnClickListener {
            val checkedBtn = binding.rgOptions.checkedRadioButtonId
            val correctBtn = when (quest[quizIndex].result) {
                binding.rbtnOptionOne.text -> binding.rbtnOptionOne.id
                binding.rbtnOptionTwo.text -> binding.rbtnOptionTwo.id
                binding.rbtnOptionThree.text -> binding.rbtnOptionThree.id
                else -> throw Exception("Invalid data, result doesn't match any option.")
            }
            if (checkedBtn == correctBtn) {
                if (quizIndex == quest.size - 1) {
                    findNavController().navigate(R.id.action_QuestFragment_to_RewardFragment)
                } else {
                    quizIndex += 1
                    setQuestion(quizIndex)
                }
            } else {
                Snackbar.make(binding.root, "Wrong answer!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}