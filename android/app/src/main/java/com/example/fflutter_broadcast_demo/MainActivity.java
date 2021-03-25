package com.example.fflutter_broadcast_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.annotation.Nullable;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;


public class MainActivity extends FlutterActivity {
    private Context context = this;
    private static final String CHANNEL = "samples.flutter.io/wangz";//值必须跟flutter端一样

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
            //注册广播，监听手机充电状态
            context.registerReceiver(
                    createChargingStateChangeReceiver(eventSink), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }

        //取消监听
        @Override
        public void onCancel(Object o) {
            eventSink = null;
        }
    };

    //电池状态广播监听，发生变化时，会调用这个广播
    private BroadcastReceiver createChargingStateChangeReceiver(final EventChannel.EventSink events) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;
                String message = "null";
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        message = "充电中";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        message = "放电中";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        message = "未充电";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        message = "已充满";
                        break;
                }
                events.success(message);//由于把拿到了EventSink，直接把当前状态通过EventSink发送给flutter
            }
        };
    }


}
