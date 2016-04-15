package com.example.factorymode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.ztemt.test.basic.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 20130901
 * @author tong
 *
 */


public class headset extends Activity{
	Button HSButton,HFButton,HHead;
	boolean  isRecorder=false;
	MediaRecorder recorder;
	MediaPlayer media;
	int ceshi_num=0;
	AudioManager  audiomanager;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{ super.onCreate(savedInstanceState);
		setContentView(R.layout.headset);
		media=new MediaPlayer();
		HHead=(Button)findViewById(R.id.ceshi);
		HSButton=(Button)findViewById(R.id.HSB);
		HFButton=(Button)findViewById(R.id.HFB);
		//成功按钮相应
		File mediafile=new File(Environment.getExternalStorageDirectory()+"/fukua.mp3");
		if(!mediafile.exists())
			writeRFile2DDR(R.raw.fukua,"fukua.mp3");
		HSButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent BSB_intent=new Intent(headset.this,MainActivity.class);
				startActivity(BSB_intent);
			}
		});

		//失败按钮
		HFButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent BFB_intent=new Intent(headset.this,MainActivity.class);
				startActivity(BFB_intent);
			}
		});
		//测试按钮
		HHead.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if((isRecorder==true)&&(ceshi_num==0))
				{ HHead.setText("停止播放");
					try {
						media.reset();
						media.setDataSource(Environment.getExternalStorageDirectory()+"/fukua.mp3");
						media.prepare();
						media.start();
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
				else if((isRecorder==true)&&(ceshi_num==1))
				{HHead.setText("开始播放");
					media.stop();
					ceshi_num=0;
				}

				// TODO Auto-generated method stub

				ceshi_num++;}

		});
		audiomanager= (AudioManager) headset.this.getSystemService(Context.AUDIO_SERVICE) ;
		if(audiomanager.isWiredHeadsetOn()==true)
		{ HHead.setText("开始播放");
			isRecorder=true;}
		else
		{ HHead.setText("请插入耳机");
			HHead.setClickable(false);
			HSButton.setClickable(false);
		}
	}
	public void writeRFile2DDR(int RID,String filename){
		try
		{
			InputStream is = getResources().openRawResource(RID);
			FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() +"/"+filename);
			byte[] buffer = new byte[8192];
			int count = 0;
			while((count=is.read(buffer)) > 0)
			{
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}



}
