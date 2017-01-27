package ru.alexangan.developer.geatech.Adapters;

import ru.alexangan.developer.geatech.Models.CaldaieReportModel;
import ru.alexangan.developer.geatech.Models.ClimaReportModel;

/**
 * Created by user on 26.01.2017.
 */

public class ModelsMapping
{
    public static Class assignClassModel(String productType)
    {
        switch (productType)
        {
            case "CLIMATIZZAZIONE":
                return ClimaReportModel.class;
            case "CALDAIE":
                return CaldaieReportModel.class;
/*            case "SOLARE TERMODINAMICO":
                return SolareTermodinamicoReportModel.class;
            case "FOTOVOLTAICO":
                return FotovoltaicoReportModel.class;
            case "LED":
                return LedReportModel.class;
            case "POMPA DI CALORE":
                return PompaDiCaloreModel.class;
            case "STORAGE":
                return StorageReportModel.class;
            case "DOMOTICA":
                return DomoticaReportModel.class;*/


            default:
                return null;
        }
    }
}
