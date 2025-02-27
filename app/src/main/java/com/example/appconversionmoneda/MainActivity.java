package com.example.appconversionmoneda;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.view.animation.AlphaAnimation;

public class MainActivity extends AppCompatActivity {

    EditText etCantidad;
    Spinner spinnerOrigen, spinnerDestino;
    TextView tvResultado;
    ServicioAPI servicioAPI;
    String llaveAPI = "1272aaa9c3d220b920ee03bc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCantidad = findViewById(R.id.etCantidad);
        spinnerOrigen = findViewById(R.id.spinnerOrigen);
        spinnerDestino = findViewById(R.id.spinnerDestino);
        tvResultado = findViewById(R.id.tvResultado);

        //Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://v6.exchangerate-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        servicioAPI = retrofit.create(ServicioAPI.class);

        //Listado de monedas
        String[] listaMonedas = {"USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TVD", "TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaMonedas); //Creamos el adaptador con el array de las siglas
        spinnerOrigen.setAdapter(adaptador); //Le aplicamos el adaptador a los spinner
        spinnerDestino.setAdapter(adaptador);
    }

    //Asignar al botón convertir
    public void clickConvertir(View v) {
        getTiposDeCambio();
    }

    //Este método se va a encargar de "aplicar" el método que hemos definido antes en la interfaz de Retrofit
    public void getTiposDeCambio() {
        String monedaOrigen = spinnerOrigen.getSelectedItem().toString(); //Recogemos todos los datos necesarios (siglas y cantidad)
        String monedaDestino = spinnerDestino.getSelectedItem().toString();

        if(etCantidad.getText().toString().isEmpty()) {
            tvResultado.setText("Introduce una cantidad");
            return;
        }

        double cantidad;

        try {
            cantidad = Double.parseDouble(etCantidad.getText().toString());
        } catch (Exception e) {
            tvResultado.setText("Número inválido");
            return;
        }


        //Llamamos al método de la interfaz que nos va a completar la URL
        servicioAPI.getTiposDeCambio(llaveAPI, monedaOrigen).enqueue(new Callback<RespuestaAPI>() {
            @Override
            public void onResponse(Call<RespuestaAPI> call, Response<RespuestaAPI> response) { //Método que define lo que vamos a hacer en caso de que la respuesta sea satisfactoria, o que simplemente, haya respuesta
                if(response.isSuccessful() && response.body() != null) { //Aquí verificamos tanto que la respuesta sea satisfactoria como que no haya sido nula
                    double tipoCambio = response.body().getConversion_rates().get(monedaDestino); //Del mapa que hemos obtenido como respuesta obtenemos el tipo de cambio a partir del String que usamos como llave que son las siglas de la moneda de destino
                    double resultado = cantidad * tipoCambio; //Multiplicamos la cantidad por el tipo de cambio que hemos obtenido
                    animarResultado(); //Animación de resultado
                    tvResultado.setText(String.format("%.2f %s", resultado, monedaDestino));
                } else {
                    Toast.makeText(MainActivity.this, "Error al obtener el tipo de cambio", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaAPI> call, Throwable t) { //Método que define lo que vamos a hacer en caso de que falle la respuesta o simplemente ni haya
                Toast.makeText(MainActivity.this, "Error al obtener la respuesta de la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método para la animación del resultado
    private void animarResultado() {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(500);
        tvResultado.startAnimation(fadeIn);
    }
}
