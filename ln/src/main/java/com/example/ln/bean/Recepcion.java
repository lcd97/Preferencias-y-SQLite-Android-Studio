package com.example.ln.bean;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.Date;

public class Recepcion implements Serializable {
    private int Id;
    private String NomFuncionario;
    private String NomVisitante;
    private String CedulaVisitante;
    private String MotivoVisita;
    private Date Fecha;
    private String Documento;
    //private String variableNoSirve

    //VARIABLE DE PREFERENCIAS
    public static final String ID = "id";
    public static final String NOMFUNCIONARIO = "funcionario";
    public static final String NOMVISITANTE = "visitante";
    public static final String CEDULAVISITANTE = "cedula";
    public static final String MOTIVOVISITA = "motivo";
    public static final String FECHA = "fecha";
    public static final String DOCUMENTO = "documento";


    public Recepcion() {
        super();
    }

    public Recepcion(int id, String nomFuncionario, String nomVisitante, String cedulaVisitante, String motivoVisita, Date fecha, String documento) {
        Id = id;
        NomFuncionario = nomFuncionario;
        NomVisitante = nomVisitante;
        CedulaVisitante = cedulaVisitante;
        MotivoVisita = motivoVisita;
        Fecha = fecha;
        Documento = documento;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNomFuncionario() {
        return NomFuncionario;
    }

    public void setNomFuncionario(String nomFuncionario) {
        NomFuncionario = nomFuncionario;
    }

    public String getNomVisitante() {
        return NomVisitante;
    }

    public void setNomVisitante(String nomVisitante) {
        NomVisitante = nomVisitante;
    }

    public String getCedulaVisitante() {
        return CedulaVisitante;
    }

    public void setCedulaVisitante(String cedulaVisitante) {
        CedulaVisitante = cedulaVisitante;
    }

    public String getMotivoVisita() {
        return MotivoVisita;
    }

    public void setMotivoVisita(String motivoVisita) {
        MotivoVisita = motivoVisita;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getDocumento() {
        return Documento;
    }

    public void setDocumento(String documento) {
        Documento = documento;
    }

    //EVENTO AGREGAR A SQLITE
    public boolean agregar(Context context){
        boolean band = false;

        SQLiteDatabase myDB = null;
        SQLiteHelper DBHelper = null;

        try {
            DBHelper= new SQLiteHelper(context.getFilesDir().getCanonicalPath(),context);
            myDB= DBHelper.getWritableDatabase();

            ContentValues values= new ContentValues();
            values.put("Id",Id);
            values.put("NomFuncionario",NomFuncionario);
            values.put("NomVisitante",NomVisitante);
            values.put("CedulaVisitante",CedulaVisitante);
            values.put("MotivoVisita",MotivoVisita);
            values.put("Documento",Documento);
            values.put("Fecha",Fecha.getTime());

            //INSERCION EN LA BD TABLA DE RECEPCION
            band = myDB.insert("Recepcion",null, values) != -1;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (myDB != null)
                myDB.close();
            if (DBHelper != null)
                DBHelper.close();
        }

        return band;
    }

    //METODO PARA MODIFICAR RECEPCION
    public boolean modificar(Context context, String cedula){
        boolean band = false;

        SQLiteDatabase myDB = null;
        SQLiteHelper DBHelper = null;

        try {

            DBHelper= new SQLiteHelper(context.getFilesDir().getCanonicalPath(),context);
            myDB= DBHelper.getWritableDatabase();

            ContentValues values= new ContentValues();
            values.put("NomFuncionario",NomFuncionario);
            values.put("NomVisitante",NomVisitante);
            values.put("CedulaVisitante",CedulaVisitante);
            values.put("MotivoVisita",MotivoVisita);
            values.put("Documento",Documento);
            values.put("Fecha",Fecha.getTime());

            //MODIFICANDO EL REGISTRO CUYA CEDULA SEA IGUAL AL PARAMETRO CEDULA
            band = myDB.update("Recepcion",values,"CedulaVisitante=?",new String[]{cedula}) != -1;

        }catch (Exception e)
        {
            e.printStackTrace();
        } finally {
            if (myDB != null)
                myDB.close();
            if (DBHelper != null)
                DBHelper.close();
        }

        return band;
    }

    //METODO PARA BUSCAR UN REGISTRO POR MEDIO DE CEDULA
    public Recepcion buscar(Context context, String Cedula){
        Recepcion obj = null;
        SQLiteDatabase myDataBase=null;
        SQLiteHelper myDbHelper=null;

        try{
            myDbHelper= new SQLiteHelper(context.getFilesDir().getCanonicalPath(),context);
            myDataBase= myDbHelper.getWritableDatabase();

            Cursor cursor= myDataBase.query(true,"Recepcion",null,"CedulaVisitante = ?",new String[]{Cedula},null,null,null,null);

            if(cursor.getCount()>0)
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    obj = new Recepcion();
                    obj.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                    obj.setNomFuncionario(cursor.getString(cursor.getColumnIndex("NomFuncionario")));
                    obj.setNomVisitante(cursor.getString(cursor.getColumnIndex("NomVisitante")));
                    obj.setCedulaVisitante(cursor.getString(cursor.getColumnIndex("CedulaVisitante")));
                    obj.setMotivoVisita(cursor.getString(cursor.getColumnIndex("MotivoVisita")));
                    obj.setFecha(new Date(cursor.getLong(cursor.getColumnIndex("Fecha"))));
                    obj.setDocumento(cursor.getString(cursor.getColumnIndex("Documento")));
                    cursor.moveToNext();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (myDataBase != null)
                myDataBase.close();
            if (myDbHelper != null)
                myDbHelper.close();
        }

        return obj;
    }

    //METODO PARA ALMACENAR LAS PREFERENCIAS
    public void savePreferences(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();

        //Lo guardamos como entero
        editor.putInt(ID, Id);
        editor.putString(NOMFUNCIONARIO, NomFuncionario);
        editor.putString(NOMVISITANTE, NomVisitante);
        editor.putString(CEDULAVISITANTE, CedulaVisitante);
        editor.putString(MOTIVOVISITA, MotivoVisita);
        editor.putString(DOCUMENTO, Documento);

        //Lo guardamos como LONG
        editor.putLong(FECHA, Fecha.getTime());

        editor.apply();
    }

    //METODO PARA OBTENER LAS PREFERENCIAS
    public static Recepcion getPreferences(SharedPreferences preferences) {
        Recepcion recepcion = new Recepcion();

        //LO RECUPERAMOS COMO ENTERO
        recepcion.setId(preferences.getInt(ID, 0));
        recepcion.setNomFuncionario(preferences.getString(NOMFUNCIONARIO,null));
        recepcion.setNomVisitante(preferences.getString(NOMVISITANTE,null));
        recepcion.setCedulaVisitante(preferences.getString(CEDULAVISITANTE,null));
        recepcion.setMotivoVisita(preferences.getString(MOTIVOVISITA,null));
        //LO RECUPERAMOS COMO FECHA
        recepcion.setFecha(new Date(preferences.getLong(FECHA, new Date().getTime())));
        recepcion.setDocumento(preferences.getString(DOCUMENTO,null));

        return recepcion;

    }
}