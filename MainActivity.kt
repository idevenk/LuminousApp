
package com.example.luminouswear

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var socket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    private val deviceAddress = "98:D3:21:F8:6F:AB" //  mac

    private val quotes = listOf(
        "",
        "B",
        "Co",
        "Yo",
        "Push beyond limits."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnConnect = findViewById<Button>(R.id.btnConnect)
        val switchLamp = findViewById<Switch>(R.id.switchLamp)
        val seekBrightness = findViewById<SeekBar>(R.id.seekBrightness)
        val txtQuote = findViewById<TextView>(R.id.txtQuote)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // Random Quote
        txtQuote.text = quotes.random()

        // CONNECT BUTTON
        btnConnect.setOnClickListener {
            connectBluetooth()
        }

        //  SWITCH
        switchLamp.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sendData("1") // ON
            } else {
                sendData("0") // OFF
            }
        }

        // BRIGHTNESS
        seekBrightness.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                sendData("B$value") // Send brightness
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun connectBluetooth() {
        val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)
        val uuid = device.uuids[0].uuid

        socket = device.createRfcommSocketToServiceRecord(uuid)
        socket?.connect()
        outputStream = socket?.outputStream

        Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show()
    }

    private fun sendData(data: String) {
        try {
            outputStream?.write(data.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
