package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.DatabaseUtils;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class FotovoltaicoReportFragment extends Fragment
{
    private int selectedVisitId;
    int id_sopralluogo;
    int id_rapporto_sopralluogo;
    ReportItem reportItem;
    View rootView;
    Context context;
    ViewUtils viewUtils;

    GeaModelloRapporto geaModello;
    private Realm realm;

    public FotovoltaicoReportFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getActivity();
        realm = Realm.getDefaultInstance();

        if (getArguments() != null)
        {
            selectedVisitId = getArguments().getInt("selectedVisitId");
        }

        VisitItem visitItem = visitItems.get(selectedVisitId);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        id_sopralluogo = geaSopralluogo.getId_sopralluogo();
        int id_product_type = productData.getIdProductType();

        realm.beginTransaction();
        geaModello = realm.where(GeaModelloRapporto.class).equalTo("id_product_type", id_product_type).findFirst();
        realm.commitTransaction();

        if (geaModello == null)
        {
            return;
        }

        realm.beginTransaction();
        reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", id_sopralluogo).findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        id_rapporto_sopralluogo = reportItem != null ? reportItem.getGea_rapporto().getId_rapporto_sopralluogo() : -1;

        realm.commitTransaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fotovoltaico_report, container, false);

        viewUtils = new ViewUtils(rootView, id_rapporto_sopralluogo, selectedVisitId);

        TextView tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(geaModello.getNome_modello());

        int idItem = viewUtils.getIdItemStart();


        idItem = viewUtils.createViewTwoRadiosAndEdit(idItem, R.id.two_radios_and_edit1);

        idItem = viewUtils.createViewEdit(idItem, R.id.edit1);

        EditText et = viewUtils.getEditTexts().get(idItem - 1);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);

        idItem = viewUtils.createViewFiveChkboxes(idItem, R.id.five_chkboxes1);

        idItem = viewUtils.createViewFourRadiosAndEdit(idItem, R.id.four_radios_and_edit1);

        idItem = viewUtils.createViewFourRadiosAndEdit(idItem, R.id.four_radios_and_edit2);

        idItem = viewUtils.createViewFourRadiosAndEditObl(idItem, R.id.four_radios_and_edit3);

        idItem = viewUtils.createViewSwitch(idItem, R.id.switch_1);

        idItem = viewUtils.createViewTwoRadios(idItem, R.id.two_radios1);

        idItem = viewUtils.createViewThreeRadiosAndEditEx(idItem, R.id.three_radios_and_edit1);

        idItem = viewUtils.createViewFourRadios(idItem, R.id.four_radios1);

        idItem = viewUtils.createViewTwoSwitchesAndEdit(idItem, R.id.two_switches_and_edit1);

