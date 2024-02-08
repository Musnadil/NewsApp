package com.musnadil.newsapp.ui.search

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.musnadil.newsapp.MainActivity
import com.musnadil.newsapp.R
import com.musnadil.newsapp.data.model.ArticleX
import com.musnadil.newsapp.databinding.FragmentSearchBinding
import com.musnadil.newsapp.utility.getApiKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    private val listArticles: MutableList<ArticleX> = ArrayList()

    @Inject
    lateinit var searchAdapter: SearchAdapter

    companion object {
        const val URL_ARTICLE_KEY = "URL_ARTICLE_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (searchAdapter.itemCount == 0) {
            binding.etSearch.requestFocus()
            showKeyboard()
        } else {
            binding.etSearch.clearFocus()
            hideKeyboard()
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                binding.etSearch.clearFocus()
                binding.progressBar.visibility = View.VISIBLE
                if (!binding.etSearch.text.isNullOrEmpty()) {
                    getArticle(binding.etSearch.text.toString())
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Enter what you want to search for!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            true
        }
        searchObserver()
        onClickArticle()

    }


    private fun getArticle(q: String) {
        val apiKey = getApiKey(requireActivity().application)
        Handler(Looper.getMainLooper()).postDelayed({
        }, 1000)
        lifecycleScope.launch {
            viewModel.search(q, apiKey)
        }
    }

    private fun searchObserver() {
        binding.apply {
            lifecycleScope.launch {
                viewModel.newsList.collectLatest { pagingData ->
                    searchAdapter.submitData(pagingData)
                    binding.rvSearch.scrollToPosition(0)
                }
            }
            rvSearch.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = searchAdapter
            }
            lifecycleScope.launch {
                searchAdapter.loadStateFlow.collect {
                    val state = it.refresh
                    progressBar.isVisible = state is LoadState.Loading
                }
            }
            rvSearch.adapter = searchAdapter.withLoadStateFooter(
                LoadMoreAdapter {
                    searchAdapter.retry()
                }
            )
        }
    }

    private fun onClickArticle() {
        searchAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putString(URL_ARTICLE_KEY, it.url)
            findNavController().navigate(
                R.id.action_searchFragment_to_detailArticleFragment,
                bundle
            )
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
    }

    private fun showKeyboard() {
        val activity = activity as MainActivity
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard() {
        val activity = activity as MainActivity
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}