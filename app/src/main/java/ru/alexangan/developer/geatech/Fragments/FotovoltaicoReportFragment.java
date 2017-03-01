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

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class FotovoltaicoReportFragment extends Fragment implements View.OnClickListener
{
    private int selectedIndex;
    int idSopralluogo;
    int id_rapporto_sopralluogo, idItemStart, idItemEnd;
    ReportStates reportStates;
    View rootView;
    Context context;
    private boolean allSections1Collapsed, allSections2Collapsed;
    GeaModelloRapporto geaModello;
    List<GeaSezioneModelliRapporto> geaSezioniModelli;
    List<GeaItemModelliRapporto> geaItemModelli;

    ArrayList <FrameLayout> FrameLayouts;
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
    ArrayList<ArrayList<CheckBox>> al_al_CheckBoxes;

    private FrameLayout flSectionHeader1, flSectionHeader2;
    
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
            chk5EditAndFiveChkboxes1;

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

        idItemStart = 48;
        idItemEnd = 72; // first not included id

        FrameLayouts = new ArrayList<>();
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
        al_al_CheckBoxes = new ArrayList<>();

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
                .between("id_item_modello", idItemStart, idItemEnd).findAll();
        realm.commitTransaction();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fotovoltaico_report, container, false);

        int headerNumber = 0;
        int sectionNumber = 0;

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        // SectionHeader1
        createViewSectionHeader(headerNumber++, R.id.header1);

        createViewTwoRadiosAndEdit(sectionNumber++, R.id.two_radios_and_edit1);

        sectionNumber = createViewEditAndFiveChkboxes(sectionNumber, R.id.edit_and_five_chkboxes1);

        createViewFourRadiosAndEdit(sectionNumber++, R.id.four_radios_and_edit1);

        createViewFourRadiosAndEdit(sectionNumber++, R.id.four_radios_and_edit2);

        //sectionNumber = createViewFourEditsAndSwitch1(sectionNumber);

        createViewTwoRadios(sectionNumber++, R.id.two_radios1);

        createViewFourRadios(sectionNumber++, R.id.four_radios1);

        sectionNumber = createViewTwoSwitches(sectionNumber, R.id.two_switches1);

        // SectionHeader2
        createViewSectionHeader(headerNumber++, R.id.header2);

        sectionNumber = createViewTwoTextsTwoEdits(sectionNumber, R.id.two_texts_two_edits1);

        // SectionHeader3
        createViewSectionHeader(headerNumber++, R.id.header3);

        //sectionNumber = createViewFiveSwitches1(sectionNumber);

        return rootView;
    }

    private int createViewEditAndFiveChkboxes(int sectionNumber, int idRes)
    {
        LinearLayout edit_and_five_chkboxes = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderEditAndFiveChkboxes = (LinearLayout) edit_and_five_chkboxes.findViewById(R.id.llHeaderEditAndFiveChkboxes);
        llHeaderEditAndFiveChkboxes.setOnClickListener(this);

        ImageView ivArrowEditAndFiveChkboxes = (ImageView) edit_and_five_chkboxes.findViewById(R.id.ivArrowEditAndFiveChkboxes);

        al_al_ImageViews.add(ivArrowEditAndFiveChkboxes);
        ImageViews.put("EditAndFiveChkboxes", al_al_ImageViews);

        TextView tvHeaderEditAndFiveChkboxes = (TextView) edit_and_five_chkboxes.findViewById(R.id.tvHeaderEditAndFiveChkboxes);
        tvHeaderEditAndFiveChkboxes.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        LinearLayout llSectionEditAndFiveChkboxes = (LinearLayout) edit_and_five_chkboxes.findViewById(R.id.llSectionEditAndFiveChkboxes);

        al_llPair.add(new Pair<>(llHeaderEditAndFiveChkboxes, llSectionEditAndFiveChkboxes));
        LinearLayouts.put("EditAndFiveChkboxes", al_llPair);

/*        ArrayList <Pair<LinearLayout,LinearLayout>> al = LinearLayouts.get("EditAndFiveChkboxes");
        LinearLayout l = al.get(serialNumber).first;*/

        EditText et1EditAndFiveChkboxes1 = (EditText) edit_and_five_chkboxes.findViewById(R.id.et1EditAndFiveChkboxes);

        ArrayList <EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1EditAndFiveChkboxes1);
        al_al_Edits.add(al_Edits);
        EditTexts.put("EditAndFiveChkboxes", al_al_Edits);

        ArrayList <CheckBox> al_chkBoxes = new ArrayList<>();

        CheckBox chk1EditAndFiveChkboxes1 = (CheckBox) edit_and_five_chkboxes.findViewById(R.id.chk1EditAndFiveChkboxes);
        chk1EditAndFiveChkboxes1.setText("sa_id_item_75[0]");
        al_chkBoxes.add(chk1EditAndFiveChkboxes1);

        CheckBox  chk2EditAndFiveChkboxes1 = (CheckBox) edit_and_five_chkboxes.findViewById(R.id.chk2EditAndFiveChkboxes);
        chk2EditAndFiveChkboxes1.setText("sa_id_item_75[1]");
        al_chkBoxes.add(chk2EditAndFiveChkboxes1);

        CheckBox  chk3EditAndFiveChkboxes1 = (CheckBox) edit_and_five_chkboxes.findViewById(R.id.chk3EditAndFiveChkboxes);
        chk3EditAndFiveChkboxes1.setText("sa_id_item_75[2]");
        al_chkBoxes.add(chk3EditAndFiveChkboxes1);

        CheckBox  chk4EditAndFiveChkboxes1 = (CheckBox) edit_and_five_chkboxes.findViewById(R.id.chk4EditAndFiveChkboxes);
        chk4EditAndFiveChkboxes1.setText("sa_id_item_75[2]");
        al_chkBoxes.add(chk4EditAndFiveChkboxes1);

        CheckBox chk5EditAndFiveChkboxes1 = (CheckBox) edit_and_five_chkboxes.findViewById(R.id.chk5EditAndFiveChkboxes);
        chk5EditAndFiveChkboxes1.setText("sa_id_item_75[2]");
        al_chkBoxes.add(chk5EditAndFiveChkboxes1);

        al_al_CheckBoxes.add(al_chkBoxes);
        CheckBoxes.put("EditAndFiveChkboxes", al_al_CheckBoxes);

        return sectionNumber;
    }
    
    private int createViewTwoTextsTwoEdits(int sectionNumber, int idRes)
    {
        LinearLayout two_texts_two_edits1 = (LinearLayout) rootView.findViewById(R.id.two_texts_two_edits1);
        llSectionTwoTextsTwoEdits1 = (LinearLayout) two_texts_two_edits1.findViewById(R.id.llSectionTwoTextTwoEdit);

        TextView tv1TwoTextTwoEdit1 = (TextView) two_texts_two_edits1.findViewById(R.id.tv1TwoTextTwoEdit);
        tv1TwoTextTwoEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        TextView tv2TwoTextTwoEdit1 = (TextView) two_texts_two_edits1.findViewById(R.id.tv2TwoTextTwoEdit);
        tv2TwoTextTwoEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        et1TwoTextTwoEdit1 = (EditText) two_texts_two_edits1.findViewById(R.id.et1TwoTextTwoEdit);

        et2TwoTextTwoEdit1 = (EditText) two_texts_two_edits1.findViewById(R.id.et2TwoTextTwoEdit);


        return sectionNumber;
    }

    private int createViewTwoSwitches(int sectionNumber, int idRes)
    {
        LinearLayout two_switches1 = (LinearLayout) rootView.findViewById(R.id.two_switches1);
        llHeaderTwoSwitches1 = (LinearLayout) two_switches1.findViewById(R.id.llHeaderTwoSwitches);
        llHeaderTwoSwitches1.setOnClickListener(this);

        TextView tvHeaderTwoSwitches1 = (TextView) two_switches1.findViewById(R.id.tvHeaderTwoSwitches);
        tvHeaderTwoSwitches1.setText("str_id_item_142_header");

        llSectionTwoSwitches1 = (LinearLayout) two_switches1.findViewById(R.id.llSectionTwoSwitches);

        ivArrowTwoSwitches1 = (ImageView) two_switches1.findViewById(R.id.ivArrowTwoSwitches);

        TextView tv1TwoSwitches1 = (TextView) two_switches1.findViewById(R.id.tv1TwoSwitches);
        tv1TwoSwitches1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        sw1TwoSwitches1 = (Switch) two_switches1.findViewById(R.id.sw1TwoSwitches);

        TextView tv2TwoSwitches1 = (TextView) two_switches1.findViewById(R.id.tv2TwoSwitches);
        tv2TwoSwitches1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        sw2TwoSwitches1 = (Switch) two_switches1.findViewById(R.id.sw2TwoSwitches);
        return sectionNumber;
    }
    
    private void createViewFourRadios(int sectionNumber, int idRes)
    {
        int i;
        LinearLayout four_radios1 = (LinearLayout) rootView.findViewById(R.id.four_radios1);
        llHeaderFourRadios1 = (LinearLayout) four_radios1.findViewById(R.id.llHeaderFourRadios);
        llHeaderFourRadios1.setOnClickListener(this);

        TextView tvHeaderFourRadios1 = (TextView) four_radios1.findViewById(R.id.tvHeaderFourRadios);
        tvHeaderFourRadios1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ivArrowFourRadios1 = (ImageView) four_radios1.findViewById(R.id.ivArrowFourRadios);

        llSectionFourRadios1 = (LinearLayout) four_radios1.findViewById(R.id.llSectionFourRadios);

        rg1FourRadios1 = (RadioGroup) four_radios1.findViewById(R.id.rg1FourRadios);

        for (i = 0; i < rg1FourRadios1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1FourRadios1.getChildAt(i);

            rb.setText("sa_id_item_139[i]");
        }
    }
    
    private void createViewTwoRadiosAndEdit(int sectionNumber, int idRes)
    {
        LinearLayout two_radios_and_edit1 = (LinearLayout) rootView.findViewById(idRes);
        
        llHeaderTwoRadiosAndEdit1 = (LinearLayout) two_radios_and_edit1.findViewById(R.id.llHeaderTwoRadiosAndEdit);
        llHeaderTwoRadiosAndEdit1.setOnClickListener(this);

        ivArrowTwoRadiosAndEdit1 = (ImageView) two_radios_and_edit1.findViewById(R.id.ivArrowTwoRadiosAndEdit);

        TextView tvHeaderTwoRadiosAndEdit1 = (TextView) two_radios_and_edit1.findViewById(R.id.tvHeaderTwoRadiosAndEdit);
        tvHeaderTwoRadiosAndEdit1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        llSectionTwoRadiosAndEdit1 = (LinearLayout) two_radios_and_edit1.findViewById(R.id.llSectionTwoRadiosAndEdit);

        rg1TwoRadiosAndEdit1 = (RadioGroup) two_radios_and_edit1.findViewById(R.id.rg1TwoRadiosAndEdit);

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String [] fields = valore.split("\\|\\|");

        for (int i = 0; i < rg1TwoRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadiosAndEdit1.getChildAt(i);

            rb.setText(fields[i]);
        }

        et1TwoRadiosAndEdit1 = (EditText) two_radios_and_edit1.findViewById(R.id.et1TwoRadiosAndEdit);
        et1TwoRadiosAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1TwoRadiosAndEdit1.clearCheck();
                return false;
            }
        });
    }

    private void createViewTwoRadios(int sectionNumber, int idRes)
    {
        int i;
        LinearLayout ll_two_radios1 = (LinearLayout) rootView.findViewById(R.id.two_radios1);
        llHeaderTwoRadios1 = (LinearLayout) ll_two_radios1.findViewById(R.id.llHeaderTwoRadios);
        llHeaderTwoRadios1.setOnClickListener(this);

        TextView tvHeaderTwoRadios1 = (TextView) ll_two_radios1.findViewById(R.id.tvHeaderTwoRadios);
        tvHeaderTwoRadios1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ivArrowTwoRadios1 = (ImageView) ll_two_radios1.findViewById(R.id.ivArrowTwoRadios);

        llSectionTwoRadios1 = (LinearLayout) ll_two_radios1.findViewById(R.id.llSectionTwoRadios);

        rg1TwoRadios1 = (RadioGroup) ll_two_radios1.findViewById(R.id.rg1TwoRadios);

        for (i = 0; i < rg1TwoRadios1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

            rb.setText("sa_id_item_139[i]");
        }
    }

    private void createViewFourRadiosAndEdit(int sectionNumber, int idRes)
    {
        int i;
        LinearLayout four_radios_and_edit1 = (LinearLayout) rootView.findViewById(R.id.four_radios_and_edit1);
        llHeaderFourRadiosAndEdit1 = (LinearLayout) four_radios_and_edit1.findViewById(R.id.llHeaderFourRadiosAndEdit);
        llHeaderFourRadiosAndEdit1.setOnClickListener(this);

        TextView tvHeaderFourRadiosAndEdit1 = (TextView) four_radios_and_edit1.findViewById(R.id.tvHeaderFourRadiosAndEdit);
        tvHeaderFourRadiosAndEdit1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ivArrowFourRadiosAndEdit1 = (ImageView) four_radios_and_edit1.findViewById(R.id.ivArrowFourRadiosAndEdit);

        llSectionFourRadiosAndEdit1 = (LinearLayout) four_radios_and_edit1.findViewById(R.id.llSectionFourRadiosAndEdit);

        rg1FourRadiosAndEdit1 = (RadioGroup) four_radios_and_edit1.findViewById(R.id.rg1FourRadiosAndEdit);

        for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

            rb.setText("sa_id_item_144[i]");
        }

        et1FourRadiosAndEdit1 = (EditText) four_radios_and_edit1.findViewById(R.id.et1FourRadiosAndEdit);
        et1FourRadiosAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1FourRadiosAndEdit1.clearCheck();
                return false;
            }
        });
    }

    private void createViewSectionHeader(int headerNumber, int idRes)
    {
        LinearLayout headerFotoV1 = (LinearLayout) rootView.findViewById(R.id.header1);
        flSectionHeader1 = (FrameLayout) headerFotoV1.findViewById(R.id.flSectionHeader);
        flSectionHeader1.setOnClickListener(this);

        TextView tvSectionHeader1 = (TextView) headerFotoV1.findViewById(R.id.tvSectionHeader);
        tvSectionHeader1.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
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
        if (view == flSectionHeader1)
        {
/*            llSectionThreeRadiosAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionTwoRadios1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionFourRadiosAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionThreeChkboxesAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);*/

            allSections1Collapsed = !allSections1Collapsed;
        }

        if (view == flSectionHeader2)
        {
/*            llSectionThreeTextThreeEdit1.setVisibility(!allSections2Collapsed ? View.GONE : View.VISIBLE);*/

            allSections2Collapsed = !allSections2Collapsed;
        }

    }
}
