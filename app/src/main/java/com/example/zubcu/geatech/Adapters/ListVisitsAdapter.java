package com.example.zubcu.geatech.Adapters;

/**
 * Created by user on 11/18/2016.
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.zubcu.geatech.R;

public class ListVisitsAdapter extends BaseAdapter
{

    //ArrayList<ListVisitsViewCellPatterns> patternses;

    private ArrayList<String> data;
    private LayoutInflater mInflater;

    public ListVisitsAdapter(Context context, int list_visits_cell_datetime_set, ArrayList<String> items) {
        mInflater = LayoutInflater.from(context);
        //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = items;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    //ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_visits_datetime_cell, parent, false);

            //holder = new RecyclerView.ViewHolder();
            //holder.textView = (TextView) convertView.findViewById(R.id.item_textView);
            //convertView.setTag(holder);
        } else {
            //holder = (ViewHolder) convertView.getTag();
        }


/*        int type = getItemViewType(position);
        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (type == 0) {
                // Inflate the layout with image
                v = inflater.inflate(R.layout.image_contact_layout, parent, false);
            }
            else {
                v = inflater.inflate(R.layout.simple_contact_layout, parent, false);
            }
        }*/

        return convertView;
    }
}
