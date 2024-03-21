package com.example.aswanna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.example.aswanna.Activities.FarmerProposalList;
import com.example.aswanna.Activities.Farmer_Request_View;
import com.example.aswanna.Activities.ProposalAdd;
import com.example.aswanna.Activities.SignInActivity;
import com.example.aswanna.Activities.SignUpActivity;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.databinding.ActivityFarmerHomePageBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Objects;

public class Farmer_Home_Page extends AppCompatActivity {

    private ActivityFarmerHomePageBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFarmerHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        getToken();
        setListners();
    }

    private void setListners(){
        binding.imageSignOut.setOnClickListener(v->signOut());
        binding.imageProfile.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(), Profile_View.class)));
        binding.viewReqBtn.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(), Farmer_Request_View.class)));
        binding.proposalIC.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(), FarmerProposalList.class)));
        //add proposal
        binding.addIC.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(), ProposalAdd.class)));
        binding.addText.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(), ProposalAdd.class)));
        //chat
        binding.ChatIC.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(), Massage_Main_Activity.class)));
        binding.chatText.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(), Massage_Main_Activity.class)));

    }

    private void loadUserDetails(){
        binding.textName.setText(preferenceManager.getString(User.KEY_NAME));
        binding.textLevel.setText(preferenceManager.getString(User.KEY_LEVEL));
        byte[] bytes = Base64.decode(preferenceManager.getString(User.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        preferenceManager.putString(User.KEY_FCM_TOKEN,token); // for notification
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(User.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(User.KEY_USER_ID)
                );
        documentReference.update(User.KEY_FCM_TOKEN,token)
//                .addOnSuccessListener(unused -> showToast("Token updated successfully"))
                .addOnFailureListener(e -> showToast("Unable to Update Token"));
    }

    private void signOut(){
        showToast("Signing out...");
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        DocumentReference documentReference=
                database.collection(User.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(User.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(User.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to SignOut"));


    }

}