package ccano.android.ardubluedroid;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * My first real project with Android ;)
 */

public class MainActivity extends AppCompatActivity {

    //used to turn on bluetooth
    private static final int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        activateBluetooth();
    }

    //open new activity with device bluetooth list
    @OnClick(R.id.btSearchDevices)
    public void handleButtonSearchDevice(){
        Intent intentDeviceList = new Intent(getApplicationContext(), BluetoothDevicesActivity.class);
        startActivity(intentDeviceList);
    }

    @OnClick(R.id.btOn)
    void handleButtonOn(){
        //TODO send info to put led on
        Toast.makeText(this, "LED ON", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btOff)
    void handleButtonOff(){
        //TODO send info to put led Off
        Toast.makeText(this, "LED OFF", Toast.LENGTH_SHORT).show();
    }

    /**
     * Check bluetooth compatibility
     * Turn on bluetooth
     */
    public void activateBluetooth(){
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_incompatible), Toast.LENGTH_SHORT).show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
}
