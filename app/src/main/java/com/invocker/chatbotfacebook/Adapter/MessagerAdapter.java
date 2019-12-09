package com.invocker.chatbotfacebook.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.invocker.chatbotfacebook.Model.Chats;
import com.invocker.chatbotfacebook.R;

import java.util.List;

public class MessagerAdapter extends RecyclerView.Adapter <MessagerAdapter.UserViewHolder>{
    public static final int MSG_LEFT=0;
    public static final int MSG_RIGHT=1;
    private FirebaseUser firebaseUser;
    private Context mcontext;
    private List<Chats> mchats;
    private String imgURL;

    public MessagerAdapter(Context mcontext, List<Chats> musers,String imgURL) {
        this.mcontext = mcontext;
        this.mchats = musers;
        this.imgURL=imgURL;
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mchats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_RIGHT;
        }else return MSG_LEFT;
    }

    @NonNull
    @Override
    public MessagerAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_RIGHT){
            View view= LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right,parent,false);
            return new MessagerAdapter.UserViewHolder(view);
        }else {
            View view= LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left,parent,false);
            return new MessagerAdapter.UserViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull MessagerAdapter.UserViewHolder holder, int position) {
                Chats chat=mchats.get(position);
                holder.showMessenger.setText(chat.getMessenger());
        if(imgURL.equals("default")){
            holder.imgUser.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mcontext).load(imgURL).into(holder.imgUser);
        }
    }

    @Override
    public int getItemCount() {
        return mchats.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView showMessenger;
        public ImageView imgUser;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessenger=itemView.findViewById(R.id.show_messenger);
            imgUser=itemView.findViewById(R.id.img_item_user);
        }
    }
}
