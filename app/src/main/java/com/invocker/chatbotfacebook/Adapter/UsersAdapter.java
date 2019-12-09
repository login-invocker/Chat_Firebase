package com.invocker.chatbotfacebook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.invocker.chatbotfacebook.R;
import com.invocker.chatbotfacebook.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter <UsersAdapter.UserViewHolder>{
    private Context mcontext;
    private List<User> musers;

    public UsersAdapter(Context mcontext, List<User> musers) {
        this.mcontext = mcontext;
        this.musers = musers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(mcontext).inflate(R.layout.item_user,parent,false);
        return new UsersAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user=musers.get(position);
            holder.txtUser.setText(user.getUsername());
            if(user.getImageURL().equals("default")){
                holder.imgUser.setImageResource(R.mipmap.ic_launcher);
            }else {
                Glide.with(mcontext).load(user.getImageURL()).into(holder.imgUser);
            }
    }

    @Override
    public int getItemCount() {
        return musers.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
                private TextView txtUser;
                private ImageView imgUser;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUser=itemView.findViewById(R.id.txt_item_user);
            imgUser=itemView.findViewById(R.id.img_item_user);
        }
    }
}
