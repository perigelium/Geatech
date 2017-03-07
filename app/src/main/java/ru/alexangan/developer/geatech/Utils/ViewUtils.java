package ru.alexangan.developer.geatech.Utils;

import android.text.InputType;
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
    int idItemStart, idItemEnd;
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
    Map<Integer, EditText> EditTexts;
    Map<Integer, Switch> Switches;
    Map<Integer, ArrayList<CheckBox>> CheckBoxes;

    Map <Integer, GeaItemModelliRapporto> itemModelli;

    public ViewUtils(View rootView, int id_rapporto_sopralluogo, int selectedIndex)
    {
        this.rootView = rootView;
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.selectedIndex = selectedIndex;
        headerNumber = 0;
        idModello = 0;
        idItemStart = 99999;
        idItemEnd = 0;

        LinearLayouts = new HashMap<>();
        RadioGroups = new HashMap<>();
        EditTexts = new HashMap<>();
        Switches = new HashMap<>();
        ImageViews = new HashMap<>();
        CheckBoxes = new HashMap<>();

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

        itemModelli = new HashMap();

        for (int i = 0; i < geaItemModelli.size(); i++)
        {
            int curItemModello = geaItemModelli.get(i).getId_item_modello();

            itemModelli.put(curItemModello, geaItemModelli.get(i));

            if (curItemModello < idItemStart)
            {
                idItemStart = curItemModello;
            }

            if(curItemModello > idItemEnd)
            {
                idItemEnd = curItemModello;
            }
        }
    }

    public View getRootView()
    {
        return rootView;
    }

    public int getIdItemStart()
    {
        return idItemStart;
    }

    public int getIdItemEnd()
    {
        return idItemEnd;
    }

    public Map<Integer, EditText> getEditTexts()
    {
        return EditTexts;
    }

    public Map<Integer, GeaItemModelliRapporto> getItemModelli()
    {
        return itemModelli;
    }

    public int createViewFiveChkboxes(int idItem, int idRes)
    {
        LinearLayout five_chkboxes = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFiveChkboxes = (LinearLayout) five_chkboxes.findViewById(R.id.llHeaderFiveChkboxes);

        final ImageView ivArrowFiveChkboxes = (ImageView) five_chkboxes.findViewById(R.id.ivArrowFiveChkboxes);
        
        ImageViews.put(idItem, ivArrowFiveChkboxes);

        TextView tvHeaderFiveChkboxes = (TextView) five_chkboxes.findViewById(R.id.tvHeaderFiveChkboxes);
        tvHeaderFiveChkboxes.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionFiveChkboxes = (LinearLayout) five_chkboxes.findViewById(R.id.llSectionFiveChkboxes);
        
        LinearLayouts.put(idItem, new Pair<>(llHeaderFiveChkboxes, llSectionFiveChkboxes));

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
        
        CheckBoxes.put(idItem, al_chkBoxes);

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        for (int i = 0; i < fields.length; i++)
        {
            al_chkBoxes.get(i).setText(fields[i]);
        }

        return  ++idItem;
    }

    public int createViewTwoTextsTwoEdits(int idItem, int idRes)
    {
        LinearLayout two_texts_two_edits = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llSectionTwoTextsTwoEdits = (LinearLayout) two_texts_two_edits.findViewById(R.id.llSectionTwoTextTwoEdit);
        
        LinearLayouts.put(idItem, new Pair<>(llSectionTwoTextsTwoEdits, llSectionTwoTextsTwoEdits));

        llSectionHeaders.add(llSectionTwoTextsTwoEdits);

        TextView tv1TwoTextsTwoEdits = (TextView) two_texts_two_edits.findViewById(R.id.tv1TwoTextTwoEdit);

        TextView tv2TwoTextsTwoEdits = (TextView) two_texts_two_edits.findViewById(R.id.tv2TwoTextTwoEdit);

        EditText et1TwoTextsTwoEdits = (EditText) two_texts_two_edits.findViewById(R.id.et1TwoTextTwoEdit);

        EditText et2TwoTextsTwoEdits = (EditText) two_texts_two_edits.findViewById(R.id.et2TwoTextTwoEdit);

        //ArrayList<EditText> al_Edits = new ArrayList<>();
        //al_Edits.add(et1TwoTextsTwoEdits);
        EditTexts.put(idItem, et1TwoTextsTwoEdits);
        tv1TwoTextsTwoEdits.setText(itemModelli.get(idItem++).getDescrizione_item());
        EditTexts.put(idItem, et2TwoTextsTwoEdits);
        //al_Edits.add(et2TwoTextsTwoEdits);
        tv2TwoTextsTwoEdits.setText(itemModelli.get(idItem++).getDescrizione_item());
        //EditTexts.put(idItem, al_Edits);




        return idItem;
    }

    public int createViewSwitchAndEdit(int idItem, int idRes)
    {
        LinearLayout switch_and_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderSwitchAndEdit = (LinearLayout) switch_and_edit.findViewById(R.id.llHeaderSwitchAndEdit);

        TextView tvHeaderSwitchAndEdit = (TextView) switch_and_edit.findViewById(R.id.tvHeaderSwitchAndEdit);
        tvHeaderSwitchAndEdit.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionSwitchAndEdit = (LinearLayout) switch_and_edit.findViewById(R.id.llSectionSwitchAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderSwitchAndEdit, llSectionSwitchAndEdit));

        llSectionHeaders.add(llSectionSwitchAndEdit);

        final ImageView ivArrowSwitchAndEdit = (ImageView) switch_and_edit.findViewById(R.id.ivArrowSwitchAndEdit);

        ImageViews.put(idItem, ivArrowSwitchAndEdit);

        llHeaderSwitchAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionSwitchAndEdit.setVisibility(llSectionSwitchAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowSwitchAndEdit.setImageResource(llSectionSwitchAndEdit.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        ArrayList<TextView> al_TextViews = new ArrayList<>();

        TextView tv1SwitchAndEdit = (TextView) switch_and_edit.findViewById(R.id.tv1SwitchAndEdit);
        al_TextViews.add(tv1SwitchAndEdit);

        Switch sw1SwitchAndEdit = (Switch) switch_and_edit.findViewById(R.id.sw1SwitchAndEdit);

        TextView tv2SwitchAndEdit = (TextView) switch_and_edit.findViewById(R.id.tv2SwitchAndEdit);
        //al_TextViews.add(tv2SwitchAndEdit);

        //Switch sw2SwitchAndEdit = (Switch) switch_and_edit.findViewById(R.id.sw2SwitchAndEdit);

        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1SwitchAndEdit);
        //al_Switches.add(sw2SwitchAndEdit);

        //Switches.put(idItem, sw2SwitchAndEdit);

        //TextView tv3SwitchAndEdit = (TextView) switch_and_edit.findViewById(R.id.tv3SwitchAndEdit);

        for (int i = 0; i < al_Switches.size(); i++)
        {
            Switches.put(idItem, al_Switches.get(i));
            al_TextViews.get(i).setText(itemModelli.get(idItem).getDescrizione_item());
            idItem++;
        }

        EditText et1SwitchAndEdit = (EditText) switch_and_edit.findViewById(R.id.et1SwitchAndEdit);

        EditTexts.put(idItem, et1SwitchAndEdit);
        tv2SwitchAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());
        idItem++;

        return idItem;
    }

    public int createViewTwoSwitchesAndEdit(int idItem, int idRes)
    {
        LinearLayout two_switches_and_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderTwoSwitchesAndEdit = (LinearLayout) two_switches_and_edit.findViewById(R.id.llHeaderTwoSwitchesAndEdit);

        TextView tvHeaderTwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tvHeaderTwoSwitchesAndEdit);
        tvHeaderTwoSwitchesAndEdit.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionTwoSwitchesAndEdit = (LinearLayout) two_switches_and_edit.findViewById(R.id.llSectionTwoSwitchesAndEdit);
        
        LinearLayouts.put(idItem, new Pair<>(llHeaderTwoSwitchesAndEdit, llSectionTwoSwitchesAndEdit));

        llSectionHeaders.add(llSectionTwoSwitchesAndEdit);

        final ImageView ivArrowTwoSwitchesAndEdit = (ImageView) two_switches_and_edit.findViewById(R.id.ivArrowTwoSwitchesAndEdit);
        
        ImageViews.put(idItem, ivArrowTwoSwitchesAndEdit);

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

        ArrayList<TextView> al_TextViews = new ArrayList<>();

        TextView tv1TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv1TwoSwitchesAndEdit);
        al_TextViews.add(tv1TwoSwitchesAndEdit);

        Switch sw1TwoSwitchesAndEdit = (Switch) two_switches_and_edit.findViewById(R.id.sw1TwoSwitchesAndEdit);

        TextView tv2TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv2TwoSwitchesAndEdit);
        al_TextViews.add(tv2TwoSwitchesAndEdit);

        Switch sw2TwoSwitchesAndEdit = (Switch) two_switches_and_edit.findViewById(R.id.sw2TwoSwitchesAndEdit);

        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1TwoSwitchesAndEdit);
        al_Switches.add(sw2TwoSwitchesAndEdit);

        Switches.put(idItem, sw2TwoSwitchesAndEdit);

        TextView tv3TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv3TwoSwitchesAndEdit);

        for (int i = 0; i < al_Switches.size(); i++)
        {
            Switches.put(idItem, al_Switches.get(i));
            al_TextViews.get(i).setText(itemModelli.get(idItem).getDescrizione_item());
            idItem++;
        }

        EditText et1TwoSwitchesAndEdit = (EditText) two_switches_and_edit.findViewById(R.id.et1TwoSwitchesAndEdit);

        EditTexts.put(idItem, et1TwoSwitchesAndEdit);
        tv3TwoSwitchesAndEdit.setText(itemModelli.get(idItem++).getDescrizione_item());

        return idItem;
    }

    public int createViewFiveSwitches(int idItem, int idRes)
    {
        LinearLayout five_switches = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFiveSwitches = (LinearLayout) five_switches.findViewById(R.id.llHeaderFiveSwitches);

        TextView tvHeaderFiveSwitches = (TextView) five_switches.findViewById(R.id.tvHeaderFiveSwitches);
        tvHeaderFiveSwitches.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionFiveSwitches = (LinearLayout) five_switches.findViewById(R.id.llSectionFiveSwitches);
        
        LinearLayouts.put(idItem, new Pair<>(llHeaderFiveSwitches, llSectionFiveSwitches));

        llSectionHeaders.add(llSectionFiveSwitches);

        final ImageView ivArrowFiveSwitches = (ImageView) five_switches.findViewById(R.id.ivArrowFiveSwitches);
        
        ImageViews.put(idItem, ivArrowFiveSwitches);

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

        Switch sw1FiveSwitches = (Switch) five_switches.findViewById(R.id.sw1FiveSwitches);

        TextView tv2FiveSwitches = (TextView) five_switches.findViewById(R.id.tv2FiveSwitches);

        Switch sw2FiveSwitches = (Switch) five_switches.findViewById(R.id.sw2FiveSwitches);

        TextView tv3FiveSwitches = (TextView) five_switches.findViewById(R.id.tv3FiveSwitches);

        Switch sw3FiveSwitches = (Switch) five_switches.findViewById(R.id.sw3FiveSwitches);

        TextView tv4FiveSwitches = (TextView) five_switches.findViewById(R.id.tv4FiveSwitches);

        Switch sw4FiveSwitches = (Switch) five_switches.findViewById(R.id.sw4FiveSwitches);

        TextView tv5FiveSwitches = (TextView) five_switches.findViewById(R.id.tv5FiveSwitches);

        Switch sw5FiveSwitches = (Switch) five_switches.findViewById(R.id.sw5FiveSwitches);

/*        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1FiveSwitches);
        al_Switches.add(sw2FiveSwitches);
        al_Switches.add(sw3FiveSwitches);
        al_Switches.add(sw4FiveSwitches);
        al_Switches.add(sw5FiveSwitches);*/

        Switches.put(idItem, sw1FiveSwitches);
        tv1FiveSwitches.setText(itemModelli.get(idItem++).getDescrizione_item());
        Switches.put(idItem, sw2FiveSwitches);
        tv2FiveSwitches.setText(itemModelli.get(idItem++).getDescrizione_item());
        Switches.put(idItem, sw3FiveSwitches);
        tv3FiveSwitches.setText(itemModelli.get(idItem++).getDescrizione_item());
        Switches.put(idItem, sw4FiveSwitches);
        tv4FiveSwitches.setText(itemModelli.get(idItem++).getDescrizione_item());
        Switches.put(idItem, sw5FiveSwitches);
        tv5FiveSwitches.setText(itemModelli.get(idItem++).getDescrizione_item());

        return idItem;
    }

    public int createViewFourRadios(int idItem, int idRes)
    {
        int i;
        
        LinearLayout four_radios = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFourRadios = (LinearLayout) four_radios.findViewById(R.id.llHeaderFourRadios);

        TextView tvHeaderFourRadios = (TextView) four_radios.findViewById(R.id.tvHeaderFourRadios);
        tvHeaderFourRadios.setText(itemModelli.get(idItem).getDescrizione_item());

        final ImageView ivArrowFourRadios = (ImageView) four_radios.findViewById(R.id.ivArrowFourRadios);
        
        ImageViews.put(idItem, ivArrowFourRadios);

        final LinearLayout llSectionFourRadios = (LinearLayout) four_radios.findViewById(R.id.llSectionFourRadios);
        
        LinearLayouts.put(idItem, new Pair<>(llHeaderFourRadios, llSectionFourRadios));

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
        
        RadioGroups.put(idItem, new Pair<>(rg1FourRadios, rg1FourRadios));

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1FourRadios.getChildCount())
        {
            for (i = 0; i < rg1FourRadios.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1FourRadios.getChildAt(i);

                rb.setText(fields[i]);
            }
        }
        return ++idItem;
    }

    public int createViewThreeRadiosAndEditEx(int idItem, int idRes)
    {
        LinearLayout three_radios_and_edit = (LinearLayout) rootView.findViewById(idRes);

        LinearLayout llHeaderThreeRadiosAndEdit = (LinearLayout) three_radios_and_edit.findViewById(R.id.llHeaderThreeRadiosAndEdit);

        final ImageView ivArrowThreeRadiosAndEdit = (ImageView) three_radios_and_edit.findViewById(R.id.ivArrowThreeRadiosAndEdit);

        ImageViews.put(idItem, ivArrowThreeRadiosAndEdit);

        TextView tvHeaderThreeRadiosAndEdit = (TextView) three_radios_and_edit.findViewById(R.id.tvHeaderThreeRadiosAndEdit);
        tvHeaderThreeRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionThreeRadiosAndEdit = (LinearLayout) three_radios_and_edit.findViewById(R.id.llSectionThreeRadiosAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderThreeRadiosAndEdit, llSectionThreeRadiosAndEdit));

        llSectionHeaders.add(llSectionThreeRadiosAndEdit);

        llHeaderThreeRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeRadiosAndEdit.setVisibility(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeRadiosAndEdit.setImageResource(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        final RadioGroup rg1ThreeRadiosAndEdit = (RadioGroup) three_radios_and_edit.findViewById(R.id.rg1ThreeRadiosAndEdit);

        RadioGroups.put(idItem, new Pair<>(rg1ThreeRadiosAndEdit, rg1ThreeRadiosAndEdit));

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        TextView tv1ThreeRadiosAndEdit = (TextView) three_radios_and_edit.findViewById(R.id.tv1ThreeRadiosAndEdit);

        final EditText et1ThreeRadiosAndEdit = (EditText) three_radios_and_edit.findViewById(R.id.et1ThreeRadiosAndEdit);

/*        et1ThreeRadiosAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1ThreeRadiosAndEdit.clearCheck();
                return false;
            }
        });*/

        if (fields.length >= rg1ThreeRadiosAndEdit.getChildCount())
        {
            for (int i = 0; i < rg1ThreeRadiosAndEdit.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1ThreeRadiosAndEdit.getChildAt(i);

                rb.setText(fields[i]);

                rb.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(rg1ThreeRadiosAndEdit.getCheckedRadioButtonId() == rg1ThreeRadiosAndEdit.getChildAt(1).getId())
                        {
                            et1ThreeRadiosAndEdit.setText("");
                            et1ThreeRadiosAndEdit.setEnabled(true);
                        }
                        else
                        {
                            et1ThreeRadiosAndEdit.setText("Non applicabile");
                            et1ThreeRadiosAndEdit.setEnabled(false);
                        }
                    }
                });
            }
        }

        idItem++;
        tv1ThreeRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());
        et1ThreeRadiosAndEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        et1ThreeRadiosAndEdit.setText("Non applicabile");
        EditTexts.put(idItem, et1ThreeRadiosAndEdit);

        return ++idItem;
    }

    public int createViewThreeRadiosAndEditTwo(int idItem, int idRes)
    {
        LinearLayout three_radios_and_edit = (LinearLayout) rootView.findViewById(idRes);

        LinearLayout llHeaderThreeRadiosAndEdit = (LinearLayout) three_radios_and_edit.findViewById(R.id.llHeaderThreeRadiosAndEdit);

        final ImageView ivArrowThreeRadiosAndEdit = (ImageView) three_radios_and_edit.findViewById(R.id.ivArrowThreeRadiosAndEdit);

        ImageViews.put(idItem, ivArrowThreeRadiosAndEdit);

        TextView tvHeaderThreeRadiosAndEdit = (TextView) three_radios_and_edit.findViewById(R.id.tvHeaderThreeRadiosAndEdit);
        tvHeaderThreeRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionThreeRadiosAndEdit = (LinearLayout) three_radios_and_edit.findViewById(R.id.llSectionThreeRadiosAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderThreeRadiosAndEdit, llSectionThreeRadiosAndEdit));

        llSectionHeaders.add(llSectionThreeRadiosAndEdit);

        llHeaderThreeRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeRadiosAndEdit.setVisibility(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeRadiosAndEdit.setImageResource(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        final RadioGroup rg1ThreeRadiosAndEdit = (RadioGroup) three_radios_and_edit.findViewById(R.id.rg1ThreeRadiosAndEdit);

        RadioGroups.put(idItem, new Pair<>(rg1ThreeRadiosAndEdit, rg1ThreeRadiosAndEdit));

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1ThreeRadiosAndEdit.getChildCount())
        {
            for (int i = 0; i < rg1ThreeRadiosAndEdit.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1ThreeRadiosAndEdit.getChildAt(i);

                rb.setText(fields[i]);
            }
        }

        TextView tv1ThreeRadiosAndEdit = (TextView) three_radios_and_edit.findViewById(R.id.tv1ThreeRadiosAndEdit);

        EditText et1ThreeRadiosAndEdit = (EditText) three_radios_and_edit.findViewById(R.id.et1ThreeRadiosAndEdit);

        et1ThreeRadiosAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1ThreeRadiosAndEdit.clearCheck();
                return false;
            }
        });

        idItem++;
        tv1ThreeRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());
        EditTexts.put(idItem, et1ThreeRadiosAndEdit);

        return ++idItem;
    }

    public int createViewThreeRadiosAndEdit(int idItem, int idRes)
    {
        LinearLayout three_radios_and_edit = (LinearLayout) rootView.findViewById(idRes);

        LinearLayout llHeaderThreeRadiosAndEdit = (LinearLayout) three_radios_and_edit.findViewById(R.id.llHeaderThreeRadiosAndEdit);

        final ImageView ivArrowThreeRadiosAndEdit = (ImageView) three_radios_and_edit.findViewById(R.id.ivArrowThreeRadiosAndEdit);

        ImageViews.put(idItem, ivArrowThreeRadiosAndEdit);

        TextView tvHeaderThreeRadiosAndEdit = (TextView) three_radios_and_edit.findViewById(R.id.tvHeaderThreeRadiosAndEdit);
        tvHeaderThreeRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionThreeRadiosAndEdit = (LinearLayout) three_radios_and_edit.findViewById(R.id.llSectionThreeRadiosAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderThreeRadiosAndEdit, llSectionThreeRadiosAndEdit));

        llSectionHeaders.add(llSectionThreeRadiosAndEdit);

        llHeaderThreeRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeRadiosAndEdit.setVisibility(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeRadiosAndEdit.setImageResource(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        final RadioGroup rg1ThreeRadiosAndEdit = (RadioGroup) three_radios_and_edit.findViewById(R.id.rg1ThreeRadiosAndEdit);

        RadioGroups.put(idItem, new Pair<>(rg1ThreeRadiosAndEdit, rg1ThreeRadiosAndEdit));

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1ThreeRadiosAndEdit.getChildCount())
        {
            for (int i = 0; i < rg1ThreeRadiosAndEdit.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1ThreeRadiosAndEdit.getChildAt(i);

                rb.setText(fields[i]);
            }
        }

        EditText et1ThreeRadiosAndEdit = (EditText) three_radios_and_edit.findViewById(R.id.et1ThreeRadiosAndEdit);

        EditTexts.put(idItem, et1ThreeRadiosAndEdit);

        et1ThreeRadiosAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1ThreeRadiosAndEdit.clearCheck();
                return false;
            }
        });

        return ++idItem;
    }
    
    public int createViewTwoRadiosAndEdit(int idItem, int idRes)
    {
        LinearLayout two_radios_and_edit = (LinearLayout) rootView.findViewById(idRes);

        LinearLayout llHeaderTwoRadiosAndEdit = (LinearLayout) two_radios_and_edit.findViewById(R.id.llHeaderTwoRadiosAndEdit);

        final ImageView ivArrowTwoRadiosAndEdit = (ImageView) two_radios_and_edit.findViewById(R.id.ivArrowTwoRadiosAndEdit);
        
        ImageViews.put(idItem, ivArrowTwoRadiosAndEdit);

        TextView tvHeaderTwoRadiosAndEdit = (TextView) two_radios_and_edit.findViewById(R.id.tvHeaderTwoRadiosAndEdit);
        tvHeaderTwoRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionTwoRadiosAndEdit = (LinearLayout) two_radios_and_edit.findViewById(R.id.llSectionTwoRadiosAndEdit);
        
        LinearLayouts.put(idItem, new Pair<>(llHeaderTwoRadiosAndEdit, llSectionTwoRadiosAndEdit));

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
        
        RadioGroups.put(idItem, new Pair<>(rg1TwoRadiosAndEdit, rg1TwoRadiosAndEdit));

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1TwoRadiosAndEdit.getChildCount())
        {
            for (int i = 0; i < rg1TwoRadiosAndEdit.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1TwoRadiosAndEdit.getChildAt(i);

                rb.setText(fields[i]);
            }
        }

        EditText et1TwoRadiosAndEdit = (EditText) two_radios_and_edit.findViewById(R.id.et1TwoRadiosAndEdit);
        
        EditTexts.put(idItem, et1TwoRadiosAndEdit);

        et1TwoRadiosAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1TwoRadiosAndEdit.clearCheck();
                return false;
            }
        });

        return ++idItem;
    }

    public int createViewThreeRadios(int idItem, int idRes)
    {
        int i;
        LinearLayout ll_three_radios = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderThreeRadios = (LinearLayout) ll_three_radios.findViewById(R.id.llHeaderThreeRadios);

        TextView tvHeaderThreeRadios = (TextView) ll_three_radios.findViewById(R.id.tvHeaderThreeRadios);
        tvHeaderThreeRadios.setText(itemModelli.get(idItem).getDescrizione_item());

        final ImageView ivArrowThreeRadios = (ImageView) ll_three_radios.findViewById(R.id.ivArrowThreeRadios);

        ImageViews.put(idItem, ivArrowThreeRadios);

        final LinearLayout llSectionThreeRadios = (LinearLayout) ll_three_radios.findViewById(R.id.llSectionThreeRadios);

        LinearLayouts.put(idItem, new Pair<>(llHeaderThreeRadios, llSectionThreeRadios));

        llSectionHeaders.add(llSectionThreeRadios);

        llHeaderThreeRadios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeRadios.setVisibility(llSectionThreeRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeRadios.setImageResource(llSectionThreeRadios.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        RadioGroup rg1ThreeRadios = (RadioGroup) ll_three_radios.findViewById(R.id.rg1ThreeRadios);

        RadioGroups.put(idItem, new Pair<>(rg1ThreeRadios, rg1ThreeRadios));

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1ThreeRadios.getChildCount())
        {
            for (i = 0; i < rg1ThreeRadios.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1ThreeRadios.getChildAt(i);

                rb.setText(fields[i]);
            }
        }
        return  ++idItem;
    }
    
    public int createViewTwoRadios(int idItem, int idRes)
    {
        int i;
        LinearLayout ll_two_radios = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderTwoRadios = (LinearLayout) ll_two_radios.findViewById(R.id.llHeaderTwoRadios);

        TextView tvHeaderTwoRadios = (TextView) ll_two_radios.findViewById(R.id.tvHeaderTwoRadios);
        tvHeaderTwoRadios.setText(itemModelli.get(idItem).getDescrizione_item());

        final ImageView ivArrowTwoRadios = (ImageView) ll_two_radios.findViewById(R.id.ivArrowTwoRadios);
        
        ImageViews.put(idItem, ivArrowTwoRadios);

        final LinearLayout llSectionTwoRadios = (LinearLayout) ll_two_radios.findViewById(R.id.llSectionTwoRadios);
        
        LinearLayouts.put(idItem, new Pair<>(llHeaderTwoRadios, llSectionTwoRadios));

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
        
        RadioGroups.put(idItem, new Pair<>(rg1TwoRadios, rg1TwoRadios));

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1TwoRadios.getChildCount())
        {
            for (i = 0; i < rg1TwoRadios.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1TwoRadios.getChildAt(i);

                rb.setText(fields[i]);
            }
        }
        return  ++idItem;
    }

    public int createViewFourRadiosAndTwoEdits(int idItem, int idRes)
    {
        int i;
        
        LinearLayout four_radios_and_two_edits = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFourRadiosAndTwoEdits = (LinearLayout) four_radios_and_two_edits.findViewById(R.id.llHeaderFourRadiosAndTwoEdits);

        TextView tvHeaderFourRadiosAndTwoEdits = (TextView) four_radios_and_two_edits.findViewById(R.id.tvHeaderFourRadiosAndTwoEdits);
        tvHeaderFourRadiosAndTwoEdits.setText(itemModelli.get(idItem).getDescrizione_item());

        final ImageView ivArrowFourRadiosAndTwoEdits = (ImageView) four_radios_and_two_edits.findViewById(R.id.ivArrowFourRadiosAndTwoEdits);

        ImageViews.put(idItem, ivArrowFourRadiosAndTwoEdits);

        final LinearLayout llSectionFourRadiosAndTwoEdits = (LinearLayout) four_radios_and_two_edits.findViewById(R.id.llSectionFourRadiosAndTwoEdits);

        LinearLayouts.put(idItem, new Pair<>(llHeaderFourRadiosAndTwoEdits, llSectionFourRadiosAndTwoEdits));

        llSectionHeaders.add(llSectionFourRadiosAndTwoEdits);

        llHeaderFourRadiosAndTwoEdits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFourRadiosAndTwoEdits.setVisibility(llSectionFourRadiosAndTwoEdits.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFourRadiosAndTwoEdits.setImageResource(llSectionFourRadiosAndTwoEdits.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        final RadioGroup rg1FourRadiosAndTwoEdits = (RadioGroup) four_radios_and_two_edits.findViewById(R.id.rg1FourRadiosAndTwoEdits);

        RadioGroups.put(idItem, new Pair<>(rg1FourRadiosAndTwoEdits, rg1FourRadiosAndTwoEdits));

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1FourRadiosAndTwoEdits.getChildCount())
        {
            if (fields.length >= rg1FourRadiosAndTwoEdits.getChildCount())
            {
                for (i = 0; i < rg1FourRadiosAndTwoEdits.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1FourRadiosAndTwoEdits.getChildAt(i);

                    rb.setText(fields[i]);
                }
            }
        }

        //TextView tv1FourRadiosAndTwoEdits = (TextView) four_radios_and_two_edits.findViewById(R.id.tv1FourRadiosAndTwoEdits);

        EditText et1FourRadiosAndTwoEdits = (EditText) four_radios_and_two_edits.findViewById(R.id.et1FourRadiosAndTwoEdits);
        EditTexts.put(idItem, et1FourRadiosAndTwoEdits);

        et1FourRadiosAndTwoEdits.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1FourRadiosAndTwoEdits.clearCheck();
                return false;
            }
        });

        idItem++;

        TextView tv2FourRadiosAndTwoEdits = (TextView) four_radios_and_two_edits.findViewById(R.id.tv2FourRadiosAndTwoEdits);
        tv2FourRadiosAndTwoEdits.setText(itemModelli.get(idItem).getDescrizione_item());

        EditText et2FourRadiosAndTwoEdits = (EditText) four_radios_and_two_edits.findViewById(R.id.et2FourRadiosAndTwoEdits);
        EditTexts.put(idItem, et2FourRadiosAndTwoEdits);

        return ++idItem;
    }

    public int createViewFourRadiosAndEdit(int idItem, int idRes)
    {
        int i;
        

        LinearLayout four_radios_and_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFourRadiosAndEdit = (LinearLayout) four_radios_and_edit.findViewById(R.id.llHeaderFourRadiosAndEdit);

        TextView tvHeaderFourRadiosAndEdit = (TextView) four_radios_and_edit.findViewById(R.id.tvHeaderFourRadiosAndEdit);
        tvHeaderFourRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final ImageView ivArrowFourRadiosAndEdit = (ImageView) four_radios_and_edit.findViewById(R.id.ivArrowFourRadiosAndEdit);

        ImageViews.put(idItem, ivArrowFourRadiosAndEdit);

        final LinearLayout llSectionFourRadiosAndEdit = (LinearLayout) four_radios_and_edit.findViewById(R.id.llSectionFourRadiosAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderFourRadiosAndEdit, llSectionFourRadiosAndEdit));

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
        
        RadioGroups.put(idItem, new Pair<>(rg1FourRadiosAndEdit, rg1FourRadiosAndEdit));

        String valore = itemModelli.get(idItem).getValore();
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

