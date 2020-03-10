package com.example.evaluacion;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ln.bean.Recepcion;

import java.util.Calendar;
import java.util.Date;

import javax.sql.RowSetEvent;

public class MainActivity extends AppCompatActivity {

    //DECLARACION DE LOS OBJETOS
    private EditText NomFuncionario, NomVisitante, CedulaVisitante, MotivoVisita, Documento, Identificador;
    private Button btnAgregar, btnModificar, btnBuscar;

    //DECLARACION DE VARIABLE PARA ALMACENAR LA FECHA
    private Date mfecha;

    //DECLARACION DE CONSTANTE LLAVE PARA LAS PREFERENCIAS
    public static final String KEY_EDITOR = "llave.editor";
    //VARIABLE PARA ALMACENAR LA PREFERENCIA
    private SharedPreferences preferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        Identificador = findViewById(R.id.identificador);
        NomFuncionario = findViewById(R.id.funcionario);
        NomVisitante = findViewById(R.id.visitante);
        CedulaVisitante = findViewById(R.id.cedula);
        MotivoVisita = findViewById(R.id.motivo);
        Documento = findViewById(R.id.documento);
        btnAgregar = findViewById(R.id.guardar);
        btnModificar = findViewById(R.id.modificar);
        btnBuscar = findViewById(R.id.buscar);

        //
        final CalendarView fechaObj = findViewById(R.id.calendarView);

