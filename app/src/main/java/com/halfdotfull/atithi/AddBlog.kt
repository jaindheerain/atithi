package com.halfdotfull.atithi

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_blog.*
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.R.attr.data
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.places.ui.PlaceAutocomplete


class AddBlog : AppCompatActivity() {

    private val GET_IMAGE_DATA=7322

    private val REQUEST_CODE_PLACE=8066
    lateinit var latitude:String
    lateinit var longitude:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_blog)
        addImageBtn.setOnClickListener{
            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i,GET_IMAGE_DATA)
        }
        addLocationBtn.setOnClickListener{
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this@AddBlog)
            startActivityForResult(intent, REQUEST_CODE_PLACE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==GET_IMAGE_DATA){
            if(resultCode== Activity.RESULT_OK){
                val selectedImage = data?.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                val name=picturePath.substring(picturePath.lastIndexOf("/")+1)
                imageName.text=name
                Log.d("TAGGER_PATH",picturePath)
                cursor.close()
            }
            else{
                Toast.makeText(this@AddBlog,"Problem",Toast.LENGTH_SHORT).show()
            }
        }
        else if (requestCode == REQUEST_CODE_PLACE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                Log.d("TAGGER_LOCATION",place.latLng.toString())
                latitude=place.latLng.latitude.toString()
                longitude=place.latLng.longitude.toString()
            }
        }
    }
}
