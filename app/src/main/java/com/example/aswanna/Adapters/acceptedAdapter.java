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

import java.util.List;




public class acceptedAdapter extends RecyclerView.Adapter<acceptedAdapter.RequestViewHolder>{

    private List<Inquiry> inquiries;
    private OnButtonClickListener buttonClickListener;

    public acceptedAdapter(List<Inquiry> inquiries, OnButtonClickListener buttonClickListener) {
        this.inquiries = inquiries;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acceptedrec, parent, false);
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


        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            projectID = itemView.findViewById(R.id.i4);
            projectName = itemView.findViewById(R.id.i3);
            investorName = itemView.findViewById(R.id.i2);

//            investorImageI = itemView.findViewById(R.id.investorImageI);
//            acceoptI = itemView.findViewById(R.id.acceoptI);
//            rejectI = itemView.findViewById(R.id.rejectI);




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
            projectName.setText(inquiry.getStatus());
            projectID.setText(inquiry.getProjectId());
            investorName.setText(inquiry.getFarmerName());

        }
    }

    // Callback interface to handle button clicks
    public interface OnButtonClickListener {
        void onButtonClick(Inquiry proposal);
    }

}
