package com.example.pickdrop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class loginactivity extends AppCompatActivity {
    EditText email,pass;
    TextView pasreset;
    Button log;
    Button reg;
    FirebaseAuth fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);


        email = findViewById(R.id.email);
        pass  = findViewById(R.id.pass);
        log = findViewById(R.id.log);
        pasreset = findViewById(R.id.passreset);
        fauth= FirebaseAuth.getInstance();




        //-----------------password reset section
        pasreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetmail =  new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog  =  new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ..");
                passwordResetDialog.setMessage("Enter Your Registered Email to Send the Reset Link .. ");
                passwordResetDialog.setView(resetmail);


                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetmail.getText().toString();
                        fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(loginactivity.this,"Reset Mail Sent ",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginactivity.this,"Reset Mail Not Sent " + e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });
        //------------ end of password reset section


     
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m = email.getText().toString().trim();
                String p = pass.getText().toString().trim();

                if (TextUtils.isEmpty(m)){
                    email.setError("Email Required");
                    return;
                }

                if (TextUtils.isEmpty(p)){
                    pass.setError("Password Required");
                    return;
                }

                if (p.length()<6){
                    pass.setError("Password must be 6 char above");
                    return;
                }
                fauth.signInWithEmailAndPassword(m,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(loginactivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),dashboardclient.class));

                    }
                });
                finish();


            }
        });

    }


    public void sendtoreg (View view){

         Intent regis = new Intent(loginactivity.this,register.class);
         startActivity(regis);
    }

}