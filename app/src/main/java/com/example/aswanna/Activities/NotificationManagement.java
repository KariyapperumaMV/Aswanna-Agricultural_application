package com.example.aswanna.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NotificationManagement extends AppCompatActivity {

    private ChipGroup chipGroup,chipGroup2;
    AutoCompleteTextView autoCompleteTextView,autoCompleteTextView1;
    ArrayAdapter<String> adapterItems,adapterItems1;
    String[] sriLankanDistricts = {"Colombo", "Gampaha", "Kalutara", "Kandy", "Matale", "Nuwara Eliya", "Galle",
            "Matara", "Hambantota", "Jaffna", "Kilinochchi", "Mannar", "Vavuniya", "Mullaitivu", "Batticaloa", "Ampara",
            "Trincomalee", "Kurunegala", "Puttalam", "Anuradhapura", "Polonnaruwa", "Badulla", "Monaragala", "Ratnapura", "Kegalle"};

    String[] type = {"Crop Production", "Livestock Farming", "Organic Farming", "Aquaculture", "Agri-Tourism",
            "Farm Equipment Purchase", "Farm Expansion", "Agribusiness Plan", "Community Farming",
            "Sustainable Agriculture", "Rural Development", "Value-Added Agriculture",
            "Agricultural Research", "Food Security", "Grant Proposals"};
    Set<String> selectedDistricts = new HashSet<>(); // Use a Set to keep track of selected districts

    Set<String> selectedTypes = new HashSet<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button saveButton;

    ImageView backButton;

    RangeSlider slider;

    private TextView rangeTextView;

    float minValue,maxValue;

    String rangeText;

    int minIntValue,maxIntValue;

    SwitchCompat switchButton;

    private String currentUser;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_management);
        //currentUser="100001";

        preferenceManager = new PreferenceManager(this);

        currentUser= preferenceManager.getString(User.KEY_USER_ID);


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String fcmToken = task.getResult();

                        if (fcmToken != null) {
                            // Now you have the FCM token, you can store it in Firestore
                            storeFCMTokenInFirestore(currentUser, fcmToken);
                        }
                    }
                });


        Toast.makeText(NotificationManagement.this, currentUser, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate: "+currentUser);

        saveButton=findViewById(R.id.save);
        backButton=findViewById(R.id.backButton);

        chipGroup = findViewById(R.id.chip_group);
        chipGroup2=findViewById(R.id.chip_group1);
        slider = findViewById(R.id.slider);
        rangeTextView = findViewById(R.id.rangeTextView);
        slider.setThumbStrokeColorResource(R.color.green);

        switchButton = findViewById(R.id.switchButton);
        switchButton.setTextColor(getResources().getColor(R.color.green));
        boolean switchState = switchButton.isChecked();

        slider.setValues(0f,10000f);



        autoCompleteTextView = findViewById(R.id.Plocation);
        autoCompleteTextView1 = findViewById(R.id.auto_complete_txt1);

        adapterItems = new ArrayAdapter<>(this, R.layout.location_item, sriLankanDistricts);
        adapterItems1=new ArrayAdapter<>(this,R.layout.location_item,type);
        autoCompleteTextView1.setAdapter(adapterItems1);
        autoCompleteTextView.setAdapter(adapterItems);



         minValue = slider.getValues().get(0);
         maxValue = slider.getValues().get(1);

        minIntValue = (int) minValue;
        maxIntValue = (int) maxValue;

        // Update the TextView with the selected range values
         rangeText = "Min: Rs " + minIntValue + " - Max: Rs " + maxIntValue;
         rangeTextView.setText(rangeText);



        retrieveUserLocations("locations");
        retrieveUserLocations("types");
        retrieveUserRange();
        retrieveSwitchStateFromFirestore();


        slider.addOnChangeListener((slider, value, fromUser) -> {
            // Get the selected range values
             minValue = slider.getValues().get(0);
             maxValue = slider.getValues().get(1);

             minIntValue = (int) minValue;
             maxIntValue = (int) maxValue;

            // Update the TextView with the selected range values
            String rangeText = "Min: Rs " + minIntValue + " - Max: Rs " + maxIntValue;
            rangeTextView.setText(rangeText);
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedDistrict = adapterView.getItemAtPosition(i).toString();

                // Check if the district is not already selected before adding a chip
                if (!selectedDistricts.contains(selectedDistrict)) {
                    addChip(selectedDistrict);
                    selectedDistricts.add(selectedDistrict); // Add to the set to mark it as selected
                }else{

                    Toast.makeText(NotificationManagement.this, "district is  already selected", Toast.LENGTH_SHORT).show();

                }
            }
        });


        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedType = adapterView.getItemAtPosition(i).toString();

                // Check if the district is not already selected before adding a chip
                if (!selectedTypes.contains(selectedType)) {
                    addChip1(selectedType);
                    selectedTypes.add(selectedType); // Add to the set to mark it as selected
                }else{

                    Toast.makeText(NotificationManagement.this, "Type is  already selected", Toast.LENGTH_SHORT).show();

                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code to display a Toast message here.



                storeDataInFirestore(selectedTypes,"types");
                storeDataInFirestore(selectedDistricts,"locations");
                storeRangeInFirestore(minIntValue,maxIntValue);






            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationManagement.this, InvestorHome.class);
                startActivity(intent);
            }
        });

        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Store the isChecked value in Firestore
             // Replace with the actual user ID
            storeSwitchStateInFirestore(isChecked);
        });


    }

    private void addChip(String label) {
        Chip chip = new Chip(this);
        chip.setCloseIconTint(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.chip)));
        chip.setChipStrokeWidth(3.0F);
        chip.setText(label);
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String removedDistrict = chip.getText().toString();
                selectedDistricts.remove(removedDistrict); // Remove it from the set
                chipGroup.removeView(v);
            }
        });

        chipGroup.addView(chip);
    }


    private void addChip1(String label) {
        Chip chip = new Chip(this);
        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        chip.setCloseIconTint(ColorStateList.valueOf(getResources().getColor(R.color.red)));

        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.chip)));
        chip.setChipStrokeWidth(3.0F);
        chip.setText(label);
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedType = chip.getText().toString();
                selectedTypes.remove(selectedType); // Remove it from the set
                chipGroup2.removeView(v);
            }
        });

        chipGroup2.addView(chip);
    }


