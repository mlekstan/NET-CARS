package com.example.shop_app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    Button buttonSignUp, buttonLoginScreen;
    TextInputEditText editTextEmail, editTextPassword, editTextForename, editTextSurname, editTextCity, editTextStreet, editTextPhone;
    FirebaseAuth mAuth;
    FirebaseFirestore firestoreDb;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        firestoreDb = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        editTextEmail = findViewById(R.id.textInputEditTextEmail);
        editTextPassword = findViewById(R.id.textInputEditTextPassword);
        editTextForename = findViewById(R.id.textInputEditTextForename);
        editTextSurname = findViewById(R.id.textInputEditTextSurname);
        editTextCity = findViewById(R.id.textInputEditTextCity);
        editTextStreet = findViewById(R.id.textInputEditTextStreet);
        editTextPhone = findViewById(R.id.textInputEditTextPhoneNumber);
        buttonSignUp = findViewById(R.id.buttonSignUp2);



        buttonSignUp.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(),R.animator.button_animation));
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password, forename, surname, city, street, phone;

                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                forename = String.valueOf(editTextForename.getText());
                surname = String.valueOf(editTextSurname.getText());
                city = String.valueOf(editTextCity.getText());
                street = String.valueOf(editTextStreet.getText());
                phone = String.valueOf(editTextPhone.getText());

                boolean isValidated = validateData(email, password, forename, surname, city, phone);
                if (!isValidated) { return; }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Account (actually) was created");

                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("forename", forename);
                                    userData.put("surname", surname);
                                    userData.put("city", city);
                                    userData.put("street", street);
                                    userData.put("phoneNumber", phone);
                                    userData.put("email", email);

                                    firestoreDb.collection("users").document(mAuth.getUid())
                                            .set(userData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "User data successfully written to document");

                                                    Toast.makeText(SignUpActivity.this, "Account created.",
                                                            Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error occured while writing user data to document", e);

                                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();


                                                    if (currentUser != null) {
                                                        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.w(TAG, "User account was deleted, becauese of error while writing document");
                                                                } else {
                                                                    Log.w(TAG, "User account not was deleted, although error occured while writing document");
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                } else {
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        buttonLoginScreen = findViewById(R.id.buttonLoginScreen);
        buttonLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void onStart() { // gdy już zalogowany przechodzi dalej
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    boolean validateData(String email,String password, String forename, String surname, String city, String phone) {
        //validate signUp data input by user

        String[] fields = {forename, surname, city, phone};
        String[] fieldNames = {"forename", "surname", "city", "phone number"};

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please provide your " + fieldNames[i] + "." ,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignUpActivity.this, "Email is invalid.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.length()<6) {
            Toast.makeText(SignUpActivity.this, "Password is too short.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}