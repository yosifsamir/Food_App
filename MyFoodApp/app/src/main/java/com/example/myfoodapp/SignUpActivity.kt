package com.example.myfoodapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.model.User
import com.example.myfoodapp.ui.home.HomeActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.*
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private var circleProgressDialog: CircleProgressDialog? = null

    private lateinit var usernameEdt: EditText
    private lateinit var emailEdt:EditText
    private lateinit var passwordEdt:EditText
    private lateinit var signUpBtn:Button

    private lateinit var username: String
    private  lateinit var email:String
    private  lateinit var password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUserAvailable()

        setContentView(R.layout.activity_sign_up)

        initViews()
        iniFirebase()
        signUpBtn.setOnClickListener({
            register()
        })
    }

    private fun iniFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference
    }

    private fun initViews() {
        usernameEdt = findViewById<EditText>(R.id.edtNameSignUp)
        emailEdt = findViewById<EditText>(R.id.etEmailSignUp)
        passwordEdt = findViewById<EditText>(R.id.edtPasswordSignUp)
        signUpBtn=findViewById(R.id.btnSignUp2)

    }

    override fun onStart() {
        super.onStart()
//        checkUserAvailable()
    }

    private fun checkUserAvailable() {
        if (FirebaseAuth.getInstance().currentUser!=null){
            FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().uid!!).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        Comm.currentUser=snapshot.getValue(User::class.java)
                        goHome()
                        finish()
                    }
                })
        }
    }


    fun goHome() {
        startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
    }
    fun goSignIn() {
        startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
    }

    fun register() {
        username = usernameEdt.getText().toString()
        email = emailEdt.getText().toString()
        password = passwordEdt.getText().toString()
        if (TextUtils.isEmpty(username)) {
            usernameEdt.requestFocus()
            usernameEdt.setError("Enter your name")
            return
        }
        if (TextUtils.isEmpty(email)) {
            emailEdt.requestFocus()
            emailEdt.setError("Enter your Email")
            return
        }
        if (TextUtils.isEmpty(password)) {
            passwordEdt.requestFocus()
            passwordEdt.setError("Enter your Passowrd")
            return
        }
        if (password.length < 6) {
            passwordEdt.requestFocus()
            passwordEdt.setError("password too short")
            return
        }


        circleProgressDialog = CircleProgressDialog(this@SignUpActivity)
        circleProgressDialog!!.setProgressColor(Color.BLACK)
        circleProgressDialog!!.setProgressWidth(5)
        circleProgressDialog!!.showDialog()
        createUser(username, email, password)
    }

    private fun createUser(
        username: String,
        email: String,
        password: String
    ) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val id = task.result!!.user.uid
                    val user= User(username,email)
                    Comm.currentUser=user
                    databaseReference.child("Users").child(id).setValue(user)
                    circleProgressDialog!!.dismiss()
                    goHome()
                    finish()
                } else {
                    circleProgressDialog!!.dismiss()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        emailEdt.requestFocus()
                        emailEdt.setError("Your Email is wrong")
                        return@OnCompleteListener
                    }
                    //                    Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            })
    }

    fun signIn(view: View) {
        goSignIn()

    }


}