package com.example.aswanna.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.aswanna.Activities.ChatActivity;
import com.example.aswanna.Activities.InvestorPostView;
import com.example.aswanna.Activities.ProposalAdd;
import com.example.aswanna.Farmer_Home_Page;
import com.example.aswanna.R;


public class ProductAddFive extends Fragment {








    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_product_add_five, container, false);

        ProposalAdd proposalAdd=(ProposalAdd) getActivity();

        ImageView imageView2 = proposalAdd.findViewById(R.id.two);
        ImageView imageView3 = proposalAdd.findViewById(R.id.three);
        ImageView imageView4 = proposalAdd.findViewById(R.id.four);

        Drawable drawable4 = ContextCompat.getDrawable(requireContext(), R.drawable.numfourb);
        Drawable drawable3 = ContextCompat.getDrawable(requireContext(), R.drawable.numthreeb);
        Drawable drawable2 = ContextCompat.getDrawable(requireContext(), R.drawable.numtwob);











        Button btn=proposalAdd.findViewById(R.id.nextButton);

        btn.setText("Go To DashBoard");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code to display a Toast message here.




                Intent chatIntent = new Intent(getContext(), Farmer_Home_Page.class);

                // Pass the farmerid as an extra to the Chat activity


                // Start the Chat activity
                startActivity(chatIntent);







            }
        });







        return view ;
    }
}