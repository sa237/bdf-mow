package com.example.mealapp.Pickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mealapp.Main.MainActivity;
import com.example.mealapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class PickupFormActivity extends AppCompatActivity {

    private EditText numberMeals;

    private Button provideLocation;
    private List<String> arr;
    private boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_form);

        numberMeals = (EditText) findViewById(R.id.form_mealnumber);
        provideLocation = (Button) findViewById(R.id.provide_location_btn);
        arr = new ArrayList<String>();





        provideLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = numberMeals.getText().toString();




                try{
                    int numberOfMeals = Integer.parseInt(text);
                    if(numberOfMeals < 30){
                        numberMeals.setError("More than 30 meals required.");

                    }

                    if(checked == false){

                        Toast.makeText(PickupFormActivity.this, "Please select the meals you want to donate.", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        //adding the number of meals as the last element of the arraylist
                        arr.add(String.valueOf(numberOfMeals));


                        Intent intent = new Intent(getApplicationContext(),UserLocationActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("foods",(Serializable)arr);
                        intent.putExtra("bundle",args);
                        startActivity(intent);

                    }

                }catch (NumberFormatException e){
                    numberMeals.setError("Enter the number of servings.");
                }





            }
        });




    }




    






    public void onCheckBoxClicked(View view) {
        checked = ((CheckBox) view).isChecked();

        //do whatever I want to with the selected options
        switch(view.getId()){
            case R.id.cb1_dry_ration:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb1_cooked_food:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb2_rice:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb2_pulses:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb2_rotis:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb2_others:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ViewMapActivity.class));
    }
}