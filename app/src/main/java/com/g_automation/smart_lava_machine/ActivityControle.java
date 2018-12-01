package com.g_automation.smart_lava_machine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.g_automation.remote_sound.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ActivityControle extends ActionBarActivity {


    //Meus Widget Tipo Botões
    Button btnligar, btnnivel, btniniciarparar, btnagitacao, btnenxague, btncentrifugar, btnavanca;
    TextView txtalto, txtmedio, txtbaixo, txtextrabaixo, txtmolho, txtlavar, txtenxaguar, txtcentrifugar,
            txtturbo, txtnormal, txttripo, txtduplo, txtunico, txtextra, txtnormal1;
    ImageView lednivelalto, lednivelmedio, lednivelbaixo, lednivelextrabaixo, ledturbo, lednormal, ledtripo, ledduplo, ledunico,
            ledextra, lednormalc, ledmolho, ledlavar, ledenxagua, ledcentrifugar;


    private static final int SOLICITA_ATIVACAO = 1;
    private static final int SOLICITA_CONEXAO = 2;
    private static final int MESSAGE_READ = 3;

    final Handler handler = new Handler();

    ConnectedThread connectedThread;

    Handler mHandler;

    StringBuilder dadosBluetooth = new StringBuilder();

    //Representa o adaptador Bluetooth local (rádio Bluetooth).
    BluetoothAdapter meuBluetoothAdapter = null;

    //Variavel que indica Bluetooth conectado
    private boolean conexao = false;

    //Variavel Endereço (MAC)
    private static String MAC = null;

    //Variavel Endereço (NOME_APARELHO)
    private static String NOME_APA = null;

    //Meu diaolog ao conectar Bluetooth
    private ProgressDialog progress;

    //Representa a interface para uma tomada de Bluetooth
    //Ele é um ponto de conexão que permite que uma aplicação troque dados com outros dispisitivos Bluetooth
    //através do InputStream e OutputStream
    BluetoothSocket btSocket = null;

    //Um identificador único (UUID) é um formato de 128 bits padronizada para um ID de string usado para identificar informações.
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //Meu diaolog ao desconectar Bluetooth
    private AlertDialog alerta;

    //Meu diaolog ao desconectar Bluetooth
    private AlertDialog alertaparalista;



    // A readable source of bytes.
    private InputStream inputStream = null;

    private RadioGroup radioGroup;


    int conta = 2;
    int conta1 = 1;
    int conta2 = 2;
    int conta3 = 1;
    int conta4 = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_controle);//Cria a vista da tela


        //Icone na barra superior
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logotipo_gremote);
        getSupportActionBar().setSubtitle("      Desconectado");


        //================Obnitendo adapotador bluetooth===========================================
        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (meuBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Seu Dispositivo Não Possui Bluetooth", Toast.LENGTH_LONG).show();
        } else if (!meuBluetoothAdapter.isEnabled()) {
            Intent ativaBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ativaBluetooth, SOLICITA_ATIVACAO);

        }


