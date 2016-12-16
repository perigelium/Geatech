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
import android.widget.Toast;

import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ClimatizzazioneModel;
import ru.alexangan.developer.geatech.R;

public class ComposeReportTemplateFragment extends Fragment implements View.OnTouchListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    private Communicator mCommunicator;

    private OnFragmentInteractionListener mListener;
    private Button sendReport;
    Context context;
    AutoCompleteTextView atvTipoDiEdificio, atvPosizionamentoUnitaEsterna,
            atvTipologiaCostruttivaMurature, atvLocaliEOPianiDelledificio;

    ClimatizzazioneModel climatizzazioneModel;

    private static final String[] TipiDiEdificie = new String[] {
            "Appartamento", "Villa(Singola/Multi)", "Negozio", "Altro"};

    private static final String[] PosizionamentiUnitaEsterna = new String[] {
            "A Parete", "A Pavimento"};

    private static final String[] TipologieCostruttiveMurature = new String[] {
            "Cemento Armato", "Mattoni Pieni", "Mattoni Forati", "Pietra", "Altro"};

    private static final String[] LocaliEOPianiDelledificio = new String[] {
            "Interrato", "Piano rialzato", "Piano Terra", "Altro"};

    public ComposeReportTemplateFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SendReportFragment newInstance(String param1, String param2)
    {
        SendReportFragment fragment = new SendReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getActivity();

        climatizzazioneModel = new ClimatizzazioneModel();

        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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


        atvTipoDiEdificio = (AutoCompleteTextView) rootView.findViewById(R.id.atvTipoDiEdificio);
        atvTipoDiEdificio.setText(climatizzazioneModel.getTipoDiEdificio());
        atvTipoDiEdificio.setAdapter(TipiDiEdificieAdapter);

        atvTipoDiEdificio.setOnTouchListener(this);
        atvTipoDiEdificio.setInputType(InputType.TYPE_NULL);

        atvTipoDiEdificio.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                climatizzazioneModel.setTipoDiEdificio(selectedItem);

                Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        atvPosizionamentoUnitaEsterna = (AutoCompleteTextView) rootView.findViewById(R.id.atvPosizionamentoUnitaEsterna);
        atvPosizionamentoUnitaEsterna.setText(climatizzazioneModel.getPosizionamentoUnitaEsterna());
        atvPosizionamentoUnitaEsterna.setAdapter(PosizionamentiUnitaEsternaAdapter);
        atvPosizionamentoUnitaEsterna.setOnTouchListener(this);
        atvPosizionamentoUnitaEsterna.setInputType(InputType.TYPE_NULL);

        atvPosizionamentoUnitaEsterna.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                climatizzazioneModel.setPosizionamentoUnitaEsterna(selectedItem);
                Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        atvTipologiaCostruttivaMurature = (AutoCompleteTextView) rootView.findViewById(R.id.atvTipologiaCostruttivaMurature);
        atvTipologiaCostruttivaMurature.setText(climatizzazioneModel.getTipologiaCostruttivaMurature());
        atvTipologiaCostruttivaMurature.setAdapter(TipologieCostruttiveMuratureAdapter);
        atvTipologiaCostruttivaMurature.setOnTouchListener(this);
        atvTipologiaCostruttivaMurature.setInputType(InputType.TYPE_NULL);

        atvTipologiaCostruttivaMurature.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                climatizzazioneModel.setTipologiaCostruttivaMurature(selectedItem);
                Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        atvLocaliEOPianiDelledificio = (AutoCompleteTextView) rootView.findViewById(R.id.atvLocaliEOPianiDelledificio);
        atvLocaliEOPianiDelledificio.setText(climatizzazioneModel.getLocaliEOPianiDelledificio());
        atvLocaliEOPianiDelledificio.setAdapter(LocaliEOPianiDelledificioAdapter);
        atvLocaliEOPianiDelledificio.setOnTouchListener(this);
        atvLocaliEOPianiDelledificio.setInputType(InputType.TYPE_NULL);

        atvLocaliEOPianiDelledificio.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                climatizzazioneModel.setLocaliEOPianiDelledificio(selectedItem);
                Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        EditText etNoteSulLuoghoDiInstallazione = (EditText) rootView.findViewById(R.id.etNoteSulLuoghoDiInstallazione);

        etNoteSulLuoghoDiInstallazione.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    climatizzazioneModel.setNoteSulLuoghoDiInstallazione();
                    Toast.makeText(context, "onFocusChange etNoteSulLuoghoDiInstallazione", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final EditText etNoteSulTipologiaDellImpianto = (EditText) rootView.findViewById(R.id.etNoteSulTipologiaDellImpianto);
        etNoteSulTipologiaDellImpianto.setText(climatizzazioneModel.getNoteSulTipologiaDellImpianto());

        etNoteSulTipologiaDellImpianto.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (v == etNoteSulTipologiaDellImpianto && !hasFocus)
                {
                    climatizzazioneModel.setNoteSulTipologiaDellImpianto();
                    Toast.makeText(context, "onFocusChange etNoteSulTipologiaDellImpianto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final EditText etNoteRelativeAlCollegamento = (EditText) rootView.findViewById(R.id.etNoteRelativeAlCollegamento);
        etNoteRelativeAlCollegamento.setText(climatizzazioneModel.getNoteRelativeAlCollegamento());

        etNoteRelativeAlCollegamento.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (v == etNoteRelativeAlCollegamento && !hasFocus)
                {
                    climatizzazioneModel.setNoteRelativeAlCollegamento();
                    Toast.makeText(context, "onFocusChange etNoteRelativeAlCollegamento", Toast.LENGTH_SHORT).show();
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
