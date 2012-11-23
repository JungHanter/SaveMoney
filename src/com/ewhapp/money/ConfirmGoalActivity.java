package com.ewhapp.money;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ewhapp.money.UserData.SaveObjectInfo;

public class ConfirmGoalActivity extends SaveMoneyBaseActivity {
	final static private int DLG_RESETGOAL = 0;

	private ImageView btn_edit;
	private ImageView btn_delete;
	private ImageView btn_cancel;
	private ImageView btn_share;
	private ImageView img_goal;
	private TextView txt_title;
	private EditText edt_title;
	private TextView txt_goalMoney;
	private EditText edt_goalMoney;
	private TextView txt_nowMoney;
	private TextView txt_rate;
	private ListView listView;
	private boolean bEditMode;
	private RelativeLayout confirmBottom;
	private UserData userData;
	private ArrayList<SaveObjectInfo> usingList;
	public static Activity mActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmgoal);
		//액티비티를 종료시키지 않고 홈으로 나왔을때 위젯을 누르면
		//액티비티가 같이 뜸. 그래서 액티비티 강종하기 위한 과정
		mActivity = ConfirmGoalActivity.this;
		
		bEditMode = false;
		userData = UserData.sharedUserData(this);
		btn_edit = (ImageView) findViewById(R.id.confirmgoal_btn_edit);
		btn_delete = (ImageView) findViewById(R.id.confirmgoal_btn_delete);
		btn_cancel = (ImageView) findViewById(R.id.confirmgoal_btn_cancel);
		btn_share = (ImageView) findViewById(R.id.confirmgoal_btn_share);
		img_goal = (ImageView) findViewById(R.id.confirmgoal_img_goal);
		edt_title = (EditText) findViewById(R.id.confirmgoal_edt_title);
		txt_title = (TextView) findViewById(R.id.confirmgoal_txt_title);
		txt_goalMoney = (TextView) findViewById(R.id.confirmgoal_txt_goalMoney);
		edt_goalMoney = (EditText) findViewById(R.id.confirmgoal_edt_goalMoney);
		txt_nowMoney = (TextView) findViewById(R.id.confirmgoal_txt_nowMoney);
		txt_rate = (TextView) findViewById(R.id.confirmgoal_txt_rate);
		listView = (ListView) findViewById(R.id.confirmgoal_list_saveobj);
		confirmBottom = (RelativeLayout)findViewById(R.id.confirmBottom);
		txt_title.setText(userData.getGoalName());
		txt_goalMoney.setText("￦ "
				+ SaveMoneyUtils.convertMoneyComma(userData.getGoalMoney()));
		refreshAchievement();

		int deviceHeight = getWindowManager().getDefaultDisplay().getHeight();
		DisplayMetrics outMatrix = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMatrix);
		if (deviceHeight > 1024)
			img_goal.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					MyUtils.dpToPixel(200, outMatrix.densityDpi)));
		try {
			Bitmap goalImg = BitmapFactory.decodeFile(getFilesDir()
					.getAbsolutePath() + "/" + SaveMoneyUtils.GOAL_IMG_NAME);
			img_goal.setImageBitmap(goalImg);
		} catch (Exception e) {
			;
		}

		btn_edit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bEditMode = true;

				txt_title.setVisibility(View.GONE);
				edt_title.setVisibility(View.VISIBLE);
				edt_title.setText(userData.getGoalName());
				txt_goalMoney.setVisibility(View.GONE);
				edt_goalMoney.setVisibility(View.VISIBLE);
				edt_goalMoney.setText("" + userData.getGoalMoney());
				img_goal.setClickable(true);

				btn_edit.setVisibility(View.INVISIBLE);
				btn_delete.setVisibility(View.VISIBLE);
				btn_cancel.setVisibility(View.VISIBLE);
				btn_share.setVisibility(View.INVISIBLE);
				listView.invalidateViews();
			}
		});

		btn_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				endEdit();
			}
		});

		btn_delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(ConfirmGoalActivity.this)
						.setMessage("벌써 또 다른 지름을?")
						.setPositiveButton("목표 변경",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// 데이터 초기화 하고 저장하고
										userData.resetUserData(ConfirmGoalActivity.this);
										userData.saveData(ConfirmGoalActivity.this);

										dialog.dismiss();
										startActivity(new Intent(
												ConfirmGoalActivity.this,
												SetGoalActivity.class));
										ConfirmGoalActivity.this.finish();
									}
								})
						.setNegativeButton("목표 유지",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).show();
			}
		});

		btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//container = (LinearLayout) findViewById(R.id.confirmLayout);
				ImageView container = (ImageView)findViewById(R.id.confirmgoal_img_goal);
				container.buildDrawingCache();
				Bitmap captureView = container.getDrawingCache();
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(Environment
							.getExternalStorageDirectory().toString()
							+ "/TempPennyWise.PNG");
					captureView.compress(Bitmap.CompressFormat.PNG, 100, fos);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				Intent intent = new Intent(ConfirmGoalActivity.this,
						TempFBloginActivity.class);
				startActivity(intent);
			}
		});

		edt_goalMoney.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().equals("")) {
					userData.setGoalMoney(0L);
					edt_goalMoney.setText("0");
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				long money = 0L;
				try {
					money = Long.parseLong(s.toString());
				} catch (Exception e) {
					money = 0L;
				}
				userData.setGoalMoney(money);
				refreshAchievement();
			}
		});

		img_goal.setClickable(false);
		img_goal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!bEditMode)
					return;
				// 사진선택하기 or 카메라
				String[] fromList = { "사진 찍기", "갤러리에서 선택" };
				new AlertDialog.Builder(ConfirmGoalActivity.this)
						.setItems(fromList,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent;
										switch (which) {
										case 0:
											doTakePhotoActionOtherType();
											break;
										case 1:
											doTakeAlbumAction();
											break;
										default:
											break;
										}
									}
								}).setTitle("목표 이미지 변경").show();
			}
		});

		usingList = new ArrayList<SaveObjectInfo>();
		ArrayList<SaveObjectInfo> objList = userData.getSaveObjList();
		for (SaveObjectInfo info : objList) {
			if (info.isUse())
				usingList.add(info);
		}

		SaveListAdapter adapter = new SaveListAdapter(this,
				R.layout.saveobj_confirm_view, usingList);
		listView = (ListView) findViewById(R.id.confirmgoal_list_saveobj);
		listView.setAdapter(adapter);
	}

	private void refreshAchievement() {
		txt_nowMoney.setText("￦ "
				+ SaveMoneyUtils.convertMoneyComma(userData.getNowMoney()));
		txt_rate.setText("(" + userData.getAchieveRate() + "%)");
	}

	public boolean isEditMode() {
		return bEditMode;
	}

	private void endEdit() {
		bEditMode = false;

		txt_title.setVisibility(View.VISIBLE);
		edt_title.setVisibility(View.GONE);
		userData.setGoalName(edt_title.getText().toString());
		txt_title.setText(userData.getGoalName());
		txt_goalMoney.setVisibility(View.VISIBLE);
		edt_goalMoney.setVisibility(View.GONE);
		userData.setGoalMoney(Long
				.parseLong(edt_goalMoney.getText().toString()));
		txt_goalMoney.setText("￦ "
				+ SaveMoneyUtils.convertMoneyComma(userData.getGoalMoney()));
		img_goal.setClickable(false);

		btn_edit.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.INVISIBLE);
		btn_cancel.setVisibility(View.INVISIBLE);
		btn_share.setVisibility(View.VISIBLE);
		listView.invalidateViews();

		refreshAchievement();
		userData.saveData(this);
	}

	@Override
	public void onBackPressed() {
		if (bEditMode) {
			endEdit();
		} else {
			new AlertDialog.Builder(this)
					.setMessage("정말 종료하시겠습니까?")
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									ConfirmGoalActivity.this.finish();
								}
							})
					.setNegativeButton("취소",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
	}

	class SaveListAdapter extends BaseAdapter {
		Context mCtx;
		LayoutInflater inflater;
		ArrayList<SaveObjectInfo> list;
		int layout;

		public SaveListAdapter(Context ctx, int layout,
				ArrayList<SaveObjectInfo> saveList) {
			mCtx = ctx;
			this.layout = layout;
			list = saveList;
			inflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public SaveObjectInfo getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return list.get(position).whatObj();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final SaveObjectInfo nowObj = list.get(position);
			
			if (convertView == null) {
				convertView = inflater.inflate(layout, parent, false);
			}

			
			// ListView에서 StartActivity.
			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(ConfirmGoalActivity.this,
							AdjustObjectDialogActivity.class);
					startActivityForResult(intent, 1);
					return false;
				}
			});

			final ImageView img_Obj = (ImageView) convertView
					.findViewById(R.id.confirmList_img);
			img_Obj.setImageResource(UserData.RESOURCES_SAVEOBJ_ON[nowObj
					.whatObj()]);
			// 위에 listView long click할때 이미지누르면 안됨 여기서 마져 처리 해 줘야함.
			img_Obj.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					Intent intent = new Intent(ConfirmGoalActivity.this,
							AdjustObjectDialogActivity.class);
					startActivityForResult(intent, 1);
					return false;
				}
			});

			final TextView txt_Money = (TextView) convertView
					.findViewById(R.id.confirmList_money);
			txt_Money.setText("" + nowObj.getMoney());

			final ImageView btn_inc = (ImageView) convertView
					.findViewById(R.id.confirmList_btn_increase);
			final ImageView btn_dec = (ImageView) convertView
					.findViewById(R.id.confirmList_btn_decrease);

			btn_inc.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					nowObj.addSave();
					refreshAchievement();
					userData.saveData(ConfirmGoalActivity.this);
				}
			});
			btn_dec.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					nowObj.subSave();
					refreshAchievement();
					userData.saveData(ConfirmGoalActivity.this);
				}
			});

			if (bEditMode) {
				btn_inc.setVisibility(View.VISIBLE);
				btn_dec.setVisibility(View.VISIBLE);
			} else {
				btn_inc.setVisibility(View.INVISIBLE);
				btn_dec.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}
	}

	private void setGoalImage(Bitmap bitmap) {
		img_goal.setImageBitmap(bitmap);
	}

	// 이미지 관련
	private Uri imageUri;
	private String imagePath;
	private int mDeviceType = 0;
	private final static int GALLERY_SELECT = 0;
	private final static int CAMERA_CAPTURE = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private String mTempPhotoFileName = "temp.jpg";
	private Bitmap tempGoalBitmap = null;

	private void doTakePhotoActionOtherType() throws ActivityNotFoundException {
		deleteTempFile(mTempPhotoFileName);
		mTempPhotoFileName = "tmp_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imageUri = getTempUri();
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CAMERA_CAPTURE);
	}

	private void doTakeAlbumAction() {
		deleteTempFile(mTempPhotoFileName);
		mTempPhotoFileName = "tmp_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		imageUri = getTempUri();
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("scale", "true");
		intent.putExtra("aspectX", 133);
		intent.putExtra("aspectY", 100);
		intent.putExtra("return-data", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, GALLERY_SELECT);
	}

	private Uri getTempUri() {
		try {
			return Uri.fromFile(getTempFile());
		} catch (NullPointerException e) {
			return null;
		}
	}

	private File getTempFile() {
		if (isSDCARDMOUNTED()) {
			File f = new File(Environment.getExternalStorageDirectory()
					+ "/temp/" + mTempPhotoFileName);
			try {
				f.createNewFile();
			} catch (IOException e) {
			}

			return f;
		} else
			return null;
	}

	private boolean isSDCARDMOUNTED() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;

		return false;
	}

	private void deleteTempFile(String fileName) {
		String filePath = Environment.getExternalStorageDirectory() + "/temp/"
				+ fileName;
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
	}

	private void SaveBitmapToFileCache(Bitmap bitmap, String strFileName) {
		// File fileCacheItem = new File(strFilePath);
		// OutputStream out = null;
		FileOutputStream out = null;

		try {
			// fileCacheItem.createNewFile();
			// out = new FileOutputStream(fileCacheItem);
			try {
				this.deleteFile(strFileName);
			} catch (Exception e) {
				;
			}
			out = openFileOutput(strFileName, MODE_PRIVATE);
			bitmap.compress(CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void callCropAction() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(imageUri, "image/*");
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);
		if (list.size() == 0) {
			// I tend to put any kind of text to be presented to the user as a
			// resource for easier translation (if it ever comes to that...)
			Toast.makeText(this, "CROP ACTIVTY NO EXIST", Toast.LENGTH_LONG);
			// this is the URI returned from the camera, it could be a file or a
			// content URI, the crop app will take any
			imageUri = null; // leave the picture there
			return; // leave this switch case...
		}
		intent.putExtra("aspectX", 133);
		intent.putExtra("aspectY", 100);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_FROM_CAMERA);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			ConfirmGoalActivity.this.finish();
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CROP_FROM_CAMERA:
				final Bundle extras = data.getExtras();
				//
				if (extras != null) {
					String filePath = Environment.getExternalStorageDirectory()
							+ "/temp/" + mTempPhotoFileName;

					if (tempGoalBitmap != null)
						tempGoalBitmap.recycle();
					tempGoalBitmap = extras.getParcelable("data");
					setGoalImage(tempGoalBitmap);

					// Log.d("SELECT FilePath by Camera", filePath);
					SaveBitmapToFileCache(tempGoalBitmap,
							SaveMoneyUtils.GOAL_IMG_NAME);
				}
				break;

			case GALLERY_SELECT:
				final Bundle extras2 = data.getExtras();
				if (extras2 != null) {
					String filePath = Environment.getExternalStorageDirectory()
							+ "/temp/" + mTempPhotoFileName;

					if (tempGoalBitmap != null)
						tempGoalBitmap.recycle();
					tempGoalBitmap = extras2.getParcelable("data");
					setGoalImage(tempGoalBitmap);

					// Log.d("SELECT FilePath by Gallery", filePath);
					SaveBitmapToFileCache(tempGoalBitmap,
							SaveMoneyUtils.GOAL_IMG_NAME);
				}
				break;

			case CAMERA_CAPTURE:
				callCropAction();
				break;

			}
		} else {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// temp file 제거
		deleteTempFile(mTempPhotoFileName);
	}

}
