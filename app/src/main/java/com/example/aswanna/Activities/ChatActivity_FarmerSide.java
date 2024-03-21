package com.example.aswanna.Activities;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aswanna.Adapters.ChatAdapter_Asa;
import com.example.aswanna.Model.ChatMessage;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.Model.UserRetrive;
import com.example.aswanna.R;
import com.example.aswanna.SendNotificationPack.Data;
import com.example.aswanna.databinding.ActivityChatBinding;
import com.example.aswanna.network.ApiClient;
import com.example.aswanna.network.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Nonnull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity_FarmerSide extends BaseActivity {
    private ActivityChatBinding binding;
    private UserRetrive receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter_Asa chatAdapter_asa;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String farmerid;
    private String senderId;
    private String receiverId;
    private String conversionId = null;

    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListners();
        loadReceiverDetails();
        init();
        listenMessages();


//        preferenceManager =new PreferenceManager(getApplicationContext());
//        farmerid = getIntent().getStringExtra("farmerid");
//
//        listenMessages();

//        senderId = preferenceManager.getString(User.KEY_USER_ID);  // Get sender id from preference manager
//        receiverId = getIntent().getStringExtra("farmerid");
    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages= new ArrayList<>();
        chatAdapter_asa = new ChatAdapter_Asa(
                chatMessages,
                getBitmapFromEncodedString(receiverUser.image),
                preferenceManager.getString(User.KEY_USER_ID)

        );
        binding.chatRecycler.setAdapter(chatAdapter_asa);
        database = FirebaseFirestore.getInstance();
    }

    //store chat in to the database
    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(User.KEY_SENDER_ID, preferenceManager.getString(User.KEY_USER_ID));
        message.put(User.KEY_RECEIVER_id, receiverUser.id);
        message.put(User.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(User.KEY_TIMESTAMP, new Date());
        database.collection(User.KEY_COLLECTION_CHAT).add(message);
//        binding.inputMessage.setText(null);


        if (conversionId != null) {
            updateConversion(binding.inputMessage.getText().toString());
        } else {

            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(User.KEY_SENDER_ID, preferenceManager.getString(User.KEY_USER_ID));
            conversion.put(User.KEY_SENDER_NAME, preferenceManager.getString(User.KEY_NAME));
            conversion.put(User.KEY_SENDER_IMAGE, preferenceManager.getString(User.KEY_IMAGE));
            conversion.put(User.KEY_RECEIVER_id, receiverUser.id);
            conversion.put(User.KEY_RECEIVER_NAME, receiverUser.name);
            conversion.put(User.KEY_RECEIVER_IMAGE, receiverUser.image);
            conversion.put(User.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversion.put(User.KEY_TIMESTAMP, new Date());
            addConversion(conversion);
        }
        //notification part
        if (!isReceiverAvailable){
            try {
                JSONArray tokens = new JSONArray();
                tokens.put(receiverUser.token);

                JSONObject data = new JSONObject();
                data.put(User.KEY_USER_ID, preferenceManager.getString(User.KEY_USER_ID));
                data.put(User.KEY_NAME, preferenceManager.getString(User.KEY_NAME));
                data.put(User.KEY_FCM_TOKEN,preferenceManager.getString(User.KEY_FCM_TOKEN));
                data.put(User.KEY_MESSAGE, binding.inputMessage.getText().toString());

                JSONObject body =new JSONObject();
                body.put(User.REMOTE_MSG_DATA,data);
                body.put(User.REMOTE_MSG_REGISTRATION_IDS,tokens);

                sendNotification(body.toString());
            }catch (Exception exception){
                showToast(exception.getMessage());
            }
        }
        binding.inputMessage.setText(null);
    }

    //For notification.
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String messageBody){
        ApiClient.getClient().create(ApiService.class).sendMessage(
                User.getRemoteMsgHeader(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull  Response<String> response) {
                if(response.isSuccessful()){
                    try {
                        if(response.body() != null){
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1){
                                JSONObject error =(JSONObject) results.get(0);
                                showToast(error.getString("error"));
                                return;
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
//                    showToast("Notification Sent successfully");
                }else {
                    showToast("Error: "+ response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    private void listenAvailabilityOfReceiver(){
        database.collection(User.KEY_COLLECTION_USERS).document(
                receiverUser.id
        ).addSnapshotListener(ChatActivity_FarmerSide.this ,(value, error) -> {
            if (error!= null){
                return;
            }if(value != null){
                if(value.getLong(User.KEY_AVAILABILITY) != null){
                    int availability = Objects.requireNonNull(
                            value.getLong(User.KEY_AVAILABILITY)
                    ).intValue();
                    isReceiverAvailable=availability==1;
                }
                receiverUser.token = value.getString(User.KEY_FCM_TOKEN); // for notification
                if (receiverUser.image == null){
                    receiverUser.image = value.getString(User.KEY_IMAGE);
                    chatAdapter_asa.setReceiverProfileImage(getBitmapFromEncodedString(receiverUser.image));
                    chatAdapter_asa.notifyItemRangeInserted(0,chatMessages.size());
                }
            }
            if(isReceiverAvailable){
                binding.textAvailability.setVisibility(View.VISIBLE);
            }else {
                binding.textAvailability.setVisibility(View.GONE);
            }

        });
    }

    private void listenMessages(){
        database.collection(User.KEY_COLLECTION_CHAT)
                .whereEqualTo(User.KEY_SENDER_ID,preferenceManager.getString(User.KEY_USER_ID))
                .whereEqualTo(User.KEY_RECEIVER_id,receiverUser.id)
                .addSnapshotListener(eventListener);
        database.collection(User.KEY_COLLECTION_CHAT)
                .whereEqualTo(User.KEY_SENDER_ID,receiverUser.id)
                .whereEqualTo(User.KEY_RECEIVER_id, preferenceManager.getString(User.KEY_USER_ID))
                .addSnapshotListener(eventListener);

    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if (encodedImage != null){
            byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }else {
            return null;
        }

    }


    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if(value!=null){
            int count = chatMessages.size();
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId =documentChange.getDocument().getString(User.KEY_SENDER_ID);
                    chatMessage.receiverId=documentChange.getDocument().getString(User.KEY_RECEIVER_id);
                    chatMessage.message=documentChange.getDocument().getString(User.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(User.KEY_TIMESTAMP));
                    chatMessage.dateObject=documentChange.getDocument().getDate(User.KEY_TIMESTAMP);

                    chatMessages.add(chatMessage);
                }

            }
            Collections.sort(chatMessages, (obj1,obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0){
                chatAdapter_asa.notifyDataSetChanged();

            }else {
                chatAdapter_asa.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecycler.smoothScrollToPosition(chatMessages.size()-1);
            }
            binding.chatRecycler.setVisibility(View.VISIBLE);

        }
        binding.progressBar.setVisibility(View.GONE);
        if(conversionId==null){
            checkForConversation();
        }
    };





    //get unique user details from user document in Firestore database
    private void loadReceiverDetails() {
        receiverUser = (UserRetrive) getIntent().getSerializableExtra(User.KEY_USER);
        binding.textName.setText(receiverUser.name);
        binding.inputImage.setImageBitmap(getBitmapFromEncodedString(receiverUser.image));

    }

//                if (receiverUser != null) {
//
//                    binding.inputImage.setImageBitmap(getBitmapFromEncodedString(receiverUser.image));
//
//                    chatMessages= new ArrayList<>();
//                    chatAdapter_asa = new ChatAdapter_Asa(
//                            chatMessages,
//                            getBitmapFromEncodedString(receiverUser.image),
//                            senderId
////               preferenceManager.getString(User.KEY_USER_ID)
//                    );
//                    binding.chatRecycler.setAdapter(chatAdapter_asa);
//                    database = FirebaseFirestore.getInstance();
//
//
//
//                    database.collection(User.KEY_COLLECTION_CHAT)
//                            .whereEqualTo(User.KEY_SENDER_ID,senderId)
//                            .whereEqualTo(User.KEY_RECEIVER_id,farmerid)
//                            .addSnapshotListener(eventListener);
//                    database.collection(User.KEY_COLLECTION_CHAT)
//                            .whereEqualTo(User.KEY_SENDER_ID,farmerid)
//                            .whereEqualTo(User.KEY_RECEIVER_id, senderId)
//                            .addSnapshotListener(eventListener);
//
////after I added
//
//
//                }
//            } else {
//                // Handle the case where the document does not exist
//            }
//        }).addOnFailureListener(e -> {
//            // Handle any errors that occurred
//        });
//    }

    private void setListners(){
        binding.layoutSend.setOnClickListener(v->sendMessage());
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
    private void addConversion(HashMap<String, Object> conversion){
        database.collection(User.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId =documentReference.getId());
    }

    private void updateConversion(String message){
        DocumentReference documentReference =
                database.collection(User.KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(
                User.KEY_LAST_MESSAGE, message,
                User.KEY_TIMESTAMP, new Date()
        );
    }

    //error
    private void checkForConversation(){
        if(chatMessages.size() != 0 ){
            checkForConversionRemotely(
                    preferenceManager.getString(User.KEY_USER_ID),
                    receiverUser.id
            );
            checkForConversionRemotely(
                    receiverUser.id,
                    preferenceManager.getString(User.KEY_USER_ID)
            );

        }
    }

    private void checkForConversionRemotely(String senderId,String receiverId){
        database.collection(User.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(User.KEY_SENDER_ID,senderId)
                .whereEqualTo(User.KEY_RECEIVER_id,receiverId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);

    }

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId =documentSnapshot.getId();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }
}