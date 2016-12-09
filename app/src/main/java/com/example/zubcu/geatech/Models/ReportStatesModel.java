package com.example.zubcu.geatech.Models;


public class ReportStatesModel
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
        dataOraRaportoCompilato = "2017-01-01 22:00:00";
        dataOraRaportoInviato = "2017-01-01 23:00:00";
        dataOraUltimoTentativo = "19:00";
        dataOraProssimoTentativo = "21:00";
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
