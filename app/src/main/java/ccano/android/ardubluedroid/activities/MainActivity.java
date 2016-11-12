package ccano.android.ardubluedroid.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ccano.android.ardubluedroid.R;

public class MainActivity extends AppCompatActivity {

    private String address = null;
    private boolean isBtConnected = false;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;

    // SPP UUID service - this should work for most devices
    private static final UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // Arduino receive this and set HIGH led
    private static final String LED_ON = "L1";
    private static final String LED_OFF = "L2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // get MAC address from BluetoothDevicesActivity
        Intent i = getIntent();
        address = i.getStringExtra(BluetoothDevicesActivity.EXTRA_DEVICE_ADDRESS);

        new BluetoothConnection().execute();
    }

    // go back to device list activity
    @OnClick(R.id.btSearchDevices)
    public void handleButtonSearchDevice(){
        finish();
    }

    @OnClick(R.id.btOn)
    void handleButtonOn(){
        write(LED_ON);
    }

    @OnClick(R.id.btOff)
    void handleButtonOff(){
        write(LED_OFF);
    }

    /**
     * Send a code via BluetoothSocket
     * That code is recived by Arduino and then it turn on/off the led (or anything)
     * @param codeToSend
     */
    void write (String codeToSend){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write(codeToSend.toString().getBytes());
            }catch (IOException e){
                showMessage(getString(R.string.error));
            }
        }
    }

    /**
     * Easy to show a message in Toast
     * @param message
     */
    void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * AsyncTask is an easy way to execute some code in a background
     * BluetoothConnection gets a new connection with a bluetooth device
     */
    private class BluetoothConnection extends AsyncTask<Void, Void, Void>
    {
        private boolean connectSuccess = true;

        @Override
        protected Void doInBackground(Void... devices){
            try{
                if (btSocket == null || !isBtConnected){
                    btAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice btDevice = btAdapter.getRemoteDevice(address); //connect to device with MAC
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
                showMessage(getString(R.string.connection_failed));
                finish();
            } else{
                showMessage(getString(R.string.connection_succesfull));
                isBtConnected = true;
            }
        }
    }
}



