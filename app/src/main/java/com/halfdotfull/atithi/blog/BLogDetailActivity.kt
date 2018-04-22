package com.halfdotfull.atithi.blog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.halfdotfull.atithi.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_blog_detail.*

class BLogDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detail)
        titleTv.text=intent.getStringExtra("title")
        location.text=intent.getStringExtra("location")
        blog.text=intent.getStringExtra("content")
        authorName.text=intent.getStringExtra("author")
        if(intent.getStringExtra("img") != "")
            Picasso.get().load(intent.getStringExtra("img")).into(blogIv)
    }
}