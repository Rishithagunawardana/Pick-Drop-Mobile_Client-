package com.example.pickdrop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class loginactivity extends AppCompatActivity {
    EditText email,pass;
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

        fauth= FirebaseAuth.getInstance();






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

                        if (task.isSuccessful()){

                            Toast.makeText(loginactivity.this,"Logged in Successfully" ,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),dashboardclient.class));
                        }else{

                            Toast.makeText(loginactivity.this,"Error !!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();





                        }


                    }
                });


            }
        });

    }


    public void sendtoreg (View view){

         Intent regis = new Intent(loginactivity.this,register.class);
         startActivity(regis);
    }

}