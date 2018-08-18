package alex.alves.imagebluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    ImageView tela;
    static TextView mensagem;
    BluetoothAdapter AdaptadorBt;
    public static int ativar_bluetooth=1;
    public static int dispositivo_pareado=2;
    public static int dispositivo_descoberto=3;

    ConnectionThread connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tela = (ImageView)findViewById(R.id.imageView);
        mensagem = (TextView)findViewById(R.id.textView);


        AdaptadorBt = BluetoothAdapter.getDefaultAdapter();
        VerificarBluetooth();
        AtivarBluetooth();
    }

    public void VerificarBluetooth() {
        if (AdaptadorBt == null) {
            Toast.makeText(getApplicationContext(), "Seu aparelho não tem bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth funcionando", Toast.LENGTH_SHORT).show();
        }

    }
    public void AtivarBluetooth(){
        if(!AdaptadorBt.isEnabled()) {
            // Se não estiver ativo solicita ativação ao usuário
            Intent intentAtivar = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentAtivar,ativar_bluetooth);
            Toast.makeText(getApplicationContext(), "Solicitando ativação do bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth ativado", Toast.LENGTH_SHORT).show();
        }
    }

    // Resposta do usuario na solicitação do bluetooth
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == ativar_bluetooth) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth ativado", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Bluetooth  não foi ativado", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode ==dispositivo_pareado || requestCode == dispositivo_descoberto) {
            if(resultCode == RESULT_OK) {
                String temp="Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress");
                Toast.makeText(getApplicationContext(),temp, Toast.LENGTH_SHORT).show();


                connect = new ConnectionThread(data.getStringExtra("btDevAddress"));
                connect.start();
            }
            else {
                Toast.makeText(getApplicationContext(),"Nenhum dispositivo foi selecionado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // deveria ser estatic
    public  static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString= new String(data);

            if(dataString.equals("---N")){
               // Toast.makeText(getApplicationContext(),"Erro durante a conexao", Toast.LENGTH_SHORT).show();
                mensagem.setText("Erro durante a conexao");
            }


            else if(dataString.equals("---S")) {
                mensagem.setText("Conectado");
              //  Toast.makeText(getApplicationContext(),"Conectado", Toast.LENGTH_SHORT).show();
            }

            else {

                mensagem.setText(new String(data));
            }
        }
    };


    public void EnviarMensagem(String mensagemBluetooth){
        byte[] data =  mensagemBluetooth.getBytes();
        connect.write(data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        // Pega o inflater já existente
        MenuInflater criarMenu = getMenuInflater();
        // Cria menu a partir da leitura de um xml
        criarMenu.inflate(R.menu.meu_menu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Implementar ação ao clicar no item do menu

        switch(item.getItemId()){
            case R.id.id_pareado:

                Intent IntentDispositivoP = new Intent(this, DispositivoPareado.class);
                startActivityForResult(IntentDispositivoP, dispositivo_pareado);
                break;
            case R.id.id_descobrir:

                Intent DescobrirDispositivo = new Intent(this, DescobrirDispositivo.class);
                startActivityForResult(DescobrirDispositivo,dispositivo_descoberto);
                break;
            case R.id.id_visivel:
                Intent VisivelIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                VisivelIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 50);
                startActivity(VisivelIntent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
