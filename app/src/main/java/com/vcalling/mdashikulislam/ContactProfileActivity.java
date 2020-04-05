package com.vcalling.mdashikulislam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ContactProfileActivity extends AppCompatActivity {
    private String receiveUserId,receiveUserName,receiveUserBio,receiveUserImage;
    private ImageView profileImage;
    private TextView txtUsername, txtBio;
    private Button btnAddFriend, btnCancleFriend;
    private FirebaseAuth auth;
    private String currentUserId;
    private String currentState = "new";

    private DatabaseReference friendRequestRef, contactsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend Request");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        initilizeField();
        receiveUserId = getIntent().getExtras().get("visit_user_id").toString();
        receiveUserName = getIntent().getExtras().get("visit_user_name").toString();
        receiveUserBio = getIntent().getExtras().get("visit_user_bio").toString();
        receiveUserImage = getIntent().getExtras().get("visit_user_image").toString();

        Picasso.get().load(receiveUserImage).into(profileImage);
        txtUsername.setText(receiveUserName);
        txtBio.setText(receiveUserBio);

        manageClickuser();
    }

    private void manageClickuser() {
        if (currentUserId.equals(receiveUserId)){
            btnAddFriend.setVisibility(View.GONE );
        }else{
            btnAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentState.equals("new")){
                        sendFriendRequest();
                    }if(currentState.equals("request_received")){

                    }if(currentState.equals("cancel_request")){

                    }
                }
            });
            btnCancleFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentState.equals("request_sent")){
                        cancelFriendRequest();
                    }
                }
            });
        }

    }

    private void cancelFriendRequest() {
        friendRequestRef.child(currentUserId).child(receiveUserId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            friendRequestRef.child(receiveUserId).child(currentUserId).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        currentState = "new";
                                        btnCancleFriend.setVisibility(View.GONE);
                                        btnAddFriend.setVisibility(View.VISIBLE);
                                        Toast.makeText(ContactProfileActivity.this, "Friend request cancel Successfully", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    }
                });
    }

    private void sendFriendRequest() {
        friendRequestRef.child(currentUserId).child(receiveUserId).child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            friendRequestRef.child(receiveUserId).child(currentUserId).child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                currentState = "request_sent";
                                                btnAddFriend.setVisibility(View.GONE);
                                                btnCancleFriend.setVisibility(View.VISIBLE);
                                                Toast.makeText(ContactProfileActivity.this, "Friend request sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void initilizeField() {
        profileImage = findViewById(R.id.profileImage);
        txtUsername = findViewById(R.id.txtUsername);
        txtBio = findViewById(R.id.txtDetails);
        btnAddFriend = findViewById(R.id.btnAddFriend);
        btnCancleFriend = findViewById(R.id.btnCancelFriend);

    }
}
