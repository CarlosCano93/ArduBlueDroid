package ccano.android.ardubluedroid.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import ccano.android.ardubluedroid.R;

/**
 * ListView with all paired devices on this smartphone
 */
public class BluetoothDevicesActivity extends AppCompatActivity {

    // see Butterknife framework to understand this @Bind and @OnClick ...
    @Bind(R.id.lvDeviceList)
    ListView lvDeviceList;

    @Bind(R.id.mySwipeRefreshLayout)
    SwipeRefreshLayout mySwipeRefreshLayout;

    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String> deviceListName = new ArrayList<String>();
    ArrayList<String> deviceListMacAdress = new ArrayList<String>();

    // name to send via intent
    public static final String EXTRA_DEVICE_ADDRESS = "device_address";
    //used to turn on bluetooth
    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);
        ButterKnife.bind(this);

        activateBluetooth();

        getBluetoothPairedDevices();

        // swipe down to refresh device list
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBluetoothPairedDevices();
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    /**
     * Get item selected from ListView
     * and send adress MAC to MainActivity
     * @param position
     */
    @OnItemClick(R.id.lvDeviceList)
    public void handleLvDeviceList(int position){
        String adressMac = deviceListMacAdress.get(position);
        //Toast.makeText(this, "Selected: " + adressMac, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_DEVICE_ADDRESS, adressMac);
        startActivity(intent);
    }

    void addNamesToList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceListName);
        lvDeviceList.setAdapter(adapter);
    }

    /**
     * get all bluetooth paired devices on this smartphone
     */
    public void getBluetoothPairedDevices(){
        deviceListName.clear();

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
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
        addNamesToList();
    }
    /**
     * Check bluetooth compatibility
     * And turn on bluetooth
     */
    public void activateBluetooth(){
        if (btAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_incompatible), Toast.LENGTH_SHORT).show();
        } else if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
}
