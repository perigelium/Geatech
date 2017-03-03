package ru.alexangan.developer.geatech.Utils;

import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class ViewUtils
{
    public int selectedIndex;
    int idItemStart;
    int headerNumber;
    int idModello;
    int idSopralluogo;
    int id_rapporto_sopralluogo;
    View rootView;

    boolean[] allSectionsCollapsed;
    GeaModelloRapporto geaModello;
    List<GeaSezioneModelliRapporto> geaSezioniModelli;
    List<GeaItemModelliRapporto> geaItemModelli;
    ArrayList<ArrayList<LinearLayout>> al_llSectionHeaders;
    ArrayList<LinearLayout> llSectionHeaders;

    Map<Integer, ImageView> ImageViews;
    Map<Integer, Pair<LinearLayout, LinearLayout>> LinearLayouts;
    Map<Integer, Pair<RadioGroup, RadioGroup>> RadioGroups;
    Map<Integer, ArrayList<EditText>> EditTexts;
    Map<Integer, ArrayList<Switch>> Switches;
    Map<Integer, ArrayList<CheckBox>> CheckBoxes;

/*    ArrayList<Pair<LinearLayout, LinearLayout>> al_llPair;
    ArrayList<Pair<RadioGroup, RadioGroup>> al_rgPair;*/
    //ArrayList<ArrayList<EditText>> al_al_Edits;
    //ArrayList<ArrayList<Switch>> al_al_Switches;
    //ArrayList<ImageView> al_al_ImageViews;
    //ArrayList<ArrayList<CheckBox>> al_al_CheckBoxes;

    public ViewUtils(View rootView, int id_rapporto_sopralluogo, int selectedIndex)
    {
        this.rootView = rootView;
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.selectedIndex = selectedIndex;
        headerNumber = 0;
        idModello = 0;
        idItemStart = 99999;

        LinearLayouts = new HashMap<>();
        RadioGroups = new HashMap<>();
        EditTexts = new HashMap<>();
        Switches = new HashMap<>();
        ImageViews = new HashMap<>();
        CheckBoxes = new HashMap<>();

/*        al_llPair = new ArrayList<>();
        al_rgPair = new ArrayList<>();*/
        //al_al_Edits = new ArrayList<>();
        //al_al_Switches = new ArrayList<>();
        //al_al_ImageViews = new ArrayList<>();
        //al_al_CheckBoxes = new ArrayList<>();

        al_llSectionHeaders = new ArrayList<>();
        llSectionHeaders = new ArrayList<>();
        allSectionsCollapsed = new boolean[]{false, false, false, false, false, false, false};

        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();
        int id_product_type = productData.getIdProductType();

        realm.beginTransaction();
        geaModello = realm.where(GeaModelloRapporto.class).equalTo("id_product_type", id_product_type).findFirst();
        realm.commitTransaction();

        if (geaModello == null)
        {
            return;
        }

        realm.beginTransaction();
        geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class)
                .equalTo("id_modello", geaModello.getId_modello()).findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        geaItemModelli = realm.where(GeaItemModelliRapporto.class)
        .between("id_sezione", geaSezioniModelli.get(0).getId_sezione(), geaSezioniModelli.get(geaSezioniModelli.size() - 1).getId_sezione()).findAll();
        realm.commitTransaction();

        for (int i = 0; i < geaItemModelli.size(); i++)
        {
            int curItemModello = geaItemModelli.get(i).getId_item_modello();

            if (curItemModello < idItemStart)
            {
                idItemStart = curItemModello;
            }
        }
    }

    public View getRootView()
    {
        return rootView;
    }


    public void createViewFiveChkboxes(int sectionNumber, int idRes)
    {
        LinearLayout five_chkboxes = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFiveChkboxes = (LinearLayout) five_chkboxes.findViewById(R.id.llHeaderFiveChkboxes);

        final ImageView ivArrowFiveChkboxes = (ImageView) five_chkboxes.findViewById(R.id.ivArrowFiveChkboxes);
        
        ImageViews.put(idRes, ivArrowFiveChkboxes);

        TextView tvHeaderFiveChkboxes = (TextView) five_chkboxes.findViewById(R.id.tvHeaderFiveChkboxes);
        tvHeaderFiveChkboxes.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        final LinearLayout llSectionFiveChkboxes = (LinearLayout) five_chkboxes.findViewById(R.id.llSectionFiveChkboxes);
        
        LinearLayouts.put(idRes, new Pair<>(llHeaderFiveChkboxes, llSectionFiveChkboxes));

        llSectionHeaders.add(llSectionFiveChkboxes);

        llHeaderFiveChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFiveChkboxes.setVisibility(llSectionFiveChkboxes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFiveChkboxes.setImageResource(llSectionFiveChkboxes.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        ArrayList<CheckBox> al_chkBoxes = new ArrayList<>();

        CheckBox chk1FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk1FiveChkboxes);
        al_chkBoxes.add(chk1FiveChkboxes);
        CheckBox chk2FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk2FiveChkboxes);
        al_chkBoxes.add(chk2FiveChkboxes);
        CheckBox chk3FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk3FiveChkboxes);
        al_chkBoxes.add(chk3FiveChkboxes);
        CheckBox chk4FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk4FiveChkboxes);
        al_chkBoxes.add(chk4FiveChkboxes);
        CheckBox chk5FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk5FiveChkboxes);
        al_chkBoxes.add(chk5FiveChkboxes);
        
        CheckBoxes.put(idRes, al_chkBoxes);

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String[] fields = valore.split("\\|\\|");

        for (int i = 0; i < fields.length; i++)
        {
            al_chkBoxes.get(i).setText(fields[i]);
        }
    }

    public int createViewTwoTextsTwoEdits(int sectionNumber, int idRes)
    {
        LinearLayout two_texts_two_edits = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llSectionTwoTextsTwoEdits = (LinearLayout) two_texts_two_edits.findViewById(R.id.llSectionTwoTextTwoEdit);
        
        LinearLayouts.put(idRes, new Pair<>(llSectionTwoTextsTwoEdits, llSectionTwoTextsTwoEdits));

        llSectionHeaders.add(llSectionTwoTextsTwoEdits);

        TextView tv1TwoTextsTwoEdits = (TextView) two_texts_two_edits.findViewById(R.id.tv1TwoTextTwoEdit);
        tv1TwoTextsTwoEdits.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        TextView tv2TwoTextsTwoEdits = (TextView) two_texts_two_edits.findViewById(R.id.tv2TwoTextTwoEdit);
        tv2TwoTextsTwoEdits.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        EditText et1TwoTextsTwoEdits = (EditText) two_texts_two_edits.findViewById(R.id.et1TwoTextTwoEdit);

        EditText et2TwoTextsTwoEdits = (EditText) two_texts_two_edits.findViewById(R.id.et2TwoTextTwoEdit);

        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1TwoTextsTwoEdits);
        al_Edits.add(et2TwoTextsTwoEdits);
        EditTexts.put(idRes, al_Edits);

        return sectionNumber;
    }

    public int createViewTwoSwitchesAndEdit(int sectionNumber, int idRes)
    {
        LinearLayout two_switches_and_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderTwoSwitchesAndEdit = (LinearLayout) two_switches_and_edit.findViewById(R.id.llHeaderTwoSwitchesAndEdit);

        TextView tvHeaderTwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tvHeaderTwoSwitchesAndEdit);
        tvHeaderTwoSwitchesAndEdit.setText(geaItemModelli.get(sectionNumber).getValore());

        final LinearLayout llSectionTwoSwitchesAndEdit = (LinearLayout) two_switches_and_edit.findViewById(R.id.llSectionTwoSwitchesAndEdit);
        
        LinearLayouts.put(idRes, new Pair<>(llHeaderTwoSwitchesAndEdit, llSectionTwoSwitchesAndEdit));

        llSectionHeaders.add(llSectionTwoSwitchesAndEdit);

        final ImageView ivArrowTwoSwitchesAndEdit = (ImageView) two_switches_and_edit.findViewById(R.id.ivArrowTwoSwitchesAndEdit);
        
        ImageViews.put(idRes, ivArrowTwoSwitchesAndEdit);

        llHeaderTwoSwitchesAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionTwoSwitchesAndEdit.setVisibility(llSectionTwoSwitchesAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowTwoSwitchesAndEdit.setImageResource(llSectionTwoSwitchesAndEdit.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        TextView tv1TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv1TwoSwitchesAndEdit);
        tv1TwoSwitchesAndEdit.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw1TwoSwitchesAndEdit = (Switch) two_switches_and_edit.findViewById(R.id.sw1TwoSwitchesAndEdit);

        TextView tv2TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv2TwoSwitchesAndEdit);
        tv2TwoSwitchesAndEdit.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw2TwoSwitchesAndEdit = (Switch) two_switches_and_edit.findViewById(R.id.sw2TwoSwitchesAndEdit);

        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1TwoSwitchesAndEdit);
        al_Switches.add(sw2TwoSwitchesAndEdit);
        Switches.put(idRes, al_Switches);

        TextView tv3TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv3TwoSwitchesAndEdit);
        tv3TwoSwitchesAndEdit.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        EditText et1TwoSwitchesAndEdit = (EditText) two_switches_and_edit.findViewById(R.id.et1TwoSwitchesAndEdit);

        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1TwoSwitchesAndEdit);
        EditTexts.put(idRes, al_Edits);

        return sectionNumber;
    }

    public int createViewFiveSwitches(int sectionNumber, int idRes)
    {
        LinearLayout five_switches = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFiveSwitches = (LinearLayout) five_switches.findViewById(R.id.llHeaderFiveSwitches);

        TextView tvHeaderFiveSwitches = (TextView) five_switches.findViewById(R.id.tvHeaderFiveSwitches);
        tvHeaderFiveSwitches.setText("Two switches header");

        final LinearLayout llSectionFiveSwitches = (LinearLayout) five_switches.findViewById(R.id.llSectionFiveSwitches);
        
        LinearLayouts.put(idRes, new Pair<>(llHeaderFiveSwitches, llSectionFiveSwitches));

        llSectionHeaders.add(llSectionFiveSwitches);

        final ImageView ivArrowFiveSwitches = (ImageView) five_switches.findViewById(R.id.ivArrowFiveSwitches);
        
        ImageViews.put(idRes, ivArrowFiveSwitches);

        llHeaderFiveSwitches.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFiveSwitches.setVisibility(llSectionFiveSwitches.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFiveSwitches.setImageResource(llSectionFiveSwitches.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        TextView tv1FiveSwitches = (TextView) five_switches.findViewById(R.id.tv1FiveSwitches);
        tv1FiveSwitches.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw1FiveSwitches = (Switch) five_switches.findViewById(R.id.sw1FiveSwitches);

        TextView tv2FiveSwitches = (TextView) five_switches.findViewById(R.id.tv2FiveSwitches);
        tv2FiveSwitches.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw2FiveSwitches = (Switch) five_switches.findViewById(R.id.sw2FiveSwitches);

        TextView tv3FiveSwitches = (TextView) five_switches.findViewById(R.id.tv3FiveSwitches);
        tv3FiveSwitches.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw3FiveSwitches = (Switch) five_switches.findViewById(R.id.sw3FiveSwitches);

        TextView tv4FiveSwitches = (TextView) five_switches.findViewById(R.id.tv4FiveSwitches);
        tv4FiveSwitches.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw4FiveSwitches = (Switch) five_switches.findViewById(R.id.sw4FiveSwitches);

        TextView tv5FiveSwitches = (TextView) five_switches.findViewById(R.id.tv5FiveSwitches);
        tv5FiveSwitches.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw5FiveSwitches = (Switch) five_switches.findViewById(R.id.sw5FiveSwitches);

        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1FiveSwitches);
        al_Switches.add(sw2FiveSwitches);
        al_Switches.add(sw3FiveSwitches);
        al_Switches.add(sw4FiveSwitches);
        al_Switches.add(sw5FiveSwitches);
        Switches.put(idRes, al_Switches);

        return sectionNumber;
    }

    public void createViewFourRadios(int sectionNumber, int idRes)
    {
        int i;
        LinearLayout four_radios = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFourRadios = (LinearLayout) four_radios.findViewById(R.id.llHeaderFourRadios);

        TextView tvHeaderFourRadios = (TextView) four_radios.findViewById(R.id.tvHeaderFourRadios);
        tvHeaderFourRadios.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        final ImageView ivArrowFourRadios = (ImageView) four_radios.findViewById(R.id.ivArrowFourRadios);
        
        ImageViews.put(idRes, ivArrowFourRadios);

        final LinearLayout llSectionFourRadios = (LinearLayout) four_radios.findViewById(R.id.llSectionFourRadios);
        
        LinearLayouts.put(idRes, new Pair<>(llHeaderFourRadios, llSectionFourRadios));

        llSectionHeaders.add(llSectionFourRadios);

        llHeaderFourRadios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFourRadios.setVisibility(llSectionFourRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFourRadios.setImageResource(llSectionFourRadios.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        RadioGroup rg1FourRadios = (RadioGroup) four_radios.findViewById(R.id.rg1FourRadios);
        
        RadioGroups.put(idRes, new Pair<>(rg1FourRadios, rg1FourRadios));

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1FourRadios.getChildCount())
        {
            for (i = 0; i < rg1FourRadios.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1FourRadios.getChildAt(i);

                rb.setText(fields[i]);
            }
        }
    }

    public void createViewTwoRadiosAndEdit(int sectionNumber, int idRes)
    {
        LinearLayout two_radios_and_edit = (LinearLayout) rootView.findViewById(idRes);

        LinearLayout llHeaderTwoRadiosAndEdit = (LinearLayout) two_radios_and_edit.findViewById(R.id.llHeaderTwoRadiosAndEdit);

        final ImageView ivArrowTwoRadiosAndEdit = (ImageView) two_radios_and_edit.findViewById(R.id.ivArrowTwoRadiosAndEdit);
        
        ImageViews.put(idRes, ivArrowTwoRadiosAndEdit);

        TextView tvHeaderTwoRadiosAndEdit = (TextView) two_radios_and_edit.findViewById(R.id.tvHeaderTwoRadiosAndEdit);
        tvHeaderTwoRadiosAndEdit.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        final LinearLayout llSectionTwoRadiosAndEdit = (LinearLayout) two_radios_and_edit.findViewById(R.id.llSectionTwoRadiosAndEdit);
        
        LinearLayouts.put(idRes, new Pair<>(llHeaderTwoRadiosAndEdit, llSectionTwoRadiosAndEdit));

        llSectionHeaders.add(llSectionTwoRadiosAndEdit);

        llHeaderTwoRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionTwoRadiosAndEdit.setVisibility(llSectionTwoRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowTwoRadiosAndEdit.setImageResource(llSectionTwoRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        final RadioGroup rg1TwoRadiosAndEdit = (RadioGroup) two_radios_and_edit.findViewById(R.id.rg1TwoRadiosAndEdit);
        
        RadioGroups.put(idRes, new Pair<>(rg1TwoRadiosAndEdit, rg1TwoRadiosAndEdit));

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1TwoRadiosAndEdit.getChildCount())
        {
            for (int i = 0; i < rg1TwoRadiosAndEdit.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1TwoRadiosAndEdit.getChildAt(i);

                rb.setText(fields[i]);
            }
        }

        //TextView tv1TwoRadiosAndEdit = (TextView) two_radios_and_edit.findViewById(R.id.tv1TwoRadiosAndEdit);

        EditText et1TwoRadiosAndEdit = (EditText) two_radios_and_edit.findViewById(R.id.et1TwoRadiosAndEdit);

        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1TwoRadiosAndEdit);
        EditTexts.put(idRes, al_Edits);

        et1TwoRadiosAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1TwoRadiosAndEdit.clearCheck();
                return false;
            }
        });
    }

    public void createViewTwoRadios(int sectionNumber, int idRes)
    {
        int i;
        LinearLayout ll_two_radios = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderTwoRadios = (LinearLayout) ll_two_radios.findViewById(R.id.llHeaderTwoRadios);

        TextView tvHeaderTwoRadios = (TextView) ll_two_radios.findViewById(R.id.tvHeaderTwoRadios);
        tvHeaderTwoRadios.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        final ImageView ivArrowTwoRadios = (ImageView) ll_two_radios.findViewById(R.id.ivArrowTwoRadios);
        
        ImageViews.put(idRes, ivArrowTwoRadios);

        final LinearLayout llSectionTwoRadios = (LinearLayout) ll_two_radios.findViewById(R.id.llSectionTwoRadios);
        
        LinearLayouts.put(idRes, new Pair<>(llHeaderTwoRadios, llSectionTwoRadios));

        llSectionHeaders.add(llSectionTwoRadios);

        llHeaderTwoRadios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionTwoRadios.setVisibility(llSectionTwoRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowTwoRadios.setImageResource(llSectionTwoRadios.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        RadioGroup rg1TwoRadios = (RadioGroup) ll_two_radios.findViewById(R.id.rg1TwoRadios);
        
        RadioGroups.put(idRes, new Pair<>(rg1TwoRadios, rg1TwoRadios));

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1TwoRadios.getChildCount())
        {
            for (i = 0; i < rg1TwoRadios.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1TwoRadios.getChildAt(i);

                rb.setText(fields[i]);
            }
        }
    }

    public void createViewFourRadiosAndEdit(int sectionNumber, int idRes)
    {
        int i;
        LinearLayout four_radios_and_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFourRadiosAndEdit = (LinearLayout) four_radios_and_edit.findViewById(R.id.llHeaderFourRadiosAndEdit);

        TextView tvHeaderFourRadiosAndEdit = (TextView) four_radios_and_edit.findViewById(R.id.tvHeaderFourRadiosAndEdit);
        tvHeaderFourRadiosAndEdit.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        final ImageView ivArrowFourRadiosAndEdit = (ImageView) four_radios_and_edit.findViewById(R.id.ivArrowFourRadiosAndEdit);

        ImageViews.put(idRes, ivArrowFourRadiosAndEdit);

        final LinearLayout llSectionFourRadiosAndEdit = (LinearLayout) four_radios_and_edit.findViewById(R.id.llSectionFourRadiosAndEdit);

        LinearLayouts.put(idRes, new Pair<>(llHeaderFourRadiosAndEdit, llSectionFourRadiosAndEdit));

        llSectionHeaders.add(llSectionFourRadiosAndEdit);

        llHeaderFourRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFourRadiosAndEdit.setVisibility(llSectionFourRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFourRadiosAndEdit.setImageResource(llSectionFourRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        final RadioGroup rg1FourRadiosAndEdit = (RadioGroup) four_radios_and_edit.findViewById(R.id.rg1FourRadiosAndEdit);
        
        RadioGroups.put(idRes, new Pair<>(rg1FourRadiosAndEdit, rg1FourRadiosAndEdit));

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1FourRadiosAndEdit.getChildCount())
        {
            if (fields.length >= rg1FourRadiosAndEdit.getChildCount())
            {
                for (i = 0; i < rg1FourRadiosAndEdit.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1FourRadiosAndEdit.getChildAt(i);

                    rb.setText(fields[i]);
                }
            }
        }

        //TextView tv1FourRadiosAndEdit = (TextView) four_radios_and_edit.findViewById(R.id.tv1FourRadiosAndEdit);

        EditText et1FourRadiosAndEdit = (EditText) four_radios_and_edit.findViewById(R.id.et1FourRadiosAndEdit);

        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1FourRadiosAndEdit);
        EditTexts.put(idRes, al_Edits);

        et1FourRadiosAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1FourRadiosAndEdit.clearCheck();
                return false;
            }
        });
    }

    public int createViewFourEditsAndSwitch(int sectionNumber, int idRes)
    {
        LinearLayout four_edits_and_switch = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFourEditsAndSwitch = (LinearLayout) four_edits_and_switch.findViewById(R.id.llHeaderFourEditsAndSwitch);

        TextView tvHeaderFourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tvHeaderFourEditsAndSwitch);
        tvHeaderFourEditsAndSwitch.setText(geaItemModelli.get(sectionNumber).getValore());

        final LinearLayout llSectionFourEditsAndSwitch = (LinearLayout) four_edits_and_switch.findViewById(R.id.llSectionFourEditsAndSwitch);
        
        LinearLayouts.put(idRes, new Pair<>(llHeaderFourEditsAndSwitch, llSectionFourEditsAndSwitch));

        llSectionHeaders.add(llSectionFourEditsAndSwitch);

        final ImageView ivArrowFourEditsAndSwitch = (ImageView) four_edits_and_switch.findViewById(R.id.ivArrowFourEditsAndSwitch);
        
        ImageViews.put(idRes, ivArrowFourEditsAndSwitch);

        llHeaderFourEditsAndSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFourEditsAndSwitch.setVisibility(llSectionFourEditsAndSwitch.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFourEditsAndSwitch.setImageResource(llSectionFourEditsAndSwitch.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        ArrayList<TextView> al_tvs = new ArrayList<>();

        TextView tv1FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv1FourEditsAndSwitch);
        EditText et1FourEditsAndSwitch = (EditText) four_edits_and_switch.findViewById(R.id.et1FourEditsAndSwitch);
        TextView tv2FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv2FourEditsAndSwitch);
        EditText et2FourEditsAndSwitch = (EditText) four_edits_and_switch.findViewById(R.id.et2FourEditsAndSwitch);
        TextView tv3FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv3FourEditsAndSwitch);
        EditText et3FourEditsAndSwitch = (EditText) four_edits_and_switch.findViewById(R.id.et3FourEditsAndSwitch);
        TextView tv4FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv4FourEditsAndSwitch);
        EditText et4FourEditsAndSwitch = (EditText) four_edits_and_switch.findViewById(R.id.et4FourEditsAndSwitch);


        al_tvs.add(tv1FourEditsAndSwitch);
        al_tvs.add(tv2FourEditsAndSwitch);
        al_tvs.add(tv3FourEditsAndSwitch);
        al_tvs.add(tv4FourEditsAndSwitch);

        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1FourEditsAndSwitch);
        al_Edits.add(et2FourEditsAndSwitch);
        al_Edits.add(et3FourEditsAndSwitch);
        al_Edits.add(et4FourEditsAndSwitch);
        EditTexts.put(idRes, al_Edits);

        for (int i = 0; i < al_tvs.size(); i++)
        {
            al_tvs.get(i).setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());
        }

        TextView tv5FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv5FourEditsAndSwitch);
        tv5FourEditsAndSwitch.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw1FourEditsAndSwitch = (Switch) four_edits_and_switch.findViewById(R.id.sw1FourEditsAndSwitch);

        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1FourEditsAndSwitch);
        Switches.put(idRes, al_Switches);

        return sectionNumber;
    }

    public void createViewEdit(int sectionNumber, int idRes)
    {
        LinearLayout ll_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderEdit = (LinearLayout) ll_edit.findViewById(R.id.llHeaderEdit);

        TextView tvHeaderEdit = (TextView) ll_edit.findViewById(R.id.tvHeaderEdit);
        tvHeaderEdit.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        final LinearLayout llSectionEdit = (LinearLayout) ll_edit.findViewById(R.id.llSectionEdit);

        llSectionHeaders.add(llSectionEdit);
        
        LinearLayouts.put(idRes, new Pair<>(llHeaderEdit, llSectionEdit));

        final ImageView ivArrowEdit = (ImageView) ll_edit.findViewById(R.id.ivArrowEdit);
        
        ImageViews.put(idRes, ivArrowEdit);

        llHeaderEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionEdit.setVisibility(llSectionEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowEdit.setImageResource(llSectionEdit.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        TextView tv1Edit = (TextView) ll_edit.findViewById(R.id.tv1Edit);
        tv1Edit.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        EditText et1Edit = (EditText) ll_edit.findViewById(R.id.et1Edit);

        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1Edit);
        EditTexts.put(idRes, al_Edits);
    }

    public void createViewSectionHeader(int idRes)
    {
        LinearLayout header = (LinearLayout) rootView.findViewById(idRes);
        FrameLayout flSectionHeader = (FrameLayout) header.findViewById(R.id.flSectionHeader);
        final int curHeader = headerNumber;

        flSectionHeader.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                for (LinearLayout ll : al_llSectionHeaders.get(curHeader))
                {
                    ll.setVisibility(!allSectionsCollapsed[curHeader] ? View.GONE : View.VISIBLE);
                }

                allSectionsCollapsed[curHeader] = !allSectionsCollapsed[curHeader];
            }
        });

        TextView tvSectionHeader = (TextView) header.findViewById(R.id.tvSectionHeader);
        tvSectionHeader.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());

        ArrayList<LinearLayout> llSecHeaders = new ArrayList<>();
        llSecHeaders.addAll(llSectionHeaders);
        al_llSectionHeaders.add(llSecHeaders);
        llSectionHeaders.clear();
        headerNumber++;
    }



