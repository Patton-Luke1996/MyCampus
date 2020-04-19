package com.my_campus.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class VerifyPhoneNumberActivity extends AppCompatActivity {

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    private FirebaseFirestore db;
    private DocumentReference docRef;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private EditText userPhoneNumberInput;
    private EditText userSMSCodeInput;

    private String userPhoneNumber = "";
    private String smsBody = "";

    private int masterVerificationCode;
    private int smsInput;

    private Button sendVerificationCodeBTN;
    private Button verifyCodeBTN;

    private ProgressBar progressBar;

    private TextView verifySuccessText;
    private TextView verifyFailureText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        userPhoneNumberInput = findViewById(R.id.phoneNumberInput);
        userSMSCodeInput = findViewById(R.id.smsCodeInput);

        sendVerificationCodeBTN = findViewById(R.id.sendSMSButton);
        verifyCodeBTN = findViewById(R.id.verifySMSCodeBTN);

        progressBar = findViewById(R.id.progressBarVPN);

        verifyFailureText = findViewById(R.id.verifyFailure);
        verifySuccessText = findViewById(R.id.verifySuccess);

        sendVerificationCodeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputPhoneNumber();
            }
        });

        verifyCodeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputCode();
            }
        });
    }

    private void getInputCode() {
        if (!userSMSCodeInput.getText().toString().matches("")) {
            smsInput = Integer.parseInt(userSMSCodeInput.getText().toString());
            validateSMSCode();
        } else {
            Toast.makeText(this, "Please enter a verification code.", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateSMSCode() {
        if (smsInput == masterVerificationCode) {
            verifySuccessText.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    verifySuccessText.setVisibility(View.INVISIBLE);
                    uploadPhoneNumber();
                }
            }, 3000);

        } else {
            verifyFailureText.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    verifyFailureText.setVisibility(View.INVISIBLE);
                }
            }, 5000);
        }
    }

    private void uploadPhoneNumber() {

        docRef = db.collection("user_profiles").document(user.getUid());
        progressBar.setVisibility(View.VISIBLE);

        docRef.update("phoneNumber", userPhoneNumber)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        verifySuccessText.setVisibility(View.INVISIBLE);

                        Toast.makeText(VerifyPhoneNumberActivity.this, "Phone number updated.", Toast.LENGTH_SHORT).show();

                        Intent nextActivity = new Intent(VerifyPhoneNumberActivity.this, AppMainActivity.class);
                        startActivity(nextActivity);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("VerifyPhoneNumbAct", "Error updating document", e);
                        Toast.makeText(VerifyPhoneNumberActivity.this, "Error updating phone number. Please re-verify sms code to try again.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getInputPhoneNumber() {
        if (!userPhoneNumberInput.getText().toString().matches("")) {
            userPhoneNumber = userPhoneNumberInput.getText().toString();
            userPhoneNumber = "1" + userPhoneNumber;

            generateVerificationCode();
        } else {
            Toast.makeText(this, "Please enter a phone number.", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateVerificationCode() {
        Random r = new Random();
        masterVerificationCode = r.nextInt(9999 - 1000) + 1000;

        generateSMSString();
    }

    private void generateSMSString() {
        smsBody = "Your MyCampus verification code is: " + masterVerificationCode;
        checkSMSPermission();
    }

    private void checkSMSPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
            }

        } else {
            sendSMS();
        }

    }

    private void sendSMS() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(userPhoneNumber, null, smsBody, null, null);
        Toast.makeText(getApplicationContext(), "Message sent. Please check your default SMS app for verification code.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    Toast.makeText(getApplicationContext(),"SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
}
