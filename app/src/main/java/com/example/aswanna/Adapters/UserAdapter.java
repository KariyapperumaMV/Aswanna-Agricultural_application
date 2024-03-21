package com.example.aswanna.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aswanna.Model.UserRetrive;
import com.example.aswanna.databinding.ItemContainerUserBinding;
import com.example.aswanna.listners.UserListner;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<UserRetrive> userRetrive;
    private final UserListner userListner;

    public UserAdapter(List<UserRetrive> userRetrive, UserListner userListner) {

        this.userRetrive = userRetrive;
        this.userListner=userListner;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(userRetrive.get(position));
    }

    @Override
    public int getItemCount() {
        return userRetrive.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        ItemContainerUserBinding binding;
        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }
        void setUserData(UserRetrive userRetrive){
            binding.textName.setText(userRetrive.name);
            binding.textEmail.setText(userRetrive.email);
            binding.imageProf.setImageBitmap(getUserImage(userRetrive.image));
            binding.getRoot().setOnClickListener(v->userListner.onUserClicked(userRetrive));
        }
    }
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
