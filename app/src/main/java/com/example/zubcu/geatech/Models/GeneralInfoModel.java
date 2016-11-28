package com.example.zubcu.geatech.Models;

import android.util.Pair;

/**
 * Created by user on 11/15/2016.
 */

public class GeneralInfoModel
{
    public String clientName = "Nome di cliente";
    public String technicianName = "Nome di tecnico";
    public String clientPhone = "";
    public String serviceName = "Type of service";
    public String clientAddress = "Indirizzio di Cliente";
    public String latitude = "";
    public String longitude = "";
    public String altitude = "";

    public Pair<String, String> visitDate;
    public Pair<String, String> visitTime;
    public Pair<String, String> reportCompletionDate;
    public Pair<String, String> reportCompletionTime;

    public static String[] reportCompletionStatus = new String[] {"Non iniziato", "Parziamente completato", "Completato"};
    public static String[] generalInfoCompletionStatus = new String[] {"Non iniziato", "Parziamente completato", "Completato"};
    public static String[] sendingReportTriesStatus = new String[] {"Invio falito per mancanza connesione dati"};
    public int photoAddedQuant = 0;
    public String [] photoAddedStatus = new String[] {"Nessun fotografia", " foto inserite"};
    public String generalInfoStatus = "";
    public String technicalReportStatus = "";

    public GeneralInfoModel
            (String clientName, String clientPhone, String clientAddress,
             String latitude, String longitude, String altitude,
             String technicianName, String serviceName )
    {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.clientAddress = clientAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.technicianName = technicianName;
        this.serviceName = serviceName;
    }

    public String getVisitDay()
    {
        return this.visitDate != null ? this.visitDate.first : "";
    }

    public String getVisitMonth()
    {
        return this.visitDate != null ? this.visitDate.second : "";
    }

    public void setVisitDate(Pair<String, String> visitDate)
    {
        this.visitDate = visitDate;
    }

    public Pair<String, String> getVisitTime()
    {
        return visitTime;
    }

    public void setVisitTime(Pair<String, String> visitTime)
    {
        this.visitTime = visitTime;
    }

    public Pair<String, String> getReportCompletionDate()
    {
        return reportCompletionDate;
    }

    public void setReportCompletionDate(Pair<String, String> reportCompletionDate)
    {
        this.reportCompletionDate = reportCompletionDate;
    }

    public Pair<String, String> getReportCompletionTime()
    {
        return reportCompletionTime;
    }

    public void setReportCompletionTime(Pair<String, String> reportCompletionTime)
    {
        this.reportCompletionTime = reportCompletionTime;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getAltitude()
    {
        return altitude;
    }

    public void setAltitude(String altitude)
    {
        this.altitude = altitude;
    }

    public String getTechnicianName()
    {
        return technicianName;
    }

    public String getClientPhone()
    {
        return clientPhone;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public String getClientAddress()
    {
        return clientAddress;
    }

    public String getClientName()
    {
        return clientName;
    }
}
