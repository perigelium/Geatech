package ru.alexangan.developer.geatech.Adapters;

import ru.alexangan.developer.geatech.Models.ReportModelCaldaia;
import ru.alexangan.developer.geatech.Models.ReportModelClimatizzatore;
import ru.alexangan.developer.geatech.Models.ReportModelDomotica;
import ru.alexangan.developer.geatech.Models.ReportModelEmpty;
import ru.alexangan.developer.geatech.Models.ReportModelFotovoltaico;
import ru.alexangan.developer.geatech.Models.ReportModelPompaDiCalore;
import ru.alexangan.developer.geatech.Models.ReportModelSTermodinamico;
import ru.alexangan.developer.geatech.Models.ReportModelStorage;

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
                return ReportModelSTermodinamico.class;
            case "CALDAIE":
                return ReportModelCaldaia.class;
            case "CLIMATIZZAZIONE":
                return ReportModelClimatizzatore.class;
            case "FOTOVOLTAICO":
                return ReportModelFotovoltaico.class;
            case "DOMOTICA":
                return ReportModelDomotica.class;
            case "STORAGE":
                return ReportModelStorage.class;
            case "POMPA DI CALORE":
                return ReportModelPompaDiCalore.class;

            default:
                return ReportModelEmpty.class;
        }
    }
}
