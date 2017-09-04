package com.iandanico.tipapplication;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.ToggleButton;

/**
 * Created by nekomarino on 9/3/17.
 */

public class WhosAroundMe extends Fragment {
    ToggleButton bluOnOff;
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.whos_around_me, container, false);
        Switch bluOnOff = (Switch) myView.findViewById(R.id.switch1);

        final boolean btstatus = checkIfBluetoothIsOnOrOff();
        bluOnOff.setChecked(btstatus);
        bluOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean btstatus = checkIfBluetoothIsOnOrOff();
                setBluetooth(!btstatus);
                Log.d("MyTagGoesHere", Boolean.toString(btstatus));
            }
        });
        return myView;
    }





    public static boolean checkIfBluetoothIsOnOrOff(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.enable();
    }
    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }
}
