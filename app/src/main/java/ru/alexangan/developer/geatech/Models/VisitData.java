
package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class VisitData  extends RealmObject
{

    private String idSopralluogo;
    private String idFornitore;
    private String idPractice;
    private String dataOraAssegnazione;
    private String dataSollecitoAppuntamento;
    private String dataOraPresaAppuntamento;
    private String dataOraSopralluogo;
    private String dataSollecitoRapporto;
    private String dataInizioRimodulazione;
    private String dataFineRimodulazione;
    private String esitoSopralluogo;
    private String tipoGestioneSopralluogo;
    private String noteSopralluogo;

    public VisitData() {
    }

    public VisitData(String dataSollecitoAppuntamento) {
/*        this.idSopralluogo = idSopralluogo;
        this.idFornitore = idFornitore;
        this.idPractice = idPractice;
        this.dataOraAssegnazione = dataOraAssegnazione;*/
        this.dataSollecitoAppuntamento = dataSollecitoAppuntamento;
//        this.dataOraPresaAppuntamento = dataOraPresaAppuntamento;
        //this.dataOraSopralluogo = dataOraSopralluogo;
/*        this.dataSollecitoRapporto = dataSollecitoRapporto;
        this.dataInizioRimodulazione = dataInizioRimodulazione;
        this.dataFineRimodulazione = dataFineRimodulazione;
        this.esitoSopralluogo = esitoSopralluogo;
        this.tipoGestioneSopralluogo = tipoGestioneSopralluogo;
        this.noteSopralluogo = noteSopralluogo;*/
    }

    /**
     * 
     * @return
     *     The idSopralluogo
     */
    public String getIdSopralluogo() {
        return idSopralluogo;
    }

    /**
     * 
     * @param idSopralluogo
     *     The id_sopralluogo
     */
    public void setIdSopralluogo(String idSopralluogo) {
        this.idSopralluogo = idSopralluogo;
    }

    /**
     * 
     * @return
     *     The idFornitore
     */
    public String getIdFornitore() {
        return idFornitore;
    }

    /**
     * 
     * @param idFornitore
     *     The id_fornitore
     */
    public void setIdFornitore(String idFornitore) {
        this.idFornitore = idFornitore;
    }

    /**
     * 
     * @return
     *     The idPractice
     */
    public String getIdPractice() {
        return idPractice;
    }

    /**
     * 
     * @param idPractice
     *     The id_practice
     */
    public void setIdPractice(String idPractice) {
        this.idPractice = idPractice;
    }

    /**
     * 
     * @return
     *     The dataOraAssegnazione
     */
    public String getDataOraAssegnazione() {
        return dataOraAssegnazione;
    }

    /**
     * 
     * @param dataOraAssegnazione
     *     The data_ora_assegnazione
     */
    public void setDataOraAssegnazione(String dataOraAssegnazione) {
        this.dataOraAssegnazione = dataOraAssegnazione;
    }

    /**
     * 
     * @return
     *     The dataSollecitoAppuntamento
     */
    public String getDataSollecitoAppuntamento() {
        return dataSollecitoAppuntamento;
    }

    /**
     * 
     * @param dataSollecitoAppuntamento
     *     The data_sollecito_appuntamento
     */
    public void setDataSollecitoAppuntamento(String dataSollecitoAppuntamento) {
        this.dataSollecitoAppuntamento = dataSollecitoAppuntamento;
    }

    /**
     * 
     * @return
     *     The dataOraPresaAppuntamento
     */
    public String getDataOraPresaAppuntamento() {
        return dataOraPresaAppuntamento;
    }

    /**
     * 
     * @param dataOraPresaAppuntamento
     *     The data_ora_presa_appuntamento
     */
    public void setDataOraPresaAppuntamento(String dataOraPresaAppuntamento) {
        this.dataOraPresaAppuntamento = dataOraPresaAppuntamento;
    }

    /**
     * 
     * @return
     *     The dataOraSopralluogo
     */
    public String getDataOraSopralluogo() {
        return dataOraSopralluogo;
    }

    /**
     * 
     * @param dataOraSopralluogo
     *     The data_ora_sopralluogo
     */
    public void setDataOraSopralluogo(String dataOraSopralluogo) {
        this.dataOraSopralluogo = dataOraSopralluogo;
    }

    /**
     * 
     * @return
     *     The dataSollecitoRapporto
     */
    public String getDataSollecitoRapporto() {
        return dataSollecitoRapporto;
    }

    /**
     * 
     * @param dataSollecitoRapporto
     *     The data_sollecito_rapporto
     */
    public void setDataSollecitoRapporto(String dataSollecitoRapporto) {
        this.dataSollecitoRapporto = dataSollecitoRapporto;
    }

    /**
     * 
     * @return
     *     The dataInizioRimodulazione
     */
    public String getDataInizioRimodulazione() {
        return dataInizioRimodulazione;
    }

    /**
     * 
     * @param dataInizioRimodulazione
     *     The data_inizio_rimodulazione
     */
    public void setDataInizioRimodulazione(String dataInizioRimodulazione) {
        this.dataInizioRimodulazione = dataInizioRimodulazione;
    }

    /**
     * 
     * @return
     *     The dataFineRimodulazione
     */
    public String getDataFineRimodulazione() {
        return dataFineRimodulazione;
    }

    /**
     * 
     * @param dataFineRimodulazione
     *     The data_fine_rimodulazione
     */
    public void setDataFineRimodulazione(String dataFineRimodulazione) {
        this.dataFineRimodulazione = dataFineRimodulazione;
    }

    /**
     * 
     * @return
     *     The esitoSopralluogo
     */
    public String getEsitoSopralluogo() {
        return esitoSopralluogo;
    }

    /**
     * 
     * @param esitoSopralluogo
     *     The esito_sopralluogo
     */
    public void setEsitoSopralluogo(String esitoSopralluogo) {
        this.esitoSopralluogo = esitoSopralluogo;
    }

    /**
     * 
     * @return
     *     The tipoGestioneSopralluogo
     */
    public String getTipoGestioneSopralluogo() {
        return tipoGestioneSopralluogo;
    }

    /**
     * 
     * @param tipoGestioneSopralluogo
     *     The tipo_gestione_sopralluogo
     */
    public void setTipoGestioneSopralluogo(String tipoGestioneSopralluogo) {
        this.tipoGestioneSopralluogo = tipoGestioneSopralluogo;
    }

    /**
     * 
     * @return
     *     The noteSopralluogo
     */
    public String getNoteSopralluogo() {
        return noteSopralluogo;
    }

    /**
     * 
     * @param noteSopralluogo
     *     The note_sopralluogo
     */
    public void setNoteSopralluogo(String noteSopralluogo) {
        this.noteSopralluogo = noteSopralluogo;
    }

}
