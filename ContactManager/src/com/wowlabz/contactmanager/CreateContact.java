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

public class CreateContact extends ActionBarActivity implements OnClickListener
{
	private EditText name,email,phone;
	private TextView nameError,emailError,phoneError;
	private Button create;
	private ContactsDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_contact);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.actionbar);

		TextView textView = (TextView)findViewById(R.id.login_nav_text);
		textView.setText("Create Contact");

		name = (EditText)findViewById(R.id.contact_name);
		email = (EditText)findViewById(R.id.contact_email);
		phone = (EditText)findViewById(R.id.contact_telephone);

		nameError = (TextView)findViewById(R.id.contact_name_error);
		emailError = (TextView)findViewById(R.id.contact_email_error);
		phoneError = (TextView)findViewById(R.id.contact_telephone_error);

		create = (Button)findViewById(R.id.crate_btn);
		create.setOnClickListener(this);
		
		ContactsDatabase.createInstance(CreateContact.this);
		database = ContactsDatabase.getInstance();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if(v.getId() == R.id.crate_btn)
		{
			if(validateFields())
			{
				database.addNewContact(name.getText().toString().trim(), email.getText().toString().trim(),
						phone.getText().toString().trim());
				
				Intent returnIntent = new Intent();
				setResult(RESULT_OK,returnIntent);
				finish();
			}
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
