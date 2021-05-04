 package com.example.pickdrop;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class dashboardclient extends AppCompatActivity {

    Button sigout;
    TextView name,varific;
    FirebaseAuth mauth;
    FirebaseUser user;
    String uid;
    ImageView userimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboardclient);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        varific = findViewById(R.id.varify);
         userimg = findViewById(R.id.profile);
        mauth = FirebaseAuth.getInstance();
        sigout = findViewById(R.id.signout);
        name = findViewById(R.id.username);

        FirebaseUser user = mauth.getCurrentUser();
        if (user.isEmailVerified()){
            varific.setText("");
        }else{
            varific.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(dashboardclient.this,"Email Varification Sent ",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }


        DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference();



        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String data = dataSnapshot.child("Users").child(uid).child("name").getValue(String.class);

                name.setText(data);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        DatabaseReference f = databaseRef.child("Users").child(uid).child("Image_url");
        f.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(userimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        sigout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                soutser();
            }

        });






    }



    private void soutser() {
        Intent log = new Intent(dashboardclient.this, loginactivity.class);
        startActivity(log);
        finish();
    }





    public void senttoreqvehi(View view) {
        Intent log = new Intent(dashboardclient.this, vehiclereq.class);
        startActivity(log);
        finish();
    }

    public void sendtoprofile(View view) {

        Intent log = new Intent(dashboardclient.this, profile.class);
        startActivity(log);
    }

    public void sendtoabout(View view) {

        Intent log = new Intent(dashboardclient.this, about.class);
        startActivity(log);
    }


    public void sendtoprof(View view) {
        Intent log = new Intent(dashboardclient.this, profile.class);
        startActivity(log);
        finish();

    }

    public void approvel(View view) {
        Intent log = new Intent(dashboardclient.this, approval.class);
        startActivity(log);
        finish();

    }
}