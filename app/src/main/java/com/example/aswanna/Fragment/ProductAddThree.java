package com.example.aswanna.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aswanna.Activities.ProposalAdd;
import com.example.aswanna.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProductAddThree extends Fragment {
    private static final int PICK_IMAGE_REQUEST=1;
    private ImageView selectedImageOne,selectedImageTwo;

    private Uri imageUri2,imageUri1;
    private StorageReference storageReference;

    private String data1,data2,data3,data4,data5,data6,data7,downloadUrl1,downloadUrl2;


    Button Click;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageReference = FirebaseStorage.getInstance().getReference();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_product_add_three, container, false);

        ProposalAdd proposalAdd=(ProposalAdd) getActivity();

        ImageView imageView = proposalAdd.findViewById(R.id.three);
        Drawable drawable1 = ContextCompat.getDrawable(requireContext(), R.drawable.numthreew);
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.correct);

        imageView.setImageDrawable(drawable1);

        Button btn=proposalAdd.findViewById(R.id.nextButton);

        selectedImageOne=view.findViewById(R.id.imageone);

        selectedImageTwo=view.findViewById(R.id.imagetwo);

        selectedImageOne.setOnClickListener(v->openImageGallery(1));


        selectedImageTwo.setOnClickListener(v->openImageGallery(2));

        btn.setText("Next");


        Bundle args = getArguments();
        if (args != null) {
            data1 = args.getString("pName");
            data2=args.getString("location");
            data3=args.getString("type");
            data4=args.getString("time");
            data5=args.getString("description");
            data6=args.getString("expected");
            data7=args.getString("fund");









        }



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code to display a Toast message here.


                if (downloadUrl1==null) {
                    Toast.makeText(requireContext(), "please upload images before go to the next", Toast.LENGTH_SHORT).show();
                    return; // Prevent navigation
                }

                if (downloadUrl2==null) {
                    Toast.makeText(requireContext(), "please upload images before go to the next", Toast.LENGTH_SHORT).show();
                    return; // Prevent navigation
                }




                imageView.setImageDrawable(drawable);
                ProductAddFour productAddFour = new ProductAddFour();


                Bundle bundle = new Bundle();
                bundle.putString("pName", data1);
                bundle.putString("location", data2);
                bundle.putString("type", data3);
                bundle.putString("time", data4);
                bundle.putString("description", data5);
                bundle.putString("expected", data6);
                bundle.putString("fund", data7);
                bundle.putString("imgUrlOne", downloadUrl1);
                bundle.putString("imgUrlTwo", downloadUrl2);


                productAddFour.setArguments(bundle);


                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPager,productAddFour,null).addToBackStack(null).commit();




            }
        });






        return view;







    }

    private void openImageGallery(int requestCode) {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == 1) {
                imageUri1 = data.getData();
                selectedImageOne.setImageURI(imageUri1);
            } else if (requestCode == 2) {
                imageUri2 = data.getData();
                selectedImageTwo.setImageURI(imageUri2);
            }

            // Upload the images to Firebase Storage
            uploadImagesToStorage();
        }
    }

    private void uploadImagesToStorage() {
        // Upload the first image
        if (imageUri1 != null) {
            StorageReference imageRef1 = storageReference.child("images/" + System.currentTimeMillis() + "_1");
            UploadTask uploadTask1 = imageRef1.putFile(imageUri1);

            uploadTask1.addOnFailureListener(e -> {
                // Handle the failure
                Toast.makeText(requireContext(), "Image 1 upload failed", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                // Image 1 uploaded successfully, get the download URL
                imageRef1.getDownloadUrl().addOnSuccessListener(uri -> {
                     downloadUrl1 = uri.toString();

                    // Save the download URL to Firestore

                });
            });
        }

        // Upload the second image
        if (imageUri2 != null) {
            StorageReference imageRef2 = storageReference.child("images/" + System.currentTimeMillis() + "_2");
            UploadTask uploadTask2 = imageRef2.putFile(imageUri2);

            uploadTask2.addOnFailureListener(e -> {
                // Handle the failure
                Toast.makeText(requireContext(), "Image 2 upload failed", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                // Image 2 uploaded successfully, get the download URL
                imageRef2.getDownloadUrl().addOnSuccessListener(uri -> {
                     downloadUrl2 = uri.toString();

                    // Save the download URL to Firestore

                });
            });
        }
    }






}