package com.example.dates.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dates.Activities.HomeActivity;
import com.example.dates.Adapters.AppointmentAdapter;
import com.example.dates.Interfaces.Constants;
import com.example.dates.Helper.SharedPreferenceApp;
import com.example.dates.Helper.ToastMessage;
import com.example.dates.Models.AppointmentModel;
import com.example.dates.Models.CompanyModel;
import com.example.dates.R;
import com.example.dates.databinding.BottomSheetBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BottomSheet extends BottomSheetDialogFragment {
    private AppointmentModel appointmentModel ;
    private String appointmentId = "";
    private FirebaseFirestore db;
    private BottomSheetBinding b;
    private Calendar myCalendar;
    private String date;
    private String time;
    private View view;
    private String userId;
    private AppointmentAdapter appointmentAdapter;
    private ArrayList<CompanyModel> companyModels;


    public BottomSheet() {
    }

    public static BottomSheet newInstance(AppointmentModel appointmentModel, String appointmentId) {
        Bundle args = new Bundle();
        BottomSheet fragment = new BottomSheet();
        args.putSerializable(Constants.APPOINTMENT_MODEL, appointmentModel);
        args.putString(Constants.APPOINTMENT_ID, appointmentId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appointmentModel = (AppointmentModel) getArguments().getSerializable(Constants.APPOINTMENT_MODEL);
            appointmentId = getArguments().getString(Constants.APPOINTMENT_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = BottomSheetBinding.inflate(inflater, container, false);
        view = b.getRoot();
        initial();

        b.txtCompany.setText(appointmentModel.getCompany());
        b.txtDate.setText(appointmentModel.getDate());
        b.txtTime.setText(appointmentModel.getTime());
        b.editOtherDetails.setText(appointmentModel.getDetails());
        b.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(true);
                b.linearButtons.setVisibility(View.GONE);
                b.btnFinish.setVisibility(View.VISIBLE);
            }
        });

        b.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentModel = new AppointmentModel(b.txtCompany.getText().toString(), b.txtDate.getText().toString(),
                        b.txtTime.getText().toString(), b.editOtherDetails.getText().toString());
                updateInSubCollection(appointmentModel);
            }
        });

        b.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromSubCollection();
            }
        });

        b.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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

        return view;
    }


    private void initial() {
        companyModels = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        myCalendar = Calendar.getInstance();
        userId = SharedPreferenceApp.getInstance(getContext()).getText(Constants.USER_ID, "UserId");
        appointmentAdapter = new AppointmentAdapter();
        setStatus(false);
    }

    private void ShowDatePicker() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };
        new DatePickerDialog(getContext(), R.style.DialogTheme, onDateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        date = sdf.format(myCalendar.getTime());
        b.txtDate.setText(date);
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String txtHour = "";
                String txtMinute = "";
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
        timePickerDialog.setTitle("Select Time ");
        timePickerDialog.show();

    }

    private void setStatus(Boolean enabled) {
        b.txtCompany.setEnabled(false);
        b.relativeDate.setEnabled(enabled);
        b.relativeTime.setEnabled(enabled);
        b.editOtherDetails.setEnabled(enabled);
    }

    private void deleteFromSubCollection() {
        db.collection(Constants.COLLECTION_USER).document(userId).collection(Constants.COLLECTION_APPOINTMENT).document(appointmentId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getContext() , HomeActivity.class));
                        ToastMessage.setMessage(getActivity(), getString(R.string.delete_success), R.drawable.ic_baseline_check_24, R.color.accent);
                    }
                });

    }

    private void updateInSubCollection(AppointmentModel appointmentModel) {
        db.collection(Constants.COLLECTION_USER).document(userId).collection(Constants.COLLECTION_APPOINTMENT)
                .document(appointmentId).update(
                Constants.COMPANY, appointmentModel.getCompany(),
                Constants.DATE, appointmentModel.getDate(),
                Constants.TIME, appointmentModel.getTime(),
                Constants.DETAILS, appointmentModel.getDetails()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getContext() , HomeActivity.class));
                ToastMessage.setMessage(getActivity(), getString(R.string.edit_success), R.drawable.ic_baseline_check_24, R.color.accent);
            }
        });
    }


}
