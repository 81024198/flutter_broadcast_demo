# flutter_broadcast_demo

flutter监听Android广播，达到混合开发目的.
由于工业级设备，比如快递员手持扫码枪，当他扫码的时候我们需要获取到这个设备扫描的二维码信息。
而市面上的设备大部分都是广播扫码或sdk扫码，所以需要监听Android广播需求

## Getting Started

Flutter **main.dart** **32 - 49行** 代码比较重要

Android **MainActiviy.java 20行到结束** 代码比较重要

可以看下代码注释

本demo监听了充电状态，如果拔掉充电器 ui则会刷新状态，插上也会刷新状态

替换成自己的广播即可跟Android混合开发，也可以不用广播，大家自行决断