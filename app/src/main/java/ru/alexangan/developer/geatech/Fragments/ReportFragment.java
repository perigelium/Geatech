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

public class ReportFragment extends Fragment implements View.OnClickListener
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
            llHeaderFourRadiosAndEdit1, llSectionFourRadiosAndEdit1, llHeaderThreeChkboxesAndEdit1, llSectionThreeChkboxesAndEdit1, llSectionThreeTextThreeEdit1;
    private TextView tvReportTitle, tv1ThreeTextThreeEdit1, tv2ThreeTextThreeEdit1, tv3ThreeTextThreeEdit1, tvSectionHeader1,
            tvSectionHeader2, tvHeaderThreeRadiosAndEdit1, tvHeaderTwoRadios1, tvHeaderFourRadiosAndEdit1, tvHeaderThreeChkboxesAndEdit1;
    private RadioGroup rg1ThreeRadiosAndEdit1, rg1TwoRadios1, rg1FourRadiosAndEdit1;
    private EditText et1ThreeRadiosAndEdit1, et1FourRadiosAndEdit1, et1ThreeChkboxesAndEdit1, et1ThreeTextThreeEdit1, et2ThreeTextThreeEdit1, et3ThreeTextThreeEdit1;
    CheckBox chk1ThreeChkboxesAndEdit1, chk2ThreeChkboxesAndEdit1, chk3ThreeChkboxesAndEdit1;
    ImageView ivArrowTwoRadios1, ivArrowThreeRadiosAndEdit1, ivArrowFourRadiosAndEdit1, ivArrowThreeChkboxesAndEdit1;

    public ReportFragment()
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

        idItemStart = 1;
        idItemEnd = 152; // first not included id

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
        rootView = inflater.inflate(R.layout.climatizzazione_report, container, false);

        int headerNumber = 0;
        int sectionNumber = 0;

        tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        // SectionHeader1
        createViewSectionHeader1(headerNumber++);


        // SectionHeader2
        createViewSectionHeader2(headerNumber++);


        return rootView;
    }

    private void createViewSectionHeader1(int headerNumber)
    {
        LinearLayout header1 = (LinearLayout) rootView.findViewById(R.id.headerClima1);
        flSectionHeader1 = (FrameLayout) header1.findViewById(R.id.flSectionHeader1);
        flSectionHeader1.setOnClickListener(this);

        tvSectionHeader1 = (TextView) header1.findViewById(R.id.tvSectionHeader1);
        tvSectionHeader1.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
    }

    private void createViewSectionHeader2(int headerNumber)
    {
        LinearLayout header2 = (LinearLayout) rootView.findViewById(R.id.headerClima2);
        flSectionHeader2 = (FrameLayout) header2.findViewById(R.id.flSectionHeader1);
        flSectionHeader2.setOnClickListener(this);

        tvSectionHeader2 = (TextView) header2.findViewById(R.id.tvSectionHeader1);
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
