import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  //samples.flutter.io/wangz 这个名字要跟Android对应上，如果有iOS需要三端一致(Android iOS Flutter必须一样)
  EventChannel _eventChannel = new EventChannel('samples.flutter.io/wangz');
  String _state = "";

  @override
  void initState() {
    super.initState();
    event();
  }

  Future<void> event() async {
    _eventChannel.receiveBroadcastStream().listen((data) {
      print("事件监听=========》" + data.toString());
      setState(() {
        //有返回值则改变电池状态
        _state = data;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              '当前的充电状态：',
            ),
            Text(
              _state,
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
    );
  }
}
