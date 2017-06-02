package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class GeaRapporto extends RealmObject
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

    public GeaRapporto() {}

    public GeaRapporto(int id_sopralluogo, int id_rapporto_sopralluogo)
    {
        this.id_sopralluogo = id_sopralluogo;
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
    }

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

    public void setData_ora_compilazione_rapporto(String data_ora_compilazione_rapporto)
    {
        this.data_ora_compilazione_rapporto = data_ora_compilazione_rapporto;
    }

    public void setData_ora_invio_rapporto(String data_ora_invio_rapporto)
    {
        this.data_ora_invio_rapporto = data_ora_invio_rapporto;
    }

    public void setCompletion_percent(int completion_percent)
    {
        this.completion_percent = completion_percent;
    }

    public void setNome_tecnico(String nome_tecnico)
    {
        this.nome_tecnico = nome_tecnico;
    }

    public void setAltitudine(String altitudine)
    {
        this.altitudine = altitudine;
    }

    public void setLatitudine(String latitudine)
    {
        this.latitudine = latitudine;
    }

    public void setLongitudine(String longitudine)
    {
        this.longitudine = longitudine;
    }

    public void setNote_tecnico(String note_tecnico)
    {
        this.note_tecnico = note_tecnico;
    }
}
