package alex.alves.imagebluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    ImageView tela;
    TextView mensagem;
    BluetoothAdapter AdaptadorBt;
    public static int ativar_bluetooth=1;
    public static int dispositivo_pareado=2;
    public static int dispositivo_descoberto=3;

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
        }
    }


}
