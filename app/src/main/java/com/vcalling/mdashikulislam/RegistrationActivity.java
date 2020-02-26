package com.vcalling.mdashikulislam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;


import java.util.concurrent.TimeUnit;

import static android.os.Build.VERSION_CODES.P;

public class RegistrationActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText phoneText;
    private CountryCodePicker countryCodePicker;
    private String countryCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth auth;
    private String phoneVerificationId;
    private PhoneAuthProvider.ForceResendingToken token;
    private ProgressDialog dialog;
    private AlertDialog.Builder alertDialog;
    private  String combineNumber;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Find Id from object
        Context context;
        dialog = new ProgressDialog(RegistrationActivity.this);
        alertDialog = new AlertDialog.Builder(this);
        this.findId();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryCode = countryCodePicker.getFullNumberWithPlus();
                String number = phoneText.getText().toString();
                combineNumber =  countryCode + number;


                if (!number.isEmpty()){
                    Intent intent = new Intent(RegistrationActivity.this,VerifyPhoneActivity.class);
                    intent.putExtra("comboNumber",combineNumber);
                    startActivity(intent);

//                    dialog.setTitle("Phone Verification");
//                    dialog.setMessage("We Send you Code");
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.show();

//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                            combineNumber,
//                            60,
//                            TimeUnit.SECONDS,
//                            RegistrationActivity.this,
//                            callbacks
//                    );


                }else{
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Feild Must Not be empty");
                    alertDialog.setIcon(R.drawable.make_call);
                    //alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.setCancelable(true);
                    alertDialog.setNeutralButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                    phoneText.requestFocus();
                }

            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                dialog.dismiss();
            }
        };

    }

    private void findId(){
        btnRegister = findViewById(R.id.btnRegister);
        phoneText = findViewById(R.id.phoneText);
        countryCodePicker = findViewById(R.id.countryCodePicker);
    }


}
