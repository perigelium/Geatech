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

public class FotovoltaicoReportFragment extends Fragment
{
    private int selectedIndex;
    int idSopralluogo;
    int id_rapporto_sopralluogo;
    ReportStates reportStates;
    View rootView;
    Context context;
    ViewUtils viewUtils;

    GeaModelloRapporto geaModello;

    public FotovoltaicoReportFragment()
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fotovoltaico_report, container, false);

        viewUtils = new ViewUtils(rootView, id_rapporto_sopralluogo, selectedIndex);

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        int idItem = viewUtils.getIdItemStart();


        idItem = viewUtils.createViewTwoRadiosAndEdit(idItem, R.id.two_radios_and_editFoto1);

        idItem = viewUtils.createViewEdit(idItem, R.id.editFoto1);

        EditText et = viewUtils.getEditTexts().get(idItem - 1);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);

        idItem = viewUtils.createViewFiveChkboxes(idItem, R.id.five_chkboxesFoto1);

        idItem = viewUtils.createViewFourRadiosAndEdit(idItem, R.id.four_radios_and_editFoto1);

        idItem = viewUtils.createViewFourRadiosAndEdit(idItem, R.id.four_radios_and_editFoto2);

        idItem = viewUtils.createViewFourEditsAndSwitch(idItem, R.id.four_edits_and_switchFoto1);

        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radiosFoto1);

        idItem = viewUtils.createViewThreeRadiosAndEditEx(idItem, R.id.three_radios_and_editFoto1);

        idItem = viewUtils.createViewFourRadios(idItem, R.id.four_radiosFoto1);

        idItem = viewUtils.createViewTwoSwitchesAndEdit(idItem, R.id.two_switches_and_editFoto1);

/*        EditText et2 = viewUtils.getEditTexts().get(idItem-1);
        et2.setVisibility(View.INVISIBLE);*/

        // SectionHeaderFoto1
        viewUtils.createViewSectionHeader(R.id.headerFoto1);

        idItem = viewUtils.createViewTwoTextsTwoEdits(idItem, R.id.two_texts_two_editsFoto1);

        // SectionHeaderFoto2
        viewUtils.createViewSectionHeader(R.id.headerFoto2);

        idItem = viewUtils.createViewFiveSwitches(idItem, R.id.five_switchesFoto1);

        // SectionHeaderFoto3
        viewUtils.createViewSectionHeader(R.id.headerFoto3);

        return viewUtils.getRootView();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (reportStates != null)
        {
            int idItem = viewUtils.getIdItemStart();

            idItem = viewUtils.saveSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralChkboxes(idItem);

            idItem = viewUtils.saveSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.saveSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 4);

            idItem = viewUtils.saveSeveralSwitches(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralSwitches(idItem, 2);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralEdits(idItem, 2);

            idItem = viewUtils.saveSeveralSwitches(idItem, 5);


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
            idItem = viewUtils.fillSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralChkboxes(idItem);

            idItem = viewUtils.fillSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.fillSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 4);

            idItem = viewUtils.fillSeveralSwitches(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralSwitches(idItem, 2);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralEdits(idItem, 2);

            idItem = viewUtils.fillSeveralSwitches(idItem, 5);
        }
    }

}
