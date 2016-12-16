package ru.alexangan.developer.geatech.Models;


import io.realm.RealmList;
import io.realm.RealmObject;

public class ReportStatesModel extends RealmObject
{
    public int reportCompletionState;
    boolean reportSent;
    public int generalInfoCompletionState;
    public int sendingReportTriesState;
    public int photoAddedState;

    public double coordNord;
    public double coordEst;

    public int altitude;

    private String dataOraProssimoTentativo;
    private String dataOraUltimoTentativo;
    private String dataOraRaportoCompilato;
    private String dataOraRaportoInviato;

    public RealmList<RealmString> reportCompletionStatuses;
    public RealmList<RealmString> generalInfoCompletionStatuses;
    public RealmList<RealmString> sendingReportFailedStatuses;
    public RealmList<RealmString> photoAddedStatuses;

    public int photoAddedQuant;

    private static ReportStatesModel ourInstance = new ReportStatesModel();

    public static ReportStatesModel getInstance()
    {
        return ourInstance;
    }

    public ReportStatesModel()
    {
        reportCompletionStatuses = new RealmList<>();
        reportCompletionStatuses.add(new RealmString("Non iniziato") );
        reportCompletionStatuses.add(new RealmString("Parziamente completato") );
        reportCompletionStatuses.add(new RealmString("Completato") );

        generalInfoCompletionStatuses = new RealmList<>();
        generalInfoCompletionStatuses.add(new RealmString("Non iniziato") );
        generalInfoCompletionStatuses.add(new RealmString("Parziamente completato") );
        generalInfoCompletionStatuses.add(new RealmString("Completato") );

        sendingReportFailedStatuses = new RealmList<>();
        sendingReportFailedStatuses.add(new RealmString("Invio falito per mancanza connesione dati") );

        photoAddedStatuses = new RealmList<>();
        photoAddedStatuses.add(new RealmString("Nessun fotografia") );
        photoAddedStatuses.add(new RealmString(" foto inserite") );

        //reportCompletionStatuses = new String[]{"Non iniziato", "Parziamente completato", "Completato"};
        //generalInfoCompletionStatuses = new String[]{"Non iniziato", "Parziamente completato", "Completato"};
        //sendingReportFailedStatuses = new String[]{"Invio falito per mancanza connesione dati"};
        //photoAddedStatuses = new String[]{"Nessun fotografia", " foto inserite"};

        photoAddedQuant = 0;
        reportCompletionState = 0;
        generalInfoCompletionState = 0;
        sendingReportTriesState = 0;
        photoAddedState = 0;
        reportSent = false;
        dataOraRaportoCompilato = "2017-01-01 22:00:00";
        dataOraRaportoInviato = "2017-01-01 23:00:00";
        dataOraUltimoTentativo = "19:00";
        dataOraProssimoTentativo = "21:00";
    }

    public RealmString getReportCompletionStateString()
    {
        return reportCompletionStatuses.get(reportCompletionState);
    }

    public RealmString getGeneralInfoCompletionStateString()
    {
        return generalInfoCompletionStatuses.get(generalInfoCompletionState);
    }

    public RealmString getSendingReportTriesStateString()
    {
        return sendingReportFailedStatuses.get(sendingReportTriesState);
    }

    public int getPhotoAddedQuant()
    {
        return photoAddedQuant;
    }

    public RealmString getPhotoAddedStateString()
    {
        return photoAddedStatuses.get(photoAddedState);
    }

    public void setReportCompletionState(int reportCompletionState)
    {
        this.reportCompletionState = reportCompletionState;
    }

    public void setReportSent(boolean reportSent)
    {
        this.reportSent = reportSent;
    }

    public int getReportCompletionState()
    {
        return reportCompletionState;
    }

    public boolean isReportSent()
    {
        return reportSent;
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

    public double getCoordNord()
    {
        return coordNord;
    }

    public void setCoordNord(double coordNord)
    {
        this.coordNord = coordNord;
    }

    public double getCoordEst()
    {
        return coordEst;
    }

    public void setCoordEst(double coordEst)
    {
        this.coordEst = coordEst;
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
