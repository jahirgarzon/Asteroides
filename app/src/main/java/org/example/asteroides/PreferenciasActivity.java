package org.example.asteroides;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by jay on 10/18/17.
 */

public class PreferenciasActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())

                .commit();







    }

}



