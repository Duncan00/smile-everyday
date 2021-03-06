package com.smile.smileeveryday.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.smile.smileeveryday.R;

public class ServiceFloating extends Service {

	public static  int ID_NOTIFICATION = 2018;

	private WindowManager windowManager;
	private ImageView chatHead;
	private PopupWindow pwindo;

	boolean mHasDoubleClicked = false;
	long lastPressTime;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override 
	public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		chatHead = new ImageView(this);
		
		chatHead.setImageResource(R.drawable.floating2);
		
//		if(prefs.getString("ICON", "floating2").equals("floating3")){
//			chatHead.setImageResource(R.drawable.floating3);
//		} else if(prefs.getString("ICON", "floating2").equals("floating4")){
//			chatHead.setImageResource(R.drawable.floating4);
//		} else if(prefs.getString("ICON", "floating2").equals("floating5")){
//			chatHead.setImageResource(R.drawable.floating5);
//		} else if(prefs.getString("ICON", "floating2").equals("floating5")){
//			chatHead.setImageResource(R.drawable.floating2);
//		}

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;

		windowManager.addView(chatHead, params);

		try {
			chatHead.setOnTouchListener(new View.OnTouchListener() {
				private WindowManager.LayoutParams paramsF = params;
				private int initialX;
				private int initialY;
				private float initialTouchX;
				private float initialTouchY;

				@Override public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// Get current time in nano seconds.
						long pressTime = System.currentTimeMillis();


						// If double click...
						if (pressTime - lastPressTime <= 300) {
							createNotification();
							ServiceFloating.this.stopSelf();
							mHasDoubleClicked = true;
						}
						else {     // If not double click....
							mHasDoubleClicked = false;
						}
						lastPressTime = pressTime; 
						initialX = paramsF.x;
						initialY = paramsF.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						break;
					case MotionEvent.ACTION_MOVE:
						paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
						paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
						windowManager.updateViewLayout(chatHead, paramsF);
						break;
					}
					return false;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

		chatHead.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				initiatePopupWindow(chatHead);
//				_enable = false;
				//				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				//				getApplicationContext().startActivity(intent);
			}
		});

	}


	private void initiatePopupWindow(View anchor) {
		try {
//			Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//			ListPopupWindow popup = new ListPopupWindow(this);
//			popup.setAnchorView(anchor);
//			popup.setWidth((int) (display.getWidth()/(1.5)));
//			//ArrayAdapter<String> arrayAdapter =
//			//new ArrayAdapter<String>(this,R.layout.list_item, myArray);
//			popup.setAdapter(new CustomAdapter(getApplicationContext(), R.layout.row, listCity));
//			popup.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View view, int position, long id3) {
//					//Log.w("tag", "package : "+apps.get(position).pname.toString());
//					Intent i;
//					PackageManager manager = getPackageManager();
//					try {
//						i = manager.getLaunchIntentForPackage(apps.get(position).pname.toString());
//						if (i == null)
//							throw new PackageManager.NameNotFoundException();
//						i.addCategory(Intent.CATEGORY_LAUNCHER);
//						startActivity(i);
//					} catch (PackageManager.NameNotFoundException e) {
//
//					}
//				}
//			});
//			popup.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createNotification(){
		Intent notificationIntent = new Intent(getApplicationContext(), ServiceFloating.class);
		PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, 0);

		Notification notification = new Notification(R.drawable.ic_launcher, "Click to start launcher",System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), "Start launcher" ,  "Click to start launcher", pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;

		NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(ID_NOTIFICATION,notification);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHead != null) windowManager.removeView(chatHead);
	}

}