package com.key.doltool.util.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.key.doltool.activity.MyApplication;
import com.key.doltool.data.sqlite.Card;
import com.key.doltool.data.sqlite.Mission;
import com.key.doltool.data.sqlite.Trove;
import com.the9tcat.hadi.ColumnAttribute;
import com.the9tcat.hadi.DatabaseManager;
import com.the9tcat.hadi.DefaultDAO;
import com.the9tcat.hadi.Util;
import com.the9tcat.hadi.annotation.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 用于优化现有数据库ORM[对象关系映射]工具的速度的特别类
 * @version 0.2
 * 0.1-加入批量更新的方法，减少更新所需时间（400条数据约13s）
 * 0.2-优化原查询方法
 * **/
public class SRPUtil {
	private DatabaseManager mDatabaseManager;
	private MyApplication mApplication;
	private static SRPUtil instance;
	private static DefaultDAO dao;
	public static SRPUtil getInstance(Context context){
		if(null == instance){
			instance = new SRPUtil(context);
		}
		return instance;
	}
	public static DefaultDAO getDAO(Context context){
		if(null == dao){
			dao = new DefaultDAO(context);
		}
		return dao;
	}
	
	public SRPUtil(Context context){
		this.mApplication =(MyApplication)context.getApplicationContext();
		this.mDatabaseManager = mApplication.getDataBaseManager();
	}
	
	public long update_Trove(List<Trove> models) {
		long result = 0;
		long tmp_id ;
		synchronized("lock") {
			SQLiteDatabase db = this.mDatabaseManager.open();
			try {
				db.beginTransaction();			
				for(Trove obj:models){
					tmp_id =update(db,obj,new String[]{"flag"},"id=?",new String[]{""+obj.getId()});
					Log.i("tag","tmp_id");
					if(tmp_id>0){
						result ++;			
					}
					db.yieldIfContendedSafely();
				}
				db.setTransactionSuccessful();
			} finally { 
				db.endTransaction();
				this.mDatabaseManager.close();			
			}
		}
		return result;
	}
	public long update_Mission(List<Mission> models) {
		long result = 0;
		long tmp_id;
		synchronized("lock") {
			SQLiteDatabase db = this.mDatabaseManager.open();
			try {				
				db.beginTransaction();			
				for(Mission obj:models){
					tmp_id =update(db, obj, new String[]{"tag"}, "id=?", new String[]{"" + obj.getId()});
					if(tmp_id>0){
						result ++;			
					}
					db.yieldIfContendedSafely();
				}
				db.setTransactionSuccessful();
			} finally { 
				db.endTransaction();
				this.mDatabaseManager.close();			
			}
		}
		return result;
	}

	public long update_Card(List<Card> models) {
		long result = 0;
		long tmp_id;
		synchronized("lock") {
			SQLiteDatabase db = this.mDatabaseManager.open();
			try {
				db.beginTransaction();
				for(Card obj:models){
					tmp_id =update(db,obj,new String[]{"flag"}, "id=?", new String[]{"" + obj.id});
					if(tmp_id>0){
						result ++;
					}
					db.yieldIfContendedSafely();
				}
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
				this.mDatabaseManager.close();
			}
		}
		return result;
	}

	public long update_list(List<?> models) {
		long result = 0;
		long tmp_id;
		synchronized("lock") {
			SQLiteDatabase db = this.mDatabaseManager.open();
			try {				
				db.beginTransaction();			
				for(Object obj:models){
					tmp_id =update_by_primary(db, obj);
					if(tmp_id>0){
						result ++;			
					}
					db.yieldIfContendedSafely();
				}
				db.setTransactionSuccessful();
			} finally { 
				db.endTransaction();
				this.mDatabaseManager.close();			
			}
		}
		return result;
	}
	
	public long countByType(boolean flag,int type){
		long result;
		synchronized("lock") {
			SQLiteDatabase db = this.mDatabaseManager.open();
			result=count(db,flag,type);
			this.mDatabaseManager.close();
		}
		return result;
	}
	public long countCard(SQLiteDatabase db,boolean flag){
		String table="card ";
		String where="where flag=? ";
		String groupBy="group by type ";
		String[] temp={"0"};
		if(flag){
			temp[0]="1";
		}
		Cursor cursor = db.rawQuery("select count(*) from (select count(id)from "+table+where+groupBy+")",temp);
		cursor.moveToFirst();
		Long count = cursor.getLong(0);
		cursor.close();
		return count;
	}

