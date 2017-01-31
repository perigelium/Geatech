package ru.alexangan.developer.geatech.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.alexangan.developer.geatech.R;

/**
 * Created by user on 27.01.2017.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter
{

    private ArrayList<LinkedHashMap<String, View>> mGroups;
    private Context mContext;
    private List<View> childViews;

    public MyExpandableListAdapter(Context context, ArrayList<LinkedHashMap<String, View>> groups)
    {
        mContext = context;
        mGroups = groups;
        childViews = Arrays.asList(new View[mGroups.size()]);
    }

    @Override
    public int getGroupCount()
    {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        int childrenCount = mGroups.get(groupPosition).keySet().size();
        return childrenCount;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        Object group = mGroups.get(groupPosition);
        return group;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        Object child = mGroups.get(groupPosition).get(childPosition);
        return child;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        long groupId = groupPosition;
        return groupId;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        long childId = childPosition;
        return childId;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent)
    {

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_view, null);
        }

        if (isExpanded)
        {
            //Изменяем что-нибудь, если текущая Group раскрыта
        } else
        {
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        String sectionName = (new ArrayList<String>(mGroups.get(groupPosition).keySet())).get(0);

        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        textGroup.setText(sectionName);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        //int grPos = groupPosition;
        //int childPos = childPosition;

        //if (convertView == null)
        {
            convertView = (new ArrayList<View>(mGroups.get(groupPosition).values())).get(0);
/*
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(child_view_id, null);*/

            //childViews.set(groupPosition, convertView);
        }
/*        else //if(childViews.get(groupPosition) != null)
        {
            return childViews.get(groupPosition);
        }*/

/*            TextView textChild = (TextView) convertView.findViewById(R.id.textChild);
            textChild.setText(mGroups.get(groupPosition).get(childPosition));

            Button button = (Button)convertView.findViewById(R.id.buttonChild);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"button is pressed", Toast.LENGTH_LONG).show();
                }
            });*/

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

}
