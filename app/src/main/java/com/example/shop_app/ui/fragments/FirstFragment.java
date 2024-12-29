package com.example.shop_app.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shop_app.DataFetcher;
import com.example.shop_app.MyNewRecyclerViewInterface;
import com.example.shop_app.R;
import com.example.shop_app.databinding.FragmentFirstBinding;
import com.example.shop_app.BasicCarInfo;
import com.example.shop_app.ui.activities.MainActivity;
import com.example.shop_app.ui.adapters.MyNewAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.ArrayList;

import javax.annotation.Nullable;



public class FirstFragment extends Fragment implements MyNewRecyclerViewInterface {
    private final static String TAG = "FirstFragment";
    private FragmentFirstBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private StorageReference storageReferenceToDocumentFolder;
    private RecyclerView recyclerView;
    private MyNewAdapter myNewAdapter;
    private List<BasicCarInfo> basicCarInfoList;
    private Uri onlineImageUri;
    private View view;
    private ListenerRegistration registration;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "I'm in onCreate");

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
        Log.i(TAG, "I'm in onCreateView");
        // Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "I'm in onViewCreated");

        basicCarInfoList = new ArrayList<BasicCarInfo>();
        myNewAdapter = new MyNewAdapter(getContext(), basicCarInfoList, this);

        recyclerView = binding.recyclerviewOffers;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(myNewAdapter);


        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
        final String collectionPath = "cars";

        registration = firestoreDb.collection(collectionPath).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                List<DocumentChange> listOfDocumentsChanges = value.getDocumentChanges();
                for (DocumentChange changedDocument : listOfDocumentsChanges) {

                    if (changedDocument.getType() == DocumentChange.Type.ADDED) {

                        DocumentSnapshot changedDocumentSnapshot = changedDocument.getDocument();
                        Object[] changedDocumentFieldsValues = DataFetcher.obtainDataFromDocument(changedDocumentSnapshot, "Brand", "Model", "Year of production", "Price [PLN]", "mainImageId", "uid");

                        final String currentDocumentId = changedDocumentSnapshot.getId();
                        final String pathToCurrentDocument = collectionPath + "/" + currentDocumentId;
                        final String pathToCurrentDocumentFileFolder = "images/" + changedDocumentFieldsValues[5] + "/" + currentDocumentId;

                        storageReferenceToDocumentFolder = FirebaseStorage.getInstance().getReference(pathToCurrentDocumentFileFolder);
                        storageReferenceToDocumentFolder.child(String.valueOf(changedDocumentFieldsValues[4])).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                onlineImageUri = uri;
                                basicCarInfoList.add(
                                        new BasicCarInfo(
                                                changedDocumentFieldsValues[0].toString(),
                                                changedDocumentFieldsValues[1].toString(),
                                                changedDocumentFieldsValues[2].toString(),
                                                changedDocumentFieldsValues[3].toString(),
                                                onlineImageUri,
                                                pathToCurrentDocument,
                                                pathToCurrentDocumentFileFolder
                                        )
                                );
                                myNewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registration != null) {
            registration.remove();
        }
    }

    @Override
    public void onItemViewClick(int position) {

        Bundle bundle = new Bundle();

        bundle.putString("Brand", basicCarInfoList.get(position).getBrand());
        bundle.putString("Model", basicCarInfoList.get(position).getModel());
        bundle.putString("Year of production", basicCarInfoList.get(position).getYearOfProduction());
        bundle.putString("Price [PLN]", basicCarInfoList.get(position).getPrice());
        bundle.putString("Online image Uri", basicCarInfoList.get(position).getMainImageUri().toString());
        bundle.putString("Path to chosen document", basicCarInfoList.get(position).getPathToDocument());
        bundle.putString("Path to chosen document files folder", basicCarInfoList.get(position).getPathToDocumentFileFolder());

        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_container_view);
        navController.navigate(R.id.offerDetailsFragment, bundle);
    }
}





