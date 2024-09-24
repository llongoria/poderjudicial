package mx.com.wcontact.poderjudicial.bl;

public final class JSONKeys {

    /**
     * Juzgado (Z.M): JUZGADO FAMILIAR ESPECIALIZADO EN NIÑAS, NIÑOS Y ADOLESENTES
     * https://api.cjj.gob.mx/bulletin/forean_date?judged=FNI&date=2024-09-18&url=bulletin/forean_date
     * {
     *             "ID_LIBGOB": "2173046.000000000000000000000000000000",
     *             "ID_EXPEDIENTE": "486041.000000000000000000000000000000",
     *             "FECHA_PROMOCION": "2024-08-01T00:00:00.000Z",
     *             "EXTRACTO": "OTRO",
     *             "TIPO": "PRINCIPAL",
     *             "DEMANDA_INI": "NO",
     *             "FECHA_RESERVADO": null,
     *             "OBSERVACIONES": "SE DESAHOGA ESCUCHA DEL NIÑO Y AUDIENCIA DE RATIFICACIÓN.",
     *             "ADJUNTOS": null,
     *             "ID_LISTA": "109687.000000000000000000000000000000",
     *             "ID_JUZGADO": "74.000000000000000000000000000000",
     *             "SINTESIS": null,
     *             "BOLETIN3": "OTRO",
     *             "BOLETIN2": "SE DESAHOGA ESCUCHA DEL NIÑO Y AUDIENCIA DE RATIFICACIÓN.",
     *             "FCH_ACU": "2024-09-18T00:00:00.000Z",
     *             "EXP": "410/2024",
     *             "DESCRIP": "JURISDICCIÓN VOLUNTARIA",
     *             "act_names": null,
     *             "dem_names": null,
     *             "dem_pro": "MINERVA LIZET PEREZ GONZALEZ, LUIS ENRIQUE PUGA ARELLANO",
     *             "aut_names": null,
     *             "CLAVE_JUZGADO": "FNI"
     *         },
     */
    public static final String[] ZM_FNI = {"ID_LIBGOB","ID_EXPEDIENTE", "FECHA_PROMOCION", "EXTRACTO", "TIPO", "DEMANDA_INI",
            "FECHA_RESERVADO", "OBSERVACIONES", "ADJUNTOS","ID_LISTA","ID_JUZGADO","SINTESIS", "BOLETIN3", "BOLETIN2",
            "FCH_ACU","EXP", "DESCRIP", "act_names", "dem_names","dem_pro", "aut_names", "CLAVE_JUZGADO"};


    /**
     * Juzgado (Z.M): JUZGADO DECIMO PRIMERO ESPECIALIZADO DE LO FAMILIAR
     * https://api.cjj.gob.mx/bulletin/forean_date?judged=XF1&date=2024-09-18&url=bulletin/forean_date
     * {
     *             "EXP": "1136/2024",
     *             "CVE_JUZ": "XF1",
     *             "FCH_PRO": "2024-09-12T00:00:00.000Z",
     *             "FCH_ACU": "2024-09-18T00:00:00.000Z",
     *             "BOLETIN3": "SE RECIBE ESCRITO INICIAL DE DEMANDA EL DÍA 12 DOCE DE SEPTIEMBRE DE 2024 DOS MIL VEINTICUATRO, SE PREVIENE. SEÑALA DOMICILIO PROCESAL Y AUTORIZADOS.",
     *             "TIPO": null,
     *             "NOTIFICACI": "B",
     *             "DI": "NO",
     *             "FCH_RES": null,
     *             "CVE_JUI": "29.000000000000000000000000000000",
     *             "DESCRIP": "CIVIL ORDINARIO",
     *             "ID_EXPEDIENTE": "7010.000000000000000000000000000000",
     *             "act_names": "ERIKA JOSELINE HERNANDEZ GRACIANO",
     *             "dem_names": "OCTAVIO VALENTIN NORIEGA ORTIZ"
     *         },
     */
    public static final String[] ZM_CIVIL_ORDINARIO = { "EXP","CVE_JUZ","FCH_PRO","FCH_ACU","BOLETIN3","TIPO","NOTIFICACI",
            "DI","FCH_RES","CVE_JUI","DESCRIP","ID_EXPEDIENTE","act_names","dem_names"
    };

    /**
     * Juzgado (Z.M): JUZGADO SECTO DE LO MERCANTIL
     * https://api.cjj.gob.mx/bulletin/zmg_date?judged=M06&date=2024-09-18&url=bulletin/zmg_date
     * {
     *             "EXP": "1531/2023",
     *             "CVE_JUZ": "M06",
     *             "FCH_PRO": "2024-09-13T00:00:00.000Z",
     *             "FCH_ACU": "2024-09-18T00:00:00.000Z",
     *             "BOLETIN": "Provee escrito endosatario actora de 13/09/2024: designa autorizado en amplios términos ",
     *             "TIPO": ".",
     *             "NOTIFICACI": "B",
     *             "DI": "N",
     *             "FCH_RES": null,
     *             "CVE_JUI": "ME",
     *             "DESCRIP": "MERCANTIL EJECUTIVO",
     *             "act_names": "MARIO EDUARDO QUINTERO ROMERO",
     *             "dem_names": "GERARDO JESUS ALFREDO RUIZ ORGADO"
     *         },
     *         {
     *             "EXP": "160/2022",
     *             "CVE_JUZ": "OM01",
     *             "FCH_PRO": "2024-09-12T00:00:00.000Z",
     *             "FCH_ACU": "2024-09-18T00:00:00.000Z",
     *             "BOLETIN": "DESIGNA NOTARIO, POR HECHAS MANIFESTACIONES, SE CONCEDE TÉRMINO DE 3 TRES DÍAS AL DEMANDADO PARA QUE OTORGUE ESCRITURA DE ADJUDICACIÓN EN FAVOR DEL ACTOR, SE ORDENA DILIGENCIA. -",
     *             "TIPO": "EMO",
     *             "NOTIFICACI": "B",
     *             "DI": "N",
     *             "FCH_RES": null,
     *             "CVE_JUI": "EMO",
     *             "DESCRIP": "EJECUTIVO MERCANTIL ORAL",
     *             "act_names": "BBVA BANCOMER, SOCIEDAD ANONIMA INSTITUCION DE BANCA MULTIPLE GRUPO FINANCIERO BBVA BANCOMER ",
     *             "dem_names": "CHE PRESENTACIONES S.A. DE C.V., MARIA DE JESUS QUINTERO RODRIGIEZ , JOSE CUAHUTEMOC HERNANDEZ ESPINOSA, BERTHA CARIN PARTIDA CORONA , JOSE CUAHUTEMOC HERNANDEZ QUINTERO "
     *         },
     */
    public static final String[] ZM_MERCANTIL = { "EXP","CVE_JUZ","FCH_PRO","FCH_ACU","BOLETIN","TIPO","NOTIFICACI","DI"
            ,"FCH_RES","CVE_JUI","DESCRIP","act_names","dem_names"
    };
    public static final String[] ZM_MERCANTIL_VALUES = {"M01","M02","M03","M04","M05","M06","M07","M08","M09","M10"
            ,"OM01","OM02","OM03","OM04","OM05","OM06","OM07","OM08","OM09"};
}
