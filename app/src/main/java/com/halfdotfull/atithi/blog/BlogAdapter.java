package com.halfdotfull.atithi.blog;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.halfdotfull.atithi.R;
import com.halfdotfull.atithi.RetrofitSingelton;
import com.halfdotfull.atithi.blog.Model.Blog;
import com.halfdotfull.atithi.blog.retrofit.LikeApi;
import com.halfdotfull.atithi.dashboard.HomeScreenActivity;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.halfdotfull.atithi.dashboard.HomeScreenActivity.blogArrayList;

/**
 * Created by nexflare on 18/03/18.
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private Context context;
    private LikeApi likeApi;
//    ArrayList<Blog> blogArrayList;

    public BlogAdapter(Context context,ArrayList<Blog> blogArrayList){
        this.context=context;
        likeApi= RetrofitSingelton.createService1(LikeApi.class);
        //this.blogArrayList=blogArrayList;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.layout_blog,parent,false);

        return new BlogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BlogViewHolder holder, final int position) {

        holder.location.setText(HomeScreenActivity.Companion.getBlogArrayList().get(position).getLocation());
        holder.time.setText(HomeScreenActivity.Companion.getBlogArrayList().get(position).getTimeStamp());
        holder.description.setText(HomeScreenActivity.Companion.getBlogArrayList().get(position).getBrief());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent= new Intent(context,BLogDetailActivity.class);
               intent.putExtra("content", HomeScreenActivity.Companion.getBlogArrayList().get(position).getContent());
               intent.putExtra("location", HomeScreenActivity.Companion.getBlogArrayList().get(position).getLocation());
               intent.putExtra("img", HomeScreenActivity.Companion.getBlogArrayList().get(position).getImg());
               intent.putExtra("title", HomeScreenActivity.Companion.getBlogArrayList().get(position).getTitle());
               intent.putExtra("author", HomeScreenActivity.Companion.getBlogArrayList().get(position).getAuthor());
               context.startActivity(intent);
            }
        });

        if(HomeScreenActivity.Companion.getBlogArrayList().get(position).getImg()!=null&& HomeScreenActivity.Companion.getBlogArrayList().get(position).getImg().length()>5){
            Picasso.get().load(HomeScreenActivity.Companion.getBlogArrayList().get(position).getImg()).into(holder.blogImage);
        }
        else{
            holder.blogImage.setImageResource(R.drawable.rajasthannight);
        }
        if(HomeScreenActivity.Companion.getBlogArrayList().get(position).getLiked()==1)
            holder.likeBtn.setChecked(true);
        else
            holder.likeBtn.setChecked(false);
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(HomeScreenActivity.Companion.getBlogArrayList().get(position).getLiked()==0){
                    RequestBody like_blog = RequestBody.create(MediaType.parse("text/plain"), "like_blog");
                    RequestBody numberRequest=RequestBody.create(MediaType.parse("text/plain"), ((HomeScreenActivity) context).getFirebaseAuth().getCurrentUser().getPhoneNumber().substring(3));
                    RequestBody userRequest=RequestBody.create(MediaType.parse("text/plain"), ((HomeScreenActivity) context).getFirebaseAuth().getCurrentUser().getUid());
                    RequestBody  token=RequestBody.create(MediaType.parse("text/plain"), ((HomeScreenActivity) context).getSharedPreferences().getString("token",""));
                    RequestBody blogId=RequestBody.create(MediaType.parse("text/plain"), HomeScreenActivity.Companion.getBlogArrayList().get(position).getBlogId());
                    likeApi.getLike(like_blog,numberRequest,userRequest,token,blogId).enqueue(new Callback<LikeResponse>() {
                        @Override
                        public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                            if(response.isSuccessful()){
                                if(response.body().getSuccess()==1){
                                    Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
                                    HomeScreenActivity.Companion.getBlogArrayList().get(position).setLiked(1);
                                }
                                else{
                                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    HomeScreenActivity.Companion.getBlogArrayList().get(position).setLiked(0);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LikeResponse> call, Throwable t) {
                            Log.e("TAGGERS", "onFailure: "+t.getMessage().toString());
                        }
                    });
                }
                else{
                    RequestBody like_blog = RequestBody.create(MediaType.parse("text/plain"), "unlike_blog");
                    RequestBody numberRequest=RequestBody.create(MediaType.parse("text/plain"), ((HomeScreenActivity) context).getFirebaseAuth().getCurrentUser().getPhoneNumber().substring(3));
                    RequestBody userRequest=RequestBody.create(MediaType.parse("text/plain"), ((HomeScreenActivity) context).getFirebaseAuth().getCurrentUser().getUid());
                    RequestBody  token=RequestBody.create(MediaType.parse("text/plain"), ((HomeScreenActivity) context).getSharedPreferences().getString("token",""));
                    RequestBody blogId=RequestBody.create(MediaType.parse("text/plain"), HomeScreenActivity.Companion.getBlogArrayList().get(position).getBlogId());
                    likeApi.getLike(like_blog,numberRequest,userRequest,token,blogId).enqueue(new Callback<LikeResponse>() {
                        @Override
                        public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                            if(response.isSuccessful()){
                                if(response.body().getSuccess()==1){
                                    Toast.makeText(context, "UnLiked", Toast.LENGTH_SHORT).show();
                                    HomeScreenActivity.Companion.getBlogArrayList().get(position).setLiked(1);
                                }
                                else{
                                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    HomeScreenActivity.Companion.getBlogArrayList().get(position).setLiked(0);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LikeResponse> call, Throwable t) {
                            Log.e("TAGGERS", "onFailure: "+t.getMessage().toString());
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return HomeScreenActivity.Companion.getBlogArrayList().size();
    }


    public class BlogViewHolder extends RecyclerView.ViewHolder{

        TextView location,time,description;
        ImageView blogImage;
        ShineButton likeBtn;
        RelativeLayout like,share;
        CardView cardView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.blogCv);
            location=itemView.findViewById(R.id.locationBlog);
            time=itemView.findViewById(R.id.timeBlog);
            description=itemView.findViewById(R.id.description);
            likeBtn=itemView.findViewById(R.id.likeBtn);
            blogImage=itemView.findViewById(R.id.imageBlog);
            like=itemView.findViewById(R.id.likeLayout);
            share=itemView.findViewById(R.id.shareLayout);

        }
    }
}



