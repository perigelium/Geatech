package ru.alexangan.developer.geatech.Adapters;

import ru.alexangan.developer.geatech.Models.CaldaiaReportModel;
import ru.alexangan.developer.geatech.Models.ClimaReportModel;
import ru.alexangan.developer.geatech.Models.DomoticaReportModel;
import ru.alexangan.developer.geatech.Models.FotovoltaicoReportModel;
import ru.alexangan.developer.geatech.Models.PompaDiCaloreReportModel;
import ru.alexangan.developer.geatech.Models.SolareTermodinamicoReportModel;
import ru.alexangan.developer.geatech.Models.StorageReportModel;

/**
 * Created by user on 26.01.2017.
 */

public class ModelsMapping
{
    public static Class assignClassModel(String productType)
    {
        switch (productType)
        {
            case "SOLARE TERMODINAMICO":
                return SolareTermodinamicoReportModel.class;
            case "CALDAIE":
                return CaldaiaReportModel.class;
            case "CLIMATIZZAZIONE":
                return ClimaReportModel.class;
            case "FOTOVOLTAICO":
                return FotovoltaicoReportModel.class;
            case "DOMOTICA":
                return DomoticaReportModel.class;
            case "STORAGE":
                return StorageReportModel.class;
            case "POMPA DI CALORE":
                return PompaDiCaloreReportModel.class;

            default:
                return null;
        }
    }
}
