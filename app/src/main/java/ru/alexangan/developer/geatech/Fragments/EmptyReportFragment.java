package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.DatabaseUtils;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class EmptyReportFragment extends Fragment
{
    private int selectedVisitId;
    int id_sopralluogo;
    int id_rapporto_sopralluogo;
    ReportItem reportItem;
    View rootView;
    Context context;
    ViewUtils viewUtils;

    GeaModelloRapporto geaModello;
    

    public EmptyReportFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getActivity();
        Realm realm = Realm.getDefaultInstance();

        if (getArguments() != null)
        {
            selectedVisitId = getArguments().getInt("selectedVisitId");
        }

        VisitItem visitItem = visitItems.get(selectedVisitId);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        id_sopralluogo = geaSopralluogo.getId_sopralluogo();
        int id_product_type = productData.getIdProductType();

        realm.beginTransaction();
        geaModello = realm.where(GeaModelloRapporto.class).equalTo("id_product_type", id_product_type).findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", id_sopralluogo).findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        id_rapporto_sopralluogo = reportItem != null ? reportItem.getGea_rapporto_sopralluogo().getId_rapporto_sopralluogo() : -1;

        realm.commitTransaction();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.empty_report, container, false);

        viewUtils = new ViewUtils(context, rootView, id_rapporto_sopralluogo, selectedVisitId);

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);

        if(geaModello != null)
        {
            tvReportTitle.setText(geaModello.getNome_modello());
        }

        int idItem = viewUtils.getIdItemStart();



/*        EditText et = viewUtils.getEditTexts().get(idItem - 1);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);*/

        // SectionHeader1
        //viewUtils.createViewSectionHeader(R.id.header1);



        // SectionHeader2
        //viewUtils.createViewSectionHeader(R.id.header2);


        return viewUtils.getRootView();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportItem != null)
        {
            int idItem = viewUtils.getIdItemStart();

            
            // Completion state

            int completionState = DatabaseUtils.getReportInitializationState(id_sopralluogo, id_rapporto_sopralluogo);

            Realm realm = Realm.getDefaultInstance();
            if (completionState == ReportStates.REPORT_COMPLETED)
            {
                realm.beginTransaction();

                Calendar calendarNow = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                String strDateTime = sdf.format(calendarNow.getTime());

                reportItem.getGea_rapporto_sopralluogo().setData_ora_compilazione_rapporto(strDateTime);

                realm.commitTransaction();
            }

            realm.beginTransaction();
            reportItem.getReportStates().setReportCompletionState(completionState);
            realm.commitTransaction();
            realm.close();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        fillViewItems();
    }

    private void fillViewItems()
    {
        int idItem = viewUtils.getIdItemStart();

        if (id_rapporto_sopralluogo != -1)
        {

            if(reportItem !=null && reportItem.getReportStates().hasTriedToSendReport())
            {
                viewUtils.markSectionsWithNotFilledItems(id_sopralluogo, id_rapporto_sopralluogo);
            }
        }
    }

}
