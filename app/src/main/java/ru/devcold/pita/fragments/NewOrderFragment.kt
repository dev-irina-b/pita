package ru.devcold.pita.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import ru.devcold.pita.BuildConfig
import ru.devcold.pita.NewPhotoAdapter
import ru.devcold.pita.R
import ru.devcold.pita.databinding.FragmentNewOrderBinding
import java.io.File

class NewOrderFragment : Fragment() {

    private lateinit var binding: FragmentNewOrderBinding
    private val adapter = NewPhotoAdapter()
    private lateinit var tmpUri: Uri

    private fun getNewTmpUri(): Uri {
        val file = File(requireContext().cacheDir, "tmpFile_${System.currentTimeMillis()}")
        file.createNewFile()
        file.deleteOnExit()
        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", file)
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                val newList = adapter.currentList.toMutableList()
                newList.add(tmpUri)
                adapter.submitList(newList)
                checkAdapterListSize(adapter)
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            val newList = adapter.currentList.toMutableList()
            newList.addAll(it)
            if(newList.size > 10) {
                adapter.submitList(newList.subList(0,10))
                checkAdapterListSize(adapter)
            } else {
                adapter.submitList(newList)
                checkAdapterListSize(adapter)
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewOrderBinding.inflate(inflater, container, false)

        binding.newPhotoRecycler.adapter = adapter

        requireActivity().title = resources.getString(R.string.new_announcement)

        setUpViews()

        return binding.root
    }

    private fun setUpViews() {
        binding.addPhoto.setOnClickListener {
            createPhotoAlertDialog()
        }

        binding.add.setOnClickListener {  }
    }

    private fun createPhotoAlertDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.add_photo)
            .setPositiveButton(R.string.take_photo) { _, _ ->
                takeImage()
            }
            .setNegativeButton(R.string.choose_from_gallery) { _, _ ->
                selectImageFromGallery()
            }
            .show()
    }

    private fun takeImage() {
        tmpUri = getNewTmpUri()
        takeImageResult.launch(tmpUri)
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun checkAdapterListSize(checkAdapter: NewPhotoAdapter) {
        if(checkAdapter.currentList.size >=10) {
            binding.addPhoto.visibility = View.GONE
        } else binding.addPhoto.visibility = View.VISIBLE
        //my change
    }
}