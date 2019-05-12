package com.example.bluetoothtimer

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, BLE.Callback {

    var timerRuntime = 10

    // variables for how often to run the bouncing ball thread
    private lateinit var readerThread : Thread
    private var connected = false
    private var startedScan = false
    private var destroyThread = false

    private var updatingConfig = false
    private var gotOK = false
    private var boardTemp = 0

    private var ble: BLE? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTimerSlider()
        setupRunTimerButton()
        setupUpdateConfigButton()
        setupBLE()
        startReaderThread()
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyThread = true
    }

    private fun setupTimerSlider()
    {
        TimerDurationSlider.setOnSeekBarChangeListener(this)
        TimerDurationSlider.max = 0
        TimerDurationSlider.max = 50
        TimerDurationSlider.progress = 0
    }

    private fun setupRunTimerButton()
    {
        RunTimerButton.setOnClickListener {
            startTimer()
        }
    }

    private fun setupUpdateConfigButton()
    {
        UpdateConfigButton.setOnClickListener {
            updateConfig()
        }
    }

    private fun setupBLE()
    {
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter != null) {
            if (!adapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

            }
        }

        // Get Bluetooth
        ble = BLE(applicationContext, DEVICE_NAME)

        // Check permissions
        ActivityCompat.requestPermissions(this,
            arrayOf( Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }

    private fun startReaderThread()
    {
        readerThread = Thread {

            while (!destroyThread) {
                runOnUiThread {
                    if (!connected) {
                        if (!startedScan) {
                            startScan()
                        }

                        RunTimerButton.isEnabled = false
                    } else {
                        RunTimerButton.isEnabled = true

                        if(!updatingConfig)
                        {
                            requestTemp()
                            ReadbackText.text = "Arduino Temp = $boardTemp"
                        }
                    }
                }
                // Don't need to run this that often. Only do it every 10 seconds.
                Thread.sleep(10000)
            }
        }

        readerThread.start()
    }

    private fun requestTemp()
    {
        // Ask for the temp
        ble!!.send("TEMP")
    }

    private fun waitForOK() : Boolean
    {
        var loop = 0
        while(!gotOK && loop < 20)
        {
            Thread.sleep(100)
            loop++
        }

        return gotOK
    }

    private fun updateConfig()
    {
        updatingConfig = true
        // Send the current configuration that the use has set.

        gotOK = false

        // length of the timer
        ble!!.send("TIMER $timerRuntime")

        if(!waitForOK())
        {
            ReadbackText.text = "Failed to Get OK"
        }

        // LED color to start from
        val starter = StartingColorFragment as ColorSelectorFragment

        val redStart = starter.redValue
        val greenStart = starter.greenValue
        val blueStart = starter.blueValue

        gotOK = false

        ble!!.send("COLOR1 $redStart $greenStart $blueStart")

        if(!waitForOK())
        {
            ReadbackText.text = "Failed to Get OK"
        }

        // LED color to end with
        val ender = EndingColorFragment as ColorSelectorFragment

        val redEnd = ender.redValue
        val greenEnd = ender.greenValue
        val blueEnd = ender.blueValue

        gotOK = false

        ble!!.send("COLOR2 $redEnd $greenEnd $blueEnd")

        if(!waitForOK())
        {
            ReadbackText.text = "Failed to Get OK"
        }

        // If the device should buzz at the end
        var buzz = 0
        if (BuzzSwitch.isChecked) {
            buzz = 1
        }

        gotOK = false

        ble!!.send("BUZZ $buzz")

        if(!waitForOK())
        {
            ReadbackText.text = "Failed to Get OK"
        }

        updatingConfig = false
    }

    private fun startTimer()
    {
        updateConfig()
        ble!!.send("RUNTIMER")
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {
        timerRuntime = progress + 10

        TimerDurationText.text = "Timer Duration: $timerRuntime (seconds)"
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

    override fun onResume() {
        super.onResume()
        ble!!.registerCallback(this)
        connected = false
        startedScan = false
    }

    override fun onStop() {
        super.onStop()
        ble!!.unregisterCallback(this)
        ble!!.disconnect()
        connected = false
        startedScan = false
    }

    private fun startScan() {
        ble!!.connectFirstAvailable()
        startedScan = true
        ReadbackText.text = "Attempting to connect to bluetooth device"
    }

    /**
     * Called when a UART device is discovered (after calling startScan)
     * @param device: the BLE device
     */
    override fun onDeviceFound(device: BluetoothDevice) {

        Log.i("BLE", "Found device : " + device.name)
        Log.i("BLE", "Waiting for a connection ...")
    }

    /**
     * Prints the devices information
     */
    override fun onDeviceInfoAvailable() {
        Log.i("BLE", ble!!.deviceInfo)
    }

    /**
     * Called when UART device is connected and ready to send/receive data
     * @param uart: the BLE UART object
     */
    override fun onConnected(uart: BLE) {
        Log.i("BLE", "Connected!")
        connected = true
        startedScan = false
    }

    /**
     * Called when some error occurred which prevented UART connection from completing
     * @param uart: the BLE UART object
     */
    override fun onConnectFailed(uart: BLE) {
        Log.i("BLE", "Error connecting to device!")
        startedScan = false
    }

    /**
     * Called when the UART device disconnected
     * @param uart: the BLE UART object
     */
    override fun onDisconnected(uart: BLE) {
        Log.i("BLE", "Disconnected!")
        connected = false
        startedScan = false
    }

    /**
     * Called when data is received by the UART
     * @param uart: the BLE UART object
     * @param rx: the received characteristic
     */
    override fun onReceive(uart: BLE, rx: BluetoothGattCharacteristic) {
        Log.i("BLE", "Received value: " + rx.getStringValue(0))

        val recieved = rx.getStringValue(0)

        when
        {
            recieved.startsWith("TEMP") -> {
                try {
                    val temp = recieved.substring("TEMP".length + 1)
                    boardTemp = temp.toInt()
                } catch (e:Exception)
                {

                }
            }
            recieved.startsWith("OK") ->
            {
                gotOK = true
            }
        }
    }

    companion object {
        private const val DEVICE_NAME = "Weston_BLE_Arduino"
        private const val REQUEST_ENABLE_BT = 0
    }
}
