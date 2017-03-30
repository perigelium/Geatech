package ru.alexangan.developer.geatech.Models;


import io.realm.RealmList;
import io.realm.RealmObject;

public class ReportStates extends RealmObject
{
    int company_id;
    int tech_id;

    private int id_sopralluogo;
    private int id_rapporto_sopralluogo;
    private String data_ora_presa_appuntamento;
    private String data_ora_sopralluogo;

    private String productType;

    private String clientName;
    private String clientAddress;
    private String clientMobile;

    private double latitudine;
    private double longitudine;
    private int altitudine;

    private int reportCompletionState;
    public static int REPORT_STATE_INDETERMINATE = -1;
    public static int REPORT_NON_INITIATED = 0;
    public static int REPORT_INITIATED = 1;
    public static int REPORT_PARTIALLY_COMPLETED = 2;
    public static int REPORT_ALMOST_COMPLETED = 3;
    public static int REPORT_COMPLETED = 4;

    private int generalInfoCompletionState; // data ora appuntamento era inserita
    public static int COORDS_SET = 2;

    private int sendingReportTriesState;

    private String dataOraProssimoTentativo;
    private String dataOraUltimoTentativo;
    private String data_ora_compilazione_rapporto;
    private int completion_percent;
    private String data_ora_invio_rapporto;

    private String nome_tecnico;
    private String note_tecnico;
    private int photoAddedNumber; // immagini numero
    public static int PHOTOS_MIN_ADDED = 3;

    private RealmList<RealmString> reportCompletionStatuses;
    private RealmList<RealmString> generalInfoCompletionStatuses;
    private RealmList<RealmString> sendingReportFailedStatuses;
    private RealmList<RealmString> photoAddedStatuses;

    public ReportStates() {}

    public ReportStates(int company_id, int tech_id, int id_sopralluogo, int id_rapporto_sopralluogo)
    {
        this.company_id = company_id;
        this.tech_id = tech_id;
        this.id_sopralluogo = id_sopralluogo;
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;


        reportCompletionStatuses = new RealmList<>();
        reportCompletionStatuses.add(new RealmString( "Non iniziato") );
        reportCompletionStatuses.add(new RealmString( "Iniziato") );
        reportCompletionStatuses.add(new RealmString( "Parziamente completato") );
        reportCompletionStatuses.add(new RealmString( "Quasi completato") );
        reportCompletionStatuses.add(new RealmString( "Completato") );

        generalInfoCompletionStatuses = new RealmList<>();
        generalInfoCompletionStatuses.add(new RealmString( "Non iniziato") );
        //generalInfoCompletionStatuses.add(new RealmString( "Iniziato") );
        generalInfoCompletionStatuses.add(new RealmString( "Parziamente completato") );
        generalInfoCompletionStatuses.add(new RealmString( "Completato") );

        sendingReportFailedStatuses = new RealmList<>();
        sendingReportFailedStatuses.add(new RealmString( "Invio falito per mancanza connesione dati") );

        photoAddedStatuses = new RealmList<>();
        photoAddedStatuses.add(new RealmString( "Nessun fotografia") );
        photoAddedStatuses.add(new RealmString( " foto inserite") );


        photoAddedNumber = 0;
        reportCompletionState = -1;
        generalInfoCompletionState = 0;
        sendingReportTriesState = 0;
        dataOraUltimoTentativo = "19:00";
        dataOraProssimoTentativo = "21:00";
        nome_tecnico = "";
        latitudine = 0;
        longitudine = 0;
        altitudine = -999;
    }

    public RealmString getReportCompletionStateString(int reportCompletionState)
    {
        return reportCompletionStatuses.get(reportCompletionState);
    }

    public RealmString getGeneralInfoCompletionStateString(int generalInfoCompletionState)
    {
        return generalInfoCompletionStatuses.get(generalInfoCompletionState);
    }

    public RealmString getSendingReportTriesStateString(int sendingReportTriesState)
    {
        return sendingReportFailedStatuses.get(sendingReportTriesState);
    }

    public RealmString getPhotoAddedNumberString(int photoAddedNumber)
    {
        if(photoAddedNumber != 0)
        {
            return photoAddedStatuses.get(1);
        }
        else
        {
            return photoAddedStatuses.get(0);
        }
    }

    public void setReportCompletionState(int reportCompletionState)
    {
        this.reportCompletionState = reportCompletionState;
    }

    public int getReportCompletionState()
    {
        return reportCompletionState;
    }

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

    public String getDataOraProssimoTentativo()
    {
        return dataOraProssimoTentativo;
    }

    public void dataOraProssimoTentativo(String dataOraProssimoTentativo)
    {
        this.dataOraProssimoTentativo = dataOraProssimoTentativo;
    }

    public String getData_ora_presa_appuntamento()
    {
        return data_ora_presa_appuntamento;
    }

    public void setData_ora_presa_appuntamento(String data_ora_presa_appuntamento)
    {
        this.data_ora_presa_appuntamento = data_ora_presa_appuntamento;
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

    public int getAltitudine()
    {
        return altitudine;
    }

    public void setAltitudine(int altitudine)
    {
        this.altitudine = altitudine;
    }

    public int getGeneralInfoCompletionState()
    {
        return generalInfoCompletionState;
    }

    public int getPhotoAddedNumber()
    {
        return photoAddedNumber;
    }

    public void setPhotoAddedNumber(int photoAddedNumber)
    {
        this.photoAddedNumber = photoAddedNumber;
    }

    public int getSendingReportTriesState()
    {
        return sendingReportTriesState;
    }

    public void setSendingReportTriesState(int sendingReportTriesState)
    {
        this.sendingReportTriesState = sendingReportTriesState;
    }

    public void setGeneralInfoCompletionState(int generalInfoCompletionState)
    {
        this.generalInfoCompletionState = generalInfoCompletionState;
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

    public String getData_ora_sopralluogo()
    {
        return data_ora_sopralluogo;
    }

    public void setData_ora_sopralluogo(String data_ora_sopralluogo)
    {
        this.data_ora_sopralluogo = data_ora_sopralluogo;
    }

    public int getTech_id()
    {
        return tech_id;
    }

    public int getCompletion_percent()
    {
        return completion_percent;
    }

    public void setCompletion_percent(int completion_percent)
    {
        this.completion_percent = completion_percent;
    }

    public String getClientName()
    {
        return clientName;
    }

    public void setClientName(String clientName)
    {
        this.clientName = clientName;
    }

    public String getClientAddress()
    {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress)
    {
        this.clientAddress = clientAddress;
    }

    public String getClientMobile()
    {
        return clientMobile;
    }

    public void setClientMobile(String clientMobile)
    {
        this.clientMobile = clientMobile;
    }

    public String getProductType()
    {
        return productType;
    }

    public void setProductType(String productType)
    {
        this.productType = productType;
    }
}
