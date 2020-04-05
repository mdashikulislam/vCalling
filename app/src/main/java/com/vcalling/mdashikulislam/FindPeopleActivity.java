package com.vcalling.mdashikulislam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindPeopleActivity extends AppCompatActivity {
    private RecyclerView searchRecylerView;
    private EditText etSearchPeople;
    private String str = "";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);

        Utils.statusBarColor(this,R.color.orange);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        this.initlizeFeild();
        searchRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        etSearchPeople.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

               if(etSearchPeople.getText().toString().isEmpty()){
                   Toast.makeText(FindPeopleActivity.this, "Please write name to search", Toast.LENGTH_SHORT).show();
               }else {
                   str = charSequence.toString();
                   onStart();
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
            btnVideoCall.setVisibility(View.GONE);

        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = null;
        if (str.equals("")){
            options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(databaseReference,Contacts.class).build();
        }else{
            options = new FirebaseRecyclerOptions.Builder<Contacts>()
                    .setQuery(databaseReference.orderByChild("name").startAt(str).endAt(str+"\uf8ff"),Contacts.class).build();
        }

        FirebaseRecyclerAdapter<Contacts,FindFriendViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder,final int position, @NonNull final Contacts contacts) {
                holder.txtUsername.setText(contacts.getName());
                holder.txtDeatils.setText(contacts.getBio());
                Picasso.get().load(contacts.getImage()).into(holder.profileImage);

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userKey = getRef(position).getKey();
                        Intent intent = new Intent(FindPeopleActivity.this,ContactProfileActivity.class);
                        intent.putExtra("visit_user_id",userKey);
                        intent.putExtra("visit_user_name",contacts.getName());
                        intent.putExtra("visit_user_bio",contacts.getBio());
                        intent.putExtra("visit_user_image",contacts.getImage());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_design, parent, false);
                FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                return  viewHolder;
            }
        };

        searchRecylerView.setAdapter(adapter);
        adapter.startListening();
    }
}
