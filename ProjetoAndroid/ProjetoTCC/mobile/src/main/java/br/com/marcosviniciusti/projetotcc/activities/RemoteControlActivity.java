package br.com.marcosviniciusti.projetotcc.activities;

import android.hardware.ConsumerIrManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;


public class RemoteControlActivity extends AppCompatActivity {

    private final static String COMANDO_POWER ="0000 0067 0000 000d 0060 0017 0030 0017 0018 0017 0030 0017 0018 0017 0030 0017 0018 0017 0018 0017 0030 0017 0018 0017 0018 0017 0018 0017 0018 03ef";

    private FloatingActionButton fabPower;
    private FloatingActionButton fabPlusVolume;
    private FloatingActionButton fabMinusVolume;
    private FloatingActionButton fabUpChannel;
    private FloatingActionButton fabDownChannel;
    private ConsumerIrManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        bind();
        createEvents();
        Log.i("RemoteControlActivity" ,"manager.hasIrEmitter(): " + manager.hasIrEmitter());
        ConsumerIrManager.CarrierFrequencyRange[] freqs = manager.getCarrierFrequencies();
        for(ConsumerIrManager.CarrierFrequencyRange freq:freqs) {
            Log.i("RemoteControlActivity" ,"freq min: " + freq.getMinFrequency());
            Log.i("RemoteControlActivity" ,"freq max: " + freq.getMaxFrequency());
        }

    }

    private void bind() {
        fabPower = findViewById(R.id.fabPower);
        /*fabPlusVolume = findViewById(R.id.fabPlusVolume);
        fabMinusVolume = findViewById(R.id.fabMinusVolume);
        fabUpChannel = findViewById(R.id.fabUpChannel);
        fabDownChannel = findViewById(R.id.fabDownChannel);*/
        manager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
    }

    private void createEvents() {
        fabPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.hasIrEmitter()){
                    // transmitir sinais passando a frequência e o padrão de sinal desejados.
                    Log.i("RemoteControlActivity", "Botão Power");
                    Toast.makeText(getApplicationContext(), "Botão Power", Toast.LENGTH_LONG).show();
                    enviarComando(COMANDO_POWER);
                } else {
                    Toast.makeText(getApplicationContext(),"Seu dispositivo não tem sensor infra-vermelho.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void enviarComando(final String comando) {

        List<String> list = new ArrayList<String>(Arrays.asList(comando.split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        frequency = (int) (1000000 / (frequency * 0.241246));
        int pulses = 1000000 / frequency;
        int count;

        int[] pattern = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            count = Integer.parseInt(list.get(i), 16);
            pattern[i] = count * pulses;
        }

        Log.d("Remote", "frequency: " + frequency);
        Log.d("Remote", "pattern: " + Arrays.toString(pattern));
        manager.transmit(frequency, pattern);
    }
}