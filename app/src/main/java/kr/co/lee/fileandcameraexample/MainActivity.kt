package kr.co.lee.fileandcameraexample

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val WRITE_STORAGE: Int = 1
    }

    lateinit var showImageBtn: Button
    lateinit var fileCameraBtn: Button
    lateinit var resultImageView: ImageView

    var write_flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showImageBtn = findViewById(R.id.show_image)
        fileCameraBtn = findViewById(R.id.btn_camera_file)
        resultImageView = findViewById(R.id.result_image_view)

        showImageBtn.setOnClickListener(buttonListener)
        fileCameraBtn.setOnClickListener(buttonListener)
        resultImageView.setOnClickListener(buttonListener)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_STORAGE)
        }
    }

    val buttonListener = View.OnClickListener {
        when(it) {
            showImageBtn -> {
                val intent = Intent(this, ImageViewActivity::class.java)
                startActivity(intent)
            }

            fileCameraBtn -> {
                if(write_flag) {
                    // Add a media item that other apps shouldn't see until the item is
                    // fully written to the media store.
                    val resolver = applicationContext.contentResolver

                    val collection =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            MediaStore.Audio.Media.getContentUri(
                                MediaStore.VOLUME_EXTERNAL_PRIMARY
                            )
                        } else {
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }

                    val details = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, "Image1.jpg")
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                        put(MediaStore.Images.Media.IS_PENDING, 1)
                    }

                    val imageContentUri = resolver.insert(collection, details)

                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageContentUri)
                    startActivityForResult(intent, 40)
                } else {
                    showToast("WRITE_EXTERNAL_PERMISSION 거부")
                }
            }
            resultImageView -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 30 && resultCode == RESULT_OK) {
            println("TestMessage1")
        } else if(requestCode == 40 && resultCode == RESULT_OK) {
            println(data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == WRITE_STORAGE && grantResults.isNotEmpty()) {
            write_flag = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}