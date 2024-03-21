package com.example.aswanna.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aswanna.Model.ChatMessage;
import com.example.aswanna.Model.UserRetrive;
import com.example.aswanna.databinding.ItemContainerRecentConversionBinding;
import com.example.aswanna.listners.ConversionListner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversionViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final ConversionListner conversionListner;

    public RecentConversationAdapter(List<ChatMessage> chatMessages, ConversionListner conversionListner) {
        this.chatMessages = chatMessages;
        this.conversionListner=conversionListner;

    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationAdapter.ConversionViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder {

        ItemContainerRecentConversionBinding binding;

        ConversionViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding){
            super(itemContainerRecentConversionBinding.getRoot());
            binding =itemContainerRecentConversionBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.imageProf.setImageBitmap(getConversionImage(chatMessage.conversionImage));
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);
            String formattedTimestamp = formatTimestamp(chatMessage.dateObject);
            binding.lastTime.setText(formattedTimestamp);


            if (chatMessage.isUpdateMessageVisible()) {
                binding.updateMessage.setVisibility(View.VISIBLE);
            } else {
                binding.updateMessage.setVisibility(View.GONE);
            }


            binding.getRoot().setOnClickListener(v -> {
                UserRetrive userRetrive = new UserRetrive();
                userRetrive.id =chatMessage.conversionId;
                userRetrive.name = chatMessage.conversionName;
                userRetrive.image = chatMessage.conversionImage;
                chatMessage.setUpdateMessageVisible(false);
                notifyDataSetChanged();
                conversionListner.onConversionClicked(userRetrive);
            });
        }

    }
    private Bitmap getConversionImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
    private String formatTimestamp(Date timestamp) {
        // Implement your timestamp formatting logic here.
        // You can use SimpleDateFormat or other date/time formatting libraries.
        // For example:
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(timestamp);
    }
}
