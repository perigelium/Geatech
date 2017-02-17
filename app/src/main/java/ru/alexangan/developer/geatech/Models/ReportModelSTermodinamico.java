package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 26.01.2017.
 */

public class ReportModelSTermodinamico extends RealmObject
{
        @PrimaryKey
        int id;
        int idSopralluogo;

        public ReportModelSTermodinamico(int id)
        {
                this.id = id;
        }

        public ReportModelSTermodinamico()
        {
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
