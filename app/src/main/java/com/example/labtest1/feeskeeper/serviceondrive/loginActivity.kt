package com.example.labtest1.feeskeeper.serviceondrive

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


lateinit var createbtn : TextView
lateinit var login :Button
class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        createbtn = findViewById(R.id.CreateAcc)
        login = findViewById(R.id.loginbtn)



        createbtn.setOnClickListener {
            var intent = Intent(this , SignupActivity::class.java)

            startActivity(intent)

        }


    }






}