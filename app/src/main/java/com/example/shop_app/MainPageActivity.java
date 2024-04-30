package com.example.shop_app;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class MainPageActivity extends AppCompatActivity {
    private static final String TAG = "MainPageActivity";

    FirebaseAuth auth;
    ImageButton buttonBasket, buttonHeart, buttonUser, buttonSearch, buttonMenu, buttonLogo;
    FirebaseUser currentUser;
    ArrayList<CarModel> carModelArrayList;
    RecyclerView recyclerView;
    MyNewAdapter myNewAdapter;
    
    List<CarModel> items;


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
        recyclerView = findViewById((R.id.recyclerviewImages));


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyNewAdapter(getApplicationContext(), items));

        buttonBasket = findViewById(R.id.buttonBasket);
        buttonHeart = findViewById(R.id.buttonHeart);
        buttonUser = findViewById(R.id.buttonUser);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonMenu = findViewById(R.id.buttonMenu);
        buttonLogo = findViewById(R.id.buttonLogo);

        items = new ArrayList<CarModel>();
        myNewAdapter = new MyNewAdapter(MainPageActivity.this, items);

        recyclerView.setAdapter(myNewAdapter);

        //EventChangeListener();


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

    }


    /* nie wiem jak to zrobic
    private void EventChangeListener() {

        db.collection("cars")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("firestore error", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {

                        items.add(dc.getDocument().toObject(CarModel.class));
                    }

                    myNewAdapter.notifyDataSetChanged();
                }
            }
        });

    }



     */

}