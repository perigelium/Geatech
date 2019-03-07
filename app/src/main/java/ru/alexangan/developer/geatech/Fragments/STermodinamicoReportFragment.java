package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
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

public class STermodinamicoReportFragment extends Fragment
{
    private int selectedVisitId;
    int id_sopralluogo;
    int id_rapporto_sopralluogo;
    ReportItem reportItem;
    View rootView;
    Context context;
    ViewUtils viewUtils;

    GeaModelloRapporto geaModello;

    public STermodinamicoReportFragment()
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

        if (geaModello == null)
        {
            return;
        }

        realm.beginTransaction();
        reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", id_sopralluogo).findFirst();
        realm.commitTransaction();

        realm.beginTransaction();

        id_rapporto_sopralluogo = reportItem != null ? reportItem.getGea_rapporto_sopralluogo().getId_rapporto_sopralluogo() : 0;
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        fillViewItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.termodinamico_report, container, false);

        viewUtils = new ViewUtils(context, rootView, id_rapporto_sopralluogo, selectedVisitId);

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        int idItem = viewUtils.getIdItemStart();

        idItem = viewUtils.createViewThreeRadiosAndEdit(idItem, R.id.three_radios_and_edit1);

        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios1);

        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios2);

        idItem = viewUtils.createViewThreeEdits(idItem, R.id.three_edits1);

        idItem = viewUtils.createViewThreeEdits(idItem, R.id.three_edits2);

        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios1);

        idItem = viewUtils.createViewEdit(idItem, R.id.edit1);

        // SectionHeader1
        viewUtils.createViewSectionHeader(R.id.header1);

        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios2);

        idItem = viewUtils.createViewFourCheckboxes(idItem, R.id.four_chkboxes1);

        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios3);

        idItem = viewUtils.createViewSwitch(idItem, R.id.switch_1);

        idItem = viewUtils.createViewFiveRadios(idItem, R.id.five_radios1);

        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios3);

        idItem = viewUtils.createViewSwitch(idItem, R.id.switch_2);

        idItem = viewUtils.createViewTwoRadiosAndSwitch(idItem, R.id.two_radios_and_switch1);

        // SectionHeader2
        viewUtils.createViewSectionHeader(R.id.header2);

        idItem = viewUtils.createViewTwoTextsTwoEdits(idItem, R.id.two_texts_two_edits1);

        // SectionHeader3
        viewUtils.createViewSectionHeader(R.id.header3);

        idItem = viewUtils.createViewFiveSwitches(idItem, R.id.five_switches1);

        // SectionHeader4
        viewUtils.createViewSectionHeader(R.id.header4);

        return viewUtils.getRootView();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportItem != null)
        {
            int idItem = viewUtils.getIdItemStart();

            idItem = viewUtils.saveSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 3);

            idItem = viewUtils.saveSeveralEdits(idItem, 3);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralChkboxes(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralSwitches(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralSwitches(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralSwitches(idItem, 1);

            idItem = viewUtils.saveSeveralEdits(idItem, 2);

            idItem = viewUtils.saveSeveralSwitches(idItem, 5);


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

    private void fillViewItems()
    {
        int idItem = viewUtils.getIdItemStart();

        if (id_rapporto_sopralluogo != -1)
        {
            idItem = viewUtils.fillSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 3);

            final EditText et0 = viewUtils.getEditTexts().get(idItem-3);
            et0.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            final EditText et1 = viewUtils.getEditTexts().get(idItem-2);
            et1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            final EditText et2 = viewUtils.getEditTexts().get(idItem-1);
            et2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            idItem = viewUtils.fillSeveralEdits(idItem, 3);

            final EditText et3 = viewUtils.getEditTexts().get(idItem-3);
            et3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralChkboxes(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralSwitches(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralSwitches(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);
            idItem = viewUtils.fillSeveralSwitches(idItem, 1);

            final Switch sw1TwoRadiosAndSwitch = viewUtils.getSwitches().get(idItem - 3);
            final Pair<LinearLayout, LinearLayout> llPair = viewUtils.getLinearLayouts().get(idItem - 2);

            sw1TwoRadiosAndSwitch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (sw1TwoRadiosAndSwitch.isChecked())
                    {
                        llPair.first.setVisibility(View.VISIBLE);
                        llPair.second.setVisibility(View.VISIBLE);
                        llPair.first.setEnabled(true);
                        llPair.second.setEnabled(true);
                    } else
                    {
                        llPair.first.setVisibility(View.GONE);
                        llPair.second.setVisibility(View.GONE);
                        llPair.first.setEnabled(false);
                        llPair.second.setEnabled(false);
                    }
                }
            });

            idItem = viewUtils.fillSeveralEdits(idItem, 2);

            idItem = viewUtils.fillSeveralSwitches(idItem, 5);

            if (reportItem != null && reportItem.getReportStates().hasTriedToSendReport())
            {
                viewUtils.markSectionsWithNotFilledItems(id_sopralluogo, id_rapporto_sopralluogo);
            }

            if (sw1TwoRadiosAndSwitch.isChecked())
            {
                llPair.first.setVisibility(View.VISIBLE);
                llPair.second.setVisibility(View.VISIBLE);
                llPair.first.setEnabled(true);
                llPair.second.setEnabled(true);
            } else
            {
                llPair.first.setVisibility(View.GONE);
                llPair.second.setVisibility(View.GONE);
                llPair.first.setEnabled(false);
                llPair.second.setEnabled(false);
            }
        }
    }
}
