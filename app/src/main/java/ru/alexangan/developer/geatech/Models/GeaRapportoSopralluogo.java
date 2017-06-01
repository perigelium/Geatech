package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class GeaRapportoSopralluogo extends RealmObject
{
    private int id_rapporto_sopralluogo;
    private int id_sopralluogo;
    private String data_ora_compilazione_rapporto; // data_ora_compilazione_rapporto
    private String data_ora_invio_rapporto; // data_ora_invio_rapporto
    private int completion_percent;
    private String nome_tecnico;
    private String altitudine; // altitudine
    private String latitudine; // latitudine
    private String longitudine; // longitudine
    private String note_tecnico;

    public GeaRapportoSopralluogo() {}

    public int getId_rapporto_sopralluogo()
    {
        return id_rapporto_sopralluogo;
    }

    public String getDataOraRaportoCompletato()
    {
        return data_ora_compilazione_rapporto;
    }

    public String getData_ora_invio_rapporto()
    {
        return data_ora_invio_rapporto;
    }

    public int getId_sopralluogo()
    {
        return id_sopralluogo;
    }

    public String getLongitudine()
    {
        return longitudine;
    }

    public String getLatitudine()
    {
        return latitudine;
    }

    public String getAltitudine()
    {
        return altitudine;
    }

    public String getNome_tecnico()
    {
        return nome_tecnico;
    }

    public String getData_ora_compilazione_rapporto()
    {
        return data_ora_compilazione_rapporto;
    }

    public int getCompletion_percent()
    {
        return completion_percent;
    }

    public String getNote_tecnico()
    {
        return note_tecnico;
    }
}
