package com.example.home.ui.liquidaciones;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home.R;
import com.example.home.ui.AdminDataBase;
import com.example.home.ui.boleto.ModeloBoleto;
import com.example.home.ui.home.ModeloSalida;
import com.example.home.ui.home.SalidaAdapter;
import com.example.home.ui.socios.ModeloSocio;
import com.example.home.ui.socios.SocioAdapter;
import com.example.home.ui.socios.SociosViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LiquidacionFragment extends Fragment {

    private LiquidacionViewModel liquidacionViewModel;
    List<ModeloSalida> listaBase;
    List <String> listaSpinner= new ArrayList<>();
    AdminDataBase adb;
    ModeloSalida aux;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    List <ModeloLiquidacion> listafdb = new ArrayList<ModeloLiquidacion>();
    HashMap<Integer,Integer> spinnerMap = new HashMap<Integer, Integer>();

    private AppCompatRadioButton actbMale;
    private AppCompatRadioButton actbFemale;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        liquidacionViewModel =
                ViewModelProviders.of(this).get(LiquidacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_liquidaciones, container, false);
        adb = new AdminDataBase(getActivity(),"empresaDeTransporte.db",null, 1);
        recyclerView = (RecyclerView)  root.findViewById(R.id.my_recycler_liquidaciones);
        mAdapter = new LiquidacionAdapter(getActivity(),(ArrayList<ModeloLiquidacion>)adb.listaLiquidaciones());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FloatingActionButton fab = root.findViewById(R.id.fabhome);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
   //             try{
                    realizarLiquidacion();
//                }
//                catch (Exception e){
//                    Toast.makeText(getActivity(),
//                "Error", Toast.LENGTH_SHORT).show();
//                };
            }
        });

        //recyclerView.invalidate();
        return root;
    }

    void realizarLiquidacion(){
        listaSpinner.clear();

        LayoutInflater inflador = LayoutInflater.from(getActivity());
        View view = inflador.inflate(R.layout.dialog_liquidaciones ,null, false);
        final Spinner idSalidaLiquidaciones;
        idSalidaLiquidaciones = (Spinner) view.findViewById(R.id.spinnerSalidasLiquidacionesD);


        AlertDialog.Builder alertAlta = new AlertDialog.Builder(getActivity());
        alertAlta.setTitle("Realizar liquidacion");
        alertAlta.setCancelable(false);
        alertAlta.setView(view);

        listaBase = adb.getListaSalidas();
        for(int i=0;i<listaBase.size();i++){
            aux=listaBase.get(i);
            Log.d("idsalida",":"+aux.getIdSalida());
            spinnerMap.put(i+1,aux.getIdSalida());
            String spnDestino = aux.getDestino();
            String spnFecha = aux.getFechaSalida();
            String spnHora = aux.getHoraSalida();
            String nombreCompleto = spnDestino+"   |   "+spnFecha+"   |   "+spnHora;
            //Log.d("spinner","y :"+nombreCompleto );
            listaSpinner.add(nombreCompleto);
        }
        listaSpinner.add(0,"Salidas Programadas");
        final Spinner spinnerDestinoOpciones = view.findViewById(R.id.spinnerSalidasLiquidacionesD);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, listaSpinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerDestinoOpciones.setAdapter(adapter2);
        spinnerDestinoOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                String text = adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        alertAlta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        alertAlta.setPositiveButton("Agregar Liquidacion", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!spinnerDestinoOpciones.getSelectedItem().toString().equalsIgnoreCase("Salidas Programadas"
                )){
                    String h;
                    int salidaPosition = idSalidaLiquidaciones.getSelectedItemPosition();
                    Log.d("placa POSITION",":"+salidaPosition);
                    Log.d("placa key",":"+spinnerMap.get(idSalidaLiquidaciones.getSelectedItemPosition()));
                    int idSalida = spinnerMap.get(idSalidaLiquidaciones.getSelectedItemPosition());
                    int estado=0;
                    //String dest = destino.getSelectedItem().toString();
                    adb.altaLiquidacion(new ModeloLiquidacion(estado,idSalida));
                    Toast.makeText(getActivity(), "El registro se grabo con exito", Toast.LENGTH_SHORT).show();
                    listafdb = adb.listaLiquidaciones();
                    mAdapter = new LiquidacionAdapter(getActivity(),(ArrayList<ModeloLiquidacion>)listafdb);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                }


            }
        });
        alertAlta.show();

    }

}