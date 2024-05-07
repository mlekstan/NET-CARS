package com.example.shop_app;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Collection;
import java.util.Map;

public class DataFetcher {
    public static Object[] obtainDataFromDocument(DocumentSnapshot documentSnapshot, String ... fields) {
        Object[] fieldsValues = new Object[fields.length];
        Map<String, Object> fieldToValuesMap = documentSnapshot.getData();

        int i = 0;
        for (String field:fields) {
            fieldsValues[i] = fieldToValuesMap.get(field);
            i++;
        }

        return fieldsValues;
    }

}