//======================//Referencia os botões da nossa tela procurando pelo ID wadget==============================================================
        btnligar = (Button) findViewById(R.id.btnligar);
        btnnivel = (Button) findViewById(R.id.btnnivel);
        btniniciarparar = (Button) findViewById(R.id.btniniciarparar);
        btnagitacao = (Button) findViewById(R.id.btnagitacao);
        btnenxague = (Button) findViewById(R.id.btnenxague);
        btncentrifugar = (Button) findViewById(R.id.btncentrifugar);
        btnavanca = (Button) findViewById(R.id.btnavanca);

        txtalto = (TextView) findViewById(R.id.txtalto);
        txtmedio = (TextView) findViewById(R.id.txtmedio);
        txtbaixo = (TextView) findViewById(R.id.txtbaixo);
        txtextrabaixo = (TextView) findViewById(R.id.txtextrabaixo);
        txtmolho = (TextView) findViewById(R.id.txtmolho);
        txtlavar = (TextView) findViewById(R.id.txtlavar);
        txtenxaguar = (TextView) findViewById(R.id.txtenxaguar);
        txtcentrifugar = (TextView) findViewById(R.id.txtcentrifugar);

        txtturbo = (TextView) findViewById(R.id.txtturbo);
        txtnormal = (TextView) findViewById(R.id.txtnormal);
        txttripo = (TextView) findViewById(R.id.txttripo);
        txtduplo = (TextView) findViewById(R.id.txtduplo);
        txtunico = (TextView) findViewById(R.id.txtunico);
        txtextra = (TextView) findViewById(R.id.txtextra);
        txtnormal1 = (TextView) findViewById(R.id.txtnormal1);


        lednivelalto = (ImageView) findViewById(R.id.lednivelalto);
        lednivelmedio = (ImageView) findViewById(R.id.lednivelmedio);
        lednivelbaixo = (ImageView) findViewById(R.id.lednivelbaixo);
        lednivelextrabaixo = (ImageView) findViewById(R.id.lednivelextrabaixo);
        ledturbo = (ImageView) findViewById(R.id.ledturbo);
        lednormal = (ImageView) findViewById(R.id.lednormal);
        ledtripo = (ImageView) findViewById(R.id.ledtripo);
        ledduplo = (ImageView) findViewById(R.id.ledduplo);
        ledunico = (ImageView) findViewById(R.id.ledunico);
        ledextra = (ImageView) findViewById(R.id.ledextra);
        lednormalc = (ImageView) findViewById(R.id.lednormalc);
        ledmolho = (ImageView) findViewById(R.id.ledmolho);
        ledlavar = (ImageView) findViewById(R.id.ledlavar);
        ledenxagua = (ImageView) findViewById(R.id.ledenxagua);
        ledcentrifugar = (ImageView) findViewById(R.id.ledcentrifugar);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
