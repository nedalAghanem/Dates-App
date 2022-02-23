package com.example.dates.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dates.Helper.Utils;
import com.example.dates.Models.AppointmentModel;
import com.example.dates.Models.CompanyModel;
import com.example.dates.Interfaces.OnAppointmentClick;
import com.example.dates.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    private ArrayList<AppointmentModel> albumModels;
    private Activity activity;
    private OnAppointmentClick onAppointmentClick;
    private AppointmentModel appointmentModel = new AppointmentModel();
    private CompanyModel companyModel = new CompanyModel();


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageLocation;
        TextView txtCompany;
        TextView txtDate;
        TextView txtTime;
        TextView txtTimeRemain;


        MyViewHolder(View view) {
            super(view);
            imageLocation = view.findViewById(R.id.imageLocation);
            txtCompany = view.findViewById(R.id.customTxtCompany);
            txtDate = view.findViewById(R.id.customTxtDate);
            txtTime = view.findViewById(R.id.customTxtTime);
            txtTimeRemain = view.findViewById(R.id.txtTimeRemain);

        }
    }

    public AppointmentAdapter(Activity activity , CompanyModel companyModel ,  ArrayList<AppointmentModel> albumModels,
                              OnAppointmentClick onAppointmentClick) {
        this.activity = activity;
        this.companyModel = companyModel;
        this.albumModels = albumModels;
        this.onAppointmentClick = onAppointmentClick;
    }

    public AppointmentAdapter() {
    }

    @NonNull
    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_appointment, parent, false);
        return new AppointmentAdapter.MyViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull final AppointmentAdapter.MyViewHolder holder,
                                 final int position) {
        appointmentModel = albumModels.get(position);

        if (appointmentModel != null) {
            holder.imageLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", companyModel.getLat(), companyModel.getLng());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    activity.startActivity(intent);
                }
            });
            holder.txtCompany.setText(appointmentModel.getCompany());
            holder.txtDate.setText(appointmentModel.getDate());
            holder.txtTime.setText(appointmentModel.getTime());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAppointmentClick.onItemClick(albumModels.get(position));
                }
            });
        }

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date format = sdf.parse(sdf.format(new Date()));
            Date date = sdf.parse(appointmentModel.getDate() + " " + appointmentModel.getTime());
            String remainTime = Utils.printDifference(format, date);
            if (remainTime.contains("-")) {
                remainTime = activity.getString(R.string.appointment_ended);
            }
            if (remainTime.equals(activity.getString(R.string.appointment_ended))) {
                holder.txtTimeRemain.setBackgroundResource(R.color.pink);
            } else holder.txtTimeRemain.setBackgroundResource(R.color.whiteBG);
            holder.txtTimeRemain.setText(remainTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return albumModels.size();
    }
}
