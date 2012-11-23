package com.ewhapp.money;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookActivity;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class RewardFBloginActivity extends FacebookActivity {
	Facebook facebook = new Facebook("109466259211773");
	
	private Button postPhotoButton;
	private UserData userData;
	private ProfilePictureView profilePictureView;
	private TextView greeting;
	private GraphUser user;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fb);

		userData = UserData.sharedUserData(this);
		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
		greeting = (TextView) findViewById(R.id.greeting);

		facebook.authorize(this, new String[] { "publish_stream",
				"offline_access", "user_about_me", "friends_about_me",
				"read_friendlists", "read_stream" }, new DialogListener() {

			public void onComplete(Bundle values) {
				// 로그인 완료후 동작
				profilePictureView.setProfileId(user.getId());
				greeting.setText(user.getFirstName());
				SharedPreferences pref = getSharedPreferences("Facebook.conf",
						0);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("ACCESS_TOKEN", facebook.getAccessToken());
				editor.commit();
			}

			public void onFacebookError(FacebookError e) {
				// 로그인 오류
			}

			public void onError(DialogError e) {
				// 동작 오류
			}

			public void onCancel() {
				// 취소 시
			}
		});
		postPhotoButton = (Button) findViewById(R.id.postPhotoButton);
		postPhotoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				String mes =   "축하해 주세요~! " + userData.getGoalName() + "을(를) 지를수 있어요. - PennyWise 에서";
	    		String imgpath = "/sdcard/PennyWise.PNG";
				Bitmap image = BitmapFactory.decodeFile(imgpath);
				Request request = Request.newUploadPhotoRequest(
						Session.getActiveSession(), mes, image,
						new Request.Callback() {
							@Override
							public void onCompleted(Response response) {
								showPublishResult(
										getString(R.string.photo_post),
										response.getGraphObject(),
										response.getError());
							}
						});
				Request.executeBatchAsync(request);
			}
		});

	}

	private interface GraphObjectWithId extends GraphObject {
		String getId();
	}

	private void showPublishResult(String message, GraphObject result,
			FacebookRequestError error) {
		String title = null;
		String alertMessage = null;
		if (error == null) {
			title = getString(R.string.success);
			String id = result.cast(GraphObjectWithId.class).getId();
			alertMessage = getString(R.string.successfully_posted_post);
			/*
			 * alertMessage = getString(R.string.successfully_posted_post,
			 * message, id);
			 */
		} else {
			title = getString(R.string.error);
			alertMessage = error.getErrorMessage();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title).setMessage(alertMessage)
				.setPositiveButton(getString(R.string.ok), null);
		builder.show();
	}

	protected void onActivity(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}
}
