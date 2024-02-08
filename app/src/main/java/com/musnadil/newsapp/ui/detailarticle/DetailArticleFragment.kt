package com.musnadil.newsapp.ui.detailarticle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.musnadil.newsapp.R
import com.musnadil.newsapp.databinding.FragmentDetailArticleBinding
import com.musnadil.newsapp.ui.newsarticles.NewsArticlesFragment

class DetailArticleFragment : Fragment() {
    private var _binding: FragmentDetailArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        val url = arguments?.getString(NewsArticlesFragment.URL_ARTICLE_KEY, "").toString()

        webView = binding.wvDetailNews
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
        webView.canGoBack()
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressbar.progress = newProgress
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressbar.visibility = View.GONE
            }
        }

    }

}