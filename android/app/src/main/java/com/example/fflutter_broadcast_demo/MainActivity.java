package com.example.fflutter_broadcast_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;


public class MainActivity extends FlutterActivity {
    private Context context = this;
    private static final String CHANNEL = "samples.flutter.io/wangz";//值必须跟flutter端一样

    // Constants for Broadcast Receiver defined below.
    public static final String ACTION_BROADCAST_RECEIVER = "com.android.decodewedge.decode_action";
    public static final String CATEGORY_BROADCAST_RECEIVER = "com.android.decodewedge.decode_category";
    public static final String EXTRA_BARCODE_DATA = "com.android.decode.intentwedge.barcode_data";
    public static final String EXTRA_BARCODE_STRING = "com.android.decode.intentwedge.barcode_string";
    public static final String EXTRA_BARCODE_TYPE = "com.android.decode.intentwedge.barcode_type";
    public static final String ACTION_START_DECODE = "com.datalogic.decode.action.START_DECODE";
    public static final String ACTION_STOP_DECODE = "com.datalogic.decode.action.STOP_DECODE";
    private BroadcastReceiver receiver = null;
    private IntentFilter filter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CHARGING_CHANNEL通道名
        EventChannel eventChannel = new EventChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL);
        //设置监听对象，此方式flutter端不用await等待返回值，而是监听返回值，可以发送多次消息
        eventChannel.setStreamHandler(streamHandler);
    }

    //事件接受对象
    private EventChannel.EventSink eventSink = null;//事件对应，可通过这个对象发送数据给flutter端

    //没有flutter基础的，推荐看看stream在来看这段代码
    private EventChannel.StreamHandler streamHandler = new EventChannel.StreamHandler() {
        //事件监听
        @Override
        public void onListen(Object o, EventChannel.EventSink sink) {
            eventSink = sink;
        }

        //取消监听
        @Override
        public void onCancel(Object o) {
            eventSink = null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.MODEL.equals("m71") || Build.MODEL.equals("m71k")) {
            receiver = new DecodeWedgeIntentReceiver();
            filter = new IntentFilter();
            filter.addAction(ACTION_BROADCAST_RECEIVER);
            filter.addCategory(CATEGORY_BROADCAST_RECEIVER);
            registerReceiver(receiver, filter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.MODEL.equals("m71") || Build.MODEL.equals("m71k")) {
            unregisterReceiver(receiver);
            receiver = null;
            filter = null;
        }
    }

    // Receives action ACTION_BROADCAST_RECEIVER
    public class DecodeWedgeIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent wedgeIntent) {
            String action = wedgeIntent.getAction();
            if (action.equals(ACTION_BROADCAST_RECEIVER)) {
                String barcodestring = wedgeIntent.getStringExtra(EXTRA_BARCODE_STRING);
                eventSink.success(barcodestring);
            }
        }
    }


}
