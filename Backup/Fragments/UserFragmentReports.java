package com.example.zubcu.geatech.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.example.zubcu.geatech.R;


/**
 * Created by zubcu on 9/30/2016.
 */

public class UserFragmentReports extends Fragment {

    Button raport;
    Button time;
    Button cloud;
    Button read;
    Button write;



 //   List<ProdModel> list = new ArrayList<>();
    //List<CategoryModel> list2 = new ArrayList<>();
    String id;
    Integer idd;
    ListView listView;
    GridView listCateg;
   // ProdAdapter adapter;
  //  TopCategoryAdapter Catadapter;
    ProgressDialog dialog;
  //  DaoMaster daoMaster;
   // DaoSession daoSession;
    SQLiteDatabase db;
  //  DaoMaster.DevOpenHelper helper;
    Button tavolaCalda;
    Button desserts;
    Button drinks;
   // ProdModel ct;
  //  CategoryModel cm;




    // private ListView list_comezi;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // helper = new DaoMaster.DevOpenHelper(getActivity(), "db", null);
      //  db = helper.getWritableDatabase();
      //  daoMaster = new DaoMaster(db);
       // daoSession = daoMaster.newSession();
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        idd = sharedpreferences.getInt("categorySelectedId", 1);
        id = sharedpreferences.getString("selectedCategory", "");
    }


    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View result = inflater.inflate(R.layout.comand_list, null);
        listView = (ListView) result.findViewById(R.id.list_comezi);
        raport = (Button) result.findViewById(R.id.button1);
        time = (Button) result.findViewById(R.id.button2);
        write = (Button) result.findViewById(R.id.button3);
        cloud = (Button) result.findViewById(R.id.button4);
        read = (Button) result.findViewById(R.id.button5);

//        for (Raport c: daoSession.getCategoryDao().loadAll()){
//            cm = new CategoryModel();
//            cm.setId(Integer.valueOf(String.valueOf(c.getId())));
//            cm.setName(c.getName());
//            list2.add(cm);
//        }


//        adapter = new ListaAdapter(list, getActivity(), this);
//
//        listView.setAdapter(adapter);
        return result;
    }


}
