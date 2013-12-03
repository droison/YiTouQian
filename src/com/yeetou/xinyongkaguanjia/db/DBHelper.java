package com.yeetou.xinyongkaguanjia.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper dbHelper;

	private int openedConnections = 0;

	public synchronized SQLiteDatabase getReadableDatabase() {
		openedConnections++;
		return super.getReadableDatabase();
	}

	public synchronized SQLiteDatabase getWritableDatabase() {
		openedConnections++;
		return super.getWritableDatabase();
	}

	public synchronized void close() {
		openedConnections--;
		if (openedConnections == 0) {
			super.close();
		}
	}

	public static DBHelper dbHelper() {
		return dbHelper;
	}

	public static void init(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
	}

	private DBHelper(Context context) {
		super(context, "yitou.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String card_streams = "create table card_streams" +
				"(_id integer primary key," +
				"thedate long," +
				"year integer," +
				"month integer," +
				"day integer," +
				"description varchar(255)," +
				"amount float," +
				"category varchar(20)," +
				"message_id integer," +  
				"bank_card_id varchar(20)," +
				"bill_id integer," +
				"isPay integer check(isPay in(0,1)),"+ //1为支出,0为收入
				"currency vachar(20)," +
				"state integer," +
				"card_number vachar(20)," +
				"bank_logo vachar(20)," +
				"bank_name vachar(20)" +
				");";
		String lccp = "create table lccp" +
				"(_id integer primary key," +
				"bank_name varchar(20)," +
				"bank_logo vachar(20)," +
				"thedate long," +
				"is_choose boolean" +
				");";
		String account = "create table account" +
				"(_id integer primary key," +
				"phone varchar(20)," +
				"email varchar(255)," +  //易投邮箱
				"secret varchar(255)," +
				"token varchar(20)," +   //15位IEMI码
				"msgscan_at long," +
				"syn_at long," +
				"iv varchar(255));";
		String email = "create table email" +
				"(_id integer primary key," +
				"email varchar(255)," +  //
				"state integer," +  //
				"created_at long," +  //
				"updated_at long);";
		String category = "create table category(_id integer primary key," +
				"name varchar(20));";
		String card_bank_cards = "create table card_bank_cards" +
				"(_id integer primary key," +
				"card_type              vachar(20)," +   //credit or 
				"bank_id              integer," +   //card_bank的主键
				"name              vachar(20)," +   //用户姓名 
				"sex                  varchar(255) default '先生（女士）'," +
				"number               vachar(20)," +       //后四位数字
				"credit_limit         integer," +
				"cash_limit           integer," +
				"source_from           vachar(20)," +
				"created_at           long," +
				"updated_at                long," +
				"bank_logo vachar(20)," +
				"bank_name vachar(20));";
		String card_bills = "create table card_bills" +
				"(_id integer primary key," +
				"bill_type vachar(20) default 'RMB'," +   //defalut RMB
				"year              vachar(10)," +        //
				"month              vachar(10)," +   //
				"due_date           long," +          //到期还款日
				"billing_date               long," +       //账单日
				"new_balance         float," +           //本期还款总额
				"min_payment           float," +         //本期最底还款额
				"pay_state           integer," +        //0未还 1部分 2还清
				"created_at           long," +           
				"updated_at                long);";
		String card_to_bills = "create table card_to_bills" +
				"(_id integer primary key," +
				"bank_card_id           integer," +        
				"bill_id           integer," +       
				"created_at           long," +           
				"updated_at                long);";
		String card_banks = "create table card_banks" +
				"(_id integer primary key," +
				"name                 varchar(20)," +  //"招商银行"
				"spell_name           varchar(20),"+  //"zhaoshang"
				"logo           varchar(20),"+  //"zhaoshang"
				"phone                varchar(20)," +     //"95555"
				"cc_yd                varchar(20)," +  //移动电话
				"cc_lt           varchar(20),"+  //联通电话
				"hotline                varchar(50)," +     //"拨打4008205555"
				"manual                 varchar(50)," +  //"拨打4008205555→身份证号码和查询密码→按9→人工服务→"
				"loss           varchar(50),"+  //"拨打4008205555→按2#→挂失"
				"query_bill                varchar(50)," +     //"移动用户发送#ZD到1065795555"
				"query_limit                varchar(50)," +     //"移动用户发送#ED到1065795555"
				"query_credit                varchar(50));";    //"移动用户发送#JF到1065795555"
		
		String msgs ="create table msgs" +
				"(_id integer primary key," +
				"raw_id varchar(20)," +
				"address varchar(20),"+
				"received_tm long," +
				"body varchar(500));";
		db.execSQL(card_streams);
		db.execSQL(lccp);
		db.execSQL(account);
		db.execSQL(email);
		db.execSQL(card_bills);
		db.execSQL(card_to_bills);
		db.execSQL(category);
		db.execSQL(card_bank_cards);
		db.execSQL(card_banks);
		db.execSQL(msgs);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF card_streams");
		db.execSQL("DROP TABLE IF account");
		db.execSQL("DROP TABLE IF email");
		db.execSQL("DROP TABLE IF card_bank_cards");
		db.execSQL("DROP TABLE IF card_banks");
		db.execSQL("DROP TABLE IF card_bills");
		db.execSQL("DROP TABLE IF card_to_bills");
		db.execSQL("DROP TABLE IF category");
		db.execSQL("DROP TABLE IF lccp");
		db.execSQL("DROP TABLE IF msgs");
		onCreate(db);
	}

}