/*        EditText et2 = viewUtils.getEditTexts().get(idItem-1);
        et2.setVisibility(View.INVISIBLE);*/

        // SectionHeader1
        viewUtils.createViewSectionHeader(R.id.header1);

        idItem = viewUtils.createViewTwoTextsTwoEdits(idItem, R.id.two_texts_two_edits1);

        // SectionHeader2
        viewUtils.createViewSectionHeader(R.id.header2);

        idItem = viewUtils.createViewFiveSwitches(idItem, R.id.five_switches1);

        // SectionHeader3
        viewUtils.createViewSectionHeader(R.id.header3);

        return viewUtils.getRootView();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportItem != null)
        {
            int idItem = viewUtils.getIdItemStart();

            idItem = viewUtils.saveSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralChkboxes(idItem);

            idItem = viewUtils.saveSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.saveSeveralRadiosAndEdit(idItem);

            //idItem = viewUtils.saveSeveralEdits(idItem, 4);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem+=2;

            idItem = viewUtils.saveSeveralSwitches(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralRadios(idItem);
            idItem = viewUtils.saveSeveralEdits(idItem, 1);

            idItem = viewUtils.saveSeveralRadios(idItem);

            idItem = viewUtils.saveSeveralSwitches(idItem, 2);
            idItem = viewUtils.saveSeveralEdits(idItem, 1);


            idItem = viewUtils.saveSeveralEdits(idItem, 2);

            idItem = viewUtils.saveSeveralSwitches(idItem, 5);


            // Completion state

            int completionState = DatabaseUtils.getReportInitializationState(id_sopralluogo, id_rapporto_sopralluogo);

            if (completionState == ReportStates.REPORT_COMPLETED)
            {
                realm.beginTransaction();

                Calendar calendarNow = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                String strDateTime = sdf.format(calendarNow.getTime());

                reportItem.getGea_rapporto().setData_ora_compilazione_rapporto(strDateTime);

                realm.commitTransaction();
            }

            realm.beginTransaction();
            reportItem.getReportStates().setReportCompletionState(completionState);
            realm.commitTransaction();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        fillViewItems();
    }

    private void fillViewItems()
    {
        int idItem = viewUtils.getIdItemStart();

        if (id_rapporto_sopralluogo != -1)
        {
            idItem = viewUtils.fillSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            idItem = viewUtils.fillSeveralChkboxes(idItem);

            idItem = viewUtils.fillSeveralRadiosAndEdit(idItem);

            idItem = viewUtils.fillSeveralRadiosAndEdit(idItem);

            //idItem = viewUtils.fillSeveralEdits(idItem, 4);
            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            //final EditText et1FourRadiosAndEdit = viewUtils.getEditTexts().get(idItem - 1);

            idItem+=2;

            idItem = viewUtils.fillSeveralSwitches(idItem, 1);

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralRadios(idItem);
            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            final RadioGroup rg1ThreeRadiosAndEdit = viewUtils.getRadioGroups().get(idItem - 2);
            final LinearLayout llEditThreeRadiosAndEdit = viewUtils.getLinearLayouts().get(idItem - 1).first;
            final EditText et1ThreeRadiosAndEdit = viewUtils.getEditTexts().get(idItem - 1);

            for (int i = 0; i < rg1ThreeRadiosAndEdit.getChildCount(); i++)
            {
                if (rg1ThreeRadiosAndEdit.getCheckedRadioButtonId() == rg1ThreeRadiosAndEdit.getChildAt(1).getId()) //second radio
                {
                    llEditThreeRadiosAndEdit.setVisibility(View.VISIBLE);
                    llEditThreeRadiosAndEdit.setEnabled(true);
                } else
                {
                    llEditThreeRadiosAndEdit.setVisibility(View.GONE);
                    llEditThreeRadiosAndEdit.setEnabled(false);
                }

                RadioButton rb = (RadioButton) rg1ThreeRadiosAndEdit.getChildAt(i);

                rb.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (rg1ThreeRadiosAndEdit.getCheckedRadioButtonId() == rg1ThreeRadiosAndEdit.getChildAt(1).getId()) //second radio
                        {
                            llEditThreeRadiosAndEdit.setVisibility(View.VISIBLE);
                            llEditThreeRadiosAndEdit.setEnabled(true);
                            et1ThreeRadiosAndEdit.setText("");
                        } else
                        {
                            llEditThreeRadiosAndEdit.setVisibility(View.GONE);
                            llEditThreeRadiosAndEdit.setEnabled(false);
                            et1ThreeRadiosAndEdit.setText("Non applicabile");
                        }
                    }
                });
            }

            idItem = viewUtils.fillSeveralRadios(idItem);

            idItem = viewUtils.fillSeveralSwitches(idItem, 2);
            idItem = viewUtils.fillSeveralEdits(idItem, 1);

            final EditText et3 = viewUtils.getEditTexts().get(idItem - 1);
            et3.setInputType(InputType.TYPE_CLASS_NUMBER);
            final Switch sw1 = viewUtils.getSwitches().get(idItem - 2);
            final LinearLayout llEdit1 = viewUtils.getLinearLayouts().get(idItem - 1).first;

            if (!sw1.isChecked())
            {
                llEdit1.setVisibility(View.GONE);
                llEdit1.setEnabled(false);
            } else
            {
                llEdit1.setVisibility(View.VISIBLE);
                llEdit1.setEnabled(true);
            }

            sw1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (sw1.isChecked())
                    {
                        llEdit1.setVisibility(View.VISIBLE);
                        llEdit1.setEnabled(true);
                        et3.setText("");
                    } else
                    {
                        llEdit1.setVisibility(View.GONE);
                        llEdit1.setEnabled(false);
                        et3.setText("Non applicabile");
                    }
                }
            });

            idItem = viewUtils.fillSeveralEdits(idItem, 2);

            idItem = viewUtils.fillSeveralSwitches(idItem, 5);

            if(reportItem !=null && reportItem.getReportStates().hasTriedToSendReport())
            {
                viewUtils.markSectionsWithNotFilledItems(id_sopralluogo, id_rapporto_sopralluogo);
            }
        }
    }

}
