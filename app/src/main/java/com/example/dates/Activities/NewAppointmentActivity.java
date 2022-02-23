package com.example.dates.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dates.Adapters.CompaniesAdapter;
import com.example.dates.Helper.ToastMessage;
import com.example.dates.Interfaces.Constants;
import com.example.dates.Helper.SharedPreferenceApp;
import com.example.dates.Models.AppointmentModel;
import com.example.dates.Models.CompanyModel;
import com.example.dates.R;
import com.example.dates.databinding.ActivityNewAppointmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewAppointmentActivity extends AppCompatActivity {
    ActivityNewAppointmentBinding b;
    AppointmentModel appointmentModel;
    private Calendar myCalendar;
    private String date = "";
    private String time = "";
    private String company = "";
    private String details = "";
    SimpleDateFormat sdf ;
    FirebaseFirestore db;
    ArrayList<CompanyModel> companyModels;
    CompanyModel companyModel;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityNewAppointmentBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        initial();
        getCompanies();

        b.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b.relativeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePicker();
            }
        });

        b.relativeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        b.newBtnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    details = b.editOtherDetails.getText().toString();
                    appointmentModel = new AppointmentModel(company, date, time, details);
                    newAppointment(appointmentModel);
                }
            }
        });

    }

    private void initial() {
        companyModels = new ArrayList<>();
        companyModel = new CompanyModel();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        db = FirebaseFirestore.getInstance();
        myCalendar = Calendar.getInstance();
        userId = SharedPreferenceApp.getInstance(NewAppointmentActivity.this).getText(Constants.USER_ID, "User");
    }

    private void newAppointment(AppointmentModel appointmentModel) {
        Map<String, Object> appointment = new HashMap<>();
        appointment.put(Constants.COMPANY, appointmentModel.getCompany());
        appointment.put(Constants.DATE, appointmentModel.getDate());
        appointment.put(Constants.TIME, appointmentModel.getTime());
        appointment.put(Constants.DETAILS, appointmentModel.getDetails());

        db.collection(Constants.COLLECTION_USER).document(userId)
                .collection(Constants.COLLECTION_APPOINTMENT)
                .document()
                .set(appointment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    }
                });
    }

    private void ShowDatePicker() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                formatDate();
            }
        };
        new DatePickerDialog(NewAppointmentActivity.this, R.style.DialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void formatDate() {
        date = sdf.format(myCalendar.getTime());
        b.txtDate.setText(date);
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(NewAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String txtHour = "";
                String txtMinute = "";
                Log.d("TAG", "onTimeSet: " + selectedHour);

                int endTime = Integer.parseInt(companyModel.getEnd_time().substring(0,2));
                int startTime = Integer.parseInt(companyModel.getStart_time().substring(0,1));

                if (selectedHour > endTime){
                    ToastMessage.setMessage(NewAppointmentActivity.this , "Time of Company is Ended in " + companyModel.getEnd_time()
                            ,R.drawable.ic_baseline_warning_24 ,R.color.accent);
                    b.txtTime.setError("Time of Company is Ended in " + companyModel.getEnd_time());

                }
                if (selectedHour < startTime){
                    ToastMessage.setMessage(NewAppointmentActivity.this , "Time of Company is Started in " + companyModel.getStart_time()
                            ,R.drawable.ic_baseline_warning_24 ,R.color.accent);
                    b.txtTime.setError("Time of Company is Started in " + companyModel.getStart_time());
                }


                if (selectedHour < 10) {
                    txtHour = "0" + selectedHour;
                } else {
                    txtHour = selectedHour + "";
                }
                if (selectedMinute < 10) {
                    txtMinute = "0" + selectedMinute;
                } else {
                    txtMinute = selectedMinute + "";
                }
                time = txtHour + ":" + txtMinute + ":00";

                b.txtTime.setText(time);
            }
        }, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    private boolean isValid() {
        if (b.txtDate.getText().toString().equals(getString(R.string.reservation_date))) {
            b.txtDate.setError(getString(R.string.error_date));
            return false;
        }

        if (b.txtTime.getText().toString().equals(getString(R.string.reservation_time))) {
            b.txtTime.setError(getString(R.string.error_time));
            return false;
        }
        return true;
    }

    private void getCompanies() {
        db.collection(Constants.COLLECTION_COMPANY)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(Constants.TAG, document.getId() + " ==|> " + document.getData());
                                companyModel = new CompanyModel(
                                        document.getString(Constants.COMPANY_NAME),
                                        document.getString(Constants.COMPANY_PHONE),
                                        document.getString(Constants.START_TIME),
                                        document.getString(Constants.END_TIME),
                                        document.getString(Constants.LAT),
                                        document.getString(Constants.LNG));
                                companyModels.add(companyModel);
                            }
                            b.spinnerCompanies.setAdapter(new CompaniesAdapter(NewAppointmentActivity.this, companyModels));
                            b.spinnerCompanies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    company = companyModels.get(position).getName();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }
                    }
                });
    }
}