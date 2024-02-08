package com.musnadil.newsapp.ui.newscategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.musnadil.newsapp.R
import com.musnadil.newsapp.databinding.FragmentNewsCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class NewsCategoriesFragment : Fragment() {
    private var _binding: FragmentNewsCategoriesBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val formattedDate = sdf.format(calendar.time)
        binding.tvDate.text = formattedDate
        onCardClick(binding.btnBusiness, binding.tvBusiness.text.toString().lowercase())
        onCardClick(binding.btnEntertainment, binding.tvEntertainment.text.toString().lowercase())
        onCardClick(binding.btnGeneral, binding.tvGeneral.text.toString().lowercase())
        onCardClick(binding.btnHealth, binding.tvHealth.text.toString().lowercase())
        onCardClick(binding.btnScience, binding.tvScience.text.toString().lowercase())
        onCardClick(binding.btnSports, binding.tvSport.text.toString().lowercase())
        onCardClick(binding.btnTechnology, binding.tvTechnology.text.toString().lowercase())
        binding.cvSearch.setOnClickListener {
            findNavController().navigate(R.id.action_newsCategoriesFragment_to_searchFragment)
        }
    }

    private fun onCardClick(button: CardView, category: String) {
        button.setOnClickListener {
            val bundle = Bundle().apply {
                putString(CATEGORY_KEY, category)
            }
            findNavController().navigate(
                R.id.action_newsCategoriesFragment_to_newsSourcesFragment,
                bundle
            )
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.primaryBlue)
    }
}