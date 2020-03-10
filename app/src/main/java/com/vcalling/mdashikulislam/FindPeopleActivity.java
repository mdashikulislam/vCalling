package com.vcalling.mdashikulislam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindPeopleActivity extends AppCompatActivity {
    private RecyclerView searchRecylerView;
    private EditText etSearchPeople;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);
        Utils.statusBarColor(this,R.color.orange);
        this.initlizeFeild();


    }

    private void initlizeFeild(){
        searchRecylerView = findViewById(R.id.search_recyler_view);
        etSearchPeople = findViewById(R.id.et_searchPeople);
    }
    public static class FindFriendViewHolder extends RecyclerView.ViewHolder {
        Button btnVideoCall;
        TextView txtUsername, txtDeatils;
        CircleImageView profileImage;
        ConstraintLayout cardView;
        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtFrName);
            txtDeatils = itemView.findViewById(R.id.txtFrDeatils);
            btnVideoCall = itemView.findViewById(R.id.imgVideoCall);
            profileImage = itemView.findViewById(R.id.profile_image);
            cardView = itemView.findViewById(R.id.constrainLayout);

        }
    }

}
