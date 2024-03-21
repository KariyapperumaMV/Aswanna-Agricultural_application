package com.example.aswanna.Adapters;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.aswanna.Activities.InvestorHome;
import com.example.aswanna.Activities.InvestorMyProjects;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.Proposal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.aswanna.Model.RatingFeedback;
import com.example.aswanna.R;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvesotorCompletedOrderAapter extends RecyclerView.Adapter<InvesotorCompletedOrderAapter.ProposalViewHolder> {
    private List<Proposal> proposals;
    private OnButtonClickListener buttonClickListener;
    private PreferenceManager preferenceManager;



    public InvesotorCompletedOrderAapter(List<Proposal> proposals, OnButtonClickListener buttonClickListener) {
        this.proposals = proposals;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public ProposalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investor_completed_order_item, parent, false);
        return new ProposalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProposalViewHolder holder, int position) {
        Proposal proposal = proposals.get(position);
        holder.bind(proposal);
    }

    @Override
    public int getItemCount() {
        return proposals.size();
    }

    public class ProposalViewHolder extends RecyclerView.ViewHolder {
        private TextView endDate, startDate, farmerName, projectName, feedback;
        private ImageView proposalImage;
        private Button actionButton;

        public ProposalViewHolder(View itemView) {
            super(itemView);
            proposalImage = itemView.findViewById(R.id.imageViewinc);
            endDate = itemView.findViewById(R.id.textView35);
            startDate = itemView.findViewById(R.id.textView34);
            farmerName = itemView.findViewById(R.id.textView33);
            projectName = itemView.findViewById(R.id.textView32);
            feedback = itemView.findViewById(R.id.textView36);

            if (feedback != null) {
                feedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFeedbackDialog(proposals.get(getAdapterPosition()));
                    }
                });
            }
        }

        public void bind(Proposal proposal) {
            if (proposal.getImageOneLink() != null) {
                Glide.with(itemView.getContext()).load(proposal.getImageOneLink()).into(proposalImage);
            } else {
                // Handle the case where the image URL is null or empty
            }

            projectName.setText(proposal.getProjectName());
            farmerName.setText("Farmer - "+proposal.getFarmerName());
            startDate.setText("Start Date - "+proposal.getStartDate());
            endDate.setText("End Date - "+proposal.getEndDate());
        }

        private void showFeedbackDialog(Proposal proposal) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            View dialogView = inflater.inflate(R.layout.feedback_dialog, null);
            builder.setView(dialogView);

            RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
            EditText feedbackEditText = dialogView.findViewById(R.id.feedbackEditText);
            String investorId="133";
            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String feedbackText = feedbackEditText.getText().toString();
                    float rating = ratingBar.getRating();

                    saveRatingFeedbackToFirestore(proposal.getFarmerID(), rating, feedbackText,investorId);

                    dialog.dismiss();
                    Intent intent = new Intent(itemView.getContext(), InvestorMyProjects.class);
                    // If you are using an Activity
                    // intent = new Intent(itemView.getContext(), YourActivity.class);



                    // Start the CompletedOrderFragment
                    itemView.getContext().startActivity(intent);

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }


        private void saveRatingFeedbackToFirestore(String farmerID, float rating, String feedback,String investorID) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference ratingsFeedbackCollection = db.collection("RatingsFeedback");

            RatingFeedback ratingFeedback = new RatingFeedback(rating, feedback,investorID,farmerID);

            ratingsFeedbackCollection.add(ratingFeedback)
                    .addOnSuccessListener(documentReference -> {
                        // The Inquiry was successfully added to the "Inquiries" collection
                        // You can add any further actions or messages here
                        // For example, showing a success message or returning to a previous screen
                    });
            // Update the farmer's rating in the FarmerRatings collection
            DocumentReference farmerRatingsRef = db.collection("FarmerRating").document(farmerID);
            farmerRatingsRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Calculate the new average rating and update the document
                    double currentRating = documentSnapshot.getDouble("rating");
                    int totalRatings = documentSnapshot.getLong("totalRatings").intValue();

                    double newRating = ((currentRating * totalRatings) + rating) / (totalRatings + 1);
                    totalRatings++;

                    // Update the document with the new rating and total ratings
                    farmerRatingsRef.update("rating", newRating, "totalRatings", totalRatings)
                            .addOnSuccessListener(aVoid -> {
                                // Farmer rating updated successfully
                            });
                }else {
                    // If the document doesn't exist, create a new document with initial values
                    Map<String, Object> initialRating = new HashMap<>();
                    initialRating.put("rating", rating);
                    initialRating.put("totalRatings", 1);

                    db.collection("FarmerRating").document(farmerID)
                            .set(initialRating)
                            .addOnSuccessListener(aVoid -> {
                                // New farmer rating document created
                                // You can add any further actions or messages here
                            });
                }
            });
        }
    }

    public interface OnButtonClickListener {
        void onButtonClick(Proposal proposal);
    }
}
