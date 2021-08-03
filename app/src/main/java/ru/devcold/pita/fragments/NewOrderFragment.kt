package ru.devcold.pita.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.devcold.pita.NewPhotoAdapter
import ru.devcold.pita.R
import ru.devcold.pita.databinding.FragmentNewOrderBinding
import java.io.File

class NewOrderFragment : Fragment() {

    private lateinit var binding: FragmentNewOrderBinding
    private val adapter = NewPhotoAdapter()
    private lateinit var currentPhotoPath: String

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        private const val IMAGE_CHOOSE = 1000;
        private const val PERMISSION_CODE = 1001;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewOrderBinding.inflate(inflater, container, false)

        requireActivity().title = resources.getString(R.string.new_announcement)

        binding.newPhotoRecycler.adapter = adapter

        setUpViews()

        return binding.root
    }

    private fun setUpViews() {
        binding.addPhoto.setOnClickListener {
            createPhotoAlertDialog()
        }
    }

    private fun createPhotoAlertDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        with(builder) {
            setTitle(R.string.add_photo)
            setPositiveButton(R.string.take_photo) { dialog, id ->
                takePhoto()
            }
            setNegativeButton(R.string.choose_from_gallery) { dialog, id ->
                chooseImageGallery()
            }
            show()
        }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "You have not camera!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //binding.image.setImageBitmap(imageBitmap)
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}