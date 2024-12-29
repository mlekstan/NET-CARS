package com.example.shop_app.ui.fragments;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.shop_app.DataFetcher;
import com.example.shop_app.R;
import com.example.shop_app.databinding.FragmentOfferDetailsBinding;
import com.example.shop_app.ui.adapters.ViewPagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class OfferDetailsFragment extends Fragment {
    FirebaseFirestore firestoreDb;
    StorageReference storageReference;
    Object[] documentFieldsValues;
    Uri[] imagesUri;
    ViewPager2 viewPager;
    FragmentOfferDetailsBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOfferDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String brand = getArguments().getString("Brand");
        String model = getArguments().getString("Model");
        String yearOfProduction = getArguments().getString("Year of production");
        String price = getArguments().getString("Price [PLN]");
        Uri onlineImageUri = Uri.parse(getArguments().getString("Online image Uri"));
        String pathToDocument = getArguments().getString("Path to chosen document");
        String pathToDocumentFilesFolder = getArguments().getString("Path to chosen document files folder");

        viewPager = binding.viewPager;


        firestoreDb = FirebaseFirestore.getInstance();

        firestoreDb.document(pathToDocument).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                documentFieldsValues = DataFetcher.obtainDataFromDocument(documentSnapshot, "Mileage [km]", "Description", "imagesIds");

                ArrayList<String> imagesIdsList = (ArrayList<String>) documentFieldsValues[2];
                imagesUri = new Uri[imagesIdsList.size()];

                storageReference = FirebaseStorage.getInstance().getReference(pathToDocumentFilesFolder);

                int expectedDownloads = imagesIdsList.size(); // Liczba oczekiwanych pobierań
                AtomicInteger completedDownloads = new AtomicInteger(0); // Licznik ukończonych pobierań
                for (int i = 0; i < imagesIdsList.size(); i++) {
                    String image = imagesIdsList.get(i);
                    int finalI = i;
                    storageReference.child(image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            int index = completedDownloads.getAndIncrement(); // Inkrementuj licznik i pobierz jego wartość
                            imagesUri[finalI] = uri;
                            if (completedDownloads.get() == expectedDownloads) { // Sprawdź, czy pobrano wszystkie obrazy
                                // Ustaw adapter ViewPager tylko po pobraniu wszystkich obrazów
                                ViewPagerAdapter adapter = new ViewPagerAdapter(imagesUri, getContext());
                                viewPager.setAdapter(adapter);
                                GridLayout gridLayout = binding.gridLayout;

                                Map<String, String> map = new HashMap<>();
                                map.put("Brand", brand);
                                map.put("Model", model);
                                map.put("Year of production", yearOfProduction);
                                map.put("Price [PLN]", price);
                                map.put("Mileage [km]", documentFieldsValues[0].toString());
                                map.put("Description", documentFieldsValues[1].toString());

                                generateTextViews(gridLayout, map);

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Obsłuż błędy pobierania obrazów, jeśli zachodzi taka potrzeba
                        }
                    });
                }
            }
        });
    }

    private void generateTextViews(GridLayout gridLayout, Map<String, String> map) {

        String[] keysArray = map.keySet().toArray(new String[0]);
        String[] valuesArray = map.values().toArray(new String[0]);
        for (String value:valuesArray) {
            Log.d("XD", value);
        }

        for (int i = 0; i < 2 * map.size(); i++) {
            // Tworzenie nowego TextView
            TextView textView = new TextView(getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            // Ustawienie parametrów tekstu
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white)); // Kolor tekstu

            // Ustawienie marginesów
            int marginTop = 40;
            int marginRight = 0;
            int marginBottom = 0;
            int marginLeft = 40;

            if (i % 2 == 0) {
                //marginLeft = 40;
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setText(keysArray[i / 2]);
            } else {
                //marginLeft = 0;
                marginRight = 40;
                textView.setText(valuesArray[i / 2]);
                params.width = 520;
            }

            if (i == 2 * map.size() - 1 || i == 2 * map.size() - 2) {
                marginBottom = 40;

            }
            textView.setPadding(marginLeft, marginTop, marginRight, marginBottom); // Ustawienie marginesów
            textView.setSingleLine(false);
            textView.setLayoutParams(params);

            // Dodanie tekstu do GridLayout
            gridLayout.addView(textView);

        }
    }
}