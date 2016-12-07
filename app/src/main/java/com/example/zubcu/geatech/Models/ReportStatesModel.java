package com.example.zubcu.geatech.Models;


public class ReportStatesModel
{
    public int reportCompletionState;
    boolean reportSent;
    public int generalInfoCompletionState;
    public int sendingReportTriesState;
    public int photoAddedState;

    private String dataOraProssimoTentativo;
    private String dataOraUltimoTentativo;
    private String dataOraRaportoCompletato;

    public String[] reportCompletionStatuses;
    public String[] generalInfoCompletionStatuses;
    public String[] sendingReportFailedStatuses;
    public String [] photoAddedStatuses;

    public int photoAddedQuant;

    private static ReportStatesModel ourInstance = new ReportStatesModel();

    public static ReportStatesModel getInstance()
    {
        return ourInstance;
    }

    public ReportStatesModel()
    {
        reportCompletionStatuses = new String[]{"Non iniziato", "Parziamente completato", "Completato"};
        generalInfoCompletionStatuses = new String[]{"Non iniziato", "Parziamente completato", "Completato"};
        sendingReportFailedStatuses = new String[]{"Invio falito per mancanza connesione dati"};
        photoAddedStatuses = new String[]{"Nessun fotografia", " foto inserite"};
        photoAddedQuant = 0;
        reportCompletionState = 0;
        generalInfoCompletionState = 0;
        sendingReportTriesState = 0;
        photoAddedState = 0;
        reportSent = false;
        dataOraRaportoCompletato = "2017-01-01 22:00:00";
        dataOraUltimoTentativo = "2017-01-01 19:00:00";
    }

    public String getReportCompletionStateString()
    {
        return reportCompletionStatuses[reportCompletionState];
    }

    public String getGeneralInfoCompletionStateString()
    {
        return generalInfoCompletionStatuses[generalInfoCompletionState];
    }

    public String getSendingReportTriesStateString()
    {
        return sendingReportFailedStatuses[sendingReportTriesState];
    }

    public int getPhotoAddedQuant()
    {
        return photoAddedQuant;
    }

    public String getPhotoAddedStateString()
    {
        return photoAddedStatuses[photoAddedState];
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
        return dataOraRaportoCompletato;
    }

    public void setDataOraRaportoCompletato(String dataOraRaportoCompletato)
    {
        this.dataOraRaportoCompletato = dataOraRaportoCompletato;
    }

    public String getDataOraUltimoTentativo()
    {
        return dataOraUltimoTentativo;
    }

    public void setDataOraUltimoTentativo(String dataOraUltimoTentativo)
    {
        this.dataOraUltimoTentativo = dataOraUltimoTentativo;
    }
}
