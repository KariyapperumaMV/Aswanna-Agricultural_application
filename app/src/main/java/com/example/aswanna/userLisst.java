package com.example.aswanna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.aswanna.Activities.ChatActivity;
import com.example.aswanna.Activities.ChatActivity_previousOne;
import com.example.aswanna.Adapters.UserAdapter;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.Model.UserRetrive;
import com.example.aswanna.databinding.ActivityUserLisstBinding;
import com.example.aswanna.listners.UserListner;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class userLisst extends AppCompatActivity implements UserListner {

    private ActivityUserLisstBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityUserLisstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager =new PreferenceManager(getApplicationContext());
        getUsers();
    }

    private void loading(Boolean isloading){
        if(isloading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s","No User Details"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    //    private void setListners(){
//        binding.
//    }
    private void getUsers(){
        loading(true);
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        database.collection(User.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false );
                    String currentUserId = preferenceManager.getString(User.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null){
                        List<UserRetrive> userRetrives = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            UserRetrive userRetrive =new  UserRetrive();
                            userRetrive.name=queryDocumentSnapshot.getString(User.KEY_NAME);
                            userRetrive.email=queryDocumentSnapshot.getString(User.KEY_EMAIL);
                            userRetrive.image=queryDocumentSnapshot.getString(User.KEY_IMAGE);
                            userRetrive.token=queryDocumentSnapshot.getString(User.KEY_FCM_TOKEN);
                            userRetrives.add(userRetrive);

                        }
                        if(userRetrives.size()>0){
                            UserAdapter userAdapter =new UserAdapter(userRetrives, this);
                            binding.usersRecyclerView.setAdapter(userAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        }else{
                            showErrorMessage();
                        }
                    }else{
                        showErrorMessage();
                    }
                });
    }

    @Override
    public void onUserClicked(UserRetrive userRetrive) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity_previousOne.class);
        intent.putExtra(User.KEY_USER,userRetrive);
        startActivity(intent);
        finish();
    }
}