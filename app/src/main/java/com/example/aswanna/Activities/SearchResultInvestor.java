package com.example.aswanna.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.aswanna.Adapters.ProposalAdapter;
import com.example.aswanna.Model.FilterData;
import com.example.aswanna.Model.Proposal;
import com.example.aswanna.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class SearchResultInvestor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProposalAdapter adapter;
    private List<Proposal> proposals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_investor);
        Intent intent = getIntent();

        // Get the search query from the intent
        String searchQuery = intent.getStringExtra("searchQuery");

        ImageView back = findViewById(R.id.imageView7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the target activity
                Intent intent = new Intent(SearchResultInvestor.this, InvestorHome.class);

                // You can also pass extra data to the target activity if needed
                // intent.putExtra("key", value);

                // Start the target activity
                startActivity(intent);
            }
        });
        // Check if additional filter parameters are present
        if (intent.hasExtra("filterData")) {
            FilterData filterData = (FilterData) intent.getSerializableExtra("filterData");
            String location = filterData.getSpinner1Value();
            String type = filterData.getSpinner2Value();
            int min = Integer.parseInt(filterData.getEditText1Value());
            int max = Integer.parseInt(filterData.getEditText2Value());
            String level=filterData.getSpinner3Value();
            String newString;
            newString = level.substring(6);

            // Initialize RecyclerView and adapter
            recyclerView = findViewById(R.id.searchresult);
            proposals = new ArrayList<>();
            adapter = new ProposalAdapter(proposals, new ProposalAdapter.OnButtonClickListener() {
                @Override
                public void onButtonClick(Proposal proposal) {

                }
            });

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Initialize Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference proposalsRef = db.collection("proposals");

            // Query the "proposals" collection based on filter criteria
            Query query = proposalsRef
                    .whereEqualTo("projectLocation", location)
                    .whereGreaterThanOrEqualTo("fundingRequired", min)
                    .whereLessThanOrEqualTo("fundingRequired", max)
                    .whereEqualTo("projectType", type)
                    .whereEqualTo("farmerLevel", newString);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (Proposal proposal : queryDocumentSnapshots.toObjects(Proposal.class)) {
                        // Add proposals that have project names containing the search query
                        if (proposal.getProjectName().toLowerCase().contains(searchQuery.toLowerCase())) {
                            proposals.add(proposal);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(e -> {
                Log.e("FirestoreError", "Error fetching data: " + e.getMessage());
            });
        } else {
            // Handle the case when only the search query is provided
            // Perform your search based on the searchQuery (filter by project names)
            recyclerView = findViewById(R.id.searchresult);
            proposals = new ArrayList<>();
            adapter = new ProposalAdapter(proposals, new ProposalAdapter.OnButtonClickListener() {
                @Override
                public void onButtonClick(Proposal proposal) {
                    Intent intent = new Intent(SearchResultInvestor.this, InvestorPostView.class);
                    intent.putExtra("proposal", proposal); // Pass the selected proposal to the next activity
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Initialize Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference proposalsRef = db.collection("proposals");

            // Query the "proposals" collection based on the search query
            Query query = proposalsRef
                    .orderBy("projectName")  // Sort the results by project names
                    .startAt(searchQuery)     // Start at the search query
                    .endAt(searchQuery + "\uf8ff");

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
        }
    }
}
