package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 12/8/2016.
 */

public class ClimaReportModel extends RealmObject
{
    @PrimaryKey
    int id;

    int idSopralluogo;

    String rgTypeOfBuilding, rgUnitOutdoorPositioning, rgWallsType, rgBuildingPlan;
    String etTypeOfBuilding, etUnitOutdoorPositioning, etWallsType, etBuildingPlan, etNoteInstallationPlace, etNoteExistingDev;

    public String getRgTypeOfBuilding()
    {
        return rgTypeOfBuilding;
    }

    public void setRgTypeOfBuilding(String rgTypeOfBuilding)
    {
        this.rgTypeOfBuilding = rgTypeOfBuilding;
    }

    public String getRgUnitOutdoorPositioning()
    {
        return rgUnitOutdoorPositioning;
    }

    public void setRgUnitOutdoorPositioning(String rgUnitOutdoorPositioning)
    {
        this.rgUnitOutdoorPositioning = rgUnitOutdoorPositioning;
    }

    public String getRgWallsType()
    {
        return rgWallsType;
    }

    public void setRgWallsType(String rgWallsType)
    {
        this.rgWallsType = rgWallsType;
    }

    public String getRgBuildingPlan()
    {
        return rgBuildingPlan;
    }

    public void setRgBuildingPlan(String rgBuildingPlan)
    {
        this.rgBuildingPlan = rgBuildingPlan;
    }

    public String getEtTypeOfBuilding()
    {
        return etTypeOfBuilding;
    }

    public void setEtTypeOfBuilding(String etTypeOfBuilding)
    {
        this.etTypeOfBuilding = etTypeOfBuilding;
    }

    public String getEtUnitOutdoorPositioning()
    {
        return etUnitOutdoorPositioning;
    }

    public void setEtUnitOutdoorPositioning(String etUnitOutdoorPositioning)
    {
        this.etUnitOutdoorPositioning = etUnitOutdoorPositioning;
    }

    public String getEtWallsType()
    {
        return etWallsType;
    }

    public void setEtWallsType(String etWallsType)
    {
        this.etWallsType = etWallsType;
    }

    public String getEtBuildingPlan()
    {
        return etBuildingPlan;
    }

    public void setEtBuildingPlan(String etBuildingPlan)
    {
        this.etBuildingPlan = etBuildingPlan;
    }

    public String getEtNoteInstallationPlace()
    {
        return etNoteInstallationPlace;
    }

    public void setEtNoteInstallationPlace(String etNoteInstallationPlace)
    {
        this.etNoteInstallationPlace = etNoteInstallationPlace;
    }

    public String getEtNoteExistingDev()
    {
        return etNoteExistingDev;
    }

    public void setEtNoteExistingDev(String etNoteExistingDev)
    {
        this.etNoteExistingDev = etNoteExistingDev;
    }

    public ClimaReportModel(){};

    public ClimaReportModel(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public int getIdSopralluogo()
    {
        return idSopralluogo;
    }

    public void setIdSopralluogo(int idSopralluogo)
    {
        this.idSopralluogo = idSopralluogo;
    }
}
