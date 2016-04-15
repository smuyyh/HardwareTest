package com.example.factorymode;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ztemt.test.basic.R;

public class Blutooth extends Activity{
	int  LENGTH_LONG =0x00000001;
	int  lanya_swich=0;
	TextView mTextView;
	Button BSButton,BFButton;
	BluetoothAdapter mBluetoothAdapter;
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			String action = intent.getAction();
			// 获得已经搜索到的蓝牙设备
			if (action.equals(BluetoothDevice.ACTION_FOUND)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 搜索到的不是已经绑定的蓝牙设备
				// 显示在TextView上
				mTextView.append(device.getName() + ":"
						+ device.getAddress()+"\n");
				// 搜索完成
			} else if (action
					.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle("搜索蓝牙设备");
			}
		}
	};
	private BroadcastReceiver bReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			String action = intent.getAction();
			if(lanya_swich==0)
			{	lanya_swich++;
				try {
					Thread.sleep(3*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				onClick_Search();
			}
			else
			{lanya_swich=0;
			}

		}
	};
	public void onCreate(Bundle savedInstanceState)
	{  super.onCreate(savedInstanceState);
		setContentView(R.layout.blutooth);
		BSButton=(Button)findViewById(R.id.BSButton);
		BFButton=(Button)findViewById(R.id.BFButton);
		mTextView = (TextView) findViewById(R.id.tvDevices);
		//成功按钮相应
		BSButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent BSB_intent=new Intent(Blutooth.this,MainActivity.class);
				startActivity(BSB_intent);
				//测试完成后关闭蓝牙
				if(mBluetoothAdapter.isEnabled())
					mBluetoothAdapter.disable();

			}
		});
		//失败按钮点击事件
		BFButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent BFB_intent=new Intent(Blutooth.this,MainActivity.class);
				startActivity(BFB_intent);
				//测试完成后关闭蓝牙  无论测试结果如何
				if(mBluetoothAdapter.isEnabled())
					mBluetoothAdapter.disable();
			}
		});

		//初始化蓝牙适配器
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			//直接返回失败
		}
		//打开蓝牙
		if (!mBluetoothAdapter.isEnabled()) {
			mTextView.setText("蓝牙正在打开.....");
			mBluetoothAdapter.enable();
		}
		else
		{mBluetoothAdapter.disable();
			mTextView.setText("蓝牙正在打开.....");
			mBluetoothAdapter.enable();
		}
		//搜索刚发现的设备
		IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, mFilter);
		//搜索注册完的设备
		mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, mFilter);

		IntentFilter bFilter  =new IntentFilter( BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(bReceiver, bFilter);




	}
	public void onClick_Search() {
		setProgressBarIndeterminateVisibility(true);
		mTextView.clearComposingText();
		mTextView.setText("开始搜索蓝牙设备.....");
		// 如果正在搜索，就先取消搜索
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}
		// 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
		mBluetoothAdapter.startDiscovery();
	}
}
