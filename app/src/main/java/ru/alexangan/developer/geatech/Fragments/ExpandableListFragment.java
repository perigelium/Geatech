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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import ru.alexangan.developer.geatech.Adapters.ClimaReportAdapterHelper;

import ru.alexangan.developer.geatech.R;


public class ExpandableListFragment extends Fragment
{

    final String LOG_TAG = "myLogs";

    ExpandableListView elvMain;
    ClimaReportAdapterHelper ah;
    SimpleExpandableListAdapter adapter;
    TextView tvInfo;
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

        tvInfo = (TextView) rootView.findViewById(R.id.tvInfo);

        // создаем адаптер
        ah = new ClimaReportAdapterHelper(context);
        adapter = ah.getAdapter();

        elvMain = (ExpandableListView) rootView.findViewById(R.id.elvMain);
        elvMain.setAdapter(adapter);

        // нажатие на элемент
        elvMain.setOnChildClickListener(new OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition,   int childPosition, long id) {
                Log.d(LOG_TAG, "onChildClick groupPosition = " + groupPosition +
                        " childPosition = " + childPosition +
                        " id = " + id);
                tvInfo.setText(ah.getGroupChildText(groupPosition, childPosition));
                return false;
            }
        });

        // нажатие на группу
        elvMain.setOnGroupClickListener(new OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Log.d(LOG_TAG, "onGroupClick groupPosition = " + groupPosition +
                        " id = " + id);
                // блокируем дальнейшую обработку события для группы с позицией 1
                //if (groupPosition == 1) return true;

                return false;
            }
        });

        // сворачивание группы
        elvMain.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            public void onGroupCollapse(int groupPosition) {
                Log.d(LOG_TAG, "onGroupCollapse groupPosition = " + groupPosition);
                tvInfo.setText("Свернули " + ah.getGroupText(groupPosition));
            }
        });

        // разворачивание группы
        elvMain.setOnGroupExpandListener(new OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                Log.d(LOG_TAG, "onGroupExpand groupPosition = " + groupPosition);
                tvInfo.setText("Развернули " + ah.getGroupText(groupPosition));
            }
        });

        // разворачиваем группу с позицией 2
        elvMain.expandGroup(2);

        return rootView;
    }
}
