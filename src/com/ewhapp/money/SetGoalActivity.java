package com.ewhapp.money;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SetGoalActivity extends SaveMoneyBaseActivity {
	private ImageView btn_next;
	private ImageView btn_prev;
	private ImageView imgv_goalImg;
	private EditText edt_goal;
	private EditText edt_money;
	private UserData userData;
	
	private boolean bSelectImg = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setgoal);
		
		SaveMoneyUtils.makeNomediaFile();
		
		userData = UserData.sharedUserData(this);
	
		btn_prev = (ImageView) findViewById(R.id.setgoal_btn_prev);
		btn_next = (ImageView) findViewById(R.id.setgoal_btn_next);
		imgv_goalImg = (ImageView) findViewById(R.id.setgoal_img_goal);
		edt_goal = (EditText) findViewById(R.id.setgoal_edt_goal);
		edt_money = (EditText) findViewById(R.id.setgoal_edt_money);
		edt_money.setFilters(new InputFilter[] {
			new InputFilter.LengthFilter(16)	
		});
		
		edt_goal.setText(userData.getGoalName());
		String moneyText = "";
		if(userData.getGoalMoney() > 0L) moneyText = ""+userData.getGoalMoney();
		edt_money.setText(moneyText);

		
		btn_next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 이미지, 목표, 목표금액 저장하
				long money = 0L;
				try {
					money = Long.parseLong(edt_money.getText().toString());
					if(money <= 0L) {
						new AlertDialog.Builder(SetGoalActivity.this)
						.setMessage("목표 금액은 0원 이상입니다.")
						.setPositiveButton("확인", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();
						return;
					}
				} catch(Exception e) {
					new AlertDialog.Builder(SetGoalActivity.this)
					.setMessage("목표 금액을 올바르게 입력해 주세요.")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
					return;
				}
				
				if(edt_goal.getText().toString().equals("")) {
					new AlertDialog.Builder(SetGoalActivity.this)
					.setMessage("목표를 입력해 주세요.")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
					return;
				}
				
				if(!bSelectImg) {
					new AlertDialog.Builder(SetGoalActivity.this)
					.setMessage("이미지를 추가해 주세요.")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
					return;
				}
				
				userData.setGoalMoney(money);
				userData.setGoalName(edt_goal.getText().toString());
				SaveBitmapToFileCache(tempGoalBitmap, SaveMoneyUtils.GOAL_IMG_NAME);

				startActivity(new Intent(SetGoalActivity.this, SetSaveObjectActivity.class));
				SetGoalActivity.this.finish();
			}
		});
		
		btn_prev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});

		imgv_goalImg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 사진선택하기 or 카메라
				String[] fromList = { "사진 찍기", "갤러리에서 선택" };
				new AlertDialog.Builder(SetGoalActivity.this).setItems(
						fromList, new DialogInterface.OnClickListener() {

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
						}).setTitle("목표 이미지 설정")
						.show();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
/*		Intent intent = new Intent();
		intent.setAction("com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST");
		sendBroadcast(intent);*/
		startActivity(new Intent(SetGoalActivity.this, MainActivity.class));
		this.finish();
	} 
	
	private void setGoalImage(Bitmap bitmap) {
		imgv_goalImg.setImageBitmap(bitmap);
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
		//File fileCacheItem = new File(strFilePath);
		//OutputStream out = null;
		FileOutputStream out = null;
		
		try {
			//fileCacheItem.createNewFile();
			//out = new FileOutputStream(fileCacheItem);
			try { this.deleteFile(strFileName); }
			catch(Exception e){;}
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
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CROP_FROM_CAMERA:
				final Bundle extras = data.getExtras();
				//
				if (extras != null) {
					String filePath = Environment.getExternalStorageDirectory()
							+ "/temp/" + mTempPhotoFileName;
					
					if(tempGoalBitmap!=null) tempGoalBitmap.recycle();
					tempGoalBitmap = extras.getParcelable("data");
					setGoalImage(tempGoalBitmap);
					
					bSelectImg = true;
					Log.d("SELECT FilePath by Camera", filePath);
				}
				break;

			case GALLERY_SELECT:
				final Bundle extras2 = data.getExtras();
				if(extras2 != null) {
					String filePath = Environment.getExternalStorageDirectory()
							+ "/temp/" + mTempPhotoFileName;
					
					if(tempGoalBitmap!=null) tempGoalBitmap.recycle();
					tempGoalBitmap = extras2.getParcelable("data");
					setGoalImage(tempGoalBitmap);
					
					bSelectImg = true;
					Log.d("SELECT FilePath by Gallery", filePath);
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
		
		//temp file 제거
		deleteTempFile(mTempPhotoFileName);
	}
}
