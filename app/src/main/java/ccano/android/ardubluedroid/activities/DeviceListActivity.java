package ccano.android.ardubluedroid.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import Adapter.MyAdapter;
import ccano.android.ardubluedroid.R;

public class DeviceListActivity extends AppCompatActivity {

    // recycler view
    private RecyclerView rvDevices;
    private RecyclerView.Adapter rvAdapterDevices;
    private RecyclerView.LayoutManager rvLayoutManagerDevices;

    // bluetooth
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
        setContentView(R.layout.activity_device_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.bluetooth_available_devices);
        setSupportActionBar(toolbar);

        rvDevices = (RecyclerView) findViewById(R.id.rvDeviceList);

        FloatingActionButton fButton = (FloatingActionButton) findViewById(R.id.fab);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        rvAdapterDevices = new MyAdapter(deviceListName);
        rvLayoutManagerDevices = new LinearLayoutManager(getApplicationContext());

        rvDevices.setLayoutManager(rvLayoutManagerDevices);
        rvDevices.setItemAnimator(new DefaultItemAnimator());
        rvDevices.setAdapter(rvAdapterDevices);

        getBluetoothPairedDevices();
    }

    /**
     * get all bluetooth paired devices on this smartphone
     */
    public void getBluetoothPairedDevices(){
        deviceListName.clear();

        if (activateBluetooth()){
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
        }
    }

    /**
     * Check bluetooth compatibility
     * And turn on bluetooth
     */
    public boolean activateBluetooth(){
        if (btAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_incompatible), Toast.LENGTH_LONG).show();
            return false;
        } else if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return true;
        }
        return true;
    }

    void msg(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
