package com.example.zubcu.geatech.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zubcu.geatech.Lists.CustomListAdapter;
import com.example.zubcu.geatech.Lists.NewsItem;
import com.example.zubcu.geatech.R;

import java.util.ArrayList;

public class MainActivity extends Activity {

	Button report;
	Button reportsByTime;
	Button write;

	private Button [] btn = new Button[5];

//	private Button btn_unfocus;
//	private int[] btn_id = {R.id.report, R.id.time, R.id.write, R.id.cloud, R.id.read};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders_list);

//		for (int i = 0; i < btn.length; i++){
//			btn[i] = (Button) findViewById(btn);
//			btn[1].setBackgroundResource(R.drawable.menu1_1);
//			btn[2].setBackgroundResource(R.drawable.menu2_1);
//			btn[3].setBackgroundResource(R.drawable.menu3_1);
//			btn[4].setBackgroundResource(R.drawable.menu4_1);
//			btn[5].setBackgroundResource(R.drawable.menu5_1);
//			btn[i].setOnClickListener((View.OnClickListener) this);
//	}

		report = (Button) findViewById(R.id.report);
		reportsByTime = (Button) findViewById(R.id.reportsFilteredByTime);

		report.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				report.setSelected(!report.isSelected());
				if(report.isSelected()) {
					report.setBackgroundResource(R.drawable.menu1_1);
				}
				else {
					report.setBackgroundResource(R.drawable.menu1);
				}
			}
		});

		reportsByTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == reportsByTime) {
					reportsByTime.setBackgroundResource(R.drawable.menu2_1);
				}
			}
		});

		ArrayList<NewsItem> image_details = getListData();
		final ListView lv1 = (ListView) findViewById(R.id.list_orders);
		lv1.setAdapter(new CustomListAdapter(this, image_details));
		lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = lv1.getItemAtPosition(position);
				NewsItem newsData = (NewsItem) o;
				Toast.makeText(MainActivity.this, "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
			}

		});

	}



	public  void onClick(View v){
		switch (v.getId()){
			case R.id.report :
				report.setBackgroundResource(R.drawable.menu1_1);
				break;
			case R.id.time :
				reportsByTime.setBackgroundResource(R.drawable.menu2_1);
				break;
			case R.id.write :
				write.setBackgroundResource(R.drawable.menu3_1);
				break;
		}
	}


	private ArrayList<NewsItem> getListData() {
		ArrayList<NewsItem> results = new ArrayList<NewsItem>();
		NewsItem newsData = new NewsItem();
		newsData.setIcClient(R.drawable.user_page1);
		newsData.setIcCommand(R.drawable.detalii_instalation);
		newsData.setIcLocation(R.drawable.location_page5);
		newsData.setClient("Dance");
		newsData.setCommand("Pankaj Gupta");
		newsData.setLocation("May 26, 2013, 13:35");
		newsData.setDay("24");
		newsData.setMonth("MAR");
		results.add(newsData);

		newsData = new NewsItem();
		newsData.setIcClient(R.drawable.user_page1);
		newsData.setIcCommand(R.drawable.detalii_instalation);
		newsData.setIcLocation(R.drawable.location__page2);
		newsData.setClient("Iaca");
		newsData.setCommand("Pankaj Gupta");
		newsData.setLocation("May 26, 2013, 13:35");
		newsData.setDay("24");
		newsData.setMonth("MAR");
		results.add(newsData);

		newsData = new NewsItem();
		newsData.setIcClient(R.drawable.user_page1);
		newsData.setIcCommand(R.drawable.detalii_instalation);
		newsData.setIcLocation(R.drawable.location__page2);
		newsData.setClient("Multa");
		newsData.setCommand("Pankaj Gupta");
		newsData.setLocation("May 26, 2013, 13:35");
		newsData.setDay("24");
		newsData.setMonth("MAR");
		results.add(newsData);

		newsData = new NewsItem();
		newsData.setIcClient(R.drawable.user_page1);
		newsData.setIcCommand(R.drawable.detalii_instalation);
		newsData.setIcLocation(R.drawable.location__page2);
		newsData.setClient("Psas");
		newsData.setCommand("Pankaj Gupta");
		newsData.setLocation("May 26, 2013, 13:35");
		newsData.setDay("24");
		newsData.setMonth("MAR");
		results.add(newsData);

		newsData = new NewsItem();
		newsData.setIcClient(R.drawable.user_page1);
		newsData.setIcCommand(R.drawable.detalii_instalation);
		newsData.setIcLocation(R.drawable.location__page2);
		newsData.setClient("Fac");
		newsData.setCommand("Pankaj Gupta");
		newsData.setLocation("May 26, 2013, 13:35");
		newsData.setDay("24");
		newsData.setMonth("MAR");
		results.add(newsData);

		return results;
	}
}
