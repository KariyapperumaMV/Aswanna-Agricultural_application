package com.example.aswanna.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aswanna.Activities.ProposalAdd;
import com.example.aswanna.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProposalAddOne extends Fragment {


    String[] time = new String[] {
            "0-3 months",
            "3-6 months",
            "6-12 months",
            "12-18 months",
            "18-24 months",
            "24-30 months",
            "30-36 months"
    };
    String[] type = {"Crop Production", "Livestock Farming", "Organic Farming", "Aquaculture", "Agri-Tourism",
            "Farm Equipment Purchase", "Farm Expansion", "Agribusiness Plan", "Community Farming",
            "Sustainable Agriculture", "Rural Development", "Value-Added Agriculture",
            "Agricultural Research", "Food Security", "Grant Proposals"};
    String[] sriLankanDistricts = {"Colombo", "Gampaha", "Kalutara", "Kandy", "Matale", "Nuwara Eliya", "Galle",
            "Matara", "Hambantota", "Jaffna", "Kilinochchi", "Mannar", "Vavuniya", "Mullaitivu", "Batticaloa", "Ampara",
            "Trincomalee", "Kurunegala", "Puttalam", "Anuradhapura", "Polonnaruwa", "Badulla", "Monaragala", "Ratnapura", "Kegalle"};
    AutoCompleteTextView autoCompleteTextView,autoCompleteTextView1,autoCompleteTextView2;

    String typeOne,locationOne,pName,timeOne;

    TextInputEditText projectName;

    Button Click;
    ArrayAdapter<String> adapterItems,adapterItems1,adapterItems2;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);







    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposal_add_one, container, false);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String  token = task.getResult();
                        Log.d("FCM Token", token);
                    } else {
                        // Handle the error if token retrieval fails.
                    }
                });



        projectName=view.findViewById(R.id.projectName);

        ProposalAdd proposalAdd=(ProposalAdd) getActivity();

        Button btn=proposalAdd.findViewById(R.id.nextButton);

        ImageView imageView = proposalAdd.findViewById(R.id.one);
        Drawable drawable1 = ContextCompat.getDrawable(requireContext(), R.drawable.numonew); // Replace "your_drawable" with the name of your drawable resource

        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.correct); // Replace "your_drawable" with the name of your drawable resource


        imageView.setImageDrawable(drawable1);



        autoCompleteTextView2= view.findViewById(R.id.auto_complete_txt2);
        adapterItems2=new ArrayAdapter<String>(requireContext(),R.layout.location_item,time);

        autoCompleteTextView2.setAdapter(adapterItems2);


        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                timeOne=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), timeOne, Toast.LENGTH_SHORT).show();




            }
        });











        autoCompleteTextView1= view.findViewById(R.id.auto_complete_txt1);
        adapterItems1=new ArrayAdapter<String>(requireContext(),R.layout.location_item,type);

        autoCompleteTextView1.setAdapter(adapterItems1);

        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typeOne=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), typeOne, Toast.LENGTH_SHORT).show();




            }
        });













        autoCompleteTextView= view.findViewById(R.id.auto_complete_txt);
        adapterItems=new ArrayAdapter<String>(requireContext(),R.layout.location_item,sriLankanDistricts);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                locationOne=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), locationOne, Toast.LENGTH_SHORT).show();




            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code to display a Toast message here.

                pName=projectName.getText().toString();
                autoCompleteTextView2.setError(null); // Removes the error message
                autoCompleteTextView.setError(null);
                autoCompleteTextView2.setError(null);


                if (pName.isEmpty()) {
                    projectName.setError("Project name cannot be empty");
                    return; // Prevent navigation
                }

                if (locationOne==null) {
                    autoCompleteTextView.setError("Location cannot be empty");
                    return; // Prevent navigation
                }

                if (typeOne==null) {
                    autoCompleteTextView1.setError("Project Type cannot be empty");
                    return; // Prevent navigation
                }

                if (timeOne==null) {
                    autoCompleteTextView2.setError("Project Duration cannot be empty");
                    return; // Prevent navigation
                }







                ProposalAddTwo proposalAddTwo = new ProposalAddTwo();
                Bundle bundle = new Bundle();
                bundle.putString("pName", pName);
                bundle.putString("location", locationOne);
                bundle.putString("type", typeOne);
                bundle.putString("time", timeOne);

                proposalAddTwo.setArguments(bundle);

                imageView.setImageDrawable(drawable);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPager,proposalAddTwo,null).addToBackStack(null).commit();




            }
        });







        return view;
    }
}
