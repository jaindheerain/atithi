package com.halfdotfull.atithi.dashboard

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.halfdotfull.atithi.AddBlog
import com.halfdotfull.atithi.R
import com.halfdotfull.atithi.RetrofitSingelton
import com.halfdotfull.atithi.blog.BlogAdapter
import com.halfdotfull.atithi.blog.Model.Blog
import com.halfdotfull.atithi.blog.Model.BlogModel
import com.halfdotfull.atithi.blog.retrofit.BlogApi
import com.halfdotfull.atithi.category.view.Categories
import com.halfdotfull.atithi.login.View.LoginActivity
import com.halfdotfull.atithi.marketplace.ShopList
import com.halfdotfull.atithi.planmytrip.PlanMyTrip
import kotlinx.android.synthetic.main.app_bar_homescreen.*

import java.util.ArrayList

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeScreenActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var firebaseAuth: FirebaseAuth

    lateinit var userName: TextView
    lateinit var planMyTrip: TextView
    lateinit var localArt: TextView
    lateinit var blogRv: RecyclerView
    lateinit var category: ImageView
    lateinit var filter: ImageView


    internal var cont: Boolean? = true
    private var blogApi: BlogApi? = null
    lateinit var blogAdapter: BlogAdapter
    private var loading = true
    private val visibleThreshold = 2
    private var progressDialog: ProgressDialog? = null
    internal var pages = 1
    private var previousTotal = 0
    internal var firstVisibleItem: Int = 0
    internal var visibleItemCount: Int = 0
    internal var totalItemCount: Int = 0
    lateinit var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor? = null
    lateinit var layoutManagerLinear: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        blogArrayList = ArrayList()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Please Wait ...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        blogApi = RetrofitSingelton.createService1(BlogApi::class.java)
        firebaseAuth = FirebaseAuth.getInstance()
        blogRv = findViewById(R.id.blogRv)
        layoutManagerLinear = LinearLayoutManager(this)
        blogRv.layoutManager = layoutManagerLinear
        blogAdapter = BlogAdapter(this, ArrayList())
        blogRv.adapter = blogAdapter
        setSupportActionBar(toolbar)

        planMyTrip = findViewById(R.id.planMyTrip)
        localArt = findViewById(R.id.localArt)
        category = findViewById(R.id.categoryBlogs)
        filter = findViewById(R.id.filter)

        findViewById<View>(R.id.appBar).bringToFront()
        //        getActionBar().setElevation(0);

        pagenation()
        //callApi("1","1");

        fab.setOnClickListener { startActivity(Intent(this@HomeScreenActivity, AddBlog::class.java)) }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        userName = headerView.findViewById(R.id.userName)
        Log.d("TAGGER", firebaseAuth.currentUser!!.phoneNumber)
        //        Log.d("TAGGER",firebaseAuth.getCurrentUser().getDisplayName());
        // userName.setText(firebaseAuth.getCurrentUser().getDisplayName());
        navigationView.setNavigationItemSelectedListener(this)


        planMyTrip.setOnClickListener { openNew(PlanMyTrip::class.java) }
        localArt.setOnClickListener { openNew(ShopList::class.java) }
        category.setOnClickListener { openNew(Categories::class.java) }
        filter.setOnClickListener { }

    }

    private fun callApi(typeArg: String, pageArg: String) {
        val get_blogs = RequestBody.create(MediaType.parse("text/plain"), "get_blogs")
        val numberRequest = RequestBody.create(MediaType.parse("text/plain"), firebaseAuth.currentUser!!.phoneNumber!!.substring(3))
        val userRequest = RequestBody.create(MediaType.parse("text/plain"), firebaseAuth.currentUser!!.uid)
        val token = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString("token", "")!!)
        val type = RequestBody.create(MediaType.parse("text/plain"), typeArg)
        val page = RequestBody.create(MediaType.parse("text/plain"), pageArg)
        if (cont!!) {

            blogApi!!.getBlogs(get_blogs, numberRequest, userRequest, token, type, page).enqueue(object : Callback<BlogModel> {
                override fun onResponse(call: Call<BlogModel>, response: Response<BlogModel>) {
                    if (response.isSuccessful) {
                        Log.d("TAGGER", "onResponse: " + response.body()!!.toString())
                        progressDialog!!.dismiss()
                        if (response.body()!!.blog == null) {
                            Toast.makeText(this@HomeScreenActivity, "NULL", Toast.LENGTH_SHORT).show()
                            cont = false
                            pages = 1
                        } else {

                            if (response.body()!!.blog.size > 0) {
                                //for (int i = 0; i < response.body().getBlog().size(); i++) {
                                blogArrayList.addAll(response.body()!!.blog)
                                blogAdapter.notifyDataSetChanged()
                                pages++
                                Toast.makeText(this@HomeScreenActivity, "" + pages, Toast.LENGTH_SHORT).show()
                                //}
                            }
                            Toast.makeText(this@HomeScreenActivity, "Success", Toast.LENGTH_SHORT).show()
                        }

                    }

                }

                override fun onFailure(call: Call<BlogModel>, t: Throwable) {
                    Log.e("TAGGER", "onFailure: " + t.message)
                    progressDialog!!.dismiss()
                }
            })
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_sign_out) {
            firebaseAuth.signOut()
            editor!!.putBoolean("login", false).commit()
            editor!!.putString("token", "").commit()
            startActivity(Intent(this@HomeScreenActivity, LoginActivity::class.java))
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        val fragment: Fragment? = null

        if (id == R.id.nav_camera) {
            openNew(PlanMyTrip::class.java)
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            openNew(ShopList::class.java)

        } else if (id == R.id.nav_manage) {

        }
        /*if(fragment!=null){
            loadFragment(fragment);
        }*/

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    internal fun openNew(activity: Class<*>) {
        val newWindow = Intent(this@HomeScreenActivity, activity)
        startActivity(newWindow)

    }


    internal fun pagenation() {
        callApi(2.toString(), pages.toString())
        blogRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)



                visibleItemCount = recyclerView!!.childCount
                totalItemCount = layoutManagerLinear.itemCount
                firstVisibleItem = layoutManagerLinear.findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }
                if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                    // End has been reached
                    callApi(2.toString(), pages.toString())
                    Log.i("Yaeye!", "end called")

                    // Do something

                    loading = true
                }
            }
        })
    }

    companion object {
        lateinit var blogArrayList: ArrayList<Blog>
        internal var Flag: Int = 0
    }
}
