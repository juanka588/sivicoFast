package com.rocket.sivico.Data;

import com.google.firebase.auth.FirebaseUser;

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
        temp.put(new String[]{"1", "SALUD", "#ba000d"}, new String[]{"INFANCIA", "ADOLESCENCIA", "JUVENTUD", "ADULTO", "VEJEZ"});
        temp.put(new String[]{"2", "SEGURIDAD", "#001064"}, new String[]{"ZONAS INSEGURAS", "COSUMO DE SUSTACIAS PSICOACTIVAS", "EXPENDIO DE SPA", "ABUSO DE AUTORIDAD"});
        temp.put(new String[]{"3", "AMBIENTE", "#ffea00"}, new String[]{"DISPOSICION DE RESIDUOS", "CONTAMINACION DE FUENTE HIDRICA", "CONTAMINACION DE AIRE", "CONTAMINACION RUIDO"});
        temp.put(new String[]{"4", "SERVICIOS PUBLICOS", "#8bc34a"}, new String[]{"AGUA", "LUZ", "GAS", "ALCANTARILLADO", "TELEFONO"});
        temp.put(new String[]{"5", "VIOLENCIA", "#4a148c"}, new String[]{"INTRAFAMILIAR", "INFANCIA", "MUJER", "ADULTO MAYOR", "HOMBRE", "ANIMALES"});
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

    public static User getUser(FirebaseUser user) {
        return new User(user.getUid()
                , user.getDisplayName()
                , "1013642638"
                , true
                , 24
                , user.getPhoneNumber()
                , "Bogota"
                , "Restrepo"
                , user.getEmail(), user.getPhotoUrl().toString() + "");
    }
}