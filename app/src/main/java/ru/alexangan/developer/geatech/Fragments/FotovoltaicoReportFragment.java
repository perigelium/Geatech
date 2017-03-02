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

public class FotovoltaicoReportFragment extends Fragment implements View.OnClickListener
{
    private int selectedIndex, idItemStart, idItemEnd;
    int idSopralluogo;
    int id_rapporto_sopralluogo;
    ReportStates reportStates;
    View rootView;
    Context context;
    private boolean allSections1Collapsed, allSections2Collapsed;
    ViewUtils viewUtils;

    GeaModelloRapporto geaModello;
    List<GeaSezioneModelliRapporto> geaSezioniModelli;
    List<GeaItemModelliRapporto> geaItemModelli;

/*    ArrayList <FrameLayout> FrameLayouts;
    Map <String, ArrayList<ImageView>> ImageViews;
    Map <String, ArrayList <Pair<LinearLayout, LinearLayout>>> LinearLayouts;
    Map <String, ArrayList<Pair<RadioGroup, RadioGroup>>> RadioGroups;
    Map <String, ArrayList<ArrayList<EditText>>> EditTexts;
    Map <String, ArrayList<ArrayList<Switch>>> Switches;
    Map <String, ArrayList<ArrayList<CheckBox>>> CheckBoxes;

    ArrayList <Pair<LinearLayout, LinearLayout>> al_llPair;
    ArrayList <Pair<RadioGroup, RadioGroup>> al_rgPair;
    ArrayList<ArrayList<EditText>> al_al_Edits;
    ArrayList<ArrayList<Switch>> al_al_Switches;
    ArrayList<ImageView> al_al_ImageViews;
    ArrayList<ArrayList<CheckBox>> al_al_CheckBoxes;*/

/*    private FrameLayout flSectionHeader1, flSectionHeader2;
    
    private LinearLayout llHeaderThreeRadiosAndEdit1, llSectionThreeRadiosAndEdit1, llHeaderTwoRadios1, llSectionTwoRadios1,
            llHeaderTwoRadios2, llSectionTwoRadios2, llHeaderFourRadiosAndEdit1, llSectionFourRadiosAndEdit1, 
            llHeaderThreeChkboxesAndEdit1, llSectionThreeChkboxesAndEdit1, llSectionThreeTextThreeEdit1, llHeaderTwoEdits1,
            llHeaderEdit1, llHeaderTwoSwitches1, llHeaderEdit2, llHeaderSwitchAndEdit1,
            llSectionEdit1, llSectionEdit2, llSectionTwoSwitches1, llSectionTwoEdits1, llSectionSwitchAndEdit1,
            llSectionFourRadiosAndEdit2, llHeaderTwoRadiosAndEdit1, llHeaderFourRadiosAndEdit2, llSectionTwoRadiosAndEdit1,
            llHeaderFourRadios1, llSectionFourRadios1, llSectionTwoTextsTwoEdits1, llHeaderEditAndFiveChkboxes1,
            llSectionEditAndFiveChkboxes1;

    private RadioGroup rg1ThreeRadiosAndEdit1, rg1TwoRadios1, rg1TwoRadios2, rg1FourRadiosAndEdit1, rg1FourRadiosAndEdit2,
            rg1TwoRadiosAndEdit1, rg1FourRadios1;

    private EditText et1ThreeRadiosAndEdit1, et1FourRadiosAndEdit1, et1ThreeChkboxesAndEdit1, et1ThreeTextThreeEdit1, 
            et2ThreeTextThreeEdit1, et3ThreeTextThreeEdit1, et1Edit1, et1Edit2, et1SwitchAndEdit1, et1TwoEdits1, et2TwoEdits1, 
            et1FourRadiosAndEdit2, et1TwoRadiosAndEdit1, et1TwoTextTwoEdit1, et2TwoTextTwoEdit1, et1EditAndFiveChkboxes1;

    Switch sw1TwoSwitches1, sw2TwoSwitches1, sw1SwitchAndEdit1;

    ImageView ivArrowTwoRadios1, ivArrowTwoRadios2, ivArrowThreeRadiosAndEdit1, ivArrowFourRadiosAndEdit1, ivArrowThreeChkboxesAndEdit1,
            ivArrowEdit1, ivArrowEdit2, ivArrowTwoSwitches1, ivArrowTwoEdits1, ivArrowSwitchAndEdit1, ivArrowFourRadiosAndEdit2,
            ivArrowTwoRadiosAndEdit1, ivArrowFourRadios1, ivArrowEditAndFiveChkboxes1;

    CheckBox chk1EditAndFiveChkboxes1, chk2EditAndFiveChkboxes1, chk3EditAndFiveChkboxes1, chk4EditAndFiveChkboxes1,
            chk5EditAndFiveChkboxes1;*/

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

        allSections1Collapsed = false;
        allSections2Collapsed = false;

        //idItemStart = 48;
        //idItemEnd = 72; // first not included id

/*        FrameLayouts = new ArrayList<>();
        LinearLayouts = new HashMap<>();
        RadioGroups = new HashMap<>();
        EditTexts = new HashMap<>();
        Switches = new HashMap<>();
        ImageViews = new HashMap<>();
        CheckBoxes = new HashMap<>();

        al_llPair = new ArrayList<>();
        al_rgPair = new ArrayList<>();
        al_al_Edits = new ArrayList<>();
        al_al_Switches = new ArrayList<>();
        al_al_ImageViews = new ArrayList<>();
        al_al_CheckBoxes = new ArrayList<>();*/

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
        geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class)
                .equalTo("id_modello", geaModello.getId_modello()).findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        geaItemModelli = realm.where(GeaItemModelliRapporto.class)
                .between("id_sezione", geaSezioniModelli.get(0).getId_sezione(), geaSezioniModelli.get(geaSezioniModelli.size() - 1).getId_sezione())
                .findAll(); //.between("id_item_modello", idItemStart, idItemEnd)
        realm.commitTransaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fotovoltaico_report, container, false);

        viewUtils = new ViewUtils(rootView, selectedIndex);

        int headerNumber = 0;
        int sectionNumber = 0;

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        // SectionHeader1
        viewUtils.createViewSectionHeader(headerNumber++, R.id.header1);

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

        // SectionHeader2
        viewUtils.createViewSectionHeader(headerNumber++, R.id.header2);

        sectionNumber = viewUtils.createViewTwoTextsTwoEdits(sectionNumber, R.id.two_texts_two_edits1);

        // SectionHeader3
        viewUtils.createViewSectionHeader(headerNumber++, R.id.header3);

        sectionNumber = viewUtils.createViewFiveSwitches(sectionNumber, R.id.five_switches1);

        return viewUtils.getRootView();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportStates != null)
        {
            int id_item = idItemStart;






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

        realm.beginTransaction();

        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

        id_rapporto_sopralluogo = reportStates!=null ? reportStates.getId_rapporto_sopralluogo() : -1;

        realm.commitTransaction();

        int idItem = idItemStart;

        if(id_rapporto_sopralluogo != -1)
        {

        }
    }

    @Override
    public void onClick(View view)
    {
        //if (view == flSectionHeader1)
        {
/*            llSectionThreeRadiosAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionTwoRadios1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionFourRadiosAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionThreeChkboxesAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);*/

            allSections1Collapsed = !allSections1Collapsed;
        }

        //if (view == flSectionHeader2)
        {
/*            llSectionThreeTextThreeEdit1.setVisibility(!allSections2Collapsed ? View.GONE : View.VISIBLE);*/

            allSections2Collapsed = !allSections2Collapsed;
        }

    }
}
