package com.zhengping.contact;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.zhengping.contact.db.DBHelper;
import com.zhengping.contact.entity.User;

public class MainPrivacy extends Activity {
	
	

	//��ʾ�������ݵ�ListView
	ListView lv;
	
	ArrayList list;
	
	//ӵ���������ݵ�Adapter
	SimpleAdapter adapter;
	//��Ļ�·��Ĺ�����
	GridView bottomMenuGrid;
	//���˵��Ĳ���
	GridView mainMenuGrid;
	//���˵�����ͼ
	View mainMenuView;
	
	View loginView;
	
	//װ�������linearlayout,Ĭ�������visibility=gone
	LinearLayout searchLinearout;
	LinearLayout mainLinearLayout;
	//������
	EditText et_search;
	EditText et_enter_file_name;
	
	//���˵��ĶԻ���
	AlertDialog mainMenuDialog;
	AlertDialog confirmDialog;
	AlertDialog progressDialog;
	AlertDialog enterFileNameDialog;
	AlertDialog loginDialog;
	
	boolean privacy = true;
	
	int i;
	ArrayList<Integer> deleteId;
	/** �˵����� **/
	String[] main_menu_itemName = { "��ʾ����", "ɾ������", "��������", "��ԭ����", "�˳����ֿܲ�", "����"};
	//���˵�ͼƬ
	int[] main_menu_itemSource = {
							   R.drawable.showall,
							   R.drawable.menu_delete,
							   R.drawable.menu_backup,
							   R.drawable.menu_restore,
							   R.drawable.menu_quit,
							   R.drawable.menu_return};
	
	String[] bottom_menu_itemName = { "����", "����", "ɾ��", "�˵�","�˳�" };
	String fileName;
	int[] bottom_menu_itemSource = {
								R.drawable.menu_new_user,
								R.drawable.menu_search,
								R.drawable.menu_delete, 
								R.drawable.controlbar_showtype_list,
								R.drawable.menu_exit };
	
	
    /**
     * onCreate���Ĺ������ǰ�listView��ʾ����
     * bottomMenuGrid��mainMenuGrid��searchLinearout���ǵ�Ҫ��
     * ��ʱ���ٳ�ʼ��������ֻ��ʼ��һ��
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("���ֿܲ�");
        Toast.makeText(this, "�ɹ��������ֿܲ�", Toast.LENGTH_LONG).show();
        mainLinearLayout = (LinearLayout)findViewById(R.id.list_ll);
        DBHelper helper = new DBHelper(this);//��������û���list
        helper.openDatabase(); //�����ݿ⣬�ʹ���һ�Σ���ΪHelper�е�SQLiteDatabase�Ǿ�̬�ġ�
        list = helper.getAllUser(privacy);//�õ������û���list
        
        lv = (ListView)findViewById(R.id.lv_userlist); //����ListView����
        if(list.size() == 0) {
			Drawable nodata_bg = getResources().getDrawable(R.drawable.nodata_bg);
			mainLinearLayout.setBackgroundDrawable(nodata_bg);
			setTitle("û�в鵽�κ�����");
        }
        //��������adapter��������
        adapter = new SimpleAdapter(this, 
									list, 
									R.layout.listitem, 
									new String[]{"imageid","name","mobilephone"}, 
									new int[]{R.id.user_image,R.id.tv_name,R.id.tv_mobilephone});
        
        lv.setAdapter(adapter);//�����Ϻõ�adapter����listview����ʾ���û���
        
 
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        	/**
        	 * ��Ӧ�����¼��������ĳһ��ѡ���ʱ����ת���û���ϸ��Ϣҳ��
        	 */
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap item = (HashMap)arg0.getItemAtPosition(arg2);
				int _id = Integer.parseInt(String.valueOf(item.get("_id")));
				
				Intent intent = new Intent(MainPrivacy.this,UserDetail.class);
				User user = new User();
				user._id = Integer.parseInt(String.valueOf(item.get("_id")));
				user.address = String.valueOf(item.get("address"));
				user.company = String.valueOf(item.get("company"));
				user.email = String.valueOf(item.get("email"));
				user.familyPhone = String.valueOf(item.get("familyphone"));
				user.mobilePhone = String.valueOf(item.get("mobilephone"));
				user.officePhone = String.valueOf(item.get("officephone"));
				user.otherContact = String.valueOf(item.get("othercontact"));
				user.position = String.valueOf(item.get("position"));
				user.remark = String.valueOf(item.get("remark"));
				user.username = String.valueOf(item.get("name"));
				user.zipCode = String.valueOf(item.get("zipcode"));
				user.imageId = Integer.parseInt(String.valueOf(item.get("imageid")));
				
