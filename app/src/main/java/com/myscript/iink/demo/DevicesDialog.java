package com.myscript.iink.demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myscript.iink.demo.models.CustomBluetoothDevice;

import java.util.ArrayList;
import java.util.Set;

public class DevicesDialog extends DialogFragment {
    RecyclerView mRecyclerView;
    public static DeviceSelectedListener mListener;
//    PairedDeviceAdapter mPairedDeviceAdapter;
    BluetoothAdapter mBluetoothAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.devices_dialog, container, false);
        mRecyclerView = rootView.findViewById(R.id.PairedDeviceRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<CustomBluetoothDevice> devices = new ArrayList<>();
        CustomBluetoothDevice m;

        for (BluetoothDevice device : pairedDevices) {
            m= new CustomBluetoothDevice();
            m.setMacAddress(device.getAddress());
            m.setName(device.getName());
            devices.add(m);
        }
//
//
        PairedDeviceAdapter mPairedDeviceAdapter = new PairedDeviceAdapter(this,devices);
        mRecyclerView.setAdapter(mPairedDeviceAdapter);
      return rootView;
    }



    public interface DeviceSelectedListener {
        void onDataEntryComplete(CustomBluetoothDevice device);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (DeviceSelectedListener) context;
    }

        public static class PairedDeviceAdapter extends RecyclerView.Adapter<PairedDeviceAdapter.PairedDeviceHolder> {


            private static final String TAG = "From Devices Adapter";
            private final ArrayList<CustomBluetoothDevice> devices;
            private final DevicesDialog c;
            Context mContext;
            public PairedDeviceAdapter(DevicesDialog c, ArrayList<CustomBluetoothDevice> devices) {
                this.c = c;
                this.devices = devices;
            }

            @NonNull
            @Override
            public PairedDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, null);
                mContext  = parent.getContext();
                return new PairedDeviceHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull PairedDeviceHolder holder, final int position) {
                holder.deviceName.setText(devices.get(position).getName().trim());
                holder.deviceMacAddress.setText(devices.get(position).getMacAddress().trim());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "You Touched" + devices.get(position).getName(), Toast.LENGTH_SHORT);
                        toast.show();
                        DevicesDialog.mListener.onDataEntryComplete(devices.get(position));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return devices.size();
            }


            class PairedDeviceHolder extends RecyclerView.ViewHolder{
                TextView deviceName;
                TextView deviceMacAddress;
                View mView;
                PairedDeviceHolder(@NonNull View itemView) {
                    super(itemView);
                    this.deviceName =itemView.findViewById(R.id.deviceName);
                    this.deviceMacAddress =itemView.findViewById(R.id.deviceMacAddress);
                    this.mView = itemView;
                }
            }
        }


    }
