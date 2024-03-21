package com.example.aswanna.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.aswanna.Activities.FilterBottomSheetFragment;
import com.example.aswanna.Activities.InvestorPostView;
import com.example.aswanna.Activities.SearchResultInvestor;
import com.example.aswanna.Adapters.ProposalAdapter;
import com.example.aswanna.Model.Proposal;
import com.example.aswanna.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomePageInvestorFragment extends Fragment {

    private String text = ""; // Declare a variable to store the search query

    // ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_investor, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView); // Replace with your RecyclerView ID
        ImageView filter = view.findViewById(R.id.imageView4);

        SearchView searchView = view.findViewById(R.id.SearchView);

        // Capture text changes in the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(getActivity(), SearchResultInvestor.class);
                searchIntent.putExtra("searchQuery", text);
                startActivity(searchIntent);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Store the search query in the 'text' variable
                text = newText;
                return true;
            }
        });

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference proposalsRef = db.collection("proposals"); // Replace with the actual Firestore collection name

        Query query = proposalsRef
                .whereEqualTo("status", "on");

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Proposal> proposals = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Proposal proposal = document.toObject(Proposal.class);
                // Fetch user details based on farmerID from another Firestore collection
                CollectionReference usersRef = db.collection("users"); // Replace with the actual Firestore collection name

                // Assuming "name" and "profileImage" are the field names in the user document

                // Set the user details in the Proposal object

                proposals.add(proposal);

                // Notify the adapter when all data is retrieved
                if (proposals.size() == queryDocumentSnapshots.size()) {
                    ProposalAdapter adapter = new ProposalAdapter(proposals, new ProposalAdapter.OnButtonClickListener() {
                        @Override
                        public void onButtonClick(Proposal proposal) {
                            // Handle the button click here
                            // You can start a new activity or perform any action you need
                            Intent intent = new Intent(getActivity(), InvestorPostView.class);
                            // Pass the proposal data to the new activity
                            intent.putExtra("proposal", proposal);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterBottomSheet();
            }
        });
        return view;
    }

    public void openFilterBottomSheet() {
        FilterBottomSheetFragment bottomSheetFragment = FilterBottomSheetFragment.newInstance(text);
        bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
    }
}
