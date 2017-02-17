package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 26.01.2017.
 */

public class ReportModelPompaDiCalore extends RealmObject
{
        @PrimaryKey
        int id;
        int idSopralluogo;

        public ReportModelPompaDiCalore(int id)
        {
                this.id = id;
        }

        public ReportModelPompaDiCalore()
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
