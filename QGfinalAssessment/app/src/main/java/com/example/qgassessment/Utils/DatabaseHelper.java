package com.example.qgassessment.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qgassessment.Finance.AccountBean;
import com.example.qgassessment.Todo.TodoBean;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_FINANCE = "finance";
    private static final String TABLE_TODO = "todo";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_USERNAME = "account";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_TYPE = "type";
    private static final String KEY_USER_MONEY = "money";
    private static final String KEY_USER_TIME = "time";
    private static final String KEY_USER_CONNECT_ID = "user_id";
    private static final String KEY_USER_YEAR = "year";
    private static final String KEY_USER_MONTH = "month";
    private static final String KEY_USER_DAY = "day";

//用户登陆注册表
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USER_USERNAME + " TEXT,"
            + KEY_USER_PASSWORD + " TEXT" + ")";
//记账表
    private static final String CREATE_USERS_FINANCE_TABLE =
            "CREATE TABLE finance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "type TEXT, " +
                    "money REAL NOT NULL, " +
                    "time DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "year INTEGER, " +
                    "month INTEGER, " +
                    "day INTEGER, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))";
//每日事项记录表
    private static final String CREATE_TODO_TABLE =
            "CREATE TABLE todo (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "title TEXT, " +
                    "start_time DATETIME, " +
                    "end_time DATETIME, " +
                    "content TEXT, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))";

    private Context mContext;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_USERS_FINANCE_TABLE);
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }
//查找表中是否已经存在用户
    public boolean checkUser(String account, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String USERS_SELECT_QUERY = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_USERNAME + " = ? AND " + KEY_USER_PASSWORD + " = ?";

        try (Cursor cursor = db.query(TABLE_USERS, null, KEY_USER_USERNAME + "=? AND " + KEY_USER_PASSWORD + "=?",
                new String[]{account, password}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("db",e.getMessage(),e);
            return false;
        }
    }

//获取用户ID的方法
    public int getUserId(String account) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        String query = "SELECT " + KEY_USER_ID + " FROM " + TABLE_USERS +
                " WHERE " + KEY_USER_USERNAME + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{account})) {
            if (cursor != null && cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID));
            }
        }
        return userId;
    }
//插入记账记录
    public long insertFinanceRecord(AccountBean record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_CONNECT_ID, record.getUserId());
        values.put(KEY_USER_TYPE, record.getType());
        values.put(KEY_USER_MONEY, record.getMoney());
        values.put(KEY_USER_TIME, record.getTime());
        values.put(KEY_USER_YEAR, record.getYear());
        values.put(KEY_USER_MONTH, record.getMonth());
        values.put(KEY_USER_DAY, record.getDay());

        return db.insert(TABLE_FINANCE, null, values);
    }
//获取当前用户记录
    public List<AccountBean> getAllRecords(int userId) {
        List<AccountBean> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_FINANCE +
                " WHERE " + KEY_USER_CONNECT_ID + "=?" +
                " ORDER BY " + KEY_USER_TIME + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {     //遍历表
            do {
                AccountBean record = new AccountBean();     //AccountBean封装记账本记录对象
                record.setId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
                record.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_CONNECT_ID)));
                record.setType(cursor.getString(cursor.getColumnIndex(KEY_USER_TYPE)));
                record.setMoney(cursor.getFloat(cursor.getColumnIndex(KEY_USER_MONEY)));
                record.setTime(cursor.getString(cursor.getColumnIndex(KEY_USER_TIME)));
                record.setYear(cursor.getInt(cursor.getColumnIndex(KEY_USER_YEAR)));
                record.setMonth(cursor.getInt(cursor.getColumnIndex(KEY_USER_MONTH)));
                record.setDay(cursor.getInt(cursor.getColumnIndex(KEY_USER_DAY)));
                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }
//删除记录
    public boolean deleteFinanceRecord(int recordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FINANCE, KEY_USER_ID + "=?", new String[]{String.valueOf(recordId)}) > 0;
    }
//计算总收入
    public float getTotalIncome(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        float total = 0;

        String selectQuery = "SELECT SUM(" + KEY_USER_MONEY + ") FROM " + TABLE_FINANCE +
                " WHERE " + KEY_USER_CONNECT_ID + "=? AND " + KEY_USER_MONEY + " > 0";      //收入记为正数

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            total = cursor.getFloat(0);
        }
        cursor.close();
        return total;
    }
//计算总支出
    public float getTotalExpense(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        float total = 0;

        String selectQuery = "SELECT SUM(" + KEY_USER_MONEY + ") FROM " + TABLE_FINANCE +
                " WHERE " + KEY_USER_CONNECT_ID + "=? AND " + KEY_USER_MONEY + " < 0";      //支出记为负数

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            total = Math.abs(cursor.getFloat(0));
        }
        cursor.close();
        return total;
    }
//添加todo记录
    public long insertTodo(TodoBean todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", todo.getUserId());
        values.put("title", todo.getTitle());
        values.put("start_time", todo.getStartTime());
        values.put("end_time", todo.getEndTime());
        values.put("content", todo.getContent());
        return db.insert("todo", null, values);
    }
//提取当前用户的记录
    public List<TodoBean> getAllTodos(int userId) {
        List<TodoBean> todos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("todo", null,
                "user_id = ?", new String[]{String.valueOf(userId)},
                null, null, "start_time ASC");

        if (cursor.moveToFirst()) {
            do {
                TodoBean todo = new TodoBean();     //TodoBean封装每日事项的对象
                todo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                todo.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
                todo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                todo.setStartTime(cursor.getString(cursor.getColumnIndex("start_time")));
                todo.setEndTime(cursor.getString(cursor.getColumnIndex("end_time")));
                todo.setContent(cursor.getString(cursor.getColumnIndex("content")));
                todos.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return todos;
    }
//删除记录
    public boolean deleteTodo(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("todo", "id = ?", new String[]{String.valueOf(todoId)}) > 0;
    }
//修改记录
    public boolean updateTodo(TodoBean todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", todo.getTitle());
        values.put("start_time", todo.getStartTime());
        values.put("end_time", todo.getEndTime());
        values.put("content", todo.getContent());
        return db.update("todo", values,
                "id = ? AND user_id = ?",
                new String[]{String.valueOf(todo.getId()), String.valueOf(todo.getUserId())}) > 0;
    }
//获取当前记录
    public TodoBean getTodoById(int todoId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        TodoBean todo = null;
        Cursor cursor = db.query("todo", null,
                "id = ? AND user_id = ?",
                new String[]{String.valueOf(todoId), String.valueOf(userId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            todo = new TodoBean();
            todo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            todo.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            todo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            todo.setStartTime(cursor.getString(cursor.getColumnIndex("start_time")));
            todo.setEndTime(cursor.getString(cursor.getColumnIndex("end_time")));
            todo.setContent(cursor.getString(cursor.getColumnIndex("content")));
            cursor.close();
        }
        return todo;
    }


}
