package com.example.aswanna.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aswanna.Adapters.acceptedAdapter;
import com.example.aswanna.Model.Inquiry;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Farmer_Side_Accepted_request extends Fragment {


    private RecyclerView recyclerView;
    private acceptedAdapter adapter;
    private List<Inquiry> inquiries;
    PreferenceManager preferenceManager;
    String farmerID ;
    public Farmer_Side_Accepted_request() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_farmer__side__accepted_request, container, false);
        // Inflate the layout for this fragment

        preferenceManager =new PreferenceManager(getContext());
        farmerID=preferenceManager.getString(User.KEY_USER_ID);

        recyclerView = view.findViewById(R.id.acceptR);
        inquiries = new ArrayList<>();
        adapter = new acceptedAdapter(inquiries, new acceptedAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(Inquiry inquiry) {
                // Handle button click actions if needed
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference proposalsRef = db.collection("Inquiries");

        Query query = proposalsRef
                .whereEqualTo("farmerID", farmerID)
                .whereEqualTo("status", "accepted");




        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (Inquiry inquiry : queryDocumentSnapshots.toObjects(Inquiry.class)) {
                    inquiries.add(inquiry);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            Log.e("FirestoreError", "Error fetching data: " + e.getMessage());
        });

        return view;



    }
}