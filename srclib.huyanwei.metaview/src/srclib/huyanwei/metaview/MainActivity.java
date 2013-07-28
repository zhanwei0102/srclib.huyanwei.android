package srclib.huyanwei.metaview;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ComponentName;
import android.database.DataSetObserver;

public class MainActivity extends Activity {
	private String TAG = "srclib.huyanwei.metaview";
	
	private ImageButton backBtn;
	private ImageButton plusBtn;
	private ImageButton minusBtn;
	private WindowManager mWindowManager;
	private LayoutInflater mLayoutInflater;
	private WindowManager.LayoutParams mLayoutParams;
	private WindowManager.LayoutParams mCacheLayoutParams;
	private View mFloatView;
	
	private ListView mListView;
		
	private Context mContext; 
	
	private ListItemAdapter mListItemAdapter;
	
	private boolean mScrolling = false;	
	private boolean mIsFloatWindow = true;
	
	private View    mDragView  ;
	private boolean mDraging    = false ;
	
	public void attachView(View view)
	{
		  mLayoutParams = new WindowManager.LayoutParams(
				  WindowManager.LayoutParams.MATCH_PARENT,
				  180,
				  WindowManager.LayoutParams.TYPE_BASE_APPLICATION,
				  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,//WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				  PixelFormat.RGBA_8888
				  );
	        /*
	        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SECURE_SYSTEM_OVERLAY;
	        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN
	                			| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
	                			| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	                			| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
	        mLayoutParams.format = PixelFormat.TRANSLUCENT;
	        
	        mLayoutParams.setTitle("PointerLocationFloatWindows");        
	        
	        mLayoutParams.inputFeatures |= WindowManager.LayoutParams.INPUT_FEATURE_NO_INPUT_CHANNEL;
	        */
	        
	        /**
	         *���¶���WindowManager.LayoutParams���������
	         * ������;�ɲο�SDK�ĵ�
	         */
	        //mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
	        //mLayoutParams.type=WindowManager.LayoutParams.TYPE_PHONE;   //����window type
	        //mLayoutParams.format=PixelFormat.RGBA_8888;   //����ͼƬ��ʽ��Ч��Ϊ����͸��

	        //����Window flag
//	        mLayoutParams.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//	                              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	        /*
	         * �����flags���Ե�Ч����ͬ����������
	         * ���������ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
	         wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL 
	                               | LayoutParams.FLAG_NOT_FOCUSABLE
	                               | LayoutParams.FLAG_NOT_TOUCHABLE;
	        */
	        
	        //mLayoutParams.gravity=Gravity.CENTER_HORIZONTAL;   //���������������ײ�����
	        
			DisplayMetrics dm = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dm);
	        int android_width = dm.widthPixels;
	        int android_height = dm.heightPixels;

	        //�����������ڳ�������
	        mLayoutParams.width=android_width;
	        mLayoutParams.height=96;

	        //����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
	        mLayoutParams.x=(android_width  - mLayoutParams.width);
	        mLayoutParams.y=(android_height - mLayoutParams.height);
	        
	        Log.d(TAG,""+mLayoutParams.toString());
	        
