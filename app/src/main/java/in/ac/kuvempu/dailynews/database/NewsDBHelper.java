package in.ac.kuvempu.dailynews.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import in.ac.kuvempu.dailynews.model.ArticleItem;

/**
 * Created by raghav on 4/10/2017.
 */

public class NewsDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NEWSDB.db";
    public static final String NEWS_TABLE_NAME = "NEWS";
    public static final String NEWS_COLUMN_ID = "ID";
    public static final String NEWS_COLUMN_AUTHOR = "AUTHOR";
    public static final String NEWS_COLUMN_TITLE = "TITLE";
    public static final String NEWS_COLUMN_DESC = "DESC";
    public static final String NEWS_COLUMN_URL = "URL";
    public static final String NEWS_COLUMN_IMG = "IMG";
    public static final String NEWS_COLUMN_PUB_AT = "PUBLISHED_AT";
    public static final String NEWS_COLUMN_SELF = "SELF";
    public static final String NEWS_COLUMN_CATEGORY = "CATEGORY";
    public static final String NEWS_COLUMN_PH_NO = "PHONE_NO";

    private HashMap hp;

    public NewsDBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + NEWS_TABLE_NAME +
                 "(" + NEWS_COLUMN_ID + " TEXT PRIMARY KEY, " + NEWS_COLUMN_AUTHOR + " TEXT, " +
                NEWS_COLUMN_TITLE + " TEXT, " + NEWS_COLUMN_DESC + " TEXT, " + NEWS_COLUMN_URL + " TEXT, " +
                NEWS_COLUMN_IMG + " TEXT, " + NEWS_COLUMN_PUB_AT + " TEXT, " + NEWS_COLUMN_SELF + " TEXT," +
                        NEWS_COLUMN_CATEGORY + " TEXT, " +  NEWS_COLUMN_PH_NO + " TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNews(ArticleItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NEWS_COLUMN_ID, UUID.randomUUID().toString());
        contentValues.put(NEWS_COLUMN_AUTHOR, (String) item.getAuthor());
        contentValues.put(NEWS_COLUMN_TITLE, item.getTitle());
        contentValues.put(NEWS_COLUMN_DESC, item.getDescription());
        contentValues.put(NEWS_COLUMN_URL, item.getUrl());
        contentValues.put(NEWS_COLUMN_IMG, item.getUrlToImage());
        contentValues.put(NEWS_COLUMN_PUB_AT, item.getPublishedAt());
        contentValues.put(NEWS_COLUMN_SELF, item.getSelf());
        contentValues.put(NEWS_COLUMN_CATEGORY, item.getCategory());
        contentValues.put(NEWS_COLUMN_PH_NO, item.getPhoneNo());

        db.insert(NEWS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertAllNews(List<ArticleItem> items, String category) {

        if(items != null && !items.isEmpty()){
            for (ArticleItem item : items) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                contentValues.put(NEWS_COLUMN_ID, UUID.randomUUID().toString());
                contentValues.put(NEWS_COLUMN_AUTHOR, (String) item.getAuthor());
                contentValues.put(NEWS_COLUMN_TITLE, item.getTitle());
                contentValues.put(NEWS_COLUMN_DESC, item.getDescription());
                contentValues.put(NEWS_COLUMN_URL, item.getUrl());
                contentValues.put(NEWS_COLUMN_IMG, item.getUrlToImage());
                contentValues.put(NEWS_COLUMN_PUB_AT, item.getPublishedAt());
                contentValues.put(NEWS_COLUMN_SELF, item.getSelf());
                contentValues.put(NEWS_COLUMN_CATEGORY, category);
                contentValues.put(NEWS_COLUMN_PH_NO, item.getPhoneNo());

                db.insert(NEWS_TABLE_NAME, null, contentValues);
            }
            return true;
        }

        return true;
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + NEWS_TABLE_NAME  +" where "+ NEWS_COLUMN_ID +" ='" + id + "'", null );
        return res;
    }

    public Integer deleteNews (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NEWS_TABLE_NAME,
                NEWS_COLUMN_ID+ "= ? ",
                new String[] { id });
    }

    public Integer deleteAllNews (String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NEWS_TABLE_NAME,NEWS_COLUMN_CATEGORY + "='"+  category +"'", null);
    }

    public ArrayList<ArticleItem> getAllNews() {
        ArrayList<ArticleItem> array_list = new ArrayList<ArticleItem>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + NEWS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ArticleItem item = new ArticleItem();
            item.setId(res.getString(res.getColumnIndex(NEWS_COLUMN_ID)));
            item.setAuthor(res.getString(res.getColumnIndex(NEWS_COLUMN_AUTHOR)));
            item.setTitle(res.getString(res.getColumnIndex(NEWS_COLUMN_TITLE)));
            item.setDescription(res.getString(res.getColumnIndex(NEWS_COLUMN_DESC)));
            item.setUrl(res.getString(res.getColumnIndex(NEWS_COLUMN_URL)));
            item.setUrlToImage(res.getString(res.getColumnIndex(NEWS_COLUMN_IMG)));
            item.setPublishedAt(res.getString(res.getColumnIndex(NEWS_COLUMN_PUB_AT)));
            item.setSelf(res.getString(res.getColumnIndex(NEWS_COLUMN_SELF)));
            item.setCategory(res.getString(res.getColumnIndex(NEWS_COLUMN_CATEGORY)));

            array_list.add(item);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<ArticleItem> getAllNews(String category) {
        ArrayList<ArticleItem> array_list = new ArrayList<ArticleItem>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + NEWS_TABLE_NAME + " WHERE " + NEWS_COLUMN_CATEGORY + "='"+  category +"'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ArticleItem item = new ArticleItem();
            item.setId(res.getString(res.getColumnIndex(NEWS_COLUMN_ID)));
            item.setAuthor(res.getString(res.getColumnIndex(NEWS_COLUMN_AUTHOR)));
            item.setTitle(res.getString(res.getColumnIndex(NEWS_COLUMN_TITLE)));
            item.setDescription(res.getString(res.getColumnIndex(NEWS_COLUMN_DESC)));
            item.setUrl(res.getString(res.getColumnIndex(NEWS_COLUMN_URL)));
            item.setUrlToImage(res.getString(res.getColumnIndex(NEWS_COLUMN_IMG)));
            item.setPublishedAt(res.getString(res.getColumnIndex(NEWS_COLUMN_PUB_AT)));
            item.setSelf(res.getString(res.getColumnIndex(NEWS_COLUMN_SELF)));
            item.setCategory(res.getString(res.getColumnIndex(NEWS_COLUMN_CATEGORY)));
            item.setPhoneNo(res.getString(res.getColumnIndex(NEWS_COLUMN_PH_NO)));

            array_list.add(item);
            res.moveToNext();
        }
        return array_list;
    }
}