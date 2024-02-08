package com.musnadil.newsapp.ui.newsarticles

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.musnadil.newsapp.R
import com.musnadil.newsapp.data.api.Status.ERROR
import com.musnadil.newsapp.data.api.Status.LOADING
import com.musnadil.newsapp.data.api.Status.SUCCESS
import com.musnadil.newsapp.data.model.Article
import com.musnadil.newsapp.databinding.FragmentNewsArticlesBinding
import com.musnadil.newsapp.ui.newssources.NewsSourcesFragment
import com.musnadil.newsapp.utility.getApiKey
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsArticlesFragment : Fragment() {
    private var _binding: FragmentNewsArticlesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsArticleViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var newsArticlesAdapter: NewsArticlesAdapter
    private val listArticles: MutableList<Article> = ArrayList()

    companion object {
        const val URL_ARTICLE_KEY = "URL_ARTICLE_KEY"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsArticlesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sourceName = arguments?.getString(NewsSourcesFragment.SOURCE_NAME_KEY, "").toString()
        binding.toolbarText.text = "Articles from $sourceName"
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        getArticle()
        articleObserver()
        detailArticle()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
    }

    private fun getArticle() {
        val source = arguments?.getString(NewsSourcesFragment.SOURCE_KEY, "").toString()
        val apiKey = getApiKey(requireActivity().application)
        viewModel.getArticle(source, apiKey)
    }

    private fun articleObserver() {
        viewModel.article.observe(viewLifecycleOwner) {
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
                    }
                    if (listArticles.size > 0) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            progressDialog.dismiss()
                            binding.rvSource.visibility = View.VISIBLE
                            newsArticlesAdapter.submitData(listArticles)
                        }, 500)
                    } else {
                        progressDialog.dismiss()
                        binding.tvDataNull.text = "There are no article yet"
                        binding.tvDataNull.visibility = View.VISIBLE
                        binding.rvSource.visibility = View.GONE
                    }
                    viewModel.article.removeObservers(viewLifecycleOwner)
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
                }

                LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }

    private fun detailArticle() {
        newsArticlesAdapter = NewsArticlesAdapter(object : NewsArticlesAdapter.OnClickListener {
            override fun onCLickItem(data: Article) {
                val bundle = Bundle()
                bundle.putString(URL_ARTICLE_KEY,data.url)
                findNavController().navigate(R.id.action_newsArticlesFragment_to_detailArticleFragment,bundle)
            }

        })
        binding.rvSource.adapter = newsArticlesAdapter
    }
}