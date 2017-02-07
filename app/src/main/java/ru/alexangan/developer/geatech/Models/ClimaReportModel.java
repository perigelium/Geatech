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

    private String id_item_72;
    private String id_item_73;
    private String id_item_74;
    private String id_item_75;
    private String id_item_76;
    private String id_item_77;
    private String id_item_78;

    public ClimaReportModel(){};

    public ClimaReportModel(int id, int idSopralluogo)
    {
        this.id = id;
        this.idSopralluogo = idSopralluogo;
    }

    public String getId_item_75()
    {
        return id_item_75;
    }

    public void setId_item_75(String id_item_75)
    {
        this.id_item_75 = id_item_75;
    }

    public String getId_item_72()
    {
        return id_item_72;
    }

    public void setId_item_72(String id_item_72)
    {
        this.id_item_72 = id_item_72;
    }

    public String getId_item_74()
    {
        return id_item_74;
    }

    public void setId_item_74(String id_item_74)
    {
        this.id_item_74 = id_item_74;
    }

    public String getId_item_73()
    {
        return id_item_73;
    }

    public void setId_item_73(String id_item_73)
    {
        this.id_item_73 = id_item_73;
    }

    public String getId_item_76()
    {
        return id_item_76;
    }

    public void setId_item_76(String id_item_76)
    {
        this.id_item_76 = id_item_76;
    }

    public String getId_item_77()
    {
        return id_item_77;
    }

    public void setId_item_77(String id_item_77)
    {
        this.id_item_77 = id_item_77;
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

    public String getId_item_78()
    {
        return id_item_78;
    }

    public void setId_item_78(String id_item_78)
    {
        this.id_item_78 = id_item_78;
    }
}