				intent.putExtra("user", user);
				
				if(searchLinearout != null && searchLinearout.getVisibility()==View.VISIBLE) {
					searchLinearout.setVisibility(View.GONE);
				}
				
				/*��arg2��Ϊ�����봫��ȥ  ���ڱ�ʶ�޸����λ��*/
				startActivityForResult(intent, arg2);
			}
		});
       
        lv.setCacheColorHint(Color.TRANSPARENT); //����ListView�ı���Ϊ͸��
        lv.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(deleteId == null) {
					deleteId = new ArrayList<Integer>();
				}
				HashMap item = (HashMap)arg0.getItemAtPosition(arg2);
				Integer _id = Integer.parseInt(String.valueOf(item.get("_id")));
				
				RelativeLayout r = (RelativeLayout)arg1;
				ImageView iv = (ImageView)r.getChildAt(2);
				iv.setEnabled(false);
				if(iv.getVisibility() == View.VISIBLE) {
					iv.setVisibility(View.GONE);
					deleteId.remove(_id);
				} else {
					iv.setVisibility(View.VISIBLE);
					deleteId.add(_id);
				}
				return true;
			}
        	
        	
        });
        
        //Ϊlist���itemѡ����
        Drawable bgDrawableSelector = getResources().getDrawable(R.drawable.list_bg);
        lv.setSelector(bgDrawableSelector);
        
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//��resultCode==3ʱ���������һ���û����أ���resultCode==4��ʱ������޸����û�������ɾ�����û�������������������û�б仯
		if(deleteId != null) {
			deleteId.clear();
		}
		if(resultCode == 3 || resultCode == 4) {
			
			DBHelper helper = new DBHelper(this);
	        list = helper.getAllUser(privacy);
	        adapter = 
	        	new SimpleAdapter(
	        					  this, 
	        					  list, 
	        					  R.layout.listitem, 
	        					  new String[]{"imageid","name","mobilephone"}, 
	        					  new int[]{R.id.user_image,R.id.tv_name,R.id.tv_mobilephone});
		}
		
		if(list.size()>0) {
			mainLinearLayout.setBackgroundDrawable(null);
		}
       
		lv.setAdapter(adapter); //�����Ϻõ�adapter����listview����ʾ���û���
		/**
		 * resultCodeֻ��3��4��5 
		 * ������4����5��ʱ�򣬴�����UserDetailת�����ġ���ת��UserDetail��ʱ��requestCode��ֵ���õ���ѡ�����λ��
		 */
		if(resultCode == 3) {
			lv.setSelection(list.size());
		} else {
			lv.setSelection(requestCode);
		}
		
		
	}

	/**
	 * ��Ӧ���Menu��ťʱ���¼����������õײ��˵��Ƿ�ɼ�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU) {
			loadBottomMenu();
			if(bottomMenuGrid.getVisibility() == View.VISIBLE) {
				if(searchLinearout != null && searchLinearout.getVisibility() == View.VISIBLE) {
					searchLinearout.setVisibility(View.GONE);
				}
				bottomMenuGrid.setVisibility(View.GONE);
			} else {
				bottomMenuGrid.setVisibility(View.VISIBLE);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void loadBottomMenu() {
		
		if(bottomMenuGrid == null) {
			bottomMenuGrid = (GridView) findViewById(R.id.gv_buttom_menu);
			bottomMenuGrid.setBackgroundResource(R.drawable.channelgallery_bg);// ���ñ���
			bottomMenuGrid.setNumColumns(5);// ����ÿ������
			bottomMenuGrid.setGravity(Gravity.CENTER);// λ�þ���
			bottomMenuGrid.setVerticalSpacing(10);// ��ֱ���
			bottomMenuGrid.setHorizontalSpacing(10);// ˮƽ���
			bottomMenuGrid.setAdapter(getMenuAdapter(bottom_menu_itemName,
					bottom_menu_itemSource));// ���ò˵�Adapter
			/** �����ײ��˵�ѡ�� **/
			bottomMenuGrid.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					
					switch (arg2) {
						case 0: {
							if(searchLinearout != null && searchLinearout.getVisibility()==View.VISIBLE) {
								searchLinearout.setVisibility(View.GONE);
							}
							if(bottomMenuGrid.getVisibility() == View.VISIBLE) {
								bottomMenuGrid.setVisibility(View.GONE);
							}
							
							Intent intent = new Intent(MainPrivacy.this,AddNew.class);
							intent.putExtra("privacy", 1);
							startActivityForResult(intent, 3);
							break;
						}
							
						case 1:
							loadSearchLinearout();
							if(searchLinearout.getVisibility()==View.VISIBLE) {
								searchLinearout.setVisibility(View.GONE);
							} else {
								searchLinearout.setVisibility(View.VISIBLE);
								et_search.requestFocus();
								et_search.selectAll();
							}
							break;
						case 2:
							if(searchLinearout != null && searchLinearout.getVisibility()==View.VISIBLE) {
								searchLinearout.setVisibility(View.GONE);
							}
							if(deleteId == null || deleteId.size() == 0) {
								Toast.makeText(MainPrivacy.this, "    û�б���κμ�¼\n����һ����¼���ɱ��", Toast.LENGTH_LONG).show();
							} else {
								new AlertDialog.Builder(MainPrivacy.this)
								.setTitle("ȷ��Ҫɾ����ǵ�"+deleteId.size()+"����¼��?")
								.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										DBHelper helper = new DBHelper(MainPrivacy.this);
								        helper.deleteMarked(deleteId);
								        //������ͼ
								        list = helper.getAllUser(privacy);
								        adapter = 
								        	new SimpleAdapter(
								        					  MainPrivacy.this, 
								        					  list, 
								        					  R.layout.listitem, 
								        					  new String[]{"imageid","name","mobilephone"}, 
								        					  new int[]{R.id.user_image,R.id.tv_name,R.id.tv_mobilephone});
								        lv.setAdapter(adapter);
								        deleteId.clear();
									}
								})
								.setNegativeButton("ȡ��", null)
								.create()
								.show()	;
							}
							
							break;
						case 3:
							if(searchLinearout != null && searchLinearout.getVisibility()==View.VISIBLE) {
								searchLinearout.setVisibility(View.GONE);
							}
							loadMainMenuDialog();
							mainMenuDialog.show();
							
							break;
						case 4:
							finish();
							break;
					}
				}
			});
		}
			
	}
	
	private void loadMainMenuDialog() {
		if(mainMenuDialog == null) {
			LayoutInflater li = LayoutInflater.from(this);
			mainMenuView = li.inflate(R.layout.main_menu_grid, null);
			 //�������˵���ͼ���������˵��Ի���
	        mainMenuDialog = new AlertDialog.Builder(this).setView(mainMenuView).create();
	        //�������˵���ͼ���õ���ͼ�ļ��е�GridView��Ȼ�����������Adapter
	        mainMenuGrid = (GridView)mainMenuView.findViewById(R.id.gridview);
	        SimpleAdapter menuAdapter = getMenuAdapter(main_menu_itemName, main_menu_itemSource);
	        mainMenuGrid.setAdapter(menuAdapter);
	        //��Ӧ����¼�
	        mainMenuGrid.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					switch(arg2){
						case 0:{
							 DBHelper helper = new DBHelper(MainPrivacy.this);
							 list = helper.getAllUser(privacy);
							 adapter = new SimpleAdapter(
		        					  MainPrivacy.this, 
		        					  list, 
		        					  R.layout.listitem, 
		        					  new String[]{"imageid","name","mobilephone"}, 
		        					  new int[]{R.id.user_image,R.id.tv_name,R.id.tv_mobilephone});

							 lv.setAdapter(adapter);//��ʾ��������
							 mainMenuDialog.dismiss();
							break;
						}
						case 1:{
							 mainMenuDialog.dismiss();
							AlertDialog.Builder builder  = new AlertDialog.Builder(MainPrivacy.this);
							confirmDialog = builder.create();
							builder.setTitle("�Ƿ�ɾ�����У�?");
							builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										DBHelper helper = new DBHelper(MainPrivacy.this);
								        helper.deleteAll(1);
										 list = helper.getAllUser(privacy);
										 adapter = new SimpleAdapter(
					        					  MainPrivacy.this, 
					        					  list, 
					        					  R.layout.listitem, 
					        					  new String[]{"imageid","name","mobilephone"}, 
					        					  new int[]{R.id.user_image,R.id.tv_name,R.id.tv_mobilephone});

										 lv.setAdapter(adapter);//��ʾ��������
								       
									}
								});
							builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										confirmDialog.dismiss();
									}
								});
							builder.create().show();
							break;				
						}
						case 2:{
							mainMenuDialog.dismiss();
							new AlertDialog.Builder(MainPrivacy.this)
							.setTitle("�Ƿ���Ҫ���ݼ�¼��SD����")
							.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								LayoutInflater li = LayoutInflater.from(MainPrivacy.this);
								View backup_view = li.inflate(R.layout.backup_progress, null);
								progressDialog =	new AlertDialog.Builder(MainPrivacy.this)
								.setTitle("�������ڽ�����...")
								.setView(backup_view)
								.create();
								progressDialog.show();
								DBHelper helper = new DBHelper(MainPrivacy.this);
								helper.backupData(privacy);
								ProgressBar bar = (ProgressBar) backup_view.findViewById(R.id.pb_backup);
								Button btn_backup_ok = (Button)backup_view.findViewById(R.id.btn_backuup_ok);
								bar.setMax(list.size());
								for(int i=0;i<=list.size();i++) {
									bar.setProgress(i);
								}
								progressDialog.setTitle("������ɣ�һ�� "+ list.size() + " ����¼");
								btn_backup_ok.setVisibility(View.VISIBLE);
								btn_backup_ok.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										progressDialog.dismiss();
										mainMenuDialog.dismiss();
										
									}
									
								});
								}
							
							})
							.setNegativeButton("ȡ��", null)
							.create()
							.show()	;
							
							break;
						}
						case 3:{
							LayoutInflater li = LayoutInflater.from(MainPrivacy.this);
							
							View enterFileNameView = li.inflate(R.layout.enterfilename, null);
							enterFileNameDialog =	new AlertDialog.Builder(MainPrivacy.this)
							.setView(enterFileNameView).setNegativeButton("ȡ��", null)
							.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									DBHelper helper = new DBHelper(MainPrivacy.this);
									fileName = et_enter_file_name.getText().toString();
									if(helper.findFile(fileName)){
										new AlertDialog.Builder(MainPrivacy.this).setTitle("��ѡ��ʽ")
										.setPositiveButton("����", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												DBHelper helper = new DBHelper(MainPrivacy.this);
												helper.deleteAll(1);
												helper.restoreData(fileName);
												list = helper.getAllUser(privacy);
												adapter = new SimpleAdapter(MainPrivacy.this, 
														list, 
														R.layout.listitem, 
														new String[]{"imageid","name","mobilephone"}, 
														new int[]{R.id.user_image,R.id.tv_name,R.id.tv_mobilephone});
												LayoutInflater li = LayoutInflater.from(MainPrivacy.this);
												View backup_view = li.inflate(R.layout.backup_progress, null);
												progressDialog =	new AlertDialog.Builder(MainPrivacy.this)
												.setTitle("���ڻ�ԭ����...")
												.setView(backup_view)
												.create();
												progressDialog.show();
												ProgressBar bar = (ProgressBar) backup_view.findViewById(R.id.pb_backup);
											
												Button btn_backup_ok = (Button)backup_view.findViewById(R.id.btn_backuup_ok);
												bar.setMax(list.size());
												for(int i=0;i<=list.size();i++) {
													bar.setProgress(i);
												}
												progressDialog.setTitle("��ԭ��ɣ�һ����ԭ�� "+ list.size() + " ����¼");
												btn_backup_ok.setVisibility(View.VISIBLE);
												btn_backup_ok.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														progressDialog.dismiss();
														mainMenuDialog.dismiss();
														lv.setAdapter(adapter);
														
													}
													
												});
											}
										})
										.setNegativeButton("���", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												DBHelper helper = new DBHelper(MainPrivacy.this);
												int preNum = list.size();
												helper.restoreData(fileName);
												list = helper.getAllUser(privacy);
												adapter = new SimpleAdapter(MainPrivacy.this, 
														list, 
														R.layout.listitem, 
														new String[]{"imageid","name","mobilephone"}, 
														new int[]{R.id.user_image,R.id.tv_name,R.id.tv_mobilephone});
												LayoutInflater li = LayoutInflater.from(MainPrivacy.this);
												View backup_view = li.inflate(R.layout.backup_progress, null);
												progressDialog =	new AlertDialog.Builder(MainPrivacy.this)
												.setTitle("���ڻ�ԭ����...")
												.setView(backup_view)
												.create();
												progressDialog.show();
												ProgressBar bar = (ProgressBar) backup_view.findViewById(R.id.pb_backup);
											
												Button btn_backup_ok = (Button)backup_view.findViewById(R.id.btn_backuup_ok);
												bar.setMax(list.size());
												for(int i=0;i<=list.size();i++) {
													bar.setProgress(i);
												}
												progressDialog.setTitle("��ԭ��ɣ�һ����ԭ�� "+ (list.size()-preNum) + " ����¼");
												btn_backup_ok.setVisibility(View.VISIBLE);
												btn_backup_ok.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														progressDialog.dismiss();
														mainMenuDialog.dismiss();
														if(list.size() != 0) {
															mainLinearLayout.setBackgroundDrawable(null);
														}
														lv.setAdapter(adapter);
													}
													
												});
											}
										})
										.setNeutralButton("ȡ��", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												
											}
										}).create().show();
										
									} else {
										Toast.makeText(enterFileNameDialog.getContext(), "�Ҳ��������ļ�", Toast.LENGTH_LONG).show();
									}
								}
							})
							.create();
							et_enter_file_name = (EditText)enterFileNameView.findViewById(R.id.et_enter_file_name);
							et_enter_file_name.setText("priv_data");
							et_enter_file_name.requestFocus();
							et_enter_file_name.selectAll();
							enterFileNameDialog.show();
							
							
							 adapter = new SimpleAdapter(
		        					  MainPrivacy.this, 
		        					  list, 
		        					  R.layout.listitem, 
		        					  new String[]{"imageid","name","mobilephone"}, 
		        					  new int[]{R.id.user_image,R.id.tv_name,R.id.tv_mobilephone});
	
							 lv.setAdapter(adapter);//��ʾ��������
							mainMenuDialog.dismiss();
							break;
						}
						case 4:{
							finish();
							break;				
						}
						case 5:{
							mainMenuDialog.dismiss();
							break;
						}
					}
					
				}});
		}
		
	}

	private void loadSearchLinearout() {
		
		if(searchLinearout == null) {
			searchLinearout = (LinearLayout) findViewById(R.id.ll_search);
			et_search = (EditText)findViewById(R.id.et_search);
			et_search.setOnKeyListener(new OnKeyListener(){
				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					String condition = et_search.getText().toString();
					if(condition.equals("")) {
						lv.setAdapter(adapter);
					}
					DBHelper helper = new DBHelper(MainPrivacy.this);
					list = helper.getUsers(condition,privacy);
					SimpleAdapter searchAdapter = 
						new SimpleAdapter(
			        					MainPrivacy.this, 
			        					list, 
			        					R.layout.listitem, 
			        					new String[]{"imageid","name","mobilephone"}, 
			        					new int[]{R.id.user_image,R.id.tv_name,R.id.tv_mobilephone});
					lv.setAdapter(searchAdapter);  //�����Ϻõ�adapter����listview����ʾ���û���
					if(list.size() == 0) {
						Drawable nodata_bg = getResources().getDrawable(R.drawable.nodata_bg);
						mainLinearLayout.setBackgroundDrawable(nodata_bg);
						setTitle("û�в鵽�κ�����");
					} else {
						setTitle( "���鵽 " + list.size()+" ����¼");
						
						mainLinearLayout.setBackgroundDrawable(null);
					}
					return false;
				}});
	       
		}
		  
	}

	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = 
				new SimpleAdapter(
								  this, 
								  data,
								  R.layout.item_menu, 
								  new String[] { "itemImage", "itemText" },
								  new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}

	/**
	 * ���˳���ʱ�򣬻�����Դ
	 */
	@Override
	protected void onDestroy() {
		if(confirmDialog != null) {
			confirmDialog = null;
		}
		if(mainMenuDialog != null) {
			mainMenuDialog = null;
		}
		if(searchLinearout != null) {
			searchLinearout = null;
		}
		if(mainMenuView != null) {
			mainMenuView = null;
		}
		if(mainMenuGrid != null) {
			mainMenuGrid = null;
		}
		if(bottomMenuGrid != null) {
			bottomMenuGrid = null;
		}
		if(adapter != null) {
			adapter = null;
		}
		if(list != null) {
			list = null;
		}
		if(lv != null) {
			lv = null;
		}

		Toast.makeText(this, "�˳����ֿܲ�", Toast.LENGTH_LONG).show();
		System.out.println("destory!!!");
		super.onDestroy();
	}

}