package com.musnadil.newsapp.ui.newssources

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
import com.musnadil.newsapp.data.model.Source
import com.musnadil.newsapp.databinding.FragmentNewsSourcesBinding
import com.musnadil.newsapp.ui.newscategories.NewsCategoriesFragment.Companion.CATEGORY_KEY
import com.musnadil.newsapp.utility.getApiKey
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsSourcesFragment : Fragment() {

    private var _binding: FragmentNewsSourcesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsSourcesViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var newsSourcesAdapter: NewsSourcesAdapter
    private val listSource: MutableList<Source> = ArrayList()

    companion object {
        const val SOURCE_KEY = "SOURCE_KEY"
        const val SOURCE_NAME_KEY = "SOURCE_NAME_KEY"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsSourcesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        getSources()
        sourcesObserver()
        articleFromSource()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
    }

    private fun getSources() {
        val category = arguments?.getString(CATEGORY_KEY, "").toString()
        val apiKey = getApiKey(requireActivity().application)
        viewModel.getSource(category, apiKey)
    }

    private fun sourcesObserver() {
        val category = arguments?.getString(CATEGORY_KEY, "").toString()

        viewModel.newsSource.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    listSource.clear()
                    if (it.data?.status == "ok") {
                        for (i in it.data.sources) {
                            listSource.add(i)
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
                    if (listSource.size > 0) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            progressDialog.dismiss()
                            binding.rvSource.visibility = View.VISIBLE
                            newsSourcesAdapter.submitData(listSource)
                        }, 500)
                    } else {
                        progressDialog.dismiss()
                        binding.tvDataNull.text = "There are no $category news sources yet"
                        binding.tvDataNull.visibility = View.VISIBLE
                        binding.rvSource.visibility = View.GONE
                    }
                    viewModel.newsSource.removeObservers(viewLifecycleOwner)
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

    private fun articleFromSource() {
        val bundle = Bundle()
        newsSourcesAdapter = NewsSourcesAdapter(object : NewsSourcesAdapter.OnClickListener {
            override fun onCLickItem(data: Source) {
                bundle.putString(SOURCE_KEY, data.id)
                bundle.putString(SOURCE_NAME_KEY, data.name)
                findNavController().navigate(
                    R.id.action_newsSourcesFragment_to_newsArticlesFragment,
                    bundle
                )
            }
        })
        binding.rvSource.adapter = newsSourcesAdapter
    }
}