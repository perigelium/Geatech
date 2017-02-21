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
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class DomoticaReportFragment extends Fragment implements View.OnClickListener
{
    View rootView;
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

/*    private final String[] strImpianto =  {
     " 2.1 NOTE SUL LUOGO DI INSTALLAZIONE E INDIVIDUAZIONE DI EVENTUALI CRITICITÀ SUI COLLEGAMENTI IDRAULICI E/O SULLA GESTIONE DELL'UTENZA:",
     " 2.2 NOTE SULLA TIPOLOGIA DELL'IMPIANTO ESISTENTE \n\n" +
                    "DESCRIVERE COMA VERRÀ INTERFACCIATO IL SISTEMA ALL'UTENZA PRINCIPALEE I COMPONENTI CHE VERRANNO UTILIZZATI:",
     "2.3 NOTE RELATIVE A COLLEGAMENTO ALL'IMPIANTO ESISTENTE"
    };*/

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
    private int id_rapporto_sopralluogo;


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

        if (reportStates != null)
        {
            realm.beginTransaction();

            int checkedBtnId;
            
            // TwoRadios1
            String str_id_item = "";
            checkedBtnId = rg1TwoRadios1.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rg1TwoRadios1.findViewById(checkedBtnId);
                str_id_item = radioButton.getText().toString();

                insertStringInReportItem(139, str_id_item);
            }

            // TwoRadios2
            String str_Id_item = "";
            checkedBtnId = rg1TwoRadios2.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rg1TwoRadios2.findViewById(checkedBtnId);
                str_Id_item = radioButton.getText().toString();

                insertStringInReportItem(140, str_Id_item);
            }

            // Edit1
            str_id_item = et1Edit1.getText().toString();
            insertStringInReportItem(141, str_Id_item);

            
            // TwoSwitches1
            if(sw1TwoSwitches1.isChecked())
            {
                insertStringInReportItem(142, str_Id_item);
            }
            else
            {
                insertStringInReportItem(142, "no");
            }

            
            if(sw2TwoSwitches1.isChecked())
            {
                insertStringInReportItem(142, str_id_item);
            }
            else
            {
                insertStringInReportItem(143, "no");
            }

            // FourRadiosAndEdit1
            str_id_item = "";
            checkedBtnId = rg1FourRadiosAndEdit1.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rg1FourRadiosAndEdit1.findViewById(checkedBtnId);
                str_id_item = radioButton.getText().toString();
            }
            else
            {
                str_id_item = et1FourRadiosAndEdit1.getText().toString();
            }

            insertStringInReportItem(144, str_id_item);

            // TwoEdits1
            str_id_item = et1TwoEdits1.getText().toString();
            insertStringInReportItem(145, str_id_item);
            
            str_id_item = et2TwoEdits1.getText().toString();
            insertStringInReportItem(146, str_id_item);

            // Edit2
            str_id_item = et1Edit2.getText().toString();
            insertStringInReportItem(147, str_id_item);

            // SwitchAndEdit1
            if(sw1SwitchAndEdit1.isChecked())
            {
                insertStringInReportItem(148, str_id_item);
            }
            else
            {
                insertStringInReportItem(148, "no");
            }

            str_id_item = et1SwitchAndEdit1.getText().toString();
            insertStringInReportItem(149, str_id_item);

            // ThreeTextThreeEdit1
            str_id_item = et1ThreeTextThreeEdit1.getText().toString();
            insertStringInReportItem(150, str_id_item);


            str_id_item = et2ThreeTextThreeEdit1.getText().toString();
            insertStringInReportItem(150, str_id_item);


            str_id_item = et3ThreeTextThreeEdit1.getText().toString();
            insertStringInReportItem(151, str_id_item);


            // Completion state
            if(getValueFromReportItem(139).length() != 0)
            {
                reportStates.setReportCompletionState(1);

                if (getValueFromReportItem(140).length() != 0 || getValueFromReportItem(141).length() != 0)
                {
                    reportStates.setReportCompletionState(2);

                    if (getValueFromReportItem(139).length() != 0 && getValueFromReportItem(141).length() != 0
                            && getValueFromReportItem(146).length() != 0)
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
        //reportModelDomotica = realm.where(ReportModelDomotica.class).equalTo("id_sopralluogo", idSopralluogo).findFirst();
        //RealmResults <ReportModelDomotica> reportModelDomoticas = realm.where(ReportModelDomotica.class).findAll();

        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        id_rapporto_sopralluogo = reportStates.getId_rapporto_sopralluogo();

        realm.commitTransaction();

        realm.beginTransaction();

        //if (reportModelDomotica != null)
        {
            int i;

            // TwoRadios1
            String str_id_item = getValueFromReportItem(139);

            if(str_id_item !=null)
            {
                for (i = 0; i < rg1TwoRadios1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

                    if (str_id_item.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }
            }

            // TwoRadios2
            str_id_item = getValueFromReportItem(140);

            if(str_id_item !=null)
            {
                for (i = 0; i < rg1TwoRadios2.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1TwoRadios2.getChildAt(i);

                    if (str_id_item.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }
            }

            // Edit1
            str_id_item = getValueFromReportItem(141);
            et1Edit1.setText(str_id_item);

            
                // TwoSwitches1
                str_id_item = getValueFromReportItem(142);

                if (str_id_item!=null && str_id_item.contains(sw1TwoSwitches1.getText().toString()))
                {
                    sw1TwoSwitches1.setChecked(true);
                }

            
                str_id_item = getValueFromReportItem(143);
                if (str_id_item!=null && str_id_item.contains(sw2TwoSwitches1.getText().toString()))
                {
                    sw2TwoSwitches1.setChecked(true);
                }

            
            // FourRadiosAndEdit1
            str_id_item = getValueFromReportItem(144);
            if(str_id_item != null)
            {
                for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

                    if (str_id_item.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }

                if (i == rg1FourRadiosAndEdit1.getChildCount())
                {
                    et1FourRadiosAndEdit1.setText(str_id_item);
                }
            }

            
            // TwoEdits1
            str_id_item = getValueFromReportItem(145);
            et1TwoEdits1.setText(str_id_item);

            str_id_item = getValueFromReportItem(146);
            et2TwoEdits1.setText(str_id_item);

            // Edit2
            str_id_item = getValueFromReportItem(147);
            et1Edit2.setText(str_id_item);

            // SwitchAndEdit1
            str_id_item = getValueFromReportItem(148);
            if (str_id_item!=null && str_id_item.contains(sw1SwitchAndEdit1.getText().toString()))
            {
                sw1SwitchAndEdit1.setChecked(true);
            }
            
            str_id_item = getValueFromReportItem(149);
            et1SwitchAndEdit1.setText(str_id_item);

            // ThreeTextsThreeEdits1
            et1ThreeTextThreeEdit1.setText(getValueFromReportItem(150));
            et2ThreeTextThreeEdit1.setText(getValueFromReportItem(151));
            et3ThreeTextThreeEdit1.setText(getValueFromReportItem(152));
        }

        realm.commitTransaction();

        realm.beginTransaction();

        if (reportStates != null)
        {
            //if (reportModelDomotica == null)
            {
                //reportModelDomotica = new ReportModelDomotica(reportModelDomoticas.size(), idSopralluogo);
                //realm.copyToRealmOrUpdate(reportModelDomotica);
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

        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
/*        reportModelDomotica = realm.where(ReportModelDomotica.class).equalTo("id_sopralluogo", idSopralluogo).findFirst();
        RealmResults<ReportModelDomotica> reportModelDomoticas = realm.where(ReportModelDomotica.class).findAll();

        if (reportStates != null)
        {
            if (reportModelDomotica == null)
            {
                reportModelDomotica = new ReportModelDomotica(reportModelDomoticas.size(), idSopralluogo);
                realm.copyToRealmOrUpdate(reportModelDomotica);
            }
        }*/
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

        LinearLayout headerDomotica1 = (LinearLayout) rootView.findViewById(R.id.headerDomotica1);
        flSectionHeader1 = (FrameLayout) headerDomotica1.findViewById(R.id.flSectionHeader1);
        flSectionHeader1.setOnClickListener(this);

        tvSectionHeader1 = (TextView) headerDomotica1.findViewById(R.id.tvSectionHeader1);
        tvSectionHeader1.setText(geaSezioniModelli.get(headerNumber++).getDescrizione_sezione());

        // TwoRadios1
        LinearLayout ll_two_radios1 = (LinearLayout) rootView.findViewById(R.id.two_radiosDomotica1);
        llHeaderTwoRadios1 = (LinearLayout) ll_two_radios1.findViewById(R.id.llHeaderTwoRadios1);
        llHeaderTwoRadios1.setOnClickListener(this);

        tvHeaderTwoRadios1 = (TextView) ll_two_radios1.findViewById(R.id.tvHeaderTwoRadios1);
        tvHeaderTwoRadios1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        ivArrowTwoRadios1 = (ImageView) ll_two_radios1.findViewById(R.id.ivArrowTwoRadios1);

        llSectionTwoRadios1 = (LinearLayout) ll_two_radios1.findViewById(R.id.llSectionTwoRadios1);

        rg1TwoRadios1 = (RadioGroup) ll_two_radios1.findViewById(R.id.rg1TwoRadios1);

        for (i = 0; i < rg1TwoRadios1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

            rb.setText(sa_id_item_139[i]);
        }

        // TwoRadios2
        LinearLayout ll_two_radios2 = (LinearLayout) rootView.findViewById(R.id.two_radiosDomotica2);
        llHeaderTwoRadios2 = (LinearLayout) ll_two_radios2.findViewById(R.id.llHeaderTwoRadios1);
        llHeaderTwoRadios2.setOnClickListener(this);

        tvHeaderTwoRadios2 = (TextView) ll_two_radios2.findViewById(R.id.tvHeaderTwoRadios1);
        tvHeaderTwoRadios2.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        ivArrowTwoRadios2 = (ImageView) ll_two_radios2.findViewById(R.id.ivArrowTwoRadios1);

        llSectionTwoRadios2 = (LinearLayout) ll_two_radios2.findViewById(R.id.llSectionTwoRadios1);

        rg1TwoRadios2 = (RadioGroup) ll_two_radios2.findViewById(R.id.rg1TwoRadios1);

        for (i = 0; i < rg1TwoRadios2.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadios2.getChildAt(i);

            rb.setText(sa_id_item_140[i]);
        }

        // Edit1
        LinearLayout ll_edit1 = (LinearLayout) rootView.findViewById(R.id.editDomotica1);
        llHeaderEdit1 = (LinearLayout) ll_edit1.findViewById(R.id.llHeaderEdit1);
        llHeaderEdit1.setOnClickListener(this);

        tvHeaderEdit1 = (TextView) ll_edit1.findViewById(R.id.tvHeaderEdit1);
        tvHeaderEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionEdit1 = (LinearLayout) ll_edit1.findViewById(R.id.llSectionEdit1);

        ivArrowEdit1 = (ImageView) ll_edit1.findViewById(R.id.ivArrowEdit1);

        tv1Edit1 = (TextView) ll_edit1.findViewById(R.id.tv1Edit1);
        tv1Edit1.setText(str_id_item_141);

        et1Edit1 = (EditText) ll_edit1.findViewById(R.id.et1Edit1);



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
        LinearLayout ll_edit2 = (LinearLayout) rootView.findViewById(R.id.editDomotica1);
        llHeaderEdit2 = (LinearLayout) ll_edit2.findViewById(R.id.llHeaderEdit1);
        llHeaderEdit2.setOnClickListener(this);

        tvHeaderEdit2 = (TextView) ll_edit2.findViewById(R.id.tvHeaderEdit1);
        tvHeaderEdit2.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionEdit2 = (LinearLayout) ll_edit2.findViewById(R.id.llSectionEdit1);

        ivArrowEdit2 = (ImageView) ll_edit2.findViewById(R.id.ivArrowEdit1);

        tv1Edit2 = (TextView) ll_edit2.findViewById(R.id.tv1Edit1);
        tv1Edit2.setText(str_id_item_147);

        et1Edit2 = (EditText) ll_edit2.findViewById(R.id.et1Edit1);

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
        LinearLayout header2 = (LinearLayout) rootView.findViewById(R.id.headerDomotica2);
        flSectionHeader2 = (FrameLayout) header2.findViewById(R.id.flSectionHeader1);
        flSectionHeader2.setOnClickListener(this);

        tvSectionHeader2 = (TextView) header2.findViewById(R.id.tvSectionHeader1);
        tvSectionHeader2.setText(geaSezioniModelli.get(headerNumber++).getDescrizione_sezione());


        // ThreeTextThreeEdit1
        llSectionThreeTextThreeEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionThreeTextThreeEdit1);

        tv1ThreeTextThreeEdit1 = (TextView) rootView.findViewById(R.id.tv1ThreeTextThreeEdit1);
        tv1ThreeTextThreeEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        tv2ThreeTextThreeEdit1 = (TextView) rootView.findViewById(R.id.tv2ThreeTextThreeEdit1);
        tv2ThreeTextThreeEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        tv3ThreeTextThreeEdit1 = (TextView) rootView.findViewById(R.id.tv3ThreeTextThreeEdit1);
        tv3ThreeTextThreeEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

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

    private void insertStringInReportItem(int idItem, String strData)
    {
        realm.beginTransaction();
        GeaItemRapporto geaItemRapporto =
                realm.where(GeaItemRapporto.class).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).equalTo("id_item_modello", idItem).findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        if(geaItemRapporto == null)
        {
            GeaItemRapporto geaItem = new GeaItemRapporto(company_id, selectedTech.getId(), id_rapporto_sopralluogo, idItem, strData);
            realm.copyToRealm(geaItem);
        }
        else
        {
            geaItemRapporto.setValore(strData);
        }
        realm.commitTransaction();
    }

    private String getValueFromReportItem(int idItem)
    {
        realm.beginTransaction();
        GeaItemRapporto geaItemRapporto =
                realm.where(GeaItemRapporto.class).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).equalTo("id_item_modello", idItem).findFirst();
        realm.commitTransaction();

        if(geaItemRapporto !=null)
        {
            return geaItemRapporto.getValore();
        }
        else
        {
            return null;
        }
    }
}
