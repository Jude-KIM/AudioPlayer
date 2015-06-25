package com.airplug.audioplug.share;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airplug.audioplug.data.ConstData.Extra;
import com.airplug.audioplug.dev.R;

public class TwitterActivity extends Activity{
	
	private static final String NAME = "TwitterActivity";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());

	private EditText messageView;
	private String link;	
	private String contentTitle;
	private String message;
	private Context context;	
	
	private ImageButton postButton;
	private ImageButton cancleButton;
	private TextView textByte;
	CharSequence body;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.overridePendingTransition(R.anim.oncreate_slide_in, R.anim.oncreate_slide_out);
		
		setContentView(R.layout.activity_twitter);
		context = this;
		
		postButton = (ImageButton) findViewById(R.id.btnTwitterDone);
		cancleButton = (ImageButton) findViewById(R.id.btnTwitterCancel);
		messageView = (EditText) findViewById(R.id.twitterMessage);
		textByte = (TextView) findViewById(R.id.textByte);
		
		messageView.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int len = messageView.length();
				body = messageView.getText();
				
				if(len == 0)
					postButton.setEnabled(false);
				else
					postButton.setEnabled(true);
				
				if(len > 140){
					((Editable) body).delete(body.length()-2, body.length()-1);
				}
				
				textByte.setText(messageView.length()+"/140");
				 
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});		
						
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			message = bundle == null ? "" : bundle.getString(Extra.POST_MESSAGE);			
			contentTitle = bundle.getString(Extra.POST_CONTENT_TITLE);
			link = bundle.getString(Extra.POST_LINK);
			//linkDescription = bundle.getString(Extra.POST_LINK_DESCRIPTION);
			messageView.setText(message);
			body = messageView.getText();
		}
		
						
		postButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
				if(TwitterBase.isLogined(context) == true){
					StrictMode.enableDefaults();
					TwitterBase.getInst(context).write(body.toString());
				}				

				finish();
			}						
		});
		
		
		cancleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}	
	
	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.onfinish_slide_in, R.anim.onfinish_slide_out);

	}
	
	
}