	public long insert(Object model) {
		SQLiteDatabase db = this.mDatabaseManager.open();
		long result = this.insertModel(db, model);
		Util.setAutoId(this.mApplication, model, result);
		this.mDatabaseManager.close();
		return result;
	}


	private long insertModel(SQLiteDatabase db, Object model) {
		ContentValues values = new ContentValues();
		List atas = Util.getTableColumn(this.mApplication, model.getClass());

		for (Object ata : atas) {
			ColumnAttribute result = (ColumnAttribute) ata;
			Object value = null;

			try {
				value = result.field.get(model);
				if (result.autoincrement && Long.parseLong(value.toString()) <= 0L) {
					continue;
				}
			} catch (Exception var9) {
//				var9.printStackTrace();
			}

			if (value != null) {
				if (!value.getClass().equals(Boolean.class) && !value.getClass().equals(Boolean.TYPE)) {
					if (value.getClass().equals(Date.class)) {
						values.put(result.name, ((Date) value).getTime());
					} else if (value.getClass().equals(java.sql.Date.class)) {
						values.put(result.name, ((java.sql.Date) value).getTime());
					} else if (!value.getClass().equals(Double.class) && !value.getClass().equals(Double.TYPE)) {
						if (!value.getClass().equals(Float.class) && !value.getClass().equals(Float.TYPE)) {
							if (!value.getClass().equals(Integer.class) && !value.getClass().equals(Integer.TYPE)) {
								if (!value.getClass().equals(Long.class) && !value.getClass().equals(Long.TYPE)) {
									if (value.getClass().equals(String.class) || value.getClass().equals(Character.TYPE)) {
										values.put(result.name, value.toString());
									}
								} else {
									values.put(result.name, (Long) value);
								}
							} else {
								values.put(result.name, (Integer) value);
							}
						} else {
							values.put(result.name, (Float) value);
						}
					} else {
						values.put(result.name, (Double) value);
					}
				} else {
					values.put(result.name, (Boolean) value);
				}
			}
		}

		return db.insert(Util.getTableName(model.getClass()),null, values);
	}

	public <T>List<T>  select(Class<T> model, boolean distinct, String selection, String[] selectionArgs, String groupBy, String having, String order, String limit) {
		SQLiteDatabase db = this.mDatabaseManager.open();
		Cursor cursor = db.query(distinct, Util.getTableName(model), null, selection, selectionArgs, groupBy, having, order, limit);
		List<T> list = processCursor(model,cursor);
		cursor.close();
		db.close();
		return list;
	}

