package com.airplug.audioplug.share;

/**
 * sns 상태 리스너
 * @author 1Ticket
 *
 */
public interface TwitterEventObserver {
	public void onLoginComplete();	/// 로그인 완료 시
	public void onWriteComplete();	/// 글 등록 완료 시
	public void onWriteFail();		/// 글 등록 실 패 시
}
