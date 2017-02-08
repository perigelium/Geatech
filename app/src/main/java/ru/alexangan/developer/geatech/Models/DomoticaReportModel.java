package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 26.01.2017.
 */

public class DomoticaReportModel extends RealmObject
{
        @PrimaryKey
        int id;
        int idSopralluogo;

    private String id_item_139;
        private String id_item_140;
        private String id_item_141;
        private String id_item_142;
        private String id_item_143;
        private String id_item_144;
        private String id_item_145;
        private String id_item_146;
        private String id_item_147;
        private String id_item_148;
        private String id_item_149;
        private String id_item_150;
        private String id_item_151;
        private String id_item_152;

    public String getId_item_139()
    {
        return id_item_139;
    }

    public void setId_item_139(String id_item_139)
    {
        this.id_item_139 = id_item_139;
    }

    public String getId_item_140()
    {
        return id_item_140;
    }

    public void setId_item_140(String id_item_140)
    {
        this.id_item_140 = id_item_140;
    }

        public String getId_item_141()
        {
                return id_item_141;
        }

        public void setId_item_141(String id_item_141)
        {
                this.id_item_141 = id_item_141;
        }

        public String getId_item_142()
        {
                return id_item_142;
        }

        public void setId_item_142(String id_item_142)
        {
                this.id_item_142 = id_item_142;
        }

        public String getId_item_143()
        {
                return id_item_143;
        }

        public void setId_item_143(String id_item_143)
        {
                this.id_item_143 = id_item_143;
        }

        public String getId_item_144()
        {
                return id_item_144;
        }

        public void setId_item_144(String id_item_144)
        {
                this.id_item_144 = id_item_144;
        }

        public String getId_item_145()
        {
                return id_item_145;
        }

        public void setId_item_145(String id_item_145)
        {
                this.id_item_145 = id_item_145;
        }

        public String getId_item_146()
        {
                return id_item_146;
        }

        public void setId_item_146(String id_item_146)
        {
                this.id_item_146 = id_item_146;
        }

        public String getId_item_147()
        {
                return id_item_147;
        }

        public void setId_item_147(String id_item_147)
        {
                this.id_item_147 = id_item_147;
        }

        public String getId_item_148()
        {
                return id_item_148;
        }

        public void setId_item_148(String id_item_148)
        {
                this.id_item_148 = id_item_148;
        }

        public String getId_item_149()
        {
                return id_item_149;
        }

        public void setId_item_149(String id_item_149)
        {
                this.id_item_149 = id_item_149;
        }

        public String getId_item_150()
        {
                return id_item_150;
        }

        public void setId_item_150(String id_item_150)
        {
                this.id_item_150 = id_item_150;
        }

        public String getId_item_151()
        {
                return id_item_151;
        }

        public void setId_item_151(String id_item_151)
        {
                this.id_item_151 = id_item_151;
        }

        public String getId_item_152()
        {
                return id_item_152;
        }

        public void setId_item_152(String id_item_152)
        {
                this.id_item_152 = id_item_152;
        }

        public DomoticaReportModel(int id, int idSopralluogo)
        {
            this.id = id;
            this.idSopralluogo = idSopralluogo;
        }

        public DomoticaReportModel()
        {
        }

        public int getIdSopralluogo()
        {
                return idSopralluogo;
        }
}
