package com.yeetou.xinyongkaguanjia.util;

import java.util.List;


import com.yeetou.xinyongkaguanjia.db.service.DbSMSService;
import com.yeetou.xinyongkaguanjia.info.MsgInfo;

public class SaveMsgToDB implements Runnable{
	private DbSMSService dbsmss;
	private List<MsgInfo> msgInfos;
	
	public SaveMsgToDB(DbSMSService dbsmss, List<MsgInfo> msgInfos){
		this.dbsmss = dbsmss;
		this.msgInfos = msgInfos;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(msgInfos != null){
			dbsmss.save(msgInfos);
		}
	}

	
}
