package com.wowlabz.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditDetailsActivity extends ActionBarActivity implements OnClickListener
{
	private EditText name,email,phone;
	private TextView nameError,emailError,phoneError;
	private Button update,delete;
	private ContactsDatabase database;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_details);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.edit_details_actionbar);
		
		
		Intent intent = getIntent();
		
		TextView textView = (TextView)findViewById(R.id.login_nav_text);
		textView.setText(intent.getStringExtra("NAME"));

		name = (EditText)findViewById(R.id.contact_name);
		email = (EditText)findViewById(R.id.contact_email);
		phone = (EditText)findViewById(R.id.contact_telephone);
		
		name.setText(intent.getStringExtra("NAME"));
		email.setText(intent.getStringExtra("EMAIL"));
		phone.setText(intent.getStringExtra("PHONE"));
		
		id = intent.getStringExtra("ID");

		nameError = (TextView)findViewById(R.id.contact_name_error);
		emailError = (TextView)findViewById(R.id.contact_email_error);
		phoneError = (TextView)findViewById(R.id.contact_telephone_error);

		update = (Button)findViewById(R.id.update_btn);
		delete = (Button)findViewById(R.id.delete_contact);
		update.setOnClickListener(this);
		delete.setOnClickListener(this);
		
		ContactsDatabase.createInstance(EditDetailsActivity.this);
		database = ContactsDatabase.getInstance();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if(v.getId() == R.id.update_btn)
		{
			if(validateFields())
			{
				ContactsHelper cotact = new ContactsHelper();
				cotact.setId(Integer.valueOf(id));
				cotact.setContactName(name.getText().toString().trim());
				cotact.setContactEmail(email.getText().toString().trim());
				cotact.setContactNumber(phone.getText().toString().trim());
				
				database.updateContact(cotact);
				
				Intent returnIntent = new Intent();
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		}
		else if(v.getId() == R.id.delete_contact)
		{
			database.deleteContact(id);
			Intent returnIntent = new Intent();
			setResult(RESULT_OK,returnIntent);
			finish();
		}
	}

	private boolean validateFields()
	{
		if(name.getText().toString().trim().equals(""))
		{
			nameError.setVisibility(View.VISIBLE);
			emailError.setVisibility(View.GONE);
			phoneError.setVisibility(View.GONE);
			return false;
		}
		else if(email.getText().toString().trim().equals(""))
		{
			emailError.setVisibility(View.VISIBLE);
			nameError.setVisibility(View.GONE);
			phoneError.setVisibility(View.GONE);
			return false;
		}
		else if(!isValidEmail(email.getText().toString().trim()))
		{
			emailError.setVisibility(View.VISIBLE);
			nameError.setVisibility(View.GONE);
			phoneError.setVisibility(View.GONE);
			return false;
		}
		else if(phone.getText().toString().trim().equals(""))
		{
			phoneError.setVisibility(View.VISIBLE);
			nameError.setVisibility(View.GONE);
			emailError.setVisibility(View.GONE);
			return false;
		}
		
		nameError.setVisibility(View.GONE);
		emailError.setVisibility(View.GONE);
		phoneError.setVisibility(View.GONE);
		
		return true;
	}
	
	private boolean isValidEmail(CharSequence target) 
	{
		return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

}
