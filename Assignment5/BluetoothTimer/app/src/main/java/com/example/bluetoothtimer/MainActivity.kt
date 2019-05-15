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


    // variables for how often to run the bouncing ball thread
    private lateinit var readerThread : Thread
    private var connected = false
    private var startedScan = false
    private var destroyThread = false

    private var startTheTimer = false

    private var gotOK = false
    private var boardTemp = 0

    private var ble: BLE? = null

    private var timerRuntime = 10
    private var lastTimerRuntime = -1

    private var lastStartColor = arrayOf(-1, -1, -1)

    private var lastEndColor = arrayOf(-1, -1, -1)

    private var lastBuzz = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTimerSlider()
        setupRunTimerButton()
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
            startTheTimer = true
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
                if (!connected) {
                    if (!startedScan) {
                        resetStored()
                        startScan()
                    }

                    runOnUiThread {
                        RunTimerButton.isEnabled = false
                    }
                } else {
                    runOnUiThread {
                        RunTimerButton.isEnabled = true
                    }

                    if(startTheTimer)
                    {
                        startTimer()
                        startTheTimer = false
                    }
                    requestTemp()
                    updateConfig()
                }
                // Don't need to run this that often. Only do it every 1 second.
                Thread.sleep(1000)
            }
        }

        readerThread.start()
    }

    private fun resetStored()
    {
       lastTimerRuntime = -1

        lastStartColor = arrayOf(-1, -1, -1)

        lastEndColor = arrayOf(-1, -1, -1)

        lastBuzz = -1
    }

    private fun requestTemp()
    {
        sendCommand("TEMP")
    }

    private fun sendCommand(command : String)
    {
        gotOK = false

        ble!!.send(command)

        var loop = 0
        while(!gotOK && loop < 20)
        {
            Thread.sleep(100)
            loop++
        }
    }

    private fun updateConfig()
    {
        // Send the current configuration that the use has set.
        updateTimerLength()
        updateStartColor()
        updateEndColor()
        updateBuzz()
    }

    private fun updateTimerLength()
    {
        val timerSnapshot = timerRuntime

        // Timer is out of date
        if(lastTimerRuntime != timerSnapshot)
        {
            sendCommand("TIMER $timerSnapshot")

            if(gotOK)
            {
                lastTimerRuntime = timerSnapshot
            }
        }
    }

    private fun updateStartColor()
    {
        // LED color to start from
        val starter = StartingColorFragment as ColorSelectorFragment

        val redStart = starter.redValue
        val greenStart = starter.greenValue
        val blueStart = starter.blueValue

        val startColorSnapshot = arrayOf(redStart, greenStart, blueStart)

        if(lastStartColor[0] != startColorSnapshot[0] ||
            lastStartColor[1] != startColorSnapshot[1] ||
            lastStartColor[2] != startColorSnapshot[2])
        {
            sendCommand("COLOR1 $redStart $greenStart $blueStart")

            if(gotOK)
            {
                lastStartColor = startColorSnapshot
            }
        }
    }

    private fun updateEndColor()
    {
        // LED color to end with
        val ender = EndingColorFragment as ColorSelectorFragment

        val redEnd = ender.redValue
        val greenEnd = ender.greenValue
        val blueEnd = ender.blueValue

        val endColorSnapshot = arrayOf(redEnd, greenEnd, blueEnd)

        if(lastEndColor[0] != endColorSnapshot[0] ||
            lastEndColor[1] != endColorSnapshot[1] ||
            lastEndColor[2] != endColorSnapshot[2])
        {
            sendCommand("COLOR2 $redEnd $greenEnd $blueEnd")

            if(gotOK)
            {
                lastEndColor = endColorSnapshot
            }
        }
    }

    private fun updateBuzz()
    {
        var buzzSnapshot = 0
        if (BuzzSwitch.isChecked) {
            buzzSnapshot = 1
        }

        if(lastBuzz != buzzSnapshot)
        {
            sendCommand("BUZZ $buzzSnapshot")

            if(gotOK)
            {
                lastBuzz = buzzSnapshot
            }
        }
    }

    private fun startTimer()
    {
        updateConfig()
        sendCommand("RUNTIMER")
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
        runOnUiThread {
            ReadbackText.text = "Attempting to connect to bluetooth device"
        }
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
                    runOnUiThread {
                        ReadbackText.text = "Arduino Temp = $boardTemp"
                    }
                    gotOK = true
                } catch (e:Exception)
                {
                    // If we have an issue with getting the int, just wait for the next round.
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
