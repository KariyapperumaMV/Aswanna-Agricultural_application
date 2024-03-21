
//public class InvestorOngoingProjectsFragment extends Fragment {
package com.example.aswanna.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aswanna.Adapters.InvestorOngoingProjectsAdapter;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.Proposal;
import com.example.aswanna.Model.User;
import com.example.aswanna.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InvestorOngoingProjectsFragment extends Fragment {

    private RecyclerView recyclerView;
    private InvestorOngoingProjectsAdapter adapter;
    private List<Proposal> proposals;

    private PreferenceManager preferenceManager;

    public InvestorOngoingProjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investor_ongoing_projects, container, false);


        preferenceManager = new PreferenceManager(getContext());

        String investorId=preferenceManager.getString(User.KEY_USER_ID);

        // Initialize RecyclerView and adapter
        recyclerView = view.findViewById(R.id.recycleviewOngoingProjects);
        proposals = new ArrayList<>();
        adapter = new InvestorOngoingProjectsAdapter(proposals, new InvestorOngoingProjectsAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(Proposal proposal) {
                // Handle button click actions if needed
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference proposalsRef = db.collection("proposals");

        // Query the "proposals" collection based on filter criteria
        Query query = proposalsRef
                .whereEqualTo("investorID", investorId)
                .whereEqualTo("status", "ongoing");

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (Proposal proposal : queryDocumentSnapshots.toObjects(Proposal.class)) {
                    proposals.add(proposal);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            Log.e("FirestoreError", "Error fetching data: " + e.getMessage());
        });

        return view;
    }
}
