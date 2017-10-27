package org.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

    public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

