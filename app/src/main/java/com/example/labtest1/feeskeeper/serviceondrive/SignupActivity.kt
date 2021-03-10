package com.example.labtest1.feeskeeper.serviceondrive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.labtest1.feeskeeper.serviceondrive.DbConfig.DriverDetails
import com.example.labtest1.feeskeeper.serviceondrive.DbConfig.driverDetailsViewModel

lateinit var loginbtn :TextView
lateinit var firstname :EditText
lateinit var lastname :EditText
lateinit var email :EditText
lateinit var password :EditText
lateinit var RegisterBtn : Button

private lateinit var DriverDetailsViewModel: driverDetailsViewModel

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


      //  DriverDetailsViewModel = ViewModelProvider(this).get(DriverDetailsViewModel::class.java)

        loginbtn =  findViewById(R.id.login)
        firstname = findViewById(R.id.firstname)
        lastname = findViewById(R.id.lastname)
        email = findViewById(R.id.Email)
        password = findViewById(R.id.password)



        loginbtn.setOnClickListener {

            finish()


        }




    }
}