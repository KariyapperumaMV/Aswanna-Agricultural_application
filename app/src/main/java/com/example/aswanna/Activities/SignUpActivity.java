package com.example.aswanna.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aswanna.Model.PreferenceManager;
import com.example.aswanna.Model.User;
import com.example.aswanna.R;
import com.example.aswanna.databinding.ActivitySignInBinding;
import com.example.aswanna.databinding.ActivitySignUpBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;
import android.util.Base64;


public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListner();

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.usertype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private void setListner(){
        binding.signupText.setOnClickListener(v->onBackPressed());
        binding.btnSignup.setOnClickListener(v->{
            if(isValidSignUpDetails()){
                signUp();
            }
        });
        binding.LayoutImage.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void signUp(){

        FirebaseFirestore database =FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(User.KEY_NAME,binding.edtFullName.getText().toString());
        user.put(User.KEY_USER_TYPE,binding.spinner.getSelectedItem().toString());
        user.put(User.KEY_EMAIL,binding.editEmail.getText().toString());
        user.put(User.KEY_PASSWORD,binding.editPassword.getText().toString());
        user.put(User.KEY_CONFIRM_PASSWORD,binding.editConfirm.getText().toString());
        user.put(User.KEY_PHONE_NO,binding.editPhoneNo.getText().toString());
        user.put(User.KEY_LEVEL,binding.level.getText().toString());
        user.put(User.KEY_COUNT,binding.count.getText().toString());
        user.put(User.KEY_IMAGE,encodedImage);
        database.collection(User.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(User.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(User.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(User.KEY_NAME,binding.edtFullName.getText().toString());
                    preferenceManager.putString(User.KEY_IMAGE,encodedImage);
                    preferenceManager.putString(User.KEY_LEVEL,binding.level.getText().toString());
                    Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                    Toast.makeText(getApplicationContext(), "Regitration Successfully", Toast.LENGTH_SHORT).show();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception ->{
                    showToast(exception.getMessage());
                });
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth =150;
        int previewHeight = bitmap.getHeight() * previewWidth/ bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes =byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null) {
                        Uri ImageUri =result.getData().getData();
                        try {
                            InputStream inputStream =getContentResolver().openInputStream(ImageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.ImageProfile.setImageBitmap(bitmap);
                            binding.txtAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        }catch (FileNotFoundException e ){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails(){
        if(encodedImage==null){
            showToast("Select Profile Iamage");
            return false;
        } else if (binding.edtFullName.getText().toString().trim().isEmpty()) {
            showToast("Enter Full Name");
            return false;
        } else if (binding.spinner.getSelectedItem().toString().equals("Choose Type")) {
            showToast("Select User Type");
            return false;
        } else if (binding.editEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Email Address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editEmail.getText().toString()).matches()) {
            showToast("Enter Valid Email");
            return false;
        } else if (binding.editPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else if (binding.editConfirm.getText().toString().trim().isEmpty()) {
            showToast("Confirm Your Password");
            return false;
        } else if (!binding.editPassword.getText().toString().equals(binding.editConfirm.getText().toString())) {
            showToast("Password and Confirm Password Must be Same");
            return false;
        } else if (binding.editPhoneNo.getText().toString().trim().isEmpty()) {
            showToast("Enter Phone Number");
            return false;
        } else if (binding.editPhoneNo.getText().toString().replaceAll("[^0-9]", "").length() != 10) {
            showToast("Phone Number must contain 10 digits");
            return false;
        }else {
            return true;
        }

    }

}