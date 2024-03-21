package com.example.aswanna.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aswanna.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class testaa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testaa);

        Button insertDataButton = findViewById(R.id.button2);

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        insertDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a data object (change this as per your data structure)
                Map<String, Object> data = new HashMap<>();
                data.put("name", "John Doe");

                // Add more fields as needed

                // Add data to Firestore
                db.collection("bavantha")
                        .add(data)
                        .addOnSuccessListener(documentReference -> {
                            // Data added successfully
                        })
                        .addOnFailureListener(e -> {
                            // Handle any errors
                        });
            }
        });
    }
}
