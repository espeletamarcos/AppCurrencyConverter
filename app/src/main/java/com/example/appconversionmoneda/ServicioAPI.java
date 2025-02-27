package com.example.appconversionmoneda;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


//Interfaz para llamar a la API
public interface ServicioAPI {
    @GET("v6/{llaveAPI}/latest/{monedaBase}")
    Call<RespuestaAPI> getTiposDeCambio( //Con estos parámetros básicamente vamos a completar la URL
            @Path("llaveAPI") String llaveAPI,
            @Path("monedaBase") String monedaBase
    );
}

// Una URL básica sería así: https://v6.exchangerate-api.com/v6/1272aaa9c3d220b920ee03bc/latest/USD
// Este método completa la URL básica que hemos especificado al crear el Retrofit en la Main que en el caso de arriba llegaria hasta el ../v6/
// Por eso se le añade el parámetro llaveAPI que es la secuencia larga, el latest se añade desde el @GET que tenemos arriba y finalmente la monedaBase que la extraemos del Spinner
