package mandrik.nick.lab_46.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mandrik.nick.lab_46.R;
import mandrik.nick.lab_46.db.data.InitialData;

public class DBHelper extends SQLiteOpenHelper implements InitialData {
    public static final int DATABASE_VERSION = 4;
    private Context context;

    private final String[] DATA_REGION_NAMES;
    private final String[] DATA_DISTRICT_NAMES;
    private final int[] DATA_DISTRICT_POPULATION;
    private final int[] DATA_DISTRICT_CODES;
    private final int[] DATA_DISTRICT_REG_IDS;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
        DATA_REGION_NAMES = this.context.getResources().getStringArray(R.array.regions);
        DATA_DISTRICT_NAMES = this.context.getResources().getStringArray(R.array.districts);
        DATA_DISTRICT_POPULATION = this.context.getResources().getIntArray(R.array.districts_population);
        DATA_DISTRICT_CODES = this.context.getResources().getIntArray(R.array.district_codes);
        DATA_DISTRICT_REG_IDS = this.context.getResources().getIntArray(R.array.districts_reg_id);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DB CREATE", SQL_CREATE_REGIONS);
        db.execSQL(SQL_CREATE_REGIONS);
        db.execSQL(SQL_CREATE_DISTRICT);
        initRegions(db);
        initDistricts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL(SQL_DELETE_DISTRICTS);
            db.execSQL(SQL_DELETE_REGION);
            onCreate(db);
        }
    }

    private void insertRegion(String regionName, int id) {
        ContentValues values = new ContentValues();
        values.put(REGION_REG_NAME, regionName);
        values.put(REGION_ID, id);
        getWritableDatabase().insert(REGION_TBL, null, values);
    }

    private void initRegions(SQLiteDatabase db) {
        StringBuilder strb = new StringBuilder();
        String start = "INSERT INTO " + REGION_TBL + " VALUES(";
        for (int i = 0; i < DATA_REGION_NAMES.length; i++) {
            String command = start + i + ", '" + DATA_REGION_NAMES[i] + "');";
            db.execSQL(command);
        }

    }

    private void initDistricts(SQLiteDatabase db) {
        String startFormat = "INSERT INTO {0} ({1},{2},{3},{4}) VALUES (";
        String start = MessageFormat.format(startFormat, DISTRICT_TBL, DISTRICT_D_NAME,
                DISTRICT_D_POPULATION, DISTRICT_REGION_ID, DISTRICT_CODE);
        for (int i = 0; i < DATA_DISTRICT_NAMES.length; i++) {
            String name = DATA_DISTRICT_NAMES[i];
            int population = DATA_DISTRICT_POPULATION[i];
            int code = DATA_DISTRICT_CODES[i];
            int regId = DATA_DISTRICT_REG_IDS[i];
            String command = start + "'" + name + "', " + population + ", " + regId + ", " + code + ");";
            db.execSQL(command);
        }
    }

    private void insertDistrict(District d) {
        ContentValues districtValues = new ContentValues();
        districtValues.put(DISTRICT_D_NAME, d.name);
        districtValues.put(DISTRICT_CODE, d.code);
        districtValues.put(DISTRICT_D_POPULATION, d.population);
        districtValues.put(DISTRICT_REGION_ID, d.regionId);
        getWritableDatabase().insert(DISTRICT_TBL, null, districtValues);
    }

    private static final String[] DISTRICT_COLUMNS = {
            DISTRICT_D_NAME, DISTRICT_CODE, DISTRICT_D_POPULATION, DISTRICT_REGION_ID
    };

    public List<District> readAllDistricts() {
        return queryDistricts(null, null, null, null);
    }

    public List<District> readAllAndSort(String sortBy) {
        return queryDistricts(null, null, sortBy, null);
    }

    private List<District> queryDistricts(String selection, String groupBy, String orderBy, String having, String... args) {
        List<District> result = new LinkedList<>();
        Cursor c = getReadableDatabase().query(DISTRICT_TBL, DISTRICT_COLUMNS,
                selection, args, groupBy, having, orderBy);
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(DISTRICT_D_NAME));
            int code = c.getInt(c.getColumnIndex(DISTRICT_CODE));
            int regId = c.getInt(c.getColumnIndex(DISTRICT_REGION_ID));
            int population = c.getInt(c.getColumnIndex(DISTRICT_D_POPULATION));
            result.add(new District(name, population, regId, code));
        }
        c.close();
        return result;
    }

    public String regionNameById(int id) {
        String result = "";
        Cursor c = getReadableDatabase().query(REGION_TBL, new String[]{REGION_REG_NAME},
                REGION_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        while (c.moveToNext()) {
            result = c.getString(c.getColumnIndex(REGION_REG_NAME));
        }
        return result;
    }

    public District findMinPopulation() {
        District result = null;
        Cursor c = getReadableDatabase().rawQuery("select * from " + DISTRICT_TBL + " where "
                + DISTRICT_D_POPULATION + " = (select min(" + DISTRICT_D_POPULATION + ") from " + DISTRICT_TBL + ");", null);
        if (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(DISTRICT_D_NAME));
            int code = c.getInt(c.getColumnIndex(DISTRICT_CODE));
            int regId = c.getInt(c.getColumnIndex(DISTRICT_REGION_ID));
            int population = c.getInt(c.getColumnIndex(DISTRICT_D_POPULATION));
            result = new District(name, population, regId, code);
        }
        c.close();
        return result;
    }

    public District findMaxPopulation() {
        District result = null;
        Cursor c = getReadableDatabase().rawQuery("select * from " + DISTRICT_TBL + " where "
                + DISTRICT_D_POPULATION + " = (select max(" + DISTRICT_D_POPULATION + ") from " + DISTRICT_TBL + ");", null);
        if (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(DISTRICT_D_NAME));
            int code = c.getInt(c.getColumnIndex(DISTRICT_CODE));
            int regId = c.getInt(c.getColumnIndex(DISTRICT_REGION_ID));
            int population = c.getInt(c.getColumnIndex(DISTRICT_D_POPULATION));
            result = new District(name, population, regId, code);
        }
        c.close();
        return result;
    }

    public int findSumPopulation() {
        Cursor c = getReadableDatabase().rawQuery("select sum(" + DISTRICT_D_POPULATION + ") as sum from " + DISTRICT_TBL + ";", null);
        int value = -1;
        if ( c.moveToNext()){
            value = c.getInt(c.getColumnIndex("sum"));
        }
        c.close();
        return value;
    }

    public List<District> findMoreThatMinPopulationAndRegion(int min, int region) {
        Cursor c = getReadableDatabase().rawQuery("select * from " + DISTRICT_TBL + " where "
                + DISTRICT_D_POPULATION + " > " + min + " AND " + DISTRICT_REGION_ID + " = " + region + ";", null);
        List<District> result = new ArrayList<>();
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(DISTRICT_D_NAME));
            int code = c.getInt(c.getColumnIndex(DISTRICT_CODE));
            int regId = c.getInt(c.getColumnIndex(DISTRICT_REGION_ID));
            int population = c.getInt(c.getColumnIndex(DISTRICT_D_POPULATION));
            result.add(new District(name, population, regId, code));
        }
        c.close();
        return result;
    }

    public List<District> findMoreThatMinPopulation(int min) {
        Cursor c = getReadableDatabase().rawQuery("select * from " + DISTRICT_TBL + " where "
                + DISTRICT_D_POPULATION + " > " + min + " ;", null);
        List<District> result = new ArrayList<>();
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(DISTRICT_D_NAME));
            int code = c.getInt(c.getColumnIndex(DISTRICT_CODE));
            int regId = c.getInt(c.getColumnIndex(DISTRICT_REGION_ID));
            int population = c.getInt(c.getColumnIndex(DISTRICT_D_POPULATION));
            result.add(new District(name, population, regId, code));
        }
        c.close();
        return result;
    }

    public List<District> findWhereDistrict(int region) {
        Cursor c = getReadableDatabase().rawQuery("select * from " + DISTRICT_TBL + " where "
                + DISTRICT_REGION_ID + " = " + region + " ;", null);
        List<District> result = new ArrayList<>();
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(DISTRICT_D_NAME));
            int code = c.getInt(c.getColumnIndex(DISTRICT_CODE));
            int regId = c.getInt(c.getColumnIndex(DISTRICT_REGION_ID));
            int population = c.getInt(c.getColumnIndex(DISTRICT_D_POPULATION));
            result.add(new District(name, population, regId, code));
        }
        c.close();
        return result;
    }

    public int idByRegionName(String regionName){
        Cursor c = getReadableDatabase().rawQuery("select " +REGION_ID + " as id from " + REGION_TBL + " where "
                + REGION_REG_NAME + " = '" + regionName + "';", null);
        int result = -1;
        if (c.moveToNext()) {
            result = c.getInt(c.getColumnIndex("id"));
        }
        c.close();
        return result;
    }

    public List<String> allRegions(){
        Cursor c = getReadableDatabase().rawQuery("select " + REGION_REG_NAME + " as name from " + REGION_TBL + ";", null);
        List<String> result = new ArrayList<>();
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("name"));
            result.add(name);
        }
        c.close();
        return result;
    }
}
