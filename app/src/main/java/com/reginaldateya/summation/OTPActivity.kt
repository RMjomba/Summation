package com.reginaldateya.summation

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.SplittableRandom
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var verifyOTPBtn: Button
    private lateinit var resendTextView: TextView
    private lateinit var etOTP: EditText

    private lateinit var OTP: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)


        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phoneNumber")!!

        init()
        resendOTPTvVisibility()

        resendTextView.setOnClickListener {
            resendVerificationCode()
            resendOTPTvVisibility()
        }

        verifyOTPBtn.setOnClickListener {
            //collect OTP from edit text
            val typedOTP = OTP
            if (typedOTP.isNotEmpty()){
                if (typedOTP.length == 6){

                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP , typedOTP
                    )

                    signInWithPhoneAuthCredential(credential)

                }else{

                    Toast.makeText(this,"Please enter correct OTP", Toast.LENGTH_SHORT).show()

                }
            }else{
                Toast.makeText(this,"Please enter OTP", Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun resendOTPTvVisibility(){
        etOTP.setText("")
        resendTextView.visibility = View.INVISIBLE
        resendTextView.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable{
            resendTextView.visibility = View.VISIBLE
            resendTextView.isEnabled = true
        } ,6000)
    }

    private fun resendVerificationCode(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(ContentValues.TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(ContentValues.TAG, "onCodeSent:$verificationId")
            OTP = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Authenticate successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun sendToMain(){
        startActivity(Intent(this, Home::class.java))
    }


    private fun init(){
        auth = FirebaseAuth.getInstance()
        verifyOTPBtn = findViewById(R.id.verifyOTPBtn)
        resendTextView = findViewById(R.id.resendTextView)
        etOTP = findViewById(R.id.etOTP)

    }
}