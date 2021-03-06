
package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

// gea_sopralluogo
public class GeaSopralluogo extends RealmObject
{
    private int id_sopralluogo;
    private int id_tecnico;

    private int completion_percent;

    //private int id_fornitore;
    //private int id_practice;
    //private String data_ora_assegnazione;

    private String data_sollecito_appuntamento;
    private String data_ora_presa_appuntamento;
    private String data_ora_sopralluogo;
    private String data_sollecito_rapporto;

/*    private String data_inizio_rimodulazione;
    private String data_fine_rimodulazione;
    private String esito_sopralluogo;
    private String tipo_gestione_sopralluogo;
    private String note_sopralluogo;*/

    public GeaSopralluogo() {}

    public GeaSopralluogo(int id_sopralluogo, int id_tecnico, String data_ora_presa_appuntamento, String data_ora_sopralluogo)
    {
        this.id_sopralluogo = id_sopralluogo;
        this.id_tecnico = id_tecnico;
        //this.inizializzazione = initialized == 1 ? true : false;

        this.data_ora_presa_appuntamento = data_ora_presa_appuntamento;
        this.data_ora_sopralluogo = data_ora_sopralluogo;
        //this.id_practice = id_practice;
    }

/*    public int getId_practice()
    {
        return id_practice;
    }*/

    public int getId_sopralluogo()
    {
        return id_sopralluogo;
    }

    public String getData_ora_sopralluogo()
    {
        return data_ora_sopralluogo;
    }

    public String getData_ora_presa_appuntamento()
    {
        return data_ora_presa_appuntamento;
    }

    public int getId_tecnico()
    {
        return id_tecnico;
    }

    public void setData_ora_presa_appuntamento(String data_ora_presa_appuntamento)
    {
        this.data_ora_presa_appuntamento = data_ora_presa_appuntamento;
    }

    public void setData_ora_sopralluogo(String data_ora_sopralluogo)
    {
        this.data_ora_sopralluogo = data_ora_sopralluogo;
    }

    public String getData_sollecito_appuntamento()
    {
        return data_sollecito_appuntamento;
    }

    public String getData_sollecito_rapporto()
    {
        return data_sollecito_rapporto;
    }

    public int getCompletion_percent()
    {
        return completion_percent;
    }
}