//========================================================================================
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radiodelicado:
                        mesagem("P1");

                        break;
                    case R.id.radioextrarpido:
                        mesagem("P2");

                        break;
                    case R.id.radiodiadia:
                        mesagem("P3");

                        break;
                    case R.id.radiopesado:
                        mesagem("P4");

                        break;
                    case R.id.radiocmabanho:
                        mesagem("P5");

                        break;

                }
            }
        });

        btnligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesagem("l");

            }
        });
        btnnivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (conta) {
                    case 0:
                        mesagem("N1");
                        txtalto.setTextColor(getColor(R.color.cornivelred));
                        txtmedio.setTextColor(getColor(R.color.cornivel));
                        txtbaixo.setTextColor(getColor(R.color.cornivel));
                        txtextrabaixo.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 1:
                        mesagem("N2");
                        txtalto.setTextColor(getColor(R.color.cornivel));
                        txtmedio.setTextColor(getColor(R.color.cornivelred));
                        txtbaixo.setTextColor(getColor(R.color.cornivel));
                        txtextrabaixo.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 2:
                        mesagem("N3");
                        txtalto.setTextColor(getColor(R.color.cornivel));
                        txtmedio.setTextColor(getColor(R.color.cornivel));
                        txtbaixo.setTextColor(getColor(R.color.cornivelred));
                        txtextrabaixo.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 3:
                        mesagem("N4");
                        txtalto.setTextColor(getColor(R.color.cornivel));
                        txtmedio.setTextColor(getColor(R.color.cornivel));
                        txtbaixo.setTextColor(getColor(R.color.cornivel));
                        txtextrabaixo.setTextColor(getColor(R.color.cornivelred));
                        break;

                }
                if (conta != 3){conta = conta + 1;}else{conta = 0;}
                //Toast.makeText(getApplicationContext(), "N" + conta, Toast.LENGTH_LONG).show();

            }
        });
        btniniciarparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesagem("i");
            }
        });
        btnagitacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (conta1) {
                    case 0:
                        mesagem("A1");
                        txtturbo.setTextColor(getColor(R.color.cornivelred));
                        txtnormal.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 1:
                        mesagem("A2");
                        txtturbo.setTextColor(getColor(R.color.cornivel));
                        txtnormal.setTextColor(getColor(R.color.cornivelred));
                        break;

                }
                if (conta1 != 1){conta1 = conta1 + 1;}else{conta1 = 0;}
                //Toast.makeText(getApplicationContext(), "N" + conta1, Toast.LENGTH_LONG).show();
            }
        });
        btnenxague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (conta2) {
                    case 0:
                        mesagem("E1");
                        txttripo.setTextColor(getColor(R.color.cornivelred));
                        txtduplo.setTextColor(getColor(R.color.cornivel));
                        txtunico.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 1:
                        mesagem("E2");
                        txttripo.setTextColor(getColor(R.color.cornivel));
                        txtduplo.setTextColor(getColor(R.color.cornivelred));
                        txtunico.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 2:
                        mesagem("E3");
                        txttripo.setTextColor(getColor(R.color.cornivel));
                        txtduplo.setTextColor(getColor(R.color.cornivel));
                        txtunico.setTextColor(getColor(R.color.cornivelred));
                        break;

                }
                if (conta2 != 2){conta2 = conta2 + 1;}else{conta2 = 0;}
                //Toast.makeText(getApplicationContext(), "N" + conta1, Toast.LENGTH_LONG).show();
            }
        });
        btncentrifugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (conta3) {
                    case 0:
                        mesagem("C1");
                        txtextra.setTextColor(getColor(R.color.cornivelred));
                        txtnormal1.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 1:
                        mesagem("C2");
                        txtextra.setTextColor(getColor(R.color.cornivel));
                        txtnormal1.setTextColor(getColor(R.color.cornivelred));
                        break;

                }
                if (conta3 != 1){conta3 = conta3 + 1;}else{conta3 = 0;}
                //Toast.makeText(getApplicationContext(), "N" + conta1, Toast.LENGTH_LONG).show();
            }
        });
        btnavanca.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                switch (conta4) {
                    case 0:
                        mesagem("V1");
                        txtmolho.setTextColor(getColor(R.color.cornivelred));
                        txtlavar.setTextColor(getColor(R.color.cornivel));
                        txtenxaguar.setTextColor(getColor(R.color.cornivel));
                        txtcentrifugar.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 1:
                        mesagem("V2");
                        txtmolho.setTextColor(getColor(R.color.cornivel));
                        txtlavar.setTextColor(getColor(R.color.cornivelred));
                        txtenxaguar.setTextColor(getColor(R.color.cornivel));
                        txtcentrifugar.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 2:
                        mesagem("V3");
                        txtmolho.setTextColor(getColor(R.color.cornivel));
                        txtlavar.setTextColor(getColor(R.color.cornivel));
                        txtenxaguar.setTextColor(getColor(R.color.cornivelred));
                        txtcentrifugar.setTextColor(getColor(R.color.cornivel));
                        break;
                    case 3:
                        mesagem("V4");
                        txtmolho.setTextColor(getColor(R.color.cornivel));
                        txtlavar.setTextColor(getColor(R.color.cornivel));
                        txtenxaguar.setTextColor(getColor(R.color.cornivel));
                        txtcentrifugar.setTextColor(getColor(R.color.cornivelred));
                        break;
                    case 4:
                        mesagem("V4");
                        txtmolho.setTextColor(getColor(R.color.cornivel));
                        txtlavar.setTextColor(getColor(R.color.cornivel));
                        txtenxaguar.setTextColor(getColor(R.color.cornivel));
                        txtcentrifugar.setTextColor(getColor(R.color.cornivel));
                        break;

                }
                if (conta4 != 4){conta4 = conta4 + 1;}else{conta4 = 0;}
                //Toast.makeText(getApplicationContext(), "N" + conta1, Toast.LENGTH_LONG).show();

            }
        });

        //==========================================================================================


        mHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {

                if(msg.what == MESSAGE_READ){
                    String recebidos = (String) msg.obj;

                    dadosBluetooth.append(recebidos);

                    int fimInformacao = dadosBluetooth.indexOf("}");

                    if(fimInformacao > 0){

                        String dadosCompletos = dadosBluetooth.substring(0, fimInformacao);

                        int tamInformacao =dadosCompletos.length();

                        if(dadosBluetooth.charAt(0) ==  '{'){

                            String dadosFinais = dadosBluetooth.substring(1, tamInformacao);

                            if (dadosFinais.contains("PON")) {
                                btnligar.setBackgroundResource(R.drawable.btn_on);
                            }else if (dadosFinais.contains("POFF")){
                                btnligar.setBackgroundResource(R.drawable.btn_off);
                            }
                            //=======================Nivel======================================
                            if (dadosFinais.contains("N1")) {
                                lednivelalto.setImageResource(R.drawable.led_on);
                                lednivelmedio.setImageResource(R.drawable.led_off);
                                lednivelbaixo.setImageResource(R.drawable.led_off);
                                lednivelextrabaixo.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("N2")){
                                lednivelalto.setImageResource(R.drawable.led_off);
                                lednivelmedio.setImageResource(R.drawable.led_on);
                                lednivelbaixo.setImageResource(R.drawable.led_off);
                                lednivelextrabaixo.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("N3")){
                                lednivelalto.setImageResource(R.drawable.led_off);
                                lednivelmedio.setImageResource(R.drawable.led_off);
                                lednivelbaixo.setImageResource(R.drawable.led_on);
                                lednivelextrabaixo.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("N4")){
                                lednivelalto.setImageResource(R.drawable.led_off);
                                lednivelmedio.setImageResource(R.drawable.led_off);
                                lednivelbaixo.setImageResource(R.drawable.led_off);
                                lednivelextrabaixo.setImageResource(R.drawable.led_on);

                            }
                            //=====================Agitação==========================================

                            if (dadosFinais.contains("A1")) {
                                ledturbo.setImageResource(R.drawable.led_on);
                                lednormal.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("A2")) {
                                ledturbo.setImageResource(R.drawable.led_off);
                                lednormal.setImageResource(R.drawable.led_on);
                            }
                            //===============================================================
                            //=====================Enxague==========================================

                            if (dadosFinais.contains("E1")) {
                                ledtripo.setImageResource(R.drawable.led_on);
                                ledduplo.setImageResource(R.drawable.led_off);
                                ledunico.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("E2")) {
                                ledtripo.setImageResource(R.drawable.led_off);
                                ledduplo.setImageResource(R.drawable.led_on);
                                ledunico.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("E3")) {
                                ledtripo.setImageResource(R.drawable.led_off);
                                ledduplo.setImageResource(R.drawable.led_off);
                                ledunico.setImageResource(R.drawable.led_on);
                            }
                            //===============================================================
                            //========================centrifugação=======================================

                            if (dadosFinais.contains("C1")) {
                                ledextra.setImageResource(R.drawable.led_on);
                                lednormalc.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("C2")) {
                                ledextra.setImageResource(R.drawable.led_off);
                                lednormalc.setImageResource(R.drawable.led_on);
                            }
                            //===============================================================

                            if (dadosFinais.contains("V1")) {
                                ledmolho.setImageResource(R.drawable.led_on);
                                ledlavar.setImageResource(R.drawable.led_off);
                                ledenxagua.setImageResource(R.drawable.led_off);
                                ledcentrifugar.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("V2")) {
                                ledmolho.setImageResource(R.drawable.led_off);
                                ledlavar.setImageResource(R.drawable.led_on);
                                ledenxagua.setImageResource(R.drawable.led_off);
                                ledcentrifugar.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("V3")) {
                                ledmolho.setImageResource(R.drawable.led_off);
                                ledlavar.setImageResource(R.drawable.led_off);
                                ledenxagua.setImageResource(R.drawable.led_on);
                                ledcentrifugar.setImageResource(R.drawable.led_off);
                            }
                            if (dadosFinais.contains("V4")) {
                                ledmolho.setImageResource(R.drawable.led_off);
                                ledlavar.setImageResource(R.drawable.led_off);
                                ledenxagua.setImageResource(R.drawable.led_off);
                                ledcentrifugar.setImageResource(R.drawable.led_on);
                            }




                            Log.d("dadosRecebidos", dadosFinais);
                        }
                        dadosBluetooth.delete(0, dadosBluetooth.length());

                    }

                }

            }
        };



    }

    //===================================Manu===============================================
    //=======================Botão de menu no ActionBar============================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //item.setIcon(R.drawable.bluedesco);
        switch (item.getItemId()) {
            case R.id.item_conexao:
                if (conexao) {
                    //Desconectar
                    messalerta();
                } else {
                    //conectar
                    Intent abrelista = new Intent(ActivityControle.this, ListaDispositivos.class);
                    startActivityForResult(abrelista, SOLICITA_CONEXAO);
                    if (MAC != null) {
                    }
                }
                return true;
        }

        if (item.getItemId() == R.id.sair) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //==========================Metodo para obter o Endereço MAC===================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //=========solicita ativação bluetooth================
            case SOLICITA_ATIVACAO:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth. Ativado ;-)", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth Não Foi Ativado", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            //=========solicita conexão bluetooth================
            case SOLICITA_CONEXAO:
                if (resultCode == Activity.RESULT_OK) {
                    //Recebemos o endereço MAC obtido na atividade anterior
                    MAC = data.getExtras().getString(ListaDispositivos.ENDERECO_MAC);
                    new ConnectBT().execute(); //Call the class to connect
                } else {
                    Toast.makeText(getApplicationContext(), "Falha ao Obter O MAC", Toast.LENGTH_LONG).show();
                }

                break;

        }
    }

    //==========================================Fim===============================================
    //=============================Motodo para conexão bluetooth===================================
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ActivityControle.this, "Conectando...", "Aguarde a conexão...!!!");
            getSupportActionBar().setSubtitle("      Conectando...");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !conexao) {
                    meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = meuBluetoothAdapter.getRemoteDevice(MAC);//conectamos al dispositivo y chequeamos si esta disponible
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                    connectedThread = new ConnectedThread(btSocket);
                    connectedThread.start();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        //========================Indica si foi conectado ou não =====================================
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (ConnectSuccess == false) {
                conexao = false;
                //btnluz.setBackgroundResource(R.drawable.bluedesco);
                Toast.makeText(getApplicationContext(), "Falha na Conexão Tente Novamente", Toast.LENGTH_LONG).show();
                getSupportActionBar().setSubtitle("      Desconectado");
            } else {
                conexao = true;
                //btnluz.setBackgroundResource(R.drawable.blueconect);
                Toast.makeText(getApplicationContext(), "Conectado ;-)", Toast.LENGTH_LONG).show();
                getSupportActionBar().setSubtitle("          Conectado");
                handler.postDelayed(atualizaStatus, 2000);

            }
            progress.dismiss();
        }
    }

    //========================================Fim===================================================
    //=====================================Motodo desconectar=======================================

    private void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
                Toast.makeText(getApplicationContext(), "OK, Desconectado", Toast.LENGTH_LONG).show();
                getSupportActionBar().setSubtitle("      Desconectado");
                MAC = null;
                conexao = false;
                //btnluz.setBackgroundResource(R.drawable.bluedesco);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Ocorreu Erro", Toast.LENGTH_LONG).show();
            }
        }
    }

    //============Método que atualiza status da tela======================
    private Runnable atualizaStatus = new Runnable() {
        @Override
        public void run() {

            //Toast.makeText(getApplicationContext(), "Solicitando", Toast.LENGTH_LONG).show();
            //mesagem 'w');
            handler.postDelayed(this, 2000);

        }
    };


    //====================Metodo para enviar mensagens via bluetooth================================
    public void mesagem(String mensagem) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(mensagem.toString().getBytes());
            } catch (IOException e) { // msg("Error");
            }
        }
    }

    //========Metodo para receber dados=============================================================
    private class ConnectedThread extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    String dadosBt = new String(buffer, 0, bytes );

                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, dadosBt)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
    }
    //===========================================fim================================================
    //=======Dialogo que pergunta si tem certeza que quer desconectar===============================
    private void messalerta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desconectar?");
        builder.setMessage("Tem Certeza??");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Disconnect();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alerta = builder.create();
        alerta.show();
    }//fim


    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(getApplicationContext(), "Stop ", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Toast.makeText(getApplicationContext(), "Bluetooth ", Toast.LENGTH_LONG).show();
                return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mesagem("");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(getApplicationContext(), "Restart ", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        meuBluetoothAdapter.disable();

    }

}