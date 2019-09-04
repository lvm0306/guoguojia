package com.lovesosoi.kotlin_shop.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView

import java.util.ArrayList

/**
 * Created by charlie on 2017/4/2.
 * Bluetooth search state braodcastrecever
 * 蓝牙搜索广播监听
 */

class DeviceReceiver(
    deviceList_found: ArrayList<String>
) : BroadcastReceiver() {


    private var deviceList_found = ArrayList<String>()

    init {
        this.deviceList_found = deviceList_found
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (BluetoothDevice.ACTION_FOUND == action) {
            //搜索到的新设备
            val btd = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            //搜索没有配对过的蓝牙设备
            if (btd.bondState != BluetoothDevice.BOND_BONDED) {
                if (!deviceList_found.contains(btd.name + '\n'.toString() + btd.address)) {
                    deviceList_found.add(btd.name + '\n'.toString() + btd.address)
                }
            }
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
            //搜索结束
            for (i in deviceList_found){
                Log.e("blue",i)
            }
        }

    }
}
