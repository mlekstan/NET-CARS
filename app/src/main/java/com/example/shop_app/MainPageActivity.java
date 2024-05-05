package com.example.shop_app;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainPageActivity extends AppCompatActivity {
    private static final String TAG = "MainPageActivity";

    FirebaseAuth auth;
    ImageButton buttonBasket, buttonHeart, buttonUser, buttonSearch, buttonMenu, buttonLogo;
    FirebaseUser currentUser;
    FirebaseFirestore firestoreDb;
    StorageReference storageReference;
    RecyclerView recyclerView;
    MyNewAdapter myNewAdapter;
    List<BasicCarInfo> basicCarInfoList;
    Uri onlineImageUri;
    TextView xd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagemain);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        recyclerView = findViewById((R.id.recyclerviewOffers));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainPageActivity.this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new MyNewAdapter(getApplicationContext(), basicCarInfoList));

        buttonBasket = findViewById(R.id.buttonBasket);
        buttonHeart = findViewById(R.id.buttonHeart);
        buttonUser = findViewById(R.id.buttonUser);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonMenu = findViewById(R.id.buttonMenu);
        buttonLogo = findViewById(R.id.buttonLogo);

        basicCarInfoList = new ArrayList<BasicCarInfo>();
        myNewAdapter = new MyNewAdapter(MainPageActivity.this, basicCarInfoList);

        recyclerView.setAdapter(myNewAdapter);



        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


        buttonBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HeartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });



        firestoreDb = FirebaseFirestore.getInstance();

        firestoreDb.collection("cars").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error == null) {

                }

                List<DocumentChange> listOfDocumentsChanges = value.getDocumentChanges();

                int i = 0;
                for (DocumentChange changedDocument:listOfDocumentsChanges) {

                    if (changedDocument.getType() == DocumentChange.Type.ADDED) {
                        Object[] changedDocumentFieldsValues = obtainDataFromChangedDocument(changedDocument, "Brand", "Model", "Year of production", "Price [PLN]", "mainImageId", "uid");

                        storageReference = FirebaseStorage.getInstance().getReference("images/" + String.valueOf(changedDocumentFieldsValues[5]) + "/" + changedDocument.getDocument().getId() + "/" + String.valueOf(changedDocumentFieldsValues[4]));
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                onlineImageUri = uri;
                                basicCarInfoList.add(new BasicCarInfo(changedDocumentFieldsValues[0].toString(), changedDocumentFieldsValues[1].toString(), changedDocumentFieldsValues[2].toString(), changedDocumentFieldsValues[3].toString(), onlineImageUri));
                                myNewAdapter.notifyDataSetChanged();
                            }
                        });

                    } else if (changedDocument.getType() == DocumentChange.Type.MODIFIED) {
                        Object[] changedDocumentFieldsValues = obtainDataFromChangedDocument(changedDocument, "Brand", "Model", "Year of production", "Price [PLN]", "mainImageId", "uid");

                        storageReference = FirebaseStorage.getInstance().getReference("images/" + String.valueOf(changedDocumentFieldsValues[5]) + "/" + changedDocument.getDocument().getId() + "/" + String.valueOf(changedDocumentFieldsValues[4]));
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                onlineImageUri = uri;

                            }
                        });

                        //listOfDocumentsChanges.set(i, new )

                    } else {
                        //listOfDocumentsChanges.remove(i);
                    }

                    i++;
                }
                myNewAdapter.notifyDataSetChanged();
            }
        });



    }


    Object[] obtainDataFromChangedDocument(DocumentChange changedDocument, String ... fields) {
        Object[] fieldsValues = new Object[fields.length];
        Map<String, Object> fieldToValuesMap = changedDocument.getDocument().getData();

        int i = 0;
        for (String field:fields) {
            fieldsValues[i] = fieldToValuesMap.get(field);
            i++;
        }

        return fieldsValues;
    }




}