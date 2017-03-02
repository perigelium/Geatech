package ru.alexangan.developer.geatech.Utils;

import android.content.Context;
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
    int headerNumber;
    int idModello;
    int idSopralluogo;
    View rootView;
    Context context;
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

    public ViewUtils(View rootView, int selectedIndex)
    {
        this.rootView = rootView;
        this.selectedIndex = selectedIndex;
        headerNumber = 0;
        idModello = 0;

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
        .between("id_sezione", geaSezioniModelli.get(0).getId_sezione(), geaSezioniModelli.get(geaSezioniModelli.size() - 1).getId_sezione()).findAll();
        //.between("id_item_modello", idItemStart, idItemEnd)
        realm.commitTransaction();
    }

    public View getRootView()
    {
        return rootView;
    }

    public void createViewFiveChkboxes(int sectionNumber, int idRes)
    {
        LinearLayout five_chkboxes = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFiveChkboxes = (LinearLayout) five_chkboxes.findViewById(R.id.llHeaderFiveChkboxes);
        //llHeaderFiveChkboxes.setOnClickListener(this);

        ImageView ivArrowFiveChkboxes = (ImageView) five_chkboxes.findViewById(R.id.ivArrowFiveChkboxes);

        al_al_ImageViews.add(ivArrowFiveChkboxes);
        ImageViews.put("FiveChkboxes", al_al_ImageViews);

        TextView tvHeaderFiveChkboxes = (TextView) five_chkboxes.findViewById(R.id.tvHeaderFiveChkboxes);
        tvHeaderFiveChkboxes.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        LinearLayout llSectionFiveChkboxes = (LinearLayout) five_chkboxes.findViewById(R.id.llSectionFiveChkboxes);

        al_llPair.add(new Pair<>(llHeaderFiveChkboxes, llSectionFiveChkboxes));
        LinearLayouts.put("FiveChkboxes", al_llPair);

/*        ArrayList <Pair<LinearLayout,LinearLayout>> al = LinearLayouts.get("FiveChkboxes");
        LinearLayout l = al.get(serialNumber).first;*/

        ArrayList <CheckBox> al_chkBoxes = new ArrayList<>();

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

        al_al_CheckBoxes.add(al_chkBoxes);
        CheckBoxes.put("FiveChkboxes", al_al_CheckBoxes);

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String [] fields = valore.split("\\|\\|");

        for (int i = 0; i < fields.length; i++)
        {
            al_chkBoxes.get(i).setText(fields[i]);
        }
    }
    
    public int createViewTwoTextsTwoEdits(int sectionNumber, int idRes)
    {
        LinearLayout two_texts_two_edits1 = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llSectionTwoTextsTwoEdits1 = (LinearLayout) two_texts_two_edits1.findViewById(R.id.llSectionTwoTextTwoEdit);

        al_llPair.add(new Pair<>(llSectionTwoTextsTwoEdits1, llSectionTwoTextsTwoEdits1));
        LinearLayouts.put("TwoTextsTwoEdits", al_llPair);

        TextView tv1TwoTextsTwoEdits = (TextView) two_texts_two_edits1.findViewById(R.id.tv1TwoTextTwoEdit);
        tv1TwoTextsTwoEdits.setText(geaItemModelli.get(sectionNumber++).getValore()); // .getDescrizione_item() !

        TextView tv2TwoTextsTwoEdits = (TextView) two_texts_two_edits1.findViewById(R.id.tv2TwoTextTwoEdit);
        tv2TwoTextsTwoEdits.setText(geaItemModelli.get(sectionNumber++).getValore()); // .getDescrizione_item()

        EditText et1TwoTextsTwoEdits = (EditText) two_texts_two_edits1.findViewById(R.id.et1TwoTextTwoEdit);

        EditText et2TwoTextsTwoEdits = (EditText) two_texts_two_edits1.findViewById(R.id.et2TwoTextTwoEdit);

        ArrayList <EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1TwoTextsTwoEdits);
        al_Edits.add(et2TwoTextsTwoEdits);
        al_al_Edits.add(al_Edits);
        EditTexts.put("TwoTextsTwoEdits", al_al_Edits);

        return sectionNumber;
    }

    public int createViewTwoSwitchesAndEdit(int sectionNumber, int idRes)
    {
        LinearLayout two_switches_and_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderTwoSwitchesAndEdit = (LinearLayout) two_switches_and_edit.findViewById(R.id.llHeaderTwoSwitchesAndEdit);
        //llHeaderTwoSwitchesAndEdit.setOnClickListener(this);

        TextView tvHeaderTwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tvHeaderTwoSwitchesAndEdit);
        tvHeaderTwoSwitchesAndEdit.setText(geaItemModelli.get(sectionNumber).getValore());

        LinearLayout llSectionTwoSwitchesAndEdit = (LinearLayout) two_switches_and_edit.findViewById(R.id.llSectionTwoSwitchesAndEdit);

        al_llPair.add(new Pair<>(llHeaderTwoSwitchesAndEdit, llSectionTwoSwitchesAndEdit));
        LinearLayouts.put("TwoSwitchesAndEditAndEdit", al_llPair);

        ImageView ivArrowTwoSwitchesAndEdit = (ImageView) two_switches_and_edit.findViewById(R.id.ivArrowTwoSwitchesAndEdit);

        al_al_ImageViews.add(ivArrowTwoSwitchesAndEdit);
        ImageViews.put("TwoSwitchesAndEditAndEdit", al_al_ImageViews);

        TextView tv1TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv1TwoSwitchesAndEdit);
        tv1TwoSwitchesAndEdit.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw1TwoSwitchesAndEdit = (Switch) two_switches_and_edit.findViewById(R.id.sw1TwoSwitchesAndEdit);

        TextView tv2TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv2TwoSwitchesAndEdit);
        tv2TwoSwitchesAndEdit.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw2TwoSwitchesAndEdit = (Switch) two_switches_and_edit.findViewById(R.id.sw2TwoSwitchesAndEdit);

        ArrayList <Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1TwoSwitchesAndEdit);
        al_Switches.add(sw2TwoSwitchesAndEdit);
        al_al_Switches.add(al_Switches);
        Switches.put("TwoSwitchesAndEditAndEdit", al_al_Switches);

        TextView tv3TwoSwitchesAndEditAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv3TwoSwitchesAndEdit);
        tv3TwoSwitchesAndEditAndEdit.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        EditText et1TwoSwitchesAndEditAndEdit = (EditText) two_switches_and_edit.findViewById(R.id.et1TwoSwitchesAndEdit);

        ArrayList <EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1TwoSwitchesAndEditAndEdit);
        al_al_Edits.add(al_Edits);
        EditTexts.put("TwoSwitchesAndEditAndEdit", al_al_Edits);

        return sectionNumber;
    }

    public int createViewFiveSwitches(int sectionNumber, int idRes)
    {
        LinearLayout five_switches = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFiveSwitches = (LinearLayout) five_switches.findViewById(R.id.llHeaderFiveSwitches);
        //llHeaderFiveSwitches.setOnClickListener(this);

        TextView tvHeaderFiveSwitches = (TextView) five_switches.findViewById(R.id.tvHeaderFiveSwitches);
        tvHeaderFiveSwitches.setText("Two switches header");

        LinearLayout llSectionFiveSwitches = (LinearLayout) five_switches.findViewById(R.id.llSectionFiveSwitches);

        al_llPair.add(new Pair<>(llHeaderFiveSwitches, llSectionFiveSwitches));
        LinearLayouts.put("TwoTextsTwoEdits", al_llPair);

        ImageView ivArrowFiveSwitches = (ImageView) five_switches.findViewById(R.id.ivArrowFiveSwitches);

        al_al_ImageViews.add(ivArrowFiveSwitches);
        ImageViews.put("TwoTextsTwoEdits", al_al_ImageViews);

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

        ArrayList <Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1FiveSwitches);
        al_Switches.add(sw2FiveSwitches);
        al_Switches.add(sw3FiveSwitches);
        al_Switches.add(sw4FiveSwitches);
        al_Switches.add(sw5FiveSwitches);
        al_al_Switches.add(al_Switches);
        Switches.put("TwoTextsTwoEdits", al_al_Switches);

        return sectionNumber;
    }
    
    public void createViewFourRadios(int sectionNumber, int idRes)
    {
        int i;
        LinearLayout four_radios = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFourRadios = (LinearLayout) four_radios.findViewById(R.id.llHeaderFourRadios);
        //llHeaderFourRadios.setOnClickListener(this);

        TextView tvHeaderFourRadios = (TextView) four_radios.findViewById(R.id.tvHeaderFourRadios);
        tvHeaderFourRadios.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ImageView ivArrowFourRadios = (ImageView) four_radios.findViewById(R.id.ivArrowFourRadios);

        al_al_ImageViews.add(ivArrowFourRadios);
        ImageViews.put("FourRadios", al_al_ImageViews);

        LinearLayout llSectionFourRadios = (LinearLayout) four_radios.findViewById(R.id.llSectionFourRadios);

        al_llPair.add(new Pair<>(llHeaderFourRadios, llSectionFourRadios));
        LinearLayouts.put("FourRadios", al_llPair);

        RadioGroup rg1FourRadios = (RadioGroup) four_radios.findViewById(R.id.rg1FourRadios);

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String [] fields = valore.split("\\|\\|");

        if(fields.length >= rg1FourRadios.getChildCount())
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
        //llHeaderTwoRadiosAndEdit.setOnClickListener(this);

        ImageView ivArrowTwoRadiosAndEdit = (ImageView) two_radios_and_edit.findViewById(R.id.ivArrowTwoRadiosAndEdit);

        al_al_ImageViews.add(ivArrowTwoRadiosAndEdit);
        ImageViews.put("TwoRadiosAndEdit", al_al_ImageViews);

        TextView tvHeaderTwoRadiosAndEdit = (TextView) two_radios_and_edit.findViewById(R.id.tvHeaderTwoRadiosAndEdit);
        tvHeaderTwoRadiosAndEdit.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        LinearLayout llSectionTwoRadiosAndEdit = (LinearLayout) two_radios_and_edit.findViewById(R.id.llSectionTwoRadiosAndEdit);

        al_llPair.add(new Pair<>(llHeaderTwoRadiosAndEdit, llSectionTwoRadiosAndEdit));
        LinearLayouts.put("TwoRadiosAndEdit", al_llPair);

        final RadioGroup rg1TwoRadiosAndEdit = (RadioGroup) two_radios_and_edit.findViewById(R.id.rg1TwoRadiosAndEdit);

        al_rgPair.add(new Pair<>(rg1TwoRadiosAndEdit, rg1TwoRadiosAndEdit));
        RadioGroups.put("TwoRadiosAndEdit", al_rgPair);

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String [] fields = valore.split("\\|\\|");

        if(fields.length >= rg1TwoRadiosAndEdit.getChildCount())
        {
            for (int i = 0; i < rg1TwoRadiosAndEdit.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1TwoRadiosAndEdit.getChildAt(i);

                rb.setText(fields[i]);
            }
        }



        EditText et1TwoRadiosAndEdit = (EditText) two_radios_and_edit.findViewById(R.id.et1TwoRadiosAndEdit);

        ArrayList <EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1TwoRadiosAndEdit);
        al_al_Edits.add(al_Edits);
        EditTexts.put("TwoRadiosAndEdit", al_al_Edits);

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
        //llHeaderTwoRadios.setOnClickListener(this);

        TextView tvHeaderTwoRadios = (TextView) ll_two_radios.findViewById(R.id.tvHeaderTwoRadios);
        tvHeaderTwoRadios.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ImageView ivArrowTwoRadios = (ImageView) ll_two_radios.findViewById(R.id.ivArrowTwoRadios);

        al_al_ImageViews.add(ivArrowTwoRadios);
        ImageViews.put("TwoRadios", al_al_ImageViews);

        LinearLayout llSectionTwoRadios = (LinearLayout) ll_two_radios.findViewById(R.id.llSectionTwoRadios);

        al_llPair.add(new Pair<>(llHeaderTwoRadios, llSectionTwoRadios));
        LinearLayouts.put("TwoRadios", al_llPair);

        RadioGroup rg1TwoRadios = (RadioGroup) ll_two_radios.findViewById(R.id.rg1TwoRadios);

        al_rgPair.add(new Pair<>(rg1TwoRadios, rg1TwoRadios));
        RadioGroups.put("TwoRadios", al_rgPair);

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String [] fields = valore.split("\\|\\|");

        if(fields.length >= rg1TwoRadios.getChildCount())
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
        //llHeaderFourRadiosAndEdit.setOnClickListener(this);

        TextView tvHeaderFourRadiosAndEdit = (TextView) four_radios_and_edit.findViewById(R.id.tvHeaderFourRadiosAndEdit);
        tvHeaderFourRadiosAndEdit.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        ImageView ivArrowFourRadiosAndEdit = (ImageView) four_radios_and_edit.findViewById(R.id.ivArrowFourRadiosAndEdit);

        al_al_ImageViews.add(ivArrowFourRadiosAndEdit);
        ImageViews.put("FourRadiosAndEdit", al_al_ImageViews);

        LinearLayout llSectionFourRadiosAndEdit = (LinearLayout) four_radios_and_edit.findViewById(R.id.llSectionFourRadiosAndEdit);

        al_llPair.add(new Pair<>(llHeaderFourRadiosAndEdit, llSectionFourRadiosAndEdit));
        LinearLayouts.put("FourRadiosAndEdit", al_llPair);

        final RadioGroup rg1FourRadiosAndEdit = (RadioGroup) four_radios_and_edit.findViewById(R.id.rg1FourRadiosAndEdit);

        al_rgPair.add(new Pair<>(rg1FourRadiosAndEdit, rg1FourRadiosAndEdit));
        RadioGroups.put("FourRadiosAndEdit", al_rgPair);

        String valore = geaItemModelli.get(sectionNumber).getValore();
        String [] fields = valore.split("\\|\\|");

        if(fields.length >= rg1FourRadiosAndEdit.getChildCount())
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





        EditText et1FourRadiosAndEdit = (EditText) four_radios_and_edit.findViewById(R.id.et1FourRadiosAndEdit);

        ArrayList <EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1FourRadiosAndEdit);
        al_al_Edits.add(al_Edits);
        EditTexts.put("FourRadiosAndEdit", al_al_Edits);

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
        //llHeaderFourEditsAndSwitch.setOnClickListener(this);

        TextView tvHeaderFourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tvHeaderFourEditsAndSwitch);
        tvHeaderFourEditsAndSwitch.setText(geaItemModelli.get(sectionNumber).getValore());

        LinearLayout llSectionFourEditsAndSwitch = (LinearLayout) four_edits_and_switch.findViewById(R.id.llSectionFourEditsAndSwitch);

        al_llPair.add(new Pair<>(llHeaderFourEditsAndSwitch, llSectionFourEditsAndSwitch));
        LinearLayouts.put("FourEditsAndSwitch", al_llPair);
        

        ImageView ivArrowFourEditsAndSwitch = (ImageView) four_edits_and_switch.findViewById(R.id.ivArrowFourEditsAndSwitch);

        al_al_ImageViews.add(ivArrowFourEditsAndSwitch);
        ImageViews.put("FourEditsAndSwitch", al_al_ImageViews);

        TextView tv1FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv1FourEditsAndSwitch);
        //tv1FourEditsAndSwitch.setText(fields[i++]);
        EditText et1FourEditsAndSwitch = (EditText) four_edits_and_switch.findViewById(R.id.et1FourEditsAndSwitch);
        TextView tv2FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv2FourEditsAndSwitch);
        //tv2FourEditsAndSwitch.setText(fields[i++]);
        EditText et2FourEditsAndSwitch = (EditText) four_edits_and_switch.findViewById(R.id.et2FourEditsAndSwitch);
        TextView tv3FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv3FourEditsAndSwitch);
        //tv3FourEditsAndSwitch.setText(fields[i++]);
        EditText et3FourEditsAndSwitch = (EditText) four_edits_and_switch.findViewById(R.id.et3FourEditsAndSwitch);
        TextView tv4FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv4FourEditsAndSwitch);
        //tv4FourEditsAndSwitch.setText(fields[i++]);
        EditText et4FourEditsAndSwitch = (EditText) four_edits_and_switch.findViewById(R.id.et4FourEditsAndSwitch);

        ArrayList <EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1FourEditsAndSwitch);
        al_Edits.add(et2FourEditsAndSwitch);
        al_Edits.add(et3FourEditsAndSwitch);
        al_Edits.add(et4FourEditsAndSwitch);
        al_al_Edits.add(al_Edits);
        EditTexts.put("FourEditsAndSwitch", al_al_Edits);


