package com.robot.control

import android.bluetooth.*
import android.content.Context
import java.util.*

class MicrobitBleSender(private val context: Context) {

    private var bluetoothGatt: BluetoothGatt? = null
    private var txCharacteristic: BluetoothGattCharacteristic? = null

    private val UART_SERVICE_UUID =
        UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")

    private val TX_UUID =
        UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E")

    fun connect(device: BluetoothDevice) {

        bluetoothGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {

            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {

                val service = gatt.getService(UART_SERVICE_UUID)
                txCharacteristic = service?.getCharacteristic(TX_UUID)
            }
        })
    }

    fun send(command: String) {

        val char = txCharacteristic ?: return

        char.value = command.toByteArray()

        bluetoothGatt?.writeCharacteristic(char)
    }

    fun disconnect() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
}
