package com.example.aswanna;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aswanna.Activities.NotificationManagement;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> userTokens = new ArrayList<>();

    Button btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.button);

        checkAllMatchingUsers();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First method
                // ...

                // When the first method completes, call the second method
                sendNotificationToAllUsers();
            }
        });




    }

    private void checkAllMatchingUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference investorPreferencesCollection = db.collection("InvestorPreferences");

        investorPreferencesCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String userId = document.getId();

                        // Retrieve the FCM token for the user
                        String fcmToken = document.getString("fcmToken");
                        if (fcmToken != null) {

                            retrieveSwitchStateFromFirestore(userId,fcmToken);
                        }

                        // Continue with other operations if needed.
                    }




                })
                .addOnFailureListener(e -> {
                    // Handle the error when retrieving user preferences
                    Toast.makeText(this, "Error while checking matching preferences", Toast.LENGTH_SHORT).show();
                });
    }





    private void retrieveSwitchStateFromFirestore(String currentUser,String Fcm) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (currentUser != null) {
            CollectionReference userSwitchCollection = db.collection("InvestorPreferences")
                    .document(currentUser)
                    .collection("switchState");

            userSwitchCollection.limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // User switch state document exists, retrieve the switch state
                            DocumentSnapshot userSwitchDocument = queryDocumentSnapshots.getDocuments().get(0);
                            Boolean switchState = userSwitchDocument.getBoolean("switchState");

                            // Set the retrieved switch state to the SwitchCompat

                            if (switchState) {
                                retrieveUserLocations(currentUser,Fcm);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error when retrieving the user switch state document
                        Toast.makeText(this, "Error while retrieving user switch state", Toast.LENGTH_SHORT).show();
                    });
        }















    }







    private void retrieveUserLocations(String userId,String Fcm) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userLocationsCollection = db.collection("InvestorPreferences").document(userId).collection("locations");
        ArrayList<String> userSelectedItems = new ArrayList<>();

        userLocationsCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        if (document.contains("locations")) {
                            ArrayList<String> locations = (ArrayList<String>) document.get("locations");
                            if (locations != null) {
                                userSelectedItems.addAll(locations);

                                retrieveUserTypes(userId,userSelectedItems,Fcm);

                            }
                        }
                    }


                })
                .addOnFailureListener(e -> {
                    // Handle the error when retrieving user locations or types
                    Toast.makeText(this, "Error while retrieving user locations", Toast.LENGTH_SHORT).show();
                });




    }


    private void retrieveUserTypes(String userId, ArrayList<String> locationList,String Fcm) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userLocationsCollection = db.collection("InvestorPreferences").document(userId).collection("types");
        ArrayList<String> userSelectedItems = new ArrayList<>();

        userLocationsCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        if (document.contains("types")) {
                            ArrayList<String> locations = (ArrayList<String>) document.get("types");
                            if (locations != null) {
                                userSelectedItems.addAll(locations);



                                checkMatching("Colombo", "Farm Equipment Purchase", locationList, userSelectedItems,Fcm);

                            }
                        }
                    }


                })
                .addOnFailureListener(e -> {
                    // Handle the error when retrieving user locations or types
                    Toast.makeText(this, "Error while retrieving user locations", Toast.LENGTH_SHORT).show();
                });




    }



    private void checkMatching(
            String inputLocation,
            String inputType,
            ArrayList<String> userSelectedLocations,
            ArrayList<String> userSelectedTypes,
            String Fcm

    ) {

        boolean isMatchingLocation = userSelectedLocations.contains(inputLocation) ;
        boolean isMatchingType = userSelectedTypes.contains(inputType);

        if (isMatchingType && isMatchingLocation){

           // Toast.makeText(this, Fcm, Toast.LENGTH_SHORT).show();

            userTokens.add(Fcm);

        }






    }



    private void sendNotificationToAllUsers( ) {


        for (String userToken : userTokens) {



            Toast.makeText(this, userToken, Toast.LENGTH_SHORT).show();




        }


    }






}