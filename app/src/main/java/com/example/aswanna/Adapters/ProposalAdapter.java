package com.example.aswanna.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import com.example.aswanna.Model.User;
import com.example.aswanna.R;

import java.util.List;

public class ProposalAdapter extends RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder> {
    private List<Proposal> proposals;
    private OnButtonClickListener buttonClickListener;




    public ProposalAdapter(List<Proposal> proposals, OnButtonClickListener buttonClickListener) {
        this.proposals = proposals;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public ProposalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card_item, parent, false);
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
        private TextView userName, postDate, userLevel, projectName, pLocation, profit, pAmount,postedDate;
        private ImageView proposalImage, profileImage;
        private Button actionButton; // Add a Button

        public ProposalViewHolder(View itemView) {
            super(itemView);
            proposalImage = itemView.findViewById(R.id.pProjectImage);
            postDate = itemView.findViewById(R.id.pPostedDate);
            profileImage = itemView.findViewById(R.id.pUserImage);
            userName = itemView.findViewById(R.id.pUserName);
            postDate = itemView.findViewById(R.id.pPostedDate);
            userLevel = itemView.findViewById(R.id.pFarmerLeve);
            projectName = itemView.findViewById(R.id.pProjectName);
            profit = itemView.findViewById(R.id.pProfit);
            pAmount = itemView.findViewById(R.id.pAmount);
            pLocation = itemView.findViewById(R.id.pLocation);
            actionButton = itemView.findViewById(R.id.pViewButton); // Replace with your Button ID

            // Set an OnClickListener for the button
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && buttonClickListener != null) {
                        buttonClickListener.onButtonClick(proposals.get(position));
                    }
                }
            });
        }

        public void bind(Proposal proposal) {
            // Load and display images using an image loading library (e.g., Glide or Picasso)
            if (proposal.getImageOneLink() != null) {
                Glide.with(itemView.getContext()).load(proposal.getImageOneLink()).into(proposalImage);
                byte[] bytes = Base64.decode(proposal.getFarmerProfileImage(),Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                profileImage.setImageBitmap(bitmap);


            } else {
                // Handle the case where the image URL is null or empty
            }

            projectName.setText(proposal.getProjectName());
            postDate.setText(proposal.getPostedDate());
            pLocation.setText(proposal.getProjectLocation());
            profit.setText("Profit-"+proposal.getExpectedReturnsOnInvestment()+"%");
            pAmount.setText("Rs "+String.valueOf(proposal.getFundingRequired())+".00");
            userName.setText(proposal.getFarmerName());
            userLevel.setText("Level " + proposal.getFarmerLevel());
        }
    }

    // Callback interface to handle button clicks
    public interface OnButtonClickListener {
        void onButtonClick(Proposal proposal);
    }
}
