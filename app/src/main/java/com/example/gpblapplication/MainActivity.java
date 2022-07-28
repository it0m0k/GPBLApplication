package com.example.gpblapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseMessaging myFirebaseMessaging = FirebaseMessaging.getInstance();
    DatabaseReference myRef;
    Button button_on;
    Button button_off;
    Button move_btn;
    TextView tFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_on = findViewById(R.id.light_on);
        button_off = findViewById(R.id.light_off);
        tFirebase = findViewById(R.id.tFirebaseResult);
        move_btn = findViewById(R.id.move_btn);
        /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            tFirebase.setText(String.valueOf(task.getResult().getValue()));
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        }
                    }
                });
            }
        });
        */
        button_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference hopperRef = myRef.child("/");
                Map<String,Object> hopperUpdates = new HashMap<>();
                hopperUpdates.put("LEDCommand","1");

                hopperRef.updateChildren(hopperUpdates);
                Toast.makeText(MainActivity.this,"The light turned on !",Toast.LENGTH_SHORT).show();
                System.out.println("status:ON");
            }
        });

        button_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference hopperRef = myRef.child("/");
                Map<String,Object> hopperUpdates = new HashMap<>();
                hopperUpdates.put("LEDCommand","0");

                hopperRef.updateChildren(hopperUpdates);
                Toast.makeText(MainActivity.this,"The light turned off !",Toast.LENGTH_SHORT).show();
                System.out.println("status:OFF");
            }
        });

        move_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,SubActivity.class);
                startActivity(intent);
            }
        });

        myRef = database.getReference("/Location2test/LatestData");
        //final DatabaseReference temperature = myRef.child("Temperature");
        //final DatabaseReference humidity = myRef.child("Humidity");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //List<String> valueList = new ArrayList<>();
                String light = (String)dataSnapshot.child("LEDCommand").getValue();
                String humidity = String.valueOf(dataSnapshot.child("Humidity").getValue());
                String temperature = String.valueOf(dataSnapshot.child("Temperature").getValue());
                // for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                //Object value = postSnapshot.getValue();
                //String parent = postSnapshot.getKey();
                //valueList.add((String) postSnapshot.getValue());
                Log.d("debug",String.format("Light:%s, hum:%s, tem:%s",light,String.valueOf(humidity),String.valueOf(temperature)));
                // Log.d("debug", "Node is : " + parent + ", temperature is :"+temperature+"humidity is :"+humidity+"Artificial_Light is :"+light);
                //tFirebase.setText(String.format("Temperature is :%s", value));
                //}
                // tFirebase.setText(String.format("Light is : %s\n"+"Humidity is %s\n"+"Temperature is %s",valueList.get(0),valueList.get(1),valueList.get(2)));
                tFirebase.setText(String.format("Light is : %s\n"+"Humidity is %s\n"+"Temperature is %s",light,humidity,temperature));


                if(Integer.parseInt(humidity)<10){
                    Toast.makeText(MainActivity.this,"Alert: Abnormal humidity was observed",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("debug", "Failed to read value.", error.toException());
            }
        });
        myFirebaseMessaging.setAutoInitEnabled(true);
        myFirebaseMessaging.getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("firebase", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                // save Token to server-side.
                DatabaseReference ref = database.getReference("/token");
                // Get new FCM registration token
                String token = task.getResult();
                ref.setValue(token);
                // Log and toast
                String msg = token;
                Log.d("firebase", msg);
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }




/*
    public void ClickEvent(View view) {
        TextView tv = (TextView) findViewById(R.id.Text);
        DoTask doTask = new DoTask(tv, myRef);
        doTask.execute();
        Log.d("debug", "push");

    }
*/

    private static class DoTask extends AsyncTask<Integer, String, String> {
        private TextView textView;
        private DatabaseReference myRef;


        public DoTask(TextView textView, DatabaseReference ref) {
            this.textView = textView;
            this.myRef = ref;
        }


        @Override
        protected String doInBackground(Integer... integers) {
            try {
                Log.d("debug", "connected");

                System.out.println("Send Prototype");

                //Show Output stream
                //out.close();

                //close
                //socket.close();
                return "Connected!!";
            } catch (Exception e) {
                Log.d("debug", "error");
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String str) {
            //Setup precondition to execute some task
            textView.setText(str);
            Log.d("debug", str);
        }
    }

}