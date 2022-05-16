package com.example.mealapp.Donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mPaymentAdapter;
    private RecyclerView.LayoutManager mPaymentLayoutManager;
    private String currentUserId;
    private TextView noPaymentText;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.hist_recycler);
        noPaymentText = (TextView) findViewById(R.id.no_payment_text);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mPaymentLayoutManager = new LinearLayoutManager(PaymentActivity.this);
        mRecyclerView.setLayoutManager(mPaymentLayoutManager);
        mPaymentAdapter = new PaymentAdapter(getDataSetMatches(),PaymentActivity.this);
        mRecyclerView.setAdapter(mPaymentAdapter);




        getUserData();




        

    }

    private void getUserData() {

        DatabaseReference payDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("payments");
        payDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot payment: snapshot.getChildren()){

                        FetchPaymentInfo(payment.getKey());

                    }

                }

                else{
                    noPaymentText.setVisibility(View.VISIBLE);
                    noPaymentText.setText("No payments made yet.");
                    //Toast.makeText(PaymentActivity.this, "No payments made yet. ", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchPaymentInfo(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("payments").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String date = key;
                    String money = "";
                    String payID = "";


                    if(snapshot.child("amount").getValue() != null){
                        money = snapshot.child("amount").getValue().toString();

                    }

                    if(snapshot.child("payment_id").getValue() != null){
                        payID = snapshot.child("payment_id").getValue().toString();

                    }

                    PaymentObject object = new PaymentObject(date,money,payID);
                    resultPayments.add(object);
                    mPaymentAdapter.notifyDataSetChanged();




                }
                else{
                    Toast.makeText(PaymentActivity.this, "No payments made yet.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<PaymentObject> resultPayments = new ArrayList<PaymentObject>();
    private List<PaymentObject> getDataSetMatches(){


        return resultPayments;

    }


}