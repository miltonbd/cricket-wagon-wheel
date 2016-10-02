package com.opengleswagonwheel;

import newfeatures.FieldPositionEditDialog;
import newfeatures.FourSixUpdate;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.opengleswagonwheel.R;
import com.utils.MyLogger;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity {

	private MyGLSurfaceView mGLView;
	private TextView fourTV;
	private TextView sixTV;
	private MainRenderer mRenderer;
	private MyGLSurfaceView my;
	private LinearLayout layo;
	private RelativeLayout relativeLayout;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		 ActionBar actionBar = getSupportActionBar();
		//  actionBar.hide();
		 // as the ContentView for this Activity.
		MyApp.activity=this;
		EventBus.getDefault().register(this);

        setContentView(R.layout.fragment_main);
        fourTV = (TextView) MyApp.activity.findViewById(R.id.fours);
        sixTV = (TextView) MyApp.activity.findViewById(R.id.six);
        layo = (LinearLayout) findViewById(R.id.myGLSurfaceView1);
        relativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        mRenderer=new MainRenderer(getApplicationContext(),relativeLayout);
        mGLView=new  MyGLSurfaceView(getApplicationContext(),mRenderer);
        layo.addView(mGLView);
       // mRenderer.showRunText();
  
	}
	
	public void onEvent(final OnNewSurfaceSizeSet event) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,(int)event.getNewHeight());
				mGLView.setLayoutParams(params);				
			}
		});
	}
	
	
	public void onEvent(final OnAddRunSegmentText event) {
		
	        runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					   TextView tv= new TextView(getApplicationContext() );
				        tv.setText(event.getText()); 
				        tv.setTextSize(30);
				        LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				        float x = event.getX();
				        float y= event.getY();
				        float width =  (event.getCamera().getWidth()*1.0f);
				        float height =  (event.getCamera().getHeight()*1.0f);
						float leftMargin=(width/2)+width*x/200;
						float topMargin=(height/2)-height*y/200;
				        
				        MyLogger.log("Run Text X="+x+", Y="+y+" , left Margin="+leftMargin+", top Margin="+topMargin);
						params.leftMargin = (int) leftMargin;
				        params.topMargin = (int) topMargin;
				        tv.setLayoutParams(params);
				        relativeLayout.addView(tv);
				        relativeLayout.invalidate();
				}
			});
	      
	}
	

	
	public void onEvent(FourSixUpdate event) {
		fourTV.setText(event.getFourCount()+"");
		sixTV.setText(event.getSixCount()+"");
	}
	
	  @Override
	    protected void onResume() {
	        super.onResume();
	        /*
	         * The activity must call the GL surface view's
	         * onResume() on activity onResume().
	         */
	        if (mGLView != null) {
	        	mGLView.onResume();
	        }
	    }
	 
	    @Override
	    protected void onPause() {
	        super.onPause();
	 
	        /*
	         * The activity must call the GL surface view's
	         * onPause() on activity onPause().
	         */
	        if (mGLView != null) {
	        	mGLView.onPause();
	        }
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_field_positions) {
			// Show Field Edit Position Dialog
			FieldPositionEditDialog dialog = new FieldPositionEditDialog();
			dialog.show(getSupportFragmentManager(), "edit_field_position_dialog");
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
