package com.yeetou.xinyongkaguanjia.http.base;

public class MsgDataBase {

	private String received_tm;
	private int msg_id;
	private int state;

	public String getReceived_tm() {
		return received_tm;
	}

	public void setReceived_tm(String received_tm) {
		this.received_tm = received_tm;
	}

	public int getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(int msg_id) {
		this.msg_id = msg_id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
