package com.invocker.chatbotfacebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.invocker.chatbotfacebook.Adapter.MessagerAdapter;
import com.invocker.chatbotfacebook.Model.Chats;
import com.invocker.chatbotfacebook.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessegerActivity extends AppCompatActivity {
    private TextView userName;
    private EditText edtSent;
    private ImageView imageUser, btnSent;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Intent intent;
    private Toolbar toolbar;
    private String userId;
    private MessagerAdapter MGSAdapter;
    private List<Chats> mchats;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messeger);
        addControl();
        setupControl();
    }

    private void setupControl() {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intent = getIntent();
        userId = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    imageUser.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MessegerActivity.this).load(user.getImageURL()).into(imageUser);
                }
                readMessenger(firebaseUser.getUid(),userId,user.getImageURL());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void readMessenger(String myId, String userID, String imageURL) {
        mchats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchats.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Chats chat=dataSnapshot1.getValue(Chats.class);
                    if(chat.getReceiver().equals(myId)&&chat.getSender().equals(userID)||chat.getReceiver().equals(userID)&&chat.getSender().equals((myId))){
                        mchats.add(chat);
                    }
                    MGSAdapter =new MessagerAdapter(MessegerActivity.this,mchats,imageURL);
                    recyclerView.setAdapter(MGSAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendMesssge(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("messenger", message);
        reference.child("chats").push().setValue(hashMap);
    }
    private void addControl() {
        recyclerView = findViewById(R.id.recy_messenger);

        edtSent = findViewById(R.id.edt_messe);
        btnSent = findViewById(R.id.btn_sent_messe);
        userName = findViewById(R.id.txt_messe_name);
        imageUser = findViewById(R.id.img_messe_user);
        toolbar = findViewById(R.id.tool_bar_messe);
    }

    public void sentMessager(View view) {
        String msg = edtSent.getText().toString();
        if (!msg.equals("")) {
            sendMesssge(firebaseUser.getUid(), userId, msg);
        } else {
            sendMesssge(firebaseUser.getUid(), userId, "I love you");
        }
        edtSent.setText("");
    }
}
