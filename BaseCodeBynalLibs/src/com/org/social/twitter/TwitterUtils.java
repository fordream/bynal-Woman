package com.org.social.twitter;

import junit.framework.Assert;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class TwitterUtils {
	public interface ITwitterUtils {
		void succesLoadProfile();

	}

	ITwitterUtils iTwitterUtils;

	public void setITwitterUtils(ITwitterUtils iTwitterUtils) {
		this.iTwitterUtils = iTwitterUtils;
	}

	private static TwitterUtils twitterUtils = new TwitterUtils();

	public static TwitterUtils getInstance() {
		return twitterUtils == null ? (twitterUtils = new TwitterUtils())
				: twitterUtils;
	}

	private TwitterUtils() {
	}


	static final String TAG = TwitterUtils.class.toString();

	public static final String VERIFY_URL_STRING = "https://api.twitter.com/1.1/account/verify_credentials.json";
	public static final String PUBLIC_TIMELINE_URL_STRING = "https://api.twitter.com/1.1/statuses/public_timeline.json";
	public static final String USER_TIMELINE_URL_STRING = "https://api.twitter.com/1.1/statuses/user_timeline.json";
	public static final String HOME_TIMELINE_URL_STRING = "https://api.twitter.com/1.1/statuses/home_timeline.json";
	public static final String FRIENDS_TIMELINE_URL_STRING = "https://api.twitter.com/1.1/statuses/friends_timeline.json";
	public static final String STATUSES_URL_STRING = "https://api.twitter.com/1.1/statuses/update.json";

	public static final String USER_TOKEN = "user_token";
	public static final String USER_SECRET = "user_secret";
	public static final String REQUEST_TOKEN = "request_token";
	public static final String REQUEST_SECRET = "request_secret";

	public static final String TWITTER_REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	public static final String TWITTER_ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	public static final String TWITTER_AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

	public static final String CALLBACK_URL = "bloa-app://twitt";

//	public static final String CALLBACK_URL = "oauth://t4jsample";
	public static final String STRING_CALLBACK = "bloa-app://twitt?oauth_token=";
	public static final String STRING_CALLBACK_DENIED = "bloa-app://twitt?denied=";
	public static final Uri CALLBACK_URI = Uri.parse(CALLBACK_URL);

	public static final int BLOA_LOADER_ID = 1;
	public static final int LIST_LOADER_ID = 2;

	// Use these so you don't have to look up the columns eat time
	public static final int IDX_USER_STATUS_USER_NAME = 0;
	public static final int IDX_USER_STATUS_USER_TEXT = 1;
	public static final int IDX_USER_STATUS_USER_ID = 2;
	public static final int IDX_USER_STATUS_USER_CREATED_DATE = 3;
	public static final int IDX_USER_STATUS_ID = 4;
	public static final int IDX_USER_STATUS_CREATED_DATE = 5;
	public static final int IDX_USER_STATUS_LATEST_STATUS = 6;

	public void saveRequestInformation(String token, String secret) {
		SharedPreferences.Editor editor = mSettings.edit();
		if (token == null) {
			editor.remove(REQUEST_TOKEN);
			Log.d(TAG, "Clearing Request Token");
		} else {
			editor.putString(REQUEST_TOKEN, token);
			Log.d(TAG, "Saving Request Token: " + token);
		}
		if (secret == null) {
			editor.remove(REQUEST_SECRET);
			Log.d(TAG, "Clearing Request Secret");
		} else {
			editor.putString(REQUEST_SECRET, secret);
			Log.d(TAG, "Saving Request Secret: " + secret);
		}
		editor.commit();

	}

	public void save(String requestToken, String replace) {
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putString(requestToken, replace);
		editor.commit();
	}

	public void saveAuthInformation(String token, String secret) {
		// null means to clear the old values
		SharedPreferences.Editor editor = mSettings.edit();
		if (token == null) {
			editor.remove(USER_TOKEN);
			Log.d(TAG, "Clearing OAuth Token");
		} else {
			editor.putString(USER_TOKEN, token);
			Log.d(TAG, "Saving OAuth Token: " + token);
		}
		if (secret == null) {
			editor.remove(USER_SECRET);
			Log.d(TAG, "Clearing OAuth Secret");
		} else {
			editor.putString(USER_SECRET, secret);
			Log.d(TAG, "Saving OAuth Secret: " + secret);
		}
		editor.commit();

	}

	private OAuthConsumer mConsumer = null;
	private OAuthProvider mProvider = null;

	private KeysProvider mKeysProvider;

	private KeysProvider getKeysProvider() {
		return mKeysProvider;
	}

	public void setKeysProvider(KeysProvider kp) {
		mKeysProvider = kp;
	}

	public OAuthConsumer getOAuthConsumer() {
		return mConsumer;
	}

	public OAuthProvider getOAuthProvider() {
		return mProvider;
	}

	private SharedPreferences mSettings;
	private Context context;

	public void onCreate(Context context) {
		this.context = context;
		mSettings = PreferenceManager.getDefaultSharedPreferences(context);
		setKeysProvider(new KeysProvider() {
			@Override
			public String getKey1() {
				// return "zusnCMZebRjHbKcwilPjo0y2Q";
				return "zusnCMZebRjHbKcwilPjo0y2Q";
			}

			@Override
			public String getKey2() {
				// return "7hcEn5HroMrYULQEFLDC1nSO3taD5QtY6WRk9QGX1jiZmbYGn7";
				return "7hcEn5HroMrYULQEFLDC1nSO3taD5QtY6WRk9QGX1jiZmbYGn7";
			}
		});

		mConsumer = new CommonsHttpOAuthConsumer(getKeysProvider().getKey1(),
				getKeysProvider().getKey2());

		mProvider = new CommonsHttpOAuthProvider(TWITTER_REQUEST_TOKEN_URL,
				TWITTER_ACCESS_TOKEN_URL, TWITTER_AUTHORIZE_URL);

		Assert.assertNotNull(mConsumer);
		Assert.assertNotNull(mProvider);

		mProvider.setOAuth10a(true);
	}

	public String getString(String key) {
		return mSettings.getString(key, null);
	}

	public boolean contains(String userToken) {
		return mSettings.contains(userToken);
	}

	public String getToken() {
		return getString(TwitterUtils.USER_TOKEN);
	}

	public String getSecret() {
		return getString(TwitterUtils.USER_SECRET);
	}

	public void onNewIntent(Intent intent) {
		Uri uri = intent.getData();
		if (uri != null) {
			String token = TwitterUtils.getInstance().getString(
					TwitterUtils.REQUEST_TOKEN);
			String secret = TwitterUtils.getInstance().getString(
					TwitterUtils.REQUEST_SECRET);

			if (token == null || secret == null) {
				throw new IllegalStateException("We should have saved!");
			}
			String otoken = uri.getQueryParameter(OAuth.OAUTH_TOKEN);
			if (otoken != null) {
				Assert.assertEquals(otoken, mConsumer.getToken());
				String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

				new AsyncTask<String, Void, Boolean>() {
					@Override
					protected Boolean doInBackground(String... params) {
						try {
							mProvider.retrieveAccessToken(mConsumer, params[0]);

							String token = mConsumer.getToken();
							String secret = mConsumer.getTokenSecret();
							TwitterUtils.getInstance().saveAuthInformation(
									token, secret);
							TwitterUtils.getInstance().saveRequestInformation(
									null, null);
							return true;
						} catch (Exception e) {
							return false;
						}
					}

					@Override
					protected void onPostExecute(Boolean success) {
						super.onPostExecute(success);
						if (success) {
						}
					}
				}.execute(verifier);
			} else {
				String denied = uri.getQueryParameter("denied");
			}
		}

	}

	public void onNewIntent(final String _url) {
		final Uri uri = Uri.parse(_url);
		String token = TwitterUtils.getInstance().getString(
				TwitterUtils.REQUEST_TOKEN);
		String secret = TwitterUtils.getInstance().getString(
				TwitterUtils.REQUEST_SECRET);

		if (token == null || secret == null) {
			throw new IllegalStateException("We should have saved!");
		}

		String otoken = uri.getQueryParameter(OAuth.OAUTH_TOKEN);
		if (otoken != null) {
			Assert.assertEquals(otoken, mConsumer.getToken());
			String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

			new AsyncTask<String, Void, Boolean>() {
				ProgressDialog progressDialog;

				protected void onPreExecute() {
					super.onPreExecute();

					if (progressDialog == null) {
						progressDialog = initProgressDialog();
					}
				};

				@Override
				protected Boolean doInBackground(String... params) {
					try {
						mProvider.retrieveAccessToken(mConsumer, params[0]);
						String token = mConsumer.getToken();
						String secret = mConsumer.getTokenSecret();
						saveAuthInformation(token, secret);
						saveRequestInformation(null, null);
						return true;
					} catch (Exception e) {
						return false;
					}
				}

				@Override
				protected void onPostExecute(Boolean success) {
					super.onPostExecute(success);

					progressDialog.dismiss();
					if (success) {
						onResume();
					}
				}
			}.execute(verifier);
		} else {
			String denied = uri.getQueryParameter("denied");
		}
	}

	public void onCreateWeb(Intent intent) {
		intent = new Intent();
		if (intent.getData() == null) {
			try {
				new AsyncTask<Void, Void, String>() {
					@Override
					protected String doInBackground(Void... params) {
						String url = null;
						try {
							url = mProvider.retrieveRequestToken(mConsumer,
									TwitterUtils.CALLBACK_URL);
						} catch (Exception e) {
						}
						return url;
					}

					@Override
					protected void onPostExecute(String url) {
						super.onPostExecute(url);
						if (url != null) {
							TwitterUtils.getInstance().saveRequestInformation(
									mConsumer.getToken(),
									mConsumer.getTokenSecret());
							TwDialogListener dialogListener = new TwDialogListener() {
								@Override
								public void onError(String value) {
									// Toast.makeText(context, value,
									// Toast.LENGTH_SHORT).show();

								}

								@Override
								public void onComplete(String value) {
									// Toast.makeText(context, value,
									// Toast.LENGTH_SHORT).show();
									onNewIntent(value);
								}
							};
							TwitterDialog twitterDialog = new TwitterDialog(
									context, url, dialogListener);
							twitterDialog.show();
						}
					}
				}.execute(new Void[0]);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void onResume() {
		if (contains(USER_TOKEN) && contains(USER_SECRET)) {
			if (!(getToken() == null || getSecret() == null)) {
				getOAuthConsumer().setTokenWithSecret(getToken(), getSecret());
				(new GetCredentialsTask()).execute();
			}
		}
	}

	class GetCredentialsTask extends AsyncTask<Void, Void, Boolean> {
		DefaultHttpClient mClient = new DefaultHttpClient();
		ProgressDialog mDialog;

		@Override
		protected void onPreExecute() {
			if (mDialog == null)
				mDialog = initProgressDialog();
		}

		JSONObject jso = null;

		@Override
		protected Boolean doInBackground(Void... arg0) {

			HttpGet get = new HttpGet(TwitterUtils.VERIFY_URL_STRING);
			try {
				TwitterUtils.getInstance().getOAuthConsumer().sign(get);
				String response = mClient.execute(get,
						new BasicResponseHandler());
				jso = new JSONObject(response);
				return true;
			} catch (Exception e) {
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean loggedIn) {
			if (mDialog != null)
				mDialog.dismiss();
			if (loggedIn) {
				saveAccount(jso);
				if (iTwitterUtils != null)
					iTwitterUtils.succesLoadProfile();
			}
		}
	}

	private void saveAccount(JSONObject jso) {
		try {

			save("name", jso.getString("name"));
			save("screen_name", jso.getString("screen_name"));
			save("id_str", jso.getString("id_str"));
		} catch (Exception exception) {
		}

	}

	public boolean isLogin() {
		return false;
	}

	private ProgressDialog initProgressDialog() {
		return ProgressDialog.show(context, "loading", "loading");
	}

	public String getID() {
		return getString("id_str");
	}

	public String getScreenName() {
		return getString("screen_name");
	}

	public String getName() {
		return getString("name");
	}

}