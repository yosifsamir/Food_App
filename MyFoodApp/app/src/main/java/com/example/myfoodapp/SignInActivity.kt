package com.example.myfoodapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfoodapp.ui.home.HomeActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.FirebaseDatabase
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog

class SignInActivity : AppCompatActivity() {
    private lateinit var emailEdt: EditText
    private lateinit var passwordEdt:EditText
    private lateinit var btnSignIn: Button
    private lateinit var circleProgressDialog: CircleProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        emailEdt = findViewById(R.id.edtEmialSignIn)
        passwordEdt = findViewById(R.id.edtPasswordSignIn)
        btnSignIn = findViewById(R.id.btnSignIn2)


        val firebaseDatabase = FirebaseDatabase.getInstance()
        val table_user = firebaseDatabase.getReference("User")

        val firebaseAuth=FirebaseAuth.getInstance()

        btnSignIn!!.setOnClickListener(View.OnClickListener {

            val email = emailEdt.getText().toString()
            val password = passwordEdt.getText().toString()
            if (TextUtils.isEmpty(email)) {
                emailEdt.requestFocus()
                emailEdt.setError("Enter your Email")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                passwordEdt.requestFocus()
                passwordEdt.setError("Enter your Password")
                return@OnClickListener

            }
            circleProgressDialog = CircleProgressDialog(this@SignInActivity)
            circleProgressDialog.setProgressColor(Color.BLACK)
            circleProgressDialog.setProgressWidth(5)
            circleProgressDialog.showDialog()

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        circleProgressDialog.dismiss()
                        val intent =
                            Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(
                                this@SignInActivity,
                                "Please check your email or password",
                                Toast.LENGTH_SHORT
                            ).show()
                            circleProgressDialog.dismiss()
                            return@OnCompleteListener
                        } else {
                            circleProgressDialog.dismiss()
                            Toast.makeText(
                                this@SignInActivity,
                                task.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    //                    Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                })
        })

    }
}