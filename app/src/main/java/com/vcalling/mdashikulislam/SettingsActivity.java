package com.vcalling.mdashikulislam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText txtName, txtDetails;
    private Button btnSave;
    private Toolbar toolbar;
    private static int galleryCode = 1;
    private Uri imageUri;
    private StorageReference profileImageRef;
    private String downlaodurl;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Utils.statusBarColor(this,R.color.orange);

        progressDialog = new ProgressDialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        profileImageRef = FirebaseStorage.getInstance().getReference().child("Image");
        this.initializeFeild();
        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,galleryCode);
            }
        });




        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = txtName.getText().toString();
                final String bio = txtDetails.getText().toString();
                if (imageUri == null){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image")){
                                saveInfoOnly();
                            }else {
                                Toast.makeText(SettingsActivity.this, "Please Select Image First", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else if (username.isEmpty()){
                    Toast.makeText(SettingsActivity.this, "Username is is mandatory", Toast.LENGTH_SHORT).show();
                }else if (bio.isEmpty()){
                    Toast.makeText(SettingsActivity.this, "Status is is mandatory", Toast.LENGTH_SHORT).show();
                }else{

                    progressDialog.setTitle("Account Setting");
                    progressDialog.setMessage("Please wait... we are updating your profile");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    final StorageReference filePath= profileImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    final UploadTask task = filePath.putFile(imageUri);
                    task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw  task.getException();
                            }
                            downlaodurl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                downlaodurl = task.getResult().toString();
                                HashMap<String ,Object> profileMap = new HashMap<>();
                                profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                profileMap.put("name",username);
                                profileMap.put("bio",bio);
                                profileMap.put("image",downlaodurl);

                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .updateChildren(profileMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Intent intent = new Intent(SettingsActivity.this,HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this,"profile setting has been updated",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });

        retriveInformation();
    }

    private void saveInfoOnly() {
        final String username = txtName.getText().toString();
        final String bio = txtDetails.getText().toString();


        if (username.isEmpty()){
            Toast.makeText(SettingsActivity.this, "Username is mandatory", Toast.LENGTH_SHORT).show();
        }else if(bio.isEmpty()){
            Toast.makeText(SettingsActivity.this, "Bio is mandatory", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle("Account Setting");
            progressDialog.setMessage("Please wait... we are updating your profile");
            progressDialog.setCancelable(false);
            progressDialog.show();
            HashMap<String ,Object> profileMap = new HashMap<>();
            profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("name",username);
            profileMap.put("bio",bio);
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                                finish();
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void initializeFeild(){
        imageView = findViewById(R.id.imageView);
        txtName = findViewById(R.id.txtName);
        txtDetails = findViewById(R.id.txtDetails);
        btnSave = findViewById(R.id.btnSave);
        toolbar = findViewById(R.id.toolbar);
    }

    private void retriveInformation(){
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String dbImage = dataSnapshot.child("image").getValue().toString();
                    String dbName = dataSnapshot.child("name").getValue().toString();
                    String dbBio = dataSnapshot.child("bio").getValue().toString();
                    txtName.setText(dbName);
                    txtDetails.setText(dbBio);
                    Picasso.get().load(dbImage).placeholder(R.drawable.profile_image).into(imageView);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryCode && resultCode == RESULT_OK && data !=null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

}