/*        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1FourRadiosAndEdit);*/
        EditTexts.put(idItem, et1FourRadiosAndEdit);

        et1FourRadiosAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1FourRadiosAndEdit.clearCheck();
                return false;
            }
        });
        return ++idItem;
    }

    public int createViewThreeEdits(int idItem, int idRes)
    {
        LinearLayout three_edits = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderThreeEdits = (LinearLayout) three_edits.findViewById(R.id.llHeaderThreeEdits);

        TextView tvHeaderThreeEdits = (TextView) three_edits.findViewById(R.id.tvHeaderThreeEdits);
        tvHeaderThreeEdits.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionThreeEdits = (LinearLayout) three_edits.findViewById(R.id.llSectionThreeEdits);

        LinearLayouts.put(idItem, new Pair<>(llHeaderThreeEdits, llSectionThreeEdits));

        llSectionHeaders.add(llSectionThreeEdits);

        final ImageView ivArrowThreeEdits = (ImageView) three_edits.findViewById(R.id.ivArrowThreeEdits);

        ImageViews.put(idItem, ivArrowThreeEdits);

        llHeaderThreeEdits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeEdits.setVisibility(llSectionThreeEdits.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeEdits.setImageResource(llSectionThreeEdits.getVisibility() == View.VISIBLE
                        ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            }
        });

        ArrayList<TextView> al_tvs = new ArrayList<>();

        TextView tv1ThreeEdits = (TextView) three_edits.findViewById(R.id.tv1ThreeEdits);
        EditText et1ThreeEdits = (EditText) three_edits.findViewById(R.id.et1ThreeEdits);
        TextView tv2ThreeEdits = (TextView) three_edits.findViewById(R.id.tv2ThreeEdits);
        EditText et2ThreeEdits = (EditText) three_edits.findViewById(R.id.et2ThreeEdits);
        TextView tv3ThreeEdits = (TextView) three_edits.findViewById(R.id.tv3ThreeEdits);
        EditText et3ThreeEdits = (EditText) three_edits.findViewById(R.id.et3ThreeEdits);


        al_tvs.add(tv1ThreeEdits);
        al_tvs.add(tv2ThreeEdits);
        al_tvs.add(tv3ThreeEdits);

        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1ThreeEdits);
        al_Edits.add(et2ThreeEdits);
        al_Edits.add(et3ThreeEdits);
        EditTexts.put(idItem, et1ThreeEdits);


        for (int i = 0; i < al_tvs.size(); i++)
        {
            EditTexts.put(idItem, al_Edits.get(i));
            al_tvs.get(i).setText(itemModelli.get(idItem++).getDescrizione_item());
        }

        return idItem;
    }

    public int createViewFourEditsAndSwitch(int idItem, int idRes)
    {
        LinearLayout four_edits_and_switch = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFourEditsAndSwitch = (LinearLayout) four_edits_and_switch.findViewById(R.id.llHeaderFourEditsAndSwitch);

        TextView tvHeaderFourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tvHeaderFourEditsAndSwitch);
        tvHeaderFourEditsAndSwitch.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionFourEditsAndSwitch = (LinearLayout) four_edits_and_switch.findViewById(R.id.llSectionFourEditsAndSwitch);
        
        LinearLayouts.put(idItem, new Pair<>(llHeaderFourEditsAndSwitch, llSectionFourEditsAndSwitch));

        llSectionHeaders.add(llSectionFourEditsAndSwitch);

        final ImageView ivArrowFourEditsAndSwitch = (ImageView) four_edits_and_switch.findViewById(R.id.ivArrowFourEditsAndSwitch);
        
        ImageViews.put(idItem, ivArrowFourEditsAndSwitch);

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
        EditTexts.put(idItem, et1FourEditsAndSwitch);

        TextView tv5FourEditsAndSwitch = (TextView) four_edits_and_switch.findViewById(R.id.tv5FourEditsAndSwitch);

        Switch sw1FourEditsAndSwitch = (Switch) four_edits_and_switch.findViewById(R.id.sw1FourEditsAndSwitch);

