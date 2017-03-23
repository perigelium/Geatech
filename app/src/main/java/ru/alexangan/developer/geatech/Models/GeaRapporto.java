package ru.alexangan.developer.geatech.Models;


public class GeaRapporto
{
    private int id_rapporto_sopralluogo;
    private int id_sopralluogo;
    private String data_ora_compilazione_rapporto; // data_ora_compilazione_rapporto
    private String data_ora_invio_rapporto; // data_ora_invio_rapporto
    private int completion_percent;
    private String nome_tecnico;
    private int altitudine; // altitudine
    private double latitudine; // latitudine
    private double longitudine; // longitudine
    private String note_tecnico;

    public GeaRapporto() {}

    public String getDataOraRaportoCompletato()
    {
        return data_ora_compilazione_rapporto;
    }

    public void setDataOraRaportoCompletato(String dataOraRaportoCompletato)
    {
        this.data_ora_compilazione_rapporto = dataOraRaportoCompletato;
    }

    public String getData_ora_invio_rapporto()
    {
        return data_ora_invio_rapporto;
    }

    public void setData_ora_invio_rapporto(String data_ora_invio_rapporto)
    {
        this.data_ora_invio_rapporto = data_ora_invio_rapporto;
    }

    public int getId_sopralluogo()
    {
        return id_sopralluogo;
    }

    public void setId_sopralluogo(int id_sopralluogo)
    {
        this.id_sopralluogo = id_sopralluogo;
    }

    public double getLongitudine()
    {
        return longitudine;
    }

    public void setLongitudine(double longitudine)
    {
        this.longitudine = longitudine;
    }

    public double getLatitudine()
    {
        return latitudine;
    }

    public void setLatitudine(double latitudine)
    {
        this.latitudine = latitudine;
    }

    public double getAltitudine()
    {
        return altitudine;
    }

    public void setAltitudine(int altitudine)
    {
        this.altitudine = altitudine;
    }

    public String getNome_tecnico()
    {
        return nome_tecnico;
    }

    public void setNome_tecnico(String nome_tecnico)
    {
        this.nome_tecnico = nome_tecnico;
    }

    public int getId_rapporto_sopralluogo()
    {
        return id_rapporto_sopralluogo;
    }

    public void setId_rapporto_sopralluogo(int id_rapporto_sopralluogo)
    {
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
    }
}
