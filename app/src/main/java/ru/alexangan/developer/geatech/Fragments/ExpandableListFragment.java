package ru.alexangan.developer.geatech.Fragments;

/**
 * Created by user on 27.01.2017.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RadioGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ru.alexangan.developer.geatech.Adapters.ClimaReportAdapterHelper;

import ru.alexangan.developer.geatech.Adapters.MyExpandableListAdapter;
import ru.alexangan.developer.geatech.R;


public class ExpandableListFragment extends Fragment
{

    final String LOG_TAG = "DEBUG";

    //ExpandableListView elvMain;
    //ClimaReportAdapterHelper ah;
    //SimpleExpandableListAdapter adapter;
    //TextView tvInfo;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.expandable_list_view, container, false);

        //tvInfo = (TextView) rootView.findViewById(R.id.tvInfo);

        ExpandableListView listView = (ExpandableListView)rootView.findViewById(R.id.exListView);

        View vTypeOfBuilding = inflater.inflate(R.layout.type_of_building, null);
        final RadioGroup rgTypeOfBuilding = (RadioGroup) vTypeOfBuilding.findViewById(R.id.rgTypeOfBuilding);

        View vUnitOutdoorPosition = inflater.inflate(R.layout.unit_outdoor_position, null);
        final RadioGroup grUnitOutdoorPosition = (RadioGroup) vUnitOutdoorPosition.findViewById(R.id.rgUnitOutdoorPosition);

        //Создаем набор данных для адаптера
        ArrayList<LinkedHashMap<String, View>> groups = new ArrayList<LinkedHashMap<String, View>>();

        LinkedHashMap<String, View> section1 = new LinkedHashMap<String, View>();
        LinkedHashMap<String, View> section2 = new LinkedHashMap<String, View>();

        section1.put(" 1.1 TIPO DI EDIFICIO:", vTypeOfBuilding);
        groups.add(section1);
        section2.put(" 1.2 POSIZIONAMENTO UNITÀ ESTERNA:", vUnitOutdoorPosition);
        groups.add(section2);

        //Создаем адаптер и передаем context и список с данными
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(context, groups);
        listView.setAdapter(adapter);

        // нажатие на элемент
        listView.setOnChildClickListener(new OnChildClickListener()
        {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id)
            {
                Log.d(LOG_TAG, "onChildClick groupPosition = " + groupPosition +
                        " childPosition = " + childPosition +
                        " id = " + id);
                //tvInfo.setText(ah.getGroupChildText(groupPosition, childPosition));
                return false;
            }
        });

        // нажатие на группу
        listView.setOnGroupClickListener(new OnGroupClickListener()
        {
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id)
            {
                Log.d(LOG_TAG, "onGroupClick groupPosition = " + groupPosition +
                        " id = " + id);
                // блокируем дальнейшую обработку события для группы с позицией 1
                //if (groupPosition == 1) return true;

                return false;
            }
        });

        // сворачивание группы
        listView.setOnGroupCollapseListener(new OnGroupCollapseListener()
        {
            public void onGroupCollapse(int groupPosition)
            {
                Log.d(LOG_TAG, "onGroupCollapse groupPosition = " + groupPosition);
                //tvInfo.setText("Свернули " + ah.getGroupText(groupPosition));

                int id1 = rgTypeOfBuilding.getCheckedRadioButtonId();
                int id2 = grUnitOutdoorPosition.getCheckedRadioButtonId();
            }
        });

        // разворачивание группы
        listView.setOnGroupExpandListener(new OnGroupExpandListener()
        {
            public void onGroupExpand(int groupPosition)
            {
                Log.d(LOG_TAG, "onGroupExpand groupPosition = " + groupPosition);
                //tvInfo.setText("Развернули " + ah.getGroupText(groupPosition));


            }
        });

        // разворачиваем группу с позицией 2
        //elvMain.expandGroup(2);

        return rootView;
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
