package ru.alexangan.developer.geatech.Models;


import io.realm.RealmList;
import io.realm.RealmObject;

public class ReportStates extends RealmObject
{
    private int reportCompletionState;
    public static int REPORT_NON_INITIATED = 0;
    public static int REPORT_INITIATED = 1;
    public static int REPORT_HALF_COMPLETED = 2;
    public static int REPORT_ALMOST_COMPLETED = 3;
    public static int REPORT_COMPLETED = 4;

    private int general_info_coords_set;
    private int general_info_datetime_set;

    public static int GENERAL_INFO_COORDS_NOT_SET = 0;
    public static int GENERAL_INFO_DATETIME_SET = 1;
    public static int GENERAL_INFO_COORDS_SET = 2;
    public static int GENERAL_INFO_DATETIME_AND_COORDS_SET = 3;

    private boolean triedToSendReport;

    private int photosAddedNumber;
    public static int PHOTOS_MIN_ADDED = 3;

    private RealmList<RealmString> reportCompletionStatuses;
    private RealmList<RealmString> generalInfoCompletionStatuses;
    private RealmList<RealmString> sendingReportFailedStatuses;
    private RealmList<RealmString> photoAddedStatuses;

    public ReportStates()
    {
    }

    public ReportStates(int general_info_datetime_set)
    {
        this.general_info_datetime_set = general_info_datetime_set;

        reportCompletionStatuses = new RealmList<>();
        reportCompletionStatuses.add(new RealmString("Non iniziato"));
        reportCompletionStatuses.add(new RealmString("Iniziato"));
        reportCompletionStatuses.add(new RealmString("Parziamente completato"));
        reportCompletionStatuses.add(new RealmString("Quasi completato"));
        reportCompletionStatuses.add(new RealmString("Completato"));

        generalInfoCompletionStatuses = new RealmList<>();
        generalInfoCompletionStatuses.add(new RealmString("Non iniziato"));
        generalInfoCompletionStatuses.add(new RealmString("Iniziato"));
        generalInfoCompletionStatuses.add(new RealmString("Iniziato"));
        generalInfoCompletionStatuses.add(new RealmString("Completato"));

        sendingReportFailedStatuses = new RealmList<>();
        sendingReportFailedStatuses.add(new RealmString("Invio falito per mancanza connesione dati"));

        photoAddedStatuses = new RealmList<>();
        photoAddedStatuses.add(new RealmString("Nessun fotografia"));
        photoAddedStatuses.add(new RealmString(" foto inserite"));

        reportCompletionState = REPORT_NON_INITIATED;
        general_info_coords_set = 0;
        photosAddedNumber = 0;
        triedToSendReport = false;
    }

    public RealmString getReportCompletionStateString()
    {
        return reportCompletionStatuses.get(reportCompletionState);
    }

    public RealmString getGeneralInfoCompletionStateString()
    {
        return generalInfoCompletionStatuses.get(general_info_coords_set + general_info_datetime_set);
    }

    public RealmString getPhotoAddedNumberString(int photoAddedNumber)
    {
        if (photoAddedNumber != 0)
        {
            return photoAddedStatuses.get(1);
        } else
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

    public int getGeneralInfoCompletionState()
    {
        return general_info_coords_set + general_info_datetime_set;
    }

    public void setGeneral_info_datetime_set(int general_info_datetime_set)
    {
        this.general_info_datetime_set = general_info_datetime_set;
    }

    public void setGeneral_info_coords_set(int general_info_coords_set)
    {

        this.general_info_coords_set = general_info_coords_set;
    }

    public boolean hasTriedToSendReport()
    {
        return triedToSendReport;
    }

    public void setTriedToSendReport(boolean triedToSendReport)
    {
        this.triedToSendReport = triedToSendReport;
    }

    public int getPhotosAddedNumber()
    {
        return photosAddedNumber;
    }

    public boolean isTriedToSendReport()
    {
        return triedToSendReport;
    }

    public RealmList<RealmString> getReportCompletionStatuses()
    {
        return reportCompletionStatuses;
    }

    public RealmList<RealmString> getGeneralInfoCompletionStatuses()
    {
        return generalInfoCompletionStatuses;
    }

    public RealmList<RealmString> getSendingReportFailedStatuses()
    {
        return sendingReportFailedStatuses;
    }

    public RealmList<RealmString> getPhotoAddedStatuses()
    {
        return photoAddedStatuses;
    }

    public void setPhotosAddedNumber(int photosAddedNumber)
    {
        this.photosAddedNumber = photosAddedNumber;
    }

    public int getGeneral_info_coords_set()
    {
        return general_info_coords_set;
    }
}
