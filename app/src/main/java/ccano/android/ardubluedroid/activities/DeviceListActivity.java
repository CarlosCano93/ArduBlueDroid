package ccano.android.ardubluedroid.activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import ccano.android.ardubluedroid.R;
import ccano.android.ardubluedroid.adapters.MyAdapter;

public class DeviceListActivity extends AppCompatActivity {

    // recycler view
    private RecyclerView rvDevices;
    private RecyclerView.Adapter rvAdapterDevices;
    private RecyclerView.LayoutManager rvLayoutManagerDevices;

    // bluetooth
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private boolean isBtConnected = false;
    private BluetoothSocket btSocket = null;
    
    // bluetooth list data
    ArrayList<String> deviceListName = new ArrayList<String>();
    ArrayList<String> deviceListMacAdress = new ArrayList<String>();

    // SPP UUID service - this should work for most devices
    private static final UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String macAddress = null; //TODO get macAddress clicked

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

        rvAdapterDevices = new MyAdapter(deviceListName, deviceListMacAdress);
        rvLayoutManagerDevices = new LinearLayoutManager(getApplicationContext());

        rvDevices.setLayoutManager(rvLayoutManagerDevices);
        rvDevices.setItemAnimator(new DefaultItemAnimator());
        rvDevices.setAdapter(rvAdapterDevices);

        getBluetoothPairedDevices();
    }

    public void connectDevice(String macAddress){
        this.macAddress = macAddress;
        new BluetoothConnection().execute();
    }

    private void getBluetoothPairedDevices(){
        if (deviceListName.size() != 0){
            deviceListName.clear();
        }

        if (activateBluetooth()){
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices != null && pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    // use two arrays; with names for the user and other with internal MAC to connect devices
                    deviceListName.add(device.getName());
                    deviceListMacAdress.add(device.getAddress());
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
    private boolean activateBluetooth(){
        if (btAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_incompatible), Toast.LENGTH_LONG).show();
            return false;
        } else if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            return true;
        }
        return true;
    }

    void msg(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    
    //
    // BLUETOOTH ASYNC TASK
    //

    /**
     * AsyncTask is an easy way to execute some code in a background
     * BluetoothConnection gets a new connection with a bluetooth device
     */
    private class BluetoothConnection extends AsyncTask<Void, Void, Void>
    {
        private boolean connectSuccess = true;
        private ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //FIXME change this for just a cirlce load
            progDailog = new ProgressDialog(DeviceListActivity.this);
            progDailog.setMessage(getString(R.string.connection_connecting));
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected Void doInBackground(Void... devices){
            try{
                if (btSocket == null || !isBtConnected){
                    btAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice btDevice = btAdapter.getRemoteDevice(macAddress); //connect to device with MAC
                    btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(DEFAULT_UUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }catch (IOException e){
                connectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!connectSuccess){
                msg(getString(R.string.connection_failed));
            } else{
                msg(getString(R.string.connection_succesfull));
                isBtConnected = true;

                btAdapter.cancelDiscovery();
            }
            if (progDailog.isShowing()){
                progDailog.dismiss();
            }

        }
    }
}
