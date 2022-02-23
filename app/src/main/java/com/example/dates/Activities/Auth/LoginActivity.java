package com.example.dates.Activities.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.dates.Activities.HomeActivity;
import com.example.dates.Interfaces.Constants;
import com.example.dates.Helper.SharedPreferenceApp;
import com.example.dates.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding b;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferenceApp.getInstance(this).getText(Constants.IS_LOGIN, "false").equals("true")) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , RegisterActivity.class));
                finishAffinity();
            }
        });

        b.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    loginUser(b.editFirstName.getText().toString(), b.editPassword.getText().toString());
                }
            }
        });

    }

    private boolean isValid() {
        if (!b.editFirstName.getText().toString().trim().isEmpty() && !b.editPassword.getText().toString().trim().isEmpty()) {
            return true;
        } else return false;
    }

    private void loginUser(String first_name, String password) {
        db.collection(Constants.COLLECTION_USER)
                .whereEqualTo(Constants.FIRST_NAME, first_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                db.collection(Constants.COLLECTION_USER)
                                        .whereEqualTo(Constants.PASSWORD, password)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                        Log.d(Constants.TAG, "onComplete "+documentSnapshot.getId());
                                                        SharedPreferenceApp.getInstance(LoginActivity.this).saveText(Constants.IS_LOGIN, "true");
                                                        SharedPreferenceApp.getInstance(LoginActivity.this).saveText(Constants.USER_ID, documentSnapshot.getId());
                                                        startActivity(new Intent(getBaseContext(), HomeActivity.class));
                                                        finishAffinity();
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

    }

}