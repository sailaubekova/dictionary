package com.example.dictionaryproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "myDB", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		
		arg0.execSQL("CREATE TABLE words ("  + "id INTEGER PRIMARY KEY AUTOINCREMENT," 
											  + "word TEXT," 
											  + "translation TEXT," 
											  + "definition TEXT" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}