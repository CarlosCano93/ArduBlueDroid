package ccano.android.ardubluedroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class BluetoothDevicesActivity extends AppCompatActivity {

    @Bind(R.id.lvDeviceList)
    ListView lvDeviceList;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String> deviceListName = new ArrayList<String>();
    ArrayList<String> deviceListMacAdress = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);
        ButterKnife.bind(this);

        getBluetoothPairedDevices();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceListName);
        lvDeviceList.setAdapter(adapter);


    }

    @OnItemClick(R.id.lvDeviceList)
    public void handleLvDeviceList(int position){
        String selectedDevice = deviceListMacAdress.get(position);
        Toast.makeText(this, "MAC adress: " + selectedDevice, Toast.LENGTH_LONG).show();
    }

    /**
     * get all bluetooth paired devices
     */
    public void getBluetoothPairedDevices(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices != null && pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // use two arrays; with names for the user and other with internal MAC to connect devices
                deviceListName.add(device.getName());
                deviceListMacAdress.add(device.getAddress());
                //DEBUG Toast.makeText(this, device.getName(), Toast.LENGTH_SHORT).show();
            }
        } else {
            deviceListName.add(getString(R.string.devices_not_found));
        }
    }
}
