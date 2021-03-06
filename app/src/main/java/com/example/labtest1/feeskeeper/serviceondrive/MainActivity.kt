package com.example.labtest1.feeskeeper.serviceondrive

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {


    private lateinit var name :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        name = findViewById(R.id.RiderName)


        val db = Firebase.firestore

        val docRef = db.collection("ridedetails").document("ride")





         db.collection("ridedetails").document("ride")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {

                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {

               // println(snapshot.exists())
                println("Current data: ${snapshot.get("firstName")   }")

                name.text = snapshot.get("firstName").toString()

            } else {


            }
        }






    }












}
