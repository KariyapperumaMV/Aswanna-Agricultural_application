package com.example.aswanna.Adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aswanna.Model.Inquiry;

import com.example.aswanna.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class RequestPendingListFarmerSide_Adapter extends RecyclerView.Adapter<RequestPendingListFarmerSide_Adapter.RequestViewHolder>{

    private List<Inquiry> inquiries;
    private OnButtonClickListener buttonClickListener;

    public RequestPendingListFarmerSide_Adapter(List<Inquiry> inquiries,OnButtonClickListener buttonClickListener) {
        this.inquiries = inquiries;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_request_container, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Inquiry inquiry = inquiries.get(position);
        holder.bind(inquiry);
    }

    @Override
    public int getItemCount() {
        return inquiries.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        private TextView projectID, projectName, investorName;
        private ImageView investorImageI,acceoptI,rejectI;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            projectID = itemView.findViewById(R.id.projectID);
            projectName = itemView.findViewById(R.id.projectName);
            investorName = itemView.findViewById(R.id.investorName);

            investorImageI = itemView.findViewById(R.id.investorImageI);
            acceoptI = itemView.findViewById(R.id.acceoptI);
            rejectI = itemView.findViewById(R.id.rejectI);



            acceoptI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show a confirmation dialog
                    showConfirmationDialog(inquiries.get(getAdapterPosition()));
                }
            });
            rejectI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show a confirmation dialog to delete the inquiry
                    showDeleteConfirmationDialog(inquiries.get(getAdapterPosition()));
                }
            });
        }
        private void showDeleteConfirmationDialog(final Inquiry inquiry) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Do you want to reject this request?");
            builder.setMessage("This will delete this request  perminently. You can not undo this action ?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle the "Delete" button click
                    if (buttonClickListener != null) {
                        buttonClickListener.onButtonClick(inquiry);

                        // If you want to delete the inquiry from the list, you can do so here
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            inquiries.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle the "Cancel" button click (optional)
                    dialog.dismiss(); // Close the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void showConfirmationDialog(final Inquiry inquiry) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Do you want to accept this request?");
            builder.setMessage("This will accept this request and investor assign with your project.");
            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle the "Accept" button click
                    if (buttonClickListener != null) {
                        buttonClickListener.onButtonClick(inquiry);

                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle the "Cancel" button click (optional)

                    dialog.dismiss(); // Close the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }




        public void bind(Inquiry inquiry) {
            // Load and display images using an image loading library (e.g., Glide or Picasso)
            if (inquiry.getImage() != null) {
                //  byte[] bytes = Base64.decode(proposal.getFarmerProfileImage(),Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                profileImage.setImageBitmap(bitmap);


            } else {
                // Handle the case where the image URL is null or empty
            }
            projectName.setText(inquiry.getProjectName());
            projectID.setText(inquiry.getProjectId());
            investorName.setText(inquiry.getFarmerName());

        }
    }

    // Callback interface to handle button clicks
    public interface OnButtonClickListener {
        void onButtonClick(Inquiry proposal);
    }

}
