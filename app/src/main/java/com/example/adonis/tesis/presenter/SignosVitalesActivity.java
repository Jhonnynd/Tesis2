package com.example.adonis.tesis.presenter;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adonis.tesis.R;
import com.example.adonis.tesis.dto.Paciente;
import com.example.adonis.tesis.dto.SignoVital;
import com.example.adonis.tesis.viewmodel.InterconsultaViewModel;
import com.example.adonis.tesis.viewmodel.PacienteViewModel;
import com.example.adonis.tesis.viewmodel.SignoVitalViewModel;

import util.Converters;


public class SignosVitalesActivity extends AppCompatActivity {

    private PacienteViewModel pacienteViewModel;
    private SignoVitalViewModel signoVitalViewModel;

    private TextView textViewPaciente;
    private TextView textViewFrecuenciaCardiaca;
    private TextView textViewFrecuenciaRespiratoria;
    private TextView textViewTemperatura;
    private TextView textViewSistolica;
    private TextView textViewDiastolica;
    private TextView textViewEdad;

    private SignoVital signoVital;
    private Paciente paciente;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signos_vitales);
        textViewPaciente = (TextView) findViewById(R.id.textViewPaciente);
        textViewFrecuenciaCardiaca = (TextView) findViewById(R.id.textViewFrecuenciaCardiaca);
        textViewFrecuenciaRespiratoria = (TextView) findViewById(R.id.textViewFrecuenciaRespiratoria);
        textViewTemperatura = (TextView) findViewById(R.id.textViewTemperatura);
        textViewSistolica = (TextView) findViewById(R.id.textViewSistolica);
        textViewDiastolica = (TextView) findViewById(R.id.textViewDiastolica);
        pacienteViewModel = ViewModelProviders.of(this).get(PacienteViewModel.class);
        signoVitalViewModel = ViewModelProviders.of(this).get(SignoVitalViewModel.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando... Por favor espere.");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            showProgressDialog();
            int paciente = bundle.getInt("paciente");
            pacienteViewModel.getPaciente(paciente).observe(this,
                    new Observer<Paciente>() {
                        @Override
                        public void onChanged(@Nullable Paciente paciente) {
                            hideProgressDialog();
                            if (paciente != null) {
                                setPaciente(paciente);
                            }
                        }
                    });
            int interconsulta = bundle.getInt("interconsulta");
            signoVitalViewModel.getSignoVitales(interconsulta).observe(
                    this, new Observer<SignoVital>() {
                        @Override
                        public void onChanged(@Nullable SignoVital signoVital) {
                            if (signoVital != null) {
                                setSignoVital(signoVital);
                            }
                        }
                    }
            );

        }
    }

    public void showProgressDialog() {
        progressDialog.show();
    }

    public void hideProgressDialog() {
        progressDialog.hide();
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        this.textViewEdad.setText(Converters.getEdad(paciente.getFechaIngreso()));
        this.textViewPaciente.setText(paciente.getNombre() + " " + paciente.getApellido());
    }

    public void setSignoVital(SignoVital signoVital) {
        this.signoVital = signoVital;
        int sistole = signoVital.getSistolica();
        int diastole = signoVital.getDiatolica();
        float temperatura = signoVital.getTemperatura();
        int tipoTemperatura = signoVital.getTipoTemperatura();
        int fc = signoVital.getFrecuenciaCardiaca();
        int fr = signoVital.getFecuenciaRespiratoria();
        int colorRojo = getResources().getColor(R.color.colorRojo);
        int colorAzul = getResources().getColor(R.color.colorVerdeAnalogo3);
        if (sistole > 130) {
            textViewSistolica.setTextColor(colorRojo);
        } else if (sistole < 100) {
            textViewSistolica.setTextColor(colorAzul);
        }
        if (diastole > 96) {
            textViewDiastolica.setTextColor(colorRojo);
        } else if (diastole < 70) {
            textViewDiastolica.setTextColor(colorAzul);
        }
        if ((temperatura > 37 && tipoTemperatura == 0)
                || (temperatura > 98.6 && tipoTemperatura == 1)) {
            textViewTemperatura.setTextColor(colorRojo);
        } else if ((temperatura < 36 && tipoTemperatura == 0)
                || (temperatura < 96.8 && tipoTemperatura == 1)) {
            textViewTemperatura.setTextColor(colorAzul);
        }
        if (fc > 100) {
            textViewFrecuenciaCardiaca.setTextColor(colorRojo);
        } else if (fc < 60) {
            textViewFrecuenciaCardiaca.setTextColor(colorAzul);
        }
        if (fr > 12) {
            textViewFrecuenciaRespiratoria.setTextColor(colorRojo);
        } else if (fr < 19) {
            textViewFrecuenciaRespiratoria.setTextColor(colorAzul);
        }
        textViewFrecuenciaRespiratoria.setText(fr);
        textViewFrecuenciaCardiaca.setText(fc);
        textViewTemperatura.setText(String.valueOf(temperatura));
        textViewDiastolica.setText(diastole);
        textViewSistolica.setText(sistole);
    }
}