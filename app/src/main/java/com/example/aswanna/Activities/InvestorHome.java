package com.example.aswanna.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aswanna.Fragment.HomePageInvestorFragment;
import com.example.aswanna.Fragment.InvestorOngoingProjectsFragment;
import com.example.aswanna.Fragment.InvestorProfile;
import com.example.aswanna.Massage_Main_Activity;
import com.example.aswanna.R;

public class InvestorHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_home);

        ImageView projects = findViewById(R.id.imageView12);
        ImageView bothome = findViewById(R.id.bothome45);
        ImageView menue=findViewById(R.id.imageView11);
        ImageView chatInvestorSide=findViewById(R.id.chatInvestorSide);

        chatInvestorSide.setOnClickListener(view -> {
            // Create an Intent to start the Chat activity
            Intent chatIntent = new Intent(InvestorHome.this, Massage_Main_Activity.class);


            // Start the Chat activity
            startActivity(chatIntent);
        });


        bothome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the target activity
                Intent intent = new Intent(InvestorHome.this, ChatBotMain.class);

                // You can also pass extra data to the target activity if needed
                // intent.putExtra("key", value);

                // Start the target activity
                startActivity(intent);
            }
        });

        projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the target activity
                Intent intent = new Intent(InvestorHome.this, InvestorMyProjects.class);

                // You can also pass extra data to the target activity if needed
                // intent.putExtra("key", value);

                // Start the target activity
                startActivity(intent);
            }
        });
        ImageView home = findViewById(R.id.imageView14);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with NewFragment
                replaceFragment(new HomePageInvestorFragment());
            }
        });

        menue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with NewFragment
                replaceFragment(new InvestorProfile());
            }
        });



    }
    private void replaceFragment(Fragment newFragment) {
        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Start a new FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragmentContainerView2, newFragment); // Replace 'fragmentContainer' with your container's ID

        // Add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}