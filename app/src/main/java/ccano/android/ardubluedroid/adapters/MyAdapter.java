package ccano.android.ardubluedroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import ccano.android.ardubluedroid.R;
import ccano.android.ardubluedroid.activities.DeviceListActivity;

/**
 * Created by Carlos on 15/11/2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> namesDevice;
    private List<String> addressesDevice;

    public String macAddress;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public TableRow tableRow;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.tvDeviceRow);
            tableRow = (TableRow) itemView.findViewById(R.id.trRow);
        }
    }

    public MyAdapter(List<String> namesDevice, List<String> addressesDevice) {
        this.namesDevice = namesDevice;
        this.addressesDevice = addressesDevice;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
        holder.mTextView.setText(namesDevice.get(position));
        holder.tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                if (context instanceof DeviceListActivity){
                    ((DeviceListActivity)context).connectDevice(addressesDevice.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return namesDevice.size();
    }
}
