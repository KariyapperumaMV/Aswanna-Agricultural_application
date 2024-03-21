package com.example.aswanna.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.aswanna.Adapters.FarmerProposal;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.Proposal;
import com.example.aswanna.Model.User;
import com.example.aswanna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FarmerProposalList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FarmerProposal proposalAdapter;

    ImageView backBtn;
    private ArrayList<Proposal> proposalList = new ArrayList<>();

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_proposal_list);


        recyclerView = findViewById(R.id.proposalList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        proposalAdapter = new FarmerProposal(proposalList);

        backBtn=findViewById(R.id.backButton);

        preferenceManager = new PreferenceManager(this);



        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference proposalsRef = firestore.collection("proposals");

        proposalsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {



                            // Iterate through the documents and add them to the list
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Proposal proposal = document.toObject(Proposal.class);

                                if(proposal.getFarmerID().equals( preferenceManager.getString(User.KEY_USER_ID))){

                                    proposalList.add(proposal);

                                }


                            }




                        recyclerView.setAdapter(proposalAdapter);




                    }

                });


        proposalAdapter.setOnItemClickListener(new FarmerProposal.OnItemClickListener() {
            @Override
            public void onDeleteButtonClick(int position) {
                showDeleteConfirmationDialog(position);
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code to display a Toast message here.



//                Intent intent = new Intent(FarmerProposalList.this, NotificationManagement.class);
//                startActivity(intent);








            }
        });






    }


    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate your custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.deletedialog, null);

        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        // Get the "Delete" and "Cancel" buttons from the custom layout
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the user's confirmation to delete
                Proposal proposalToDelete = proposalList.get(position);
                deleteItemFromFirestore(proposalToDelete);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the user's choice not to delete
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void deleteItemFromFirestore(Proposal proposal) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // Get the reference to the Firestore document you want to delete
        CollectionReference proposalsRef = firestore.collection("proposals");
        proposalsRef.document(proposal.getDocumentID()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Handle successful deletion (e.g., remove the item from the local list)
                            proposalList.remove(proposal);
                            proposalAdapter.notifyDataSetChanged();
                        } else {
                            // Handle deletion failure
                            // You can display an error message or take other actions as needed
                        }
                    }
                });
    }



}