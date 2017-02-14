
package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

// gea_sopralluoghi
public class VisitStates extends RealmObject
{
    private int id;
    private int id_sopralluogo;
    private String inizializzazione;
    private String id_fornitore;
    private String id_practice;
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

    public VisitStates(){};

    public VisitStates(int id)
    {
        this.id = id;
    }

    public VisitStates(int id_sopralluogo, String data_sollecito_appuntamento)
    {
        this.id_sopralluogo = id_sopralluogo;
/*
        this.id_fornitore = id_fornitore;
        this.id_practice = id_practice;
        this.data_ora_assegnazione = data_ora_assegnazione;*/
        this.data_sollecito_appuntamento = data_sollecito_appuntamento;
//        this.data_ora_presa_appuntamento = data_ora_presa_appuntamento;
        //this.data_ora_sopralluogo = data_ora_sopralluogo;
/*        this.data_sollecito_rapporto = data_sollecito_rapporto;
        this.data_inizio_rimodulazione = data_inizio_rimodulazione;
        this.data_fine_rimodulazione = data_fine_rimodulazione;
        this.esito_sopralluogo = esito_sopralluogo;
        this.tipo_gestione_sopralluogo = tipo_gestione_sopralluogo;
        this.note_sopralluogo = note_sopralluogo;*/
    }

    /**
     * 
     * @return
     *     The id_sopralluogo
     */
    public int getId_sopralluogo() {
        return id_sopralluogo;
    }

    /**
     * 
     * @param id_sopralluogo
     *     The id_sopralluogo
     */
    public void setId_sopralluogo(int id_sopralluogo) {
        this.id_sopralluogo = id_sopralluogo;
    }

    /**
     * 
     * @return
     *     The id_fornitore
     */
    public String getId_fornitore() {
        return id_fornitore;
    }

    /**
     * 
     * @param id_fornitore
     *     The id_fornitore
     */
    public void setId_fornitore(String id_fornitore) {
        this.id_fornitore = id_fornitore;
    }

    /**
     * 
     * @return
     *     The id_practice
     */
    public String getId_practice() {
        return id_practice;
    }

    /**
     * 
     * @param id_practice
     *     The id_practice
     */
    public void setId_practice(String id_practice) {
        this.id_practice = id_practice;
    }

    /**
     * 
     * @return
     *     The data_ora_assegnazione
     */
    public String getData_ora_assegnazione() {
        return data_ora_assegnazione;
    }

    /**
     * 
     * @param data_ora_assegnazione
     *     The data_ora_assegnazione
     */
    public void setData_ora_assegnazione(String data_ora_assegnazione) {
        this.data_ora_assegnazione = data_ora_assegnazione;
    }

    /**
     * 
     * @return
     *     The data_sollecito_appuntamento
     */
    public String getData_sollecito_appuntamento() {
        return data_sollecito_appuntamento;
    }

    /**
     * 
     * @param data_sollecito_appuntamento
     *     The data_sollecito_appuntamento
     */
    public void setData_sollecito_appuntamento(String data_sollecito_appuntamento) {
        this.data_sollecito_appuntamento = data_sollecito_appuntamento;
    }

    /**
     * 
     * @return
     *     The data_ora_presa_appuntamento
     */
    public String getData_ora_presa_appuntamento() {
        return data_ora_presa_appuntamento;
    }

    /**
     * 
     * @param data_ora_presa_appuntamento
     *     The data_ora_presa_appuntamento
     */
    public void setData_ora_presa_appuntamento(String data_ora_presa_appuntamento) {
        this.data_ora_presa_appuntamento = data_ora_presa_appuntamento;
    }

    /**
     * 
     * @return
     *     The data_ora_sopralluogo
     */
    public String getData_ora_sopralluogo() {
        return data_ora_sopralluogo;
    }

    /**
     * 
     * @param data_ora_sopralluogo
     *     The data_ora_sopralluogo
     */
    public void setData_ora_sopralluogo(String data_ora_sopralluogo) {
        this.data_ora_sopralluogo = data_ora_sopralluogo;
    }

    /**
     * 
     * @return
     *     The data_sollecito_rapporto
     */
    public String getData_sollecito_rapporto() {
        return data_sollecito_rapporto;
    }

    /**
     * 
     * @param data_sollecito_rapporto
     *     The data_sollecito_rapporto
     */
    public void setData_sollecito_rapporto(String data_sollecito_rapporto) {
        this.data_sollecito_rapporto = data_sollecito_rapporto;
    }

    /**
     * 
     * @return
     *     The data_inizio_rimodulazione
     */
    public String getData_inizio_rimodulazione() {
        return data_inizio_rimodulazione;
    }

    /**
     * 
     * @param data_inizio_rimodulazione
     *     The data_inizio_rimodulazione
     */
    public void setData_inizio_rimodulazione(String data_inizio_rimodulazione) {
        this.data_inizio_rimodulazione = data_inizio_rimodulazione;
    }

    /**
     * 
     * @return
     *     The data_fine_rimodulazione
     */
    public String getData_fine_rimodulazione() {
        return data_fine_rimodulazione;
    }

    /**
     * 
     * @param data_fine_rimodulazione
     *     The data_fine_rimodulazione
     */
    public void setData_fine_rimodulazione(String data_fine_rimodulazione) {
        this.data_fine_rimodulazione = data_fine_rimodulazione;
    }

    /**
     * 
     * @return
     *     The esito_sopralluogo
     */
    public String getEsito_sopralluogo() {
        return esito_sopralluogo;
    }

    /**
     * 
     * @param esito_sopralluogo
     *     The esito_sopralluogo
     */
    public void setEsito_sopralluogo(String esito_sopralluogo) {
        this.esito_sopralluogo = esito_sopralluogo;
    }

    /**
     * 
     * @return
     *     The tipo_gestione_sopralluogo
     */
    public String getTipo_gestione_sopralluogo() {
        return tipo_gestione_sopralluogo;
    }

    /**
     * 
     * @param tipo_gestione_sopralluogo
     *     The tipo_gestione_sopralluogo
     */
    public void setTipo_gestione_sopralluogo(String tipo_gestione_sopralluogo) {
        this.tipo_gestione_sopralluogo = tipo_gestione_sopralluogo;
    }

    /**
     * 
     * @return
     *     The note_sopralluogo
     */
    public String getNote_sopralluogo() {
        return note_sopralluogo;
    }

    /**
     * 
     * @param note_sopralluogo
     *     The note_sopralluogo
     */
    public void setNote_sopralluogo(String note_sopralluogo) {
        this.note_sopralluogo = note_sopralluogo;
    }

    public int getId()
    {
        return id;
    }
}
