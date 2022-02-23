package com.example.dates.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.dates.Adapters.AppointmentAdapter;
import com.example.dates.Interfaces.Constants;
import com.example.dates.Helper.SharedPreferenceApp;
import com.example.dates.Helper.Utils;
import com.example.dates.Models.AppointmentModel;
import com.example.dates.Models.CompanyModel;
import com.example.dates.Interfaces.OnAppointmentClick;
import com.example.dates.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class HomeFragment extends Fragment {

    RecyclerView appointmentRecycler;
    ArrayList<AppointmentModel> list;
    ArrayList<AppointmentModel> listByDate;
    ArrayList<AppointmentModel> cashedList;
    AppointmentModel appointmentModel;
    AppointmentAdapter appointmentAdapter;
    FirebaseFirestore db;
    CalendarView calendarView;
    Realm realm;
    String documentId;
    String userId;
    CompanyModel companyModel;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initial(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Realm.init(getContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(realmConfiguration);
        if (Utils.isWifiConnected(getActivity())) {
            getDataFromSubCollection();
        } else {
            getDataFromCashed();
        }
    }

    private void initial(View view) {
        appointmentRecycler = view.findViewById(R.id.appointmentRecycler);
        calendarView = view.findViewById(R.id.calendarView);
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        listByDate = new ArrayList<>();
        cashedList = new ArrayList<>();
        appointmentModel = new AppointmentModel();
        appointmentAdapter = new AppointmentAdapter();
        userId = SharedPreferenceApp.getInstance(getContext()).getText(Constants.USER_ID, "User");
        companyModel = new CompanyModel();
    }

    private void getDataFromSubCollection() {
        db.collection(Constants.COLLECTION_USER).document(userId).collection(Constants.COLLECTION_APPOINTMENT)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documentId = document.getId();
                                appointmentModel = new AppointmentModel (
                                        (String) document.get(Constants.COMPANY),
                                                (String) document.get(Constants.DATE),
                                                (String) document.get(Constants.TIME),
                                                (String) document.get(Constants.DETAILS));
                                list.add(appointmentModel);
                                addAppointmentToRealM(appointmentModel);
                            }
                            appointmentAdapter = new AppointmentAdapter(getActivity(), getCompany(), list, new OnAppointmentClick() {
                                @Override
                                public void onItemClick(AppointmentModel model) {
                                    BottomSheet bottomSheet = BottomSheet.newInstance(model, documentId);
                                    bottomSheet.show(getActivity().getSupportFragmentManager(), null);
                                }
                            });
                            appointmentRecycler.setAdapter(appointmentAdapter);
                            appointmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                            setCalenderEvents();
                        }
                    }
                });
    }

    private void getAppointmentsByDate(String date) {
        listByDate = new ArrayList<>();
        db.collection(Constants.COLLECTION_USER).document(userId).collection(Constants.COLLECTION_APPOINTMENT)
                .whereEqualTo(Constants.DATE, date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documentId = document.getId();
                                appointmentModel = new AppointmentModel
                                        ((String) document.get(Constants.COMPANY),
                                                (String) document.get(Constants.DATE),
                                                (String) document.get(Constants.TIME),
                                                (String) document.get(Constants.DETAILS));
                                listByDate.add(appointmentModel);
                                addAppointmentToRealM(appointmentModel);
                            }
                            if (listByDate.isEmpty()){
                                listByDate = list;
                            }
                            appointmentAdapter = new AppointmentAdapter(getActivity(), getCompany(), listByDate, new OnAppointmentClick() {
                                @Override
                                public void onItemClick(AppointmentModel model) {
                                    BottomSheet bottomSheet = BottomSheet.newInstance(model, documentId);
                                    bottomSheet.show(getActivity().getSupportFragmentManager(), null);
                                }
                            });
                            appointmentRecycler.setAdapter(appointmentAdapter);
                            appointmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                            setCalenderEvents();
                        }
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setCalenderEvents() {
        List<EventDay> events = new ArrayList<>();
        String date = "";
        int count = 0;
        List<String> firstList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            firstList.add(list.get(i).getDate());
        }
        List<String> secondList = new ArrayList<>();
        for (int i = 0; i < firstList.size(); i++) {
            secondList.add(firstList.get(i) + " " + Collections.frequency(firstList, firstList.get(i)));
        }
        for (int i = 0; i < secondList.size(); i++) {
            date = secondList.get(i);
            //yyyy-mm-dd
            int year = Integer.parseInt(secondList.get(i).substring(0, 4));
            int month = Integer.parseInt(secondList.get(i).substring(5, 7));
            int day = Integer.parseInt(secondList.get(i).substring(8, 10));
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day);
            int x = Integer.parseInt(secondList.get(i).substring(secondList.get(i).length() - 1));
            switch (x) {
                case 1:
                    events.add(new EventDay(calendar, R.drawable.ic_baseline_brightness_1_24));
                    break;
                case 2:
                    events.add(new EventDay(calendar, R.drawable.sample_icon_2));
                    break;
                case 3:
                    events.add(new EventDay(calendar, R.drawable.sample_icon_3));
                    break;
                default:
                    events.add(new EventDay(calendar, R.drawable.ic_baseline_read_more_24));
                    break;

            }
        }
        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Log.d("TAG", "onDayClick: " + clickedDayCalendar.getTime().getTime());
                Date date = new Date(clickedDayCalendar.getTime().getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String txtDate = sdf.format(date);
                Log.d("TAG", "onDayClick: " + txtDate);
                getAppointmentsByDate(txtDate);

            }
        });
    }

    private void addAppointmentToRealM(AppointmentModel model) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(model);
            }
        });
    }

    private void getDataFromCashed() {
        RealmResults<AppointmentModel> results = realm.where(AppointmentModel.class).findAll();
        cashedList.addAll(results);
        appointmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentRecycler.setAdapter(new AppointmentAdapter(getActivity(), getCompany(), cashedList, new OnAppointmentClick() {
                    @Override
                    public void onItemClick(AppointmentModel model) {
                        BottomSheet bottomSheet = BottomSheet.newInstance(model, documentId);
                        bottomSheet.show(getActivity().getSupportFragmentManager(), null);
                    }
                })
        );

    }

    private CompanyModel getCompany() {
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
                            }
                        }
                    }
                });
        return companyModel;
    }

}
