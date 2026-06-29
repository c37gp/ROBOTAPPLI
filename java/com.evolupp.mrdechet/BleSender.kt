package com.evolupp.mrdechet

import android.content.Context
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothDevice
import java.util.UUID

class BleSender(private val context: Context) {

    private var gatt: BluetoothGatt? = null

    // UART micro:bit BLE
    private val UART_SERVICE_UUID =
        UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")

    private val UART_CHAR_UUID =
        UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")

    fun connect(device: BluetoothDevice) {
        gatt = device.connectGatt(context, false, object : BluetoothGattCallback() {})
    }

    fun send(cmd: String) {
        val g = gatt ?: return

        val service = g.getService(UART_SERVICE_UUID)
        val characteristic = service.getCharacteristic(UART_CHAR_UUID)

        characteristic.value = (cmd + "\n").toByteArray()
        g.writeCharacteristic(characteristic)
    }
}
