package com.halfdotfull.atithi.planmytrip;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.halfdotfull.atithi.R;
import com.halfdotfull.atithi.RetrofitSingelton;
import com.halfdotfull.atithi.blog.retrofit.BlogApi;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanMyTrip extends AppCompatActivity {

    public SharedPreferences sharedPreferences;
    private BlogApi blogApi;
    static ArrayList<String> arrayListLocation;
    EditText hourFirst,hourSecond;
    CheckBox food,hidden,shopping;
    ProgressDialog dialog;
    Button ok;
    ArrayList<String> interest;
    JSONArray inte = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_my_trip);

        sharedPreferences=getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);

        food=findViewById(R.id.boolEating);
        hidden=findViewById(R.id.boolHidden);
        shopping=findViewById(R.id.boolShop);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait ...");
        dialog.setCancelable(false);
        hourFirst=findViewById(R.id.hourFirst);
        hourSecond=findViewById(R.id.hourSecond);

        arrayListLocation=new ArrayList<>();
        blogApi= RetrofitSingelton.INSTANCE.createService1(BlogApi.class);

        ok=findViewById(R.id.loginBtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inte = new JSONArray();
                if(food.isChecked())
                    inte.put("food");
                if(hidden.isChecked())
                    inte.put("untapped");
                if(shopping.isChecked())
                    inte.put("shopping");
                dialog.show();
                callApi(""+hourFirst.getText().toString()+hourSecond.getText().toString(),"26.9167","75.8167 ");

            }
        });


    }

    private void callApi(String hour,String lat,String lon) {

        RequestBody get_blogs = RequestBody.create(MediaType.parse("text/plain"), "plan_my_trip");

        RequestBody  token=RequestBody.create(MediaType.parse("text/plain"),sharedPreferences.getString("token",""));
        RequestBody user_id=RequestBody.create(MediaType.parse("text/plain"),FirebaseAuth.getInstance().getCurrentUser().getUid());
        RequestBody phone_no=RequestBody.create(MediaType.parse("text/plain"),FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3));
        RequestBody type=RequestBody.create(MediaType.parse("text/plain"),hour);
        RequestBody lati=RequestBody.create(MediaType.parse("text/plain"),lat);
        RequestBody loni=RequestBody.create(MediaType.parse("text/plain"),lon);
        RequestBody radius=RequestBody.create(MediaType.parse("text/plain"),"10");
        Log.e("Tagger",String.valueOf(inte));
        RequestBody interests=RequestBody.create(MediaType.parse("text/plain"),String.valueOf(inte));


            blogApi.getPlan(get_blogs,token,user_id,phone_no, type,lati,loni,radius,interests).enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    if (response.isSuccessful()) {
                        Log.d("TAGGER", "onResponse: " + response.body().toString());

                                //for (int i = 0; i < response.body().getBlog().size(); i++) {
                                //Toast.makeText(HomeScreenActivity.this, ""+pages, Toast.LENGTH_SHORT).show();
                                //}
                        dialog.dismiss();
                                arrayListLocation.addAll(response.body());
                                Intent i=new Intent(PlanMyTrip.this,TimeLineActivity.class);
                                startActivity(i);

                            //Toast.makeText(HomeScreenActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                        else {
                        Intent i=new Intent(PlanMyTrip.this,TimeLineActivity.class);
                        startActivity(i);
                        arrayListLocation.add("Jaipur");
                        arrayListLocation.add("Jaipur");
                        arrayListLocation.add("Jaipur");
                        arrayListLocation.add("Jaipur");
                        arrayListLocation.add("Jaipur");
                        arrayListLocation.add("Jaipur");
                        arrayListLocation.add("Jaipur");
                        arrayListLocation.add("Jaipur");
                    }

                    }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("TAGGER", "onFailure: " + t.getMessage());
                    Intent i=new Intent(PlanMyTrip.this,TimeLineActivity.class);
                    startActivity(i);
                }
            });
        }
    }

