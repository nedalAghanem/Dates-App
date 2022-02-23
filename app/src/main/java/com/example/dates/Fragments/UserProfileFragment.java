package com.example.dates.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dates.Activities.Auth.LoginActivity;
import com.example.dates.Adapters.CompanyAdapter;
import com.example.dates.Interfaces.Constants;
import com.example.dates.Helper.SharedPreferenceApp;
import com.example.dates.Models.CompanyModel;
import com.example.dates.Models.UserModel;
import com.example.dates.databinding.FragmentUserProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.dates.Interfaces.Constants.TAG;

public class UserProfileFragment extends Fragment {
    UserModel userModel;
    String userId;
    FragmentUserProfileBinding b;
    View view;

    public UserProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentUserProfileBinding.inflate(inflater, container, false);
        view = b.getRoot();
        userId = SharedPreferenceApp.getInstance(getContext()).getText(Constants.USER_ID, "UserId");
        getUserData();
        b.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        return view;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferenceApp.getInstance(getContext()).saveText(Constants.IS_LOGIN, "false");
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    private void getUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.COLLECTION_USER).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = new UserModel();
                userModel.setFirst_name(documentSnapshot.getString(Constants.FIRST_NAME));
                userModel.setLast_name(documentSnapshot.getString(Constants.LAST_NAME));
                userModel.setPhone(documentSnapshot.getString(Constants.PHONE));
                userModel.setPassword(documentSnapshot.getString(Constants.PASSWORD));
                userModel.setAddress(documentSnapshot.getString(Constants.ADDRESS));
                userModel.setGender(documentSnapshot.getString(Constants.GENDER));

                b.editFirstName.setText(documentSnapshot.getString(Constants.FIRST_NAME));
                b.editLastName.setText(documentSnapshot.getString(Constants.LAST_NAME));
                b.editAddress.setText(documentSnapshot.getString(Constants.ADDRESS));
                b.editPhoneNumber.setText(documentSnapshot.getString(Constants.PHONE));
            }
        });
    }

}