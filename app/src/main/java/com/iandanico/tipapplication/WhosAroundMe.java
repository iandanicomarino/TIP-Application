package com.iandanico.tipapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.support.v4.app.ListFragment;

import com.google.android.gms.internal.ada;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by nekomarino on 9/3/17.
 */

public class WhosAroundMe extends ListFragment{
    ListView btList;
    Switch bluOnOff;
    IntentFilter filter;
    boolean btstatus;
    static ArrayAdapter adapter;
    static ArrayList<String> mobileArray = new ArrayList<String>();
    static BluetoothAdapter bluetoothAdapter;


    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.whos_around_me, container, false);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.bt_list_view, mobileArray);
        bluOnOff = (Switch) myView.findViewById(R.id.switch1);
        setListAdapter(adapter);

        //handle bluetooth intents
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        btstatus = checkIfBluetoothIsOnOrOff();
        bluOnOff.setChecked(btstatus);

        bluOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btstatus = checkIfBluetoothIsOnOrOff();
                mobileArray.add("Starting Discovery");
                adapter.notifyDataSetChanged();
                setBluetooth(!btstatus);
            }
        });
        return myView;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                mobileArray.add(deviceName);
                adapter.notifyDataSetChanged();
            }
        }
    };

    public static boolean checkIfBluetoothIsOnOrOff(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.enable();
    }

    public static void setBluetooth(boolean enable) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            mobileArray.clear();
            adapter.notifyDataSetChanged();
            bluetoothAdapter.disable();
        }

    }

}