/*    public void saveSeveralRadiosAndEdit(int idRes, int sectionNumber)
    {
        String str_Id_item;
        RadioGroup rg = RadioGroups.get(groupName).get(groupNumber).first;
        int checkedBtnId = rg.getCheckedRadioButtonId();

        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg.findViewById(checkedBtnId);
            str_Id_item = radioButton.getText().toString();
        } else
        {
            EditText et = EditTexts.get(groupName).get(groupNumber).get(0);
            str_Id_item = et.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItemStart + sectionNumber, str_Id_item);
    }*/

    public void saveSeveralRadios(int idRes, int sectionNumber)
    {
        String str_Id_item="";
        RadioGroup rg = RadioGroups.get(idRes).first;
        int checkedBtnId = rg.getCheckedRadioButtonId();

        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg.findViewById(checkedBtnId);
            str_Id_item = radioButton.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItemStart + sectionNumber, str_Id_item);
    }

    public int saveSeveralChkboxes(int idRes, int sectionNumber)
    {
        ArrayList<CheckBox> alChks = CheckBoxes.get(idRes);
        String str_Id_item = "";

        for (int i = 0; i < alChks.size(); i++)
        {
            str_Id_item += alChks.get(i).isChecked() ? alChks.get(i).getText().toString() : "";
            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItemStart + sectionNumber + i, str_Id_item);
        }

        sectionNumber = sectionNumber + alChks.size();

        return sectionNumber;
    }

    public int saveSeveralSwitches(int idRes, int sectionNumber)
    {
        ArrayList<Switch> alSw = Switches.get(idRes);
        String str_Id_item = "";

        for (int i = 0; i < alSw.size(); i++)
        {
            str_Id_item += alSw.get(i).isChecked() ? alSw.get(i).getText().toString() : "";
            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItemStart + sectionNumber + i, str_Id_item);
        }

        sectionNumber = sectionNumber + alSw.size();

        return sectionNumber;
    }

    public int saveSeveralEdits(int idRes, int sectionNumber)
    {
        ArrayList <EditText> alEt = EditTexts.get(idRes);

        for (int i = 0; i < alEt.size(); i++)
        {
            String str_id_item = alEt.get(i).getText().toString();
            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItemStart + sectionNumber + i, str_id_item);
        }

        sectionNumber = sectionNumber + alEt.size();

        return sectionNumber;
    }
}
