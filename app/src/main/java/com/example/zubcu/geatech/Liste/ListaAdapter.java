package com.example.zubcu.geatech.Liste;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zubcu.geatech.R;

/**
 * Created by zubcu on 10/4/2016.
 */

public class ListaAdapter extends ArrayAdapter<Lista> {

    Context context;
    int layoutResourceId;
    Lista data[] = null;
    ImageView imageView;
    ImageView imageView2;
    ImageView imageView3;
    TextView textView1;
    TextView textView2;
    TextView textView3;

    public ListaAdapter(Context context, int layoutResourceId, Lista[] data){
        super( context, layoutResourceId, data);
        this.context =  context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;

    }

//    public Object getItem(int position){
//        return  data.get(position);
//    }



    @NonNull
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View row = inflater.inflate(R.layout.comand_cell, parent, false);
//            final Produs item = (Produs)getItem(position);

            imageView = (ImageView)row.findViewById(R.id.imageView);
            imageView2 = (ImageView)row.findViewById(R.id.imageView2);
            imageView3 = (ImageView)row.findViewById(R.id.imageView3);
            textView1 = (TextView) row.findViewById(R.id.nameClient);
            textView2 = (TextView)row.findViewById(R.id.textView2);
            textView3 = (TextView)row.findViewById(R.id.textView3);






//        Lista lista = this.ListaHolder.get(position);
//        holder.textView1.setText(lista.txt1);
//        holder.textView2.setText(lista.txt2);
//        holder.textView3.setText(lista.txt3);
//        holder.imageView.setImageResource(lista.icon1);
//        holder.imageView2.setImageResource(lista.icon2);
//        holder.imageView3.setImageResource(lista.icon3);

        return row;

    }

    static class ListaHolder
    {
        ImageView imageView;
        ImageView imageView2;
        ImageView imageView3;
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }

}
