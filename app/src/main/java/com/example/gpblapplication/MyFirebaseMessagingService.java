package com.example.gpblapplication;


import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s){
        Log.d("firebase","Refreshed token: " + s);
    }
}
