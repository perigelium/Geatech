package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.Clima1Model;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.visitItems;

public class Clima1ReportFragment extends Fragment implements View.OnTouchListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int selectedIndex;
    View rootView;
    private Communicator mCommunicator;

    private OnFragmentInteractionListener mListener;
    private Button sendReport;
    Context context;
    AutoCompleteTextView atvTipoDiEdificio, atvPosizionamentoUnitaEsterna,
            atvTipologiaCostruttivaMurature, atvLocaliEOPianiDelledificio;

    EditText etNoteSulLuoghoDiInstallazione, etNoteSulTipologiaDellImpianto, etNoteRelativeAlCollegamento;

    Clima1Model clima1Model;

    private static final String[] TipiDiEdificie = new String[] {
            "Appartamento", "Villa(Singola/Multi)", "Negozio", "Altro"};

    private static final String[] PosizionamentiUnitaEsterna = new String[] {
            "A Parete", "A Pavimento"};

    private static final String[] TipologieCostruttiveMurature = new String[] {
            "Cemento Armato", "Mattoni Pieni", "Mattoni Forati", "Pietra", "Altro"};

    private static final String[] LocaliEOPianiDelledificio = new String[] {
            "Interrato", "Piano rialzato", "Piano Terra", "Altro"};

    public Clima1ReportFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SendReportFragment newInstance(String param1, String param2)
    {
        SendReportFragment fragment = new SendReportFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator)getActivity();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        realm.beginTransaction();

        VisitItem visitItem = visitItems.get(selectedIndex);
        VisitStates visitStates = visitItem.getVisitStates();
        int idSopralluogo = visitStates.getIdSopralluogo();
        ReportStates reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        //clima1Model = realm.where(Clima1Model.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        RealmResults<Clima1Model> clima1Models = realm.where(Clima1Model.class).findAll();

        if (reportStates != null && clima1Model!=null)
        {
            clima1Model.setTipoDiEdificio(atvTipoDiEdificio.getText().toString());
            clima1Model.setPosizionamentoUnitaEsterna(atvPosizionamentoUnitaEsterna.getText().toString());
            clima1Model.setTipologiaCostruttivaMurature(atvTipologiaCostruttivaMurature.getText().toString());
            clima1Model.setLocaliEOPianiDelledificio(atvLocaliEOPianiDelledificio.getText().toString());

            clima1Model.setNoteSulLuoghoDiInstallazione(etNoteSulLuoghoDiInstallazione.getText().toString());
            clima1Model.setNoteSulTipologiaDellImpianto(etNoteSulTipologiaDellImpianto.getText().toString());
            clima1Model.setNoteRelativeAlCollegamento(etNoteRelativeAlCollegamento.getText().toString());

            if (clima1Model.getTipoDiEdificio().length() != 0)
            {
                reportStates.setReportCompletionState(1);

                if (clima1Model.getNoteRelativeAlCollegamento().length() != 0)
                {
                    reportStates.setReportCompletionState(2);
                }
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

        if (clima1Model != null)
        {
                atvTipoDiEdificio.setText(clima1Model.getTipoDiEdificio());
                atvPosizionamentoUnitaEsterna.setText((clima1Model.getPosizionamentoUnitaEsterna()));
                atvTipologiaCostruttivaMurature.setText(clima1Model.getTipologiaCostruttivaMurature());
                atvLocaliEOPianiDelledificio.setText(clima1Model.getLocaliEOPianiDelledificio());
                etNoteSulLuoghoDiInstallazione.setText(clima1Model.getNoteSulLuoghoDiInstallazione());
                etNoteSulTipologiaDellImpianto.setText(clima1Model.getNoteSulTipologiaDellImpianto());
                etNoteRelativeAlCollegamento.setText(clima1Model.getNoteRelativeAlCollegamento());
        }
        realm.commitTransaction();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView =  inflater.inflate(R.layout.climatizzazione0_report, container, false);

        ArrayAdapter<String> TipiDiEdificieAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, TipiDiEdificie);

        ArrayAdapter<String> PosizionamentiUnitaEsternaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, PosizionamentiUnitaEsterna);

        ArrayAdapter<String> TipologieCostruttiveMuratureAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, TipologieCostruttiveMurature);

        ArrayAdapter<String> LocaliEOPianiDelledificioAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, LocaliEOPianiDelledificio);


        realm.beginTransaction();

        VisitItem visitItem = visitItems.get(selectedIndex);
        VisitStates visitStates = visitItem.getVisitStates();
        int idSopralluogo = visitStates.getIdSopralluogo();

        ReportStates reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        clima1Model = realm.where(Clima1Model.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        RealmResults <Clima1Model> clima1Models = realm.where(Clima1Model.class).findAll();

        if (reportStates != null)
        {
            if (clima1Model == null)
            {
                clima1Model = new Clima1Model(clima1Models.size());
                clima1Model.setIdSopralluogo(idSopralluogo);
                realm.copyToRealmOrUpdate(clima1Model);
            }
        }
        realm.commitTransaction();

        atvTipoDiEdificio = (AutoCompleteTextView) rootView.findViewById(R.id.atvTipoDiEdificio);
        atvTipoDiEdificio.setText(clima1Model.getTipoDiEdificio());
        atvTipoDiEdificio.setAdapter(TipiDiEdificieAdapter);

        atvTipoDiEdificio.setOnTouchListener(this);
        atvTipoDiEdificio.setInputType(InputType.TYPE_NULL);

        atvTipoDiEdificio.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                clima1Model.setTipoDiEdificio(selectedItem);

                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        atvPosizionamentoUnitaEsterna = (AutoCompleteTextView) rootView.findViewById(R.id.atvPosizionamentoUnitaEsterna);
        atvPosizionamentoUnitaEsterna.setText(clima1Model.getPosizionamentoUnitaEsterna());
        atvPosizionamentoUnitaEsterna.setAdapter(PosizionamentiUnitaEsternaAdapter);
        atvPosizionamentoUnitaEsterna.setOnTouchListener(this);
        atvPosizionamentoUnitaEsterna.setInputType(InputType.TYPE_NULL);

        atvPosizionamentoUnitaEsterna.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                clima1Model.setPosizionamentoUnitaEsterna(selectedItem);
                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        atvTipologiaCostruttivaMurature = (AutoCompleteTextView) rootView.findViewById(R.id.atvTipologiaCostruttivaMurature);
        atvTipologiaCostruttivaMurature.setText(clima1Model.getTipologiaCostruttivaMurature());
        atvTipologiaCostruttivaMurature.setAdapter(TipologieCostruttiveMuratureAdapter);
        atvTipologiaCostruttivaMurature.setOnTouchListener(this);
        atvTipologiaCostruttivaMurature.setInputType(InputType.TYPE_NULL);

        atvTipologiaCostruttivaMurature.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                clima1Model.setTipologiaCostruttivaMurature(selectedItem);
                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        atvLocaliEOPianiDelledificio = (AutoCompleteTextView) rootView.findViewById(R.id.atvLocaliEOPianiDelledificio);
        atvLocaliEOPianiDelledificio.setText(clima1Model.getLocaliEOPianiDelledificio());
        atvLocaliEOPianiDelledificio.setAdapter(LocaliEOPianiDelledificioAdapter);
        atvLocaliEOPianiDelledificio.setOnTouchListener(this);
        atvLocaliEOPianiDelledificio.setInputType(InputType.TYPE_NULL);

        atvLocaliEOPianiDelledificio.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                clima1Model.setLocaliEOPianiDelledificio(selectedItem);
                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        etNoteSulLuoghoDiInstallazione = (EditText) rootView.findViewById(R.id.etNoteSulLuoghoDiInstallazione);

        etNoteSulLuoghoDiInstallazione.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    clima1Model.setNoteSulLuoghoDiInstallazione(etNoteSulLuoghoDiInstallazione.getText().toString());
                    //Toast.makeText(context, "onFocusChange etNoteSulLuoghoDiInstallazione", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etNoteSulTipologiaDellImpianto = (EditText) rootView.findViewById(R.id.etNoteSulTipologiaDellImpianto);

        etNoteSulTipologiaDellImpianto.setText(clima1Model.getNoteSulTipologiaDellImpianto());

        etNoteSulTipologiaDellImpianto.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (v == etNoteSulTipologiaDellImpianto && !hasFocus)
                {
                    clima1Model.setNoteSulTipologiaDellImpianto(etNoteSulTipologiaDellImpianto.getText().toString());
                    //Toast.makeText(context, "onFocusChange etNoteSulTipologiaDellImpianto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etNoteRelativeAlCollegamento = (EditText) rootView.findViewById(R.id.etNoteRelativeAlCollegamento);
        etNoteRelativeAlCollegamento.setText(clima1Model.getNoteRelativeAlCollegamento());

        etNoteRelativeAlCollegamento.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (v == etNoteRelativeAlCollegamento && !hasFocus)
                {
                    clima1Model.setNoteRelativeAlCollegamento(etNoteRelativeAlCollegamento.getText().toString());
                    //Toast.makeText(context, "onFocusChange etNoteRelativeAlCollegamento", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if(view == atvTipoDiEdificio)
        {
            atvTipoDiEdificio.showDropDown();
        }

        if(view == atvPosizionamentoUnitaEsterna)
        {
            atvPosizionamentoUnitaEsterna.showDropDown();
        }

        if(view == atvTipologiaCostruttivaMurature)
        {
            atvTipologiaCostruttivaMurature.showDropDown();
        }

        if(view == atvLocaliEOPianiDelledificio)
        {
            atvLocaliEOPianiDelledificio.showDropDown();
        }

        return false;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
