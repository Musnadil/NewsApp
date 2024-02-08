package com.musnadil.newsapp.ui.search

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.musnadil.newsapp.MainActivity
import com.musnadil.newsapp.R
import com.musnadil.newsapp.data.api.Status.ERROR
import com.musnadil.newsapp.data.api.Status.LOADING
import com.musnadil.newsapp.data.api.Status.SUCCESS
import com.musnadil.newsapp.data.model.ArticleX
import com.musnadil.newsapp.databinding.FragmentSearchBinding
import com.musnadil.newsapp.utility.getApiKey
import com.musnadil.newsapp.utility.showViewSmoothly
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var searchAdapter: SearchAdapter
    private val listArticles: MutableList<ArticleX> = ArrayList()

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
        if (listArticles.size>0){
            hideKeyboard()
            binding.etSearch.clearFocus()
        } else {
            binding.etSearch.requestFocus()
            showKeyboard()
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        articleObserver()
        detailArticle()

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                binding.etSearch.clearFocus()
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

    }

    private fun getArticle(q: String) {
        val apiKey = getApiKey(requireActivity().application)
        viewModel.searchArticle(q, apiKey)
    }

    private fun articleObserver() {
        viewModel.searchResult.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    listArticles.clear()
                    if (it.data?.status == "ok") {
                        for (i in it.data.articles) {
                            listArticles.add(i)
                        }
                    } else {
                        progressDialog.dismiss()
                        AlertDialog.Builder(requireContext())
                            .setTitle("Message")
                            .setMessage(it.message ?: "error")
                            .setPositiveButton("Ok") { positiveButton, _ ->
                                positiveButton.dismiss()
                            }
                            .show()
                        viewModel.searchResult.removeObservers(viewLifecycleOwner)
                    }
                    if (listArticles.size > 0) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            progressDialog.dismiss()
                            binding.tvDataNull.visibility = View.GONE
//                            binding.rvSearch.visibility = View.VISIBLE
                            searchAdapter.submitData(listArticles)
                            showViewSmoothly(binding.rvSearch)
                            searchAdapter.notifyDataSetChanged()
                        }, 500)
                    } else {
                        progressDialog.dismiss()
                        binding.tvDataNull.text = "There are no article yet"
                        binding.tvDataNull.visibility = View.VISIBLE
                        binding.rvSearch.visibility = View.GONE
                    }

                }

                ERROR -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setTitle("Message")
                        .setMessage(it.message ?: "error")
                        .setPositiveButton("Ok") { positiveButton, _ ->
                            positiveButton.dismiss()
                        }
                        .show()
                    viewModel.searchResult.removeObservers(viewLifecycleOwner)
                }

                LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }

    private fun detailArticle() {
        searchAdapter = SearchAdapter(object : SearchAdapter.OnClickListener {
            override fun onCLickItem(data: ArticleX) {
                val bundle = Bundle()
                bundle.putString(URL_ARTICLE_KEY, data.url)
                findNavController().navigate(
                    R.id.action_searchFragment_to_detailArticleFragment,
                    bundle
                )
            }

        })
        binding.rvSearch.adapter = searchAdapter
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