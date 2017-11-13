package nagaiko.track_alcohol;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by Konstantin on 11.11.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public final static String DB_NAME = "cocktails";
    public final static int DB_VER = 2;
    private AssetManager assetManager;
    String[] column = new String[]{"cocktail_id", "name", "categoryName", "iba", "alcoholic", "glass", "instruction", "thumb"};
    private final static String INGREDIENTS = "CREATE TABLE ingredientS (ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT);";
    private final static String CATEGORIES = "CREATE TABLE categories (category_id INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT);";
    private final static String COCKTAILS = "CREATE TABLE cocktails (cocktail_id INTEGER PRIMARY KEY, name TEXT, categoryName TEXT, iba TEXT, " +
            "alcoholic TEXT, glass TEXT, instruction TEXT, thumb TEXT);";
    private final static String INGREDIENTS_IN_COCKTAILS = "CREATE TABLE ingr_cocktails (cocktail_id INTEGER, ingredient_id INTEGER, " +
            "PRIMARY KEY (cocktail_id, ingredient_id), \n" +
            "FOREIGN KEY (cocktail_id) REFERENCES cocktails (cocktails_id) ON DELETE CASCADE ON UPDATE NO ACTION, \n" +
            "FOREIGN KEY (ingredient_id) REFERENCES ingredients (ingredient_id) ON DELETE CASCADE ON UPDATE NO ACTION);";

    public final String LOG_TAG = this.getClass().getSimpleName();


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        assetManager = context.getAssets();
    }

    private static final class Factory implements SQLiteDatabase.CursorFactory {
        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            return new SQLiteCursor(sqLiteCursorDriver, s, sqLiteQuery);
        }
    }

    public DBHelper(Context context){
        this(context, DB_NAME, new Factory(), DB_VER);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CATEGORIES);
        db.execSQL(INGREDIENTS);
        db.execSQL(COCKTAILS);
        db.execSQL(INGREDIENTS_IN_COCKTAILS);
        List<String> categories = new ArrayList<String>();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(assetManager.open("table.json"), "UTF-8"))){
            StringBuffer bfr = new StringBuffer();
            String str;

            while ((str=in.readLine()) != null) {
                bfr.append(str);
            }
            JSONArray jsonArray = new JSONArray(bfr.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                categories.add(jsonObject.getString("strCategory"));
            }

        } catch (IOException e) {
            Log.d(LOG_TAG,e.getMessage());
        } catch (JSONException e) {
            Log.d(LOG_TAG,e.getMessage());
        }

        for (String category:categories){
            ContentValues v = new ContentValues();
            v.put("category", category);
            db.insert("categories", null, v);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addCategory(String name){
        SQLiteDatabase db = getWritableDatabase();
        if (db != null){
            ContentValues v = new ContentValues();
            v.put("category", name);
            db.insert("categories", null, v);
            db.close();
        }
    }

    public String getCategory(int pos){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cr = null;
        String category = null;
        if (db != null){
            try {
                cr = db.query("categories", new String[]{"category"}, "category_id=?" , new String[]{String.valueOf(pos)}, null, null, null);
                cr.moveToFirst();
                category = cr.getString(cr.getColumnIndexOrThrow("category"));
            } catch (Exception e){
                Log.d(LOG_TAG,e.getMessage());
            } finally {
                if(cr != null) cr.close();
            }
            db.close();

        }

        return category;
    }

    public void addOrUpdateCocktail(Cocktail cocktail){
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            Log.d(LOG_TAG, "addOrUpdateCocktail_Begin");
            ContentValues values = new ContentValues();
            values.put("cocktail_id", cocktail.getId());
            values.put("name", cocktail.getName());
            values.put("categoryName", cocktail.getCategoryName());
            values.put("iba", cocktail.getIBA());
            values.put("alcoholic", cocktail.getAlcoholic());
            values.put("glass", cocktail.getGlass());
            values.put("instruction", cocktail.getInstruction());
            values.put("thumb", cocktail.getThumb());

            long conflict =  db.insertWithOnConflict("cocktails", null, values, SQLiteDatabase.CONFLICT_IGNORE);
            if (conflict == -1){
                Log.d(LOG_TAG, "addOrUpdateCocktail_Conflict");
                long res = db.update("cocktails", values, "cocktail_id=?", new String[]{String.valueOf(cocktail.getId())});
                Log.d(LOG_TAG, "response_msg" + res);
            }
            db.close();
        }
        Log.d(LOG_TAG, "addOrUpdateCocktail_End");
    }

    public Cocktail getCocktailById(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cr = null;
        Cocktail cocktail = null;
        if(db != null){
            try {
                cr = db.query("cocktails", column, "cocktail_id=?" , new String[]{String.valueOf(id)}, null, null, null);
                cocktail = cocktailsFromCursor(cr).get(0);
            } catch (Exception e){
                Log.d(LOG_TAG,e.getMessage());
            }finally {
                if (cr != null) cr.close();
            }
            db.close();
        }
        return cocktail;
    }

    public ArrayList<Cocktail> getCocktailsByCategory(String category){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cr = null;
        ArrayList<Cocktail> cocktails = null;
        if(db != null){
            try {
                cr = db.query("cocktails", column, "categoryName=?" , new String[]{category}, null, null, null);
                cocktails = cocktailsFromCursor(cr);
            } catch (Exception e){
                Log.d(LOG_TAG,e.getMessage());
            }finally {
                if (cr != null) cr.close();
            }
            db.close();
        }
        return cocktails;
    }

    private ArrayList<Cocktail> cocktailsFromCursor(Cursor cursor) throws Exception{
        ArrayList<Cocktail> cocktails = new ArrayList<>();
        cursor.moveToFirst();
        Cocktail cocktail;
        while (!cursor.isAfterLast()){
            cocktail = new Cocktail();

            cocktail.setId(cursor.getInt(cursor.getColumnIndexOrThrow("cocktail_id")));
            cocktail.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            cocktail.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow("categoryName")));
            cocktail.setIBA(cursor.getString(cursor.getColumnIndexOrThrow("iba")));
            cocktail.setAlcoholic(cursor.getString(cursor.getColumnIndexOrThrow("alcoholic")));
            cocktail.setGlass(cursor.getString(cursor.getColumnIndexOrThrow("glass")));
            cocktail.setInstruction(cursor.getString(cursor.getColumnIndexOrThrow("instruction")));
            cocktail.setThumb(cursor.getString(cursor.getColumnIndexOrThrow("thumb")));

            cocktails.add(cocktail);
            cursor.moveToNext();
        }
        return cocktails;
    }

}
