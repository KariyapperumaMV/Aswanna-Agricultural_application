package com.example.aswanna.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.aswanna.Farmer_Home_Page;
import com.example.aswanna.Fragment.FarmerSide_pending_request;
import com.example.aswanna.Fragment.Farmer_Side_Accepted_request;
import com.example.aswanna.Fragment.HomePageInvestorFragment;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.R;

public class Farmer_Request_View extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private boolean isCompletedSelected = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_request_view);
        preferenceManager = new PreferenceManager(getApplicationContext());

        ImageView back = findViewById(R.id.BtnBACKView);
        ImageView pending = findViewById(R.id.pendingGreenBtn);
        ImageView accepted = findViewById(R.id.acceptedWhiteBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the target activity
                Intent intent = new Intent(Farmer_Request_View.this, Farmer_Home_Page.class);
                // Start the target activity
                startActivity(intent);
            }
        });
        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with NewFragment
                replaceFragment(new Farmer_Side_Accepted_request());
                accepted.setImageResource(R.drawable.accepted_req_greeeen_farmer);
                pending.setImageResource(R.drawable.pending_req_farmer_white);
            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with NewFragment
                replaceFragment(new FarmerSide_pending_request());
                accepted.setImageResource(R.drawable.accepted_farmerreq_white);
                pending.setImageResource(R.drawable.pending_req_green_farmer);
            }
        });

    }
    private void replaceFragment(Fragment newFragment) {
        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Start a new FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragmentReqView, newFragment); // Replace 'fragmentContainer' with your container's ID

        // Add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}