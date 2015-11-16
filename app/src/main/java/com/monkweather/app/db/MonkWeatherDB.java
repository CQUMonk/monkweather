package com.monkweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.monkweather.app.model.City;
import com.monkweather.app.model.County;
import com.monkweather.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CQUMonk on 2015/11/16.
 */
public class MonkWeatherDB {
    public static final String DB_NAME="monk_weather";
    public static final int VERSION=1;
    private static MonkWeatherDB _monkWeatherDB;
    private SQLiteDatabase db;

    private MonkWeatherDB(Context context){
        MonkWeatherOpenHelper dbOpenHelper=new MonkWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db=dbOpenHelper.getWritableDatabase();
    }

    public static MonkWeatherDB getInstance(Context context){
        if (_monkWeatherDB==null){
            synchronized (MonkWeatherDB.class){
                if (_monkWeatherDB==null){
                    _monkWeatherDB=new MonkWeatherDB(context);
                }
            }
        }
        return _monkWeatherDB;

    }

    public void saveProvince(Province province){
        if (province!=null){
            ContentValues contentValues=new ContentValues(2);
            contentValues.put("province_name",province.getProvinceName());
            contentValues.put("province_code",province.getProvinceCode());
            db.insert("Province",null,contentValues);

        }
    }
    public void saveCity(City city){
        if (city!=null){
            ContentValues values=new ContentValues(3);
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City",null,values);
        }
    }
    public void saveCounty(County county){
        if (county!=null){
            ContentValues values=new ContentValues(3);
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
        }
    }
    public List<Province> loadProvinces(){
        List<Province> provinces=new ArrayList<Province>();
        Cursor cursor=db.query("Province",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return provinces;
    }
    public List<City> loadCities(int provinceId){
        List<City> cityList=new ArrayList<City>();
        Cursor cursor=db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
        if (cursor.moveToFirst()){
            do {
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);

                cityList.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return cityList;
    }
    public List<County> loadCounties(int cityId){
        List<County> countyList=new ArrayList<County>();
        Cursor cursor=db.query("County",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
        if (cursor.moveToFirst()){
            do {
                County county=new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                countyList.add(county);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return countyList;
    }


}
