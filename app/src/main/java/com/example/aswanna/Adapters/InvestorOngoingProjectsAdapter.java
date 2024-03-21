
package com.example.aswanna.Adapters;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aswanna.Model.Proposal;

import com.example.aswanna.R;

import java.util.List;

public class InvestorOngoingProjectsAdapter extends RecyclerView.Adapter<InvestorOngoingProjectsAdapter.ProposalViewHolder> {
    private List<Proposal> proposals;
    private OnButtonClickListener buttonClickListener;

    public InvestorOngoingProjectsAdapter(List<Proposal> proposals, OnButtonClickListener buttonClickListener) {
        this.proposals = proposals;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public ProposalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investor_ongoing_project_item, parent, false);
        return new ProposalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProposalViewHolder holder, int position) {
        Proposal proposal = proposals.get(position);
        Log.d("Adapter", "Proposal at position " + position + ": " + proposal.getProjectName());

        holder.bind(proposal);
    }

    @Override
    public int getItemCount() {
        return proposals.size();
    }

    public class ProposalViewHolder extends RecyclerView.ViewHolder {
        private TextView endDate, startDate, farmerName, projectName, viewProjectsOn;
        private ImageView proposalImage;
        private Button actionButton; // Add a Button

        public ProposalViewHolder(View itemView) {
            super(itemView);
            proposalImage = itemView.findViewById(R.id.imageView66);
            endDate = itemView.findViewById(R.id.textView356ong);
            startDate = itemView.findViewById(R.id.textView346ong);
            farmerName = itemView.findViewById(R.id.textView336ong);
            projectName = itemView.findViewById(R.id.textViewong);
            viewProjectsOn = itemView.findViewById(R.id.textView376ong);

            if (viewProjectsOn != null) {
                viewProjectsOn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle TextView click actions
                    }
                });
            }
        }

        public void bind(Proposal proposal) {
            // Load and display images using an image loading library (e.g., Glide or Picasso)
            if (proposal.getImageOneLink() != null) {
                Glide.with(itemView.getContext()).load(proposal.getImageOneLink()).into(proposalImage);
                //  byte[] bytes = Base64.decode(proposal.getFarmerProfileImage(),Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                profileImage.setImageBitmap(bitmap);


            } else {
                // Handle the case where the image URL is null or empty
            }

            projectName.setText(proposal.getProjectName());
            farmerName.setText(proposal.getFarmerName());
            startDate.setText(proposal.getStartDate());
            endDate.setText(proposal.getEndDate());

        }
    }

    // Callback interface to handle button clicks
    public interface OnButtonClickListener {
        void onButtonClick(Proposal proposal);
    }
}
