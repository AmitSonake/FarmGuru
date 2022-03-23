package com.farms.farmguru.ui.language

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.farms.farmguru.MainActivity
import com.farms.farmguru.R
import com.farms.farmguru.databinding.FragmentHomeBinding
import com.farms.farmguru.databinding.FragmentLanguageBinding
import com.farms.farmguru.ui.home.HomeViewModel

class LanguageFragment : Fragment() {

    private lateinit var homeViewModel: LanguageViewModel
    private var _binding: FragmentLanguageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(LanguageViewModel::class.java)

        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        startActivity(Intent(context,LanguageSelectionActivity::class.java))
        activity?.finish()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}