package com.example.shop_app;

import android.animation.AnimatorInflater;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddCarActivity extends AppCompatActivity {
    private final String TAG = "AddCarActivity";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestoreDb;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private Button buttonPostAdverisement, buttonExit, buttonAddImage;
    //private TextView editTextBrand, editTextModel, editTextPrice, editTextYearOfProduction, editTextMileage, editTextDescription;
    private List<Uri> imagesUriList;
    private Uri imageUri;

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


        firestoreDb = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        imagesUriList = new ArrayList<Uri>();

        buttonPostAdverisement = findViewById(R.id.buttonPostAdvertisement);
        buttonExit = findViewById(R.id.buttonExit);
        buttonAddImage = findViewById(R.id.buttonImage);

        TextView editTextBrand = findViewById(R.id.textInputEditTextBrand);
        TextView editTextPrice = findViewById(R.id.textInputEditTextPrice);
        TextView editTextModel = findViewById(R.id.textInputEditTextModel);
        TextView editTextYearOfProduction = findViewById(R.id.textInputEditTextYearOfProduction);
        TextView editTextMileage = findViewById(R.id.textInputEditTextMileage);
        TextView editTextDescription = findViewById(R.id.textInputEditTextDescription);


        recyclerView = findViewById(R.id.recyclerviewImages);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(AddCarActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(imagesUriList, AddCarActivity.this);
        recyclerView.setAdapter(mAdapter);


        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        buttonPostAdverisement.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(), R.animator.button_animation));
        buttonPostAdverisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidated = validateData(editTextBrand, editTextModel, editTextPrice, editTextYearOfProduction, editTextMileage, editTextDescription);
                if (!isValidated) {
                    return;
                }

                if (imagesUriList.isEmpty()) {
                    Toast.makeText(AddCarActivity.this, "Please, select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<String> imagesIds = generateListOfIDs(imagesUriList.size());

                uploadNewDocument(imagesIds, "cars", editTextBrand, editTextModel, editTextPrice, editTextYearOfProduction, editTextMileage, editTextDescription);

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


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagesUriList.add(imageUri);
            mAdapter.notifyDataSetChanged();
        }
    }


    private boolean uploadImages(List<String> imagesIds, String imagesUploadPath, DocumentReference documentReference) {

        progressDialog = new ProgressDialog(AddCarActivity.this);
        progressDialog.setTitle("Uploading files...");
        progressDialog.show();

        final boolean[] errorOccured = {false};
        boolean uploadingError = false;

        int imagesIdsSize = imagesIds.size();
        for (int i = 0; i < imagesIdsSize; i++) {
            storageReference = FirebaseStorage.getInstance().getReference(imagesUploadPath + imagesIds.get(i));

            final int finalI = i;
            storageReference.putFile(imagesUriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (finalI == imagesIdsSize - 1 && progressDialog.isShowing()) {
                        Log.d(TAG, "Last file uploaded succesfully");
                        progressDialog.dismiss();
                        imagesUriList.clear();
                        mAdapter.notifyDataSetChanged();

                        Intent intent = new Intent(AddCarActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    errorOccured[0] = true;

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    //documentReference.delete();

                    Toast.makeText(AddCarActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            });

            if (errorOccured[0] == true) {
                uploadingError = errorOccured[0];
                break;
            }
        }
        return uploadingError;
    }


    private void uploadNewDocument(List<String> imagesIds, String collectionPath, TextView ... textViews) {
        String mainImageId = imagesIds.get(0);

        Map<String,Object> fieldsNamesToValuesMap = new HashMap<>();

        for (TextView textView:textViews) {
            fieldsNamesToValuesMap.put(String.valueOf(textView.getHint()), String.valueOf(textView.getText()));
        }
        fieldsNamesToValuesMap.put("uid", currentUser.getUid());
        fieldsNamesToValuesMap.put("imagesIds", imagesIds);
        fieldsNamesToValuesMap.put("mainImageId", mainImageId);

        List<String> finalImagesIds = imagesIds;
        firestoreDb.collection(collectionPath)
                .add(fieldsNamesToValuesMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String documentId = documentReference.getId();
                        String imagesUploadPath = "images/" + currentUser.getUid() + "/" + documentId + "/";
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentId);

                        boolean uploadingError = uploadImages(finalImagesIds, imagesUploadPath, documentReference);

                        if (uploadingError == false) {
                            Log.d(TAG, "Files uploaded succesfully to the remote path: " + imagesUploadPath);
                            Toast.makeText(AddCarActivity.this, "Offer added successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e(TAG, "Error occured while uploading files " + documentId);
                            Log.e(TAG, "Added DocumentSnapshot and uploaded files were deleted from Firestore " + documentId);
                            Toast.makeText(AddCarActivity.this, "Adding offer failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error occured while adding DocumentSnapshot", e);
                        Toast.makeText(AddCarActivity.this, "Adding offer failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private boolean validateData(TextView ... textViews) {
        //validate data input by user

        for (int i = 0; i < textViews.length; i++) {
            if (String.valueOf(textViews[i].getText()).isEmpty()) {
                Toast.makeText(AddCarActivity.this, "Please, fill " + String.valueOf(textViews[i].getHint()) + " field.",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }


    private List<String> generateListOfIDs(int numberOfIdsToGenerate) {
        List<String> uuidStringList = new ArrayList<String>();


        for (int i = 0; i < numberOfIdsToGenerate; i++) {
            String uuidString = UUID.randomUUID().toString();

            while (uuidStringList.contains(uuidString) == true) {
                uuidString = UUID.randomUUID().toString();
            }

            uuidStringList.add(uuidString);
        }

        return uuidStringList;
    }

}