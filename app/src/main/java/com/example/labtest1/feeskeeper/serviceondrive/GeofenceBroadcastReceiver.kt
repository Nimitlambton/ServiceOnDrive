package com.example.labtest1.feeskeeper.serviceondrive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Toast.makeText(context , "hey hey he entered " , Toast.LENGTH_LONG).show()

        println("hey hey hey Enteteddddddd")

    }
}