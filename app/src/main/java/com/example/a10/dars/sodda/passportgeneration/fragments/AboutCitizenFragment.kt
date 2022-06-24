package com.example.a10.dars.sodda.passportgeneration.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.a10.dars.sodda.passportgeneration.databinding.FragmentAboutCitizenBinding
import com.example.a10.dars.sodda.passportgeneration.room.entity.Passport

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutCitizenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutCitizenFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentAboutCitizenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutCitizenBinding.inflate(inflater, container, false)
        binding.apply {
            toolBar.setNavigationOnClickListener { findNavController().popBackStack() }
            val passport = arguments?.getSerializable("passport") as Passport
            image.setImageURI(Uri.parse(passport.image))
            tvName.text = "${passport.surName} ${passport.name} ${passport.middleName}"
            tvResult.text = "Viloyati: ${passport.region}\n" +
                    "Shahri: ${passport.city}\n" +
                    "Uy manzili: ${passport.description}\n" +
                    "Jinsi: ${passport.gender}\n" +
                    "Passport olgan sanasi: ${passport.passportDate}\n" +
                    "Passportning amal qilish muddati: ${passport.bestBeforeDate}\n" +
                    "Passport seriyasi: ${passport.passportNumber}"
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AboutCitizenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutCitizenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}