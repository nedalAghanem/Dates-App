package com.example.dates.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dates.Models.CompanyModel;
import com.example.dates.R;

import java.util.ArrayList;

public class CompaniesAdapter extends BaseAdapter {
   private Context context;
   private ArrayList<CompanyModel> albumModels;

    public CompaniesAdapter(Context context, ArrayList<CompanyModel> albumModels) {
        this.albumModels = albumModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return albumModels.size();
    }

    @Override
    public Object getItem(int position) {
        return albumModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(context).inflate(R.layout.custom_spinner, parent, false);

        TextView txtTitle = view.findViewById(R.id.txtTitle);
        CompanyModel model = (CompanyModel) getItem(position);
        txtTitle.setText(model.getName());

        return view;
    }
}
