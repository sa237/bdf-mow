package com.example.mealapp.Pickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mealapp.R;

import static java.lang.Integer.parseInt;

public class PickupFormActivity extends AppCompatActivity {

    private EditText numberMeals,userName;

    private Button provideLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_form);

        numberMeals = (EditText) findViewById(R.id.form_mealnumber);
        userName  = (EditText) findViewById(R.id.form_name);
        provideLocation = (Button) findViewById(R.id.provide_location_btn);
        String name = userName.getText().toString();

        if(name.isEmpty()){
            userName.setError("Enter name.");
        }







        provideLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = numberMeals.getText().toString();
                try{
                    int numberOfMeals = Integer.parseInt(text);
                    if(numberOfMeals < 30){
                        numberMeals.setError("More than 30 meals required.");

                    }
                    else{
                        startActivity(new Intent(getApplicationContext(),UserLocationActivity.class));
                    }

                }catch (NumberFormatException e){
                    numberMeals.setError("Enter the number of servings.");
                }





            }
        });




    }

    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        //do whatever I want to with the selected options


    }
}