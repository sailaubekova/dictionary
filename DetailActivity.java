package com.example.dictionaryproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity {
	
	private TextView txtWord;
	private TextView txtTranslation;
	private TextView txtDefiniton;
	private Intent intent;
	private DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		txtWord = (TextView) findViewById(R.id.txtWord);
		txtTranslation = (TextView) findViewById(R.id.txtTranslation);
		txtDefiniton = (TextView) findViewById(R.id.txtDefinition);
		intent = getIntent();
		txtWord.setText(intent.getStringExtra("word"));
		txtTranslation.setText(intent.getStringExtra("translation"));
		txtDefiniton.setText(intent.getStringExtra("definition"));
		dbHelper = new DBHelper(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if(id == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		} else if(id == R.id.itemAdd) {
			new AddToTheElected(intent.getStringExtra("word"), intent.getStringExtra("translation"), intent.getStringExtra("definition")).execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class AddToTheElected extends AsyncTask<Void, Void, Long> {
		
		private String word;
		private String translation;
		private String definition;
		private ProgressDialog progressDialog;
		private ContentValues cv;
		private SQLiteDatabase db;
		
		public AddToTheElected(String _word, String _translation, String _definition) {
			this.word = _word;
			this.translation = _translation;
			this.definition = _definition;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(DetailActivity.this);
			progressDialog.setTitle("Please, wait!");
			progressDialog.setMessage("Storing in the database...");
			progressDialog.setCancelable(true);
			progressDialog.setIndeterminate(false);
			progressDialog.show();
		}
		
		@Override
		protected Long doInBackground(Void... params) {
			db = dbHelper.getWritableDatabase();
			cv = new ContentValues();
			cv.put("word", word);
			cv.put("translation", translation);
			cv.put("definition", definition);
			long success = db.insert("words", null, cv);
			dbHelper.close();
			return success;
		}
		
		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result != -1) {
				Toast.makeText(getApplicationContext(), "The word successfully added to the elected!", Toast.LENGTH_SHORT).show();
			} else Toast.makeText(getApplicationContext(), "Some error happens!", Toast.LENGTH_SHORT).show();
		}
	}
}
