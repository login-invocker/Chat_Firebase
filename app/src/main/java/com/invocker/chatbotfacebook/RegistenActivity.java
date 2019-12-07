package com.invocker.chatbotfacebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class RegistenActivity extends AppCompatActivity {
    @BindView(R.id.txt_reg_email)
    TextView txtemail;
    @BindView(R.id.txt_reg_password)
    TextView txtPass;
    @BindView(R.id.txt_reg_username)
    TextView txtUserName;
    @BindView(R.id.btn_register)
    Button btnRegi;
    @BindView(R.id.my_tool_bar_re)
    Toolbar mybar;
    // auth : xac thuc quyen .Bao mat an toan
    private FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registen);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        getControl();

        getEvent();


    }

    private void getControl() {
        setSupportActionBar(mybar);
        getSupportActionBar().setTitle("register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getEvent() {
        btnRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtemail.getText().toString();
                String pass = txtPass.getText().toString();
                String userName = txtUserName.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegistenActivity.this, "input your full infomation", Toast.LENGTH_LONG).show();
                } else if (txtPass.length() <= 6) {
                    Toast.makeText(RegistenActivity.this, "password must be at 6 char", Toast.LENGTH_SHORT).show();
                } else {
                    register(userName, pass, email);
                }
            }
        });
    }

    private void register(final String userName, String pass, String email) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", userName);
                    hashMap.put("imageURL", "default");
                    Toast.makeText(RegistenActivity.this, userName + "\nid: " + userid + "\npass:" + pass, Toast.LENGTH_SHORT).show();
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegistenActivity.this, StartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else
                    Toast.makeText(RegistenActivity.this, "Your Can register woth this email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
