package ru.alexangan.developer.geatech.Utils;

import android.graphics.Color;
import android.text.InputType;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class ViewUtils
{
    private final Realm realm;

    private ReportItem reportItem;
    private List<GeaItemRapporto> l_geaItemRapporto;
    private int idItemStart, idItemEnd;
    private int headerNumber;
    private View rootView;

    boolean[] allSectionsCollapsed;
    GeaModelloRapporto geaModello;
    List<GeaSezioneModelliRapporto> geaSezioniModelli;
    ArrayList<ArrayList<LinearLayout>> al_llHeaderSections;
    ArrayList<LinearLayout> llHeaderSections;

    Map<Integer, ImageView> ImageViews;
    Map<Integer, Pair<LinearLayout, LinearLayout>> LinearLayouts;
    Map<Integer, RadioGroup> RadioGroups;
    Map<Integer, EditText> EditTexts;
    Map<Integer, Switch> Switches;
    Map<Integer, ArrayList<CheckBox>> CheckBoxes;

    Map<Integer, GeaItemModelliRapporto> itemModelli;

    public ViewUtils(View rootView, int id_rapporto_sopralluogo, int selectedVisitId)
    {
        this.rootView = rootView;
        headerNumber = 0;
        idItemStart = 99999;
        idItemEnd = 0;

        LinearLayouts = new HashMap<>();
        RadioGroups = new HashMap<>();
        EditTexts = new HashMap<>();
        Switches = new HashMap<>();
        ImageViews = new HashMap<>();
        CheckBoxes = new HashMap<>();

        al_llHeaderSections = new ArrayList<>();
        llHeaderSections = new ArrayList<>();
        allSectionsCollapsed = new boolean[]{false, false, false, false, false, false, false};

        VisitItem visitItem = visitItems.get(selectedVisitId);
        //GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        int id_product_type = productData.getIdProductType();

        realm = Realm.getDefaultInstance();

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
        List<GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class)
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

            if (curItemModello > idItemEnd)
            {
                idItemEnd = curItemModello;
            }
        }

        realm.beginTransaction();
        reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findFirst();
        realm.commitTransaction();

        l_geaItemRapporto = reportItem.getGea_items_rapporto_sopralluogo();
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

    public Map<Integer, Pair<LinearLayout, LinearLayout>> getLinearLayouts()
    {
        return LinearLayouts;
    }

    public Map<Integer, Switch> getSwitches()
    {
        return Switches;
    }

    public Map<Integer, RadioGroup> getRadioGroups()
    {
        return RadioGroups;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int createViewFourCheckboxes(int idItem, int idRes)
    {
        LinearLayout four_chkboxes = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFourChkboxes = (LinearLayout) four_chkboxes.findViewById(R.id.llHeaderFourChkboxes);

        final ImageView ivArrowFourChkboxes = (ImageView) four_chkboxes.findViewById(R.id.ivArrowFourChkboxes);

        ImageViews.put(idItem, ivArrowFourChkboxes);

        final TextView tvHeaderFourChkboxes = (TextView) four_chkboxes.findViewById(R.id.tvHeaderFourChkboxes);
        tvHeaderFourChkboxes.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionFourChkboxes = (LinearLayout) four_chkboxes.findViewById(R.id.llSectionFourChkboxes);

        LinearLayouts.put(idItem, new Pair<>(llHeaderFourChkboxes, llSectionFourChkboxes));

        llHeaderSections.add(llSectionFourChkboxes);

        llHeaderFourChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFourChkboxes.setVisibility(llSectionFourChkboxes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFourChkboxes.setImageResource(llSectionFourChkboxes.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final CheckBox chk1FourChkboxes = (CheckBox) four_chkboxes.findViewById(R.id.chk1FourChkboxes);
        final CheckBox chk2FourChkboxes = (CheckBox) four_chkboxes.findViewById(R.id.chk2FourChkboxes);
        final CheckBox chk3FourChkboxes = (CheckBox) four_chkboxes.findViewById(R.id.chk3FourChkboxes);
        final CheckBox chk4FourChkboxes = (CheckBox) four_chkboxes.findViewById(R.id.chk4FourChkboxes);

        final ArrayList<CheckBox> al_chkBoxes = new ArrayList<>();
        al_chkBoxes.add(chk1FourChkboxes);
        al_chkBoxes.add(chk2FourChkboxes);
        al_chkBoxes.add(chk3FourChkboxes);
        al_chkBoxes.add(chk4FourChkboxes);

        final TextView tv1FourChkboxes = (TextView) four_chkboxes.findViewById(R.id.tv1FourChkboxes);
        final TextView tv2FourChkboxes = (TextView) four_chkboxes.findViewById(R.id.tv2FourChkboxes);
        final TextView tv3FourChkboxes = (TextView) four_chkboxes.findViewById(R.id.tv3FourChkboxes);
        final TextView tv4FourChkboxes = (TextView) four_chkboxes.findViewById(R.id.tv4FourChkboxes);

        final ArrayList<TextView> al_TextViews = new ArrayList<>();
        al_TextViews.add(tv1FourChkboxes);
        al_TextViews.add(tv2FourChkboxes);
        al_TextViews.add(tv3FourChkboxes);
        al_TextViews.add(tv4FourChkboxes);

        final FrameLayout fl1FourChkboxes = (FrameLayout) four_chkboxes.findViewById(R.id.fl1FourChkboxes);
        final FrameLayout fl2FourChkboxes = (FrameLayout) four_chkboxes.findViewById(R.id.fl2FourChkboxes);
        final FrameLayout fl3FourChkboxes = (FrameLayout) four_chkboxes.findViewById(R.id.fl3FourChkboxes);
        final FrameLayout fl4FourChkboxes = (FrameLayout) four_chkboxes.findViewById(R.id.fl4FourChkboxes);

        fl1FourChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk1FourChkboxes.setChecked(!chk1FourChkboxes.isChecked());

                if (chk1FourChkboxes.isChecked())
                {
                    for (int i = 0; i < al_chkBoxes.size(); i += 2)
                    {
                        al_chkBoxes.get(i).setChecked(false);
                        al_TextViews.get(i).setTextColor(Color.parseColor("#ff808080"));
                    }
                    chk1FourChkboxes.setChecked(true);

                    tv1FourChkboxes.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv1FourChkboxes.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        fl2FourChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk2FourChkboxes.setChecked(!chk2FourChkboxes.isChecked());

                if (chk2FourChkboxes.isChecked())
                {
                    for (int i = 1; i < al_chkBoxes.size(); i += 2)
                    {
                        al_chkBoxes.get(i).setChecked(false);
                        al_TextViews.get(i).setTextColor(Color.parseColor("#ff808080"));
                    }
                    chk2FourChkboxes.setChecked(true);

                    tv2FourChkboxes.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv2FourChkboxes.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        fl3FourChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk3FourChkboxes.setChecked(!chk3FourChkboxes.isChecked());

                if (chk3FourChkboxes.isChecked())
                {
                    for (int i = 0; i < al_chkBoxes.size(); i += 2)
                    {
                        al_chkBoxes.get(i).setChecked(false);
                        al_TextViews.get(i).setTextColor(Color.parseColor("#ff808080"));
                    }
                    chk3FourChkboxes.setChecked(true);

                    tv3FourChkboxes.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv3FourChkboxes.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        fl4FourChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk4FourChkboxes.setChecked(!chk4FourChkboxes.isChecked());

                if (chk4FourChkboxes.isChecked())
                {
                    for (int i = 1; i < al_chkBoxes.size(); i += 2)
                    {
                        al_chkBoxes.get(i).setChecked(false);
                        al_TextViews.get(i).setTextColor(Color.parseColor("#ff808080"));
                    }
                    chk4FourChkboxes.setChecked(true);

                    tv4FourChkboxes.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv4FourChkboxes.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        String valore = itemModelli.get(idItem).getValore();
        final String[] fields = valore.split("\\|\\|");

        for (int i = 0; i < al_TextViews.size(); i++)
        {
            al_chkBoxes.get(i).setText(fields[i]);
            al_TextViews.get(i).setText(fields[i]);
        }

        CheckBoxes.put(idItem, al_chkBoxes);

        return ++idItem;
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

        llHeaderSections.add(llSectionFiveChkboxes);

        llHeaderFiveChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFiveChkboxes.setVisibility(llSectionFiveChkboxes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFiveChkboxes.setImageResource(llSectionFiveChkboxes.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final CheckBox chk1FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk1FiveChkboxes);
        final CheckBox chk2FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk2FiveChkboxes);
        final CheckBox chk3FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk3FiveChkboxes);
        final CheckBox chk4FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk4FiveChkboxes);
        final CheckBox chk5FiveChkboxes = (CheckBox) five_chkboxes.findViewById(R.id.chk5FiveChkboxes);


        ArrayList<CheckBox> al_chkBoxes = new ArrayList<>();
        al_chkBoxes.add(chk1FiveChkboxes);
        al_chkBoxes.add(chk2FiveChkboxes);
        al_chkBoxes.add(chk3FiveChkboxes);
        al_chkBoxes.add(chk4FiveChkboxes);
        al_chkBoxes.add(chk5FiveChkboxes);

        final TextView tv1FiveChkboxes = (TextView) five_chkboxes.findViewById(R.id.tv1FiveChkboxes);
        final TextView tv2FiveChkboxes = (TextView) five_chkboxes.findViewById(R.id.tv2FiveChkboxes);
        final TextView tv3FiveChkboxes = (TextView) five_chkboxes.findViewById(R.id.tv3FiveChkboxes);
        final TextView tv4FiveChkboxes = (TextView) five_chkboxes.findViewById(R.id.tv4FiveChkboxes);
        final TextView tv5FiveChkboxes = (TextView) five_chkboxes.findViewById(R.id.tv5FiveChkboxes);

        final ArrayList<TextView> al_TextViews = new ArrayList<>();
        al_TextViews.add(tv1FiveChkboxes);
        al_TextViews.add(tv2FiveChkboxes);
        al_TextViews.add(tv3FiveChkboxes);
        al_TextViews.add(tv4FiveChkboxes);
        al_TextViews.add(tv5FiveChkboxes);

        final FrameLayout fl1FiveChkboxes = (FrameLayout) five_chkboxes.findViewById(R.id.fl1FiveChkboxes);
        final FrameLayout fl2FiveChkboxes = (FrameLayout) five_chkboxes.findViewById(R.id.fl2FiveChkboxes);
        final FrameLayout fl3FiveChkboxes = (FrameLayout) five_chkboxes.findViewById(R.id.fl3FiveChkboxes);
        final FrameLayout fl4FiveChkboxes = (FrameLayout) five_chkboxes.findViewById(R.id.fl4FiveChkboxes);
        final FrameLayout fl5FiveChkboxes = (FrameLayout) five_chkboxes.findViewById(R.id.fl5FiveChkboxes);

        fl1FiveChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk1FiveChkboxes.setChecked(!chk1FiveChkboxes.isChecked());

                if (chk1FiveChkboxes.isChecked())
                {
                    tv1FiveChkboxes.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv1FiveChkboxes.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        fl2FiveChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk2FiveChkboxes.setChecked(!chk2FiveChkboxes.isChecked());

                if (chk2FiveChkboxes.isChecked())
                {
                    tv2FiveChkboxes.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv2FiveChkboxes.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        fl3FiveChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk3FiveChkboxes.setChecked(!chk3FiveChkboxes.isChecked());

                if (chk3FiveChkboxes.isChecked())
                {
                    tv3FiveChkboxes.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv3FiveChkboxes.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        fl4FiveChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk4FiveChkboxes.setChecked(!chk4FiveChkboxes.isChecked());

                if (chk4FiveChkboxes.isChecked())
                {
                    tv4FiveChkboxes.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv4FiveChkboxes.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        fl5FiveChkboxes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk5FiveChkboxes.setChecked(!chk5FiveChkboxes.isChecked());

                if (chk5FiveChkboxes.isChecked())
                {
                    tv5FiveChkboxes.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv5FiveChkboxes.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        for (int i = 0; i < al_TextViews.size(); i++)
        {
            al_chkBoxes.get(i).setText(fields[i]);
            al_TextViews.get(i).setText(fields[i]);
        }
        CheckBoxes.put(idItem, al_chkBoxes);

        return ++idItem;
    }

    public int createViewFourTextsFourEdits(int idItem, int idRes)
    {
        LinearLayout four_texts_four_edits = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llSectionTwoTextsTwoEdits = (LinearLayout) four_texts_four_edits.findViewById(R.id.llSectionFourTextsFourEdits);

        LinearLayouts.put(idItem, new Pair<>(four_texts_four_edits, llSectionTwoTextsTwoEdits));

        llHeaderSections.add(llSectionTwoTextsTwoEdits);

        TextView tv1TwoTextsTwoEdits = (TextView) four_texts_four_edits.findViewById(R.id.tv1FourTextsFourEdits);
        TextView tv2TwoTextsTwoEdits = (TextView) four_texts_four_edits.findViewById(R.id.tv2FourTextsFourEdits);
        TextView tv3TwoTextsTwoEdits = (TextView) four_texts_four_edits.findViewById(R.id.tv3FourTextsFourEdits);
        TextView tv4TwoTextsTwoEdits = (TextView) four_texts_four_edits.findViewById(R.id.tv4FourTextsFourEdits);

        EditText et1TwoTextsTwoEdits = (EditText) four_texts_four_edits.findViewById(R.id.et1FourTextsFourEdits);
        EditText et2TwoTextsTwoEdits = (EditText) four_texts_four_edits.findViewById(R.id.et2FourTextsFourEdits);
        EditText et3TwoTextsTwoEdits = (EditText) four_texts_four_edits.findViewById(R.id.et3FourTextsFourEdits);
        EditText et4TwoTextsTwoEdits = (EditText) four_texts_four_edits.findViewById(R.id.et4FourTextsFourEdits);

        EditTexts.put(idItem, et1TwoTextsTwoEdits);
        tv1TwoTextsTwoEdits.setText(itemModelli.get(idItem++).getValore());
        EditTexts.put(idItem, et2TwoTextsTwoEdits);
        tv2TwoTextsTwoEdits.setText(itemModelli.get(idItem++).getValore());
        EditTexts.put(idItem, et3TwoTextsTwoEdits);
        tv3TwoTextsTwoEdits.setText(itemModelli.get(idItem++).getValore());
        EditTexts.put(idItem, et4TwoTextsTwoEdits);
        tv4TwoTextsTwoEdits.setText(itemModelli.get(idItem++).getValore());

        return idItem;
    }

    public int createViewThreeTextsThreeEdits(int idItem, int idRes)
    {
        LinearLayout three_texts_three_edits = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llSectionThreeTextsThreeEdits = (LinearLayout) three_texts_three_edits.findViewById(R.id.llSectionThreeTextsThreeEdits);

        LinearLayouts.put(idItem, new Pair<>(three_texts_three_edits, llSectionThreeTextsThreeEdits));

        llHeaderSections.add(llSectionThreeTextsThreeEdits);

        TextView tv1ThreeTextsThreeEdits = (TextView) three_texts_three_edits.findViewById(R.id.tv1ThreeTextsThreeEdits);
        TextView tv2ThreeTextsThreeEdits = (TextView) three_texts_three_edits.findViewById(R.id.tv2ThreeTextsThreeEdits);
        TextView tv3ThreeTextsThreeEdits = (TextView) three_texts_three_edits.findViewById(R.id.tv3ThreeTextsThreeEdits);

        EditText et1ThreeTextsThreeEdits = (EditText) three_texts_three_edits.findViewById(R.id.et1ThreeTextsThreeEdits);
        EditText et2ThreeTextsThreeEdits = (EditText) three_texts_three_edits.findViewById(R.id.et2ThreeTextsThreeEdits);
        EditText et3ThreeTextsThreeEdits = (EditText) three_texts_three_edits.findViewById(R.id.et3ThreeTextsThreeEdits);

        EditTexts.put(idItem, et1ThreeTextsThreeEdits);
        tv1ThreeTextsThreeEdits.setText(itemModelli.get(idItem++).getValore());
        EditTexts.put(idItem, et2ThreeTextsThreeEdits);
        tv2ThreeTextsThreeEdits.setText(itemModelli.get(idItem++).getValore());
        EditTexts.put(idItem, et3ThreeTextsThreeEdits);
        tv3ThreeTextsThreeEdits.setText(itemModelli.get(idItem++).getValore());

        return idItem;
    }

    public int createViewTwoTextsTwoEdits(int idItem, int idRes)
    {
        LinearLayout two_texts_two_edits = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llSectionTwoTextsTwoEdits = (LinearLayout) two_texts_two_edits.findViewById(R.id.llSectionTwoTextsTwoEdits);

        LinearLayouts.put(idItem, new Pair<>(two_texts_two_edits, llSectionTwoTextsTwoEdits));

        llHeaderSections.add(llSectionTwoTextsTwoEdits);

        TextView tv1TwoTextsTwoEdits = (TextView) two_texts_two_edits.findViewById(R.id.tv1TwoTextsTwoEdits);

        TextView tv2TwoTextsTwoEdits = (TextView) two_texts_two_edits.findViewById(R.id.tv2TwoTextsTwoEdits);

        EditText et1TwoTextsTwoEdits = (EditText) two_texts_two_edits.findViewById(R.id.et1TwoTextsTwoEdits);

        EditText et2TwoTextsTwoEdits = (EditText) two_texts_two_edits.findViewById(R.id.et2TwoTextsTwoEdits);

        EditTexts.put(idItem, et1TwoTextsTwoEdits);
        tv1TwoTextsTwoEdits.setText(itemModelli.get(idItem++).getValore());
        EditTexts.put(idItem, et2TwoTextsTwoEdits);
        tv2TwoTextsTwoEdits.setText(itemModelli.get(idItem++).getValore());

        return idItem;
    }

    public int createViewThreeChkboxesAndEdit(int idItem, int idRes)
    {
        LinearLayout three_chkboxes_and_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderThreeChkboxesAndEdit = (LinearLayout) three_chkboxes_and_edit.findViewById(R.id.llHeaderThreeChkboxesAndEdit);

        TextView tvHeaderThreeChkboxesAndEdit = (TextView) three_chkboxes_and_edit.findViewById(R.id.tvHeaderThreeChkboxesAndEdit);
        tvHeaderThreeChkboxesAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionThreeChkboxesAndEdit = (LinearLayout) three_chkboxes_and_edit.findViewById(R.id.llSectionThreeChkboxesAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderThreeChkboxesAndEdit, llSectionThreeChkboxesAndEdit));

        llHeaderSections.add(llSectionThreeChkboxesAndEdit);

        final ImageView ivArrowThreeChkboxesAndEdit = (ImageView) three_chkboxes_and_edit.findViewById(R.id.ivArrowThreeChkboxesAndEdit);

        ImageViews.put(idItem, ivArrowThreeChkboxesAndEdit);

        llHeaderThreeChkboxesAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeChkboxesAndEdit.setVisibility(llSectionThreeChkboxesAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeChkboxesAndEdit.setImageResource(llSectionThreeChkboxesAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        String valore = itemModelli.get(idItem).getValore();
        final String[] fields = valore.split("\\|\\|");

        final CheckBox chk1ThreeChkboxesAndEdit = (CheckBox) three_chkboxes_and_edit.findViewById(R.id.chk1ThreeChkboxesAndEdit);
        final CheckBox chk2ThreeChkboxesAndEdit = (CheckBox) three_chkboxes_and_edit.findViewById(R.id.chk2ThreeChkboxesAndEdit);
        final CheckBox chk3ThreeChkboxesAndEdit = (CheckBox) three_chkboxes_and_edit.findViewById(R.id.chk3ThreeChkboxesAndEdit);

        final TextView tv1ThreeChkboxesAndEdit = (TextView) three_chkboxes_and_edit.findViewById(R.id.tv1ThreeChkboxesAndEdit);
        final TextView tv2ThreeChkboxesAndEdit = (TextView) three_chkboxes_and_edit.findViewById(R.id.tv2ThreeChkboxesAndEdit);
        final TextView tv3ThreeChkboxesAndEdit = (TextView) three_chkboxes_and_edit.findViewById(R.id.tv3ThreeChkboxesAndEdit);

        ArrayList<CheckBox> al_Chkboxes = new ArrayList<>();
        al_Chkboxes.add(chk1ThreeChkboxesAndEdit);
        al_Chkboxes.add(chk2ThreeChkboxesAndEdit);
        al_Chkboxes.add(chk3ThreeChkboxesAndEdit);

        final ArrayList<TextView> al_TextViews = new ArrayList<>();
        al_TextViews.add(tv1ThreeChkboxesAndEdit);
        al_TextViews.add(tv2ThreeChkboxesAndEdit);
        al_TextViews.add(tv3ThreeChkboxesAndEdit);

        int i;

        for (i = 0; i < al_TextViews.size(); i++)
        {
            al_Chkboxes.get(i).setText(fields[i]);
            al_TextViews.get(i).setText(fields[i]);
        }
        CheckBoxes.put(idItem, al_Chkboxes);

        TextView tvEditThreeChkboxesAndEdit = (TextView) three_chkboxes_and_edit.findViewById(R.id.tvEditThreeChkboxesAndEdit);
        tvEditThreeChkboxesAndEdit.setText(fields[i]);

        final EditText et1ThreeChkboxesAndEdit = (EditText) three_chkboxes_and_edit.findViewById(R.id.et1ThreeChkboxesAndEdit);

        EditTexts.put(idItem, et1ThreeChkboxesAndEdit);

        final FrameLayout fl1ThreeChkboxesAndEdit = (FrameLayout) three_chkboxes_and_edit.findViewById(R.id.fl1ThreeChkboxesAndEdit);
        final FrameLayout fl2ThreeChkboxesAndEdit = (FrameLayout) three_chkboxes_and_edit.findViewById(R.id.fl2ThreeChkboxesAndEdit);
        final FrameLayout fl3ThreeChkboxesAndEdit = (FrameLayout) three_chkboxes_and_edit.findViewById(R.id.fl3ThreeChkboxesAndEdit);

        fl1ThreeChkboxesAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk1ThreeChkboxesAndEdit.setChecked(!chk1ThreeChkboxesAndEdit.isChecked());

                if (chk1ThreeChkboxesAndEdit.isChecked())
                {
                    et1ThreeChkboxesAndEdit.setText("");
                    et1ThreeChkboxesAndEdit.clearFocus();
                    tv1ThreeChkboxesAndEdit.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv1ThreeChkboxesAndEdit.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        fl2ThreeChkboxesAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk2ThreeChkboxesAndEdit.setChecked(!chk2ThreeChkboxesAndEdit.isChecked());

                if (chk2ThreeChkboxesAndEdit.isChecked())
                {
                    et1ThreeChkboxesAndEdit.setText("");
                    et1ThreeChkboxesAndEdit.clearFocus();
                    tv2ThreeChkboxesAndEdit.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv2ThreeChkboxesAndEdit.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        fl3ThreeChkboxesAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk3ThreeChkboxesAndEdit.setChecked(!chk3ThreeChkboxesAndEdit.isChecked());

                if (chk3ThreeChkboxesAndEdit.isChecked())
                {
                    et1ThreeChkboxesAndEdit.setText("");
                    et1ThreeChkboxesAndEdit.clearFocus();
                    tv3ThreeChkboxesAndEdit.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv3ThreeChkboxesAndEdit.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        et1ThreeChkboxesAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                chk1ThreeChkboxesAndEdit.setChecked(false);
                chk2ThreeChkboxesAndEdit.setChecked(false);
                chk3ThreeChkboxesAndEdit.setChecked(false);

                for (int i = 0; i < al_TextViews.size(); i++)
                {
                    al_TextViews.get(i).setTextColor(Color.parseColor("#ff808080"));
                }
                return false;
            }
        });

        return ++idItem;
    }

    public int createViewChkboxAndEdit(int idItem, int idRes)
    {
        LinearLayout chkbox_and_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderChkboxAndEdit = (LinearLayout) chkbox_and_edit.findViewById(R.id.llHeaderChkboxAndEdit);

        TextView tvHeaderChkboxAndEdit = (TextView) chkbox_and_edit.findViewById(R.id.tvHeaderChkboxAndEdit);
        tvHeaderChkboxAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionChkboxAndEdit = (LinearLayout) chkbox_and_edit.findViewById(R.id.llSectionChkboxAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderChkboxAndEdit, llSectionChkboxAndEdit));

        llHeaderSections.add(llSectionChkboxAndEdit);

        final ImageView ivArrowChkboxAndEdit = (ImageView) chkbox_and_edit.findViewById(R.id.ivArrowChkboxAndEdit);

        ImageViews.put(idItem, ivArrowChkboxAndEdit);

        llHeaderChkboxAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionChkboxAndEdit.setVisibility(llSectionChkboxAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowChkboxAndEdit.setImageResource(llSectionChkboxAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        String valore = itemModelli.get(idItem).getValore();
        final String[] fields = valore.split("\\|\\|");


        final CheckBox chk1ChkboxAndEdit = (CheckBox) chkbox_and_edit.findViewById(R.id.chk1ChkboxAndEdit);
        //chk1ChkboxAndEdit.setText(fields[0]);

        ArrayList<CheckBox> al_Chkboxes = new ArrayList<>();
        al_Chkboxes.add(chk1ChkboxAndEdit);

        for (int i = 0; i < al_Chkboxes.size(); i++)
        {
            al_Chkboxes.get(i).setText(fields[i]);
        }
        CheckBoxes.put(idItem, al_Chkboxes);

        final TextView tv1ChkboxAndEdit = (TextView) chkbox_and_edit.findViewById(R.id.tv1ChkboxAndEdit);
        tv1ChkboxAndEdit.setText(fields[0]);

        TextView tvEditChkboxAndEdit = (TextView) chkbox_and_edit.findViewById(R.id.tvEditChkboxAndEdit);
        tvEditChkboxAndEdit.setText(fields[1]);

        final EditText et1ChkboxAndEdit = (EditText) chkbox_and_edit.findViewById(R.id.et1ChkboxAndEdit);

        EditTexts.put(idItem, et1ChkboxAndEdit);

        final FrameLayout fl1ChkboxAndEdit = (FrameLayout) chkbox_and_edit.findViewById(R.id.fl1ChkboxAndEdit);

        fl1ChkboxAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chk1ChkboxAndEdit.setChecked(!chk1ChkboxAndEdit.isChecked());

                if (chk1ChkboxAndEdit.isChecked())
                {
                    et1ChkboxAndEdit.setText("");
                    et1ChkboxAndEdit.clearFocus();
                    tv1ChkboxAndEdit.setTextColor(Color.parseColor("#ff29b352"));
                } else
                {
                    tv1ChkboxAndEdit.setTextColor(Color.parseColor("#ff808080"));
                }
            }
        });

        et1ChkboxAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                chk1ChkboxAndEdit.setChecked(false);
                tv1ChkboxAndEdit.setTextColor(Color.parseColor("#ff808080"));
                return false;
            }
        });

        return ++idItem;
    }

    public int createViewSwitchAndEdit(int idItem, int idRes)
    {
        LinearLayout switch_and_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderSwitchAndEdit = (LinearLayout) switch_and_edit.findViewById(R.id.llHeaderSwitchAndEdit);

        TextView tvHeaderSwitchAndEdit = (TextView) switch_and_edit.findViewById(R.id.tvHeaderSwitchAndEdit);
        tvHeaderSwitchAndEdit.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionSwitchAndEdit = (LinearLayout) switch_and_edit.findViewById(R.id.llSectionSwitchAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderSwitchAndEdit, llSectionSwitchAndEdit));

        llHeaderSections.add(llSectionSwitchAndEdit);

        final ImageView ivArrowSwitchAndEdit = (ImageView) switch_and_edit.findViewById(R.id.ivArrowSwitchAndEdit);

        ImageViews.put(idItem, ivArrowSwitchAndEdit);

        llHeaderSwitchAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionSwitchAndEdit.setVisibility(llSectionSwitchAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowSwitchAndEdit.setImageResource(llSectionSwitchAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        //ArrayList<TextView> al_TextViews = new ArrayList<>();

        //TextView tv1SwitchAndEdit = (TextView) switch_and_edit.findViewById(R.id.tv1SwitchAndEdit);
        //al_TextViews.add(tv1SwitchAndEdit);

        final Switch sw1SwitchAndEdit = (Switch) switch_and_edit.findViewById(R.id.sw1SwitchAndEdit);
        sw1SwitchAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());


        Switches.put(idItem, sw1SwitchAndEdit);

/*        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1SwitchAndEdit);

        for (int i = 0; i < al_Switches.size(); i++)
        {
            Switches.put(idItem, al_Switches.get(i));

        }*/

        idItem++;
        TextView tv1SwitchAndEdit = (TextView) switch_and_edit.findViewById(R.id.tv1SwitchAndEdit);
        tv1SwitchAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final EditText et1SwitchAndEdit = (EditText) switch_and_edit.findViewById(R.id.et1SwitchAndEdit);

        final LinearLayout llEdit = (LinearLayout) switch_and_edit.findViewById(R.id.llEditSwitchAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llEdit, llEdit));

        EditTexts.put(idItem, et1SwitchAndEdit);
        tv1SwitchAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());
        idItem++;

        return idItem;
    }

    public int createViewTwoSwitches(int idItem, int idRes)
    {
        LinearLayout two_switches = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderTwoSwitches = (LinearLayout) two_switches.findViewById(R.id.llHeaderTwoSwitches);

        TextView tvHeaderTwoSwitches = (TextView) two_switches.findViewById(R.id.tvHeaderTwoSwitches);
        tvHeaderTwoSwitches.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionTwoSwitches = (LinearLayout) two_switches.findViewById(R.id.llSectionTwoSwitches);

        LinearLayouts.put(idItem, new Pair<>(llHeaderTwoSwitches, llSectionTwoSwitches));

        llHeaderSections.add(llSectionTwoSwitches);

        final ImageView ivArrowTwoSwitches = (ImageView) two_switches.findViewById(R.id.ivArrowTwoSwitches);

        ImageViews.put(idItem, ivArrowTwoSwitches);

        llHeaderTwoSwitches.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionTwoSwitches.setVisibility(llSectionTwoSwitches.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowTwoSwitches.setImageResource(llSectionTwoSwitches.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        ArrayList<TextView> al_TextViews = new ArrayList<>();

        TextView tv1TwoSwitches = (TextView) two_switches.findViewById(R.id.tv1TwoSwitches);
        al_TextViews.add(tv1TwoSwitches);

        Switch sw1TwoSwitches = (Switch) two_switches.findViewById(R.id.sw1TwoSwitches);

        TextView tv2TwoSwitches = (TextView) two_switches.findViewById(R.id.tv2TwoSwitches);
        al_TextViews.add(tv2TwoSwitches);

        final Switch sw2TwoSwitches = (Switch) two_switches.findViewById(R.id.sw2TwoSwitches);

        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1TwoSwitches);
        al_Switches.add(sw2TwoSwitches);

        Switches.put(idItem, sw2TwoSwitches);

        for (int i = 0; i < al_Switches.size(); i++)
        {
            Switches.put(idItem, al_Switches.get(i));
            al_TextViews.get(i).setText(itemModelli.get(idItem).getDescrizione_item());
            idItem++;
        }

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

        llHeaderSections.add(llSectionTwoSwitchesAndEdit);

        final ImageView ivArrowTwoSwitchesAndEdit = (ImageView) two_switches_and_edit.findViewById(R.id.ivArrowTwoSwitchesAndEdit);

        ImageViews.put(idItem, ivArrowTwoSwitchesAndEdit);

        llHeaderTwoSwitchesAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionTwoSwitchesAndEdit.setVisibility(llSectionTwoSwitchesAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowTwoSwitchesAndEdit.setImageResource(llSectionTwoSwitchesAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        ArrayList<TextView> al_TextViews = new ArrayList<>();

        TextView tv1TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv1TwoSwitchesAndEdit);
        al_TextViews.add(tv1TwoSwitchesAndEdit);

        Switch sw1TwoSwitchesAndEdit = (Switch) two_switches_and_edit.findViewById(R.id.sw1TwoSwitchesAndEdit);

        TextView tv2TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv2TwoSwitchesAndEdit);
        al_TextViews.add(tv2TwoSwitchesAndEdit);

        final Switch sw2TwoSwitchesAndEdit = (Switch) two_switches_and_edit.findViewById(R.id.sw2TwoSwitchesAndEdit);

        ArrayList<Switch> al_Switches = new ArrayList<>();
        al_Switches.add(sw1TwoSwitchesAndEdit);
        al_Switches.add(sw2TwoSwitchesAndEdit);

        Switches.put(idItem, sw2TwoSwitchesAndEdit);

        final TextView tv3TwoSwitchesAndEdit = (TextView) two_switches_and_edit.findViewById(R.id.tv3TwoSwitchesAndEdit);

        for (int i = 0; i < al_Switches.size(); i++)
        {
            Switches.put(idItem, al_Switches.get(i));
            al_TextViews.get(i).setText(itemModelli.get(idItem).getDescrizione_item());
            idItem++;
        }

        final EditText et1TwoSwitchesAndEdit = (EditText) two_switches_and_edit.findViewById(R.id.et1TwoSwitchesAndEdit);

/*        sw2TwoSwitchesAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (sw2TwoSwitchesAndEdit.isChecked())
                {
                    tv3TwoSwitchesAndEdit.setVisibility(View.VISIBLE);
                    et1TwoSwitchesAndEdit.setVisibility(View.VISIBLE);
                    et1TwoSwitchesAndEdit.setText("");
                }
                else
                {
                    tv3TwoSwitchesAndEdit.setVisibility(View.GONE);
                    et1TwoSwitchesAndEdit.setVisibility(View.GONE);
                    et1TwoSwitchesAndEdit.setText("Non applicabile");
                }
            }
        });*/

        final LinearLayout llEdit = (LinearLayout) two_switches_and_edit.findViewById(R.id.llEditTwoSwitchesAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llEdit, llEdit));

        EditTexts.put(idItem, et1TwoSwitchesAndEdit);
        tv3TwoSwitchesAndEdit.setText(itemModelli.get(idItem++).getDescrizione_item());

        return idItem;
    }


    public int createViewSwitch(int idItem, int idRes)
    {
        LinearLayout switch_ = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderSwitch = (LinearLayout) switch_.findViewById(R.id.llHeaderSwitch);

        TextView tvHeaderSwitch = (TextView) switch_.findViewById(R.id.tvHeaderSwitch);
        tvHeaderSwitch.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionSwitch = (LinearLayout) switch_.findViewById(R.id.llSectionSwitch);

        LinearLayouts.put(idItem, new Pair<>(llHeaderSwitch, llSectionSwitch));

        llHeaderSections.add(llSectionSwitch);

        final ImageView ivArrowSwitch = (ImageView) switch_.findViewById(R.id.ivArrowSwitch);

        ImageViews.put(idItem, ivArrowSwitch);

        llHeaderSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionSwitch.setVisibility(llSectionSwitch.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowSwitch.setImageResource(llSectionSwitch.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        TextView tv1Switch = (TextView) switch_.findViewById(R.id.tv1Switch);

        Switch sw1Switch = (Switch) switch_.findViewById(R.id.sw1Switch);

        Switches.put(idItem, sw1Switch);
        tv1Switch.setText(itemModelli.get(idItem++).getDescrizione_item());

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

        llHeaderSections.add(llSectionFiveSwitches);

        final ImageView ivArrowFiveSwitches = (ImageView) five_switches.findViewById(R.id.ivArrowFiveSwitches);

        ImageViews.put(idItem, ivArrowFiveSwitches);

        llHeaderFiveSwitches.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFiveSwitches.setVisibility(llSectionFiveSwitches.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFiveSwitches.setImageResource(llSectionFiveSwitches.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
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

    public int createViewFiveRadios(int idItem, int idRes)
    {
        int i;

        LinearLayout five_radios = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderFiveRadios = (LinearLayout) five_radios.findViewById(R.id.llHeaderFiveRadios);

        TextView tvHeaderFiveRadios = (TextView) five_radios.findViewById(R.id.tvHeaderFiveRadios);
        tvHeaderFiveRadios.setText(itemModelli.get(idItem).getDescrizione_item());

        final ImageView ivArrowFiveRadios = (ImageView) five_radios.findViewById(R.id.ivArrowFiveRadios);

        ImageViews.put(idItem, ivArrowFiveRadios);

        final LinearLayout llSectionFiveRadios = (LinearLayout) five_radios.findViewById(R.id.llSectionFiveRadios);

        LinearLayouts.put(idItem, new Pair<>(llHeaderFiveRadios, llSectionFiveRadios));

        llHeaderSections.add(llSectionFiveRadios);

        llHeaderFiveRadios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFiveRadios.setVisibility(llSectionFiveRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFiveRadios.setImageResource(llSectionFiveRadios.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        RadioGroup rg1FiveRadios = (RadioGroup) five_radios.findViewById(R.id.rg1FiveRadios);

        RadioGroups.put(idItem, rg1FiveRadios);

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1FiveRadios.getChildCount())
        {
            for (i = 0; i < rg1FiveRadios.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1FiveRadios.getChildAt(i);

                rb.setText(fields[i]);
            }
        }
        return ++idItem;
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

        llHeaderSections.add(llSectionFourRadios);

        llHeaderFourRadios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFourRadios.setVisibility(llSectionFourRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFourRadios.setImageResource(llSectionFourRadios.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        RadioGroup rg1FourRadios = (RadioGroup) four_radios.findViewById(R.id.rg1FourRadios);

        RadioGroups.put(idItem, rg1FourRadios);

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

    public int createViewFourRadiosAndEditObl(int idItem, int idRes)
    {
        LinearLayout four_radios_and_edit = (LinearLayout) rootView.findViewById(idRes);

        LinearLayout llHeaderFourRadiosAndEdit = (LinearLayout) four_radios_and_edit.findViewById(R.id.llHeaderFourRadiosAndEdit);

        final ImageView ivArrowFourRadiosAndEdit = (ImageView) four_radios_and_edit.findViewById(R.id.ivArrowFourRadiosAndEdit);

        ImageViews.put(idItem, ivArrowFourRadiosAndEdit);

        TextView tvHeaderFourRadiosAndEdit = (TextView) four_radios_and_edit.findViewById(R.id.tvHeaderFourRadiosAndEdit);
        tvHeaderFourRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionFourRadiosAndEdit = (LinearLayout) four_radios_and_edit.findViewById(R.id.llSectionFourRadiosAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderFourRadiosAndEdit, llSectionFourRadiosAndEdit));

        llHeaderSections.add(llSectionFourRadiosAndEdit);

        llHeaderFourRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFourRadiosAndEdit.setVisibility(llSectionFourRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFourRadiosAndEdit.setImageResource(llSectionFourRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final RadioGroup rg1FourRadiosAndEdit = (RadioGroup) four_radios_and_edit.findViewById(R.id.rg1FourRadiosAndEdit);

        RadioGroups.put(idItem, rg1FourRadiosAndEdit);

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        TextView tv1FourRadiosAndEdit = (TextView) four_radios_and_edit.findViewById(R.id.tv1FourRadiosAndEdit);

        final EditText et1FourRadiosAndEdit = (EditText) four_radios_and_edit.findViewById(R.id.et1FourRadiosAndEdit);

        final LinearLayout llEditFourRadiosAndEdit = (LinearLayout) four_radios_and_edit.findViewById(R.id.llEditFourRadiosAndEdit);

/*        et1FourRadiosAndEdit.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1FourRadiosAndEdit.clearCheck();
                return false;
            }
        });*/

        if (fields.length >= rg1FourRadiosAndEdit.getChildCount())
        {
            for (int i = 0; i < rg1FourRadiosAndEdit.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1FourRadiosAndEdit.getChildAt(i);

                rb.setText(fields[i]);

/*                rb.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(rg1FourRadiosAndEdit.getCheckedRadioButtonId() == rg1FourRadiosAndEdit.getChildAt(1).getId()) //second radio
                        {
                            llEditFourRadiosAndEdit.setVisibility(View.VISIBLE);
                            et1FourRadiosAndEdit.setText("");
                        }
                        else
                        {
                            llEditFourRadiosAndEdit.setVisibility(View.GONE);
                            et1FourRadiosAndEdit.setText("Non applicabile");
                        }
                    }
                });*/
            }
        }

        idItem++;
        LinearLayouts.put(idItem, new Pair<>(llEditFourRadiosAndEdit, llEditFourRadiosAndEdit));
        tv1FourRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());
        et1FourRadiosAndEdit.setText("Non applicabile");
        et1FourRadiosAndEdit.setHint(itemModelli.get(idItem).getUnita_misura());
        EditTexts.put(idItem, et1FourRadiosAndEdit);

        idItem += 2;

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

        llHeaderSections.add(llSectionThreeRadiosAndEdit);

        llHeaderThreeRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeRadiosAndEdit.setVisibility(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeRadiosAndEdit.setImageResource(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final RadioGroup rg1ThreeRadiosAndEdit = (RadioGroup) three_radios_and_edit.findViewById(R.id.rg1ThreeRadiosAndEdit);

        RadioGroups.put(idItem, rg1ThreeRadiosAndEdit);

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        TextView tv1ThreeRadiosAndEdit = (TextView) three_radios_and_edit.findViewById(R.id.tv1ThreeRadiosAndEdit);

        final EditText et1ThreeRadiosAndEdit = (EditText) three_radios_and_edit.findViewById(R.id.et1ThreeRadiosAndEdit);

        final LinearLayout llEditThreeRadiosAndEdit = (LinearLayout) three_radios_and_edit.findViewById(R.id.llEditThreeRadiosAndEdit);

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

/*                rb.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(rg1ThreeRadiosAndEdit.getCheckedRadioButtonId() == rg1ThreeRadiosAndEdit.getChildAt(1).getId()) //second radio
                        {
                            llEditThreeRadiosAndEdit.setVisibility(View.VISIBLE);
                            et1ThreeRadiosAndEdit.setText("");
                        }
                        else
                        {
                            llEditThreeRadiosAndEdit.setVisibility(View.GONE);
                            et1ThreeRadiosAndEdit.setText("Non applicabile");
                        }
                    }
                });*/
            }
        }

        idItem++;
        LinearLayouts.put(idItem, new Pair<>(llEditThreeRadiosAndEdit, llEditThreeRadiosAndEdit));
        tv1ThreeRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());
        et1ThreeRadiosAndEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        et1ThreeRadiosAndEdit.setText("Non applicabile");
        et1ThreeRadiosAndEdit.setHint(itemModelli.get(idItem).getUnita_misura());
        EditTexts.put(idItem, et1ThreeRadiosAndEdit);

        return ++idItem;
    }

    public int createViewThreeRadiosAndEditObl(int idItem, int idRes)
    {
        LinearLayout three_radios_and_edit = (LinearLayout) rootView.findViewById(idRes);

        LinearLayout llHeaderThreeRadiosAndEdit = (LinearLayout) three_radios_and_edit.findViewById(R.id.llHeaderThreeRadiosAndEdit);

        final ImageView ivArrowThreeRadiosAndEdit = (ImageView) three_radios_and_edit.findViewById(R.id.ivArrowThreeRadiosAndEdit);

        ImageViews.put(idItem, ivArrowThreeRadiosAndEdit);

        TextView tvHeaderThreeRadiosAndEdit = (TextView) three_radios_and_edit.findViewById(R.id.tvHeaderThreeRadiosAndEdit);
        tvHeaderThreeRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionThreeRadiosAndEdit = (LinearLayout) three_radios_and_edit.findViewById(R.id.llSectionThreeRadiosAndEdit);

        LinearLayouts.put(idItem, new Pair<>(llHeaderThreeRadiosAndEdit, llSectionThreeRadiosAndEdit));

        llHeaderSections.add(llSectionThreeRadiosAndEdit);

        llHeaderThreeRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeRadiosAndEdit.setVisibility(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeRadiosAndEdit.setImageResource(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final RadioGroup rg1ThreeRadiosAndEdit = (RadioGroup) three_radios_and_edit.findViewById(R.id.rg1ThreeRadiosAndEdit);

        RadioGroups.put(idItem, rg1ThreeRadiosAndEdit);

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

        idItem++;
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

        tv1ThreeRadiosAndEdit.setText(itemModelli.get(idItem).getDescrizione_item());
        et1ThreeRadiosAndEdit.setHint(itemModelli.get(idItem).getUnita_misura());
        EditTexts.put(idItem, et1ThreeRadiosAndEdit);
        //et1ThreeRadiosAndEdit.setBackgroundColor(Color.RED);
        //et1ThreeRadiosAndEdit.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

/*        rg1ThreeRadiosAndEdit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                et1ThreeRadiosAndEdit.setText("");
            }
        });*/

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

        llHeaderSections.add(llSectionThreeRadiosAndEdit);

        llHeaderThreeRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeRadiosAndEdit.setVisibility(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeRadiosAndEdit.setImageResource(llSectionThreeRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final RadioGroup rg1ThreeRadiosAndEdit = (RadioGroup) three_radios_and_edit.findViewById(R.id.rg1ThreeRadiosAndEdit);

        RadioGroups.put(idItem, rg1ThreeRadiosAndEdit);

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

        final EditText et1ThreeRadiosAndEdit = (EditText) three_radios_and_edit.findViewById(R.id.et1ThreeRadiosAndEdit);
        et1ThreeRadiosAndEdit.setBackgroundColor(Color.TRANSPARENT);

        EditTexts.put(idItem, et1ThreeRadiosAndEdit);

        rg1ThreeRadiosAndEdit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
            {
                if (checkedId != -1)
                {
                    et1ThreeRadiosAndEdit.setText("");
                }
            }
        });

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

    public int createViewTwoRadiosAndSwitch(int idItem, int idRes)
    {
        LinearLayout two_radios_and_switch = (LinearLayout) rootView.findViewById(idRes);

        LinearLayout llHeaderTwoRadiosAndSwitch = (LinearLayout) two_radios_and_switch.findViewById(R.id.llHeaderTwoRadiosAndSwitch);

        final ImageView ivArrowTwoRadiosAndSwitch = (ImageView) two_radios_and_switch.findViewById(R.id.ivArrowTwoRadiosAndSwitch);

        ImageViews.put(idItem, ivArrowTwoRadiosAndSwitch);

        TextView tvHeaderTwoRadiosAndSwitch = (TextView) two_radios_and_switch.findViewById(R.id.tvHeaderTwoRadiosAndSwitch);
        tvHeaderTwoRadiosAndSwitch.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionTwoRadiosAndSwitch = (LinearLayout) two_radios_and_switch.findViewById(R.id.llSectionTwoRadiosAndSwitch);

        LinearLayouts.put(idItem, new Pair<>(llHeaderTwoRadiosAndSwitch, llSectionTwoRadiosAndSwitch));

        llHeaderSections.add(llSectionTwoRadiosAndSwitch);

        llHeaderTwoRadiosAndSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionTwoRadiosAndSwitch.setVisibility(llSectionTwoRadiosAndSwitch.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowTwoRadiosAndSwitch.setImageResource(llSectionTwoRadiosAndSwitch.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final RadioGroup rg1TwoRadiosAndSwitch = (RadioGroup) two_radios_and_switch.findViewById(R.id.rg1TwoRadiosAndSwitch);

        RadioGroups.put(idItem, rg1TwoRadiosAndSwitch);

        String valore = itemModelli.get(idItem).getValore();
        String[] fields = valore.split("\\|\\|");

        if (fields.length >= rg1TwoRadiosAndSwitch.getChildCount())
        {
            for (int i = 0; i < rg1TwoRadiosAndSwitch.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rg1TwoRadiosAndSwitch.getChildAt(i);

                rb.setText(fields[i]);
            }
        }

        idItem++;

        TextView tv1TwoRadiosAndSwitch = (TextView) two_radios_and_switch.findViewById(R.id.tv1TwoRadiosAndSwitch);
        tv1TwoRadiosAndSwitch.setText(itemModelli.get(idItem).getDescrizione_item());

        final Switch sw1TwoRadiosAndSwitch = (Switch) two_radios_and_switch.findViewById(R.id.sw1TwoRadiosAndSwitch);

        Switches.put(idItem, sw1TwoRadiosAndSwitch);

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

        llHeaderSections.add(llSectionTwoRadiosAndEdit);

        llHeaderTwoRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionTwoRadiosAndEdit.setVisibility(llSectionTwoRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowTwoRadiosAndEdit.setImageResource(llSectionTwoRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final RadioGroup rg1TwoRadiosAndEdit = (RadioGroup) two_radios_and_edit.findViewById(R.id.rg1TwoRadiosAndEdit);

        RadioGroups.put(idItem, rg1TwoRadiosAndEdit);

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

        final EditText et1TwoRadiosAndEdit = (EditText) two_radios_and_edit.findViewById(R.id.et1TwoRadiosAndEdit);

        EditTexts.put(idItem, et1TwoRadiosAndEdit);

        rg1TwoRadiosAndEdit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
            {
                if (checkedId != -1)
                {
                    et1TwoRadiosAndEdit.setText("");
                }
            }
        });

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

        llHeaderSections.add(llSectionThreeRadios);

        llHeaderThreeRadios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeRadios.setVisibility(llSectionThreeRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeRadios.setImageResource(llSectionThreeRadios.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        RadioGroup rg1ThreeRadios = (RadioGroup) ll_three_radios.findViewById(R.id.rg1ThreeRadios);

        RadioGroups.put(idItem, rg1ThreeRadios);

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
        return ++idItem;
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

        llHeaderSections.add(llSectionTwoRadios);

        llHeaderTwoRadios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionTwoRadios.setVisibility(llSectionTwoRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowTwoRadios.setImageResource(llSectionTwoRadios.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        RadioGroup rg1TwoRadios = (RadioGroup) ll_two_radios.findViewById(R.id.rg1TwoRadios);

        RadioGroups.put(idItem, rg1TwoRadios);

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
        return ++idItem;
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

        llHeaderSections.add(llSectionFourRadiosAndTwoEdits);

        llHeaderFourRadiosAndTwoEdits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFourRadiosAndTwoEdits.setVisibility(llSectionFourRadiosAndTwoEdits.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFourRadiosAndTwoEdits.setImageResource(llSectionFourRadiosAndTwoEdits.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final RadioGroup rg1FourRadiosAndTwoEdits = (RadioGroup) four_radios_and_two_edits.findViewById(R.id.rg1FourRadiosAndTwoEdits);

        RadioGroups.put(idItem, rg1FourRadiosAndTwoEdits);

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

        final EditText et1FourRadiosAndTwoEdits = (EditText) four_radios_and_two_edits.findViewById(R.id.et1FourRadiosAndTwoEdits);
        EditTexts.put(idItem, et1FourRadiosAndTwoEdits);

        rg1FourRadiosAndTwoEdits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
            {
                if (checkedId != -1)
                {
                    et1FourRadiosAndTwoEdits.setText("");
                }
            }
        });

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
        et2FourRadiosAndTwoEdits.setHint(itemModelli.get(idItem).getUnita_misura());
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

        llHeaderSections.add(llSectionFourRadiosAndEdit);

        llHeaderFourRadiosAndEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionFourRadiosAndEdit.setVisibility(llSectionFourRadiosAndEdit.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowFourRadiosAndEdit.setImageResource(llSectionFourRadiosAndEdit.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        final RadioGroup rg1FourRadiosAndEdit = (RadioGroup) four_radios_and_edit.findViewById(R.id.rg1FourRadiosAndEdit);

        RadioGroups.put(idItem, rg1FourRadiosAndEdit);

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

        final EditText et1FourRadiosAndEdit = (EditText) four_radios_and_edit.findViewById(R.id.et1FourRadiosAndEdit);

/*        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1FourRadiosAndEdit);*/
        EditTexts.put(idItem, et1FourRadiosAndEdit);

        rg1FourRadiosAndEdit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
            {
                if (checkedId != -1)
                {
                    et1FourRadiosAndEdit.setText("");
                }
            }
        });

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

    public int createViewTwoEdits(int idItem, int idRes)
    {
        LinearLayout two_edits = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderTwoEdits = (LinearLayout) two_edits.findViewById(R.id.llHeaderTwoEdits);

        TextView tvHeaderTwoEdits = (TextView) two_edits.findViewById(R.id.tvHeaderTwoEdits);
        tvHeaderTwoEdits.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionTwoEdits = (LinearLayout) two_edits.findViewById(R.id.llSectionTwoEdits);

        LinearLayouts.put(idItem, new Pair<>(llHeaderTwoEdits, llSectionTwoEdits));

        llHeaderSections.add(llSectionTwoEdits);

        final ImageView ivArrowTwoEdits = (ImageView) two_edits.findViewById(R.id.ivArrowTwoEdits);

        ImageViews.put(idItem, ivArrowTwoEdits);

        llHeaderTwoEdits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionTwoEdits.setVisibility(llSectionTwoEdits.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowTwoEdits.setImageResource(llSectionTwoEdits.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        ArrayList<TextView> al_tvs = new ArrayList<>();

        TextView tv1TwoEdits = (TextView) two_edits.findViewById(R.id.tv1TwoEdits);
        EditText et1TwoEdits = (EditText) two_edits.findViewById(R.id.et1TwoEdits);
        TextView tv2TwoEdits = (TextView) two_edits.findViewById(R.id.tv2TwoEdits);
        EditText et2TwoEdits = (EditText) two_edits.findViewById(R.id.et2TwoEdits);

        al_tvs.add(tv1TwoEdits);
        al_tvs.add(tv2TwoEdits);

        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1TwoEdits);
        al_Edits.add(et2TwoEdits);

        for (int i = 0; i < al_tvs.size(); i++)
        {
            EditTexts.put(idItem, al_Edits.get(i));
            al_tvs.get(i).setText(itemModelli.get(idItem).getDescrizione_item());
            al_Edits.get(i).setHint(itemModelli.get(idItem).getUnita_misura());
            idItem++;
        }

        return idItem;
    }

    public int createViewThreeEdits(int idItem, int idRes)
    {
        LinearLayout three_edits = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderThreeEdits = (LinearLayout) three_edits.findViewById(R.id.llHeaderThreeEdits);

        TextView tvHeaderThreeEdits = (TextView) three_edits.findViewById(R.id.tvHeaderThreeEdits);
        tvHeaderThreeEdits.setText(itemModelli.get(idItem).getValore());

        final LinearLayout llSectionThreeEdits = (LinearLayout) three_edits.findViewById(R.id.llSectionThreeEdits);

        LinearLayouts.put(idItem, new Pair<>(llHeaderThreeEdits, llSectionThreeEdits));

        llHeaderSections.add(llSectionThreeEdits);

        final ImageView ivArrowThreeEdits = (ImageView) three_edits.findViewById(R.id.ivArrowThreeEdits);

        ImageViews.put(idItem, ivArrowThreeEdits);

        llHeaderThreeEdits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                llSectionThreeEdits.setVisibility(llSectionThreeEdits.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivArrowThreeEdits.setImageResource(llSectionThreeEdits.getVisibility() == View.VISIBLE
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
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

        for (int i = 0; i < al_tvs.size(); i++)
        {
            EditTexts.put(idItem, al_Edits.get(i));
            al_tvs.get(i).setText(itemModelli.get(idItem).getDescrizione_item());
            al_Edits.get(i).setHint(itemModelli.get(idItem).getUnita_misura());
            idItem++;
        }

        return idItem;
    }

    public int createViewEdit(int idItem, int idRes)
    {
        LinearLayout ll_edit = (LinearLayout) rootView.findViewById(idRes);
        LinearLayout llHeaderEdit = (LinearLayout) ll_edit.findViewById(R.id.llHeaderEdit);

        TextView tvHeaderEdit = (TextView) ll_edit.findViewById(R.id.tvHeaderEdit);
        tvHeaderEdit.setText(itemModelli.get(idItem).getDescrizione_item());

        final LinearLayout llSectionEdit = (LinearLayout) ll_edit.findViewById(R.id.llSectionEdit);

        llHeaderSections.add(llSectionEdit);

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
                        ? R.drawable.report_green_up : R.drawable.report_green_down);
            }
        });

        EditText et1Edit = (EditText) ll_edit.findViewById(R.id.et1Edit);
        et1Edit.setHint(itemModelli.get(idItem).getUnita_misura());

/*        TextView tv1EditUnit = (TextView) ll_edit.findViewById(R.id.tv1EditUnit);
        tv1EditUnit.setText(itemModelli.get(idItem).getUnita_misura());*/

/*        ArrayList<EditText> al_Edits = new ArrayList<>();
        al_Edits.add(et1Edit);*/
        EditTexts.put(idItem, et1Edit);

        return ++idItem;
    }

    public void createViewSectionHeader(int idRes)
    {
        LinearLayout header = (LinearLayout) rootView.findViewById(idRes);
        FrameLayout flSectionHeader = (FrameLayout) header.findViewById(R.id.flSectionHeader);
        final ImageView ivArrowSectionHeader = (ImageView) header.findViewById(R.id.ivArrowSectionHeader);
        final int curHeader = headerNumber;

        if (geaSezioniModelli == null)
        {
            return;
        }
        realm.beginTransaction();
        final List<GeaItemModelliRapporto> itemModelli = realm.where(GeaItemModelliRapporto.class)
                .between("id_sezione", geaSezioniModelli.get(0).getId_sezione(), geaSezioniModelli.get(geaSezioniModelli.size() - 1).getId_sezione())
                .equalTo("ordine", geaSezioniModelli.get(headerNumber).getOrdine()).findAll();
        realm.commitTransaction();


        flSectionHeader.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                for (LinearLayout ll : al_llHeaderSections.get(curHeader))
                {
                    ll.setVisibility(!allSectionsCollapsed[curHeader] ? View.GONE : View.VISIBLE);

                    if(allSectionsCollapsed[curHeader])
                    {
                        ll.getParent().requestChildFocus(ll, ll);
                    }
                }

                ivArrowSectionHeader.setImageResource(allSectionsCollapsed[curHeader]
                        ? R.drawable.report_white_up : R.drawable.report_white_down);

                for (int i = 0; i < itemModelli.size(); i++)
                {
                    int idItem = itemModelli.get(i).getId_item_modello();
                    ImageView ivSection = ImageViews.get(idItem);

                    if (ivSection != null)
                    {
                        ivSection.setImageResource(allSectionsCollapsed[curHeader]
                                ? R.drawable.report_green_up : R.drawable.report_green_down);
                    }
                }

                allSectionsCollapsed[curHeader] = !allSectionsCollapsed[curHeader];
            }
        });

        TextView tvSectionHeader = (TextView) header.findViewById(R.id.tvSectionHeader);
        tvSectionHeader.setText((headerNumber + 1) + ". " + geaSezioniModelli.get(headerNumber).getDescrizione_sezione());

        ArrayList<LinearLayout> llSecHeaders = new ArrayList<>();
        llSecHeaders.addAll(llHeaderSections);
        al_llHeaderSections.add(llSecHeaders);
        llHeaderSections.clear();

        headerNumber++;
    }

    public void markSectionsWithNotFilledItems(int id_sopralluogo, int id_rapporto_sopralluogo)
    {
        //int completionState = DatabaseUtils.getReportInitializationState(id_sopralluogo, id_rapporto_sopralluogo);
        ArrayList<Integer> notSetItems = DatabaseUtils.getNotSetItems(id_sopralluogo, id_rapporto_sopralluogo);

/*        for (Map.Entry entry : Switches.entrySet())
        {
            int key = (int) entry.getKey();
            Pair <LinearLayout, LinearLayout> pairLL = LinearLayouts.get(key);

            if(pairLL != null)
            {
                notSetItems.add((Integer) entry.getKey());
            }
        }*/

        for (int k = 0; k < headerNumber; k++) // for each section separately
        {
            for (LinearLayout ll : al_llHeaderSections.get(k))
            {
                if (notSetItems != null) //  && completionState >= ReportStates.REPORT_ALMOST_COMPLETED
                {
                    int i;
                    for (i = 0; i < notSetItems.size(); i++)
                    {
                        LinearLayout llSection = null;
                        Pair<LinearLayout, LinearLayout> llSections = null;
                        int idItem = notSetItems.get(i);

                        do
                        {
                            llSections = getLinearLayouts().get(idItem);

                            if (llSections != null)
                            {
                                LinearLayout llSection1 = llSections.second;
                                LinearLayout llSection0 = llSections.first;

                                if (!llSection1.equals(llSection0)) // only section layouts have both layouts identical
                                {
                                    llSection = llSection1;
                                    break;
                                }
                            }
                            idItem--; // go to previous (parent) item

                        } while (idItem != 0);

/*                        if(llSections != null)
                        {
                            LinearLayout llSection1 = llSections.second;
                            LinearLayout llSection0 = llSections.first;

                            if (llSection1.equals(llSection0))
                            {
                                Pair<LinearLayout, LinearLayout> llpreviousSections = getLinearLayouts().get(idItem);

                                if (llpreviousSections != null)
                                {
                                    llSection = llpreviousSections.second;
                                }
                            } else
                            {
                                llSection = llSection1;
                            }*/

                        if (ll.equals(llSection))
                        {
                            //ll.setVisibility(View.VISIBLE);
                            ll.setBackgroundResource(R.drawable.shape_red_border_rect);
                            break;
                        }
                        //}
                    }

/*                    if (i == notSetItems.size())
                    {
                        //ll.setVisibility(View.GONE);
                        ll.setBackgroundResource(R.drawable.shape_transparent_border_rect);
                    }*/
                } /*else
                {
                    ll.setBackgroundResource(R.drawable.shape_transparent_border_rect);
                    //ll.setVisibility(View.VISIBLE);
                }*/
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int saveSeveralRadiosAndEdit(int idItem)
    {
        String str_id_item = "";
        RadioGroup rg = RadioGroups.get(idItem);

        int checkedBtnId = rg.getCheckedRadioButtonId();

        if (checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rg.findViewById(checkedBtnId);
            //str_Id_item = radioButton.getVisibility() == View.VISIBLE ? radioButton.getText().toString() : "Not applicabile";
            str_id_item = radioButton.getText().toString();
        } else
        {
            EditText et = EditTexts.get(idItem);
            str_id_item = et.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(l_geaItemRapporto, idItem, str_id_item);

        return ++idItem;
    }

    public int saveSeveralRadios(int idItem)
    {
        String str_id_item = "";
        RadioGroup rg = RadioGroups.get(idItem);

        LinearLayout ll = LinearLayouts.get(idItem).first;

        if (ll.isEnabled() == false)
        {
            str_id_item = "Non applicabile";
        } else
        {
            int checkedBtnId = rg.getCheckedRadioButtonId();

            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rg.findViewById(checkedBtnId);
                //str_Id_item = radioButton.getVisibility() == View.VISIBLE ? radioButton.getText().toString() : "Not applicabile";
                str_id_item = radioButton.getText().toString();
            }
        }
        DatabaseUtils.insertStringInReportItem(l_geaItemRapporto, idItem, str_id_item);

        return ++idItem;
    }

    public int saveSeveralChkboxesAndEdit(int idItem)
    {
        ArrayList<CheckBox> alChks = CheckBoxes.get(idItem);
        String str_Id_item = "";
        boolean checkedChkboxes = false;

        for (int i = 0; i < alChks.size(); i++)
        {
            if (alChks.get(i).isChecked())
            {
                checkedChkboxes = true;
            }
            str_Id_item += alChks.get(i).isChecked() ? alChks.get(i).getText().toString() + "||" : "";
        }

        if (!checkedChkboxes)
        {
            EditText et = EditTexts.get(idItem);
            str_Id_item = et.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(l_geaItemRapporto, idItem, str_Id_item);

        return ++idItem;
    }

    public int saveChkboxAndEdit(int idItem)
    {
        CheckBox chk = CheckBoxes.get(idItem).get(0);
        String str_Id_item;

        if (!chk.isChecked())
        {
            EditText et = EditTexts.get(idItem);
            str_Id_item = et.getText().toString();
        } else
        {
            str_Id_item = chk.getText().toString();
        }
        DatabaseUtils.insertStringInReportItem(l_geaItemRapporto, idItem, str_Id_item);

        return ++idItem;
    }

    public int saveSeveralChkboxes(int idItem)
    {
        ArrayList<CheckBox> alChks = CheckBoxes.get(idItem);
        String str_Id_item = "";

        LinearLayout ll = LinearLayouts.get(idItem).first;

        if (!ll.isEnabled())
        {
            str_Id_item = "Non applicabile";
        } else
        {
            for (int i = 0; i < alChks.size(); i++)
            {
                str_Id_item += alChks.get(i).isChecked() ? alChks.get(i).getText().toString() + "||" : "";
            }
        }
        DatabaseUtils.insertStringInReportItem(l_geaItemRapporto, idItem, str_Id_item);

        return ++idItem;
    }

    public int saveSeveralSwitches(int idItem, int quant)
    {
        String str_Id_item = "";

        for (int i = 0; i < quant; i++)
        {
            Switch sw = Switches.get(idItem);
            str_Id_item = sw.isChecked() ? "SI" : "NO";

            if (sw.isEnabled() == false)
            {
                str_Id_item = "NO";
            }

            DatabaseUtils.insertStringInReportItem(l_geaItemRapporto, idItem, str_Id_item);
            idItem++;
        }

        return idItem;
    }

    public int saveSeveralEdits(int idItem, int quant)
    {
        String str_id_item = "";
        boolean llDisabled = false;
        LinearLayout ll = null;
        Pair<LinearLayout, LinearLayout> llPair = LinearLayouts.get(idItem);
        if (llPair != null)
        {
            ll = llPair.first;
        }

        if (ll != null && !ll.isEnabled())
        {
            llDisabled = true;
        }

        for (int i = 0; i < quant; i++)
        {
            if (llDisabled)
            {
                str_id_item = "Non applicabile";
            } else
            {
                EditText et = EditTexts.get(idItem);
                //str_id_item = et.getVisibility() == View.VISIBLE ? et.getText().toString() : "Not applicabile";

/*                String unit = itemModelli.get(idItem).getUnita_misura();

                if(unit.equals("#") || unit.length() > 2)
                {
                    unit = "";
                }*/

                str_id_item = et.getText().toString(); // + unit;
            }
            DatabaseUtils.insertStringInReportItem(l_geaItemRapporto, idItem, str_id_item);
            idItem++;
        }

        return idItem;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public int fillSeveralRadiosAndEdit(int idItem)
    {
        int i;
        String strId_item = DatabaseUtils.getValueFromReportItem(l_geaItemRapporto, idItem);

        RadioGroup radiogroup = RadioGroups.get(idItem);

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
                EditText et = EditTexts.get(idItem);
                et.setText(strId_item);
            }
        }
        return ++idItem;
    }

    public int fillSeveralRadios(int idItem)
    {
        int i;
        String strId_item = DatabaseUtils.getValueFromReportItem(l_geaItemRapporto, idItem);

        RadioGroup radiogroup = RadioGroups.get(idItem);

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
            String str_id_item = DatabaseUtils.getValueFromReportItem(l_geaItemRapporto, idItem);
            EditTexts.get(idItem).setText(str_id_item);
            idItem++;
        }

        return idItem;
    }

    public int fillSeveralSwitches(int idItem, int quant)
    {
        for (int i = 0; i < quant; i++)
        {
            String str_id_item = DatabaseUtils.getValueFromReportItem(l_geaItemRapporto, idItem);
            Switch sw = Switches.get(idItem);

            if (str_id_item.equals("SI"))
            {
                sw.setChecked(true);
            }
            idItem++;
        }

        return idItem;
    }

    public int fillSeveralChkboxes(int idItem)
    {
        String str_id_item = DatabaseUtils.getValueFromReportItem(l_geaItemRapporto, idItem);

        String[] strChkArray = str_id_item.split("\\|\\|");

        ArrayList<CheckBox> al_Chks = CheckBoxes.get(idItem);

        for (CheckBox chkBox : al_Chks)
        {
            String chkBoxText = chkBox.getText().toString();

            for (int i = 0; i < strChkArray.length; i++)
            {
                if (chkBoxText.length() != 0 && strChkArray[i].contains(chkBoxText))
                {
                    chkBox.setChecked(true);
                    break;
                } else
                {
                    chkBox.setChecked(false);
                }
            }
        }

        return ++idItem;
    }

    public int fillChkboxAndEdit(int idItem)
    {
        String str_id_item = DatabaseUtils.getValueFromReportItem(l_geaItemRapporto, idItem);

        CheckBox chkBox = CheckBoxes.get(idItem).get(0);
        EditText et = EditTexts.get(idItem);
        String chkBoxText = chkBox.getText().toString();

        if (chkBoxText.length() != 0 && str_id_item.contains(chkBoxText))
        {
            chkBox.setChecked(true);
        } else
        {
            chkBox.setChecked(false);
            et.setText(str_id_item);
        }

        return ++idItem;
    }

    public int fillSeveralChkboxesAndEdit(int idItem)
    {
        String str_id_item = DatabaseUtils.getValueFromReportItem(l_geaItemRapporto, idItem);

        String[] strChkArray = str_id_item.split("\\|\\|");

        boolean checkedChkboxes = false;

        ArrayList<CheckBox> al_Chks = CheckBoxes.get(idItem);

        for (CheckBox chkBox : al_Chks)
        {
            for (int i = 0; i < strChkArray.length; i++)
            {
                String chkBoxText = chkBox.getText().toString();
                if (chkBoxText.length() != 0 && str_id_item.contains(chkBox.getText()))
                {
                    chkBox.setChecked(true);
                    checkedChkboxes = true;
                } else
                {
                    chkBox.setChecked(false);
                }
            }
        }

        if (!checkedChkboxes)
        {
            EditText et = EditTexts.get(idItem);
            et.setText(str_id_item);
        }

        return ++idItem;
    }

/*    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += (view.getMeasuredHeight());
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1) + 20);
        listView.setLayoutParams(params);
    }*/

/*    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null)
            {
                // This next line is needed before you call measure or else you won't get measured height at all.
                // The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }*/

    public static void setGridViewHeight(GridView gridView)
    {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null)
        {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > 3)
        {
            x = items / 3;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

}
