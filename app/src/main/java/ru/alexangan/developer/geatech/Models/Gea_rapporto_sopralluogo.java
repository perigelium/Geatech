package ru.alexangan.developer.geatech.Models;


import io.realm.RealmList;
import io.realm.RealmObject;

public class Gea_rapporto_sopralluogo
{
    private int id_rapporto_sopralluogo;
    private int idSopralluogo;

    private double latitude; // latitudine
    private double longitude; // longitudine
    private int altitude; // altitudine

    private String dataOraSopralluogo;
    private String dataOraRaportoCompilato; // data_ora_compilazione_rapporto
    private String dataOraRaportoInviato; // data_ora_invio_rapporto
    private String nome_tecnico;
    private String note_tecnico;

    public Gea_rapporto_sopralluogo() {}

    public String getDataOraRaportoCompletato()
    {
        return dataOraRaportoCompilato;
    }

    public void setDataOraRaportoCompletato(String dataOraRaportoCompletato)
    {
        this.dataOraRaportoCompilato = dataOraRaportoCompletato;
    }

    public String getDataOraRaportoInviato()
    {
        return dataOraRaportoInviato;
    }

    public void setDataOraRaportoInviato(String dataOraRaportoInviato)
    {
        this.dataOraRaportoInviato = dataOraRaportoInviato;
    }

    public int getIdSopralluogo()
    {
        return idSopralluogo;
    }

    public void setIdSopralluogo(int idSopralluogo)
    {
        this.idSopralluogo = idSopralluogo;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getAltitude()
    {
        return altitude;
    }

    public void setAltitude(int altitude)
    {
        this.altitude = altitude;
    }
}
