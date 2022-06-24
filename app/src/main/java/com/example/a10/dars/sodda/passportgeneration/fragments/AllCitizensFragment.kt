package com.example.a10.dars.sodda.passportgeneration.fragments

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.a10.dars.sodda.passportgeneration.R
import com.example.a10.dars.sodda.passportgeneration.adapters.MyRecycklerViewAdapter
import com.example.a10.dars.sodda.passportgeneration.databinding.FragmentAllCitizensBinding
import com.example.a10.dars.sodda.passportgeneration.room.dataBase.AppDataBase
import com.example.a10.dars.sodda.passportgeneration.room.entity.Passport
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllCitizensFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllCitizensFragment : Fragment() {
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

    val TAG = "aaa"
    lateinit var appDataBase: AppDataBase
    lateinit var list: ArrayList<Passport>
    lateinit var list1: ArrayList<Passport>
    lateinit var myRecyclerViewAdapter: MyRecycklerViewAdapter
    lateinit var binding: FragmentAllCitizensBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllCitizensBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        setHasOptionsMenu(true)
        binding.apply {
            toolBar.overflowIcon?.colorFilter =
                BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
            toolBar.setNavigationOnClickListener { findNavController().popBackStack() }
//            toolBar.inflateMenu(R.menu.my_menu)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        list = ArrayList()
        list1 = ArrayList()
        appDataBase = AppDataBase.getInstance(requireContext())
        if (appDataBase.passportsDao().getAll().isNotEmpty()) {
            for (passport in appDataBase.passportsDao().getAll()) {
                list.add(passport)
            }
        }
        list1.addAll(list)
        myRecyclerViewAdapter = MyRecycklerViewAdapter(list1, onRootClickListener())
        binding.rv.adapter = myRecyclerViewAdapter

    }

    fun onRootClickListener(): MyRecycklerViewAdapter.OnRootClickListener {
        val onRootClickListener = object : MyRecycklerViewAdapter.OnRootClickListener {
            override fun onRootClickListener(passport: Passport, position: Int) {
                val bundle = bundleOf("passport" to passport)
                findNavController().navigate(R.id.aboutCitizenFragment, bundle)
            }

        }
        return onRootClickListener
    }

    fun menuIconColor(menuItem: MenuItem, color: Int) {
        val drawable = menuItem.icon
        if (drawable != null) {
            drawable.mutate()
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        menuIconColor(searchItem, Color.YELLOW)
        val searchView = searchItem.actionView as SearchView


//
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText?.lowercase(Locale.getDefault())
                if (searchText!!.isNotEmpty()) {
                    list.forEach {
                        if (it.name?.lowercase(Locale.getDefault())!!
                                .contains(searchText)
                        ) {
                            list1.clear()
                            list1.add(it)

                            Log.d(TAG, "onQueryTextChange: true")
                        }
                    }
                    myRecyclerViewAdapter.notifyDataSetChanged()
                } else {
                    Log.d(TAG, "onQueryTextChange: true")
                    list1.clear()
                    list1.addAll(list)

                    myRecyclerViewAdapter.notifyDataSetChanged()
                }
                return true
            }

        })

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AllCitizensFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllCitizensFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}