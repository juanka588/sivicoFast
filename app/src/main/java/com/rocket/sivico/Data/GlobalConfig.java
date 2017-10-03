package com.rocket.sivico.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JuanCamilo on 2/10/2017.
 */

public class GlobalConfig {
    public static final String PARAM_REPORT = "report";
    public static final Map<String[], String[]> temp = new HashMap<>();
    public static final String PARAM_CATEGORY = "category";

    static {
        temp.put(new String[]{"1", "SALUD", "red"}, new String[]{"INFANCIA", "ADOLESCENCIA", "JUVENTUD", "ADULTO", "VEJEZ"});
        temp.put(new String[]{"2", "SEGURIDAD", "blue"}, new String[]{"ZONAS INSEGURAS", "COSUMO DE SUSTACIAS PSICOACTIVAS", "EXPENDIO DE SPA", "ABUSO DE AUTORIDAD"});
        temp.put(new String[]{"3", "AMBIENTE", "yellow"}, new String[]{"DISPOSICION DE RESIDUOS", "CONTAMINACION DE FUENTE HIDRICA", "CONTAMINACION DE AIRE", "CONTAMINACION RUIDO"});
        temp.put(new String[]{"4", "SERVICIOS PUBLICOS", "green"}, new String[]{"AGUA", "LUZ", "GAS", "ALCANTARILLADO", "TELEFONO"});
        temp.put(new String[]{"5", "VIOLENCIA", "purple"}, new String[]{"INTRAFAMILIAR", "INFANCIA", "MUJER", "ADULTO MAYOR", "HOMBRE", "ANIMALES"});
    }

    public static List<Category> initCategories() {
        List<Category> categories = new ArrayList<>(5);
        for (Map.Entry<String[], String[]> entry : temp.entrySet()) {
            String[] key = entry.getKey();
            String[] val = entry.getValue();
            Category parent = new Category(Integer.parseInt(key[0]), key[1], key[2]);
            for (int i = 0; i < val.length; i++) {
                parent.addChild(new Category(parent.getTitle(), i + 1, val[i], parent.getColor()));
            }
            categories.add(parent);
        }
        return categories;
    }
}