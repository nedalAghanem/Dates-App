package com.example.dates.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.dates.Adapters.CompanyAdapter;
import com.example.dates.Interfaces.Constants;
import com.example.dates.Helper.Utils;
import com.example.dates.Models.CompanyModel;
import com.example.dates.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class CompanyFragment extends Fragment {

    CompanyAdapter companyAdapter;
    CompanyModel companyModel;
    ArrayList<CompanyModel> list;
    ArrayList<CompanyModel> cashedList;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    SearchView searchView;
    Realm realm;

    public CompanyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initial(view);
        if (Utils.isWifiConnected(getActivity())) {
            getCompany();
        } else {
            getDataFromCashed();
        }
        setSearchView();
        return view;
    }

    private void initial(View view) {
        Realm.init(getContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        realm = Realm.getInstance(config);
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.companyRecycler);
        searchView = view.findViewById(R.id.searchView);
        companyAdapter = new CompanyAdapter();
        list = new ArrayList<>();
        cashedList = new ArrayList<>();
    }

    private void getCompany() {
        list = new ArrayList<>();
        db.collection(Constants.COLLECTION_COMPANY)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                companyModel = new CompanyModel
                                        (document.get(Constants.COMPANY_NAME).toString(),
                                                document.get(Constants.COMPANY_PHONE).toString(),
                                                document.get(Constants.START_TIME).toString(),
                                                document.get(Constants.END_TIME).toString(),
                                                document.get(Constants.LAT).toString(),
                                                document.get(Constants.LNG).toString());
                                list.add(companyModel);
                                addCompanyToRealM(companyModel);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(new CompanyAdapter(list));
                        } else {
                            Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getCompanySearched(String company) {
        db.collection(Constants.COLLECTION_COMPANY)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list = new ArrayList<>();
                                if (document.get(Constants.COMPANY_NAME).toString().equals(company)) {
                                    companyModel = new CompanyModel
                                            (document.get(Constants.COMPANY_NAME).toString(),
                                                    document.get(Constants.COMPANY_PHONE).toString(),
                                                    document.get(Constants.START_TIME).toString(),
                                                    document.get(Constants.END_TIME).toString(),
                                                    document.get(Constants.LAT).toString(),
                                                    document.get(Constants.LNG).toString());
                                    list.add(companyModel);

                                } else {
                                    return;
                                }
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(new CompanyAdapter(list));
                            companyAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setSearchView() {
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String modelSearch) {
                getCompanySearched(modelSearch);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String modelSearch) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getCompany();
                return false;
            }
        });

    }

    private void addCompanyToRealM(CompanyModel model) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                    realm.insertOrUpdate(model);
            }
        });
    }

    private void getDataFromCashed() {
        RealmResults<CompanyModel> results = realm.where(CompanyModel.class).findAll();
        cashedList.addAll(results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CompanyAdapter(cashedList));
    }


}