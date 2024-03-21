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

import com.example.aswanna.Fragment.InvestorCompletedProjectsFragment;
import com.example.aswanna.Fragment.InvestorOngoingProjectsFragment;
import com.example.aswanna.R;

public class InvestorMyProjects extends AppCompatActivity {
    private boolean isCompletedSelected = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_my_projects);


        ImageView completed = findViewById(R.id.imageView22);
        ImageView ongoing = findViewById(R.id.imageView23);
        ImageView back = findViewById(R.id.imageView15);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the target activity
                Intent intent = new Intent(InvestorMyProjects.this, InvestorHome.class);

                // You can also pass extra data to the target activity if needed
                // intent.putExtra("key", value);

                // Start the target activity
                startActivity(intent);
            }
        });
        // Set an OnClickListener for the TextView
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with NewFragment
                replaceFragment(new InvestorCompletedProjectsFragment());
                ongoing.setImageResource(R.drawable.ongoingnonselected);
                completed.setImageResource(R.drawable.completedselected);
            }
        });
        ongoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with NewFragment
                replaceFragment(new InvestorOngoingProjectsFragment());
                ongoing.setImageResource(R.drawable.ongoingselected);
                completed.setImageResource(R.drawable.completenonselect);
            }
        });
    }

    private void replaceFragment(Fragment newFragment) {
        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Start a new FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragmentContainerViewcin, newFragment); // Replace 'fragmentContainer' with your container's ID

        // Add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}