/*        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1FourEditsAndSwitch);*/


        for (int i = 0; i < al_tvs.size(); i++)
        {
            EditTexts.put(idItem, al_Edits.get(i));
            al_tvs.get(i).setText(itemModelli.get(idItem++).getDescrizione_item());
        }
        Switches.put(idItem, sw1FourEditsAndSwitch);
        tv5FourEditsAndSwitch.setText(itemModelli.get(idItem++).getDescrizione_item());

        return idItem;
    }

    public int createViewEdit(int idItem, int idRes)
    {
        LinearLayout ll_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderEdit = (LinearLayout) ll_edit.findViewById(R.id.llHeaderEdit);

        TextView tvHeaderEdit = (TextView) ll_edit.findViewById(R.id.tvHeaderEdit);
        tvHeaderEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionEdit = (LinearLayout) ll_edit.findViewById(R.id.llSectionEdit);

        llSectionHeaders.add(llSectionEdit);
        
        LinearLayouts.put(idItem, new Pair<>(llHeaderEdit, llSectionEdit));

        final ImageView ivArrowEdit = (ImageView) ll_edit.findViewById(R.id.ivArrowEdit);
        
        ImageViews.put(idItem, ivArrowEdit);

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
        tv1Edit.setText(itemModelli.get(idItem).getDescrizione_item());

        EditText et1Edit = (EditText) ll_edit.findViewById(R.id.et1Edit);

/*        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1Edit);*/
        EditTexts.put(idItem, et1Edit);

        return ++idItem;
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int saveSeveralRadiosAndEdit(int idItem)
    {
        String str_Id_item="";
        RadioGroup rg = RadioGroups.get(idItem).first;
        int checkedBtnId = rg.getCheckedRadioButtonId();

        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg.findViewById(checkedBtnId);
            str_Id_item = radioButton.getText().toString();
        } else
        {
            EditText et = EditTexts.get(idItem);
            str_Id_item = et.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItem, str_Id_item);

        return  ++idItem;
    }

    public int saveSeveralRadios(int idItem)
    {
        String str_Id_item="";
        RadioGroup rg = RadioGroups.get(idItem).first;
        int checkedBtnId = rg.getCheckedRadioButtonId();

        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg.findViewById(checkedBtnId);
            str_Id_item = radioButton.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItem, str_Id_item);

        return  ++idItem;
    }

    public int saveSeveralChkboxes(int idItem)
    {
        ArrayList<CheckBox> alChks = CheckBoxes.get(idItem);
        String str_Id_item = "";

        for (int i = 0; i < alChks.size(); i++)
        {
            str_Id_item += alChks.get(i).isChecked() ? alChks.get(i).getText().toString() + "||" : "";
        }
        DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItem, str_Id_item);

        return ++idItem;
    }

    public int saveSeveralSwitches(int idItem, int quant)
    {

        String str_Id_item = "";

        for (int i = 0; i < quant; i++)
        {
            Switch sw = Switches.get(idItem);
            str_Id_item = sw.isChecked() ? "SI" : "NO";
            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItem, str_Id_item);
            idItem++;
        }

        return idItem;
    }

    public int saveSeveralEdits(int idItem, int quant)
    {
        for (int i = 0; i < quant; i++)
        {
            EditText et = EditTexts.get(idItem);
            String str_id_item = et.getText().toString();
            DatabaseUtils.insertStringInReportItem(id_rapporto_sopralluogo, idItem, str_id_item);
            idItem++;
        }

        return idItem;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public int fillSeveralRadiosAndEdit(int idItem)
    {
        int i;
        String strId_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);

        RadioGroup radiogroup = RadioGroups.get(idItem).first;

        if (strId_item != null)
        {
            for (i = 0; i < radiogroup.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) radiogroup.getChildAt(i);

                if (strId_item.equals(rb.getText().toString()))
                {
                    rb.setChecked(true);
                    break;
                }
            }

            if (i == radiogroup.getChildCount())
            {
                EditTexts.get(idItem).setText(strId_item);
            }
        }
        return ++idItem;
    }

    public int fillSeveralRadios(int idItem)
    {
        int i;
        String strId_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);

        RadioGroup radiogroup = RadioGroups.get(idItem).first;

        if (strId_item != null)
        {
            for (i = 0; i < radiogroup.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) radiogroup.getChildAt(i);

                if (strId_item.equals(rb.getText().toString()))
                {
                    rb.setChecked(true);
                    break;
                }
            }
        }
        return ++idItem;
    }

    public int fillSeveralEdits(int idItem, int quant)
    {
        for (int i = 0; i < quant; i++)
        {
            String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);
            EditTexts.get(idItem).setText(str_id_item);
            idItem++;
        }

        return idItem;
    }

    public int fillSeveralSwitches(int idItem, int quant)
    {
        for (int i = 0; i < quant; i++)
        {
            String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);
            Switch sw = Switches.get(idItem);

            if(str_id_item.equals("SI"))
            {
                sw.setChecked(true);
            }
            idItem++;
        }

        return idItem;
    }

    public int fillSeveralChkboxes(int idItem)
    {
        String str_id_item = DatabaseUtils.getValueFromReportItem(id_rapporto_sopralluogo, idItem);

        ArrayList<CheckBox> al_Chks = CheckBoxes.get(idItem);

        for (CheckBox chkbox : al_Chks)
        {
            String chkBoxText = chkbox.getText().toString();

            if(str_id_item.contains(chkBoxText))
            {
                chkbox.setChecked(true);
            }
        }

        return ++idItem;
    }
}
