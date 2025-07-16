package uzb.smt.questionsdemo.presenter.screen.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uzb.smt.questionsdemo.R
import uzb.smt.questionsdemo.databinding.ScreenCreateQuestionBinding
import uzb.smt.questionsdemo.domen.model.QuestionType
import uzb.smt.questionsdemo.domen.model.create.CreateOptionData
import uzb.smt.questionsdemo.presenter.adapters.OptionAdapter
import uzb.smt.questionsdemo.presenter.dialog.CreateOptionDialog
import uzb.smt.questionsdemo.presenter.dialog.ErrorDialog

@AndroidEntryPoint
class CreateQuestionScreen : Fragment() {
    private val types = listOf("Radio", "Checkbox", "Input", "Dropdown")
    private var _binding: ScreenCreateQuestionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateQuestionViewModel by viewModels()

    private lateinit var optionAdapter: OptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenCreateQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        optionAdapter = OptionAdapter(
            onCorrectSelected = { option, index -> viewModel.selectCorrectOption(option, index) },
            onDelete = {
                viewModel.deleteOption(it)
            }
        )
        binding.rv.adapter = optionAdapter

        binding.tb.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.tb.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_done -> {
                    viewModel.saveQuestion()
                    true
                }

                else -> false
            }
        }
        binding.edittextQuestion.doAfterTextChanged {
            viewModel.changeQuestionText(it.toString())
        }


        binding.btnAdd.setOnClickListener {
            CreateOptionDialog(requireContext()) { optionText ->
                viewModel.addQuestionOption(
                    CreateOptionData(optionText = optionText, isCorrect = viewModel.type.value == QuestionType.Input)
                )
            }.show()
        }

        binding.dropdownMenu.setOnItemClickListener { parent, view, position, id ->
            val selectedType = when (position) {
                0 -> QuestionType.Radio
                1 -> QuestionType.CheckBox
                2 -> QuestionType.Input
                3 -> QuestionType.Selected
                else -> QuestionType.Radio
            }
            optionAdapter.type = selectedType
            viewModel.selectType(selectedType)
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdownMenu.setAdapter(adapter)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.type.collect { type ->
                        binding.dropdownMenu.setSelection(
                            when (type) {
                                QuestionType.Radio -> 0
                                QuestionType.CheckBox -> 1
                                QuestionType.Input -> 2
                                QuestionType.Selected -> 3
                            }
                        )
                    }
                }

                launch {
                    viewModel.options.collect {
                        optionAdapter.submitList(it)
                    }
                }

                launch {
                    viewModel.showDialog.collect {
                        if (it) {
                            ErrorDialog(requireContext()).show(
                                viewModel.errorMessage.value ?: "Xatolik mavjud"
                            )
                        }
                    }
                }

                launch {
                    viewModel.success.collect { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Savol saqlandi!", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        } else {
                            Toast.makeText(requireContext(), "Xatolik yuz berdi", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
