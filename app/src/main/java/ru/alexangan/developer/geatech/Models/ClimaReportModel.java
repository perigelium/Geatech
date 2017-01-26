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
    String tipoDiEdificio, posizionamentoUnitaEsterna, tipologiaCostruttivaMurature, localiEOPianiDelledificio,
            noteSulLuoghoDiInstallazione, noteSulTipologiaDellImpianto, noteRelativeAlCollegamento;

    public ClimaReportModel(){};

    public ClimaReportModel(int id)
    {
        this.id = id;
    }

    public void setTipoDiEdificio(String tipoDiEdificio)
    {
        this.tipoDiEdificio = tipoDiEdificio;
    }

    public void setPosizionamentoUnitaEsterna(String posizionamentoUnitaEsterna)
    {
        this.posizionamentoUnitaEsterna = posizionamentoUnitaEsterna;
    }

    public void setTipologiaCostruttivaMurature(String tipologiaCostruttivaMurature)
    {
        this.tipologiaCostruttivaMurature = tipologiaCostruttivaMurature;
    }

    public void setLocaliEOPianiDelledificio(String localiEOPianiDelledificio)
    {
        this.localiEOPianiDelledificio = localiEOPianiDelledificio;
    }

    public void setNoteSulLuoghoDiInstallazione(String NoteSulLuoghoDiInstallazione)
    {
        this.noteSulLuoghoDiInstallazione = NoteSulLuoghoDiInstallazione;
    }

    public void setNoteSulTipologiaDellImpianto(String NoteSulTipologiaDellImpianto)
    {
        this.noteSulTipologiaDellImpianto = NoteSulTipologiaDellImpianto;
    }

    public void setNoteRelativeAlCollegamento(String NoteRelativeAlCollegamento)
    {
        this.noteRelativeAlCollegamento = NoteRelativeAlCollegamento;
    }

    public String getTipoDiEdificio()
    {
        return tipoDiEdificio;
    }

    public String getPosizionamentoUnitaEsterna()
    {
        return posizionamentoUnitaEsterna;
    }

    public String getTipologiaCostruttivaMurature()
    {
        return tipologiaCostruttivaMurature;
    }

    public String getLocaliEOPianiDelledificio()
    {
        return localiEOPianiDelledificio;
    }

    public String getNoteSulTipologiaDellImpianto()
    {
        return noteSulTipologiaDellImpianto;
    }

    public String getNoteRelativeAlCollegamento()
    {
        return noteRelativeAlCollegamento;
    }

    public int getId()
    {
        return id;
    }

    public String getNoteSulLuoghoDiInstallazione()
    {
        return noteSulLuoghoDiInstallazione;
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
