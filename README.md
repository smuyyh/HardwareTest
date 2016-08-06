# HardwareTest
Android 各个硬件模块自动化测试。包括LCD、摄像头、键盘、闪光灯、声音、磁盘存储、震动、触摸屏、NFC及各类传感器的测试。

```java
private static final BaseTest[] ALL_ITEMS = {
    new VersionTest(),
    new LCDTest(),
    new BacklightTest(),
    new ReceiverTest(),
    new SpeakerTest(),
    new EarphoneTest(),
    new FmRadioTest(),
    new KeyTest(),
    new VibratorTest(),
    new StorageTest(),
    new CameraTest(),
    new FlashlightTest(),
    new TouchTest(),
    new CompassTest(),
    new ChargerTest(),
    new LoopbackTest(),
    new MicrophoneTest(),
    new GSensorTest(),
    new ProxSensorTest(),
    new LightSensorTest(),
    new NfcTest(),
    new GyroSensorTest(),
};
```
