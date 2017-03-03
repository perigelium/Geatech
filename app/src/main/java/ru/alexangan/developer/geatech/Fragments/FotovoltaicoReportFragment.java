package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
    private int selectedIndex, idItemStart, idItemEnd;
    int idSopralluogo;
    int id_rapporto_sopralluogo;
    ReportStates reportStates;
    View rootView;
    Context context;
    ViewUtils viewUtils;

    GeaModelloRapporto geaModello;
/*    List<GeaSezioneModelliRapporto> geaSezioniModelli;
    List<GeaItemModelliRapporto> geaItemModelli;*/

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

        if(geaModello == null)
        {
            return;
        }

        realm.beginTransaction();

        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

        id_rapporto_sopralluogo = reportStates!=null ? reportStates.getId_rapporto_sopralluogo() : -1;

        realm.commitTransaction();

/*        realm.beginTransaction();
        geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class)
                .equalTo("id_modello", geaModello.getId_modello()).findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        geaItemModelli = realm.where(GeaItemModelliRapporto.class)
                .between("id_sezione", geaSezioniModelli.get(0).getId_sezione(), geaSezioniModelli.get(geaSezioniModelli.size() - 1).getId_sezione())
                .findAll();
        realm.commitTransaction();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fotovoltaico_report, container, false);

        viewUtils = new ViewUtils(rootView, id_rapporto_sopralluogo, selectedIndex);

        int sectionNumber = 0;

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());


        viewUtils.createViewTwoRadiosAndEdit(sectionNumber++, R.id.two_radios_and_edit1);

        viewUtils.createViewEdit(sectionNumber++, R.id.edit1);

        viewUtils.createViewFiveChkboxes(sectionNumber++, R.id.five_chkboxes1);

        viewUtils.createViewFourRadiosAndEdit(sectionNumber++, R.id.four_radios_and_edit1);

        viewUtils.createViewFourRadiosAndEdit(sectionNumber++, R.id.four_radios_and_edit2);

        sectionNumber = viewUtils.createViewFourEditsAndSwitch(sectionNumber, R.id.four_edits_and_switch1);

        viewUtils.createViewTwoRadios(sectionNumber++, R.id.two_radios1);

        sectionNumber++;
        sectionNumber++;

        viewUtils.createViewFourRadios(sectionNumber++, R.id.four_radios1);

        sectionNumber = viewUtils.createViewTwoSwitchesAndEdit(sectionNumber, R.id.two_switches_and_edit1);

        // SectionHeader1
        viewUtils.createViewSectionHeader(R.id.header1);

        sectionNumber = viewUtils.createViewTwoTextsTwoEdits(sectionNumber, R.id.two_texts_two_edits1);

        // SectionHeader2
        viewUtils.createViewSectionHeader(R.id.header2);

        sectionNumber = viewUtils.createViewFiveSwitches(sectionNumber, R.id.five_switches1);

        // SectionHeader3
        viewUtils.createViewSectionHeader(R.id.header3);

        return viewUtils.getRootView();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportStates != null)
        {
            int sectionNumber = 0;

            viewUtils.saveSeveralRadios(R.id.two_radios_and_edit1, sectionNumber++);

            sectionNumber = viewUtils.saveSeveralEdits(R.id.two_radios_and_edit1, sectionNumber);

            sectionNumber = viewUtils.saveSeveralEdits(R.id.edit1, sectionNumber);

            sectionNumber = viewUtils.saveSeveralChkboxes(R.id.five_chkboxes1, sectionNumber);

            viewUtils.saveSeveralRadios(R.id.four_radios_and_edit1, sectionNumber++);

            sectionNumber = viewUtils.saveSeveralEdits(R.id.four_radios_and_edit1, sectionNumber);

            viewUtils.saveSeveralRadios(R.id.four_radios_and_edit2, sectionNumber++);

            sectionNumber = viewUtils.saveSeveralEdits(R.id.four_radios_and_edit2, sectionNumber);

            sectionNumber = viewUtils.saveSeveralEdits(R.id.four_edits_and_switch1, sectionNumber);

            sectionNumber = viewUtils.saveSeveralSwitches(R.id.four_edits_and_switch1, sectionNumber);

            viewUtils.saveSeveralRadios(R.id.two_radios, sectionNumber++);

            sectionNumber++;
            sectionNumber++;

            viewUtils.saveSeveralRadios(R.id.four_radios1, sectionNumber++);

            sectionNumber = viewUtils.saveSeveralSwitches(R.id.two_switches_and_edit1, sectionNumber);

            sectionNumber = viewUtils.saveSeveralEdits(R.id.two_switches_and_edit1, sectionNumber);

            sectionNumber = viewUtils.saveSeveralEdits(R.id.two_texts_two_edits1, sectionNumber);

            sectionNumber = viewUtils.saveSeveralSwitches(R.id.five_switches1, sectionNumber);


            // Completion state
            int completionState = DatabaseUtils.getReportInitializationState(id_rapporto_sopralluogo, idItemStart, idItemEnd);

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
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        int idItem = idItemStart;

        if(id_rapporto_sopralluogo != -1)
        {






        }
    }
}
