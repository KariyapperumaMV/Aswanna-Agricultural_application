package com.example.aswanna;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.databinding.ActivityFarmerHomePageBinding;
import com.example.aswanna.databinding.ActivityProfileViewBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


public class Profile_View extends AppCompatActivity {
    private ImageView profileImage;
    private PreferenceManager preferenceManager;
    private ActivityProfileViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();

       // preferenceManager = new PreferenceManager(getContext());

        String investorId=preferenceManager.getString(User.KEY_USER_ID);
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference proposalsRef = db.collection("FarmerRating");

        // Query the "proposals" collection based on filter criteria


        db = FirebaseFirestore.getInstance();

        // Replace 'your_user_id' with the actual user ID you want to retrieve
    preferenceManager.getString(User.KEY_USER_ID);

        // Retrieve the user document from Firestore



        String farmerID =     preferenceManager.getString(User.KEY_USER_ID);
        ;

        db.collection("FarmerRating")
                .whereEqualTo("farmerID", farmerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Retrieve ratings and other data
                                Long ratingValue = document.getLong("rating");
                                binding.countTextView.setText("Level "+ratingValue.toString());
                                // Process the ratings (e.g., display, compute averages, etc.)
                                Log.d(TAG, "Rating: " + ratingValue + ", Comments: ");
                            }
                        } else {
                            Log.d(TAG, "Error getting ratings: ", task.getException());
                        }
                    }
   });

    }
    private void loadUserDetails(){
        binding.fullNameTextView.setText(preferenceManager.getString(User.KEY_NAME));

        binding.emailTextView.setText(preferenceManager.getString(User.KEY_EMAIL));
       // binding.countTextView.setText(ratingValue);
        binding.phoneNoTextView.setText(preferenceManager.getString(User.KEY_PHONE_NO));
        binding.countTextView.setText(preferenceManager.getString(User.KEY_COUNT));
        byte[] bytes = Base64.decode(preferenceManager.getString(User.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.profileImage.setImageBitmap(bitmap);
    }






}