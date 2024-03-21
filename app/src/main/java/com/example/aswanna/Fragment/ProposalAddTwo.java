package com.example.aswanna.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aswanna.Activities.ProposalAdd;
import com.example.aswanna.R;
import com.google.android.material.textfield.TextInputEditText;

public class ProposalAddTwo extends Fragment {
    @Nullable

    private String data1,data2,data3,data4;

    TextInputEditText pDescription,pFund,pExpected;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_proposal_add_two, container, false);

        ProposalAdd proposalAdd=(ProposalAdd) getActivity();

        ImageView imageView = proposalAdd.findViewById(R.id.two);
        Drawable drawable1 = ContextCompat.getDrawable(requireContext(), R.drawable.numtwow);
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.correct);

        imageView.setImageDrawable(drawable1);


        pDescription=view.findViewById(R.id.projectDescription);
        pFund=view.findViewById(R.id.fund);
        pExpected=view.findViewById(R.id.expect);


        Button btn=proposalAdd.findViewById(R.id.nextButton);





        Bundle args = getArguments();
        if (args != null) {
             data1 = args.getString("pName");
             data2=args.getString("location");
             data3=args.getString("type");
             data4=args.getString("time");








        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code to display a Toast message here.


               String description=pDescription.getText().toString();
               String  expected=pExpected.getText().toString();
               String fund=pFund.getText().toString();

                if (description.isEmpty()) {
                    pDescription.setError("Project Description cannot be empty");
                    return; // Prevent navigation
                }

                if (expected.isEmpty()) {
                    pExpected.setError("Returns on investment cannot be empty");
                    return; // Prevent navigation
                }

                if (fund.isEmpty()) {
                    pFund.setError("Funding Required cannot be empty");
                    return; // Prevent navigation
                }





                imageView.setImageDrawable(drawable);

                ProductAddThree productAddThree = new ProductAddThree();
                Bundle bundle = new Bundle();
                bundle.putString("pName", data1);
                bundle.putString("location", data2);
                bundle.putString("type", data3);
                bundle.putString("time", data4);
                bundle.putString("description", description);
                bundle.putString("expected", expected);
                bundle.putString("fund", fund);

                productAddThree.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPager,productAddThree,null).addToBackStack(null).commit();




            }
        });





        return view;
    }
}
