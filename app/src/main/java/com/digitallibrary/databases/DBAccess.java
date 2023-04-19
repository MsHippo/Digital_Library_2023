package com.digitallibrary.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import com.digitallibrary.User;
import com.digitallibrary.model.Book;
import com.digitallibrary.model.RecyclerData;

import java.util.ArrayList;
import java.util.List;

public class DBAccess <instance> {
    private final SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DBAccess instance;
    private Context context;

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    // User table name
    private static final String TABLE_USER = "user";

    // Library collection table name
    private static final String TABLE_LIBRARY = "malaysia_library";
    //Library collection column
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_LIBRARY_NAME = "name";
    private static final String COLUMN_Link = "link";

    //Book table name
    private static final String TABLE_BOOK = "books";
    //Book table column
    private static final String COLUMN_BOOK_ID = "book_id";
    private static final String COLUMN_BOOK_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_READING_URL = "reading_url";
    private static final String COLUMN_PAGES = "pages";
    private static final String COLUMN_CATEGORIES = "categories";
    private static final String COLUMN_IMAGES = "images";
    private static final String COLUMN_FAV_STATUS = "fav_status";

    //my_library table name
    private static final String TABLE_MY_LIBRARY= "my_library";

    //my_library table column
    private static final String COLUMN_NOTE_ID = "_id";
    private static final String COLUMN_NOTE_BOOK_TITLE = "book_title";
    private static final String COLUMN_NOTE_BOOK_AUTHOR = "book_author";
    private static final String COLUMN_NOTE_BOOK_PAGES = "book_pages";
    private static final String COLUMN_NOTE_BOOK_YEAR = "book_year";


    public DBAccess(Context context) {
        this.openHelper = new DBHelper(context);
        this.context = context;
    }

    public static DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }


    //This method is to create user record

    public void addUser(User user) {
//        Context context = getApplicationContext();
//        DBHelper helper = new DBHelper(context);
//        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // Inserting Row
        database.insert(TABLE_USER, null, values);
        database.close();
    }
    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = database.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        // return user list
        return userList;
    }
    //This method to update user record

    public void updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // updating row
        database.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    //This method is to delete user record

    public void deleteUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        database.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        database.close();
    }

    //This method to check user exist or not
    public boolean checkUser(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        Cursor cursor = database.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        database.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    //This method to check user exist or not

    public boolean checkUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
//        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions

        Cursor cursor = database.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        database.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public User checkCorrectUser(String email, String password){
//        SQLiteDatabase db = this.getReadableDatabase();
        String queryUser = "Select * from user where user_email = '" + email + "' and user_password = '" + password + "'";
        Cursor cursor = database.rawQuery(queryUser, null);
        User user = new User();
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            int user_id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            String user_name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            String user_email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
            String user_password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD));
            user.setId(user_id);
            user.setUsername(user_name);
            user.setEmail(user_email);
            user.setPassword(user_password);
        }
        cursor.close();
        return user;
    }

    public Cursor getLatestUser(int id){
        Cursor res = database.rawQuery("select * from " + TABLE_USER + " where user_id = " + id, null);
        return res;
    }

    public ArrayList<RecyclerData> getAllImagesData(){
        try
        {
            ArrayList<RecyclerData> libraryDataArrayList = new ArrayList<>();
            Cursor libraryCursor = database.rawQuery("select * from " + TABLE_LIBRARY, null);
            if (libraryCursor.getCount()!=0){
                while (libraryCursor.moveToNext()){
                    String library_name = libraryCursor.getString(libraryCursor.getColumnIndex(COLUMN_LIBRARY_NAME));
                    byte[] imagesBytes = libraryCursor.getBlob(libraryCursor.getColumnIndex(COLUMN_IMAGE));
                    String library_link = libraryCursor.getString(libraryCursor.getColumnIndex(COLUMN_Link));

                    Bitmap libraryBitmap = BitmapFactory.decodeByteArray(imagesBytes, 0, imagesBytes.length);
                    libraryDataArrayList.add(new RecyclerData(library_name, libraryBitmap, library_link));
                }
                return libraryDataArrayList;
            }
            else {
                Log.d("Insertion failed", "No values exists in Database ");

                return null;
            }
        } catch (Exception e){
            Log.d("Insertion failed", "No values exists in Database ");

            return null;
        }
    }

    public ArrayList<Book> getAllBookData(){
        try
        {
            ArrayList<Book> bookDataArrayList = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from " + TABLE_BOOK, null);
            if (cursor.getCount()!=0){
                while (cursor.moveToNext()){
                    int book_id = cursor.getInt(cursor.getColumnIndex(COLUMN_BOOK_ID));
                    String book_title = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_TITLE));
                    String desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                    String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
                    int pages = cursor.getInt(cursor.getColumnIndex(COLUMN_PAGES));
                    String categories = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIES));
                    byte[] imagesBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGES));
                    String url = cursor.getString(cursor.getColumnIndex(COLUMN_READING_URL));
                    String fav_status = cursor.getString(cursor.getColumnIndex(COLUMN_FAV_STATUS));

                    Bitmap libraryBitmap = BitmapFactory.decodeByteArray(imagesBytes, 0, imagesBytes.length);
                    bookDataArrayList.add(new Book(book_id, book_title, desc, author,url, pages,categories,libraryBitmap, fav_status));
                }
                return bookDataArrayList;
            }
            else {
                Log.d("Insertion failed", "No values exists in Database ");

                return null;
            }
        } catch (Exception e){
            Log.d("Insertion failed", "No values exists in Database ");

            return null;
        }
    }
    public Book checkCorrespondingBook(int book_id){
//        SQLiteDatabase db = this.getReadableDatabase();
        String queryBook = "Select * from " + TABLE_BOOK + " where book_id = '" + book_id + "'";
        Cursor cursor = database.rawQuery(queryBook, null);
//        ArrayList<Book> bookDataArrayList = new ArrayList<>();
        Book bookDataArrayList = new Book();
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            int the_book_id = cursor.getInt(cursor.getColumnIndex(COLUMN_BOOK_ID));
            String book_title = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_TITLE));
            String desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
            int pages = cursor.getInt(cursor.getColumnIndex(COLUMN_PAGES));
            String categories = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIES));
            byte[] imagesBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGES));
            String url = cursor.getString(cursor.getColumnIndex(COLUMN_READING_URL));
            String fav_status = cursor.getString(cursor.getColumnIndex(COLUMN_FAV_STATUS));

            Bitmap libraryBitmap = BitmapFactory.decodeByteArray(imagesBytes, 0, imagesBytes.length);

