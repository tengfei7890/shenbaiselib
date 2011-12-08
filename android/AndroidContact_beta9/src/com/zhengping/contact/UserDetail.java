package com.zhengping.contact;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;

import com.zhengping.contact.AddNew.ImageAdapter;
import com.zhengping.contact.db.DBHelper;
import com.zhengping.contact.entity.User;

public class UserDetail extends Activity implements ViewFactory {

	EditText et_name;
	EditText et_mobilePhone;
	EditText et_officePhone;
	EditText et_familyPhone;
	EditText et_position;
	EditText et_company;
	EditText et_address;
	EditText et_zipCode;
	EditText et_otherContact;
	EditText et_email;
	EditText et_remark;
	
	Button btn_save;
	Button btn_return;
	Button btn_delete;
	//ͷ��İ�ť
	ImageButton imageButton;
	//��flag���жϰ�ť��״̬   false��ʾ�鿴����޸�״̬  true��ʾ����޸ı���״̬
	boolean flag = false;
	boolean imageChanged = false;
	boolean isDataChanged = false;
	
	int currentImagePosition;
	int previousImagePosition;
	
	String[] callData;
	//��ʾ״̬����绰�������ţ����ʼ�
	String status;
	//ӵ��һ��userʵ�������������Intent������
	User user;
	Gallery gallery;
	ImageSwitcher is;
	
	View numChooseView;
	View imageChooseView;
	
