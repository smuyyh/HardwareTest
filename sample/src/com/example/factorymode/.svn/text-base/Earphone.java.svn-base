package com.example.factorymode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.ztemt.test.basic.R;

import java.io.FileOutputStream;
import java.io.InputStream;

public class Earphone extends Activity{
	AudioManager audioManager;
	Button EarSButton,EarFButton;
	MediaPlayer mMediaPlayer=null;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{ super.onCreate(savedInstanceState);
		setContentView(R.layout.earphone2);
		EarSButton=(Button)findViewById(R.id.earsbutton);
		EarFButton=(Button)findViewById(R.id.earfbutton);
		//成功点击事件
		EarSButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent BSB_intent=new Intent(Earphone.this,MainActivity.class);
				startActivity(BSB_intent);
				mMediaPlayer.stop();
				mMediaPlayer.reset();
				mMediaPlayer.release();
				audioManager.setMode(AudioManager.MODE_NORMAL);

			}
		});
		//失败点击事件
		EarFButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent BSB_intent=new Intent(Earphone.this,MainActivity.class);
				startActivity(BSB_intent);
				mMediaPlayer.stop();
				mMediaPlayer.reset();
				mMediaPlayer.release();
				audioManager.setMode(AudioManager.MODE_NORMAL);
			}
		});

		audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setMode(AudioManager.MODE_IN_CALL);// 把模式调成听筒放音模式
		//这里的资源文件要求要小点的文件，要注意
		try {mMediaPlayer = MediaPlayer.create(Earphone.this, R.raw.fukua);
			mMediaPlayer.setLooping(true);
			mMediaPlayer.start();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
