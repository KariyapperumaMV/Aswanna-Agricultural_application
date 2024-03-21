package com.example.aswanna.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.aswanna.Farmer_Home_Page;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.Profile_View;
import com.example.aswanna.databinding.ActivitySignInBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.grpc.internal.Framer;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager= new PreferenceManager(getApplicationContext());
//        if(preferenceManager.getBoolean(User.KEY_IS_SIGNED_IN)){
//                if(preferenceManager.getString(User.KEY_USER_TYPE).equals("Farmer")){
//                    Intent intent = new Intent(getApplicationContext(), Farmer_Home_Page.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                }else if(preferenceManager.getString(User.KEY_USER_TYPE).equals("Investor")){
//                    Intent intent = new Intent(getApplicationContext(), InvestorHome.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                }
//        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListners();

    }
    private void setListners(){
        binding.signUptext.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class)));
        binding.signinBtn.setOnClickListener(v->{
            if(isValidSignInDetails()){
                signIn();
            }
        });
    }

    private void signIn(){
        loading(true);
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        database.collection(User.KEY_COLLECTION_USERS)
                .whereEqualTo(User.KEY_EMAIL,binding.inputEmail.getText().toString())
                .whereEqualTo(User.KEY_PASSWORD,binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() !=null
                    && task.getResult().getDocuments().size()>0){
                        DocumentSnapshot documentSnapshot =task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(User.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(User.KEY_USER_ID,documentSnapshot.getId());
                        preferenceManager.putString(User.KEY_NAME,documentSnapshot.getString(User.KEY_NAME));
                        preferenceManager.putString(User.KEY_IMAGE,documentSnapshot.getString(User.KEY_IMAGE));
                        preferenceManager.putString(User.KEY_USER_TYPE,documentSnapshot.getString(User.KEY_USER_TYPE));
                        preferenceManager.putString(User.KEY_LEVEL,documentSnapshot.getString(User.KEY_LEVEL));
                        preferenceManager.putString(User.KEY_EMAIL,documentSnapshot.getString(User.KEY_EMAIL));
                        preferenceManager.putString(User.KEY_COUNT,documentSnapshot.getString(User.KEY_COUNT));
                        preferenceManager.putString(User.KEY_PHONE_NO,documentSnapshot.getString(User.KEY_PHONE_NO));

                        showToast("Have a Nice Day " + preferenceManager.getString(User.KEY_NAME));
                        if(preferenceManager.getString(User.KEY_USER_TYPE).equals("Farmer")){
                            Intent intent = new Intent(getApplicationContext(), Farmer_Home_Page.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else if(preferenceManager.getString(User.KEY_USER_TYPE).equals("Investor")){
                            Intent intent = new Intent(getApplicationContext(), InvestorHome.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }else {
                        loading(false);
                        showToast("Unable to Sign In.Please TryAgain");
                    }
                });
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.signinBtn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.signinBtn.setVisibility(View.VISIBLE);
        }

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails(){
        if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Please Enter Email Address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter Valid Email Address");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Please Enter Your Password");
            return false;
        }else {
            return true;
        }
    }


}