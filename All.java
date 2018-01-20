package com.example.dictionaryproject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class All extends Fragment {
	
	private ProgressDialog pDialog;
	private ListView listView;
	private List<String> words;
	private List<String> translations;
	private List<String> definitions;
	private ArrayAdapter<String> adapter;
	private AutoCompleteTextView autoCompleteTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.all, container, false);
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int pos, long arg3) {
				String str = ((TextView) arg1).getText().toString();
				int x;
				for(x=0;x<words.size();x++) {
					if(str.equals(words.get(x))) {
						break;
					}
				}
				Intent i = new Intent(getActivity(), DetailActivity.class);
				i.putExtra("word", words.get(x));
				i.putExtra("translation", translations.get(x));
				i.putExtra("definition", definitions.get(x));
				startActivity(i);
			}
			
		});
		pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                Constants.GET_DICTIONARY, null, new Response.Listener<JSONObject>() {
     
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                        	if(response.getInt("success") == 1) {
                        		JSONArray objects = response.getJSONArray("objects");
                        		for(int i=0;i<objects.length();i++) {
                        			JSONObject object = objects.getJSONObject(i);
                        			words.add(object.getString("word"));
                        			translations.add(object.getString("translation"));
                        			definitions.add(object.getString("definition"));
                        		}
                        	}
						} catch (JSONException e) {
							e.printStackTrace();
						}
                        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, words);
                        autoCompleteTextView.setAdapter(adapter);
                        listView.setAdapter(adapter);
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
     
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
		return rootView;
	}
	
	private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
 
    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
