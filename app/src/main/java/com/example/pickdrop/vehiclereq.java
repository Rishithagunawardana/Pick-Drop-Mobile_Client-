package com.example.pickdrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

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



        reqdb = FirebaseDatabase.getInstance().getReference().child("Vehicle Requests").child(uid);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertReqdata();
                Intent start = new Intent(vehiclereq.this,dashboardclient.class);
                startActivity(start);
               
            }
        });
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