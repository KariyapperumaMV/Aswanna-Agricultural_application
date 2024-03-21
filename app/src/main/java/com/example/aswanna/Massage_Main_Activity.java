package com.example.aswanna;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aswanna.Activities.BaseActivity;
import com.example.aswanna.Activities.ChatActivity;
import com.example.aswanna.Activities.ChatActivity_FarmerSide;
import com.example.aswanna.Adapters.RecentConversationAdapter;
import com.example.aswanna.Model.ChatMessage;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.Model.UserRetrive;
import com.example.aswanna.databinding.ActivityMassageMainBinding;
import com.example.aswanna.listners.ConversionListner;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class Massage_Main_Activity extends BaseActivity implements ConversionListner {

    private ActivityMassageMainBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private RecentConversationAdapter conversationAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMassageMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager =new PreferenceManager(getApplicationContext());
        init();
        loadUserDetails();
        listenConversations();

    }


    private void loadUserDetails(){
//        binding.textName.setText(preferenceManager.getString(User.KEY_NAME));
//        binding.textLevel.setText(preferenceManager.getString(User.KEY_LEVEL));
        byte[] bytes = Base64.decode(preferenceManager.getString(User.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//        binding.imageProf.setImageBitmap(bitmap);

    }

    private void init(){
        conversations =new ArrayList<>();
        conversationAdapter = new RecentConversationAdapter(conversations, this);
        binding.conversationRecyclerView.setAdapter(conversationAdapter);
        database= FirebaseFirestore.getInstance();
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    private void listenConversations(){
        database.collection(User.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(User.KEY_SENDER_ID, preferenceManager.getString(User.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(User.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(User.KEY_RECEIVER_id,preferenceManager.getString(User.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }


    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null){
            return;
        }if (value !=null){
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    String senderId = documentChange.getDocument().getString(User.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(User.KEY_RECEIVER_id);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId =senderId;
                    chatMessage.receiverId =receiverId;
                    if (preferenceManager.getString(User.KEY_USER_ID).equals(senderId)){
                        chatMessage.conversionImage = documentChange.getDocument().getString(User.KEY_RECEIVER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(User.KEY_RECEIVER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(User.KEY_RECEIVER_id);

                    }else {
                        chatMessage.conversionImage =documentChange.getDocument().getString(User.KEY_SENDER_IMAGE);
                        chatMessage.conversionName =documentChange.getDocument().getString(User.KEY_SENDER_NAME);
                        chatMessage.conversionId =documentChange.getDocument().getString(User.KEY_SENDER_ID);

                    }
                    chatMessage.message =documentChange.getDocument().getString(User.KEY_LAST_MESSAGE);
                    chatMessage.dateObject =documentChange.getDocument().getDate(User.KEY_TIMESTAMP);
                    conversations.add(chatMessage);



                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {

                    for (int i=0; i<conversations.size(); i++){
                        String senderId = documentChange.getDocument().getString(User.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(User.KEY_RECEIVER_id);
                        if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
                            conversations.get(i).message = documentChange.getDocument().getString(User.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(User.KEY_TIMESTAMP);
                            conversations.get(i).setUpdateMessageVisible(true);

                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationAdapter.notifyDataSetChanged();
            binding.conversationRecyclerView.smoothScrollToPosition(0);
            binding.conversationRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    public void onConversionClicked(UserRetrive userRetrive) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity_FarmerSide.class);
        intent.putExtra(User.KEY_USER,userRetrive);
        startActivity(intent);

    }
}