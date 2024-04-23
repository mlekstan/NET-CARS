package com.example.shop_app;

import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCarActivity extends AppCompatActivity {
    private final String TAG = "AddCarActivity";
    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseFirestore firestoreDb;
    Button buttonSale, buttonExit;
    TextView editTextBrand, editTextModel, editTextPrice, editTextStreet, editTextMileage, editTextDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        buttonSale = findViewById(R.id.buttonSaleCar);
        buttonExit = findViewById(R.id.buttonExit);
        editTextBrand = findViewById(R.id.textInputEditTextBrand);
        editTextPrice = findViewById(R.id.textInputEditTextPrice);
        editTextModel = findViewById(R.id.textInputEditTextModel);
        editTextStreet = findViewById(R.id.textInputEditTextStreet);
        editTextMileage = findViewById(R.id.textInputEditTextMileage);
        editTextDescription = findViewById(R.id.textInputEditTextDescription);

        buttonSale.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(),R.animator.button_animation));
        buttonSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand, price, model, street, mileage, description;
                brand = String.valueOf(editTextBrand.getText());
                price = String.valueOf(editTextPrice.getText());
                model = String.valueOf(editTextModel.getText());
                street = String.valueOf(editTextStreet.getText());
                mileage = String.valueOf(editTextMileage.getText());
                description = String.valueOf(editTextDescription.getText());

                Map<String, Object> car = new HashMap<>();
                car.put("uid", currentUser.getUid());
                car.put("brand", brand);
                car.put("price", price);
                car.put("model", model);
                car.put("street", street);
                car.put("mileage", mileage);
                car.put("description", description);



                db.collection("cars")
                        .add(car)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });


            }
        });


        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}