package com.example.aswanna.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aswanna.Model.ChatMessage;
import com.example.aswanna.Model.User;
import com.example.aswanna.Model.UserRetrive;
import com.example.aswanna.databinding.ActivityChatBinding;

public class ChatActivity_previousOne extends AppCompatActivity {

    private ActivityChatBinding binding;
    private UserRetrive receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReceiverDetails();
    }

    //specific user image retrieve
    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    private void loadReceiverDetails(){
        receiverUser = (UserRetrive) getIntent().getSerializableExtra(User.KEY_USER);
        binding.textName.setText(receiverUser.name);
        binding.inputImage.setImageBitmap(getBitmapFromEncodedString(receiverUser.image));


    }


}