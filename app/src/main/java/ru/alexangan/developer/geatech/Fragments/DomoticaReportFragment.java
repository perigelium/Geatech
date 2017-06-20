package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
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

public class DomoticaReportFragment extends Fragment
{
    private int selectedVisitId;
    int id_sopralluogo;
    int id_rapporto_sopralluogo;
    ReportItem reportItem;
    View rootView;
    Context context;
    ViewUtils viewUtils;

    GeaModelloRapporto geaModello;
    private Realm realm;

    public DomoticaReportFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        context = getActivity();

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

        if (geaModello == null)
        {
            return;
        }

        realm.beginTransaction();
        reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", id_sopralluogo).findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        id_rapporto_sopralluogo = reportItem != null ? reportItem.getGea_rapporto_sopralluogo().getId_rapporto_sopralluogo() : -1;

        realm.commitTransaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.domotica_report, container, false);

        viewUtils = new ViewUtils(context, rootView, id_rapporto_sopralluogo, selectedVisitId);

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        int idItem = viewUtils.getIdItemStart();

        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios1);

        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios2);

        idItem = viewUtils.createViewEdit(idItem, R.id.edit1);

        idItem = viewUtils.createViewTwoSwitches(idItem, R.id.two_switches1);

        idItem = viewUtils.createViewFourRadiosAndEdit(idItem, R.id.four_radios_and_edit1);

        idItem = viewUtils.createViewTwoEdits(idItem, R.id.two_edits1);

        idItem = viewUtils.createViewEdit(idItem, R.id.edit2);

        idItem = viewUtils.createViewSwitchAndEdit(idItem, R.id.switch_and_edit1);

        // SectionHeader1
        viewUtils.createViewSectionHeader(R.id.header1);

        idItem = viewUtils.createViewThreeTextsThreeEdits(idItem, R.id.three_texts_three_edits1);

        // SectionHeader2
        viewUtils.createViewSectionHeader(R.id.header2);


        return viewUtils.getRootView();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportItem != null)
        {
            int idItem = viewUtils.getIdItemStart();

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralSwitches(idItem, 2);

            idItem = viewUtils.saveSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 3);

            idItem = viewUtils.saveSeveralSwitches(idItem, 1);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralEdits(idItem, 3);

            
            // Completion state

            int completionState = DatabaseUtils.getReportInitializationState(id_sopralluogo, id_rapporto_sopralluogo);

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
            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralSwitches(idItem, 2);

            idItem = viewUtils.fillSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 3);

            final EditText et0 = viewUtils.getEditTexts().get(idItem-3);
            et0.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            final EditText et1 = viewUtils.getEditTexts().get(idItem-2);
            et1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            idItem = viewUtils.fillSeveralSwitches(idItem, 1);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            final Switch sw = viewUtils.getSwitches().get(idItem-2);
            final LinearLayout llEdit = viewUtils.getLinearLayouts().get(idItem-1).first;
            final EditText et2 = viewUtils.getEditTexts().get(idItem-1);
            et2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            if(sw.isChecked())
            {
                llEdit.setVisibility(View.GONE);
                llEdit.setEnabled(false);
            }
            else
            {
                llEdit.setVisibility(View.VISIBLE);
                llEdit.setEnabled(true);
            }

            sw.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (!sw.isChecked())
                    {
                        llEdit.setVisibility(View.VISIBLE);
                        llEdit.setEnabled(true);
                        et2.setText("");
                    }
                    else
                    {
                        llEdit.setVisibility(View.GONE);
                        llEdit.setEnabled(false);
                        et2.setText(getString(R.string.NotApplicable));
                    }
                }
            });

            idItem = viewUtils.fillSeveralEdits(idItem, 3);

            if(reportItem !=null && reportItem.getReportStates().hasTriedToSendReport())
            {
                viewUtils.markSectionsWithNotFilledItems(id_sopralluogo, id_rapporto_sopralluogo);
            }
        }
    }
}
