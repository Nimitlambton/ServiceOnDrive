package com.example.labtest1.feeskeeper.serviceondrive

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {


    private lateinit var title :TextView
    private lateinit var fname :TextView
    private lateinit var lname :TextView
    private lateinit var destination :TextView
    private lateinit var currentloction :TextView
    private lateinit var Accept :Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var riderImg = findViewById<ImageView>(R.id.riderimg)

        title = findViewById(R.id.title)
        fname = findViewById(R.id.firstName)
        lname  = findViewById(R.id.lastName)
        destination  = findViewById(R.id.Destination)
        currentloction = findViewById(R.id.currentlocation)
        Accept = findViewById(R.id.accept)


        val db = Firebase.firestore

        val docRef = db.collection("ridedetails").document("ride")


         db.collection("ridedetails").document("ride")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {


                return@addSnapshotListener

            }

            if (snapshot != null && snapshot.exists()) {

               // println(snapshot.exists())
                println("Current data: ${snapshot.get("firstName") }")

                title.text = "Riders Found "
                fname.text = snapshot.get("firstName").toString()
                lname.text = snapshot.get("lastNmae").toString()
                destination.text = "Destination "  + snapshot.get("formattedDestination").toString()
                currentloction.text = "Rider's current location "  + snapshot.get("formattedCurrentLocation").toString()

                val imgData = snapshot.get("userImg").toString()
                val k =  Base64.decode(imgData, Base64.DEFAULT)
                val image = BitmapFactory.decodeByteArray(k, 0, k.size)
                riderImg.setImageBitmap( image)


                Accept.setOnClickListener {


                    val docData = hashMapOf(
                        "DriverName" to "mohan",
                        "DriverlastName" to "birjwasi",
                        "currentAdrees" to 0.0 ,
                        "time" to 0.0
                    )

                    val db = Firebase.firestore

                    db.collection("ridedetails").document("ride").collection("driverDetails").document("details" ).set(docData)


                }




            } else {


            }
        }


    }

}
