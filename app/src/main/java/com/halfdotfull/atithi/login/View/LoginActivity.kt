package com.halfdotfull.atithi.login.View

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.halfdotfull.atithi.dashboard.HomeScreenActivity
import com.halfdotfull.atithi.R
import com.halfdotfull.atithi.RetrofitSingelton
import com.halfdotfull.atithi.login.retrofit.LoginApi
import com.halfdotfull.atithi.login.model.loginResponse
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit



class LoginActivity : AppCompatActivity() {

    private var mobileEntered=false
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    private var dialog: ProgressDialog? = null
    var verificationId: String?=null
    var token: PhoneAuthProvider.ForceResendingToken?=null
    var number: String=""
    private lateinit var sharedPreferences:SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var mDatabase:FirebaseDatabase
    lateinit var loginApi: LoginApi
    private lateinit var task: Task<AuthResult>
    private var otpEntered: Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth= FirebaseAuth.getInstance()
        mDatabase= FirebaseDatabase.getInstance()
        loginApi= RetrofitSingelton.createService1(LoginApi::class.java)
        sharedPreferences=this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        dialog = ProgressDialog(this@LoginActivity)
        dialog!!.setMessage("Please wait...")
        dialog!!.setCancelable(false)
        editor=sharedPreferences.edit()
        loginBtn.setOnClickListener{
            if (!mobileEntered) {
                if(phoneNumberEt.text.toString().length==10) {
                    loginDetailTv.text = "OTP has been send to your \n mobile number. Please enter it below."
                    mobileEntered = true
                    number=phoneNumberEt.text.toString()
                    phoneNumberTil.visibility= View.GONE
                    otpTil.visibility=View.VISIBLE
                    if(phoneNumberTil.isErrorEnabled){
                        phoneNumberTil.isErrorEnabled=false
                    }
                    loginBtn.text = "LOGIN"
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91"+number,
                            60,
                            TimeUnit.SECONDS,
                            this@LoginActivity,
                            callback
                    )
                }
                else{
                    phoneNumberTil.error="Enter valid phone number"
                }
            }
            else if(!otpEntered){
                if(otpEt.text.toString().isNotEmpty()) {
                    val code = otpEt.text.toString()
                    val phoneAuthCredentials = PhoneAuthProvider.getCredential(this.verificationId!!, code)

                    dialog!!.show()
                    otpEntered=true
                    signInWithPhoneAuthCredential(phoneAuthCredentials)
                }
                else{
                    Toast.makeText(this@LoginActivity,"Please enter correct Otp",Toast.LENGTH_LONG).show()
                }
            }
            else{

                if(nameEt.text.toString().isEmpty())
                    nameTil.error="Please enter your name"
                else {
                    dialog!!.show()
                    val userProfile=UserProfileChangeRequest.Builder().setDisplayName(nameEt.text.toString()).build()
                    task.result.user.updateProfile(userProfile).addOnCompleteListener { res ->
                        if(res.isSuccessful) {
                            mDatabase.getReference("User").child(task.result.user.uid).child("Phone Number").setValue(task.result.user.phoneNumber)
                            val login = RequestBody.create(MediaType.parse("text/plain"), "login")
                            val numberRequest=RequestBody.create(MediaType.parse("text/plain"),number)
                            val userRequest=RequestBody.create(MediaType.parse("text/plain"),task.result.user.uid)
                            loginApi.getLoginResponse(login,
                                   numberRequest,
                                    userRequest).enqueue(object : retrofit2.Callback<loginResponse> {
                                override fun onFailure(call: Call<loginResponse>?, t: Throwable?) {
                                    dialog!!.dismiss()
                                    Log.e("TAGGERE",call.toString())
                                }

                                override fun onResponse(call: Call<loginResponse>?, response: Response<loginResponse>?) {
                                    Log.d("TAG",response.toString())
                                    Log.d("TAGGERS", response?.body().toString())
                                    Log.d("TAGGERS",response?.body().toString())
                                    if(response?.body()!!.success==1){
                                        dialog!!.dismiss()
                                        editor.putString("token", response.body()!!.token).commit()
                                        editor.putBoolean("login", true).commit()
                                        startActivity(Intent(this@LoginActivity, HomeScreenActivity::class.java))
                                        finishAffinity()
                                    }
                                }

                            })

                        } else{
                            dialog!!.dismiss()
                            Toast.makeText(this@LoginActivity,"Some Error Occurred. Try Again.",Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }
        callback=object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                if (p0 != null) {
                    dialog!!.show()
                    signInWithPhoneAuthCredential(p0)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                Toast.makeText(this@LoginActivity,p0?.localizedMessage, Toast.LENGTH_LONG).show()
                finish()
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(verificationId, token)
                this@LoginActivity.verificationId=verificationId
                this@LoginActivity.token=token
                loginBtn.isEnabled=true
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String?) {
                super.onCodeAutoRetrievalTimeOut(p0)
                this@LoginActivity.verificationId=verificationId

            }
        }

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, { task ->
                    this.task=task
                    if (task.isSuccessful) {
                        otpEntered=true
                        val mDatabase= FirebaseDatabase.getInstance()
                        mDatabase.getReference("User").child(task.result.user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError?) {

                            }

                            override fun onDataChange(p0: DataSnapshot?) {
                                if(p0!!.value!=null){
                                    Log.d("TAGGER",p0.value.toString())
                                    val login = RequestBody.create(MediaType.parse("text/plain"), "login")
                                    val numberRequest=RequestBody.create(MediaType.parse("text/plain"),number)
                                    val userRequest=RequestBody.create(MediaType.parse("text/plain"),task.result.user.uid)
                                    loginApi.getLoginResponse(login,
                                            numberRequest,
                                            userRequest).enqueue(object : retrofit2.Callback<loginResponse> {
                                        override fun onFailure(call: Call<loginResponse>?, t: Throwable?) {
                                            Toast.makeText(this@LoginActivity,"Some error occurred",Toast.LENGTH_SHORT).show()
                                        }

                                        override fun onResponse(call: Call<loginResponse>?, response: Response<loginResponse>?) {
                                            Log.d("TAG", response?.toString())
                                            Log.d("TAGGERS",response?.body().toString())
                                            dialog?.dismiss()
                                            if(response?.body()!!.success==1){
                                                editor.putString("token", response.body()!!.token).commit()
                                                editor.putBoolean("login", true).commit()
                                                startActivity(Intent(this@LoginActivity, HomeScreenActivity::class.java))
                                                finishAffinity()
                                            }
                                            else{
                                                Toast.makeText(this@LoginActivity,"Unable to Login",Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                    })
                                }
                                else{
                                    dialog?.dismiss()
                                    otpTil.visibility=View.GONE
                                    nameTil.visibility=View.VISIBLE
                                    loginBtn.text = "Done"

                                }
                            }

                        })

                        Log.d("TAGGER", "signInWithCredential:success")

                    } else {
                        dialog?.dismiss()
                        // Sign in failed, display a message and update the UI
                        Log.w("TAGGER", "signInWithCredential:failure", task.exception)


                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this@LoginActivity,"Please enter a valid otp.",Toast.LENGTH_LONG).show()
                        }
                        if(task.exception is FirebaseNetworkException){
                            Toast.makeText(this@LoginActivity,"Please check your network connection",Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }
                })
    }
}
