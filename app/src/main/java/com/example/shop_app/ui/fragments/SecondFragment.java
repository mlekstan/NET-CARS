package com.example.shop_app.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shop_app.databinding.FragmentSecondBinding;
import com.example.shop_app.ui.activities.MainActivity;
import com.example.shop_app.ui.activities.MainPageActivity;
import com.example.shop_app.ui.activities.SignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SecondFragment extends Fragment {
    private static final String TAG = "UserActivity";

    private FirebaseAuth auth;
    private Button buttonLogout;
    private TextView textEmail, textName;
    private FirebaseUser currentUser;
    private FragmentSecondBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        currentUser= auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonLogout = binding.buttonLogOut;
        textEmail = binding.textUserEmail;
        textName = binding.textUserName;

        textEmail.setText(currentUser.getEmail());


        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = firestoreDb.collection("users").document(userId);


        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String forename = documentSnapshot.getString("forename");
                    String surname = documentSnapshot.getString("surname");

                    textName.setText(forename + " " + surname);

                } else {
                    Log.d(TAG, "Error while loading Name from document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error while getting document", e);
            }
        });


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }
}