//            bookDataArrayList.add(new Book(the_book_id, book_title, desc, author,url, pages,categories,libraryBitmap));

            bookDataArrayList.setBook_id(the_book_id);
            bookDataArrayList.setTitle(book_title);
            bookDataArrayList.setDescription(desc);
            bookDataArrayList.setAuthor(author);
            bookDataArrayList.setPages(pages);
            bookDataArrayList.setCategories(categories);
            bookDataArrayList.setImgUrl(url);
            bookDataArrayList.setImgid(libraryBitmap);
            bookDataArrayList.setFav_status(fav_status);

        }
        cursor.close();
        return bookDataArrayList;
    }

    //read all data for fav status
    public Cursor read_fav_data(int book_id) {

        String sql = "select * from " + TABLE_BOOK + " where " + COLUMN_BOOK_ID + " = " + book_id;

        return database.rawQuery(sql, null);

    }


    //remove fav by changing the status
    public void update_fav(int book_id){
        String sql = "update " + TABLE_BOOK + " set " + COLUMN_FAV_STATUS + "='1' where " + COLUMN_BOOK_ID + "= " + book_id;
        database.execSQL(sql);
        Log.d("add fav: ", String.valueOf(book_id));

    }

    //remove fav by changing the status
    public void remove_fav(int book_id){
        String sql = "update " + TABLE_BOOK + " set " + COLUMN_FAV_STATUS + "='0' where " + COLUMN_BOOK_ID + "='" + book_id +"'";
        database.execSQL(sql);
        Log.d("remove: ", String.valueOf(book_id));

    }

    //select all favorite list
    public Cursor select_all_favorite_list(){
        String sql = " select * from " + TABLE_BOOK + " where " + COLUMN_FAV_STATUS + "= '1' ";
        return database.rawQuery(sql, null);
    }

    //adding wishing list
    public void addWishingBook(String title, String author, int pages, int year){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTE_BOOK_TITLE, title);
        cv.put(COLUMN_NOTE_BOOK_AUTHOR, author);
        cv.put(COLUMN_NOTE_BOOK_PAGES, pages);
        cv.put(COLUMN_NOTE_BOOK_YEAR, year);
        long result = database.insert(TABLE_MY_LIBRARY,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            Log.d("Insertion failed", "No values inserted in Database ");
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
            Log.d("Insertion success", "Values Added Successfully into Database ");
        }
    }

    public Cursor readAllWishingData(){
        String query = "SELECT * FROM " + TABLE_MY_LIBRARY;
        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateWishingData(String row_id, String title, String author, String pages, String year){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTE_BOOK_TITLE, title);
        cv.put(COLUMN_NOTE_BOOK_AUTHOR, author);
        cv.put(COLUMN_NOTE_BOOK_PAGES, pages);
        cv.put(COLUMN_NOTE_BOOK_YEAR, year);

        long result = database.update(TABLE_MY_LIBRARY, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            Log.d("Update failed", "No values update in Database ");
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
            Log.d("Update success", "Values updated Successfully into Database ");
        }
    }

    public void deleteOneWishingRow(String row_id){
        long result = database.delete(TABLE_MY_LIBRARY, "_id=?", new String[]{row_id});
        if(result == -1){
            Log.d("Deletion failed", "Failed to Delete.");
        }else{
            Log.d("Deletion success", "Successfully Deleted.");
        }
    }

    public void deleteAllWishingData(){
        database.execSQL("DELETE FROM " + TABLE_MY_LIBRARY);
    }
}
