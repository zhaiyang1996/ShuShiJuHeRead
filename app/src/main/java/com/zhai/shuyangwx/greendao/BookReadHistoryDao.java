package com.zhai.shuyangwx.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zhai.shuyangwx.bean.BookReadHistory;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOOK_READ_HISTORY".
*/
public class BookReadHistoryDao extends AbstractDao<BookReadHistory, Long> {

    public static final String TABLENAME = "BOOK_READ_HISTORY";

    /**
     * Properties of entity BookReadHistory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Bookid = new Property(1, String.class, "bookid", false, "BOOKID");
        public final static Property Mix = new Property(2, int.class, "mix", false, "MIX");
        public final static Property Paga = new Property(3, int.class, "paga", false, "PAGA");
    }


    public BookReadHistoryDao(DaoConfig config) {
        super(config);
    }
    
    public BookReadHistoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOK_READ_HISTORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BOOKID\" TEXT NOT NULL ," + // 1: bookid
                "\"MIX\" INTEGER NOT NULL ," + // 2: mix
                "\"PAGA\" INTEGER NOT NULL );"); // 3: paga
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOK_READ_HISTORY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BookReadHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getBookid());
        stmt.bindLong(3, entity.getMix());
        stmt.bindLong(4, entity.getPaga());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BookReadHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getBookid());
        stmt.bindLong(3, entity.getMix());
        stmt.bindLong(4, entity.getPaga());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public BookReadHistory readEntity(Cursor cursor, int offset) {
        BookReadHistory entity = new BookReadHistory( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // bookid
            cursor.getInt(offset + 2), // mix
            cursor.getInt(offset + 3) // paga
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BookReadHistory entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBookid(cursor.getString(offset + 1));
        entity.setMix(cursor.getInt(offset + 2));
        entity.setPaga(cursor.getInt(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(BookReadHistory entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(BookReadHistory entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BookReadHistory entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
