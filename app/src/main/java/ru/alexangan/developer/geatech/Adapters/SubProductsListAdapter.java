package ru.alexangan.developer.geatech.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.alexangan.developer.geatech.Models.SubproductItem;
import ru.alexangan.developer.geatech.R;

/**
 * Created by user on 11/15/2016.*/

public class SubProductsListAdapter extends ArrayAdapter
{
    private List<SubproductItem> subproductItems;

    public SubProductsListAdapter
            (Context context, List<SubproductItem> subproductItems)
    {

        super(context, 0, subproductItems);

        this.subproductItems = subproductItems;
    }

    @NonNull
    @Override

    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {

        // Get the data item for this position

        //ru.alexangan.developer.geatech.Models.DateTimeSetListCellModel array = (ru.alexangan.developer.geatech.Models.DateTimeSetListCellModel)getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null)
        {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.visits_day_time_set_list_row, parent, false);

        }


        // Lookup view for data population

        TextView tvProdottoDataSDT = (TextView) convertView.findViewById(R.id.tvProdottoDataSDT);

        TextView tvTipoDataSDT = (TextView) convertView.findViewById(R.id.tvTipoDataSDT);

        TextView tvN_PezzoDataSDT = (TextView) convertView.findViewById(R.id.tvN_PezzoDataSDT);

        // Populate the data into the template view using the data object

        tvProdottoDataSDT.setText(subproductItems.get(position).getSubproduct());

        tvTipoDataSDT.setText(subproductItems.get(position).getProductType());

        Integer piecesNr = subproductItems.get(position).getPiecesNr();

        tvN_PezzoDataSDT.setText(String.valueOf(piecesNr));

        // Return the completed view to render on screen

        return convertView;

    }
}