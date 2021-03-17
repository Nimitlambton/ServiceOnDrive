package com.example.labtest1.feeskeeper.serviceondrive

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import com.lambtonserviceon.models.directions.Direction
import com.lambtonserviceon.models.directions.Step
import okhttp3.*
import java.io.IOException

class customerEnrouteMap : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener  ,
    LocationListener {



    private lateinit var mMap: GoogleMap
    private lateinit var myMarker: Marker
    private var driverLocation = LatLng(0.0, 0.0)

    private var destinationlocation = LatLng(0.0, 0.0)

    //Current location
    private var riderlocation = LatLng(0.0, 0.0)
    private val client = OkHttpClient()
    private lateinit var decodedPolyLine: List<LatLng>

    lateinit var geofencingClient: GeofencingClient

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_enroute_map)


        //setting up Googlemap
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {

        getLocation()

        geofencingClient = LocationServices.getGeofencingClient(this)


        val db = Firebase.firestore

        val docRef = db.collection("ridedetails").document("ride").collection("driverDetails")
            .document("details")
        db.collection("ridedetails").document("ride")


        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("DRIVER DETAILS ERR")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {

                println("DRIVER DETAILS SUCCESS")
                println("Current data: ${snapshot.get("firstName")}")

                val lati = snapshot.get("ridersLatititue").toString()
                val longi = snapshot.get("ridersLongitude").toString()

                riderlocation = LatLng(lati.toDouble(), longi.toDouble())

                val Driverlati = snapshot.get("currentLatititue").toString()
                val Driverlongi = snapshot.get("currentLongitude").toString()
                driverLocation = LatLng(Driverlati.toDouble(), Driverlongi.toDouble())


                val destilati = snapshot.get("destinationLatititue").toString()
                val destilongi = snapshot.get("destinationLongitude").toString()

                destinationlocation = LatLng(destilati.toDouble(), destilongi.toDouble())


                mMap = googleMap
                mMap.clear()
                myMarker = mMap.addMarker(
                    MarkerOptions().icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE
                        )
                    ).position(driverLocation).title("you")
                )
                myMarker.showInfoWindow()


                myMarker =
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN
                            )
                        )
                            .position(riderlocation).title("Pickup Location")
                    )

                myMarker =
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_YELLOW
                            )
                        )
                            .position(destinationlocation).title("Destination!!")
                    )




                mMap?.animateCamera(CameraUpdateFactory.newLatLng(driverLocation))
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 10f))


                val url = getURL(driverLocation, riderlocation)

                val url2 = getURL(riderlocation, destinationlocation)
                println(url)
                this.run(url, "GREEN")
                println(url2)
                this.run(url2, "RED")

            } else {


            }
        }

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }


    //build Url to fetch google api
    private fun getURL(from: LatLng, to: LatLng): String {

        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest"
        val key = "&key=AIzaSyDfitQFZjRn76sFCbB4dXzjf7r1i3GU-Lc"

        return "https://maps.googleapis.com/maps/api/directions/json?$params$key"

    }


    fun run(url: String, color: String) {

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {


            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            }

            override fun onResponse(call: Call, response: Response) {

                val gson = Gson()
                var Direction2 = gson.fromJson(response.body?.string(), Direction::class.java)

                //function to fetch steps and pass to ADD polyline
                addPolyLines(Direction2.routes[0].legs[0].steps, color)
            }

        })


    }


    private fun addPolyLines(steps: List<Step>, color: String) {

        val path: MutableList<List<LatLng>> = ArrayList()

        for (step in steps) {
            decodedPolyLine = PolyUtil.decode(step.polyline.points);
            path.add(decodedPolyLine)

        }

        val polylines: List<Polyline> = ArrayList()
        runOnUiThread {


            val polyLineOption = PolylineOptions()

            if (color == "RED") {

                val col1 = Color.RED
                polyLineOption.color(col1)

            } else {

                val color2 = Color.YELLOW
                polyLineOption.color(color2)
            }



            for (p in path)
                polyLineOption.addAll(p)


            val polyline = mMap.addPolyline(polyLineOption)




        }




    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }


    override fun onLocationChanged(location: Location) {

        val db = Firebase.firestore

        val docRef = db.collection("ridedetails").document("ride").collection("driverDetails").document("details" )
        db.collection("ridedetails").document("ride")

        docRef.update("currentLatititue",location.latitude)
        docRef.update("currentLongitude",location.longitude)


        println("hey this is called!!")
//        println(location.latitude)
//        println(location.longitude)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }


    }
}