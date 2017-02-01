package ru.alexangan.developer.geatech.Models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;
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

    private String etTypeOfBuilding;
    private String etUnitOutdoorPositioning;
    private String etWallsType;
    private String etNoteInstallationPlace;
    private String etNoteExistingDev;

    private String etBuildingPlan;

    private String etAltroBuildingPlan;

    public ClimaReportModel(){};

    public ClimaReportModel(int id, int idSopralluogo)
    {
        this.id = id;
        this.idSopralluogo = idSopralluogo;
    }

    public String getEtAltroBuildingPlan()
    {
        return etAltroBuildingPlan;
    }

    public void setEtAltroBuildingPlan(String etAltroBuildingPlan)
    {
        this.etAltroBuildingPlan = etAltroBuildingPlan;
    }

    public String getEtBuildingPlan()
    {
        return etBuildingPlan;
    }

    public void setEtBuildingPlan(String etBuildingPlan)
    {
        this.etBuildingPlan = etBuildingPlan;
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
