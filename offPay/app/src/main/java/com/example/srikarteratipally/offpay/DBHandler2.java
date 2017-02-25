package com.example.srikarteratipally.offpay;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import java.util.ArrayList;
        import java.util.List;

public class DBHandler2 extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "RegistrationTableInfo";
    // Contacts table name
    private static final String TABLE_TRANSACTIONS = "RegistrationTables";
    // RegistrationTables Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "Amount";
    private static final String KEY_USERMOBILE = "UserMobile";
    private static final String KEY_PIN= "PIN";

    public DBHandler2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +KEY_AMOUNT + " TEXT," + KEY_USERMOBILE + " TEXT,"
                + KEY_PIN + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
// Creating tables again
        onCreate(db);
    }
    // Adding new RegistrationTable
    public void addRegistrationTable(String a,String b,String c) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT,c);
        values.put(KEY_USERMOBILE, a); // RegistrationTable Name
        values.put(KEY_PIN, b); // RegistrationTable Phone Number
// Inserting Row
        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close(); // Closing database connection
    }
    // Getting one RegistrationTable
    public RegistrationTable getRegistrationTable(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRANSACTIONS, new String[]{KEY_ID,
                        KEY_AMOUNT,KEY_USERMOBILE, KEY_PIN}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RegistrationTable contact = new RegistrationTable(Integer.parseInt(cursor.getString(0))
                , cursor.getString(1),cursor.getString(2),cursor.getString(3));
// return RegistrationTable
        return contact;
    }
    // Getting All RegistrationTables
    public List<RegistrationTable> getAllRegistrationTables() {
        List<RegistrationTable> RegistrationTableList = new ArrayList<RegistrationTable>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RegistrationTable RegistrationTable = new RegistrationTable();
                RegistrationTable.setId(Integer.parseInt(cursor.getString(0)));
                RegistrationTable.setAmount(cursor.getString(1));
                RegistrationTable.setUserMobile(cursor.getString(2));
                RegistrationTable.setPIN(cursor.getString(3));
// Adding contact to list
                RegistrationTableList.add(RegistrationTable);
            } while (cursor.moveToNext());
        }

// return contact list
        return RegistrationTableList;
    }
    // Getting RegistrationTables Count
    public int getRegistrationTablesCount() {
        String countQuery = "SELECT * FROM " + TABLE_TRANSACTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

// return count
        return cursor.getCount();
    }
    // Updating a RegistrationTable
    public int updateRegistrationTable(RegistrationTable RegistrationTable) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT,RegistrationTable.getAmount());
        values.put(KEY_USERMOBILE, RegistrationTable.getUserMobile());
        values.put(KEY_PIN, RegistrationTable.getPIN());

// updating row
        return db.update(TABLE_TRANSACTIONS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(RegistrationTable.getId())});
    }

    // Deleting a RegistrationTable
    public void deleteRegistrationTable(RegistrationTable RegistrationTable) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(RegistrationTable.getId()) });
        db.close();
    }
    public void clearTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS,null,null);
        db.close();
    }
}


