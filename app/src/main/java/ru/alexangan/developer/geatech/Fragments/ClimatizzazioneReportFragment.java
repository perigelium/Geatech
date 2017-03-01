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
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.DatabaseUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class ClimatizzazioneReportFragment extends Fragment implements View.OnClickListener
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
    private LinearLayout llHeaderThreeRadiosAndEdit1, llSectionThreeRadiosAndEdit1, llHeaderTwoRadios1, llSectionTwoRadios1,
            llHeaderFourRadiosAndEdit1, llSectionFourRadiosAndEdit1, llHeaderThreeChkboxesAndEdit1, llSectionThreeChkboxesAndEdit1,
            llSectionThreeTextThreeEdit1;

    private RadioGroup rg1ThreeRadiosAndEdit1, rg1TwoRadios1, rg1FourRadiosAndEdit1;

    private EditText et1ThreeRadiosAndEdit1, et1FourRadiosAndEdit1, et1ThreeChkboxesAndEdit1, et1ThreeTextThreeEdit1,
            et2ThreeTextThreeEdit1, et3ThreeTextThreeEdit1;

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

    private final String[] sa_id_item_72 = {"Appartamento", "Villa (Singola/Multi)", "Negozio"};

    private final String[] sa_id_item_73 = {"Cemento Armato", "Mattoni Pieni", "Mattoni Forati", "Pietra"};

    private final String[] sa_id_item_74 = {"A Parete", "A Pavimento"};

    private final String[] sa_id_item_75 = {"Interrato", "Piano rialzato", "Piano Terra"};

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

        idItemStart = 72;
        idItemEnd = 79; // first not included id

        realm.beginTransaction();
        geaModello = realm.where(GeaModelloRapporto.class).equalTo("nome_modello", "CLIMATIZZATORE").findFirst();
        realm.commitTransaction();

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
        rootView = inflater.inflate(R.layout.climatizzazione_report, container, false);

        int headerNumber = 0;
        int sectionNumber = 0;

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        createViewSectionHeader1(headerNumber++);

        // ThreeRadiosAndEdit1
        createViewThreeRadiosAndEdit1(sectionNumber++);

        // FourRadiosAndEdit1
        createViewFourRadiosAndEdit1(sectionNumber++);

        // TwoRadios1
        createViewTwoRadios1(sectionNumber++);

        // ThreeChkboxesAndEdit1
        createViewThreeChkboxesAndEdit1(sectionNumber++);

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

    private void createViewSectionHeader1(int headerNumber)
    {
        LinearLayout header1 = (LinearLayout) rootView.findViewById(R.id.header1);
        flSectionHeader1 = (FrameLayout) header1.findViewById(R.id.flSectionHeader);
        flSectionHeader1.setOnClickListener(this);

        TextView tvSectionHeader1 = (TextView) header1.findViewById(R.id.tvSectionHeader);
        tvSectionHeader1.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
    }

    private void createViewSectionHeader2(int headerNumber)
    {
        LinearLayout header2 = (LinearLayout) rootView.findViewById(R.id.header2);
        flSectionHeader2 = (FrameLayout) header2.findViewById(R.id.flSectionHeader);
        flSectionHeader2.setOnClickListener(this);

        TextView tvSectionHeader2 = (TextView) header2.findViewById(R.id.tvSectionHeader);
        tvSectionHeader2.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
    }

    private void createViewThreeChkboxesAndEdit1(int sectionNumber)
    {
        LinearLayout three_chkboxes_and_edit1 = (LinearLayout) rootView.findViewById(R.id.three_chkboxes_and_edit1);
        llHeaderThreeChkboxesAndEdit1 = (LinearLayout) three_chkboxes_and_edit1.findViewById(R.id.llHeaderThreeChkboxesAndEdit);
        llHeaderThreeChkboxesAndEdit1.setOnClickListener(this);

        ivArrowThreeChkboxesAndEdit1 = (ImageView) three_chkboxes_and_edit1.findViewById(R.id.ivArrowThreeChkboxesAndEdit);

        TextView tvHeaderThreeChkboxesAndEdit1 = (TextView) three_chkboxes_and_edit1.findViewById(R.id.tvHeaderThreeChkboxesAndEdit);
        tvHeaderThreeChkboxesAndEdit1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        llSectionThreeChkboxesAndEdit1 = (LinearLayout) three_chkboxes_and_edit1.findViewById(R.id.llSectionThreeChkboxesAndEdit);

        chk1ThreeChkboxesAndEdit1 = (CheckBox) three_chkboxes_and_edit1.findViewById(R.id.chk1ThreeChkboxesAndEdit);
        chk1ThreeChkboxesAndEdit1.setText(sa_id_item_75[0]);
        chk2ThreeChkboxesAndEdit1 = (CheckBox) three_chkboxes_and_edit1.findViewById(R.id.chk2ThreeChkboxesAndEdit);
        chk2ThreeChkboxesAndEdit1.setText(sa_id_item_75[1]);
        chk3ThreeChkboxesAndEdit1 = (CheckBox) three_chkboxes_and_edit1.findViewById(R.id.chk3ThreeChkboxesAndEdit);
        chk3ThreeChkboxesAndEdit1.setText(sa_id_item_75[2]);

        et1ThreeChkboxesAndEdit1 = (EditText) three_chkboxes_and_edit1.findViewById(R.id.et1ThreeChkboxesAndEdit);
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
    }

    private void createViewTwoRadios1(int sectionNumber)
    {
        LinearLayout two_radios1 = (LinearLayout) rootView.findViewById(R.id.two_radios1);
        llHeaderTwoRadios1 = (LinearLayout) two_radios1.findViewById(R.id.llHeaderTwoRadios);
        llHeaderTwoRadios1.setOnClickListener(this);

        TextView tvHeaderTwoRadios1 = (TextView) two_radios1.findViewById(R.id.tvHeaderTwoRadios);
        tvHeaderTwoRadios1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ivArrowTwoRadios1 = (ImageView) two_radios1.findViewById(R.id.ivArrowTwoRadios);

        llSectionTwoRadios1 = (LinearLayout) two_radios1.findViewById(R.id.llSectionTwoRadios);

        rg1TwoRadios1 = (RadioGroup) two_radios1.findViewById(R.id.rg1TwoRadios);

        for (int i = 0; i < rg1TwoRadios1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

            rb.setText(sa_id_item_74[i]);
        }
    }

    private void createViewFourRadiosAndEdit1(int sectionNumber)
    {
        LinearLayout four_radios_and_edit1 = (LinearLayout) rootView.findViewById(R.id.four_radios_and_edit1);
        llHeaderFourRadiosAndEdit1 = (LinearLayout) four_radios_and_edit1.findViewById(R.id.llHeaderFourRadiosAndEdit);
        llHeaderFourRadiosAndEdit1.setOnClickListener(this);

        ivArrowFourRadiosAndEdit1 = (ImageView) four_radios_and_edit1.findViewById(R.id.ivArrowFourRadiosAndEdit);

        TextView tvHeaderFourRadiosAndEdit1 = (TextView) four_radios_and_edit1.findViewById(R.id.tvHeaderFourRadiosAndEdit);
        tvHeaderFourRadiosAndEdit1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        llSectionFourRadiosAndEdit1 = (LinearLayout) four_radios_and_edit1.findViewById(R.id.llSectionFourRadiosAndEdit);

        rg1FourRadiosAndEdit1 = (RadioGroup) four_radios_and_edit1.findViewById(R.id.rg1FourRadiosAndEdit);

        for (int i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

            rb.setText(sa_id_item_73[i]);
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

    private void createViewThreeRadiosAndEdit1(int sectionNumber)
    {
        LinearLayout three_radios_and_edit1 = (LinearLayout) rootView.findViewById(R.id.three_radios_and_edit1);

        llHeaderThreeRadiosAndEdit1 = (LinearLayout) three_radios_and_edit1.findViewById(R.id.llHeaderThreeRadiosAndEdit);
        llHeaderThreeRadiosAndEdit1.setOnClickListener(this);

        ivArrowThreeRadiosAndEdit1 = (ImageView) three_radios_and_edit1.findViewById(R.id.ivArrowThreeRadiosAndEdit);

        TextView tvHeaderThreeRadiosAndEdit1 = (TextView) three_radios_and_edit1.findViewById(R.id.tvHeaderThreeRadiosAndEdit);
        tvHeaderThreeRadiosAndEdit1.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        llSectionThreeRadiosAndEdit1 = (LinearLayout) three_radios_and_edit1.findViewById(R.id.llSectionThreeRadiosAndEdit);

        rg1ThreeRadiosAndEdit1 = (RadioGroup) three_radios_and_edit1.findViewById(R.id.rg1ThreeRadiosAndEdit);

        for (int i = 0; i < rg1ThreeRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1ThreeRadiosAndEdit1.getChildAt(i);

            rb.setText(sa_id_item_72[i]);
        }

        et1ThreeRadiosAndEdit1 = (EditText) three_radios_and_edit1.findViewById(R.id.et1ThreeRadiosAndEdit);
        et1ThreeRadiosAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1ThreeRadiosAndEdit1.clearCheck();
                return false;
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportStates != null)
        {
            int id_item = idItemStart;

            // ThreeRadiosAndEdit1
            saveThreeRadiosAndEdit1(id_item++);

            // FourRadiosAndEdit1
            saveFourRadiosAndEdit1(id_item++);

            // TwoRadios1
            saveTwoRadios1(id_item++);

            // ThreeChkboxesAndEdit1
            saveThreeChkboxesAndEdit1(id_item++);

            // ThreeTextThreeEdit1
            saveThreeTextThreeEdit1(id_item);

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

        if (reportStates == null)
        {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    private void saveThreeTextThreeEdit1(int id_item)
    {
        String str_Id_item;
        str_Id_item = et1ThreeTextThreeEdit1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_Id_item);


        str_Id_item = et2ThreeTextThreeEdit1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_Id_item);


        str_Id_item = et3ThreeTextThreeEdit1.getText().toString();
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item++, str_Id_item);
    }

    private void saveThreeChkboxesAndEdit1(int id_item)
    {
        String str_Id_item = et1ThreeChkboxesAndEdit1.getText().toString();

        if (str_Id_item.length() < 4)
        {
            String strChk1 = chk1ThreeChkboxesAndEdit1.isChecked() ? chk1ThreeChkboxesAndEdit1.getText().toString() : "";
            String strChk2 = chk2ThreeChkboxesAndEdit1.isChecked() ? chk2ThreeChkboxesAndEdit1.getText().toString() : "";
            String strChk3 = chk3ThreeChkboxesAndEdit1.isChecked() ? chk3ThreeChkboxesAndEdit1.getText().toString() : "";
            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, strChk1 + " " + strChk2 + " " + strChk3);
        } else
        {
            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, str_Id_item);
        }
    }

    private void saveTwoRadios1(int id_item)
    {
        int checkedBtnId;
        String str_Id_item;
        checkedBtnId = rg1TwoRadios1.getCheckedRadioButtonId();
        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg1TwoRadios1.findViewById(checkedBtnId);
            str_Id_item = radioButton.getText().toString();

            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, str_Id_item);
        }
    }

    private void saveFourRadiosAndEdit1(int id_item)
    {
        String str_Id_item;
        int checkedBtnId;
        checkedBtnId = rg1FourRadiosAndEdit1.getCheckedRadioButtonId();
        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg1FourRadiosAndEdit1.findViewById(checkedBtnId);
            str_Id_item = radioButton.getText().toString();
        } else
        {
            str_Id_item = et1FourRadiosAndEdit1.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, str_Id_item);
    }

    private void saveThreeRadiosAndEdit1(int id_item)
    {
        String str_Id_item;
        int checkedBtnId = rg1ThreeRadiosAndEdit1.getCheckedRadioButtonId();
        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg1ThreeRadiosAndEdit1.findViewById(checkedBtnId);
            str_Id_item = radioButton.getText().toString();
        } else
        {
            str_Id_item = et1ThreeRadiosAndEdit1.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, id_item, str_Id_item);
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

        id_rapporto_sopralluogo = reportStates!=null ? reportStates.getId_rapporto_sopralluogo() : -1;

        realm.commitTransaction();

        int idItem = idItemStart;

        if(id_rapporto_sopralluogo != -1)
        {
            // ThreeRadiosAndEdit1
            fillThreeRadiosAndEdit1(idItem++);

            // FourRadiosAndEdit1
            fillFourRadiosAndEdit1(idItem++);

            // TwoRadios1
            fillTwoRadios1(idItem++);

            // ThreeChkboxesAndEdit1
            fillThreeChkboxesAndEdit1(idItem++);

            et1ThreeTextThreeEdit1.setText(DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++));
            et2ThreeTextThreeEdit1.setText(DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++));
            et3ThreeTextThreeEdit1.setText(DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem++));
        }
    }

    private void fillThreeChkboxesAndEdit1(int idItem)
    {
        String strValue = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);

        if (strValue != null)
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
    }

    private void fillTwoRadios1(int idItem)
    {
        String strId_item;
        int i;
        strId_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);

        if (strId_item != null)
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
    }

    private void fillFourRadiosAndEdit1(int idItem)
    {
        String strId_item;
        int i;
        strId_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);

        if (strId_item != null)
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
    }

    private void fillThreeRadiosAndEdit1(int idItem)
    {
        int i;
        String strId_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);

        if (strId_item != null)
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

            allSections1Collapsed = !allSections1Collapsed;
        }

        if (view == flSectionHeader2)
        {
            llSectionThreeTextThreeEdit1.setVisibility(!allSections2Collapsed ? View.GONE : View.VISIBLE);

            allSections2Collapsed = !allSections2Collapsed;
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
}
