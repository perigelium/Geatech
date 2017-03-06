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
import ru.alexangan.developer.geatech.Utils.DatabaseUtils;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class DomoticaReportFragment extends Fragment implements View.OnClickListener
{
    View rootView;
    private int selectedIndex;
    int idSopralluogo;
    int id_rapporto_sopralluogo, idItemStart, idItemEnd;
    ReportStates reportStates;
    Context context;
    GeaModelloRapporto geaModello;
    List<GeaSezioneModelliRapporto> geaSezioniModelli;
    List<GeaItemModelliRapporto> geaItemModelli;

    private boolean allSections1Collapsed, allSections2Collapsed;

    private FrameLayout flSectionHeader1, flSectionHeader2;

    private LinearLayout llHeaderTwoRadios1, llSectionTwoRadios1, llHeaderTwoRadios2, llSectionTwoRadios2,
            llHeaderFourRadiosAndEdit1, llSectionFourRadiosAndEdit1,
            llSectionThreeTextThreeEdit1, llHeaderTwoEdits1, llHeaderEdit1, llHeaderTwoSwitches1, llHeaderEdit2, llHeaderSwitchAndEdit1,
            llSectionEdit1, llSectionEdit2, llSectionTwoSwitches1, llSectionTwoEdits1, llSectionSwitchAndEdit1;

    private RadioGroup rg1TwoRadios1, rg1TwoRadios2, rg1FourRadiosAndEdit1;

    private EditText et1FourRadiosAndEdit1, et1ThreeTextThreeEdit1, et2ThreeTextThreeEdit1,
            et3ThreeTextThreeEdit1, et1Edit1, et1Edit2, et1SwitchAndEdit1, et1TwoEdits1, et2TwoEdits1;

    Switch sw1TwoSwitches1, sw2TwoSwitches1, sw1SwitchAndEdit1;

    ImageView ivArrowTwoRadios1, ivArrowTwoRadios2, ivArrowFourRadiosAndEdit1,
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

    private final String[] sa_id_item_139 = {"Camera Stagna", "Camera Aperta"};
    private final String[] sa_id_item_140 = {"Caldaia istantanea", "Caldaia con accumulo"};
    private final String str_id_item_141 = "#"; // Marca e Modello caldaia:
    private final String str_id_item_142_header = "Collegamenti";
    private final String str_id_item_142 = "Presenza di contatto pulito per My Virtuoso";
    private final String str_id_item_143 = "Presenza Conessione Wi-Fi";
    private final String[] sa_id_item_144 = {"Radiatori alluminio", "Radiatori ghisa", "Radiatori ferro", "Pavimento radiante"};
    private final String str_id_item_145_header = "Numero elementi radianti";
    private final String str_id_item_145 = "#"; // Numero elementi radianti
    private final String str_id_item_146 = "Nr.totale:";
    private final String str_id_item_147 = "#"; // Marca valvola e detenitori:
    private final String str_id_item_148_header = "Abitazione";
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

        allSections1Collapsed = false;
        allSections2Collapsed = false;

        idItemStart = 139;
        idItemEnd = 153; // first not included id

        realm.beginTransaction();
        geaModello = realm.where(GeaModelloRapporto.class).equalTo("nome_modello", "DOMOTICA").findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class).equalTo("id_modello", geaModello.getId_modello()).findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        geaItemModelli = realm.where(GeaItemModelliRapporto.class)
            .between("id_sezione", geaSezioniModelli.get(0).getId_sezione(), geaSezioniModelli.get(geaSezioniModelli.size() - 1).getId_sezione())
                .between("id_item_modello", idItemStart, idItemEnd).findAll();
        realm.commitTransaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.domotica_report, container, false);

        int headerNumber = 0;
        int sectionNumber = 0;

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        // SectionHeader1
        createViewSectionHeader1(headerNumber++);

        // TwoRadios1
        createViewTwoRadios1(sectionNumber++);

        // TwoRadios2
        createViewTwoRadios2(sectionNumber++);

        // Edit1
        createViewEdit1(sectionNumber++);

        // TwoSwitches1
        sectionNumber = createViewTwoSwitches1(sectionNumber);

        // FourRadiosAndEdit1
        createViewFourRadiosAndEdit1(sectionNumber++);

        // TwoEdits1
        sectionNumber = createViewTwoEdits1(sectionNumber);

        // Edit2
        createViewEdit2(sectionNumber++);

        // SwitchAndEdit1
        sectionNumber = createViewSwitchAndEdit1(sectionNumber);

        // SectionHeader2
        createViewSectionHeader2(headerNumber++);

        // ThreeTextThreeEdit1
        sectionNumber = createViewThreeTextsThreeEdits1(sectionNumber);

        sectionNumber++;

        return rootView;
    }

    private int createViewThreeTextsThreeEdits1(int sectionNumber)
    {
        LinearLayout three_texts_three_edits1 = (LinearLayout) rootView.findViewById(R.id.three_texts_three_edits1);
        llSectionThreeTextThreeEdit1 = (LinearLayout) three_texts_three_edits1.findViewById(R.id.llSectionThreeTextThreeEdit);

        TextView tv1ThreeTextThreeEdit1 = (TextView) three_texts_three_edits1.findViewById(R.id.tv1ThreeTextThreeEdit);
        tv1ThreeTextThreeEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        TextView tv2ThreeTextThreeEdit1 = (TextView) three_texts_three_edits1.findViewById(R.id.tv2ThreeTextThreeEdit);
        tv2ThreeTextThreeEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        TextView tv3ThreeTextThreeEdit1 = (TextView) three_texts_three_edits1.findViewById(R.id.tv3ThreeTextThreeEdit);
        tv3ThreeTextThreeEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        et1ThreeTextThreeEdit1 = (EditText) three_texts_three_edits1.findViewById(R.id.et1ThreeTextThreeEdit);
        et2ThreeTextThreeEdit1 = (EditText) three_texts_three_edits1.findViewById(R.id.et2ThreeTextThreeEdit);
        et3ThreeTextThreeEdit1 = (EditText) three_texts_three_edits1.findViewById(R.id.et3ThreeTextThreeEdit);

        return sectionNumber;
    }

    private int createViewSwitchAndEdit1(int sectionNumber)
    {
        LinearLayout switch_and_edit1 = (LinearLayout) rootView.findViewById(R.id.switch_and_edit1);
        llHeaderSwitchAndEdit1 = (LinearLayout) switch_and_edit1.findViewById(R.id.llHeaderSwitchAndEdit);
        llHeaderSwitchAndEdit1.setOnClickListener(this);

        TextView tvHeaderSwitchAndEdit1 = (TextView) switch_and_edit1.findViewById(R.id.tvHeaderSwitchAndEdit);
        tvHeaderSwitchAndEdit1.setText(str_id_item_148_header);

        llSectionSwitchAndEdit1 = (LinearLayout) switch_and_edit1.findViewById(R.id.llSectionSwitchAndEdit);

        ivArrowSwitchAndEdit1 = (ImageView) switch_and_edit1.findViewById(R.id.ivArrowSwitchAndEdit);

        TextView tv1SwitchAndEdit1 = (TextView) switch_and_edit1.findViewById(R.id.tv1SwitchAndEdit);
        tv1SwitchAndEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        sw1SwitchAndEdit1 = (Switch) switch_and_edit1.findViewById(R.id.sw1SwitchAndEdit);
        sw1SwitchAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(!sw1SwitchAndEdit1.isChecked())
                {
                    et1SwitchAndEdit1.setText("1");
                }
                return false;
            }
        });

        TextView tv2SwitchAndEdit1 = (TextView) switch_and_edit1.findViewById(R.id.tv2SwitchAndEdit);
        tv2SwitchAndEdit1.setText(str_id_item_149);
        sectionNumber++;

        et1SwitchAndEdit1 = (EditText) switch_and_edit1.findViewById(R.id.et1SwitchAndEdit);
        et1SwitchAndEdit1.setInputType(InputType.TYPE_CLASS_NUMBER);
        et1SwitchAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                sw1SwitchAndEdit1.setChecked(false);
                return false;
            }
        });

        return sectionNumber;
    }

    private int createViewTwoEdits1(int sectionNumber)
    {
        LinearLayout two_edits1 = (LinearLayout) rootView.findViewById(R.id.two_edits1);
        llHeaderTwoEdits1 = (LinearLayout) two_edits1.findViewById(R.id.llHeaderTwoEdits);
        llHeaderTwoEdits1.setOnClickListener(this);

        TextView tvHeaderTwoEdits1 = (TextView) two_edits1.findViewById(R.id.tvHeaderTwoEdits);
        tvHeaderTwoEdits1.setText(str_id_item_145_header);

        llSectionTwoEdits1 = (LinearLayout) two_edits1.findViewById(R.id.llSectionTwoEdits);

        ivArrowTwoEdits1 = (ImageView) two_edits1.findViewById(R.id.ivArrowTwoEdits);

        TextView tv1TwoEdits1 = (TextView) two_edits1.findViewById(R.id.tv1TwoEdits);
        tv1TwoEdits1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        et1TwoEdits1 = (EditText) two_edits1.findViewById(R.id.et1TwoEdits);
        et1TwoEdits1.setInputType(InputType.TYPE_CLASS_NUMBER);

        TextView tv2TwoEdits1 = (TextView) two_edits1.findViewById(R.id.tv2TwoEdits);
        tv2TwoEdits1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        et2TwoEdits1 = (EditText) two_edits1.findViewById(R.id.et2TwoEdits);
        et2TwoEdits1.setInputType(InputType.TYPE_CLASS_NUMBER);
        return sectionNumber;
    }

    private int createViewTwoSwitches1(int sectionNumber)
    {
        LinearLayout two_switches1 = (LinearLayout) rootView.findViewById(R.id.two_switches1);
        llHeaderTwoSwitches1 = (LinearLayout) two_switches1.findViewById(R.id.llHeaderTwoSwitches);
        llHeaderTwoSwitches1.setOnClickListener(this);

        TextView tvHeaderTwoSwitches1 = (TextView) two_switches1.findViewById(R.id.tvHeaderTwoSwitches);
        tvHeaderTwoSwitches1.setText(str_id_item_142_header);

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

    private void createViewSectionHeader2(int headerNumber)
    {
        LinearLayout header2 = (LinearLayout) rootView.findViewById(R.id.header2);
        flSectionHeader2 = (FrameLayout) header2.findViewById(R.id.flSectionHeader);
        flSectionHeader2.setOnClickListener(this);

        TextView tvSectionHeader2 = (TextView) header2.findViewById(R.id.tvSectionHeader);
        tvSectionHeader2.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
    }

    private void createViewEdit2(int sectionNumber)
    {
        LinearLayout ll_edit2 = (LinearLayout) rootView.findViewById(R.id.edit2);
        llHeaderEdit2 = (LinearLayout) ll_edit2.findViewById(R.id.llHeaderEdit);
        llHeaderEdit2.setOnClickListener(this);

        TextView tvHeaderEdit2 = (TextView) ll_edit2.findViewById(R.id.tvHeaderEdit);
        tvHeaderEdit2.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        llSectionEdit2 = (LinearLayout) ll_edit2.findViewById(R.id.llSectionEdit);

        ivArrowEdit2 = (ImageView) ll_edit2.findViewById(R.id.ivArrowEdit);

        TextView tv1Edit2 = (TextView) ll_edit2.findViewById(R.id.tv1Edit);
        tv1Edit2.setText(str_id_item_147);

        et1Edit2 = (EditText) ll_edit2.findViewById(R.id.et1Edit);
    }

    private void createViewFourRadiosAndEdit1(int sectionNumber)
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

            rb.setText(sa_id_item_144[i]);
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

    private void createViewEdit1(int sectionNumber)
    {
        LinearLayout ll_edit1 = (LinearLayout) rootView.findViewById(R.id.edit1);
        llHeaderEdit1 = (LinearLayout) ll_edit1.findViewById(R.id.llHeaderEdit);
        llHeaderEdit1.setOnClickListener(this);

        TextView tvHeaderEdit1 = (TextView) ll_edit1.findViewById(R.id.tvHeaderEdit);
        tvHeaderEdit1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        llSectionEdit1 = (LinearLayout) ll_edit1.findViewById(R.id.llSectionEdit);

        ivArrowEdit1 = (ImageView) ll_edit1.findViewById(R.id.ivArrowEdit);

        TextView tv1Edit1 = (TextView) ll_edit1.findViewById(R.id.tv1Edit);
        tv1Edit1.setText(str_id_item_141);

        et1Edit1 = (EditText) ll_edit1.findViewById(R.id.et1Edit);
    }

    private void createViewTwoRadios2(int sectionNumber)
    {
        int i;
        LinearLayout ll_two_radios2 = (LinearLayout) rootView.findViewById(R.id.two_radios2);
        llHeaderTwoRadios2 = (LinearLayout) ll_two_radios2.findViewById(R.id.llHeaderTwoRadios);
        llHeaderTwoRadios2.setOnClickListener(this);

        TextView tvHeaderTwoRadios2 = (TextView) ll_two_radios2.findViewById(R.id.tvHeaderTwoRadios);
        tvHeaderTwoRadios2.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ivArrowTwoRadios2 = (ImageView) ll_two_radios2.findViewById(R.id.ivArrowTwoRadios);

        llSectionTwoRadios2 = (LinearLayout) ll_two_radios2.findViewById(R.id.llSectionTwoRadios);

        rg1TwoRadios2 = (RadioGroup) ll_two_radios2.findViewById(R.id.rg1TwoRadios);

        for (i = 0; i < rg1TwoRadios2.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadios2.getChildAt(i);

            rb.setText(sa_id_item_140[i]);
        }
    }

    private void createViewTwoRadios1(int sectionNumber)
    {
        int i;LinearLayout ll_two_radios1 = (LinearLayout) rootView.findViewById(R.id.two_radios1);
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

            rb.setText(sa_id_item_139[i]);
        }
    }

    private void createViewSectionHeader1(int headerNumber)
    {
        LinearLayout headerDomotica1 = (LinearLayout) rootView.findViewById(R.id.header1);
        flSectionHeader1 = (FrameLayout) headerDomotica1.findViewById(R.id.flSectionHeader);
        flSectionHeader1.setOnClickListener(this);

        TextView tvSectionHeader1 = (TextView) headerDomotica1.findViewById(R.id.tvSectionHeader);
        tvSectionHeader1.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportStates != null)
        {
            int id_item = idItemStart;

            // TwoRadios1
            saveTwoRadios1(id_item++);

            // TwoRadios2
            saveTwoRadios2(id_item++);

            // Edit1
            saveEdit1(id_item++);

            // TwoSwitches1
            id_item = saveTwoSwitches1(id_item);

            // FourRadiosAndEdit1
            saveFourRadiosAndEdit1(id_item++);

            // TwoEdits1
            id_item = saveTwoEdits1(id_item);

            // Edit2
            saveEdit2(id_item++);

            // SwitchAndEdit1
            id_item = saveSwitchAndEdit1(id_item);

            // ThreeTextThreeEdit1
            saveThreeTextThreeEdit1(id_item);

            //ViewUtils viewUtils = new ViewUtils(rootView, id_rapporto_sopralluogo, selectedIndex);
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

    private void saveThreeTextThreeEdit1(int id_item)
    {
        String str_id_item = et1ThreeTextThreeEdit1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_id_item);

        str_id_item = et2ThreeTextThreeEdit1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_id_item);

        str_id_item = et3ThreeTextThreeEdit1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_id_item);
    }

    private int saveSwitchAndEdit1(int id_item)
    {
        String str_id_item = "";
        if (sw1SwitchAndEdit1.isChecked())
        {
            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, "SI");
        } else
        {
            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, "NO");
        }
        id_item++;

        str_id_item = et1SwitchAndEdit1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_id_item);

        return id_item;
    }

    private void saveEdit2(int id_item)
    {
        String str_id_item = et1Edit2.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, str_id_item);
    }

    private int saveTwoEdits1(int id_item)
    {
        String str_id_item;
        str_id_item = et1TwoEdits1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_id_item);

        str_id_item = et2TwoEdits1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_id_item);
        return id_item;
    }

    private void saveFourRadiosAndEdit1(int id_item)
    {
        int checkedBtnId;
        String str_id_item;
        checkedBtnId = rg1FourRadiosAndEdit1.getCheckedRadioButtonId();
        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg1FourRadiosAndEdit1.findViewById(checkedBtnId);
            str_id_item = radioButton.getText().toString();
        } else
        {
            str_id_item = et1FourRadiosAndEdit1.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, str_id_item);
    }

    private int saveTwoSwitches1(int id_item)
    {
        String str_id_item;
        if (sw1TwoSwitches1.isChecked())
        {
            str_id_item = "SI";
        } else
        {
            str_id_item = "NO";
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_id_item);

        if (sw2TwoSwitches1.isChecked())
        {
            str_id_item = "SI";
        } else
        {
            str_id_item = "NO";
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_id_item);
        return id_item;
    }

    private void saveEdit1(int id_item)
    {
        String str_id_item = et1Edit1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, str_id_item);
    }

    private void saveTwoRadios2(int id_item)
    {
        int checkedBtnId;
        checkedBtnId = rg1TwoRadios2.getCheckedRadioButtonId();
        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg1TwoRadios2.findViewById(checkedBtnId);
            String str_Id_item = radioButton.getText().toString();

            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, str_Id_item);
        }
    }

    private void saveTwoRadios1(int id_item)
    {
        int checkedBtnId;
        String str_id_item;
        checkedBtnId = rg1TwoRadios1.getCheckedRadioButtonId();
        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg1TwoRadios1.findViewById(checkedBtnId);
            str_id_item = radioButton.getText().toString();

            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, str_id_item);
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

        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

        id_rapporto_sopralluogo = reportStates!=null ? reportStates.getId_rapporto_sopralluogo() : -1;

        realm.commitTransaction();

        int idItem = idItemStart;

        // TwoRadios1
        fillTwoRadios1(idItem++);

        // TwoRadios2
        fillTwoRadios2(idItem++);

        // Edit1
        fillEdit1(idItem++);

        // TwoSwitches1
        idItem = fillTwoSwitches1(idItem);

        // FourRadiosAndEdit1
        fillFourRadiosAndEdit1(idItem++);

        // TwoEdits1
        idItem = fillTwoEdits1(idItem);

        // Edit2
        fillEdit2(idItem++);

        // SwitchAndEdit1
        idItem = fillSwitchAndEdit1(idItem);

        // ThreeTextsThreeEdits1
        et1ThreeTextThreeEdit1.setText(DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++));
        et2ThreeTextThreeEdit1.setText(DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++));
        et3ThreeTextThreeEdit1.setText(DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++));
    }

    private int fillSwitchAndEdit1(int idItem)
    {
        String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++);

        if (str_id_item.equals("SI"))
        {
            sw1SwitchAndEdit1.setChecked(true);
        }

        str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++);

        et1SwitchAndEdit1.setText(str_id_item);

        return idItem;
    }

    private void fillEdit2(int idItem)
    {
        String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);
        et1Edit2.setText(str_id_item);
    }

    private int fillTwoEdits1(int idItem)
    {
        String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++);
        et1TwoEdits1.setText(str_id_item);

        str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++);
        et2TwoEdits1.setText(str_id_item);
        return idItem;
    }

    private void fillFourRadiosAndEdit1(int idItem)
    {
        int i;
        String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);
        if (str_id_item != null)
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
    }

    private int fillTwoSwitches1(int idItem)
    {
        String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++);

        if (str_id_item != null && str_id_item.contains("SI"))
        {
            sw1TwoSwitches1.setChecked(true);
        }

        str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++);
        if (str_id_item != null && str_id_item.contains("SI"))
        {
            sw2TwoSwitches1.setChecked(true);
        }
        return idItem;
    }

    private void fillEdit1(int idItem)
    {
        String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);
        et1Edit1.setText(str_id_item);
    }

    private void fillTwoRadios2(int idItem)
    {
        int i;
        String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);

        if (str_id_item != null)
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
    }

    private void fillTwoRadios1(int idItem)
    {
        int i;
        String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);

        if (str_id_item != null)
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


            allSections1Collapsed = !allSections1Collapsed;
        }

        if (view == flSectionHeader2)
        {
            llSectionThreeTextThreeEdit1.setVisibility(!allSections2Collapsed ? View.GONE : View.VISIBLE);

            allSections2Collapsed = !allSections2Collapsed;
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
