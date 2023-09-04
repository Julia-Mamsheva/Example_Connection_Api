package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.databinding.ActivityImageLayoutBinding
import java.io.ByteArrayOutputStream
import java.util.*

class ImageLayout : AppCompatActivity() {
    private var encodedImage: String? = null
    private lateinit var binding: ActivityImageLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
    }
    private fun setListener() {
        binding.profileImg.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImg.launch(intent)
        }
    }

    private fun encodeImage(bitmap: Bitmap): String? {
        val prevW = 150
        val prevH = bitmap.height * prevW / bitmap.width
        val b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(bytes)
        } else ""
    }

    private val pickImg = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            if (result.data != null) {
                val uri = result.data!!.data
                try {
                    val `is` =
                        contentResolver.openInputStream(uri!!)
                    val bitmap = BitmapFactory.decodeStream(`is`)
                    binding.profileImg.setImageBitmap(bitmap)
                    encodedImage = encodeImage(bitmap)
                } catch (_: Exception) {
                }
            }
        }
    }

}