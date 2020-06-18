package com.jimilab.uwclient.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.jimilab.uwclient.dao.TaskDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TASK_DAO".
*/
public class TaskDaoDao extends AbstractDao<TaskDao, Long> {

    public static final String TABLENAME = "TASK_DAO";

    /**
     * Properties of entity TaskDao.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property TaskId = new Property(0, Long.class, "taskId", true, "_id");
        public final static Property TaskName = new Property(1, String.class, "taskName", false, "TASK_NAME");
        public final static Property TaskSupplier = new Property(2, String.class, "taskSupplier", false, "TASK_SUPPLIER");
        public final static Property Destination = new Property(3, String.class, "destination", false, "DESTINATION");
        public final static Property DestinationIndex = new Property(4, String.class, "destinationIndex", false, "DESTINATION_INDEX");
    }


    public TaskDaoDao(DaoConfig config) {
        super(config);
    }
    
    public TaskDaoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TASK_DAO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: taskId
                "\"TASK_NAME\" TEXT," + // 1: taskName
                "\"TASK_SUPPLIER\" TEXT," + // 2: taskSupplier
                "\"DESTINATION\" TEXT," + // 3: destination
                "\"DESTINATION_INDEX\" TEXT);"); // 4: destinationIndex
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TASK_DAO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TaskDao entity) {
        stmt.clearBindings();
 
        Long taskId = entity.getTaskId();
        if (taskId != null) {
            stmt.bindLong(1, taskId);
        }
 
        String taskName = entity.getTaskName();
        if (taskName != null) {
            stmt.bindString(2, taskName);
        }
 
        String taskSupplier = entity.getTaskSupplier();
        if (taskSupplier != null) {
            stmt.bindString(3, taskSupplier);
        }
 
        String destination = entity.getDestination();
        if (destination != null) {
            stmt.bindString(4, destination);
        }
 
        String destinationIndex = entity.getDestinationIndex();
        if (destinationIndex != null) {
            stmt.bindString(5, destinationIndex);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TaskDao entity) {
        stmt.clearBindings();
 
        Long taskId = entity.getTaskId();
        if (taskId != null) {
            stmt.bindLong(1, taskId);
        }
 
        String taskName = entity.getTaskName();
        if (taskName != null) {
            stmt.bindString(2, taskName);
        }
 
        String taskSupplier = entity.getTaskSupplier();
        if (taskSupplier != null) {
            stmt.bindString(3, taskSupplier);
        }
 
        String destination = entity.getDestination();
        if (destination != null) {
            stmt.bindString(4, destination);
        }
 
        String destinationIndex = entity.getDestinationIndex();
        if (destinationIndex != null) {
            stmt.bindString(5, destinationIndex);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TaskDao readEntity(Cursor cursor, int offset) {
        TaskDao entity = new TaskDao( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // taskId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // taskName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // taskSupplier
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // destination
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // destinationIndex
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TaskDao entity, int offset) {
        entity.setTaskId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTaskName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTaskSupplier(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDestination(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDestinationIndex(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TaskDao entity, long rowId) {
        entity.setTaskId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TaskDao entity) {
        if(entity != null) {
            return entity.getTaskId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TaskDao entity) {
        return entity.getTaskId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
