package com.suthar.jay.ma_001_beta;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.neurosky.connection.ConnectionStates;
import com.neurosky.connection.DataType.MindDataType;
import com.neurosky.connection.EEGPower;
import com.neurosky.connection.TgStreamHandler;
import com.neurosky.connection.TgStreamReader;


public class MindWave_Start extends AppCompatActivity {
    private static final String TAG = MindWave_Start.class.getSimpleName();
    private TgStreamReader tgStreamReader;
    PowerManager screenWake;
    private TextView tv_ps = null;
    private TextView tv_attention = null;
    private TextView tv_meditation = null;
    private TextView tv_delta = null;
    private TextView tv_theta = null;
    private TextView tv_lowalpha = null;

    private TextView tv_highalpha = null;
    private TextView tv_lowbeta = null;
    private TextView tv_highbeta = null;

    private TextView tv_lowgamma = null;
    private TextView tv_middlegamma = null;
    private TextView tv_badpacket = null;

    private Button btn_start = null;
    private Button btn_stop = null;
    private Button btn_selectdevice = null;
    //TODO Jay :-
    private EditText ip = null;
    String espIp = null;
    // TODO END Jay:-//
    Context con;
    private BluetoothAdapter mBluetoothAdapter;
    Intent i2;
    RequestQueue queue;
    PowerManager.WakeLock srON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mind_wave__start);
        tv_ps = findViewById(R.id.tv_ps);
        tv_attention = findViewById(R.id.tv_attention);
        tv_meditation = findViewById(R.id.tv_meditation);
        tv_delta = findViewById(R.id.tv_delta);
        tv_theta = findViewById(R.id.tv_theta);
        tv_lowalpha = findViewById(R.id.tv_lowalpha);

        tv_highalpha = findViewById(R.id.tv_highalpha);
        tv_lowbeta = findViewById(R.id.tv_lowbeta);
        tv_highbeta = findViewById(R.id.tv_highbeta);
        con = getApplication();
        tv_lowgamma = findViewById(R.id.tv_lowgamma);
        tv_middlegamma = findViewById(R.id.tv_middlegamma);
        tv_badpacket = findViewById(R.id.tv_badpacket);
        ip = findViewById(R.id.IP);
        queue = Volley.newRequestQueue(MindWave_Start.this);
        i2 = getIntent();
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        screenWake = (PowerManager) getSystemService(con.POWER_SERVICE);
        srON = screenWake.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        srON.acquire();
        initView();
        try {

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableIntent);


            }
        } catch (Exception e) {

            e.printStackTrace();
            Log.i(TAG, "error:" + e.getMessage());
            return;
        }
        tgStreamReader = new TgStreamReader(mBluetoothAdapter, tgStreamHandler);
        // (2) Demo of setGetDataTimeOutTime, the default time is 5s, please call it before connect() of connectAndStart()
        tgStreamReader.setGetDataTimeOutTime(16);
        // (3) Demo of startLog, you will get more sdk log by logcat if you call this function
        tgStreamReader.startLog();

    }

    private void initView() {
        tv_ps = findViewById(R.id.tv_ps);
        tv_attention = findViewById(R.id.tv_attention);
        tv_meditation = findViewById(R.id.tv_meditation);
        tv_delta = findViewById(R.id.tv_delta);
        tv_theta = findViewById(R.id.tv_theta);
        tv_lowalpha = findViewById(R.id.tv_lowalpha);

        tv_highalpha = findViewById(R.id.tv_highalpha);
        tv_lowbeta = findViewById(R.id.tv_lowbeta);
        tv_highbeta = findViewById(R.id.tv_highbeta);

        tv_lowgamma = findViewById(R.id.tv_lowgamma);
        tv_middlegamma = findViewById(R.id.tv_middlegamma);
        tv_badpacket = findViewById(R.id.tv_badpacket);


        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "Trying to Connect...", Toast.LENGTH_SHORT).show();
                final int REQUEST_CALL = 1;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MindWave_Start.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CALL);
                }
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MindWave_Start.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CALL);
                }

                if (tgStreamReader != null && tgStreamReader.isBTConnected()) {

                    // Prepare for connecting
                    tgStreamReader.stop();
                    tgStreamReader.close();
                }
                espIp = ip.getText().toString();
                Toast.makeText(getApplicationContext(),"IP:"+espIp+" Got Selected",Toast.LENGTH_SHORT).show();
                // (4) Demo of  using connect() and start() to replace connectAndStart(),
                // please call start() when the state is changed to STATE_CONNECTED
                tgStreamReader.connect();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // TODO Auto-generated method stub
                tgStreamReader.stop();
                tgStreamReader.close();
            }

        });

        btn_selectdevice = findViewById(R.id.btn_selectdevice);

        btn_selectdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "Trying to Select Device", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void stop() {
        if (tgStreamReader != null) {
            tgStreamReader.stop();
            tgStreamReader.close();
        }
    }

    @Override
    protected void onDestroy() {
        //(6) use close() to release resource
        if (tgStreamReader != null) {
            tgStreamReader.close();
            tgStreamReader = null;
        }
        srON.release();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    private TgStreamHandler tgStreamHandler = new TgStreamHandler() {

        @Override
        public void onDataReceived(int datatype, int data, Object obj) {
            Message msg = LinkDetectedHandler.obtainMessage();
            msg.what = datatype;
            msg.arg1 = data;
            msg.obj = obj;
            LinkDetectedHandler.sendMessage(msg);
        }

        @Override
        public void onStatesChanged(int connectionStates) {
            Log.d(TAG, "connectionStates change to: " + connectionStates);
            switch (connectionStates) {
                case ConnectionStates.STATE_CONNECTING:
                    // Do something when connecting
                    showToast("STATE_CONNECTING", Toast.LENGTH_SHORT);
                    break;
                case ConnectionStates.STATE_CONNECTED:
                    // Do something when connected
                    tgStreamReader.start();
                    showToast("Connected", Toast.LENGTH_SHORT);
                    break;
                case ConnectionStates.STATE_WORKING:
                    // Do something when working

                    //(9) demo of recording raw data , stop() will call stopRecordRawData,
                    //or you can add a button to control it.
                    //You can change the save path by calling setRecordStreamFilePath(String filePath) before startRecordRawData
                    tgStreamReader.startRecordRawData();

                    break;
                case ConnectionStates.STATE_GET_DATA_TIME_OUT:
                    // Do something when getting data timeout

                    //(9) demo of recording raw data, exception handling
                    tgStreamReader.stopRecordRawData();

                    showToast("Get data time out!", Toast.LENGTH_SHORT);
                    break;
                case ConnectionStates.STATE_STOPPED:
                    // Do something when stopped
                    // We have to call tgStreamReader.stop() and tgStreamReader.close() much more than
                    // tgStreamReader.connectAndstart(), because we have to prepare for that.

                    Toast.makeText(getApplicationContext(), "STATE_STOPPED", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionStates.STATE_DISCONNECTED:
                    // Do something when disconnected
                    Toast.makeText(getApplicationContext(), "STATE_DISCONNECTED", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionStates.STATE_ERROR:
                    Toast.makeText(getApplicationContext(), "STATE_ERROR", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionStates.STATE_FAILED:
                    // Do something when you get failed message
                    // It always happens when open the BluetoothSocket error or timeout
                    // Maybe the device is not working normal.
                    // Maybe you have to try again
                    //  Toast.makeText(getApplicationContext(), "STATE_FAILED", Toast.LENGTH_SHORT).show();
                    break;
            }
            Message msg = LinkDetectedHandler.obtainMessage();
            msg.what = MSG_UPDATE_STATE;
            msg.arg1 = connectionStates;
            LinkDetectedHandler.sendMessage(msg);
        }

        @Override
        public void onChecksumFail(byte[] bytes, int i, int i1) {

        }

        @Override
        public void onRecordFail(int i) {

        }
    };

    private static final int MSG_UPDATE_BAD_PACKET = 1001;
    private static final int MSG_UPDATE_STATE = 1002;

    int raw;
    private Handler LinkDetectedHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // (8) demo of MindDataType
            switch (msg.what) {
                case MindDataType.CODE_RAW:

                    break;
                case MindDataType.CODE_MEDITATION:
                    Log.d(TAG, "HeadDataType.CODE_MEDITATION " + msg.arg1);
                    if (msg.arg1 == 0) {
                        LEDMED0();
                    } else if (msg.arg1 <= 20 && msg.arg1 > 0) {
                        LEDMED20();
                    } else if (msg.arg1 <= 40 && msg.arg1 > 20) {
                        LEDMED2140();
                    } else if (msg.arg1 <= 60 && msg.arg1 > 40) {
                        LEDMED4160();
                    } else if (msg.arg1 <= 80 && msg.arg1 > 60) {
                        LEDMED6180();
                    } else if (msg.arg1 <= 100 && msg.arg1 > 80) {
                        LEDMED081100();
                    }
                    tv_meditation.setText("" + msg.arg1);
                    break;
                case MindDataType.CODE_ATTENTION:
                    Log.d(TAG, "CODE_ATTENTION " + msg.arg1);
                    if (msg.arg1 == 0) {
                        LEDATT0();
                    } else if (msg.arg1 <= 20 && msg.arg1 > 0) {
                        LEDATT20();
                    } else if (msg.arg1 <= 40 && msg.arg1 > 20) {
                        LEDATT2140();
                    } else if (msg.arg1 <= 60 && msg.arg1 > 40) {
                        LEDATT4160();
                    } else if (msg.arg1 <= 80 && msg.arg1 > 60) {
                        LEDATT6180();
                    } else if (msg.arg1 <= 100 && msg.arg1 > 80) {
                        LEDATT081100();
                    }

                    tv_attention.setText("" + msg.arg1);
                    break;
                case MindDataType.CODE_EEGPOWER:
                    EEGPower power = (EEGPower) msg.obj;
                    if (power.isValidate()) {
                        tv_delta.setText("" + power.delta);
                        tv_theta.setText("" + power.theta);
                        tv_lowalpha.setText("" + power.lowAlpha);
                        tv_highalpha.setText("" + power.highAlpha);
                        tv_lowbeta.setText("" + power.lowBeta);
                        tv_highbeta.setText("" + power.highBeta);
                        tv_lowgamma.setText("" + power.lowGamma);
                        tv_middlegamma.setText("" + power.middleGamma);
                    }
                    break;
                case MindDataType.CODE_POOR_SIGNAL://
                    int poorSignal = msg.arg1;
                    Log.d(TAG, "poorSignal:" + poorSignal);
                    tv_ps.setText("" + msg.arg1);

                    break;
                case MSG_UPDATE_BAD_PACKET:
                    tv_badpacket.setText("" + msg.arg1);

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void showToast(final String msg, final int timeStyle) {
        MindWave_Start.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), msg, timeStyle).show();
            }
        });
    }

    public void tog1On() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/toggle1ON",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDATT0() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDATT0",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDATT20() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDATT20",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDATT2140() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDATT2140",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDATT4160() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDATT4160",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDATT6180() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDATT6180",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDATT081100() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDATT81100",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    public void LEDMED0() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDATT0",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDMED20() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDMED20",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDMED2140() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDMED2140",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDMED4160() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDMED4160",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDMED6180() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDMED6180",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void LEDMED081100() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + espIp + "/LEDMED81100",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "LED 1 ON", Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}