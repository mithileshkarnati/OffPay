package com.example.srikarteratipally.offpay;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import java.util.ArrayList;
        import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TransactionInfo";
    // Contacts table name
    private static final String TABLE_TRANSACTIONS = "Transactions";
    // Transactions Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "Amount";
    private static final String KEY_FROMUSER = "FromUser";
    private static final String KEY_TOUSER= "ToUser";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +KEY_AMOUNT + " TEXT," + KEY_FROMUSER + " TEXT,"
                + KEY_TOUSER + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
// Creating tables again
        onCreate(db);
    }
    // Adding new Transaction
    public void addTransaction(String a,String b,String c) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT,a);
        values.put(KEY_FROMUSER,b); // Transaction Name
        values.put(KEY_TOUSER,c); // Transaction Phone Number
// Inserting Row
        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close(); // Closing database connection
    }
    // Getting one Transaction
    public Transaction getTransaction(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRANSACTIONS, new String[]{KEY_ID,
                        KEY_AMOUNT,KEY_FROMUSER, KEY_TOUSER}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Transaction contact = new Transaction(Integer.parseInt(cursor.getString(0))
                , cursor.getString(1),cursor.getString(2),cursor.getString(3));
// return Transaction
        return contact;
    }
    // Getting All Transactions
    public List<Transaction> getAllTransactions() {
        List<Transaction> TransactionList = new ArrayList<Transaction>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction Transaction = new Transaction();
                Transaction.setId(Integer.parseInt(cursor.getString(0)));
                Transaction.setAmount(cursor.getString(1));
                Transaction.setFromUser(cursor.getString(2));
                Transaction.setToUser(cursor.getString(3));
// Adding contact to list
                TransactionList.add(Transaction);
            } while (cursor.moveToNext());
        }

// return contact list
        return TransactionList;
    }
    // Getting Transactions Count
    public int getTransactionsCount() {
        String countQuery = "SELECT * FROM " + TABLE_TRANSACTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

// return count
        return cursor.getCount();
    }
    // Updating a Transaction
    public int updateTransaction(Transaction Transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT,Transaction.getAmount());
        values.put(KEY_FROMUSER, Transaction.getFromUser());
        values.put(KEY_TOUSER, Transaction.getToUser());

// updating row
        return db.update(TABLE_TRANSACTIONS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(Transaction.getId())});
    }

    // Deleting a Transaction
    public void deleteTransaction(Transaction Transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(Transaction.getId()) });
        db.close();
    }
    public void clearTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS,null,null);
        db.close();
    }
}

