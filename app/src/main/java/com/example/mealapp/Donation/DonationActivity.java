package com.example.mealapp.Donation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.Main.MainActivity;
import com.example.mealapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DonationActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView money_view,meal_view;
    private Button donate_btn;
    private ImageButton decreaseBtn,increaseBtn;
    private int money;
    private int meal;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private FirebaseUser user;
    private String userId;
    private int amnt = 0;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);



        money_view = findViewById(R.id.meal_money);
        meal_view = findViewById(R.id.meal_number);
        decreaseBtn = findViewById(R.id.btn_decrease);
        increaseBtn = findViewById(R.id.btn_increase);
        donate_btn = findViewById(R.id.don_btn);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        money = 60;
        meal = 1;



        money_view.setText("Rs. "+ money);
        meal_view.setText("" + meal + " meal");

//
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(meal == 1){
                    Toast.makeText(DonationActivity.this, "Minimum one meal is required", Toast.LENGTH_SHORT).show();
                }

                else{
                    money = money-60;
                    meal = meal-1;
                }

                money_view.setText("Rs. "+ money);
                meal_view.setText(""+ meal + " meal");

                amnt = Math.round(money * 100);

            }});

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(meal > 100){
                    Toast.makeText(DonationActivity.this,"You can donate maximum 100 meals at a time",Toast.LENGTH_SHORT).show();

                }

                else{
                    money += 60;
                    meal += 1;
                    money_view.setText("Rs. "+ money);
                    meal_view.setText(""+ meal + " meals");

                    amnt = Math.round(money * 100);
                }
            }
        });




        donate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Checkout checkout = new Checkout();

                //Set key id
                checkout.setKeyID("rzp_live_WSiKiTYffwOMjc");

                //Set image
                checkout.setImage(R.drawable.razorpay_logo);

                //initialize json obj
                JSONObject object = new JSONObject();

                try {
                    //put name
                    object.put("name","Building Dreams Foundation");

                    //put description
                    object.put("description","Payments");

                    //Put theme color
                    object.put("theme.color","#0093DD");

                    //Put currency unit
                    object.put("currency","INR");

                    //Put amount
                    object.put("amount",amnt);

                    //put mobile number
                    object.put("prefill.contact","8384897524");

                    //Put email
                    object.put("prefill.email","mailbuildingdreams@gmail.com");

                    //Open razorpay checkout activity
                    checkout.open(DonationActivity.this,object);







                } catch (JSONException e) {
                    e.printStackTrace();
                }




//                Intent intent = new Intent(DonationActivity.this,PaymentActivity.class);
//                intent.putExtra("total_money",money);
//                intent.putExtra("num_meal",meal);
//                startActivity(intent);


            }
        });



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set title
        builder.setTitle("The payment is successful.Payment ID:");

        //set message
        builder.setMessage(s);

        //get date to put in database
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = simpleDateFormat.format(calendar.getTime());
        //String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("payments").child(date);
        Map userInfo = new HashMap<>();
        userInfo.put("payment_id", s);
        userInfo.put("amount", money);
        currentUserDb.setValue(userInfo);

        //show alert dialog
        builder.show();








    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

    }
}