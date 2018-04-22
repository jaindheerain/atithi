package com.halfdotfull.atithi.blog

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.halfdotfull.atithi.R
import com.halfdotfull.atithi.RetrofitSingelton
import com.halfdotfull.atithi.blog.Model.Blog
import com.halfdotfull.atithi.blog.retrofit.LikeApi
import com.halfdotfull.atithi.dashboard.HomeScreenActivity
import com.halfdotfull.atithi.dashboard.HomeScreenActivity.blogArrayList
import com.sackcentury.shinebuttonlib.ShineButton
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by nexflare on 18/03/18.
 */

class BlogAdapter
//    ArrayList<Blog> blogArrayList;

(private val context: Context, blogArrayList: ArrayList<Blog>) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
    private val likeApi: LikeApi

    init {
        likeApi = RetrofitSingelton.createService1(LikeApi::class.java)
        //this.blogArrayList=blogArrayList;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_blog, parent, false)

        return BlogViewHolder(v)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {

        holder.location.text = blogArrayList[position].location
        holder.time.text = blogArrayList[position].timeStamp
        holder.description.text = blogArrayList[position].brief
        holder.cardView.setOnClickListener {
            val intent = Intent(context, BLogDetailActivity::class.java)
            intent.putExtra("content", blogArrayList[position].content)
            intent.putExtra("location", blogArrayList[position].location)
            intent.putExtra("img", blogArrayList[position].img)
            intent.putExtra("title", blogArrayList[position].title)
            intent.putExtra("author", blogArrayList[position].author)
            context.startActivity(intent)
        }

        if (blogArrayList[position].img != null && blogArrayList[position].img.length > 5) {
            Picasso.get().load(blogArrayList[position].img).into(holder.blogImage)
        } else {
            holder.blogImage.setImageResource(R.drawable.rajasthannight)
        }
        holder.likeBtn.isChecked = blogArrayList[position].liked == 1
        holder.likeBtn.setOnClickListener {
            if (blogArrayList[position].liked == 0) {
                val like_blog = RequestBody.create(MediaType.parse("text/plain"), "like_blog")
                val numberRequest = RequestBody.create(MediaType.parse("text/plain"), (context as HomeScreenActivity).firebaseAuth.currentUser!!.phoneNumber!!.substring(3))
                val userRequest = RequestBody.create(MediaType.parse("text/plain"), context.firebaseAuth.currentUser!!.uid)
                val token = RequestBody.create(MediaType.parse("text/plain"), context.sharedPreferences.getString("token", "")!!)
                val blogId = RequestBody.create(MediaType.parse("text/plain"), blogArrayList[position].blogId)
                likeApi.getLike(like_blog, numberRequest, userRequest, token, blogId).enqueue(object : Callback<LikeResponse> {
                    override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                        if (response.isSuccessful) {
                            if (response.body()!!.success == 1) {
                                Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
                                blogArrayList[position].liked = 1
                            } else {
                                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show()
                                blogArrayList[position].liked = 0
                            }
                        }
                    }

                    override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                        Log.e("TAGGERS", "onFailure: " + t.message.toString())
                    }
                })
            } else {
                val like_blog = RequestBody.create(MediaType.parse("text/plain"), "unlike_blog")
                val numberRequest = RequestBody.create(MediaType.parse("text/plain"), (context as HomeScreenActivity).firebaseAuth.currentUser!!.phoneNumber!!.substring(3))
                val userRequest = RequestBody.create(MediaType.parse("text/plain"), context.firebaseAuth.currentUser!!.uid)
                val token = RequestBody.create(MediaType.parse("text/plain"), context.sharedPreferences.getString("token", "")!!)
                val blogId = RequestBody.create(MediaType.parse("text/plain"), blogArrayList[position].blogId)
                likeApi.getLike(like_blog, numberRequest, userRequest, token, blogId).enqueue(object : Callback<LikeResponse> {
                    override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                        if (response.isSuccessful) {
                            if (response.body()!!.success == 1) {
                                Toast.makeText(context, "UnLiked", Toast.LENGTH_SHORT).show()
                                blogArrayList[position].liked = 1
                            } else {
                                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show()
                                blogArrayList[position].liked = 0
                            }
                        }
                    }

                    override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                        Log.e("TAGGERS", "onFailure: " + t.message.toString())
                    }
                })
            }
        }

    }

    override fun getItemCount(): Int {
        return blogArrayList.size
    }


    inner class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var location: TextView
        internal var time: TextView
        internal var description: TextView
        internal var blogImage: ImageView
        internal var likeBtn: ShineButton
        internal var like: RelativeLayout
        internal var share: RelativeLayout
        internal var cardView: CardView

        init {
            cardView = itemView.findViewById(R.id.blogCv)
            location = itemView.findViewById(R.id.locationBlog)
            time = itemView.findViewById(R.id.timeBlog)
            description = itemView.findViewById(R.id.description)
            likeBtn = itemView.findViewById(R.id.likeBtn)
            blogImage = itemView.findViewById(R.id.imageBlog)
            like = itemView.findViewById(R.id.likeLayout)
            share = itemView.findViewById(R.id.shareLayout)

        }
    }
}



