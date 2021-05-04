package com.example.pickdrop;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class payment extends AppCompatActivity {
    TextView txtNote, txtTotal;
    Button btnPay ;
    DatabaseReference dbRef;
    String position;



    private  final int PAYHERE_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);









        txtTotal = findViewById(R.id.edttxtTotal);
        txtNote = findViewById(R.id.edttxtNote);
        btnPay = findViewById(R.id.btnPaybill);
        LoadBillData();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String Name=dataSnapshot.child("name").getValue().toString();

                        PayBill(position,Name);
//                        SavePaymentData(); //Todo: undo that

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void PayBill(String position, String name) {
        InitRequest req = new InitRequest();
        req.setMerchantId("1216814");       // Your Merchant PayHere ID
        req.setMerchantSecret("4TrIkEEQfqi8Rm9lY1Dp7z4pHMRw1sYSr8m36d4wNaRX"); // Your Merchant secret (Add your app at Settings > Domains & Credentials, to get this))
        req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
        req.setAmount(Double.parseDouble(txtTotal.getText().toString()));             // Final Amount to be charged
        req.setOrderId(position);        // Unique Reference ID
        req.setItemsDescription(txtNote.getText().toString());  // Item description title
        req.setCustom1("Paid user :"+txtNote.getText().toString());
        req.getCustomer().setFirstName(name);//name
        req.getCustomer().setLastName("rathnauyaka");
        req.getCustomer().setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()); //mail
        req.getCustomer().setPhone("0775845623"); //contact
        req.getCustomer().getAddress().setAddress("No.1, Galle Road");
        req.getCustomer().getAddress().setCity("galle"); //district
        req.getCustomer().getAddress().setCountry("Sri Lanka");

        //mail//contact//district

        SavePaymentData();
        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
        startActivityForResult(intent, PAYHERE_REQUEST); //unique request ID like private
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                String msg;
                if (response != null)
                    if (response.isSuccess()){
                        msg = "Activity result gg:" + response.getData();
                        SavePaymentData();
                        Log.e("TAG", "onActivityResult gg: "+response.getData().toString());
                    }
                    else
                        msg = "Result:" + response.toString();
                else
                    msg = "Result: no response";
                Log.d("Result ok", msg);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null)
                    Log.e("Response not null", response.toString());
                else
                    Log.e("response null", "error");

            }
        }
    }

    private void SavePaymentData() {
        Log.e("TAG", "SavePaymentData: "+position );
        dbRef= FirebaseDatabase.getInstance().getReference().child("pays");
        dbRef.child("paidBy").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbRef.child("ststus").setValue("1");


    }

    private void LoadBillData() {



        dbRef= FirebaseDatabase.getInstance().getReference().child("Payments");
        dbRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {



                String Total =  dataSnapshot.child("total").getValue().toString();
                String Note =  dataSnapshot.child("note").getValue().toString();
                txtNote.setText(Note);
                txtTotal.setText((Total));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });



    }


}

