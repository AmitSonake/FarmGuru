package com.farms.farmguru.ui.logout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.MainActivity
import com.farms.farmguru.R
import com.farms.farmguru.databinding.FragmentHomeBinding
import com.farms.farmguru.databinding.FragmentLogoutBinding
import com.farms.farmguru.login.LoginActivity
import com.farms.farmguru.ui.home.HomeViewModel
import com.farms.farmguru.ui.myplots.MyPlotActivity

class LogoutFragment : Fragment() {

    private lateinit var homeViewModel: LogoutViewModel
    private var _binding: FragmentLogoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(LogoutViewModel::class.java)

        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val builder = this.context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Logout")
        builder?.setMessage("Are you sure you want to logout?")
        builder?.setPositiveButton(android.R.string.yes) { dialog, which ->
            context?.let { SharedPreferencesHelper.invoke(it).saveUserLoggedIn(false) }
            startActivity(Intent(context, LoginActivity::class.java))
           activity?.finish()
        }

        builder?.setNegativeButton(android.R.string.no) { dialog, which ->
            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        }

        builder?.show()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}