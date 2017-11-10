package org.example.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.view.KeyEvent.KEYCODE_DPAD_UP;

/**
 * Created by jay on 10/28/17.
 */

public class VistaJuego extends View {
    // //// THREAD Y TIEMPO //////
// Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;
    private static final int MAX_VELOCIDAD_NAVE = 20;
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;
    ////////ASTEROIDES//////////
    private List<Grafico> asteroides; //lista con los Asteroides
    private int numAsteroides = 5;//Numero inicial de asteroides
    private int numFragmentos = 3;//Fragmentos en que se divide
    ///////NAVE////////////////
    private Grafico nave; // Gráfico de la nave
    private int giroNave; // Incremento de dirección
    private double aceleracionNave; // aumento de velocidad


    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable drawableNave, drawableAsteroide, drawableMisil;


        SharedPreferences pref = PreferenceManager.
                getDefaultSharedPreferences(getContext());

        if (pref.getString("graficos", "1").equals("0")) {

            Path pathAsteroide = new Path();
            pathAsteroide.moveTo((float) 0.3, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.3);
            pathAsteroide.lineTo((float) 0.8, (float) 0.2);
            pathAsteroide.lineTo((float) 1.0, (float) 0.4);
            pathAsteroide.lineTo((float) 0.8, (float) 0.6);
            pathAsteroide.lineTo((float) 0.9, (float) 0.9);
            pathAsteroide.lineTo((float) 0.8, (float) 1.0);
            pathAsteroide.lineTo((float) 0.4, (float) 1.0);
            pathAsteroide.lineTo((float) 0.0, (float) 0.6);
            pathAsteroide.lineTo((float) 0.0, (float) 0.2);
            pathAsteroide.lineTo((float) 0.3, (float) 0.0);

            ShapeDrawable dAsteroide = new ShapeDrawable(
                    new PathShape(pathAsteroide, 1, 1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(50);
            dAsteroide.setIntrinsicHeight(50);

            drawableAsteroide = dAsteroide;

            Path pathNave = new Path();
            pathNave.moveTo((float) 0.0, (float) 0.0);
            pathNave.lineTo((float) 1, (float) 0.5);
            pathNave.lineTo((float) 0, (float) 1);
            pathNave.lineTo((float) 0, (float) 0);
            ShapeDrawable dNave = new ShapeDrawable(
                    new PathShape(pathNave, 1, 1));
            dNave.getPaint().setColor(Color.WHITE);
            dNave.getPaint().setStyle(Paint.Style.STROKE);
            dNave.setIntrinsicWidth(20);
            dNave.setIntrinsicHeight(15);
            drawableNave = dNave;

            setBackgroundColor(Color.BLACK);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else if (pref.getString("graficos", "1").equals("1")) {
            drawableAsteroide =
                    context.getResources().getDrawable(R.drawable.ic_project_3);
            ///draw nave
            drawableNave =
                    context.getResources().getDrawable(R.drawable.ic_nave_color);
        } else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            drawableAsteroide =
                    context.getResources().getDrawable(R.drawable.asteroide1);
            ContextCompat.getDrawable(context, R.drawable.asteroide1);

            drawableNave =
                    context.getResources().getDrawable(R.drawable.nave);
            ContextCompat.getDrawable(context, R.drawable.nave);
        }
        nave = new Grafico(this, drawableNave);
        asteroides = new ArrayList<Grafico>();
        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }
    }

    @Override
    protected void onSizeChanged(int ancho, int alto,
                                 int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        // Una vez que conocemos nuestro ancho y alto.
        for (Grafico asteroide : asteroides) {
            do {
                asteroide.setCenX((int) (Math.random() * ancho));
                asteroide.setCenY((int) (Math.random() * alto));
            } while (asteroide.distancia(nave) < ((alto + ancho) / 5));

        }

        nave.setCenX(ancho / 2);
        nave.setCenY(alto / 2);

        ultimoProceso = System.currentTimeMillis();
        thread.start();


    }

    @Override
   synchronized protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafico asteroide : asteroides) {
            asteroide.dibujaGrafico(canvas);
        }

        nave.dibujaGrafico(canvas);
    }
    synchronized protected void actualizaFisica() {
        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return; // Salir si el período de proceso no se ha cumplido.
        }
// Para una ejecución en tiempo real calculamos el factor de movimiento
        double factorMov = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez
// Actualizamos velocidad y dirección de la nave a partir de
// giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * factorMov));
        double nIncX = nave.getIncX() + aceleracionNave *
                Math.cos(Math.toRadians(nave.getAngulo())) * factorMov;
        double nIncY = nave.getIncY() + aceleracionNave *
                Math.sin(Math.toRadians(nave.getAngulo())) * factorMov;
// Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX,nIncY) <=  MAX_VELOCIDAD_NAVE ){
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        nave.incrementaPos(factorMov); // Actualizamos posición
        for (Grafico asteroide : asteroides) {
            asteroide.incrementaPos(factorMov);
        }
    }
    class ThreadJuego extends Thread{
        @Override
                public void run(){
            while (true){
               actualizaFisica();
            }
        }
    }
    @Override
    public  boolean onKeyDown(int codigoTecla, KeyEvent evento) {
        super.onKeyDown(codigoTecla, evento);
        //Suponemos que vamos a procesar la pulsacion
        boolean procesada = true;
        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = +PASO_ACELERACION_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                giroNave = - PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = + PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                //activaMisil();
                break;
            default:
                //Si estamos aqui no hay pulsacion que nos interese
                procesada = false;
        }
        return procesada;
    }
    @Override
  public boolean onKeyUp(int codigoTecla,KeyEvent evento){
        super.onKeyUp(codigoTecla,evento);
        boolean procesada = true;
        switch(codigoTecla){
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave=0;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = 0;
                break;
            default:
                procesada = false;
                break;



        }
        return procesada;

    }

}




