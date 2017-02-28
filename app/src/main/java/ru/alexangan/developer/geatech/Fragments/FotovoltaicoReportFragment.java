package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    private FrameLayout flSectionHeader1, flSectionHeader2;
    private LinearLayout llHeaderThreeRadiosAndEdit1, llSectionThreeRadiosAndEdit1, llHeaderTwoRadios1, llSectionTwoRadios1, llHeaderTwoRadios2, llSectionTwoRadios2,
            llHeaderFourRadiosAndEdit1, llSectionFourRadiosAndEdit1, llHeaderThreeChkboxesAndEdit1, llSectionThreeChkboxesAndEdit1,
            llSectionThreeTextThreeEdit1, llHeaderTwoEdits1, llHeaderEdit1, llHeaderTwoSwitches1, llHeaderEdit2, llHeaderSwitchAndEdit1,
            llSectionEdit1, llSectionEdit2, llSectionTwoSwitches1, llSectionTwoEdits1, llSectionSwitchAndEdit1;

    private TextView tvReportTitle, tv1ThreeTextThreeEdit1, tv2ThreeTextThreeEdit1, tv3ThreeTextThreeEdit1, tvSectionHeader1, tv1Edit1,
            tvSectionHeader2, tvHeaderThreeRadiosAndEdit1, tvHeaderTwoRadios1, tvHeaderTwoRadios2, tvHeaderFourRadiosAndEdit1,
            tvHeaderSwitchAndEdit1, tvHeaderEdit2, tvHeaderTwoEdits1, tvHeaderTwoSwitches1, tvHeaderEdit1, tv2SwitchAndEdit1,
            tvHeaderThreeChkboxesAndEdit1, tv1TwoSwitches1, tv2TwoSwitches1, tv1TwoEdits1, tv2TwoEdits1, tv1SwitchAndEdit1, tv1Edit2;

    private RadioGroup rg1ThreeRadiosAndEdit1, rg1TwoRadios1, rg1TwoRadios2, rg1FourRadiosAndEdit1;

    private EditText et1ThreeRadiosAndEdit1, et1FourRadiosAndEdit1, et1ThreeChkboxesAndEdit1, et1ThreeTextThreeEdit1, et2ThreeTextThreeEdit1,
            et3ThreeTextThreeEdit1, et1Edit1, et1Edit2, et1SwitchAndEdit1, et1TwoEdits1, et2TwoEdits1;

    Switch sw1TwoSwitches1, sw2TwoSwitches1, sw1SwitchAndEdit1;

    ImageView ivArrowTwoRadios1, ivArrowTwoRadios2, ivArrowThreeRadiosAndEdit1, ivArrowFourRadiosAndEdit1, ivArrowThreeChkboxesAndEdit1,
            ivArrowEdit1, ivArrowEdit2, ivArrowTwoSwitches1, ivArrowTwoEdits1, ivArrowSwitchAndEdit1;

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fotovoltaico_report, container, false);

        int headerNumber = 0;
        int sectionNumber = 0;

        tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        // SectionHeader1
        createViewSectionHeader1(headerNumber++);

        // ThreeRadiosAndEdit1
        //createViewTwoRadiosAndEdit1(sectionNumber++);

        //sectionNumber = createViewEditAndFiveChkboxes1(sectionNumber);

        createViewFourRadiosAndEdit1(sectionNumber++);

        createViewFourRadiosAndEdit2(sectionNumber++);

        //sectionNumber = createViewFourEditsAndSwitch1(sectionNumber);

        createViewTwoRadios1(sectionNumber++);

        //createViewFourRadios1(sectionNumber++);

        //createViewTwoSwitches1(sectionNumber++);

        // SectionHeader2
        createViewSectionHeader2(headerNumber++);

        //sectionNumber = createViewTwoTextsTwoEdits1(sectionNumber);

        // SectionHeader3
        createViewSectionHeader3(headerNumber++);

        //sectionNumber = createViewFiveSwitches1(sectionNumber);

        return rootView;
    }

    private void createViewTwoRadios1(int sectionNumber)
    {
        int i;
        LinearLayout ll_two_radios1 = (LinearLayout) rootView.findViewById(R.id.two_radiosFotoV1);
        llHeaderTwoRadios1 = (LinearLayout) ll_two_radios1.findViewById(R.id.llHeaderTwoRadios1);
        llHeaderTwoRadios1.setOnClickListener(this);

        tvHeaderTwoRadios1 = (TextView) ll_two_radios1.findViewById(R.id.tvHeaderTwoRadios1);
        tvHeaderTwoRadios1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ivArrowTwoRadios1 = (ImageView) ll_two_radios1.findViewById(R.id.ivArrowTwoRadios1);

        llSectionTwoRadios1 = (LinearLayout) ll_two_radios1.findViewById(R.id.llSectionTwoRadios1);

        rg1TwoRadios1 = (RadioGroup) ll_two_radios1.findViewById(R.id.rg1TwoRadios1);

        for (i = 0; i < rg1TwoRadios1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

            rb.setText("sa_id_item_139[i]");
        }
    }

    private void createViewFourRadiosAndEdit2(int sectionNumber)
    {
        int i;
        LinearLayout four_radios_and_edit2 = (LinearLayout) rootView.findViewById(R.id.four_radios_and_editFotoV2);
        llHeaderFourRadiosAndEdit1 = (LinearLayout) four_radios_and_edit2.findViewById(R.id.llHeaderFourRadiosAndEdit1);
        llHeaderFourRadiosAndEdit1.setOnClickListener(this);

        tvHeaderFourRadiosAndEdit1 = (TextView) four_radios_and_edit2.findViewById(R.id.tvHeaderFourRadiosAndEdit1);
        tvHeaderFourRadiosAndEdit1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ivArrowFourRadiosAndEdit1 = (ImageView) four_radios_and_edit2.findViewById(R.id.ivArrowFourRadiosAndEdit1);

        llSectionFourRadiosAndEdit1 = (LinearLayout) four_radios_and_edit2.findViewById(R.id.llSectionFourRadiosAndEdit1);

        rg1FourRadiosAndEdit1 = (RadioGroup) four_radios_and_edit2.findViewById(R.id.rg1FourRadiosAndEdit1);

        for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

            rb.setText("sa_id_item_144[i]");
        }

        et1FourRadiosAndEdit1 = (EditText) four_radios_and_edit2.findViewById(R.id.et1FourRadiosAndEdit1);
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

    private void createViewFourRadiosAndEdit1(int sectionNumber)
    {
        int i;
        LinearLayout four_radios_and_edit1 = (LinearLayout) rootView.findViewById(R.id.four_radios_and_editFotoV1);
        llHeaderFourRadiosAndEdit1 = (LinearLayout) four_radios_and_edit1.findViewById(R.id.llHeaderFourRadiosAndEdit1);
        llHeaderFourRadiosAndEdit1.setOnClickListener(this);

        tvHeaderFourRadiosAndEdit1 = (TextView) four_radios_and_edit1.findViewById(R.id.tvHeaderFourRadiosAndEdit1);
        tvHeaderFourRadiosAndEdit1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ivArrowFourRadiosAndEdit1 = (ImageView) four_radios_and_edit1.findViewById(R.id.ivArrowFourRadiosAndEdit1);

        llSectionFourRadiosAndEdit1 = (LinearLayout) four_radios_and_edit1.findViewById(R.id.llSectionFourRadiosAndEdit1);

        rg1FourRadiosAndEdit1 = (RadioGroup) four_radios_and_edit1.findViewById(R.id.rg1FourRadiosAndEdit1);

        for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

            rb.setText("sa_id_item_144[i]");
        }

        et1FourRadiosAndEdit1 = (EditText) four_radios_and_edit1.findViewById(R.id.et1FourRadiosAndEdit1);
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

    private void createViewSectionHeader1(int headerNumber)
    {
        LinearLayout headerFotoV1 = (LinearLayout) rootView.findViewById(R.id.headerFotoV1);
        flSectionHeader1 = (FrameLayout) headerFotoV1.findViewById(R.id.flSectionHeader1);
        flSectionHeader1.setOnClickListener(this);

        tvSectionHeader1 = (TextView) headerFotoV1.findViewById(R.id.tvSectionHeader1);
        tvSectionHeader1.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
    }

    private void createViewSectionHeader2(int headerNumber)
    {
        LinearLayout headerFotoV2 = (LinearLayout) rootView.findViewById(R.id.headerFotoV2);
        flSectionHeader2 = (FrameLayout) headerFotoV2.findViewById(R.id.flSectionHeader1);
        flSectionHeader2.setOnClickListener(this);

        tvSectionHeader2 = (TextView) headerFotoV2.findViewById(R.id.tvSectionHeader1);
        tvSectionHeader2.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
    }

    private void createViewSectionHeader3(int headerNumber)
    {
        LinearLayout headerFotoV3 = (LinearLayout) rootView.findViewById(R.id.headerFotoV3);
        flSectionHeader2 = (FrameLayout) headerFotoV3.findViewById(R.id.flSectionHeader1);
        flSectionHeader2.setOnClickListener(this);

        tvSectionHeader2 = (TextView) headerFotoV3.findViewById(R.id.tvSectionHeader1);
        tvSectionHeader2.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
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
