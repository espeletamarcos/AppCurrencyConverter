package com.example.appconversionmoneda;

import java.util.Map;

//Clase de la que vamos a recibir el JSON
public class RespuestaAPI {

    private String result;
    private String base_code;
    private Map<String, Double> conversion_rates; //IMPORTANTÍSIMO que tengan el mismo nombre que en el JSON

    public String getBase_code() {
        return base_code;
    }

    public Map<String, Double> getConversion_rates() {
        return conversion_rates;
    }

    public String getResult() {
        return result;
    }
}
