package ru.alexangan.developer.geatech.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.alexangan.developer.geatech.Models.ItalianMonths;
import ru.alexangan.developer.geatech.Models.ReportsSearchResultItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;

public class ReportsSearchResultsListAdapter extends BaseAdapter
{
    private Context mContext;
    private List<ReportsSearchResultItem> lSearchResults;
    private int layout_id;
    //ViewHolder holder;

    public ReportsSearchResultsListAdapter(Context context, int layout_id, List<ReportsSearchResultItem> lSearchResults)
    {
        //super(context, textViewResourceId, objects);
        mContext = context;
        this.lSearchResults = lSearchResults;
        this.layout_id = layout_id;
    }

    @Override
    public int getCount()
    {
        return lSearchResults.size();
    }

    @Override
    public Object getItem(int i)
    {
        return i;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(layout_id, parent, false);

/*        View row = convertView;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout_id, parent, false);

            holder = new ViewHolder();

            row.setTag(holder);
        } else
        {
            row = convertView;

            holder = (ViewHolder) row.getTag();*/

        TextView tvVisitDay = (TextView) row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView) row.findViewById(R.id.tvVisitMonth);
        TextView tvVisitTime = (TextView) row.findViewById(R.id.tvVisitTime);
        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        TextView tvClientAddress = (TextView) row.findViewById(R.id.tvClientAddress);
        TextView tvClientPhones = (TextView) row.findViewById(R.id.tvClientPhones);
        TextView tvTypeOfService = (TextView) row.findViewById(R.id.tvTypeOfService);

        String strClientPhones = lSearchResults.get(position).getMobile() + " , " + lSearchResults.get(position).getPhone();
        tvClientPhones.setText(strClientPhones);

        String clientName = lSearchResults.get(position).getName();

        clientName = clientName.toLowerCase();
        String[] strArray = clientName.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray)
        {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }

        String strClientName = builder.toString();
        String strAddress = lSearchResults.get(position).getAddress();
        String searchString = mSettings.getString("reportSearchLastQueryString", "");
        String searchStrLower = searchString.toLowerCase();

        if (strClientName.toLowerCase().contains(searchStrLower))
        {
            SpannableString ssText = makeColoredString(strClientName, searchStrLower);
            clientNameTextView.setText(ssText);
        }
        else
        {
            clientNameTextView.setText(strClientName);
        }

        if (strClientPhones.toLowerCase().contains(searchStrLower))
        {
            SpannableString ssText = makeColoredString(strClientPhones, searchStrLower);
            tvClientPhones.setText(ssText);
        }
        else
        {
            tvClientPhones.setText(strClientPhones);
        }

        if (strAddress.toLowerCase().contains(searchStrLower))
        {
            SpannableString ssText = makeColoredString(strAddress, searchStrLower);
            tvClientAddress.setText(ssText);
        }
        else
        {
            tvClientAddress.setText(strAddress);
        }

        String productType = lSearchResults.get(position).getProduct_type();
        tvTypeOfService.setText(productType);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);
        String dataOraSopralluogo = lSearchResults.get(position).getData_ora_sopralluogo();

        if (dataOraSopralluogo != null)
        {
            Calendar calendar = Calendar.getInstance();

            try
            {
                calendar.setTime(sdf.parse(dataOraSopralluogo));

            } catch (ParseException e)
            {
                e.printStackTrace();
            }

            tvVisitDay.setVisibility(View.VISIBLE);
            tvVisitMonth.setVisibility(View.VISIBLE);

            tvVisitDay.setText(String.format(Locale.ITALIAN, "%d", calendar.get(Calendar.DAY_OF_MONTH)));
            tvVisitMonth.setText(ItalianMonths.numToString(calendar.get(Calendar.MONTH) + 1));

            String minuteStr = Integer.toString(calendar.get(Calendar.MINUTE));
            if (minuteStr.length() == 1)
            {
                minuteStr = "0" + minuteStr;
            }

            tvVisitTime.setText(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + minuteStr);
        }
        return row;
    }

    @NonNull
    private SpannableString makeColoredString(String strSource, String searchString)
    {
        String sourceStrLower = strSource.toLowerCase();

        int iStart = sourceStrLower.indexOf(searchString);
        int iEnd = iStart + searchString.length();

        Spannable spannable = new SpannableString(strSource);
        SpannableString ssText = new SpannableString(spannable);
        ssText.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffd100")), iStart, iEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssText;
    }

/*static class ViewHolder
{
    ImageView calendarioIcon;
    View vVisitDateView;
    TextView tvVisitDay;
    TextView tvVisitMonth;
    TextView tvVisitTime;
    ImageView ivPersonTimeSet;
    ImageView ivPersonTimeUnset;

    TextView clientNameTextView;
    TextView serviceTypeTextView;
    TextView clientAddressTextView;
}*/
}
