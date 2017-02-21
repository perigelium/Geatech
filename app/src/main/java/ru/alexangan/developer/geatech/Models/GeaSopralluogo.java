
package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

// gea_sopralluogo
public class GeaSopralluogo extends RealmObject
{
    private int id_sopralluogo;
    private boolean inizializzazione;
    //private int id_fornitore;
    //private int id_practice;
    private String data_ora_assegnazione;
    private String data_sollecito_appuntamento;
    private String data_ora_presa_appuntamento;
    private String data_ora_sopralluogo;
    private String data_sollecito_rapporto;
    private String data_inizio_rimodulazione;
    private String data_fine_rimodulazione;
    private String esito_sopralluogo;
    private String tipo_gestione_sopralluogo;
    private String note_sopralluogo;

    public GeaSopralluogo(){};

    public GeaSopralluogo(int id_sopralluogo, int initialized, String data_ora_presa_appuntamento, String data_ora_sopralluogo)
    {
        this.id_sopralluogo = id_sopralluogo;
        this.inizializzazione = initialized == 1 ? true : false;
/*
        this.id_fornitore = id_fornitore;
        this.id_practice = id_practice;
        this.data_ora_assegnazione = data_ora_assegnazione;*/
        //this.data_sollecito_appuntamento = data_sollecito_appuntamento;
        this.data_ora_presa_appuntamento = data_ora_presa_appuntamento;
        this.data_ora_sopralluogo = data_ora_sopralluogo;
/*        this.data_sollecito_rapporto = data_sollecito_rapporto;
        this.data_inizio_rimodulazione = data_inizio_rimodulazione;
        this.data_fine_rimodulazione = data_fine_rimodulazione;
        this.esito_sopralluogo = esito_sopralluogo;
        this.tipo_gestione_sopralluogo = tipo_gestione_sopralluogo;
        this.note_sopralluogo = note_sopralluogo;*/
    }

    public int getId_sopralluogo()
    {
        return id_sopralluogo;
    }

    public boolean getInizializzazione()
    {
        return inizializzazione;
    }

    public String getData_ora_sopralluogo()
    {
        return data_ora_sopralluogo;
    }

    public String getData_ora_presa_appuntamento()
    {
        return data_ora_presa_appuntamento;
    }
}
