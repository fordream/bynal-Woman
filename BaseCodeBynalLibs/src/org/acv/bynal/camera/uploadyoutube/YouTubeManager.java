package org.acv.bynal.camera.uploadyoutube;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.acv.bynal.camera.VideoActivity;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Video;
import android.util.Log;

//YouTubeManager.YOUTUBENAME
//YouTubeManager.YOUTUBE_PASSWORD
public class YouTubeManager {

	public static final String YOUTUBENAME = "bynalytb@gmail.com";
	public static final String YOUTUBE_PASSWORD = "yt1212bynal";

	//
	public static final String INITIAL_UPLOAD_URL = "http://uploads.gdata.youtube.com/resumable/feeds/api/users/default/uploads";

	public static final String DEFAULT_VIDEO_CATEGORY = "News";
	public static final String DEFAULT_VIDEO_TAGS = "mobile";
	public static final boolean USEPATH = true;

	public static void submitToYtdDomain(String ytdDomain, String assignmentId, String videoId, String youTubeName, String clientLoginToken, String title, String description, Date dateTaken,
			String videoLocation, String tags) {
		JSONObject payload = new JSONObject();
		try {
			payload.put("method", "NEW_MOBILE_VIDEO_SUBMISSION");
			JSONObject params = new JSONObject();

			params.put("videoId", videoId);
			params.put("youTubeName", youTubeName);
			params.put("clientLoginToken", clientLoginToken);
			params.put("title", title);
			params.put("description", description);
			params.put("videoDate", dateTaken.toString());
			params.put("tags", tags);

			if (videoLocation != null) {
				params.put("videoLocation", videoLocation);
			}

			if (assignmentId != null) {
				params.put("assignmentId", assignmentId);
			}

			payload.put("params", params);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String jsonRpcUrl = "http://" + ytdDomain + "/jsonrpc";
		String json = VideoUtil.makeJsonRpcCall(jsonRpcUrl, payload);

		Log.i("JSON-Upload", payload.toString());

		if (json != null) {
			try {
				JSONObject jsonObj = new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public static final File getFileFromUri(Context context, Uri uri) throws IOException {

		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.getCount() == 0) {
			throw new IOException(String.format("cannot find data from %s", uri.toString()));
		} else {
			cursor.moveToFirst();
		}

		String filePath = cursor.getString(cursor.getColumnIndex(Video.VideoColumns.DATA));

		File file = new File(filePath);
		cursor.close();
		return file;
	}

	public static final String parseVideoId(InputStream atomDataStream) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(atomDataStream);

		NodeList nodes = doc.getElementsByTagNameNS("*", "*");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String nodeName = node.getNodeName();
			if (nodeName != null && nodeName.equals("yt:videoid")) {
				return node.getFirstChild().getNodeValue();
			}
		}
		return null;
	}

	public static final String getYouTubeId() {
		String youTubeId = null;
		try {
			youTubeId = new JSONObject(new JSONObject(VideoUtil.jsonData).getString("params")).getString("videoId");
		} catch (JSONException e) {
		}

		return youTubeId;

	}

	public static File getFilePath() {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "BynalCameraApp");
		mediaStorageDir.mkdirs();
		//if this "JCGCamera folder does not exist
        if (!mediaStorageDir.exists()) {
            //if you cannot make this folder return
            /*if (!mediaStorageDir.mkdirs()) {
                return null;
            }*/
        }
		// File mDirectory = new File(Environment.getExternalStorageDirectory()
		// + "/temp/");
		// mDirectory.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File videoFile = new File(mediaStorageDir.getPath(), "mvideo" + timeStamp + ".mp4");
		return videoFile;
	}
}