package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.DatabaseUtils;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class PompaDiCaloreReportFragment extends Fragment
{
    private int selectedIndex;
    int idSopralluogo;
    int id_rapporto_sopralluogo;
    ReportStates reportStates;
    View rootView;
    Context context;
    ViewUtils viewUtils;

    GeaModelloRapporto geaModello;

    public PompaDiCaloreReportFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getActivity();

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }

        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();
        int id_product_type = productData.getIdProductType();

        realm.beginTransaction();
        geaModello = realm.where(GeaModelloRapporto.class).equalTo("id_product_type", id_product_type).findFirst();
        realm.commitTransaction();

        if (geaModello == null)
        {
            return;
        }

        realm.beginTransaction();

        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

        id_rapporto_sopralluogo = reportStates != null ? reportStates.getId_rapporto_sopralluogo() : -1;

        realm.commitTransaction();
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
        rootView = inflater.inflate(R.layout.pompadicalore_report, container, false);

        viewUtils = new ViewUtils(rootView, id_rapporto_sopralluogo, selectedIndex);

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        int idItem = viewUtils.getIdItemStart();


        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios1);

        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios2);
        idItem = viewUtils.createViewEdit(idItem, R.id.edit1);
        idItem = viewUtils.createViewEdit(idItem, R.id.edit2);

        EditText et0 = viewUtils.getEditTexts().get(idItem-1);
        et0.setInputType(InputType.TYPE_CLASS_NUMBER);

        idItem = viewUtils.createViewFourRadios(idItem, R.id.four_radios1);

        // SectionHeader1
        viewUtils.createViewSectionHeader(R.id.header1);

        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios3);

        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios1);

        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios2);


        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios3);
        idItem = viewUtils.createViewEdit(idItem, R.id.edit3);

        EditText et1 = viewUtils.getEditTexts().get(idItem-1);
        et1.setInputType(InputType.TYPE_CLASS_NUMBER);

        idItem = viewUtils.createViewSwitch(idItem, R.id.switch_1);


        idItem = viewUtils.createViewFourRadios(idItem, R.id.four_radios2);
        idItem = viewUtils.createViewEdit(idItem, R.id.edit4);

        EditText et2 = viewUtils.getEditTexts().get(idItem-1);
        et2.setInputType(InputType.TYPE_CLASS_NUMBER);

        idItem = viewUtils.createViewTwoEdits(idItem, R.id.two_edits1);

        EditText et3 = viewUtils.getEditTexts().get(idItem-2);
        et3.setInputType(InputType.TYPE_CLASS_NUMBER);

        idItem = viewUtils.createViewEdit(idItem, R.id.edit5);

        EditText et4 = viewUtils.getEditTexts().get(idItem-1);
        et4.setInputType(InputType.TYPE_CLASS_NUMBER);

        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios4);


        // SectionHeader2
        viewUtils.createViewSectionHeader(R.id.header2);

        idItem = viewUtils.createViewFourRadios(idItem, R.id.four_radios3);

        idItem = viewUtils.createViewEdit(idItem, R.id.edit6);

        EditText et5 = viewUtils.getEditTexts().get(idItem-1);
        et5.setInputType(InputType.TYPE_CLASS_NUMBER);


        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios4);

        idItem = viewUtils.createViewSwitchAndEdit(idItem, R.id.switch_and_edit1);


        // SectionHeader3
        viewUtils.createViewSectionHeader(R.id.header3);


        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios5);
        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios5);
        idItem = viewUtils.createViewEdit(idItem, R.id.edit7);

        EditText et6 = viewUtils.getEditTexts().get(idItem-1);
        et6.setInputType(InputType.TYPE_CLASS_NUMBER);

        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios6);
        idItem = viewUtils.createViewEdit(idItem, R.id.edit8);

        EditText et7 = viewUtils.getEditTexts().get(idItem-1);
        et7.setInputType(InputType.TYPE_CLASS_NUMBER);

        idItem = viewUtils.createViewChkboxAndEdit(idItem, R.id.chkbox_and_edit1);

        idItem = viewUtils.createViewChkboxAndEdit(idItem, R.id.chkbox_and_edit2);

        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios7);

        idItem = viewUtils.createViewThreeRadios(idItem, R.id.three_radios8);

        // SectionHeader4
        viewUtils.createViewSectionHeader(R.id.header4);


        return viewUtils.getRootView();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportStates != null)
        {
            int idItem = viewUtils.getIdItemStart();


            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralSwitches(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 4);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralSwitches(idItem, 1);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveChkboxAndEdit(idItem);

            idItem = viewUtils.saveChkboxAndEdit(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);
            

            // Completion state

            int completionState = DatabaseUtils.getReportInitializationState(id_rapporto_sopralluogo);

            if (completionState == 3)
            {
                realm.beginTransaction();

                Calendar calendarNow = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                String strDateTime = sdf.format(calendarNow.getTime());

                reportStates.setDataOraRaportoCompletato(strDateTime);

                realm.commitTransaction();
            }

            realm.beginTransaction();
            reportStates.setReportCompletionState(completionState);
            realm.commitTransaction();
        }
    }

    private void fillViewItems()
    {
        int idItem = viewUtils.getIdItemStart();

        if (id_rapporto_sopralluogo != -1)
        {
            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralSwitches(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 4);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralSwitches(idItem, 1);
            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            final EditText et3 = viewUtils.getEditTexts().get(idItem-1);
            et3.setInputType(InputType.TYPE_CLASS_NUMBER);
            final Switch sw1 = viewUtils.getSwitches().get(idItem-2);
            final LinearLayout llEdit1 = viewUtils.getLinearLayouts().get(idItem-1).first;

            if(!sw1.isChecked())
            {
                llEdit1.setVisibility(View.GONE);
                llEdit1.setEnabled(false);
            }
            else
            {
                llEdit1.setVisibility(View.VISIBLE);
                llEdit1.setEnabled(true);
            }

            sw1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (sw1.isChecked())
                    {
                        llEdit1.setVisibility(View.VISIBLE);
                        llEdit1.setEnabled(true);
                        et3.setText("");
                    }
                    else
                    {
                        llEdit1.setVisibility(View.GONE);
                        llEdit1.setEnabled(false);
                        et3.setText("Non applicabile");
                    }
                }
            });


            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillChkboxAndEdit(idItem);

            idItem = viewUtils.fillChkboxAndEdit(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);
        }
    }
}
