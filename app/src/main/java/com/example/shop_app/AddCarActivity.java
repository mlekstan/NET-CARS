package com.example.shop_app;

import android.animation.AnimatorInflater;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shop_app.databinding.ActivityAddCarBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddCarActivity extends AppCompatActivity {
    private final String TAG = "AddCarActivity";
    ActivityAddCarBinding binding;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseFirestore firestoreDb;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    Button buttonSale, buttonExit, buttonImage;
    TextView editTextBrand, editTextModel, editTextPrice, editTextStreet, editTextMileage, editTextDescription;
    ImageView imageViewCar;
    Uri imageUri;

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
        buttonImage = findViewById(R.id.buttonImage);
        imageViewCar = findViewById(R.id.imageViewCar);
        editTextBrand = findViewById(R.id.textInputEditTextBrand);
        editTextPrice = findViewById(R.id.textInputEditTextPrice);
        editTextModel = findViewById(R.id.textInputEditTextModel);
        editTextStreet = findViewById(R.id.textInputEditTextStreet);
        editTextMileage = findViewById(R.id.textInputEditTextMileage);
        editTextDescription = findViewById(R.id.textInputEditTextDescription);

        String imageId = getSaltString();


        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        buttonSale.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(),R.animator.button_animation));
        buttonSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String brand, price, model, street, mileage, description, uId, imageId;
                brand = String.valueOf(editTextBrand.getText());
                price = String.valueOf(editTextPrice.getText());
                model = String.valueOf(editTextModel.getText());
                street = String.valueOf(editTextStreet.getText());
                mileage = String.valueOf(editTextMileage.getText());
                description = String.valueOf(editTextDescription.getText());
                uId = currentUser.getUid();
                imageId = getSaltString();

                boolean isValidated = validateData(brand, price, model, street, mileage, description);
                if(!isValidated){ return; }

                if (imageUri == null) {
                    Toast.makeText(AddCarActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }


                Map<String, Object> car = new HashMap<>();
                car.put("uid", uId);
                car.put("brand", brand);
                car.put("price", price);
                car.put("model", model);
                car.put("street", street);
                car.put("mileage", mileage);
                car.put("description", description);
                car.put("imageId", imageId);


                db.collection("cars")
                        .add(car)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(AddCarActivity.this, "Car added successfully", Toast.LENGTH_SHORT).show();
                                uploadImage(imageId);
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
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

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewCar.setImageURI(imageUri);
        }

    }

    private void uploadImage(String imageId) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading files...");
        progressDialog.show();

        storageReference = FirebaseStorage.getInstance().getReference("images/" + imageId);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageViewCar.setImageURI(null);

                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Toast.makeText(AddCarActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    boolean validateData(String brand, String price, String model, String street, String mileage, String description) {
        //validate data input by user

        String[] fields = {brand, price, model, street, mileage, description};
        String[] fieldNames = {"brand", "price", "model", "street", "mileage", "description"};

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isEmpty()) {
                Toast.makeText(AddCarActivity.this, "Please provide your " + fieldNames[i] + "." ,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }


    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


}