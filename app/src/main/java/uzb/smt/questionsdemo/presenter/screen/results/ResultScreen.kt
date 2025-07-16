package uzb.smt.questionsdemo.presenter.screen.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import uzb.smt.questionsdemo.databinding.ScreenResultBinding
import uzb.smt.questionsdemo.presenter.adapters.QuestionResultAdapter


@AndroidEntryPoint
class ResultScreen : Fragment() {

    private var _binding: ScreenResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResultViewModel by viewModels()
    private val adapter by lazy { QuestionResultAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rv.adapter = adapter
        binding.tb.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.results.collectLatest {
                adapter.submitList(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.correctCountText.collectLatest { correct ->
                binding.tvResult.text = correct
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
