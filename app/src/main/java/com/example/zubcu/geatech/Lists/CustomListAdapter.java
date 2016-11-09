package com.example.zubcu.geatech.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zubcu.geatech.R;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

	private ArrayList<NewsItem> listData;

	private LayoutInflater layoutInflater;

	public CustomListAdapter(Context context, ArrayList<NewsItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.cell_unfilled_item, null);
			holder = new ViewHolder();
			holder.icClientView = (ImageView) convertView.findViewById(R.id.imageView);
			holder.icCommandView = (ImageView) convertView.findViewById(R.id.imageView2);
			holder.icLocationView = (ImageView) convertView.findViewById(R.id.imageView3);
			holder.clientView = (TextView) convertView.findViewById(R.id.nameClient);
			holder.commandView = (TextView) convertView.findViewById(R.id.command);
			holder.locationView = (TextView) convertView.findViewById(R.id.street);
			holder.dayView = (TextView) convertView.findViewById(R.id.dd);
			holder.monthView = (TextView) convertView.findViewById(R.id.mm);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.icClientView.setImageResource(listData.get(position).getIcClient());
		holder.icCommandView.setImageResource(listData.get(position).getIcCommand());
		holder.icLocationView.setImageResource(listData.get(position).getIcLocation());
		holder.clientView.setText(listData.get(position).getClient());
		holder.commandView.setText(listData.get(position).getCommand());
		holder.locationView.setText(listData.get(position).getLocation());
		holder.dayView.setText(listData.get(position).getDay());
		holder.monthView.setText(listData.get(position).getMonth());

		return convertView;
	}

	static class ViewHolder {

		ImageView icClientView;
		ImageView icCommandView;
		ImageView icLocationView;
		TextView clientView;
		TextView commandView;
		TextView locationView;
		TextView dayView;
		TextView monthView;

	}

}
