package com.wowlabz.contactmanager;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Contacts extends ActionBarActivity
{
	private ContactsDatabase database;
	private ListView listView;
	private ListAdapter adapter;
	private ArrayList<ContactsHelper> contactsList = new ArrayList<ContactsHelper>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.contacts_list);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.actionbar);

		listView = (ListView)findViewById(R.id.contacts_list);
		ContactsDatabase.createInstance(Contacts.this);
		database = ContactsDatabase.getInstance();

		contactsList = database.getAllContacts();

		adapter = new ListAdapter(Contacts.this, contactsList);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(Contacts.this,EditDetailsActivity.class);
				intent.putExtra("NAME",contactsList.get(position).getContactName() );
				intent.putExtra("EMAIL", contactsList.get(position).getContactNumber());
				intent.putExtra("PHONE", contactsList.get(position).getContactEmail());
				intent.putExtra("ID", ""+contactsList.get(position).getId());
				startActivityForResult(intent, 10);
			}
		});

		if(contactsList!=null && contactsList.size()==0)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Contacts.this);

			// set title
			alertDialogBuilder.setTitle("Contacts Info");

			// set dialog message
			alertDialogBuilder
			.setMessage("You don't have any contacts, please add contacts")
			.setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
					Intent intent = new Intent(Contacts.this,CreateContact.class);
					startActivityForResult(intent, 10);
				}
			});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_search, menu);

		SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
		searchView.setOnQueryTextListener(queryListener);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId()) 
		{
		case R.id.create:

			Intent intent = new Intent(Contacts.this,CreateContact.class);
			startActivityForResult(intent, 10);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	final private OnQueryTextListener queryListener = new OnQueryTextListener() {       
		@Override
		public boolean onQueryTextChange(String newText) {
			if (TextUtils.isEmpty(newText)) {

				adapter.filter("");
			} else {

				adapter.filter(newText);
			}   
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {            

			Toast.makeText(Contacts.this, "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();

			adapter.filter(query);

			return false;
		}
	};

	@Override
	protected void onNewIntent(Intent intent) {

		handleIntent(intent);
	}

	private void handleIntent(Intent intent) 
	{
		if(Intent.ACTION_SEARCH.equals(intent.getAction())) 
		{
			String query = intent.getStringExtra(SearchManager.QUERY);
			Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
		}
	}

	public class ListAdapter extends BaseAdapter {
		Context ctx;
		LayoutInflater lInflater;
		ArrayList<ContactsHelper> objects;
		ArrayList<ContactsHelper> arraylist;

		ListAdapter(Context context, ArrayList<ContactsHelper> contacts) {
			ctx = context;

			this.objects = new ArrayList<ContactsHelper>();
			this.objects = contacts;

			this.arraylist = new ArrayList<ContactsHelper>();
			this.arraylist.addAll(contacts);

			lInflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return objects.size();
		}

		@Override
		public Object getItem(int position) {
			return objects.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = lInflater.inflate(R.layout.contact_row, parent, false);
			}

			ContactsHelper p = getProduct(position);

			((TextView) view.findViewById(R.id.contact_name)).setText(p.getContactName());
			((TextView) view.findViewById(R.id.contact_phone)).setText(p.getContactNumber());
			((TextView) view.findViewById(R.id.contact_email)).setText(p.getContactEmail());

			return view;
		}

		ContactsHelper getProduct(int position) {
			return ((ContactsHelper) getItem(position));
		}


		// Filter Class
		public void filter(String charText) {
			charText = charText.toLowerCase(Locale.getDefault());
			objects.clear();
			if (charText.length() == 0) {
				objects.addAll(arraylist);
			} 
			else 
			{
				for (ContactsHelper wp : arraylist) 
				{
					if(wp.getContactName().toLowerCase(Locale.getDefault()).contains(charText)) 
					{
						objects.add(wp);
					}
					else if(wp.getContactNumber().toLowerCase(Locale.getDefault()).contains(charText)) 
					{
						objects.add(wp);
					}
					else if(wp.getContactEmail().toLowerCase(Locale.getDefault()).contains(charText)) 
					{
						objects.add(wp);
					}
				}
			}
			notifyDataSetChanged();
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 10)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				contactsList.clear();
				contactsList = database.getAllContacts();
				adapter = new ListAdapter(Contacts.this, contactsList);
				listView.setAdapter(adapter);
			}
		}
	}
}
