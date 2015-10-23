package org.acv.bynal.camera.uploadyoutube;

public class ResumeInfo {
	private int nextByteToUpload;
	private String videoId;

	public ResumeInfo(int nextByteToUpload) {
		this.nextByteToUpload = nextByteToUpload;
	}

	public ResumeInfo(String videoId) {
		this.videoId = videoId;
	}

	public int getNextByteToUpload() {
		return nextByteToUpload;
	}

	public String getVideoId() {
		return videoId;
	}
}