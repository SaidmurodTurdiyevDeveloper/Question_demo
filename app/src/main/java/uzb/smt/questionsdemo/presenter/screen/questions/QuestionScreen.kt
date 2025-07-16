package uzb.smt.questionsdemo.presenter.screen.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uzb.smt.questionsdemo.R
import uzb.smt.questionsdemo.databinding.ScreenQuestionsBinding
import uzb.smt.questionsdemo.presenter.adapters.QuestionAdapter
import uzb.smt.questionsdemo.presenter.dialog.DeleteDialog


@AndroidEntryPoint
class QuestionScreen : Fragment() {

    private var _binding: ScreenQuestionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: DeleteDialog
    private val viewModel: QuestionViewModel by viewModels()
    private lateinit var adapter: QuestionAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupListeners()
        collectUi()
        viewModel.loadQuestions()
    }


    private fun setupRecyclerView() {
        adapter = QuestionAdapter(
            changeItem = { updatedItem, index ->
                viewModel.changeQuestionOption(updatedItem, index)
            },
            deleteItem = { item ->
                viewModel.openDeleteQuestion(item)
            },
            onCheckClick = {
                viewModel.openResult()
            }
        )
        dialog = DeleteDialog(context = requireContext(), listener = {
            viewModel.deleteQuestion(it)
        })
        binding.rv.adapter = adapter
    }

    private fun setupListeners() {
        binding.fb.setOnClickListener {
            findNavController().navigate(R.id.action_questionScreen_to_createQuestionScreen)
        }
    }

    private fun collectUi() {
        lifecycleScope.launchWhenStarted {
            viewModel.questions.collectLatest { list ->
                adapter.submitList(list)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteQuestion.collectLatest { item ->
                dialog.show("Siz rostan ham o`chirmoqchimisiz?", item)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isActive.collectLatest { isActive ->
                adapter.isActive = isActive
                adapter.notifyItemChanged(viewModel.questions.value.size)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.openResult.collectLatest { json ->
                    val currentDestId = findNavController().currentDestination?.id
                    if (currentDestId == R.id.questionScreen) {
                        findNavController().navigate(
                            R.id.action_questionScreen_to_resultScreen,
                            bundleOf("result" to json)
                        )
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.errorMessage.collectLatest { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
