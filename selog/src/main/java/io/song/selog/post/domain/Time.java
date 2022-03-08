package io.song.selog.post.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {

	public static String calculateTime(Date date) {
		long curTime = System.currentTimeMillis();
		long regTime = date.getTime();
		
		long seconds = (curTime - regTime) / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		String msg = null;
		
		if (seconds < 300) {
			msg = "방금 전";
		} else if (minutes < 60) {
			msg = (int)Math.floor(minutes) + "분 전";
		} else if (hours < 24) {
			msg = (int)Math.floor(hours) + "시간 전";
		} else if (days < 7) {
			msg = (int)Math.floor(days) + "일 전";
		} else {
			msg = new SimpleDateFormat("yyyy년 MM월 dd일").format(date);
		}

		return msg;
	}
}
