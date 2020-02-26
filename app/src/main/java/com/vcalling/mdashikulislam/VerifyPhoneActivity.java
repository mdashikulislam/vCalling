package com.vcalling.mdashikulislam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView btnReceiveCodeText,countText,btnResend,btnVerify;
    private static String comboNumber;
    int count = 60;
    private String phoneVerificationId;

    private FirebaseAuth auth;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        dialog = new ProgressDialog(VerifyPhoneActivity.this);
        auth = FirebaseAuth.getInstance();
        this.initilizeScope();
        this.toolbarSetting();
        this.statusBarColorChange();
        comboNumber =  getIntent().getStringExtra("comboNumber");
        phoneAuth();
        timeCount();

    }

    private void phoneAuth() {
        dialog.setTitle("Phone Authentication");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("We are verifying your phone number");
        dialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                comboNumber,
                60,
                TimeUnit.SECONDS,
                VerifyPhoneActivity.this,
                callbacks
        );

    }
    public void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,code);
        signInWithPhoneCredential(credential);
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(VerifyPhoneActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(VerifyPhoneActivity.this,"Not verify",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
           String code = phoneAuthCredential.getSmsCode();

           if (code !=null){
               verifyCode(code);
               dialog.dismiss();
           }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            dialog.dismiss();
            Toast.makeText(VerifyPhoneActivity.this, "Verification Faild" + comboNumber, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            dialog.dismiss();
            phoneVerificationId = s;
            Toast.makeText(VerifyPhoneActivity.this, "Code Send " + comboNumber, Toast.LENGTH_SHORT).show();
        }
    };

    private void toolbarSetting(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
    }

    public void initilizeScope(){
        countText = findViewById(R.id.timecount);
        btnResend = findViewById(R.id.btnResend);
        btnReceiveCodeText = findViewById(R.id.txtRcvCodeTxt);
        btnVerify = findViewById(R.id.btnVerify);
    }
    private void statusBarColorChange(){
        Utils.statusBarColor(this,R.color.orange);
    }
    private void timeCount(){
        Thread th = new Thread(){
            @Override
            public void run() {
                while (!isInterrupted()){
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count--;
                                if (count <=60 && count >0){
                                    countText.setText(String.valueOf(count)+"s");
                                }else{
                                    countText.setVisibility(View.GONE);
                                    btnResend.setVisibility(View.VISIBLE);
                                    isInterrupted();
                                }

                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        th.start();
}
}
