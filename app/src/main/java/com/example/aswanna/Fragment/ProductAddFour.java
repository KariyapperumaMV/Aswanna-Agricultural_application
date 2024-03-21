package com.example.aswanna.Fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aswanna.Activities.NotificationManagement;
import com.example.aswanna.Activities.ProposalAdd;
import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.Proposal;
import com.example.aswanna.Model.User;
import com.example.aswanna.R;
import com.example.aswanna.SendNotificationPack.APIService;
import com.example.aswanna.SendNotificationPack.Client;
import com.example.aswanna.SendNotificationPack.Data;
import com.example.aswanna.SendNotificationPack.MyResponse;
import com.example.aswanna.SendNotificationPack.NotificationSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductAddFour extends Fragment {

    private StorageReference storageReference;

    private ImageView img;

    private TextView proposalId,projectName;

    private String data1,data2,data3,data4,data5,data6,data7,downloadUrl1,downloadUrl2,PID;
    private int funding;

    private APIService apiService;

    private PreferenceManager preferenceManager;

    private List<String> userTokens = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        View view=inflater.inflate(R.layout.fragment_product_add_four, container, false);

        preferenceManager = new PreferenceManager(getContext());


        apiService=  Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        ProposalAdd proposalAdd=(ProposalAdd) getActivity();

        ImageView imageView = proposalAdd.findViewById(R.id.four);
        Drawable drawable1 = ContextCompat.getDrawable(requireContext(), R.drawable.numfourw);
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.correct);

        imageView.setImageDrawable(drawable1);


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference proposalsCollection = firestore.collection("proposals");
        img=view.findViewById(R.id.proposalImage);
        proposalId=view.findViewById(R.id.propsalid);
        projectName=view.findViewById(R.id.productname);

        Random random = new Random();


        int lastTwoDigits = random.nextInt(100);

        // Combine with the first two digits "00"
        String PID = String.format("00%02d", lastTwoDigits);

        Bundle args = getArguments();

        if (args != null) {
            data1 = args.getString("pName");
            data2=args.getString("location");
            data3=args.getString("type");
            data4=args.getString("time");
            data5=args.getString("description");
            data6=args.getString("expected");
            data7=args.getString("fund");
            funding=Integer.parseInt(data7);
            downloadUrl1=args.getString("imgUrlOne");
            downloadUrl2=args.getString("imgUrlTwo");

        }


          projectName.setText(data1);
          proposalId.setText("Proposal ID-"+PID);


         // Change the path to your image




        Glide.with(this).load(downloadUrl1).into(img);






        Button btn=proposalAdd.findViewById(R.id.nextButton);

        btn.setText("Pay & Submit");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code to display a Toast message here.







                sendNotificationToAllUsers(userTokens);

                String status="on";
                String documentId = proposalsCollection.document().getId();

                Date currentDate = new Date();

                // Define the desired date format
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                // Format the date as a string
                String postedDate = sdf.format(currentDate);




                Proposal proposal=new Proposal("","","",preferenceManager.getString(User.KEY_LEVEL),preferenceManager.getString(User.KEY_IMAGE),preferenceManager.getString(User.KEY_NAME),PID,documentId,preferenceManager.getString(User.KEY_USER_ID),data1,data3,data2,data4,data5,funding,data6,downloadUrl1,downloadUrl2,status,postedDate);


                proposalsCollection.document(documentId).set(proposal)
                        .addOnSuccessListener(aVoid -> {


                            imageView.setImageDrawable(drawable);
                            ProductAddFive productAddFive = new ProductAddFive();

                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPager,productAddFive,null).addToBackStack(null).commit();


                        })
                        .addOnFailureListener(e -> {

                            Toast.makeText(requireContext(), "DataBase error", Toast.LENGTH_SHORT).show();


                        });







                // Start listening for new proposals










            }
        });







        checkAllMatchingUsers();









        return view;
    }



    private void sendNotificationToAllUsers(List<String> userTokens) {


        for (String userToken : userTokens) {
            Toast.makeText(getContext(), userToken, Toast.LENGTH_SHORT).show();
            sendNotifications(userToken,data1,data3);
        }
    }


   public void sendNotifications(String userToken,String proposalName,String proposalType){
       Data data=new Data(proposalName,proposalType,downloadUrl1,data2,data7);



       NotificationSender sender=new NotificationSender(data,userToken);




       apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
           @Override
           public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

               if(response.code()==200){

                   if(response.body().success !=1){

                       Toast.makeText(requireContext(),"FAILED",Toast.LENGTH_LONG).show();


                   }

               }

           }

           @Override
           public void onFailure(Call<MyResponse> call, Throwable t) {



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
                    Toast.makeText(getContext(), "Error while checking matching preferences", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Error while retrieving user switch state", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Error while retrieving user locations", Toast.LENGTH_SHORT).show();
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


                                retrieveUserRange(userId,locationList,userSelectedItems,Fcm);


                            }
                        }
                    }


                })
                .addOnFailureListener(e -> {
                    // Handle the error when retrieving user locations or types
                    Toast.makeText(getContext(), "Error while retrieving user locations", Toast.LENGTH_SHORT).show();
                });




    }



    private void checkMatching(
            String inputLocation,
            String inputType,
            int fund,
            ArrayList<String> userSelectedLocations,
            ArrayList<String> userSelectedTypes,
            int minRange,
            int maxRange,
            String Fcm

    ) {

        boolean isMatchingLocation = userSelectedLocations.contains(inputLocation);
        boolean isMatchingType = userSelectedTypes.contains(inputType);
        boolean isMatchingFund = (minRange<=fund) && (maxRange>=fund);

        Log.d(TAG, "checkMatching: "+minRange);
        Log.d(TAG, "checkMatching: "+maxRange);
        Log.d(TAG, "checkMatching: "+fund);
        if (isMatchingType && isMatchingLocation && isMatchingFund) {

            // Toast.makeText(this, Fcm, Toast.LENGTH_SHORT).show();

            userTokens.add(Fcm);

        }

    }
        private void retrieveUserRange(String userId, ArrayList<String> locationList, ArrayList<String> userSelectedTypes,String Fcm ) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            if (userId != null) {
                CollectionReference userRangeCollection = db.collection("InvestorPreferences")
                        .document(userId)
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

                                    checkMatching(data2,data3,funding,locationList,userSelectedTypes,minRange,maxRange,Fcm);


                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle the error when retrieving user range
                            Toast.makeText(getContext(), "Error while retrieving user range", Toast.LENGTH_SHORT).show();
                        });
            }
        }






    }

















    // Call this function to check for matching preferences with switch state as true