	public static <T>List<T> processCursor(Class<T> object, Cursor cursor) {
		ArrayList<T> entities = new ArrayList<>();
		try {
			if(cursor.moveToFirst()) {
				do {
					T e = object.newInstance();
					loadModel(object, cursor, e);
					entities.add(e);
				} while(cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("Hadi", e.getMessage());
		}
		return entities;
	}

	public static void loadModel(Class<?> object, Cursor cursor, Object model) {
		Field[] fields = object.getDeclaredFields();
		for (Field field : fields) {
			Column tmp_c = field.getAnnotation(Column.class);
			String fieldName = "";
			if (tmp_c != null) {
				fieldName = tmp_c.name();
				if (fieldName.equals("")) {
					fieldName = field.getName();
				}
			}

			Class fieldType = field.getType();
			int columnIndex = cursor.getColumnIndex(fieldName);
			if (columnIndex >= 0) {
				field.setAccessible(true);

				try {
					if (!fieldType.equals(Boolean.class) && !fieldType.equals(Boolean.TYPE)) {
						if (fieldType.equals(Character.TYPE)) {
							field.set(model, cursor.getString(columnIndex).charAt(0));
						} else if (fieldType.equals(Date.class)) {
							field.set(model, new Date(cursor.getLong(columnIndex)));
						} else if (fieldType.equals(java.sql.Date.class)) {
							field.set(model, new java.sql.Date(cursor.getLong(columnIndex)));
						} else if (!fieldType.equals(Double.class) && !fieldType.equals(Double.TYPE)) {
							if (!fieldType.equals(Float.class) && !fieldType.equals(Float.TYPE)) {
								if (!fieldType.equals(Integer.class) && !fieldType.equals(Integer.TYPE)) {
									if (!fieldType.equals(Long.class) && !fieldType.equals(Long.TYPE)) {
										if (fieldType.equals(String.class)) {
											field.set(model, cursor.getString(columnIndex));
										}
									} else {
										field.set(model, cursor.getLong(columnIndex));
									}
								} else {
									field.set(model, cursor.getInt(columnIndex));
								}
							} else {
								field.set(model, cursor.getFloat(columnIndex));
							}
						} else {
							field.set(model, cursor.getDouble(columnIndex));
						}
					} else {
						field.set(model, cursor.getInt(columnIndex) != 0);
					}
				} catch (Exception e) {
					Log.e("Hadi", e.getMessage());
				}
			}
		}

	}

	private long count(SQLiteDatabase db,boolean flag,int type){
		String table="mission ";
		String where="where tag=?";
		String[] temp={"0"};
		String groupBy="";
		if(type==1){
			table="trove ";
			where="where flag=?";
		}else if(type==2){
			return countCard(db,flag);
		}
		if(flag){
			temp[0]="1";
		}
		Cursor cursor = db.rawQuery("select count(id)from "+table+where+groupBy,temp);
		cursor.moveToFirst();
		Long count = cursor.getLong(0);
		cursor.close();
		return count;
	}
	/**
	 * @param model update by primary key, please make sure your class has primary key
	 * @return the number of rows affected
	 */
	private long update_by_primary(SQLiteDatabase db,Object model){
		List<ColumnAttribute> atas = Util.getTableColumn(mApplication,model.getClass());
		String whereClause = "";
		List<String> whereArgs = new ArrayList<>();
		List<String> columns = new ArrayList<>();
		for(ColumnAttribute ata:atas){
			if(ata.primary){
				whereClause = whereClause + "and "+ata.name+" = ?";
				try {
					whereArgs.add(ata.field.get(model).toString());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					Log.e("tag", "update_by_primary : " , e);
				}
			}else{
				columns.add(ata.name);
			}
		}
		if(whereArgs.size()>0){
			return update(db,model,columns.toArray(new String[columns.size()]),whereClause.substring(4),whereArgs.toArray(new String[whereArgs.size()]));
		}
		return -1;
	}
	/**
	 * 
	 * @param model the object that you want to update
	 * @param columns columns that you want to changed
	 * @param whereClause the optional WHERE clause to apply when updating. Passing null will update all rows.
	 * @param whereArgs the optional WHERE clause to apply when updating. Passing null will update all rows.
	 * @return the number of rows affected
	 */
	private long update(SQLiteDatabase db,Object model,String[] columns,String whereClause,String[] whereArgs){
		boolean update_column = false;
		
		if(columns != null && columns.length > 0){
			update_column = true;
		}
		ContentValues values = new ContentValues();
		List<ColumnAttribute> atas = Util.getTableColumn(mApplication,model.getClass());
		for (ColumnAttribute ata : atas) {
			if(update_column&&!Util.hasColumn(ata.name, columns)){
				continue;
			}
			Object value = null;
			try {
				value = ata.field.get(model);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				Log.e("tag", "update : " , e);
			}
			if (value == null) {
				values.put(ata.name, (String) null);
			} else if ((value.getClass().equals(Boolean.class)) || (value.getClass().equals(Boolean.TYPE))) {
				values.put(ata.name, (Boolean) value);
			} else if (value.getClass().equals(java.util.Date.class)) {
				values.put(ata.name, ((java.util.Date)value).getTime());
			} else if (value.getClass().equals(java.sql.Date.class)) {
				values.put(ata.name, ((java.sql.Date)value).getTime());
			} else if ((value.getClass().equals(Double.class)) || (value.getClass().equals(Double.TYPE))) {
				values.put(ata.name, (Double) value);
			} else if ((value.getClass().equals(Float.class)) || (value.getClass().equals(Float.TYPE))) {
				values.put(ata.name, (Float) value);
			} else if ((value.getClass().equals(Integer.class))	|| (value.getClass().equals(Integer.TYPE))) {
				values.put(ata.name, (Integer) value);
			} else if ((value.getClass().equals(Long.class)) || (value.getClass().equals(Long.TYPE))) {
				values.put(ata.name, (Long) value);
			} else if ((value.getClass().equals(String.class)) || (value.getClass().equals(Character.TYPE))) {
				values.put(ata.name, value.toString());
			} 
		}
		return db.update(Util.getTableName(model.getClass()), values, whereClause, whereArgs);
	}
}
