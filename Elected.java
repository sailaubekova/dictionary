package com.example.dictionaryproject;

import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

public class Elected extends Fragment {
	
	private ProgressDialog pDialog;
	private ListView listView;
	private List<String> words;
	private List<String> translations;
	private List<String> definitions;
	private DBHelper dbHelper;
	private ArrayAdapter<String> adapter;
	private AutoCompleteTextView autoCompleteTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.elected, container, false);
		words = new ArrayList<String>();
		translations = new ArrayList<String>();
		definitions = new ArrayList<String>();
		autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);
		autoCompleteTextView.setThreshold(0);
		autoCompleteTextView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				adapter.getFilter().filter(s);
			}
		});
		listView = (ListView) rootView.findViewById(R.id.listView);
		dbHelper = new DBHelper(getActivity());
		return rootView;	
		}
	
	public class ReadFromSQLite extends AsyncTask<Void, Void, Boolean> {
		
		private ProgressDialog progressDialog;
		private SQLiteDatabase db;
		private Cursor c;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setTitle("Please, wait!");
			progressDialog.setMessage("Loading elected words...");
			progressDialog.setCancelable(true);
			progressDialog.setIndeterminate(false);
			progressDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			db = dbHelper.getWritableDatabase();
			c = db.rawQuery("SELECT * FROM words ORDER BY word;", null);
			if(c.getCount() > 0) {
				return true;
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result) {
				if(c.moveToFirst()) {
					do {
						words.add(c.getString(c.getColumnIndex("word")));
						translations.add(c.getString(c.getColumnIndex("translation")));
						definitions.add(c.getString(c.getColumnIndex("definition")));
					} while(c.moveToNext());
				}
				adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, words);
                autoCompleteTextView.setAdapter(adapter);
                listView.setAdapter(adapter);
			} else Toast.makeText(getActivity(), "There is no any words!", Toast.LENGTH_SHORT).show();
		}
	}
}