	//����ѡ��ĶԻ���
	AlertDialog numChooseDialog;
	AlertDialog imageChooseDialog;
	/**
	 * ���е�ͼ��ͼƬ
	 */
	private  int[] images 
			= new int[]{R.drawable.icon
		,R.drawable.image1,R.drawable.image2,R.drawable.image3
		,R.drawable.image4,R.drawable.image5,R.drawable.image6
		,R.drawable.image7,R.drawable.image8,R.drawable.image9
		,R.drawable.image10,R.drawable.image11,R.drawable.image12
		,R.drawable.image13,R.drawable.image14,R.drawable.image15
		,R.drawable.image16,R.drawable.image17,R.drawable.image18
		,R.drawable.image19,R.drawable.image20,R.drawable.image21
		,R.drawable.image22,R.drawable.image23,R.drawable.image24
		,R.drawable.image25,R.drawable.image26,R.drawable.image27
		,R.drawable.image28,R.drawable.image29,R.drawable.image30};
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userdetail);

		//�����ͼ
		Intent intent = getIntent();
		//����ͼ�еõ���Ҫ��user����
		user = (User) intent.getSerializableExtra("user");
		// ��������,���ؼ��ϸ�ֵ
		loadUserData();
		// ����EditText���ɱ༭
		setEditTextDisable();
		
		//Ϊ��ť��Ӽ�����
		btn_save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(!flag) {
					btn_save.setText("�����޸�");
					setEditTextAble();
					flag = true;
				} else {
					//�����ݿ������������
					setTitle("modify");
					modify();
					setEditTextDisable();
					setColorToWhite();
					btn_save.setText("�޸�");
					flag = false;
				}
				
			}});
		
		
		btn_return.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(isDataChanged) {
					setResult(4);
				}  else {
					setResult(5);
				}
				finish();
			}});
		
		
		btn_delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(UserDetail.this).
				setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						delete();
						setResult(4);
						finish();
					}
				}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).setTitle("�Ƿ�Ҫɾ��?").create().show();
				
			}});
		
		
		imageButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				loadImage();//����imageChooseView��ֻ����һ��
				initImageChooseDialog();//����imageChooseDialog��ֻ����һ��
				imageChooseDialog.show();
				
				
			}});
		
		

	}

	/**
	 * ��ò����ļ��еĿؼ������Ҹ��ݴ��ݹ���user����Կؼ����и�ֵ
	 */
	public void loadUserData() {
		// ���EditText�ؼ�
		et_name = (EditText) findViewById(R.id.username);
		et_mobilePhone = (EditText) findViewById(R.id.mobilephone);
		et_officePhone = (EditText) findViewById(R.id.officephone);
		et_familyPhone = (EditText) findViewById(R.id.familyphone);
		et_position = (EditText) findViewById(R.id.position);
		et_company = (EditText) findViewById(R.id.company);
		et_address = (EditText) findViewById(R.id.address);
		et_zipCode = (EditText) findViewById(R.id.zipcode);
		et_otherContact = (EditText) findViewById(R.id.othercontact);
		et_email = (EditText) findViewById(R.id.email);
		et_remark = (EditText) findViewById(R.id.remark);
		
		// ���Button�ؼ�
		btn_save = (Button)findViewById(R.id.save);
		btn_return = (Button)findViewById(R.id.btn_return);
		btn_delete = (Button)findViewById(R.id.delete);
		imageButton = (ImageButton)findViewById(R.id.image_button);
		
		// Ϊ�ؼ���ֵ
		et_name.setText(user.username);
		et_mobilePhone.setText(user.mobilePhone);
		et_familyPhone.setText(user.familyPhone);
		et_officePhone.setText(user.officePhone);
		et_company.setText(user.company);
		et_address.setText(user.address);
		et_zipCode.setText(user.zipCode);
		et_otherContact.setText(user.otherContact);
		et_email.setText(user.email);
		et_remark.setText(user.remark);
		et_position.setText(user.position);
		imageButton.setImageResource(user.imageId);
	}

	/**
	 * ����EditTextΪ������
	 */
	private void setEditTextDisable() {
		et_name.setEnabled(false);
		et_mobilePhone.setEnabled(false);
		et_officePhone.setEnabled(false);
		et_familyPhone.setEnabled(false);
		et_position.setEnabled(false);
		et_company.setEnabled(false);
		et_address.setEnabled(false);
		et_zipCode.setEnabled(false);
		et_otherContact.setEnabled(false);
		et_email.setEnabled(false);
		et_remark.setEnabled(false);
		imageButton.setEnabled(false);
		setColorToWhite();

	}

	/**
	 * ����EditTextΪ����״̬
	 */
	private void setEditTextAble() {
		et_name.setEnabled(true);
		et_mobilePhone.setEnabled(true);
		et_officePhone.setEnabled(true);
		et_familyPhone.setEnabled(true);
		et_position.setEnabled(true);
		et_company.setEnabled(true);
		et_address.setEnabled(true);
		et_zipCode.setEnabled(true);
		et_otherContact.setEnabled(true);
		et_email.setEnabled(true);
		et_remark.setEnabled(true);
		imageButton.setEnabled(true);
		setColorToBlack();
	}
	
	/**
	 *  ������ʾ��������ɫΪ��ɫ
	 */
	private void setColorToBlack() {
		
		et_name.setTextColor(Color.BLACK);
		et_mobilePhone.setTextColor(Color.BLACK);
		et_officePhone.setTextColor(Color.BLACK);
		et_familyPhone.setTextColor(Color.BLACK);
		et_position.setTextColor(Color.BLACK);
		et_company.setTextColor(Color.BLACK);
		et_address.setTextColor(Color.BLACK);
		et_zipCode.setTextColor(Color.BLACK);
		et_otherContact.setTextColor(Color.BLACK);
		et_email.setTextColor(Color.BLACK);
		et_remark.setTextColor(Color.BLACK);
	}
	
	/**
	 *  ������ʾ��������ɫΪ��ɫ
	 */
	private void setColorToWhite() {
		et_name.setTextColor(Color.WHITE);
		et_mobilePhone.setTextColor(Color.WHITE);
		et_officePhone.setTextColor(Color.WHITE);
		et_familyPhone.setTextColor(Color.WHITE);
		et_position.setTextColor(Color.WHITE);
		et_company.setTextColor(Color.WHITE);
		et_address.setTextColor(Color.WHITE);
		et_zipCode.setTextColor(Color.WHITE);
		et_otherContact.setTextColor(Color.WHITE);
		et_email.setTextColor(Color.WHITE);
		et_remark.setTextColor(Color.WHITE);
	}

	/**
	 * ����������ݣ�����DBHelper���󣬸������ݿ�
	 */
	private void modify() {
		user.username = et_name.getText().toString();
		user.address = et_address.getText().toString();
		user.company = et_company.getText().toString();
		user.email = et_email.getText().toString();
		user.familyPhone = et_familyPhone.getText().toString();
		user.mobilePhone = et_mobilePhone.getText().toString();
		user.officePhone = et_officePhone.getText().toString();
		user.otherContact = et_otherContact.getText().toString();
		user.position = et_position.getText().toString();
		user.remark = et_remark.getText().toString();
		user.zipCode = et_zipCode.getText().toString();
		if(imageChanged) {
			user.imageId = images[currentImagePosition%images.length];
		}
		
		DBHelper helper = new DBHelper(this);
		//�����ݿ�
		helper.openDatabase();
		helper.modify(user);
		isDataChanged = true;
	}
	
	private void delete() {
		DBHelper helper = new DBHelper(this);
		//�����ݿ�
		helper.openDatabase();
		helper.delete(user._id);
	}
	
	/**
	 * ΪMenu��Ӽ���ѡ��
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.addSubMenu(0, Menu.FIRST, 1, "��绰");
		menu.addSubMenu(0, Menu.FIRST+1, 2, "������");
		menu.addSubMenu(0, Menu.FIRST+2, 3, "���ʼ�");
		
		//Ϊÿһ��Item����ͼ��
		MenuItem item = menu.getItem(Menu.FIRST-1);
		item.setIcon(R.drawable.dial);
		MenuItem item1 = menu.getItem(Menu.FIRST);
		item1.setIcon(R.drawable.send_sms);
		MenuItem item2 = menu.getItem(Menu.FIRST+1);
		item2.setIcon(R.drawable.mail);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Ϊÿһ��MenuItem����¼�
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch(item.getItemId()){
			case Menu.FIRST: {
				//��״̬����Ϊ��绰
				status = Intent.ACTION_CALL;
				if(callData == null) {
					//���ؿ��õĺ���
					loadAvailableCallData();
				}
				
				if(callData.length == 0) {
					//��ʾû�п��õĺ���
					Toast.makeText(this, "û�п��õĺ��룡", Toast.LENGTH_LONG).show();
				} else if(callData.length == 1) {
					//���֮��һ�����õĺ��룬��ֱ��ʹ��������벦��
					Intent intent = 
						new Intent(Intent.ACTION_CALL,Uri.parse("tel://" + callData[0]));
					startActivity(intent);
				} else {
					//�����2������2�����Ϻ��룬��������ѡ��Ի���
					initNumChooseDialog();
				}
				break;
			}
			case Menu.FIRST+1: {
				status = Intent.ACTION_SENDTO;
				if(callData == null) {
					loadAvailableCallData();
				}
				if(callData.length == 0) {
					//��ʾû�п��õĺ���
					Toast.makeText(this, "û�п��õĺ��룡", Toast.LENGTH_LONG).show();
				} else if(callData.length == 1) {
					//���֮����һ�����õĺ��룬��ֱ��ʹ��������벦��
					Intent intent = 
						new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto://" + callData[0]));
					startActivity(intent);
				} else {
					initNumChooseDialog();
				}
				break;
			}
			case Menu.FIRST+2: {
				
				if(user.email.equals("")) {
					Toast.makeText(this, "û�п��õ����䣡", Toast.LENGTH_LONG).show();
				} else {
					Uri emailUri = Uri.parse("mailto:" + user.email);
					Intent intent = new Intent(Intent.ACTION_SENDTO, emailUri);
					startActivity(intent);
				}
				break;
			}
		
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	/**
	 * װ��ͷ��
	 */
	public void loadImage() {
		if(imageChooseView == null) {
			LayoutInflater li = LayoutInflater.from(UserDetail.this);
			imageChooseView = li.inflate(R.layout.imageswitch, null);
			gallery = (Gallery)imageChooseView.findViewById(R.id.gallery);
			gallery.setAdapter(new ImageAdapter(this));
			gallery.setSelection(images.length/2);
			is = (ImageSwitcher)imageChooseView.findViewById(R.id.imageswitch);
			is.setFactory(this);
			gallery.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					currentImagePosition = arg2 % images.length;
					is.setImageResource(images[arg2 % images.length]);
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}});
		}
		
	}
	
	public void initNumChooseDialog() {
		if(numChooseDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = LayoutInflater.from(this);
			numChooseView = inflater.inflate(R.layout.numchoose, null);
			ListView lv = (ListView)numChooseView.findViewById(R.id.num_list);
		    ArrayAdapter array = 
		        	new ArrayAdapter(this,android.R.layout.simple_list_item_1,callData);
		    lv.setAdapter(array);
		    lv.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					String num = String.valueOf(arg0.getItemAtPosition(arg2));
					Intent intent = null;
					if(status.equals(Intent.ACTION_CALL)) {
						intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel://" + num));
					} else {
						intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto://" + num));
					}
					
					startActivity(intent);
					//�Ի�����ʧ
					numChooseDialog.dismiss();
				}});
		    
		    
			builder.setView(numChooseView);
			numChooseDialog = builder.create();
			
		}
		numChooseDialog.show();
	}
	
	public void initImageChooseDialog() {
		if(imageChooseDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("��ѡ��ͼ��")
			.setView(imageChooseView).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					imageChanged = true;
					previousImagePosition = currentImagePosition;
					imageButton.setImageResource(images[currentImagePosition%images.length]);
				}
			})
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					currentImagePosition = previousImagePosition;
					
				}
			});
			imageChooseDialog = builder.create();
		}
	}
	/**
	 * װ�ؿ��õĺ���
	 */
	public void loadAvailableCallData() {
		ArrayList<String> callNums = new ArrayList<String>();
		if(!user.mobilePhone.equals("")) {
			callNums.add(user.mobilePhone);
		}
		if(!user.familyPhone.equals("")) {
			callNums.add(user.familyPhone);
		}
		
		if(!user.officePhone.equals("")) {
			callNums.add(user.officePhone);
		}
		
		
		callData = new String[callNums.size()];
		
		for(int i=0;i<callNums.size();i++) {
			callData[i] = callNums.get(i);
		}
		
		
	}
	
	
	/**
	 * �Զ���ͷ��������
	 * @author Administrator
	 *
	 */
	class ImageAdapter extends BaseAdapter {

		private Context context;
		
		public ImageAdapter(Context context) {
			this.context = context;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		/**
		 * gallery������������õ�image
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView iv = new ImageView(context);
			iv.setImageResource(images[position%images.length]);
			iv.setAdjustViewBounds(true);
			iv.setLayoutParams(new Gallery.LayoutParams(80,80));
			iv.setPadding(15, 10, 15, 10);
			return iv;
		}
		
	}

	@Override
	public View makeView() {
		ImageView view = new ImageView(this);
		view.setBackgroundColor(0xff000000);
		view.setScaleType(ScaleType.FIT_CENTER);
		view.setLayoutParams(new ImageSwitcher.LayoutParams(90,90));
		return view;
	}
	/**
	 * ���˳���ʱ�򣬻�����Դ
	 */
	@Override
	protected void onDestroy() {
		if(is != null) {
			is = null;
		}
		if(gallery != null) {
			gallery = null;
		}
		if(imageChooseDialog != null) {
			imageChooseDialog = null;
		}
		if(imageChooseView != null) {
			imageChooseView = null;
		}
		if(imageButton != null) {
			imageButton = null;
		}
		if(numChooseDialog != null) {
			numChooseDialog = null;
		}
		if(numChooseView != null) {
			numChooseView = null;
		}
		
		super.onDestroy();
	}
}