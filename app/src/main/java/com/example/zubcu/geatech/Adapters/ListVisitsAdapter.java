package com.example.zubcu.geatech.Adapters;

/**
 * Created by user on 11/18/2016.
 */

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zubcu.geatech.R;

import static com.google.android.gms.internal.a.v;

public class ListVisitsAdapter extends BaseAdapter
{

    //ArrayList<ListVisitsViewCellPatterns> patternses;

    private ArrayList<String> data;
    private LayoutInflater mInflater;

 /*   @Override
    public int getItemViewType(int position)
    {
        return (patternses.get(position).getContactType() == ListVisitsViewCellPatterns.LayoutTypes.DateTimeNotSet) ? 0 : 1;
    }*/

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
            convertView = mInflater.inflate(R.layout.list_visits_cell_datetime_set, parent, false);

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

/*    private View getInflatedLayoutForType(int type) {
        if (type == ListVisitsViewCellPatterns.LayoutTypes.DateTimeNotSet.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.list_visits_cell_datetime_not_set, null);
        } else if (type == ListVisitsViewCellPatterns.LayoutTypes.DateTimeSet.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.list_visits_cell_datetime_set, null);
        }else {
            return null;
        }
    }*/
}