	        //��ʾFloatViewͼ��
	        mWindowManager.addView(view, mLayoutParams);
	}
	
	public void disattachView(View view)
	{
		mWindowManager.removeViewImmediate(view);
	}
	
	public void go_to_back()
	{
		this.finish();
	}
	
	private OnClickListener mOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
				case R.id.btn_back:			
					go_to_back();
					break;
				case R.id.btn_plus:
					//Toast.makeText(mContext, "Seting", Toast.LENGTH_SHORT).show();
					mListItemAdapter.twice();
					mListItemAdapter.notifyDataSetChanged();
					break;
				case R.id.btn_minus:
					//Toast.makeText(mContext, "Seting", Toast.LENGTH_SHORT).show();
					mListItemAdapter.half();
					mListItemAdapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
		}		
	};
	
	private class ListItemAdapter extends BaseAdapter
	{
		private Context mContext; 
		
		private int mCount ;
		
		public ListItemAdapter(Context c)
		{
			mContext = c;
			mCount = 1 ;
		}
		
		public void twice()
		{
			mCount *= 2 ;
		}
		
		public void half()
		{
			mCount /= 2 ;
			
			if(mCount < 1)
				mCount = 1 ;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.d(TAG,"getCount()");
			return mCount;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			Log.d(TAG,"getItem("+position+")");
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			Log.d(TAG,"getItemId("+position+")");
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.d(TAG,"getView("+position+")");
			
			LinearLayout ll;
			// ÿ�ζ����أ������ظ�ʹ��View
            //if (convertView == null) {
            	ll = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            //} else {
            //	ll = (LinearLayout) convertView;
            //}
            
            /*
            if(position%2 == 0)
            {
            	ll.setBackgroundResource(R.drawable.bottom_button);
            	ll.setAlpha(0.618f);            	
            }
            else
            {
            	ll.setBackgroundResource(R.drawable.bottom_button);
            }
            */
            
            int first = mListView.getFirstVisiblePosition();
            int last  = mListView.getLastVisiblePosition();
            int child_count = mListView.getChildCount(); // �ɼ���view
            int count = mListView.getCount();     		// ��view
            int header_count = ((ListView) mListView).getHeaderViewsCount();
            int footer_count = ((ListView) mListView).getFooterViewsCount();

            if(count ==1)
            {
            	// Only One Item
            	ll.setBackgroundResource(R.drawable.v5_preference_item_single_bg);
            }
            else if(position == 0)
            {
            	// first
            	ll.setBackgroundResource(R.drawable.v5_preference_item_first_bg);
            }
            else if(position == count -1)
            {
            	// last
            	ll.setBackgroundResource(R.drawable.v5_preference_item_last_bg);
            }
            else
            {
            	// moddile
            	ll.setBackgroundResource(R.drawable.v5_preference_item_middle_bg);
            }            
            
            TextView mTextView = (TextView) ll.findViewById(R.id.textView1);   
            if(mScrolling == true)            
            {
            	mTextView.setTextColor(0xffff00ff);
            	mTextView.setText("LOADING ITEM"+(position+header_count)+" ......");            	            	
            }
            else
            {
            	mTextView.setTextColor(0xff0000ff);
            	mTextView.setText("ITEM"+(position+header_count));
            }
            return ll;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			Log.d(TAG,"isEnabled("+position+")");
			if(position %2 == 0)
			{
				return true;
			}
			else
			{
				return true;
				//return false;
			}
			//return super.isEnabled(position);
		}
	};
	
	private OnScrollListener mOnScrollListener = new OnScrollListener()
	{

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
	        switch (scrollState) {
	        	case OnScrollListener.SCROLL_STATE_IDLE:
	        		// ������ֹͣʱ��
 	        		mScrolling = false;
	                int first = view.getFirstVisiblePosition();
	                int last  = view.getLastVisiblePosition();
	                int child_count = view.getChildCount(); // �ɼ���view
	                int count = view.getCount();     		// ��view
	                int header_count = ((ListView) view).getHeaderViewsCount();
	                int footer_count = ((ListView) view).getFooterViewsCount();
	                Log.d(TAG,"["+first+"-"+last+"]/["+child_count+"-"+count+"]["+header_count+"/"+footer_count+"]");

	                for (int i=0; i<child_count; i++)
	                {	
	                	if(((i+first) >= (0+header_count)) && ( (i+first) <= (count -1 - footer_count) ))
	                	{	
		                	LinearLayout ll = (LinearLayout)view.getChildAt(i);
		                    TextView mTextView = (TextView) ll.findViewById(R.id.textView1);                        
		                    
		                    mTextView.setTextColor(0xffff0000);
		                    mTextView.setText("UPDATA ITEM("+(first)+"+"+i+")");
	                	}
	                }
	        		Log.d(TAG,"SCROLL_STATE_IDLE");
	        		break;
	        	case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
	        		mScrolling = true;
	        		Log.d(TAG,"SCROLL_STATE_TOUCH_SCROLL");
	        		break;
	        	case OnScrollListener.SCROLL_STATE_FLING:
	        		mScrolling = true;
	        		Log.d(TAG,"SCROLL_STATE_FLING");
	        		break;
	        }
		}
	};
	
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onItemClick("+arg0+","+arg1+","+arg2+","+arg3+")");
                int first = arg0.getFirstVisiblePosition();
                int last  = arg0.getLastVisiblePosition();
                int child_count = arg0.getChildCount();
                int count = arg0.getCount();
                int header_count = ((ListView) arg0).getHeaderViewsCount();
                int footer_count = ((ListView) arg0).getFooterViewsCount();
                Log.d(TAG,"["+first+"-"+last+"/"+count+"]["+header_count+"/"+footer_count+"]");
                if((arg2 > (header_count-1)) && (arg2 < (count-footer_count))) // index calc
				{
                	TextView mTextView = (TextView) arg1.findViewById(R.id.textView1);
                	mTextView.setTextColor(0xff000000);
                	mTextView.setText("clicked Item"+(arg2));
                	Toast.makeText(mContext, "Item"+(arg2)+" is clicked!", Toast.LENGTH_SHORT).show();
				}
                else
				{
					Toast.makeText(mContext, "mFloatView("+arg2+") is clicked!", Toast.LENGTH_SHORT).show();
				}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this ;
		
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.activity_main);
		
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.customer_title);
		
		mWindowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
		mLayoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
        backBtn = (ImageButton)findViewById(R.id.btn_back);

        backBtn.setOnClickListener(mOnClickListener);
        
        mListView = (ListView) this.findViewById(R.id.listView1);
        
        mListView.setOnScrollListener(mOnScrollListener);
        
        mListView.setOnItemClickListener(mOnItemClickListener);
		
        mFloatView = mLayoutInflater.inflate(R.layout.floater, null);
        
        plusBtn = (ImageButton) mFloatView.findViewById(R.id.btn_plus);
        
        plusBtn.setOnClickListener(mOnClickListener);
        
        minusBtn = (ImageButton) mFloatView.findViewById(R.id.btn_minus);
        
        minusBtn.setOnClickListener(mOnClickListener);
        
        if(mIsFloatWindow)
        {
        	attachView(mFloatView);
        }
        else
        {
        	mListView.addHeaderView(mFloatView);  // must appear before setAdapter().
        	//mListView.setListFooter(mFloatView);
        	//mListView.addFooterView(mFloatView);  // must appear before setAdapter().
        }
        
        mListItemAdapter = new ListItemAdapter(this);
        
        mListView.setAdapter(mListItemAdapter);        

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mIsFloatWindow)
		{
			disattachView(mFloatView);
		}
		super.onDestroy();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub		
		super.onPause();
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub		
		super.onResume();
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// getRawX()��getRawY()����õ��������Ļ��λ��
		// getX()��getY()����õ���Զ�����view�Ĵ���λ�� ���꣨������ֵ���ᳬ��view�ĳ��ȺͿ���)
		// getLeft , getTop, getBottom,getRight, ���ָ���Ǹÿؼ�����ڸ��ؼ��ľ���.
		int x,y ;
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				x = (int) event.getX();// ��ȡ�����ListView��x���� 
				y = (int) event.getY();// ��ȡ��Ӧ��ListView��y����
				Log.d(TAG,"onTouchEvent down ("+x+","+y+");");
				startDrag(x,y);
				break;
			case MotionEvent.ACTION_MOVE: 
				x = (int) event.getX();// ��ȡ�����ListView��x���� 
				y = (int) event.getY();// ��ȡ��Ӧ��ListView��y����
				Log.d(TAG,"onTouchEvent move ("+x+","+y+");");
				onDrag(y);				
				break;
			case MotionEvent.ACTION_UP:
				x = (int) event.getX();// ��ȡ�����ListView��x���� 
				y = (int) event.getY();// ��ȡ��Ӧ��ListView��y����
				Log.d(TAG,"onTouchEvent up ("+x+","+y+");");
				stopDrag();
				break; 
			default: 
				break;
		}
		return false ;
		
		//return super.onTouchEvent(event);
	}
		

		private void startDrag(int x , int y)
		{
			/*** 
			* ��ʼ��window. 
			*/ 
			mCacheLayoutParams = new WindowManager.LayoutParams(); 
			mCacheLayoutParams.gravity = Gravity.TOP; 
			mCacheLayoutParams.x = 0; 
			mCacheLayoutParams.y = y; 
			mCacheLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT; 
			mCacheLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; 

			mCacheLayoutParams.flags =  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE		// �����ȡ���� 
								| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE		// ������ܴ����¼� 
								| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON	// �����豸���������������Ȳ��䡣 
								| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;	// ����ռ��������Ļ��������Χ��װ�α߿�����״̬�������˴����迼�ǵ�װ�α߿�����ݡ� 

			// windowParams.format = PixelFormat.TRANSLUCENT;// Ĭ��Ϊ��͸�����������͸��Ч��. 
			mCacheLayoutParams.windowAnimations = 0;// ������ʹ�õĶ������� 

			//��ʼ��Ӱ��
			
			Log.d(TAG,"mListView.getFirstVisiblePosition()="+mListView.getFirstVisiblePosition());
			
			View ListItemView = mListView.getChildAt(0);
			ListItemView.setDrawingCacheEnabled(true);// ����cache. 
			Bitmap bm = Bitmap.createBitmap(ListItemView.getDrawingCache());// ����cache����һ���µ�bitmap����.
			
			ImageView imageView = new ImageView(this); 
			imageView.setImageBitmap(bm);
			mWindowManager.addView(imageView, mCacheLayoutParams);
			
			mDragView = imageView;
			
			if(mDragView != null)
			{
				mDraging = true ;
			}
		}
		
		/** 
		* �϶�ִ�У���Move������ִ�� 
		* 
		* @param y 
		*/
		public void onDrag(int y)
		{ 
			if (mDraging)
			{ 
				mCacheLayoutParams.alpha = 0.5f; 
				mCacheLayoutParams.y = y; 
				mWindowManager.updateViewLayout(mDragView, mCacheLayoutParams);// ʱʱ�ƶ�.
				
				//onChange(y);// ʱʱ����
				//doScroller(y);// listview�ƶ�.
			} 
		} 
		
		/** 
		* ֹͣ�϶���ɾ��Ӱ�� 
		*/ 
		public void stopDrag()
		{ 
			if (mDraging) 
			{ 
				mWindowManager.removeView(mDragView); 
				mDragView = null; 				
				mDraging = false ;
			}
		}
		
		/** 
		* �϶����µ�ʱ�� 
		* 
		* @param y 
		*/ 
		public void onDrop(int y) { 
			((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();// ˢ��.
		} 
}