        //EVENTO CAMBIO DE FECHA
        fechaObj.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mfecha = c.getTime();
            }
        });

        //DESACTIVAR TODOS LOS BOTONES
        btnModificar.setEnabled(false);

        //CREAR LAS PREFERENCIAS
        preferencia = getSharedPreferences(KEY_EDITOR, MODE_PRIVATE);

        //OBTENGO LAS PREFERENCIAS
        Recepcion recep = Recepcion.getPreferences(preferencia);

        //ASIGNACION DE CAMPOS CON LA ULTIMA PREFERENCIA ALMACENADA
            Identificador.setText(String.valueOf(recep.getId()));
            NomFuncionario.setText(recep.getNomFuncionario());
            NomVisitante.setText(recep.getNomVisitante());
            CedulaVisitante.setText(recep.getCedulaVisitante());
            MotivoVisita.setText(recep.getMotivoVisita());
            Documento.setText(recep.getDocumento());
            fechaObj.setDate(recep.getFecha().getTime());
            mfecha = recep.getFecha();

        //EVENTO GUARDAR
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //VALIDACION DE CAMPOS A LLENAR
                if (Identificador.getText().toString().length() != 0 && NomFuncionario.getText().toString().length() != 0 &&
                    CedulaVisitante.getText().toString().length() != 0 && NomVisitante.getText().toString().length() != 0 &&
                    MotivoVisita.getText().toString().length() != 0) {
                    Recepcion recepcion = new Recepcion();

                    //ASIGNAR LOS VALORES A LA CLASE CREADA
                    recepcion.setId(Integer.parseInt(Identificador.getText().toString()));
                    recepcion.setNomFuncionario(NomFuncionario.getText().toString());
                    recepcion.setNomVisitante(NomVisitante.getText().toString());
                    recepcion.setCedulaVisitante(CedulaVisitante.getText().toString());
                    recepcion.setMotivoVisita(MotivoVisita.getText().toString());
                    recepcion.setDocumento(Documento.getText().toString());
                    recepcion.setFecha(mfecha);

                    //SI SE GUARDO EN SQLITE
                    if (recepcion.agregar(MainActivity.this)) {
                        Identificador.setText("");
                        NomFuncionario.setText("");
                        NomVisitante.setText("");
                        CedulaVisitante.setText("");
                        MotivoVisita.setText("");
                        Documento.setText("");
                        fechaObj.setDate(new Date().getTime());

                        // SOLO NECESITABAS LLAMAR A TU METODO CON LA MISMA INSTANCIA
                        recepcion.savePreferences(preferencia);

                        //ESTE CODIGO ES INNECESARIO, CON LA MISMA INSTACIA QUE GUARDAS EN SQLite,
                        // CON ESA MISMA INSTANCIA GUARDAS LAS PREFERENCIAS
                    /*ALMACENAR PREFERENCIAS
                    Recepcion rec = new Recepcion();
                    rec.setId(Integer.parseInt(Identificador.getText().toString()));
                    rec.setNomFuncionario(NomFuncionario.getText().toString());
                    rec.setNomVisitante(NomVisitante.getText().toString());
                    rec.setCedulaVisitante(CedulaVisitante.getText().toString());
                    rec.setMotivoVisita(MotivoVisita.getText().toString());
                    rec.setDocumento(Documento.getText().toString());
                    rec.setFecha(mfecha);
                        //GUARDAR LAS PREFERENCIAS
                        rec.savePreferences(preferencia);*/

                        Toast.makeText(MainActivity.this, "Almacenado", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity.this, "No almacenado", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MainActivity.this, "Complete los campos necesarios", Toast.LENGTH_SHORT).show();
            }
        });

        //EVENTO BUSCAR
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recepcion recepcion = new Recepcion();

                recepcion = recepcion.buscar(MainActivity.this, CedulaVisitante.getText().toString());

                if (recepcion != null) {

                    btnModificar.setEnabled(true);
                    btnAgregar.setEnabled(false);
                    Identificador.setEnabled(false);

                    NomFuncionario.setText(String.valueOf(recepcion.getNomFuncionario()));
                    NomVisitante.setText(String.valueOf(recepcion.getNomVisitante()));
                    Identificador.setText(String.valueOf(recepcion.getId()));
                    MotivoVisita.setText(String.valueOf(recepcion.getMotivoVisita()));
                    Documento.setText(String.valueOf(recepcion.getDocumento()));
                    fechaObj.setDate(recepcion.getFecha().getTime());
                    mfecha = recepcion.getFecha();
                } else
                    Toast.makeText(MainActivity.this, "No existe Cedula", Toast.LENGTH_SHORT).show();

            }
        });

        //EVENTO MODIFICAR
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recepcion recepcion = new Recepcion();
                //ALMACENAR LOS DATOS MODIFICADOS
                //recepcion.setId(Integer.parseInt(Identificador.getText().toString()));
                recepcion.setNomFuncionario(NomFuncionario.getText().toString());
                recepcion.setNomVisitante(NomVisitante.getText().toString());
                recepcion.setCedulaVisitante(CedulaVisitante.getText().toString());
                recepcion.setMotivoVisita(MotivoVisita.getText().toString());
                recepcion.setDocumento(Documento.getText().toString());
                recepcion.setFecha(mfecha);

                //SI SE MODIFICO LA RECEPCION
                if(recepcion.modificar(MainActivity.this, CedulaVisitante.getText().toString()))
                {
                    //GUARDAS EL ID EN TU INSTACIA
                    recepcion.setId(Integer.parseInt(Identificador.getText().toString()));
                    //SOBREESCRIBES LA PREFERENCIA Y SE GUARDA EL CAMBIO
                    recepcion.savePreferences(preferencia);

                    //LIMPIAR LOS CAMPOS
                    Identificador.setText("");
                    NomFuncionario.setText("");
                    NomVisitante.setText("");
                    CedulaVisitante.setText("");
                    MotivoVisita.setText("");
                    Documento.setText("");
                    fechaObj.setDate(new Date().getTime());
                    mfecha = new Date();

                    //ACTIVACION Y DESACTIVACION DE OBJETOS
                    btnModificar.setEnabled(false);
                    Identificador.setEnabled(true);
                    btnAgregar.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Modificado", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "No Modificado", Toast.LENGTH_SHORT).show();
            }
        });

    }
}