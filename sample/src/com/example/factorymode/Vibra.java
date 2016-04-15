package com.example.factorymode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.ztemt.test.basic.R;

public class Vibra extends Activity{
	 Button ViSButton,ViFButton;
	 Vibrator vibrator;
	  @Override
	    public void onCreate(Bundle savedInstanceState) 
		  {
			  super.onCreate(savedInstanceState);
			  setContentView(R.layout.vibrator2);
			  ViSButton=(Button)findViewById(R.id.vibraS);
			  ViFButton=(Button)findViewById(R.id.vibraF);
	 ViSButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent BSB_intent=new Intent(Vibra.this,MainActivity.class);
				startActivity(BSB_intent);
					}
		});
		
 //失败按钮
	  ViFButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent BFB_intent=new Intent(Vibra.this,MainActivity.class);
				startActivity(BFB_intent);
					}
		});
	  vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);      
	  long[] pattern = {100,10,100,1000}; // OFF/ON/OFF/ON... 
		vibrator.vibrate(pattern, 1);
	  
	  
		  }
}

