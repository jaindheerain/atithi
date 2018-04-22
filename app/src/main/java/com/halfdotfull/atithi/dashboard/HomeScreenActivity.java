package com.halfdotfull.atithi.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.halfdotfull.atithi.AddBlog;
import com.halfdotfull.atithi.R;
import com.halfdotfull.atithi.RetrofitSingelton;
import com.halfdotfull.atithi.blog.BlogAdapter;
import com.halfdotfull.atithi.blog.Model.Blog;
import com.halfdotfull.atithi.blog.Model.BlogModel;
import com.halfdotfull.atithi.blog.retrofit.BlogApi;
import com.halfdotfull.atithi.category.view.Categories;
import com.halfdotfull.atithi.login.View.LoginActivity;
import com.halfdotfull.atithi.marketplace.ShopList;
import com.halfdotfull.atithi.planmytrip.PlanMyTrip;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public FirebaseAuth firebaseAuth;

    TextView userName;
    TextView planMyTrip,localArt;
    RecyclerView blogRv;
    ImageView category,filter;


    Boolean cont=true;
    private BlogApi blogApi;
    public static ArrayList<Blog> blogArrayList;
    BlogAdapter blogAdapter;
    private boolean loading = true;
    private int visibleThreshold = 2;
    private ProgressDialog progressDialog;
    static int Flag;
    int pages=1;
    private int previousTotal = 0;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    LinearLayoutManager layoutManagerLinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        sharedPreferences=getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();
        blogArrayList=new ArrayList<>();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        blogApi= RetrofitSingelton.INSTANCE.createService1(BlogApi.class);
        firebaseAuth=FirebaseAuth.getInstance();
        blogRv= findViewById(R.id.blogRv);
        layoutManagerLinear=new LinearLayoutManager(this);
        blogRv.setLayoutManager(layoutManagerLinear);
        blogAdapter=new BlogAdapter(this,new ArrayList<Blog>());
        blogRv.setAdapter(blogAdapter);
        setSupportActionBar(toolbar);

        planMyTrip=findViewById(R.id.planMyTrip);
        localArt=findViewById(R.id.localArt);
        category=findViewById(R.id.categoryBlogs);
        filter=findViewById(R.id.filter);

        findViewById(R.id.appBar).bringToFront();
//        getActionBar().setElevation(0);

        pagenation();
        //callApi("1","1");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreenActivity.this, AddBlog.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        userName=headerView.findViewById(R.id.userName);
        Log.d("TAGGER",firebaseAuth.getCurrentUser().getPhoneNumber());
//        Log.d("TAGGER",firebaseAuth.getCurrentUser().getDisplayName());
       // userName.setText(firebaseAuth.getCurrentUser().getDisplayName());
        navigationView.setNavigationItemSelectedListener(this);


        planMyTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNew(PlanMyTrip.class);
            }
        });localArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNew(ShopList.class);
            }
        });category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNew(Categories.class);
            }
        });filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void callApi(String typeArg,String pageArg) {
        RequestBody get_blogs = RequestBody.create(MediaType.parse("text/plain"), "get_blogs");
        RequestBody numberRequest=RequestBody.create(MediaType.parse("text/plain"),firebaseAuth.getCurrentUser().getPhoneNumber().substring(3));
        RequestBody userRequest=RequestBody.create(MediaType.parse("text/plain"),firebaseAuth.getCurrentUser().getUid());
        RequestBody  token=RequestBody.create(MediaType.parse("text/plain"),sharedPreferences.getString("token",""));
        RequestBody type=RequestBody.create(MediaType.parse("text/plain"),typeArg);
        RequestBody page=RequestBody.create(MediaType.parse("text/plain"),pageArg);
        if(cont) {

            blogApi.getBlogs(get_blogs, numberRequest, userRequest, token, type, page).enqueue(new Callback<BlogModel>() {
                @Override
                public void onResponse(Call<BlogModel> call, Response<BlogModel> response) {
                    if (response.isSuccessful()) {
                        Log.d("TAGGER", "onResponse: " + response.body().toString());
                        progressDialog.dismiss();
                        if(response.body().getBlog()==null){
                            Toast.makeText(HomeScreenActivity.this, "NULL", Toast.LENGTH_SHORT).show();
                            cont = false;
                            pages=1;
                        }else{

                            if (response.body().getBlog().size() > 0) {
                                //for (int i = 0; i < response.body().getBlog().size(); i++) {
                                    blogArrayList.addAll(response.body().getBlog());
                                    blogAdapter.notifyDataSetChanged();
                                    pages++;
                                Toast.makeText(HomeScreenActivity.this, ""+pages, Toast.LENGTH_SHORT).show();
                                //}
                            }
                            Toast.makeText(HomeScreenActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }

                        }

                }

                @Override
                public void onFailure(Call<BlogModel> call, Throwable t) {
                    Log.e("TAGGER", "onFailure: " + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            firebaseAuth.signOut();
            editor.putBoolean("login",false).commit();
            editor.putString("token","").commit();
            startActivity(new Intent(HomeScreenActivity.this,LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment=null;

        if (id == R.id.nav_camera) {
           openNew(PlanMyTrip.class);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            openNew(ShopList.class);

        } else if (id == R.id.nav_manage) {

        }
        /*if(fragment!=null){
            loadFragment(fragment);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void openNew(Class activity){
        Intent newWindow=new Intent(HomeScreenActivity.this, activity);
        startActivity(newWindow);

    }


    void pagenation(){
        callApi(String.valueOf(2),String.valueOf(pages));
        blogRv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);



                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = layoutManagerLinear.getItemCount();
                    firstVisibleItem = layoutManagerLinear.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                        callApi(String.valueOf(2),String.valueOf(pages));
                        Log.i("Yaeye!", "end called");

                        // Do something

                        loading = true;
                    }
            }
        });
    }
}
