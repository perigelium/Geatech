package ru.alexangan.developer.geatech.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.alexangan.developer.geatech.R;

public class GridViewAdapter extends ArrayAdapter<Bitmap>
{

    private int layoutResourceId;
    private ArrayList<Bitmap> imagesArrayList;
    private Context galleryContext;

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Bitmap> imagesArrayList)
    {
        super(context, layoutResourceId, imagesArrayList);

        this.layoutResourceId = layoutResourceId;
        this.imagesArrayList = imagesArrayList;

        galleryContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) galleryContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else
        {
            holder = (ViewHolder) row.getTag();
        }

        if(imagesArrayList.size() != 0)
        {
            Bitmap item = imagesArrayList.get(position);
            holder.image.setImageBitmap(item);

            // scale type within view area
            holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return row;
    }

    private class ViewHolder
    {
        TextView imageTitle;
        ImageView image;
    }
}