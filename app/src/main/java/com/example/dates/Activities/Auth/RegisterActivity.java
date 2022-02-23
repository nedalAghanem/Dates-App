package com.example.dates.Activities.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.dates.Activities.HomeActivity;
import com.example.dates.Interfaces.Constants;
import com.example.dates.Helper.SharedPreferenceApp;
import com.example.dates.Models.UserModel;
import com.example.dates.R;
import com.example.dates.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.example.dates.Interfaces.Constants.FIRST_NAME;
import static com.example.dates.Interfaces.Constants.SECOND_OPTION;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding b;

    FirebaseFirestore db;

    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String password = "";
    private String address = "";
    private String gender = "Male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        db = FirebaseFirestore.getInstance();

        b.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(Constants.TAG, "onCheckedChangedCheckedId: " + checkedId);
                if (checkedId == SECOND_OPTION) {
                    gender = "Female";
                } else {
                    gender = "Male";
                }
            }
        });

        b.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid() != null) {
                    signUpUser(isValid());
                }
            }
        });

    }

    private void signUpUser(UserModel model) {

        Map<String, String> user  = new HashMap<>();
        user.put(Constants.FIRST_NAME, model.getFirst_name());
        user.put(Constants.LAST_NAME, model.getLast_name());
        user.put(Constants.PHONE, model.getPhone());
        user.put(Constants.PASSWORD, model.getPassword());
        user.put(Constants.ADDRESS, model.getAddress());
        user.put(Constants.GENDER, model.getGender());

        db.collection(Constants.COLLECTION_USER)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Constants.TAG, "DocumentSnapshot successfully written!");

                        db.collection(Constants.COLLECTION_USER)
                                .whereEqualTo(Constants.FIRST_NAME, firstName)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                                Log.d(Constants.TAG, "UserId "+documentSnapshot.getId());
                                                SharedPreferenceApp.getInstance(RegisterActivity.this).saveText(Constants.IS_LOGIN, "true");
                                                SharedPreferenceApp.getInstance(RegisterActivity.this).saveText(Constants.USER_ID, documentSnapshot.getId());
                                                startActivity(new Intent(getBaseContext(), HomeActivity.class));
                                                finishAffinity();
                                            }
                                        }
                                    }
                                });

                    }
                });
    }


    private String getTextFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private UserModel isValid() {

        firstName = b.editFirstName.getText().toString();
        lastName = getTextFromEditText(b.editLastName);
        phoneNumber = getTextFromEditText(b.editPhoneNumber);
        password = getTextFromEditText(b.editPassword);
        address = getTextFromEditText(b.editAddress);

        if (firstName.isEmpty()) {
            b.editFirstName.setError(getString(R.string.errorFirstName));
        }
        if (lastName.isEmpty()) {
            b.editLastName.setError(getString(R.string.errorLastName));
        }
        if (phoneNumber.isEmpty()) {
            b.editPhoneNumber.setError(getString(R.string.errorPhoneNumber));
        }
        if (password.isEmpty()) {
            b.editPassword.setError(getString(R.string.errorPassword));
        }
        if (address.isEmpty()) {
            b.editAddress.setError(getString(R.string.errorAddress));
        }

        if (!firstName.isEmpty() && !lastName.isEmpty() && !phoneNumber.isEmpty()
                && !password.isEmpty() && !address.isEmpty()) {
            return new UserModel(firstName, lastName, phoneNumber, password, address, gender);
        } else return null;
    }
}