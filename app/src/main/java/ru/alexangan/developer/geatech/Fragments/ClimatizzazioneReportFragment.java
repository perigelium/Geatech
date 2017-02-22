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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class ClimatizzazioneReportFragment extends Fragment implements View.OnClickListener
{
    private int selectedIndex;
    int idSopralluogo;
    int id_rapporto_sopralluogo;
    ReportStates reportStates;
    View rootView;
    Context context;
    private boolean allSections1Collapsed, allSections2Collapsed;

    private FrameLayout flSectionHeader1, flSectionHeader2;
    private LinearLayout llHeaderThreeRadiosAndEdit1, llSectionThreeRadiosAndEdit1, llHeaderTwoRadios1, llSectionTwoRadios1,
            llHeaderFourRadiosAndEdit1, llSectionFourRadiosAndEdit1, llHeaderThreeChkboxesAndEdit1, llSectionThreeChkboxesAndEdit1, llSectionThreeTextThreeEdit1;
    private TextView tvReportTitle, tv1ThreeTextThreeEdit1, tv2ThreeTextThreeEdit1, tv3ThreeTextThreeEdit1, tvSectionHeader1,
            tvSectionHeader2, tvHeaderThreeRadiosAndEdit1, tvHeaderTwoRadios1, tvHeaderFourRadiosAndEdit1, tvHeaderThreeChkboxesAndEdit1;
    private RadioGroup rg1ThreeRadiosAndEdit1, rg1TwoRadios1, rg1FourRadiosAndEdit1;
    private EditText et1ThreeRadiosAndEdit1, et1FourRadiosAndEdit1, et1ThreeChkboxesAndEdit1, et1ThreeTextThreeEdit1, et2ThreeTextThreeEdit1, et3ThreeTextThreeEdit1;
    CheckBox chk1ThreeChkboxesAndEdit1, chk2ThreeChkboxesAndEdit1, chk3ThreeChkboxesAndEdit1;
    ImageView ivArrowTwoRadios1, ivArrowThreeRadiosAndEdit1, ivArrowFourRadiosAndEdit1, ivArrowThreeChkboxesAndEdit1;

/*    private final String strReportTitle = "Climatizzatore";

    private final String [] saHeaderTitles = {"1. Rilievo dello stato dei luoghi", "2. Impianto"};

    private final String[] saSectionTitles = new String[] {
            " 1.1 TIPO DI EDIFICIO:",
            " 1.2 TIPOLOGIA COSTRUTTIVA MURATURE:",
            " 1.3 POSIZIONAMENTO UNITÀ ESTERNA:",
            " 1.4 LOCALI E/O PIANI DELL'EDIFICIO:"
    };*/

    private final String[] sa_id_item_72 =  {"Appartamento", "Villa (Singola/Multi)", "Negozio"};

    private final String[] sa_id_item_73 =  {"Cemento Armato", "Mattoni Pieni", "Mattoni Forati", "Pietra"};

    private final String[] sa_id_item_74 =  {"A Parete", "A Pavimento"};

    private final String[] sa_id_item_75 =  {"Interrato", "Piano rialzato", "Piano Terra"};

/*    private final String[] strImpianto =  {
    " 2.1 NOTE SUL LUOGO DI INSTALLAZIONE E INDIVIDUAZIONE DI EVENTUALI CRITICITÀ SUI COLLEGAMENTI IDRAULICI E/O SULLA GESTIONE DELL'UTENZA:",
    " 2.2 NOTE SULLA TIPOLOGIA DELL'IMPIANTO ESISTENTE (CALDAIA, ELEMENTI RADIANTI, TUBAZIONI, ETC.)\n" +
      "DESCRIVERE COMA VERRÀ INTERFACCIATO IL SISTEMA ALL'UTENZA PRINCIPALEE I COMPONENTI CHE VERRANNO UTILIZZATI:",
    "2.3 NOTE RELATIVE A COLLEGAMENTO DELL'IMPIANTO ESISTENTE"
    };*/

    public ClimatizzazioneReportFragment()
    {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportStates != null)
        {
                // ThreeRadiosAndEdit1
                String str_Id_item = "";
                int checkedBtnId = rg1ThreeRadiosAndEdit1.getCheckedRadioButtonId();
                if (checkedBtnId != -1)
                {
                    RadioButton radioButton = (RadioButton) rg1ThreeRadiosAndEdit1.findViewById(checkedBtnId);
                    str_Id_item = radioButton.getText().toString();
                }
                else
                {
                    str_Id_item = et1ThreeRadiosAndEdit1.getText().toString();
                }
                insertStringInReportItem(72, str_Id_item);

                // FourRadiosAndEdit1
                str_Id_item = "";
                checkedBtnId = rg1FourRadiosAndEdit1.getCheckedRadioButtonId();
                if (checkedBtnId != -1)
                {
                    RadioButton radioButton = (RadioButton) rg1FourRadiosAndEdit1.findViewById(checkedBtnId);
                    str_Id_item = radioButton.getText().toString();
                }
                else
                {
                    str_Id_item = et1FourRadiosAndEdit1.getText().toString();
                }
            insertStringInReportItem(73, str_Id_item);

            // TwoRadios1
            str_Id_item = "";
            checkedBtnId = rg1TwoRadios1.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rg1TwoRadios1.findViewById(checkedBtnId);
                str_Id_item = radioButton.getText().toString();

                insertStringInReportItem(74, str_Id_item);
            }

            // ThreeChkboxesAndEdit1
            str_Id_item = et1ThreeChkboxesAndEdit1.getText().toString();

            if(str_Id_item == null || str_Id_item.length() < 4)
            {
                String strChk1 = chk1ThreeChkboxesAndEdit1.isChecked() ? chk1ThreeChkboxesAndEdit1.getText().toString() : "";
                String strChk2 = chk2ThreeChkboxesAndEdit1.isChecked() ? chk2ThreeChkboxesAndEdit1.getText().toString() : "";
                String strChk3 = chk3ThreeChkboxesAndEdit1.isChecked() ? chk3ThreeChkboxesAndEdit1.getText().toString() : "";
                insertStringInReportItem(75, strChk1 + " " + strChk2 + " " + strChk3);
            }
            else
            {
                insertStringInReportItem(75, str_Id_item);
            }

            // ThreeTextThreeEdit1
            str_Id_item = et1ThreeTextThreeEdit1.getText().toString();
            insertStringInReportItem(76, str_Id_item);


            str_Id_item = et2ThreeTextThreeEdit1.getText().toString();
            insertStringInReportItem(77, str_Id_item);


            str_Id_item = et3ThreeTextThreeEdit1.getText().toString();
            insertStringInReportItem(78, str_Id_item);

            // Completion state
            if(getValueFromReportItem(72).length() != 0)
            {
                realm.beginTransaction();
                reportStates.setReportCompletionState(1);
                realm.commitTransaction();

                if (getValueFromReportItem(74).length() != 0 || getValueFromReportItem(73).length() != 0)
                {
                    realm.beginTransaction();
                    reportStates.setReportCompletionState(2);
                    realm.commitTransaction();

                    if (getValueFromReportItem(72).length() != 0 && getValueFromReportItem(74).length() != 0 && getValueFromReportItem(73).length() != 0)
                    {
                        realm.beginTransaction();
                        reportStates.setReportCompletionState(3);

                        Calendar calendarNow = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                        String strDateTime = sdf.format(calendarNow.getTime());
                        reportStates.setDataOraRaportoCompletato(strDateTime);
                        realm.commitTransaction();
                    }
                }
            }
            else
            {
                realm.beginTransaction();
                reportStates.setReportCompletionState(0);
                reportStates.setDataOraRaportoCompletato(null);
                realm.commitTransaction();
            }
        }

        if (reportStates == null)
        {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        realm.beginTransaction();

        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();

        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        id_rapporto_sopralluogo = reportStates.getId_rapporto_sopralluogo();

        realm.commitTransaction();

            int i;

            // ThreeRadiosAndEdit1
            String strId_item = getValueFromReportItem(72);

            if(strId_item != null)
            {
                for (i = 0; i < rg1ThreeRadiosAndEdit1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1ThreeRadiosAndEdit1.getChildAt(i);

                    if (strId_item.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }

                if (i == rg1ThreeRadiosAndEdit1.getChildCount())
                {
                    et1ThreeRadiosAndEdit1.setText(strId_item);
                }
            }

            // FourRadiosAndEdit1
            strId_item = getValueFromReportItem(73);

            if(strId_item != null)
            {
                for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

                    if (strId_item.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }

                if (i == rg1FourRadiosAndEdit1.getChildCount())
                {
                    et1FourRadiosAndEdit1.setText(strId_item);
                }
            }

            // TwoRadios1
            strId_item = getValueFromReportItem(74);

            if(strId_item !=null)
            {
                for (i = 0; i < rg1TwoRadios1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

                    if (strId_item.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }
            }

            // ThreeChkboxesAndEdit1
            String strValue = getValueFromReportItem(75);

            if(strValue!=null)
            {
                String strChk1 = chk1ThreeChkboxesAndEdit1.getText().toString();
                if (strValue.contains(strChk1))
                {
                    chk1ThreeChkboxesAndEdit1.setChecked(true);
                    strValue = strValue.replace(strChk1, "");
                }

                String strChk2 = chk2ThreeChkboxesAndEdit1.getText().toString();
                if (strValue.contains(strChk2))
                {
                    chk2ThreeChkboxesAndEdit1.setChecked(true);
                    strValue = strValue.replace(strChk2, "");
                }

                String strChk3 = chk3ThreeChkboxesAndEdit1.getText().toString();
                if (strValue.contains(strChk3))
                {
                    chk3ThreeChkboxesAndEdit1.setChecked(true);
                    strValue = strValue.replace(strChk3, "");
                }
                et1ThreeChkboxesAndEdit1.setText(strValue);
            }

            et1ThreeTextThreeEdit1.setText(getValueFromReportItem(76));
            et2ThreeTextThreeEdit1.setText(getValueFromReportItem(77));
            et3ThreeTextThreeEdit1.setText(getValueFromReportItem(78));
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView =  inflater.inflate(R.layout.climatizzazione_report, container, false);


        realm.beginTransaction();
        GeaModelloRapporto geaModello = realm.where(GeaModelloRapporto.class).equalTo("nome_modello", "CLIMATIZZATORE").findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        List<GeaSezioneModelliRapporto> geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class).equalTo("id_modello", geaModello.getId_modello()).findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        List<GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class)
                .between("id_sezione", geaSezioniModelli.get(0).getId_sezione(), geaSezioniModelli.get(geaSezioniModelli.size()-1).getId_sezione())
                .between("id_item_modello", 72, 78).findAll();
        realm.commitTransaction();

        int headerNumber = 0;
        int sectionNumber = 0;

        tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        LinearLayout header1 = (LinearLayout) rootView.findViewById(R.id.headerClima1);
        flSectionHeader1 = (FrameLayout) header1.findViewById(R.id.flSectionHeader1);
        flSectionHeader1.setOnClickListener(this);

        tvSectionHeader1 = (TextView) header1.findViewById(R.id.tvSectionHeader1);
        tvSectionHeader1.setText(geaSezioniModelli.get(headerNumber++).getDescrizione_sezione());

        // ThreeRadiosAndEdit1
        llHeaderThreeRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llHeaderThreeRadiosAndEdit1);
        llHeaderThreeRadiosAndEdit1.setOnClickListener(this);

        ivArrowThreeRadiosAndEdit1 = (ImageView) rootView.findViewById(R.id.ivArrowThreeRadiosAndEdit1);

        tvHeaderThreeRadiosAndEdit1 = (TextView) rootView.findViewById(R.id.tvHeaderThreeRadiosAndEdit1);
        tvHeaderThreeRadiosAndEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionThreeRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionThreeRadiosAndEdit1);

        rg1ThreeRadiosAndEdit1 = (RadioGroup) rootView.findViewById(R.id.rg1ThreeRadiosAndEdit1);
        int i;
        for (i = 0; i < rg1ThreeRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1ThreeRadiosAndEdit1.getChildAt(i);

            rb.setText(sa_id_item_72[i]);
        }

        et1ThreeRadiosAndEdit1 = (EditText) rootView.findViewById(R.id.et1ThreeRadiosAndEdit1);
        et1ThreeRadiosAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1ThreeRadiosAndEdit1.clearCheck();
                return false;
            }
        });


        // FourRadiosAndEdit1
        llHeaderFourRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llHeaderFourRadiosAndEdit1);
        llHeaderFourRadiosAndEdit1.setOnClickListener(this);

        ivArrowFourRadiosAndEdit1 = (ImageView) rootView.findViewById(R.id.ivArrowFourRadiosAndEdit1);

        tvHeaderFourRadiosAndEdit1 = (TextView) rootView.findViewById(R.id.tvHeaderFourRadiosAndEdit1);
        tvHeaderFourRadiosAndEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionFourRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionFourRadiosAndEdit1);

        rg1FourRadiosAndEdit1 = (RadioGroup) rootView.findViewById(R.id.rg1FourRadiosAndEdit1);

        for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

            rb.setText(sa_id_item_73[i]);
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

            rb.setText(sa_id_item_74[i]);
        }


        // ThreeChkboxesAndEdit1
        llHeaderThreeChkboxesAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llHeaderThreeChkboxesAndEdit1);
        llHeaderThreeChkboxesAndEdit1.setOnClickListener(this);

        ivArrowThreeChkboxesAndEdit1 = (ImageView) rootView.findViewById(R.id.ivArrowThreeChkboxesAndEdit1);

        tvHeaderThreeChkboxesAndEdit1 = (TextView) rootView.findViewById(R.id.tvHeaderThreeChkboxesAndEdit1);
        tvHeaderThreeChkboxesAndEdit1.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        llSectionThreeChkboxesAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionThreeChkboxesAndEdit1);

            chk1ThreeChkboxesAndEdit1 = (CheckBox) rootView.findViewById(R.id.chk1ThreeChkboxesAndEdit1);
        chk1ThreeChkboxesAndEdit1.setText(sa_id_item_75[0]);
            chk2ThreeChkboxesAndEdit1 = (CheckBox) rootView.findViewById(R.id.chk2ThreeChkboxesAndEdit1);
        chk2ThreeChkboxesAndEdit1.setText(sa_id_item_75[1]);
            chk3ThreeChkboxesAndEdit1 = (CheckBox) rootView.findViewById(R.id.chk3ThreeChkboxesAndEdit1);
        chk3ThreeChkboxesAndEdit1.setText(sa_id_item_75[2]);

        et1ThreeChkboxesAndEdit1 = (EditText) rootView.findViewById(R.id.et1ThreeChkboxesAndEdit1);
        et1ThreeChkboxesAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                chk1ThreeChkboxesAndEdit1.setChecked(false);
                chk2ThreeChkboxesAndEdit1.setChecked(false);
                chk3ThreeChkboxesAndEdit1.setChecked(false);
                return false;
            }
        });


        // SectionHeader2
        LinearLayout header2 = (LinearLayout) rootView.findViewById(R.id.headerClima2);
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
            llSectionThreeRadiosAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionTwoRadios1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionFourRadiosAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);
            llSectionThreeChkboxesAndEdit1.setVisibility(!allSections1Collapsed ? View.GONE : View.VISIBLE);

            allSections1Collapsed=!allSections1Collapsed;
        }

        if (view == flSectionHeader2)
        {
            llSectionThreeTextThreeEdit1.setVisibility(!allSections2Collapsed ? View.GONE : View.VISIBLE);

            allSections2Collapsed=!allSections2Collapsed;
        }

        if (view == llHeaderThreeRadiosAndEdit1)
        {
            llSectionThreeRadiosAndEdit1.setVisibility(llSectionThreeRadiosAndEdit1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowThreeRadiosAndEdit1.setImageResource(llSectionThreeRadiosAndEdit1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderTwoRadios1)
        {
            llSectionTwoRadios1.setVisibility(llSectionTwoRadios1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowTwoRadios1.setImageResource(llSectionTwoRadios1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderFourRadiosAndEdit1)
        {
            llSectionFourRadiosAndEdit1.setVisibility(llSectionFourRadiosAndEdit1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowFourRadiosAndEdit1.setImageResource(llSectionFourRadiosAndEdit1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderThreeChkboxesAndEdit1)
        {
            llSectionThreeChkboxesAndEdit1.setVisibility(llSectionThreeChkboxesAndEdit1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowThreeChkboxesAndEdit1.setImageResource(llSectionThreeChkboxesAndEdit1.getVisibility() == View.VISIBLE
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
            return "";
        }
    }
}
