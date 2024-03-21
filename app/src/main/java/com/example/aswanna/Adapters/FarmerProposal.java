package com.example.aswanna.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aswanna.Model.Proposal;
import com.example.aswanna.R;

import java.util.ArrayList;

public class FarmerProposal extends RecyclerView.Adapter<FarmerProposal.ViewHolder> {

    private ArrayList<Proposal> pList;


    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteButtonClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public FarmerProposal.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.proposalcardfarmer,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerProposal.ViewHolder holder, int position) {

        Proposal proposal=pList.get(position);
        holder.proposalID.setText("Proposal ID-" + proposal.getPID());

        holder.proposalType.setText(proposal.getProjectType());
        holder.postedDate.setText("Posted Date "+proposal.getPostedDate());
        String imageLink=proposal.getImageOneLink();
        Glide.with(holder.itemView.getContext()).load(imageLink).into(holder.proposalImage);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDeleteButtonClick(position);
                }
            }
        });


    }

    public FarmerProposal(ArrayList<Proposal> pList) {
        this.pList = pList;
    }


    @Override
    public int getItemCount() {
        return pList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView proposalID,proposalType,postedDate;
        ImageView proposalImage,delete,update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            proposalID=itemView.findViewById(R.id.proid);
            proposalType=itemView.findViewById(R.id.proposalName);
            postedDate=itemView.findViewById(R.id.date);
            proposalImage=itemView.findViewById(R.id.proposalimage);
            delete=itemView.findViewById(R.id.delete);
            update=itemView.findViewById(R.id.edit);





        }
    }



}
