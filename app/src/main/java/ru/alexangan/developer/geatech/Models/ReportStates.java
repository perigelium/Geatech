package ru.alexangan.developer.geatech.Models;


import io.realm.RealmList;
import io.realm.RealmObject;

public class ReportStates extends RealmObject
{
    int id;
    public int reportCompletionState;
    boolean reportSent;
    public int generalInfoCompletionState;
    public int sendingReportTriesState;
    public int photoAddedState;

    private String dataOraProssimoTentativo;
    private String dataOraUltimoTentativo;
    private String dataOraRaportoCompilato;
    private String dataOraRaportoInviato;

    public RealmList<RealmString> reportCompletionStatuses;
    public RealmList<RealmString> generalInfoCompletionStatuses;
    public RealmList<RealmString> sendingReportFailedStatuses;
    public RealmList<RealmString> photoAddedStatuses;

    public int photoAddedQuant;

    //private static ReportStates ourInstance = new ReportStates();

    /*    public static ReportStates getInstance()
        {
            return ourInstance;
        }*/

    public ReportStates() {}

    public ReportStates(int id)
    {
        this.id = id;
        reportCompletionStatuses = new RealmList<>();
        reportCompletionStatuses.add(new RealmString(id, "Non iniziato") );
        reportCompletionStatuses.add(new RealmString(id, "Parziamente completato") );
        reportCompletionStatuses.add(new RealmString(id, "Completato") );

        generalInfoCompletionStatuses = new RealmList<>();
        generalInfoCompletionStatuses.add(new RealmString(id, "Non iniziato") );
        generalInfoCompletionStatuses.add(new RealmString(id, "Parziamente completato") );
        generalInfoCompletionStatuses.add(new RealmString(id, "Completato") );

        sendingReportFailedStatuses = new RealmList<>();
        sendingReportFailedStatuses.add(new RealmString(id, "Invio falito per mancanza connesione dati") );

        photoAddedStatuses = new RealmList<>();
        photoAddedStatuses.add(new RealmString(id, "Nessun fotografia") );
        photoAddedStatuses.add(new RealmString(id, " foto inserite") );

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

    public int getId()
    {
        return id;
    }
}
