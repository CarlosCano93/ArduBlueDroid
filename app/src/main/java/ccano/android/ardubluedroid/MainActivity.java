package ccano.android.ardubluedroid;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * My first real project with Android ;)
 */

public class MainActivity extends AppCompatActivity {

    //used to turn on bluetooth
    private static final int REQUEST_ENABLE_BT = 1;

    //See ButterKnife framework to understand @Bind @OnClick and more..
    @Bind(R.id.btOn)
    Button btOn;
    @Bind(R.id.btOff)
    Button btOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To check compatibility and turn on bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_incompatible), Toast.LENGTH_SHORT).show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


    }

    @OnClick(R.id.btOn)
    public void handleButtonOn(){
        //TODO send info to put led on
    }

    @OnClick(R.id.btOff)
    public void handleButtonOff(){
        //TODO send info to put led Off
    }
}
