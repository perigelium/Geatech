package ru.alexangan.developer.geatech.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.alexangan.developer.geatech.Models.SpinnerItemData;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;

// Created by Alex Angan 19.04.2017.

public class CustomSpinnerAdapter extends ArrayAdapter<SpinnerItemData>
{
    int groupid;
    Activity activity;
    ArrayList<SpinnerItemData> list;
    LayoutInflater inflater;

    public CustomSpinnerAdapter(Activity activity, int groupid, int id, ArrayList<SpinnerItemData> list)
    {
        super(activity, id, list);
        this.list = list;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View itemView = inflater.inflate(groupid, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.ivVisitsFilterDialogItem);
        int listVisitsMode = mSettings.getInt("listVisitsFilterMode", 0);

        if(listVisitsMode == position)
        {
            imageView.setImageResource(R.drawable.check_mark_green_small);
        }
        else
        {
            imageView.setImageResource(R.drawable.transparent21px);
        }

        TextView textView = (TextView) itemView.findViewById(R.id.tvVisitsFilterDialogItem);
        textView.setText(list.get(position).getText());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView(position, convertView, parent);

    }
}
