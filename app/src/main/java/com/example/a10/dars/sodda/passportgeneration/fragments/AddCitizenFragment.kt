package com.example.a10.dars.sodda.passportgeneration.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.a10.dars.sodda.passportgeneration.R
import com.example.a10.dars.sodda.passportgeneration.databinding.DialogLayoutBinding
import com.example.a10.dars.sodda.passportgeneration.databinding.FragmentAddCitizenBinding
import com.example.a10.dars.sodda.passportgeneration.room.dataBase.AppDataBase
import com.example.a10.dars.sodda.passportgeneration.room.entity.Passport
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddCitizenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCitizenFragment : Fragment() {
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

    lateinit var listOfRegions: ArrayList<String>
    lateinit var spinnerAdapter: ArrayAdapter<String>
    lateinit var binding: FragmentAddCitizenBinding
    var pathImage: String? = null
    lateinit var gendersSpinner: ArrayAdapter<String>
    var chosenGender: String? = null
    var region: String? = null
    lateinit var appDataBase: AppDataBase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCitizenBinding.inflate(inflater, container, false)

        binding.apply {
            toolBar.setNavigationOnClickListener { findNavController().popBackStack() }
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        setRegionsToSpinner()
        setOnclickMethods()
        setGendersToSpinner()
        appDataBase = AppDataBase.getInstance(requireContext())
    }

    private fun setGendersToSpinner() {
        val gendersList = arrayListOf<String>("Erkak", "Ayol")
        gendersSpinner = ArrayAdapter(requireContext(), R.layout.drop_down_item, gendersList)
        binding.gender.setAdapter(gendersSpinner)
    }

    private fun randomLetters(): String = List(2) {
        (('A'..'Z')).random()
    }.joinToString("")

    private fun randomNumbers(): String = List(7) {
        ((0..9)).random()
    }.joinToString("")

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    private fun setOnclickMethods() {

        binding.apply {
            var letters = randomLetters()
            var numbers = randomNumbers()
            chooseRegion.setOnItemClickListener { adapterView, view, i, l ->
                region = chooseRegion.text.toString()
            }
            image.setOnClickListener {
                askPermission()
            }
            edtPassportBestBefore.setOnClickListener {
                Toast.makeText(requireContext(), "avtomatic tanlanadi", Toast.LENGTH_SHORT)
                    .show()
            }
            edtDate.setOnClickListener {
                Toast.makeText(requireContext(), "data avtomatic belgilanadi", Toast.LENGTH_SHORT)
                    .show()
            }
            gender.setOnItemClickListener { adapterView, view, i, l ->
                chosenGender = gender.text.toString()
            }
            btnSave.setOnClickListener {

                var number = letters + numbers
                val name = edtName.text?.trim().toString()
                val surName = edtSurname.text?.trim().toString()
                val middleName = edtMiddlename.text?.trim().toString()
                val city = edtCity.text?.trim().toString()
                val description = edtDescription.text?.trim().toString()
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val currentDate = sdf.format(Date())
                val date = currentDate.split(' ')
                var dateTime = LocalDateTime.now()
                dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy H:m:ss"))
                dateTime = dateTime.plusYears(10)

                val dateBestBefore = "${dateTime.dayOfMonth}/${dateTime.month}/${dateTime.year}"
                if (name.isNotEmpty() && surName.isNotEmpty() && middleName.isNotEmpty() && city.isNotEmpty() && description.isNotEmpty() && chosenGender != null && region != null && pathImage != null) {
                    val dialog = AlertDialog.Builder(requireContext())
                    val create = dialog.create()
                    create.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val customView =
                        DialogLayoutBinding.inflate(layoutInflater)
                    customView.apply {
                        tvOk.setOnClickListener {

                            val passport = Passport(
                                name,
                                surName,
                                middleName,
                                number,
                                region,
                                city,
                                description,
                                date[0],
                                dateBestBefore,
                                chosenGender,
                                pathImage
                            )
                            appDataBase.passportsDao().insertPassport(passport)
                            findNavController().popBackStack()
                            create.cancel()
                        }
                        tvNo.setOnClickListener {
                            create.cancel()
                        }
                    }
                    create.setView(customView.root)
                    create.show()


                } else {
                    Toast.makeText(requireContext(), "fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setRegionsToSpinner() {
        listOfRegions = ArrayList()
        listOfRegions.add("Toshkent")
        listOfRegions.add("Toshkent viloyati")
        listOfRegions.add("Namangan viloyati")
        listOfRegions.add("Andijon viloyati")
        listOfRegions.add("Fargona viloyati")
        listOfRegions.add("Jizzah viloyati")
        listOfRegions.add("Samarqand viloyati")
        listOfRegions.add("Buhoro viloyati")
        listOfRegions.add("Qashqadaryo viloyati")
        listOfRegions.add("Surhandaryo viloyati")
        listOfRegions.add("Sirdaryo viloyati")
        listOfRegions.add("Navoi viloyati")
        listOfRegions.add("Xorazm viloyati")
        spinnerAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, listOfRegions)
        binding.chooseRegion.setAdapter(spinnerAdapter)


    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.image.setImageURI(uri)
            val contentResolver = requireContext().contentResolver
            val openInputStream = requireActivity().contentResolver.openInputStream(uri)
            val count = System.currentTimeMillis()
            val file = File(requireContext().filesDir, "image${count.toString()}.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()
            val fileAbsolutePath = file.absolutePath
            pathImage = file.absolutePath
        }

    private fun askPermission() {
        Dexter.withContext(binding.root.context)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener, PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    getImageFromGallery()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Snackbar.make(
                        binding.root,
                        "Please permit the permission through Settings screen.",
                        Snackbar.LENGTH_LONG
                    ).setAction("Settings", object : View.OnClickListener {
                        override fun onClick(p0: View?) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri: Uri =
                                Uri.fromParts("package", requireContext().packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }

                    })
                        .show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: com.karumi.dexter.listener.PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        getImageFromGallery()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Permission Denied! plz enable application permission from app settings!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).onSameThread().check()
    }

    fun getImageFromGallery() {
        getImageContent.launch("image/*")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddCitizenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCitizenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}