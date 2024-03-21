package com.example.aswanna.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aswanna.Adapters.RequestPendingListFarmerSide_Adapter;
import com.example.aswanna.Model.Inquiry;
import com.example.aswanna.Model.PreferenceManager;

import com.example.aswanna.Model.User;
import com.example.aswanna.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FarmerSide_pending_request extends Fragment {

    private RecyclerView recyclerView;
    private RequestPendingListFarmerSide_Adapter adapter;
    private List<Inquiry> inquiries;
    PreferenceManager preferenceManager;
    String farmerID ;
    public FarmerSide_pending_request() {
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


        View view = inflater.inflate(R.layout.fragment_farmer_side_pending_request, container, false);

        preferenceManager =new PreferenceManager(getContext());
        farmerID=preferenceManager.getString(User.KEY_USER_ID);



        recyclerView = view.findViewById(R.id.pendingRecycler);
        inquiries = new ArrayList<>();

        adapter = new RequestPendingListFarmerSide_Adapter(inquiries, new RequestPendingListFarmerSide_Adapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(Inquiry inquiry) {
                // Handle button click actions if needed
                updateStatusToAccepted(inquiry);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference proposalsRef = db.collection("Inquiries");

        Query query = proposalsRef
                .whereEqualTo("farmerID", farmerID)
                .whereEqualTo("status", "pending");



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
    private void updateStatusToAccepted(Inquiry inquiry) {
        // Update the status field to "accepted" in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference inquiriesRef = db.collection("Inquiries");

        inquiriesRef.document(inquiry.getDocumentID()).update("status", "accepted")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // The status is updated to "accepted"
                        // You can also update your UI to reflect the change
                        // For example, remove the inquiry from the list
                        inquiries.remove(inquiry);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to update the status
                        Log.e("FirestoreError", "Error updating status: " + e.getMessage());
                    }
                });
    }
}
