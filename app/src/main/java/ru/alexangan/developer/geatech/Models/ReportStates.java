package ru.alexangan.developer.geatech.Models;


import io.realm.RealmList;
import io.realm.RealmObject;

public class ReportStates extends RealmObject
{
    int id;
    int visitId;
    private int id_rapporto_sopralluogo;
    private int idSopralluogo;
    private String dataOraSopralluogo;

    private double latitude; // latitudine
    private double longitude; // longitudine
    private int altitude; // altitudine
    private int reportCompletionState;
    private int generalInfoCompletionState; // data ora appuntamento era inserita

    private int sendingReportTriesState;

    private String dataOraProssimoTentativo;
    private String dataOraUltimoTentativo;
    private String dataOraRaportoCompilato; // data_ora_compilazione_rapporto
    private String dataOraRaportoInviato; // data_ora_invio_rapporto
    private String nome_tecnico;
    private String note_tecnico;
    private int photoAddedNumber; // immagini numero

    private RealmList<RealmString> reportCompletionStatuses;
    private RealmList<RealmString> generalInfoCompletionStatuses;
    private RealmList<RealmString> sendingReportFailedStatuses;
    private RealmList<RealmString> photoAddedStatuses;

    public ReportStates() {}

    public ReportStates(int id, int visitId)
    {
        this.id = id;
        id_rapporto_sopralluogo = id;
        this.visitId = visitId;
        reportCompletionStatuses = new RealmList<>();
        reportCompletionStatuses.add(new RealmString( "Non iniziato") );
        reportCompletionStatuses.add(new RealmString( "Iniziato") );
        reportCompletionStatuses.add(new RealmString( "Parziamente completato") );
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
        reportCompletionState = 0;
        generalInfoCompletionState = 0;
        sendingReportTriesState = 0;
        dataOraUltimoTentativo = "19:00";
        dataOraProssimoTentativo = "21:00";
    }

    public void setId(int id)
    {
        this.id = id;
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

    public String getDataOraProssimoTentativo()
    {
        return dataOraProssimoTentativo;
    }

    public void dataOraProssimoTentativo(String dataOraProssimoTentativo)
    {
        this.dataOraProssimoTentativo = dataOraProssimoTentativo;
    }

    public int getId()
    {
        return id;
    }

    public String getDataOraSopralluogo()
    {
        return dataOraSopralluogo;
    }

    public void setDataOraSopralluogo(String dataOraSopralluogo)
    {
        this.dataOraSopralluogo = dataOraSopralluogo;
    }

    public int getVisitId()
    {
        return visitId;
    }

    public void setVisitId(int visitId)
    {
        this.visitId = visitId;
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
}
