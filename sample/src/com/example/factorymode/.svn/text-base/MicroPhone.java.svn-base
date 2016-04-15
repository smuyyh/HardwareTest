package com.example.factorymode;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.ztemt.test.basic.R;

import java.io.File;
import java.io.IOException;

public class MicroPhone extends Activity{
	 Button MIChead;
	 MediaRecorder recorder;
	 MediaPlayer   media;
	 File file;
	 boolean isrecorder=false;
	 Button MICSButton,MICFButton;
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	 {  	super.onCreate(savedInstanceState);
		 setContentView(R.layout.microphone2);
		
		
		 MICSButton=(Button)findViewById(R.id.micS);
		 MICFButton=(Button)findViewById(R.id.micF);
		 MIChead=(Button)findViewById(R.id.MICCESHI);
		 //成功按钮的监听
	file=new File(Environment.getExternalStorageDirectory()+"/luyin.mp3");
	MICSButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent MIC_intent=new Intent(MicroPhone.this,MainActivity.class);
				startActivity(MIC_intent);
					}
		});
	 //失败按钮的监听
	 MICFButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent MIC_intent=new Intent(MicroPhone.this,MainActivity.class);
				startActivity(MIC_intent);
					}
		});
	 //头文件按钮的监听
	 MIChead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MICFButton.setClickable(true);
				MICSButton.setClickable(true);
				if(MIChead.getText().toString().equals("开始录音"))
				{	
					try {
						if(recorder==null)
						recorder=new MediaRecorder();
						recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
						recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);   
						recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); 
						if(file.exists())
							file.delete();
						recorder.setOutputFile(Environment.getExternalStorageDirectory()+"/luyin.mp3");
						recorder.prepare();
						recorder.start();
						MIChead.setText("停止录音并播放");
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return ;
				}
				if(MIChead.getText().toString().equals("停止录音并播放"))
				{
					recorder.stop();
					try {
						Thread.sleep(1*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					synchronized (file) {
						if(media==null)
						media=new MediaPlayer();
						try {
							media.setLooping(true);
							media.setDataSource(Environment.getExternalStorageDirectory()+"/luyin.mp3");
							media.prepare();
							media.start();
							
							MIChead.setText("停止播放");
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					return ;
				}
				if(MIChead.getText().toString().equals("停止播放"))
				{	media.stop();
					media.reset();
					MIChead.setText("开始录音");
					return ;
				}
			}
		});
	 MIChead.setText("开始录音");
	 MICFButton.setClickable(false);
	 MICSButton.setClickable(false);
	 
	 
	 }

}
