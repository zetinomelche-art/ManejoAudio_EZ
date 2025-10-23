package sv.edu.menejoaudio_ez;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ImageButton bGrabar, bDetenerG, bReproducir, bDetenerR;
    private TextView tvRuta;
    private MediaRecorder mRec;
    private MediaPlayer mPlayer;
    private String fichero;
    Calendar calNow = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        bGrabar = findViewById(R.id.btnRecord);
        bDetenerG = findViewById(R.id.btnStopRec);
        bReproducir = findViewById(R.id.btnPlay);
        bDetenerR = findViewById(R.id.btnStopPlay);
        tvRuta = findViewById(R.id.tvRuta);

        //Verificacion de permisos
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != (PackageManager.PERMISSION_GRANTED)
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != (PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1000);
        }
        cambiarEstadoBotones(true, false, false, false);





        bGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generarRutaArchivo();

                mRec = new MediaRecorder();
                mRec.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRec.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                mRec.setOutputFile(fichero);
                try {
                    mRec.prepare();
                    mRec.start();
                    Toast.makeText(MainActivity.this, "Grabando...", Toast.LENGTH_SHORT).show();
                    cambiarEstadoBotones(false, true, false, false);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error al grabar",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        bDetenerG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRec.stop();
                mRec.release();
                mRec = null;
                Toast.makeText(MainActivity.this, "Grabacion finalizada",
                        Toast.LENGTH_SHORT).show();

                cambiarEstadoBotones(true, false, true, false);
            }
        });

        bReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(fichero);
                    mPlayer.prepare();
                    mPlayer.start();
                    Toast.makeText(MainActivity.this, "Reproduciendo...",
                            Toast.LENGTH_SHORT).show();
                    cambiarEstadoBotones(false, false, false, true);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Error al reproducir",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bDetenerR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                Toast.makeText(MainActivity.this, "Reproduccion finalizada",
                        Toast.LENGTH_SHORT).show();
                cambiarEstadoBotones(true, false, true, false);
            }
        });

    }

    private void cambiarEstadoBotones(boolean b, boolean b1, boolean b2, boolean b3) {
        bGrabar.setEnabled(b);
        bDetenerG.setEnabled(b1);
        bReproducir.setEnabled(b2);
        bDetenerR.setEnabled(b3);
    }

    private void generarRutaArchivo() {
        String strTiempo = "" + calNow.get(Calendar.DATE)
                + calNow.get(Calendar.HOUR_OF_DAY)
                + calNow.get(Calendar.MINUTE)
                + calNow.get(Calendar.SECOND)
                + calNow.get(Calendar.MILLISECOND);
        ContextWrapper contexW = new ContextWrapper(getApplicationContext());
        File DirectorioMusica = contexW.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File MiArchivo = new File(DirectorioMusica, "Migrabacion-" + strTiempo + ".3gp");
        fichero = MiArchivo.getAbsolutePath();
        tvRuta.setText("Guardado en" + fichero);
    }

    public void PlayFirstSound(View view) {
        MediaPlayer sonido = MediaPlayer.create(this, R.raw.click);
        sonido.start();
    }

    public void PlaySecondSound(View view) {
        MediaPlayer sonido = MediaPlayer.create(this, R.raw.alarma);
        sonido.start();
    }

    public void PlayThirdSound(View view) {
        MediaPlayer sonido = MediaPlayer.create(this, R.raw.explosion);
        sonido.start();
    }
}