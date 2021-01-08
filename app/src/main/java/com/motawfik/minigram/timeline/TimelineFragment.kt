package com.motawfik.minigram.timeline

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.motawfik.minigram.R
import com.motawfik.minigram.databinding.TimelineFragmentBinding
import com.motawfik.minigram.viewModels.ViewModelFactory


class TimelineFragment : Fragment() {
    private lateinit var binding: TimelineFragmentBinding
    private lateinit var viewModel: TimelineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        binding = TimelineFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val viewModelFactory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TimelineViewModel::class.java)
        binding.viewModel = viewModel


        binding.speedDial.addAllActionItems(
            listOf(
                SpeedDialActionItem.Builder(R.id.fab_upload, R.drawable.ic_baseline_add_a_photo_24)
                    .setLabel("Take New Photo")
                    .setLabelClickable(false)
                    .setFabImageTintColor(Color.WHITE)
                    .create(),
                SpeedDialActionItem.Builder(
                    R.id.fab_pick,
                    R.drawable.ic_baseline_add_photo_alternate_24
                )
                    .setLabel("Upload Photo")
                    .setLabelClickable(false)
                    .setFabImageTintColor(Color.WHITE)
                    .create()
            )
        )

        binding.speedDial.setOnActionSelectedListener { item ->
            when (item.id) {
                R.id.fab_upload -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    imageTakerLauncher.launch(takePicture)
                    binding.speedDial.close()
                    return@setOnActionSelectedListener true
                }
                R.id.fab_pick -> {
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    imagePickerLauncher.launch(pickPhoto)
                    binding.speedDial.close()
                    return@setOnActionSelectedListener true
                }
            }
            false
        }


        val adapter = TimelineAdapter(PostListener { postID ->
            viewModel.likePost(postID)
        })
        binding.postsList.adapter = adapter
        viewModel.getPosts()
        viewModel.posts.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })


        return binding.root
    }

    private var imageTakerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val bitmap = intent?.extras?.get("data") as Bitmap
                viewModel.uploadNewImage(bitmap)
            }
        }


    private var imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val uri = intent?.data
                viewModel.uploadSavedImage(uri!!)
            }
        }
}