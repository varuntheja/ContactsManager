package com.wowlabz.contactmanager;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDatabase extends SQLiteOpenHelper 
{
	// All Static variables
	private Context context;
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "contactsManager";
	// Contacts table name
	private static final String TABLE_LOCATIONS = "ContactsTable";
	// Contacts Table Columns names
	private static final String KEY_ID = "_id";
	private static final String KEY_CONTACT_NAME = "contactName";
	private static final String KEY_CONTACT_EMAIL = "contactEmail";
	private static final String KEY_CONTACT_NUMBER = "contactNumber";
	private static ContactsDatabase instance = null;
	private static SQLiteDatabase db; 

	public ContactsDatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		this.db = getWritableDatabase();
	}

	public static synchronized void createInstance(Context ctx) {
		if (instance == null) {
			instance = new ContactsDatabase(ctx);
		}
	}

	public static ContactsDatabase getInstance() {
		return instance;
	}

	//Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_CONTACT_NAME + " TEXT,"
				+ KEY_CONTACT_EMAIL + " TEXT," + KEY_CONTACT_NUMBER + " TEXT"+")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public void addNewContact( String name,String email,String number)
	{
		//SQLiteDatabase db = this.getWritableDatabase();

		File database=context.getDatabasePath(DATABASE_NAME);

		if(!database.exists()) 
		{
			// Database does not exist so copy it from assets here
			Log.i("Database", "Database Not Found");
			return;
		} 
		else 
		{
			Log.i("Database", "addNewContact "+number+","+name+","+email);
		}

		ContentValues values = new ContentValues();
		
		values.put(KEY_CONTACT_NAME, name);
		values.put(KEY_CONTACT_EMAIL, email);
		values.put(KEY_CONTACT_NUMBER, number); 

		// Inserting Row
		db.insert(TABLE_LOCATIONS, null, values);
		//db.close(); // Closing database connection
	}
	
	
	// Getting All Contacts
	public ArrayList<ContactsHelper> getAllContacts() 
	{
		File database = context.getDatabasePath(DATABASE_NAME);

		if(!database.exists()) 
		{
			// Database does not exist so copy it from assets here
			Log.i("Database", "getPickupLocation Database Not Found");
		} 
		else 
		{
			Log.i("Database", "getPickupLocation Database Found");
		}

		ArrayList<ContactsHelper> contactList = new ArrayList<ContactsHelper>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do 
			{
				ContactsHelper contact = new ContactsHelper();
				contact.setId(Integer.parseInt(cursor.getString(0)));
				contact.setContactNumber(cursor.getString(2));
				contact.setContactName(cursor.getString(1));
				contact.setContactEmail(cursor.getString(3));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		cursor.close();
		//db.close();
		// return contact list
		return contactList;
	}

	// Updating single contact
	public int updateContact(ContactsHelper cotact) 
	{
		//SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CONTACT_NAME, cotact.getContactName());
		values.put(KEY_CONTACT_EMAIL, cotact.getContactEmail());
		values.put(KEY_CONTACT_NUMBER, cotact.getContactNumber());

		String where = "_id LIKE ?";
		String[] whereArgs = { String.valueOf(cotact.getId())};

		return db.update(TABLE_LOCATIONS, values, where, whereArgs);
	}
	
	
	// Deleting single contact
	public void deleteContact(String number) {
		//SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOCATIONS, KEY_ID + " = ?",
				new String[] { String.valueOf(number) });
		
		//db.close();
	}

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT * FROM " + TABLE_LOCATIONS;
		//SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int c = cursor.getCount();
		cursor.close();
		//db.close();
		// return count
		return c;
	}
}