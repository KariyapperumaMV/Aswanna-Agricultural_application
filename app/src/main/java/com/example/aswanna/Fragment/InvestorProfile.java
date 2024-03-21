package com.example.aswanna.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aswanna.Activities.NotificationManagement;
import com.example.aswanna.Activities.SignInActivity;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class InvestorProfile extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private PreferenceManager preferenceManager;

    Button manageN,logout;

    ImageView proPic;

    TextView name;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_investor_profile, container, false);


        manageN=view.findViewById(R.id.managenotification);
        logout=view.findViewById(R.id.logout);
        proPic=view.findViewById(R.id.propic);
        name=view.findViewById(R.id.profileName);

        preferenceManager = new PreferenceManager(getContext());

        name.setText(preferenceManager.getString(User.KEY_NAME));

        byte[] bytes = Base64.decode(preferenceManager.getString(User.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        proPic.setImageBitmap(bitmap);


        manageN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code to display a Toast message here.



                Intent intent = new Intent(getContext(), NotificationManagement.class);
                startActivity(intent);








            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code to display a Toast message here.



              signOut();








            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    private void signOut() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(User.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(User.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(User.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(requireContext(), SignInActivity.class));

                });

    }
}