/*        String valore = geaItemModelli.get(sectionNumber++).getValore();
        String [] fields = valore.split("\\|\\|");*/

        for (int i = 0; i < al_Edits.size(); i++)
        {
            al_Edits.get(i).setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());
        }

        TextView tv5FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv5FourEditsAndSwitch);
        tv5FourEditsAndSwitch.setText(geaItemModelli.get(sectionNumber++).getDescrizione_item());

        Switch sw1FourEditsAndSwitch = (Switch) four_edits_and_switch.findViewById(R.id.sw1FourEditsAndSwitch);

        ArrayList <Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1FourEditsAndSwitch);
        al_al_Switches.add(al_Switches);
        Switches.put("FourEditsAndSwitch", al_al_Switches);

        return sectionNumber;
    }

    public void createViewEdit(int sectionNumber, int idRes)
    {
        LinearLayout ll_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderEdit = (LinearLayout) ll_edit.findViewById(R.id.llHeaderEdit);
        //llHeaderEdit.setOnClickListener(this);

        TextView tvHeaderEdit = (TextView) ll_edit.findViewById(R.id.tvHeaderEdit);
        tvHeaderEdit.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        LinearLayout llSectionEdit = (LinearLayout) ll_edit.findViewById(R.id.llSectionEdit);

        al_llPair.add(new Pair<>(llHeaderEdit, llSectionEdit));
        LinearLayouts.put("Edit", al_llPair);

        ImageView ivArrowEdit = (ImageView) ll_edit.findViewById(R.id.ivArrowEdit);

        al_al_ImageViews.add(ivArrowEdit);
        ImageViews.put("Edit", al_al_ImageViews);

        TextView tv1Edit = (TextView) ll_edit.findViewById(R.id.tv1Edit);
        tv1Edit.setText(geaItemModelli.get(sectionNumber).getDescrizione_item());

        EditText et1Edit = (EditText) ll_edit.findViewById(R.id.et1Edit);

        ArrayList <EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1Edit);
        al_al_Edits.add(al_Edits);
        EditTexts.put("Edit", al_al_Edits);
    }

    public void createViewSectionHeader(int headerNumber, int idRes)
    {
        LinearLayout header = (LinearLayout) rootView.findViewById(idRes);
        FrameLayout flSectionHeader = (FrameLayout) header.findViewById(R.id.flSectionHeader);
        //flSectionHeader1.setOnClickListener(this);

        FrameLayouts.add(flSectionHeader);

        TextView tvSectionHeader = (TextView) header.findViewById(R.id.tvSectionHeader);
        tvSectionHeader.setText(geaSezioniModelli.get(headerNumber).getDescrizione_sezione());
    }


}
