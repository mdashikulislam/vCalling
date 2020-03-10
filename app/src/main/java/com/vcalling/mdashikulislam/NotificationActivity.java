package com.vcalling.mdashikulislam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    public RecyclerView notificatinRecyler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        this.initilizeFeild();
        notificatinRecyler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
    public void initilizeFeild(){
        notificatinRecyler = findViewById(R.id.NotificationRecyleView);
    }
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
         Button btnSendRequest, btnCancleRequest;
         TextView txtUsername;
         ConstraintLayout cardView;
         ImageView profileImageView;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            btnSendRequest = itemView.findViewById(R.id.btnSendRequest);
            btnCancleRequest = itemView.findViewById(R.id.btnCancelRequest);
            profileImageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.constrainLayout);

        }
    }
}
