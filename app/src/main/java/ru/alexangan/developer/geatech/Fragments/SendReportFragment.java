package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Activities.LoginActivity;
import ru.alexangan.developer.geatech.Activities.MainActivity;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Interfaces.RESTdataReceiverEventListener;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.Network.RESTdataReceiver;
import ru.alexangan.developer.geatech.R;

import static android.content.Context.RESTRICTIONS_SERVICE;
import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.visitItems;

public class SendReportFragment extends Fragment implements View.OnClickListener, Callback
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    private Communicator mCommunicator;

    private Button sendReport;
    private int selectedIndex;
    ReportStates reportStates;
    VisitItem visitItem;

    String reportSendResponse;
    Call callSendReport;
    Activity activity;

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String REST_URL = "http://www.bludelego.com/dev/geatech/api.php";
    private final String DATA_URL_SUFFIX = "?case=set_data_supraluogo";

    public SendReportFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SendReportFragment newInstance(String param1, String param2)
    {
        SendReportFragment fragment = new SendReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.send_report_fragment, container, false);

        sendReport = (Button) rootView.findViewById(R.id.btnSendReport);
        sendReport.setOnClickListener(this);

        visitItem = visitItems.get(selectedIndex);
        VisitStates visitStates = visitItem.getVisitStates();
        int idSopralluogo = visitStates.getIdSopralluogo();

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if (reportStates != null)
        {
            if (visitItem.getVisitStates().getIdSopralluogo() == reportStates.getIdSopralluogo()
                    && (reportStates.getGeneralInfoCompletionState() == 2 && reportStates.getReportCompletionState() == 3)
                    && reportStates.getPhotoAddedNumber() >= 3 && reportStates.getDataOraRaportoInviato() == null)
            {
                sendReport.setAlpha(1.0f);
                sendReport.setEnabled(true);
            } else
            {
                sendReport.setAlpha(.4f);
                sendReport.setEnabled(false);
            }


            String generalInfoCompletionState = reportStates.getGeneralInfoCompletionStateString(reportStates.getGeneralInfoCompletionState()).Value();
            String reportCompletionState = reportStates.getReportCompletionStateString(reportStates.getReportCompletionState()).Value();

            int photoAddedNumber = reportStates.getPhotoAddedNumber();
            String photoAddedNumberStr;

            if (photoAddedNumber == 0)
            {
                photoAddedNumberStr = reportStates.getPhotoAddedNumberString(photoAddedNumber).Value();
            } else
            {
                photoAddedNumberStr = photoAddedNumber + reportStates.getPhotoAddedNumberString(photoAddedNumber).Value();
            }

            TextView tvPhotosPresent = (TextView) rootView.findViewById(R.id.tvPhotosQuant);
            tvPhotosPresent.setText(photoAddedNumberStr);

            TextView tvGeneralInfo = (TextView) rootView.findViewById(R.id.tvGeneralInfo);
            tvGeneralInfo.setText(generalInfoCompletionState);

            TextView tvTecnicalReportState = (TextView) rootView.findViewById(R.id.tvTecnicalReportState);
            tvTecnicalReportState.setText(reportCompletionState);
        }

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnSendReport)
        {
            if (reportStates != null)
            {
                sendReport.setAlpha(.4f);
                sendReport.setEnabled(false);

                Calendar calendarNow = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                String strDateTime = sdf.format(calendarNow.getTime());

                realm.beginTransaction();
                reportStates.setDataOraRaportoInviato(strDateTime);
                realm.commitTransaction();


                reportStates = realm.copyFromRealm(reportStates);

                Gson gson = new Gson();
                String json = gson.toJson(reportStates);

                Log.d("DEBUG", json);

                //sendReport(json);
                NetworkUtils networkUtils = new NetworkUtils();
                callSendReport = networkUtils.sendReport(this, json);

                Toast.makeText(getActivity(), "Rapporto inviato", Toast.LENGTH_LONG).show();

                mCommunicator.onSendReportReturned();

            }
        }
    }

    public void sendReport(String gsonStr)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = defaultHttpClient.build();

        RequestBody body = RequestBody.create(JSON, gsonStr);

        Request request = new Request.Builder()
                .url(REST_URL + DATA_URL_SUFFIX)
                .post(body)
                .build();

        callSendReport = okHttpClient.newCall(request);
        callSendReport.enqueue(this);
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callSendReport)
        {
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, "Send report data failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callSendReport)
        {
            reportSendResponse = response.body().string();

            if (reportSendResponse == null)
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Send report data response does not received", Toast.LENGTH_LONG).show();
                    }
                });

                return;
            } else
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, reportSendResponse, Toast.LENGTH_LONG).show();
                    }
                });
            }

            mCommunicator.onSendReportReturned();
        }
    }
}
