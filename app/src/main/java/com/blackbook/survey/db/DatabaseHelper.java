package com.blackbook.survey.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.blackbook.survey.Utils.Utils;
import com.blackbook.survey.model.OrganizationType;
import com.blackbook.survey.model.Questions;
import com.blackbook.survey.model.ScoreMatrix;
import com.blackbook.survey.model.SurveyType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "surveyapp.sqlite";
	private static final int DATABASE_VERSION = 2;
	private final Context myContext;
	private SQLiteDatabase myDataBase;

	private static String DB_PATH;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 **/
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}

		this.myContext = context;

		try {
			createDataBase();
		} catch (Exception e) {
			Log.i(Utils.TAG,
					"DatabaseHelper_constuctor createDataBase Error :"
							+ e.fillInStackTrace());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    Log.i(Utils.TAG, "Creating tables.");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 **/
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			// Database already exist
			openDataBase();

		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			myDataBase = getWritableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.i(Utils.TAG, "createDataBase Error : " + e.fillInStackTrace());
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 **/
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			// database does't exist yet.
			Log.i(Utils.TAG, "checkDataBase : " + e.fillInStackTrace());
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transferring bytestream.
	 **/
	private void copyDataBase() throws IOException {
		try {
			// Open your local db as the input stream
			InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
			// Path to the just created empty db
			String outFileName = DB_PATH + DATABASE_NAME;

			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[myInput.available()];
			int read;
			while ((read = myInput.read(buffer)) != -1) {
				myOutput.write(buffer, 0, read);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();

		} catch (IOException e) {
			Log.i(Utils.TAG, "copyDataBase Error : " + e.getMessage());
		}
	}

	public void openDataBase() throws SQLException {
		try {
			// Open the database
			String myPath = DB_PATH + DATABASE_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

		} catch (SQLiteException e) {
			Log.i(Utils.TAG, "openDataBase Error : " + e.fillInStackTrace());
		}
	}

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy to you to create adapters for your views.

	// ***************************** My DML Methods
	// *****************************

	public List<String> getSingleColumn(String query, String[] arguments) {
		List<String> list = new ArrayList<>();
		Cursor cursor;
		try {

			cursor = myDataBase.rawQuery(query, arguments);

			if (cursor != null) {
				while (cursor.moveToNext())
					list.add(cursor.getString(0));
				cursor.close();
			}

		} catch (Exception e) {
			Log.i(Utils.TAG, "getSingleColumn Error : " + e.fillInStackTrace());
		}
		return list;
	}

	public boolean insertSingleRow(String tableName, ContentValues values) {
		boolean success = false;
		try {
			if (myDataBase.insert(tableName, null, values) != -1) {
				// Log.i(TAG,"Successfully inserted into "+ tableName +"..."
				// +values);
				success = true;
			} else
				Log.i(Utils.TAG, "insertSingleRow : -1 returned " + tableName + "..." + values);
		} catch (Exception e) {
			Log.i(Utils.TAG, "insertSingleRow Error : " + e.fillInStackTrace());
		}
		return success;
	}

	public boolean updateSingleRow(String tablename,
			ContentValues contentValues, String whereCondition,
			String[] whereArgs) {
		boolean isUpdated = false;
		try {

			if (myDataBase.update(tablename, contentValues, whereCondition,
					whereArgs) != -1) {
				isUpdated = true;
				Log.i(Utils.TAG, "Successfully updated : " + tablename + " "
						+ contentValues + " " + whereCondition + " "
						+ whereArgs);
			} else
				Log.i(Utils.TAG, "updateSingleRow : -1 returned");
		} catch (Exception e) {
			Log.i(Utils.TAG, "updateSingleRow Error : " + e.fillInStackTrace()
					+ "_____Err : " + tablename + " " + contentValues + " "
					+ whereCondition + " " + whereArgs);
		}
		return isUpdated;
	}

	public boolean deleteQuery(String table, String whereClause,
			String[] whereArgs) {
		boolean isDeleted = false;
		try {
			// deletes from database
			if (myDataBase.delete(table, whereClause, whereArgs) >= 0) {
				isDeleted = true;
				Log.i(Utils.TAG, "Successfully deleted : " + table + " "
						+ whereClause + " " + whereArgs);
			} else
				Log.i(Utils.TAG, "deleteQuery Error : < 0 returned");
			//
		} catch (Exception e) {
			Log.i(Utils.TAG, "deleteQuery Error : " + e.fillInStackTrace());
		}
		return isDeleted;
	}

	// This Method Returns class column data
	public ArrayList<SurveyType> GetaAllSurviceType(String parentid)
	{
		ArrayList<SurveyType> list = new ArrayList<>();
		list.clear();

		Cursor cursor;
		String selectQuery = "select * from tblsurveytype where parentid="+ "\'" + parentid + "\'";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					SurveyType obj = new SurveyType();

					obj.setId(cursor.getString(cursor.getColumnIndex("surveytypeid")));
					obj.setSurvey_type_name(cursor.getString(cursor.getColumnIndex("surveytypename")));
					obj.setLevel(cursor.getString(cursor.getColumnIndex("level")));
					obj.setParent_id(cursor.getString(cursor.getColumnIndex("parentid")));
					obj.setCreated_date(cursor.getString(cursor.getColumnIndex("createddate")));
					obj.setModified_date(cursor.getString(cursor.getColumnIndex("modifieddate")));
					obj.setIs_deleted(cursor.getString(cursor.getColumnIndex("isdeleted")));

					list.add(obj);
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return list;
	}

	// This Method Returns class column data
	public ArrayList<String> GetaDataparentid(String parentid)
	{
		ArrayList<String> list = new ArrayList<>();
		list.clear();

		Cursor cursor;
		String selectQuery = "select surveytypename from tblsurveytype where parentid="+ "\'" + parentid + "\'";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					list.add(cursor.getString(0));
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return list;
	}

	// This Method Returns class column data
	public ArrayList<String> GetaRoleDataparentid(String parentid)
	{
		ArrayList<String> list = new ArrayList<>();
		list.clear();

		Cursor cursor;
		String selectQuery = "select rolename from tblroles where parentid="+ "\'" + parentid + "\'";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					list.add(cursor.getString(0));
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return list;
	}

	// This Method Returns class column data
	public ArrayList<ScoreMatrix> Getscorematrixdata()
	{
		ArrayList<ScoreMatrix> list = new ArrayList<ScoreMatrix>();
		list.clear();

		Cursor cursor;
		String selectQuery = "select * from tblscorematrix";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					ScoreMatrix sm = new ScoreMatrix();

					sm.setId(cursor.getString(cursor.getColumnIndex("matrixid")));
					sm.setMatrix_title(cursor.getString(cursor.getColumnIndex("matrixtitle")));
					sm.setMatrix_description(cursor.getString(cursor.getColumnIndex("matrixdescription")));
					sm.setStart_range(cursor.getString(cursor.getColumnIndex("startrange")));
					sm.setEnd_range(cursor.getString(cursor.getColumnIndex("endrange")));
					sm.setCreated_date(cursor.getString(cursor.getColumnIndex("createddate")));
					sm.setModified_date(cursor.getString(cursor.getColumnIndex("modifieddate")));
					sm.setIs_deleted(cursor.getString(cursor.getColumnIndex("isdeleted")));

					list.add(sm);
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return list;
	}

	// This Method Returns class column data
	public ArrayList<String> GetQuestions(String quetype)
	{
		ArrayList<String> list = new ArrayList<>();
		list.clear();

		Cursor cursor;
		String selectQuery = "select questiontitle from tblquestion where questiontype="+ "\'" + quetype + "\'";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					list.add(cursor.getString(0));
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return list;
	}

	// This Method Returns class column data
	public ArrayList<String> GetAllVendors()
	{
		ArrayList<String> list = new ArrayList<>();
		list.clear();

		//select vendorname from tblvendors where vendorname != Other
		Cursor cursor;
		String selectQuery = "select vendorname from tblvendors where vendorname != 'Other'";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					list.add(cursor.getString(0));
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return list;
	}

    // This Method Returns class column data
    public ArrayList<String> GetAllRates()
    {
        ArrayList<String> list = new ArrayList<>();
        list.clear();

        Cursor cursor;
        String selectQuery = "select ratename from tblrates";

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    list.add(cursor.getString(0));
                }

                cursor.close();
            }
        }
        catch (Exception e)
        {
            Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
        }
        return list;
    }

    // This Method Returns class column data
    public ArrayList<String> GetAllRatings()
    {
        ArrayList<String> list = new ArrayList<>();
        list.clear();

        //select vendorname from tblvendors where vendorname != Other
        Cursor cursor;
        String selectQuery = "select vendorname from tblvendors where vendorname != 'Other'";

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    list.add(cursor.getString(0));
                }

                cursor.close();
            }
        }
        catch (Exception e)
        {
            Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
        }
        return list;
    }

	// This Method Returns class column data
	public ArrayList<String> GetAllCountries()
	{
		ArrayList<String> list = new ArrayList<>();
		list.clear();

		Cursor cursor;
		String selectQuery = "select shortname from tblcountrycode";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					list.add(cursor.getString(0));
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return list;
	}


	public String getCountrycode(String countryname)
	{
		Cursor cursor;
		String selectQuery = "select callingcode from tblcountrycode where shortname=" + "\'" + countryname + "\'";

		String countrycode = null;
		try {
			cursor = myDataBase.rawQuery(selectQuery, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					countrycode = cursor.getString(0);
				}

				cursor.close();
			}

		} catch (Exception e) {
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return countrycode;
	}

	public ArrayList<Questions> GetAllQuestions(String quetype)
	{

		ArrayList<Questions> list = new ArrayList<>();
		list.clear();

		Cursor cursor;
		String selectQuery = "select * from tblquestion where questiontype="+ "\'" + quetype + "\'";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					Questions qq = new Questions();

					qq.setId(cursor.getString(cursor.getColumnIndex("questionid")));
					qq.setQuestion_title(cursor.getString(cursor.getColumnIndex("questiontitle")));
					qq.setQuestion_description(cursor.getString(cursor.getColumnIndex("questiondescription")));
					qq.setQuestion_type(cursor.getString(cursor.getColumnIndex("questiontype")));
					qq.setQuestion_format(cursor.getString(cursor.getColumnIndex("questionformat")));
					qq.setOption_count(cursor.getString(cursor.getColumnIndex("optioncount")));
					qq.setCreated_date(cursor.getString(cursor.getColumnIndex("createddate")));
					qq.setModified_date(cursor.getString(cursor.getColumnIndex("modifieddate")));
					qq.setIs_deleted(cursor.getString(cursor.getColumnIndex("isdeleted")));

					list.add(qq);
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return list;
	}

	public String getscoreid(double score)
	{
		Cursor cursor;
		String selectQuery = "select matrixid from tblscorematrix where " + score + ">= startrange and " + score + "< endrange ";

		String scoreid = null;

		try {
			cursor = myDataBase.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					scoreid = cursor.getString(0);
				}

				cursor.close();
			}

		} catch (Exception e) {
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}

		return scoreid;
	}

	public ArrayList<OrganizationType> Getorganizationdata(String parentid)
	{
		ArrayList<OrganizationType> list = new ArrayList<>();
		list.clear();

		Cursor cursor;
		String selectQuery = "select * from tblorganizationtype where parentid="+ "\'" + parentid + "\'";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					OrganizationType ot = new OrganizationType();

					ot.setId(cursor.getString(cursor.getColumnIndex("organizationtypeid")));
					ot.setOrganization_type_name(cursor.getString(cursor.getColumnIndex("organizationtypename")));
					ot.setLevel(cursor.getString(cursor.getColumnIndex("level")));
					ot.setParent_id(cursor.getString(cursor.getColumnIndex("parentid")));
					ot.setIs_optional(cursor.getString(cursor.getColumnIndex("isoptional")));
					ot.setCreated_date(cursor.getString(cursor.getColumnIndex("createddate")));
					ot.setModified_date(cursor.getString(cursor.getColumnIndex("modifieddate")));
					ot.setIs_deleted(cursor.getString(cursor.getColumnIndex("isdeleted")));

					list.add(ot);
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return list;
	}

    public OrganizationType GetParent(String parentid)
    {
        OrganizationType ot = new OrganizationType();

        Cursor cursor;
        String selectQuery = "select * from tblorganizationtype where organizationtypeid="+ "\'" + parentid + "\'";

        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    ot.setId(cursor.getString(cursor.getColumnIndex("organizationtypeid")));
                    ot.setOrganization_type_name(cursor.getString(cursor.getColumnIndex("organizationtypename")));
                    ot.setLevel(cursor.getString(cursor.getColumnIndex("level")));
                    ot.setParent_id(cursor.getString(cursor.getColumnIndex("parentid")));
                    ot.setIs_optional(cursor.getString(cursor.getColumnIndex("isoptional")));
                    ot.setCreated_date(cursor.getString(cursor.getColumnIndex("createddate")));
                    ot.setModified_date(cursor.getString(cursor.getColumnIndex("modifieddate")));
                    ot.setIs_deleted(cursor.getString(cursor.getColumnIndex("isdeleted")));
                }

                cursor.close();
            }
        }
        catch (Exception e)
        {
            Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
        }
        return ot;
    }

	public int getorgsubchid(String parentid)
	{
		Cursor cursor;
		String selectQuery = "select count(*) from tblorganizationtype where parentid="+ "\'" + parentid + "\'";

		int childid = 0;

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					childid = Integer.parseInt(cursor.getString(0));
				}
				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return childid;
	}

	public OrganizationType GetOrganizationtypename(String name)
	{
        OrganizationType ot = new OrganizationType();

		Cursor cursor;
		String selectQuery = "select * from tblorganizationtype where organizationtypename="+ "\'" + name + "\'";

		try
		{
			SQLiteDatabase db = this.getWritableDatabase();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					ot.setId(cursor.getString(cursor.getColumnIndex("organizationtypeid")));
					ot.setOrganization_type_name(cursor.getString(cursor.getColumnIndex("organizationtypename")));
					ot.setLevel(cursor.getString(cursor.getColumnIndex("level")));
					ot.setParent_id(cursor.getString(cursor.getColumnIndex("parentid")));
					ot.setIs_optional(cursor.getString(cursor.getColumnIndex("isoptional")));
					ot.setCreated_date(cursor.getString(cursor.getColumnIndex("createddate")));
					ot.setModified_date(cursor.getString(cursor.getColumnIndex("modifieddate")));
					ot.setIs_deleted(cursor.getString(cursor.getColumnIndex("isdeleted")));
				}

				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return ot;
	}

	public String Getsurveytypeparentid(String surveytypename)
	{
		//select * from tblsurveytype where surveytypename = 'Analytics Software' and level = 1 and parentid = 3
		Cursor cursor;
		String selectQuery = "select surveytypeid from tblsurveytype where surveytypename="+ "\'" + surveytypename + "\'"+" and level = 0";

		String typename = null;
		try {
			cursor = myDataBase.rawQuery(selectQuery, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					typename = cursor.getString(0);
				}

				cursor.close();
			}

		} catch (Exception e) {
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return typename;
	}

	public String Getsurveytychildid(String surveytypename,String parentid)
	{
		//select * from tblsurveytype where surveytypename = 'Analytics Software' and level = 1 and parentid = 3
		Cursor cursor;
		String selectQuery = "select surveytypeid from tblsurveytype where surveytypename="+ "\'" + surveytypename + "\'"+" and level = 1 and " +
				"parentid="+ "\'" + parentid + "\'";

		String typename = null;
		try {
			cursor = myDataBase.rawQuery(selectQuery, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					typename = cursor.getString(0);
				}

				cursor.close();
			}

		} catch (Exception e) {
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return typename;
	}

	public String Getvendorid(String vendorname)
	{
		Cursor cursor;
		String selectQuery = "select vendorid from tblvendors where vendorname="+ "\'" + vendorname + "\'";

		String vid = null;
		try {
			cursor = myDataBase.rawQuery(selectQuery, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					vid = cursor.getString(0);
				}

				cursor.close();
			}

		} catch (Exception e) {
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return vid;
	}

	public String Getroleparentid(String rolename)
	{
		Cursor cursor;
		String selectQuery = "select roleid from tblroles where rolename="+ "\'" + rolename + "\'"+" and level = 0";

		String typename = null;
		try {
			cursor = myDataBase.rawQuery(selectQuery, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					typename = cursor.getString(0);
				}

				cursor.close();
			}

		} catch (Exception e) {
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return typename;
	}

	public String Getrolechildid(String rolename,String parentid)
	{
		Cursor cursor;
		String selectQuery = "select roleid from tblroles where rolename="+ "\'" + rolename + "\'"+" and level = 1 and " +
				"parentid="+ "\'" + parentid + "\'";

		String typename = null;
		try {
			cursor = myDataBase.rawQuery(selectQuery, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					typename = cursor.getString(0);
				}

				cursor.close();
			}

		} catch (Exception e) {
			Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
		}
		return typename;
	}


    public String Getrateid(String stringPreference) {
        Cursor cursor;
        String selectQuery = "select rateid from tblrates where ratename="+ "\'" + stringPreference + "\'";

        String rid = null;
        try {
            cursor = myDataBase.rawQuery(selectQuery, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    rid = cursor.getString(0);
                }

                cursor.close();
            }

        } catch (Exception e) {
            Log.i(Utils.TAG, "getPostDataList Error : " + e.fillInStackTrace());
        }
        return rid;

    }
}
