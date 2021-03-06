package org.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;


public class MainActivity extends Activity {

    public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();
    private Button acercade;
    private Button jugar;
    private Button puntuaciones;
    private Button configurar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView titulo = findViewById(R.id.textView);
        Animation animaTitulo = AnimationUtils.loadAnimation(this, R.anim.giro_con_zoom);
        titulo.startAnimation(animaTitulo);

        jugar = findViewById(R.id.button1);
        final Animation animaBut1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha);
        jugar.startAnimation(animaBut1);
        jugar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarJuego(null);
            }
        });


        configurar = findViewById(R.id.button2);

        Animation animaBut2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha1);
        configurar.startAnimation(animaBut2);
        configurar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarPreferencias(null);

            }
        });

        // Event listener por codigo para abrir acerca de.
        acercade = findViewById(R.id.button3);
        final Animation animaBut3 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha2);
        acercade.startAnimation(animaBut3);
        acercade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarAcercaDe(null);




            }
        });


        puntuaciones = findViewById(R.id.button4);
        final Animation animaBut4 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha3);
        puntuaciones.startAnimation(animaBut4);
        puntuaciones.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarPuntuaciones(null);


            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true; /** true -> el menú ya está visible */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }
        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lanzarAcercaDe(View view) {


        Intent i = new Intent(this, AcercaDeActivity.class);

        startActivity(i);
        //animacion de boton


    }

    public void lanzarJuego(View view) {
        Intent i = new Intent(this, Juego.class);

        startActivity(i);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivity(i);
    }


    public void mostrarPreferencias(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "musica:  " + pref.getBoolean("musica", true)
                + ", graficos: " + pref.getString("graficos", "?")
                + "fragmentos: " + pref.getString("fragmentos", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();


    }
    public void lanzarPuntuaciones(View view) {

        Intent i = new Intent(this, Puntuaciones.class);
        startActivity(i);
    }

}

