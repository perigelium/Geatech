package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

/**
 * Created by user on 12/8/2016.
 */

public class ClimatizzazioneModel extends RealmObject
{

    String tipoDiEdificio, posizionamentoUnitaEsterna, tipologiaCostruttivaMurature, localiEOPianiDelledificio,
            NoteSulLuoghoDiInstallazione, NoteSulTipologiaDellImpianto, NoteRelativeAlCollegamento, noteSulTipologiaDellImpianto,
            noteRelativeAlCollegamento;

    public ClimatizzazioneModel()
    {

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

    public void setNoteSulLuoghoDiInstallazione()
    {
        this.NoteSulLuoghoDiInstallazione = NoteSulLuoghoDiInstallazione;
    }

    public void setNoteSulTipologiaDellImpianto()
    {
        this.NoteSulTipologiaDellImpianto = NoteSulTipologiaDellImpianto;
    }

    public void setNoteRelativeAlCollegamento()
    {
        this.NoteRelativeAlCollegamento = NoteRelativeAlCollegamento;
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
}
