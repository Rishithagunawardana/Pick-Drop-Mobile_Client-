package com.example.pickdrop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class vehiclereq extends AppCompatActivity {
    FirebaseUser user;
    String uid;
    EditText name,phone,date,passenger,desti,ac;
    Button  sub , back, loc;
 DatabaseReference reqdb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_vehiclereq);

       name = findViewById(R.id.name);
        phone = findViewById(R.id.pass);
        date = findViewById(R.id.dateandtime);
        passenger = findViewById(R.id.passengers);
        desti = findViewById(R.id.decelerate);
        ac = findViewById(R.id.acnonacstat);
        sub = findViewById(R.id.normal);
        back = findViewById(R.id.bounce);
        loc = findViewById(R.id.map);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();









        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(vehiclereq.this,placepicker.class);
                startActivity(start);
            }
        });



        reqdb = FirebaseDatabase.getInstance().getReference().child("Vehicle Requests");
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NAME = name.getText().toString().trim();
                String PHONE =phone.getText().toString().trim();
                String DATE = date.getText().toString().trim();
                String PASSENGER = passenger.getText().toString().trim();
                String DESTI = desti.getText().toString().trim();
                String AC = ac.getText().toString().trim();


                if (PHONE.length()>10 | PHONE.length()<10){
                    phone.setError("enter a valid phone number ! ");

                }

                if (TextUtils.isEmpty(NAME)|TextUtils.isEmpty(PHONE)|TextUtils.isEmpty(DATE)|TextUtils.isEmpty(PASSENGER)|TextUtils.isEmpty(DESTI)|TextUtils.isEmpty(AC)){
                    name.setError("Enter the name");
                    phone.setError("enter the phone no");
                    date.setError("enter the date ");
                    passenger.setError("enter the No of passengers ");
                    desti.setError("enter the destination ");
                    ac.setError("Type - AC or on AC ");
                }

                    InsertReqdata();
                    reqdb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            notification();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent start = new Intent(vehiclereq.this, dashboardclient.class);
                    startActivity(start);

                }

        });

    }

    private void notification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(vehiclereq.this,"n")
                .setContentText("Pick & Drop")
                .setSmallIcon(R.drawable.logonew)
                .setAutoCancel(true)
                .setContentText("Your Vehicle Request Sent to the service provider !");
        NotificationManagerCompat managerCompat =   NotificationManagerCompat.from(vehiclereq.this);
        managerCompat.notify(999,builder.build());
    }


    private void InsertReqdata(){
        String NAME = name.getText().toString();
        String TELNO = phone.getText().toString();
       String DATE  = date.getText().toString();
       String PASSENGERS = passenger.getText().toString();
       String DESTINATION = desti.getText().toString();
       String ACTYPE =  ac.getText().toString();
        requests req = new requests(NAME,TELNO,DATE,PASSENGERS,DESTINATION,ACTYPE);
        reqdb.push().setValue(req);
        Toast.makeText(vehiclereq.this,"Request Sent !",Toast.LENGTH_SHORT).show();
        final Intent start = new Intent(vehiclereq.this,dashboardclient.class);
        startActivity(start);
    }

    public void sendtodash(View view) {

        Intent log = new Intent(vehiclereq.this, dashboardclient.class);
        startActivity(log);
    }



}