//    private void storeLocationInFirestore(Set<String> districts) {
//        String currentUser = "10001"; // Replace with the actual user ID
//
//        if (currentUser != null) {
//            CollectionReference userLocationsCollection = db.collection("InvestorPreferences").document(currentUser).collection("locations");
//
//            // Check if the user document already exists in Firestore
//            userLocationsCollection.limit(1) // Limit to one document
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        if (queryDocumentSnapshots.isEmpty()) {
//                            // User document doesn't exist, create a new one
//                            Map<String, Object> data = new HashMap<>();
//                            data.put("locations", new ArrayList<>(districts)); // Convert Set to List
//                            userLocationsCollection.add(data)
//                                    .addOnSuccessListener(documentReference -> {
//                                        // Document added successfully
//                                        Toast.makeText(NotificationManagement.this, "Location added to Firestore", Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Handle the error
//                                        Toast.makeText(NotificationManagement.this, "Failed to add location to Firestore", Toast.LENGTH_SHORT).show();
//                                    });
//                        } else {
//                            // User document already exists, update the location information
//                            DocumentSnapshot userDocument = queryDocumentSnapshots.getDocuments().get(0);
//                            userDocument.getReference().update("locations", new ArrayList<>(districts))
//                                    .addOnSuccessListener(aVoid -> {
//                                        // Document updated successfully
//                                        Toast.makeText(NotificationManagement.this, "Location updated in Firestore", Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Handle the error
//                                        Toast.makeText(NotificationManagement.this, "Failed to update location in Firestore", Toast.LENGTH_SHORT).show();
//                                    });
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle the error when checking if the user document exists
//                        Toast.makeText(NotificationManagement.this, "Error while checking user document", Toast.LENGTH_SHORT).show();
//                    });
//        }
//    }


    private void retrieveUserLocations(String fieldName) {


        if (currentUser != null) {
            CollectionReference userLocationsCollection = db.collection("InvestorPreferences").document(currentUser).collection(fieldName);

            userLocationsCollection
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            // Extract the locations data and set it to selectedDistricts
                            if (document.contains(fieldName)) {
                                ArrayList<String> locations = (ArrayList<String>) document.get(fieldName);
                                if (locations != null) {

                                    if(fieldName.equals("locations")){
                                        selectedDistricts.clear();
                                        selectedDistricts.addAll(locations);
                                        // Add chips for the retrieved locations
                                        for (String location : locations) {
                                            addChip(location);
                                        }

                                    } else if (fieldName.equals("types")) {

                                        selectedTypes.clear();
                                        selectedTypes.addAll(locations);

                                        for(String type:locations){

                                            addChip1(type);
                                        }

                                    }

                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error when retrieving user locations
                        Toast.makeText(NotificationManagement.this, "Error while retrieving user " +fieldName, Toast.LENGTH_SHORT).show();
                    });
        }

}


//    private void storeTypeInFirestore(Set<String> types) {
//        String currentUser = "10001"; // Replace with the actual user ID
//
//        if (currentUser != null) {
//            CollectionReference userTypesCollection = db.collection("InvestorPreferences").document(currentUser).collection("types");
//
//            // Check if the user document already exists in Firestore
//            userTypesCollection.limit(1) // Limit to one document
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        if (queryDocumentSnapshots.isEmpty()) {
//                            // User document doesn't exist, create a new one
//                            Map<String, Object> data = new HashMap<>();
//                            data.put("locations", new ArrayList<>(types)); // Convert Set to List
//                            userTypesCollection.add(data)
//                                    .addOnSuccessListener(documentReference -> {
//                                        // Document added successfully
//                                        Toast.makeText(NotificationManagement.this, "Types added to Firestore", Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Handle the error
//                                        Toast.makeText(NotificationManagement.this, "Failed to add Types to Firestore", Toast.LENGTH_SHORT).show();
//                                    });
//                        } else {
//                            // User document already exists, update the location information
//                            DocumentSnapshot userDocument = queryDocumentSnapshots.getDocuments().get(0);
//                            userDocument.getReference().update("types", new ArrayList<>(types))
//                                    .addOnSuccessListener(aVoid -> {
//                                        // Document updated successfully
//                                        Toast.makeText(NotificationManagement.this, "types updated in Firestore", Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Handle the error
//                                        Toast.makeText(NotificationManagement.this, "Failed to update types in Firestore", Toast.LENGTH_SHORT).show();
//                                    });
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle the error when checking if the user document exists
//                        Toast.makeText(NotificationManagement.this, "Error while checking user document", Toast.LENGTH_SHORT).show();
//                    });
//        }
//    }


    private void storeDataInFirestore(Set<String> data, String fieldName) {


        if (currentUser != null) {

            Toast.makeText(NotificationManagement.this, currentUser, Toast.LENGTH_SHORT).show();
            CollectionReference userCollection = db.collection("InvestorPreferences").document(currentUser).collection(fieldName);

            userCollection.limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Map<String, Object> dataMap = new HashMap<>();
                            dataMap.put(fieldName, new ArrayList<>(data));
                            userCollection.add(dataMap)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(NotificationManagement.this, fieldName + " added to Firestore", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(NotificationManagement.this, "Failed to add " + fieldName + " to Firestore", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            DocumentSnapshot userDocument = queryDocumentSnapshots.getDocuments().get(0);
                            userDocument.getReference().update(fieldName, new ArrayList<>(data))
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(NotificationManagement.this, fieldName + " updated in Firestore", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(NotificationManagement.this, "Failed to update " + fieldName + " in Firestore", Toast.LENGTH_LONG).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NotificationManagement.this, "Error while checking user document", Toast.LENGTH_LONG).show();
                    });
        }
    }


    private void storeRangeInFirestore(int minRange, int maxRange) {


        if (currentUser != null) {
            CollectionReference userRangeCollection = db.collection("InvestorPreferences")
                    .document(currentUser)
                    .collection("range");

            userRangeCollection.limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            // User range document doesn't exist, create a new one
                            Map<String, Object> dataMap = new HashMap<>();
                            dataMap.put("minRange", minRange);
                            dataMap.put("maxRange", maxRange);

                            userRangeCollection.add(dataMap)
                                    .addOnSuccessListener(documentReference -> {
                                        // Document added successfully
                                        Toast.makeText(NotificationManagement.this, "Slider range added to Firestore", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the error
                                        Toast.makeText(NotificationManagement.this, "Failed to add slider range to Firestore", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // User range document exists, update the range values
                            DocumentSnapshot userRangeDocument = queryDocumentSnapshots.getDocuments().get(0);
                            userRangeDocument.getReference().update("minRange", minRange, "maxRange", maxRange)
                                    .addOnSuccessListener(aVoid -> {
                                        // Document updated successfully
                                        Toast.makeText(NotificationManagement.this, "Slider range updated in Firestore", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the error
                                        Toast.makeText(NotificationManagement.this, "Failed to update slider range in Firestore", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error when checking if the user range document exists
                        Toast.makeText(NotificationManagement.this, "Error while checking user range document", Toast.LENGTH_SHORT).show();
                    });
        }
    }



    private void retrieveUserRange() {


        if (currentUser != null) {
            CollectionReference userRangeCollection = db.collection("InvestorPreferences")
                    .document(currentUser)
                    .collection("range");

            userRangeCollection
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // User range document exists, retrieve the range values
                            DocumentSnapshot userRangeDocument = queryDocumentSnapshots.getDocuments().get(0);
                            if (userRangeDocument.contains("minRange") && userRangeDocument.contains("maxRange")) {
                                int minRange = userRangeDocument.getLong("minRange").intValue();
                                int maxRange = userRangeDocument.getLong("maxRange").intValue();

                                // Set the retrieved range values to the RangeSlider
                                slider.setValues((float) minRange, (float) maxRange);

                                // Update the TextView with the selected range values
                                rangeText = "Min: Rs " + minRange + " - Max: Rs " + maxRange;
                                rangeTextView.setText(rangeText);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error when retrieving user range
                        Toast.makeText(NotificationManagement.this, "Error while retrieving user range", Toast.LENGTH_SHORT).show();
                    });
        }
    }


    private void storeSwitchStateInFirestore(boolean switchState) {


        if (currentUser != null) {
            CollectionReference userSwitchCollection = db.collection("InvestorPreferences")
                    .document(currentUser)
                    .collection("switchState");

            userSwitchCollection.limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            // User switch state document doesn't exist, create a new one
                            Map<String, Object> dataMap = new HashMap<>();
                            dataMap.put("switchState", switchState);

                            userSwitchCollection.add(dataMap)
                                    .addOnSuccessListener(documentReference -> {
                                        // Document added successfully
                                        Toast.makeText(NotificationManagement.this, "Switch state added to Firestore", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the error
                                        Toast.makeText(NotificationManagement.this, "Failed to add switch state to Firestore", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // User switch state document exists, update the switch state
                            DocumentSnapshot userSwitchDocument = queryDocumentSnapshots.getDocuments().get(0);
                            userSwitchDocument.getReference().update("switchState", switchState)
                                    .addOnSuccessListener(aVoid -> {
                                        // Document updated successfully
                                        Toast.makeText(NotificationManagement.this, "Switch state updated in Firestore", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the error
                                        Toast.makeText(NotificationManagement.this, "Failed to update switch state in Firestore", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error when checking if the user switch state document exists
                        Toast.makeText(NotificationManagement.this, "Error while checking user switch state document", Toast.LENGTH_SHORT).show();
                    });
        }
    }




    private void retrieveSwitchStateFromFirestore() {

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

                            if (switchState != null) {
                                switchButton.setChecked(switchState);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error when retrieving the user switch state document
                        Toast.makeText(NotificationManagement.this, "Error while retrieving user switch state", Toast.LENGTH_SHORT).show();
                    });
        }















}






    private void storeFCMTokenInFirestore(String userId, String fcmToken) {
        if (userId != null && fcmToken != null) {
            // Reference to the user's document in Firestore
            DocumentReference userDocument = db.collection("InvestorPreferences").document(userId);

            // Create a map with the FCM token
            Map<String, Object> data = new HashMap<>();
            data.put("fcmToken", fcmToken);

            // Use set with merge options to update or create the document
            userDocument.set(data, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(NotificationManagement.this, "FCM token added/updated in Firestore", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NotificationManagement.this, "Failed to add/update FCM token in Firestore", Toast.LENGTH_SHORT).show();
                    });
        }
    }

















}


