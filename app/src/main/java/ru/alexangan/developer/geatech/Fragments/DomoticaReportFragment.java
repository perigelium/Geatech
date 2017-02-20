package ru.alexangan.developer.geatech.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Models.ReportModelDomotica;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class DomoticaReportFragment extends Fragment implements View.OnClickListener
{
    View rootView;
    ReportModelDomotica reportModelDomotica;
    private int selectedIndex;
    int idSopralluogo;
    ReportStates reportStates;
    Context context;

    private boolean allSections1Collapsed, allSections2Collapsed;

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

/*    private final String strReportTitle = "Domotica componenti";

    private final String [] saHeaderTitles = {"1. Rilievo dello stato dei luoghi", "2. Impianto"};

    private final String[] saSectionTitles = new String[] {
            " 1.1 TIPO DI CALDAIA:",
            " 1.2 TIPO DI ACS:",
            " 1.3 MARCO E MODELLO CALDAIA:",
            " 1.4 MY VIRTUOSO E WIFI:",
            " 1.5 ELEMENTI RADIANTI:",
            " 1.6 NUMERO ELEMENTI RADIANTI:",
            " 1.7 MARCA VALVOLE E DETENITORI:",
            " 1.8 ABITAZIONE:",
    };*/

    private final String[] strImpianto =  {
     " 2.1 NOTE SUL LUOGO DI INSTALLAZIONE E INDIVIDUAZIONE DI EVENTUALI CRITICITÀ SUI COLLEGAMENTI IDRAULICI E/O SULLA GESTIONE DELL'UTENZA:",
     " 2.2 NOTE SULLA TIPOLOGIA DELL'IMPIANTO ESISTENTE \n\n" +
                    "DESCRIVERE COMA VERRÀ INTERFACCIATO IL SISTEMA ALL'UTENZA PRINCIPALEE I COMPONENTI CHE VERRANNO UTILIZZATI:",
     "2.3 NOTE RELATIVE A COLLEGAMENTO ALL'IMPIANTO ESISTENTE"
    };

    private final String[] sa_id_item_139 =  {"Camera Stagna", "Camera Aperta"};
    private final String[] sa_id_item_140 =  {"Caldaia istantanea", "Caldaia con accumulo"};
    private final String str_id_item_141 = "#"; // Marca e Modello caldaia:
    private final String str_id_item_142 = "Presenza di contatto pulito per My Virtuoso";
    private final String str_id_item_143 = "Presenza Conessione Wi-Fi";
    private final String[] sa_id_item_144 =  {"Radiatori alluminio", "Radiatori ghisa", "Radiatori ferro", "Pavimento radiante"};
    private final String str_id_item_145 = "#"; // Numero elementi radianti
    private final String str_id_item_146 = "Nr.totale:";
    private final String str_id_item_147 = "#"; // Marca valvola e detenitori:
    private final String str_id_item_148 = "Abitazione singola:";
    private final String str_id_item_149 = "Indicare il nr. delle\n unità abitative:";


    public DomoticaReportFragment()
    {
        // Required empty public constructor
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
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportStates != null && reportModelDomotica !=null)
        {
            realm.beginTransaction();

            int checkedBtnId;
            
            // TwoRadios1
            String strId_item_139 = "";
            checkedBtnId = rg1TwoRadios1.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rg1TwoRadios1.findViewById(checkedBtnId);
                strId_item_139 = radioButton.getText().toString();

                reportModelDomotica.setId_item_139(strId_item_139);
            }

            // TwoRadios2
            String strId_item_140 = "";
            checkedBtnId = rg1TwoRadios2.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rg1TwoRadios2.findViewById(checkedBtnId);
                strId_item_140 = radioButton.getText().toString();

                reportModelDomotica.setId_item_140(strId_item_140);
            }

            // Edit1
            String strId_item_141 = et1Edit1.getText().toString();
            reportModelDomotica.setId_item_141(strId_item_141);

            // TwoSwitches1
            if(sw1TwoSwitches1.isChecked())
            {
                reportModelDomotica.setId_item_142(str_id_item_142);
            }
            else
            {
                reportModelDomotica.setId_item_142("no");
            }

            if(sw2TwoSwitches1.isChecked())
            {
                reportModelDomotica.setId_item_143(str_id_item_143);
            }
            else
            {
                reportModelDomotica.setId_item_143("no");
            }

            // FourRadiosAndEdit1
            String strId_item_144 = "";
            checkedBtnId = rg1FourRadiosAndEdit1.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rg1FourRadiosAndEdit1.findViewById(checkedBtnId);
                strId_item_144 = radioButton.getText().toString();
            }
            else
            {
                strId_item_144 = et1FourRadiosAndEdit1.getText().toString();
            }
            reportModelDomotica.setId_item_144(strId_item_144);

            // TwoEdits1
            String strId_item_145 = et1TwoEdits1.getText().toString();
            reportModelDomotica.setId_item_145(strId_item_145);
            String strId_item_146 = et2TwoEdits1.getText().toString();
            reportModelDomotica.setId_item_146(strId_item_146);

            // Edit2
            String strId_item_147 = et1Edit2.getText().toString();
            reportModelDomotica.setId_item_147(strId_item_147);

            // SwitchAndEdit1
            if(sw1SwitchAndEdit1.isChecked())
            {
                reportModelDomotica.setId_item_148(str_id_item_148);
            }
            else
            {
                reportModelDomotica.setId_item_148("no");
            }

            String strId_item_149 = et1SwitchAndEdit1.getText().toString();
            reportModelDomotica.setId_item_149(strId_item_149);

            // ThreeTextThreeEdit1
            String strId_item_150 = et1ThreeTextThreeEdit1.getText().toString();
            reportModelDomotica.setId_item_150(strId_item_150);


            String strId_item_151 = et2ThreeTextThreeEdit1.getText().toString();
            reportModelDomotica.setId_item_151(strId_item_151);


            String str_Id_item_152 = et3ThreeTextThreeEdit1.getText().toString();
            reportModelDomotica.setId_item_152(str_Id_item_152);


            // Completion state
            if(strId_item_139.length() != 0)
            {
                reportStates.setReportCompletionState(1);

                if (strId_item_140.length() != 0 || strId_item_141.length() != 0)
                {
                    reportStates.setReportCompletionState(2);

                    if (strId_item_139.length() != 0 && strId_item_141.length() != 0 && strId_item_146.length() != 0)
                    {
                        reportStates.setReportCompletionState(3);

                        Calendar calendarNow = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                        String strDateTime = sdf.format(calendarNow.getTime());
                        reportStates.setDataOraRaportoCompletato(strDateTime);
                    }
                }
            }
            else
            {
                reportStates.setReportCompletionState(0);
                reportStates.setDataOraRaportoCompletato(null);
            }
        }
        realm.commitTransaction();

        if (reportStates == null)
        {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        realm.beginTransaction();
        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();

        reportStates = realm.where(ReportStates.class).equalTo("id_sopralluogo", idSopralluogo).findFirst();
        reportModelDomotica = realm.where(ReportModelDomotica.class).equalTo("id_sopralluogo", idSopralluogo).findFirst();
        RealmResults <ReportModelDomotica> reportModelDomoticas = realm.where(ReportModelDomotica.class).findAll();

        realm.commitTransaction();

        realm.beginTransaction();

        if (reportModelDomotica != null)
        {
            int i;

            // TwoRadios1
            String strId_item_139 = reportModelDomotica.getId_item_139();

            if(strId_item_139 !=null)
            {
                for (i = 0; i < rg1TwoRadios1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

                    if (strId_item_139.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }
            }

            // TwoRadios2
            String strId_item_140 = reportModelDomotica.getId_item_140();

            if(strId_item_140 !=null)
            {
                for (i = 0; i < rg1TwoRadios2.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1TwoRadios2.getChildAt(i);

                    if (strId_item_140.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }
            }

            // Edit1
            String strId_item_141 = reportModelDomotica.getId_item_141();
            et1Edit1.setText(strId_item_141);

                // TwoSwitches1
                String strId_item_142 = reportModelDomotica.getId_item_142();

                if (strId_item_142!=null && strId_item_142.contains(sw1TwoSwitches1.getText().toString()))
                {
                    sw1TwoSwitches1.setChecked(true);
                }

                String strId_item_143 = reportModelDomotica.getId_item_143();
                if (strId_item_143!=null && strId_item_143.contains(sw2TwoSwitches1.getText().toString()))
                {
                    sw2TwoSwitches1.setChecked(true);
                }

            // FourRadiosAndEdit1
            String strId_item_144 = reportModelDomotica.getId_item_144();
            if(strId_item_144 != null)
            {
                for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

                    if (strId_item_144.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }

                if (i == rg1FourRadiosAndEdit1.getChildCount())
                {
                    et1FourRadiosAndEdit1.setText(strId_item_144);
                }
            }

            // TwoEdits1
            String strId_item_145 = reportModelDomotica.getId_item_145();
            et1TwoEdits1.setText(strId_item_145);

            String strId_item_146 = reportModelDomotica.getId_item_146();
            et2TwoEdits1.setText(strId_item_146);

            // Edit2
            String strId_item_147 = reportModelDomotica.getId_item_147();
            et1Edit2.setText(strId_item_147);

            // SwitchAndEdit1
            String strId_item_148 = reportModelDomotica.getId_item_143();
            if (strId_item_148!=null && strId_item_148.contains(sw1SwitchAndEdit1.getText().toString()))
            {
                sw1SwitchAndEdit1.setChecked(true);
            }
            
            String strId_item_149 = reportModelDomotica.getId_item_149();
            et1SwitchAndEdit1.setText(strId_item_149);

            // ThreeTextsThreeEdits1
            et1ThreeTextThreeEdit1.setText(reportModelDomotica.getId_item_150());
            et2ThreeTextThreeEdit1.setText(reportModelDomotica.getId_item_151());
            et3ThreeTextThreeEdit1.setText(reportModelDomotica.getId_item_152());
        }

        realm.commitTransaction();

        realm.beginTransaction();

        if (reportStates != null)
        {
            if (reportModelDomotica == null)
            {
                reportModelDomotica = new ReportModelDomotica(reportModelDomoticas.size(), idSopralluogo);
                realm.copyToRealmOrUpdate(reportModelDomotica);
            }
        }
        realm.commitTransaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView =  inflater.inflate(R.layout.domotica_report, container, false);

        realm.beginTransaction();
        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();

        reportStates = realm.where(ReportStates.class).equalTo("id_sopralluogo", idSopralluogo).findFirst();
        reportModelDomotica = realm.where(ReportModelDomotica.class).equalTo("id_rapporto_sopralluogo", idSopralluogo).findFirst();
        RealmResults<ReportModelDomotica> reportModelDomoticas = realm.where(ReportModelDomotica.class).findAll();

        if (reportStates != null)
        {
            if (reportModelDomotica == null)
            {
                reportModelDomotica = new ReportModelDomotica(reportModelDomoticas.size(), idSopralluogo);
                realm.copyToRealmOrUpdate(reportModelDomotica);
            }
        }
        realm.commitTransaction();
        
        int i;
        int headerNumber = 0;
        int sectionNumber = 0;
        
        realm.beginTransaction();
        GeaModelloRapporto geaModello = realm.where(GeaModelloRapporto.class).equalTo("nome_modello", "DOMOTICA").findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        List<GeaSezioneModelliRapporto> geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class).equalTo("id_modello", geaModello.getId_modello()).findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        List<GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class)
                .between("id_sezione", geaSezioniModelli.get(0).getId_sezione(), geaSezioniModelli.get(geaSezioniModelli.size()-1).getId_sezione())
                .between("id_item_modello", 139, 152).findAll();
        realm.commitTransaction();

        
        tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        flSectionHeader1 = (FrameLayout) rootView.findViewById(R.id.flSectionHeader1);
        flSectionHeader1.setOnClickListener(this);

        tvSectionHeader1 = (TextView) rootView.findViewById(R.id.tvSectionHeader1);
        tvSectionHeader1.setText(geaSezioniModelli.get(headerNumber++).getDescrizione_sezione());

        // TwoRadios1
        llHeaderTwoRadios1 = (LinearLayout) rootView.findViewById(R.id.llHeaderTwoRadios1);
        llHeaderTwoRadios1.setOnClickListener(this);

        tvHeaderTwoRadios1 = (TextView) rootView.findViewById(R.id.tvHeaderTwoRadios1);
        tvHeaderTwoRadios1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        ivArrowTwoRadios1 = (ImageView) rootView.findViewById(R.id.ivArrowTwoRadios1);

        llSectionTwoRadios1 = (LinearLayout) rootView.findViewById(R.id.llSectionTwoRadios1);

        rg1TwoRadios1 = (RadioGroup) rootView.findViewById(R.id.rg1TwoRadios1);

        for (i = 0; i < rg1TwoRadios1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

            rb.setText(sa_id_item_139[i]);
        }

        // TwoRadios2
        llHeaderTwoRadios2 = (LinearLayout) rootView.findViewById(R.id.llHeaderTwoRadios2);
        llHeaderTwoRadios2.setOnClickListener(this);

        tvHeaderTwoRadios2 = (TextView) rootView.findViewById(R.id.tvHeaderTwoRadios2);
        tvHeaderTwoRadios2.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        ivArrowTwoRadios2 = (ImageView) rootView.findViewById(R.id.ivArrowTwoRadios2);

        llSectionTwoRadios2 = (LinearLayout) rootView.findViewById(R.id.llSectionTwoRadios2);

        rg1TwoRadios2 = (RadioGroup) rootView.findViewById(R.id.rg1TwoRadios2);

        for (i = 0; i < rg1TwoRadios2.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadios2.getChildAt(i);

            rb.setText(sa_id_item_140[i]);
        }

        // Edit1
        llHeaderEdit1 = (LinearLayout) rootView.findViewById(R.id.llHeaderEdit1);
        llHeaderEdit1.setOnClickListener(this);

        tvHeaderEdit1 = (TextView) rootView.findViewById(R.id.tvHeaderEdit1);
        tvHeaderEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionEdit1);

        ivArrowEdit1 = (ImageView) rootView.findViewById(R.id.ivArrowEdit1);

        tv1Edit1 = (TextView) rootView.findViewById(R.id.tv1Edit1);
        tv1Edit1.setText(str_id_item_141);

        et1Edit1 = (EditText) rootView.findViewById(R.id.et1Edit1);



        // TwoSwitches1
        llHeaderTwoSwitches1 = (LinearLayout) rootView.findViewById(R.id.llHeaderTwoSwitches1);
        llHeaderTwoSwitches1.setOnClickListener(this);

        tvHeaderTwoSwitches1 = (TextView) rootView.findViewById(R.id.tvHeaderTwoSwitches1);
        tvHeaderTwoSwitches1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionTwoSwitches1 = (LinearLayout) rootView.findViewById(R.id.llSectionTwoSwitches1);

        ivArrowTwoSwitches1 = (ImageView) rootView.findViewById(R.id.ivArrowTwoSwitches1);

        tv1TwoSwitches1 = (TextView) rootView.findViewById(R.id.tv1TwoSwitches1);
        tv1TwoSwitches1.setText(str_id_item_142);

        sw1TwoSwitches1 = (Switch) rootView.findViewById(R.id.sw1TwoSwitches1);
        tv2TwoSwitches1 = (TextView) rootView.findViewById(R.id.tv2TwoSwitches1);
        tv2TwoSwitches1.setText(str_id_item_143);
        sw2TwoSwitches1 = (Switch) rootView.findViewById(R.id.sw2TwoSwitches1);

        // FourRadiosAndEdit1
        llHeaderFourRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llHeaderFourRadiosAndEdit1);
        llHeaderFourRadiosAndEdit1.setOnClickListener(this);

        tvHeaderFourRadiosAndEdit1 = (TextView) rootView.findViewById(R.id.tvHeaderFourRadiosAndEdit1);
        tvHeaderFourRadiosAndEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        ivArrowFourRadiosAndEdit1 = (ImageView) rootView.findViewById(R.id.ivArrowFourRadiosAndEdit1);

        llSectionFourRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionFourRadiosAndEdit1);

        rg1FourRadiosAndEdit1 = (RadioGroup) rootView.findViewById(R.id.rg1FourRadiosAndEdit1);

        for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

            rb.setText(sa_id_item_144[i]);
        }

        et1FourRadiosAndEdit1 = (EditText) rootView.findViewById(R.id.et1FourRadiosAndEdit1);
        et1FourRadiosAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1FourRadiosAndEdit1.clearCheck();
                return false;
            }
        });


        // TwoEdits1
        llHeaderTwoEdits1 = (LinearLayout) rootView.findViewById(R.id.llHeaderTwoEdits1);
        llHeaderTwoEdits1.setOnClickListener(this);

        tvHeaderTwoEdits1 = (TextView) rootView.findViewById(R.id.tvHeaderTwoEdits1);
        tvHeaderTwoEdits1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionTwoEdits1 = (LinearLayout) rootView.findViewById(R.id.llSectionTwoEdits1);

        ivArrowTwoEdits1 = (ImageView) rootView.findViewById(R.id.ivArrowTwoEdits1);

        tv1TwoEdits1 = (TextView) rootView.findViewById(R.id.tv1TwoEdits1);
        tv1TwoEdits1.setText(str_id_item_145);

        et1TwoEdits1 = (EditText) rootView.findViewById(R.id.et1TwoEdits1);
        et1TwoEdits1.setInputType(InputType.TYPE_CLASS_NUMBER);

        tv2TwoEdits1 = (TextView) rootView.findViewById(R.id.tv2TwoEdits1);
        tv2TwoEdits1.setText(str_id_item_146);

        et2TwoEdits1 = (EditText) rootView.findViewById(R.id.et2TwoEdits1);
        et2TwoEdits1.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Edit2
        llHeaderEdit2 = (LinearLayout) rootView.findViewById(R.id.llHeaderEdit2);
        llHeaderEdit2.setOnClickListener(this);

        tvHeaderEdit2 = (TextView) rootView.findViewById(R.id.tvHeaderEdit2);
        tvHeaderEdit2.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionEdit2 = (LinearLayout) rootView.findViewById(R.id.llSectionEdit2);

        ivArrowEdit2 = (ImageView) rootView.findViewById(R.id.ivArrowEdit2);

        tv1Edit2 = (TextView) rootView.findViewById(R.id.tv1Edit2);
        tv1Edit2.setText(str_id_item_147);

        et1Edit2 = (EditText) rootView.findViewById(R.id.et1Edit2);

        // SwitchAndEdit1
        llHeaderSwitchAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llHeaderSwitchAndEdit1);
        llHeaderSwitchAndEdit1.setOnClickListener(this);

        tvHeaderSwitchAndEdit1 = (TextView) rootView.findViewById(R.id.tvHeaderSwitchAndEdit1);
        tvHeaderSwitchAndEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionSwitchAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionSwitchAndEdit1);

        ivArrowSwitchAndEdit1 = (ImageView) rootView.findViewById(R.id.ivArrowSwitchAndEdit1);

        tv1SwitchAndEdit1 = (TextView) rootView.findViewById(R.id.tv1SwitchAndEdit1);
        tv1SwitchAndEdit1.setText(str_id_item_148);

        sw1SwitchAndEdit1 = (Switch) rootView.findViewById(R.id.sw1SwitchAndEdit1);

        tv2SwitchAndEdit1 = (TextView) rootView.findViewById(R.id.tv2SwitchAndEdit1);
        tv2SwitchAndEdit1.setText(str_id_item_149);

        et1SwitchAndEdit1 = (EditText) rootView.findViewById(R.id.et1SwitchAndEdit1);
        et1SwitchAndEdit1.setInputType(InputType.TYPE_CLASS_NUMBER);


        // SectionHeader2
        flSectionHeader2 = (FrameLayout) rootView.findViewById(R.id.flSectionHeader2);
        flSectionHeader2.setOnClickListener(this);

        tvSectionHeader2 = (TextView) rootView.findViewById(R.id.tvSectionHeader2);
        tvSectionHeader2.setText(geaSezioniModelli.get(headerNumber++).getDescrizione_sezione());


        // ThreeTextThreeEdit1
        llSectionThreeTextThreeEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionThreeTextThreeEdit1);

        tv1ThreeTextThreeEdit1 = (TextView) rootView.findViewById(R.id.tv1ThreeTextThreeEdit1);
        tv1ThreeTextThreeEdit1.setText(strImpianto[0]);

        tv2ThreeTextThreeEdit1 = (TextView) rootView.findViewById(R.id.tv2ThreeTextThreeEdit1);
        tv2ThreeTextThreeEdit1.setText(strImpianto[1]);

        tv3ThreeTextThreeEdit1 = (TextView) rootView.findViewById(R.id.tv3ThreeTextThreeEdit1);
        tv3ThreeTextThreeEdit1.setText(strImpianto[2]);

        et1ThreeTextThreeEdit1 = (EditText) rootView.findViewById(R.id.et1ThreeTextThreeEdit1);
        et2ThreeTextThreeEdit1 = (EditText) rootView.findViewById(R.id.et2ThreeTextThreeEdit1);
        et3ThreeTextThreeEdit1 = (EditText) rootView.findViewById(R.id.et3ThreeTextThreeEdit1);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view == flSectionHeader1)
        {
            llSectionTwoRadios1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionTwoRadios2.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionTwoSwitches1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionEdit2.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionFourRadiosAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionTwoEdits1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionSwitchAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            

            allSections1Collapsed=!allSections1Collapsed;
        }

        if (view == flSectionHeader2)
        {
            llSectionThreeTextThreeEdit1.setVisibility(!allSections2Collapsed ? View.GONE : View.VISIBLE);

            allSections2Collapsed=!allSections2Collapsed;
        }

        if (view == llHeaderTwoRadios1)
        {
            llSectionTwoRadios1.setVisibility(llSectionTwoRadios1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowTwoRadios1.setImageResource(llSectionTwoRadios1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderTwoRadios2)
        {
            llSectionTwoRadios2.setVisibility(llSectionTwoRadios2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowTwoRadios2.setImageResource(llSectionTwoRadios2.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderEdit1)
        {
            llSectionEdit1.setVisibility(llSectionEdit1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowEdit1.setImageResource(llSectionEdit1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderTwoSwitches1)
        {
            llSectionTwoSwitches1.setVisibility(llSectionTwoSwitches1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowTwoSwitches1.setImageResource(llSectionTwoSwitches1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderFourRadiosAndEdit1)
        {
            llSectionFourRadiosAndEdit1.setVisibility(llSectionFourRadiosAndEdit1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowFourRadiosAndEdit1.setImageResource(llSectionFourRadiosAndEdit1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderTwoEdits1)
        {
            llSectionTwoEdits1.setVisibility(llSectionTwoEdits1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowTwoEdits1.setImageResource(llSectionTwoEdits1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderEdit2)
        {
            llSectionEdit2.setVisibility(llSectionEdit2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowEdit2.setImageResource(llSectionEdit2.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderSwitchAndEdit1)
        {
            llSectionSwitchAndEdit1.setVisibility(llSectionSwitchAndEdit1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowSwitchAndEdit1.setImageResource(llSectionSwitchAndEdit1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }
    }
}
