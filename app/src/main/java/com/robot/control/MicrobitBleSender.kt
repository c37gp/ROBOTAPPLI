package com.robot.control

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import java.util.UUID

class MicrobitBleSender(private val context: Context) {

    private var bluetoothGatt: BluetoothGatt? = null
    private var txCharacteristic: BluetoothGattCharacteristic? = null

    private val UART_SERVICE_UUID =
        UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")

    private val TX_UUID =
        UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E")

    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice) {
        // For Android 12+, we need to handle BLUETOOTH_CONNECT permission
        // This should be checked by the caller
        bluetoothGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {

            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    bluetoothGatt = null
                    txCharacteristic = null
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {

                val service = gatt.getService(UART_SERVICE_UUID)
                txCharacteristic = service?.getCharacteristic(TX_UUID)
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun send(command: String) {

        val char = txCharacteristic ?: return
        val gatt = bluetoothGatt ?: return

        char.value = command.toByteArray()

        gatt.writeCharacteristic(char)
    }

    fun disconnect() {
        bluetoothGatt?.close()
        bluetoothGatt = null
        txCharacteristic = null
    }

    fun isConnected(): Boolean {
        return bluetoothGatt != null
    }
}
