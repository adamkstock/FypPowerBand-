package org.eclipse.paho.android.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Iterator;
import java.util.UUID;
import org.eclipse.paho.android.service.MessageStore.StoredMessage;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class DatabaseMessageStore implements MessageStore {
    private static final String ARRIVED_MESSAGE_TABLE_NAME = "MqttArrivedMessageTable";
    private static final String MTIMESTAMP = "mtimestamp";
    private static String TAG = "DatabaseMessageStore";
    /* access modifiers changed from: private */

    /* renamed from: db */
    public SQLiteDatabase f95db = null;
    /* access modifiers changed from: private */
    public MQTTDatabaseHelper mqttDb = null;
    private MqttTraceHandler traceHandler = null;

    private class DbStoredData implements StoredMessage {
        private String clientHandle;
        private MqttMessage message;
        private String messageId;
        private String topic;

        DbStoredData(String str, String str2, String str3, MqttMessage mqttMessage) {
            this.messageId = str;
            this.topic = str3;
            this.message = mqttMessage;
        }

        public String getMessageId() {
            return this.messageId;
        }

        public String getClientHandle() {
            return this.clientHandle;
        }

        public String getTopic() {
            return this.topic;
        }

        public MqttMessage getMessage() {
            return this.message;
        }
    }

    private static class MQTTDatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "mqttAndroidService.db";
        private static final int DATABASE_VERSION = 1;
        private static String TAG = "MQTTDatabaseHelper";
        private MqttTraceHandler traceHandler = null;

        public MQTTDatabaseHelper(MqttTraceHandler mqttTraceHandler, Context context) {
            super(context, DATABASE_NAME, null, 1);
            this.traceHandler = mqttTraceHandler;
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            MqttTraceHandler mqttTraceHandler = this.traceHandler;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onCreate {");
            String str2 = "CREATE TABLE MqttArrivedMessageTable(messageId TEXT PRIMARY KEY, clientHandle TEXT, destinationName TEXT, payload BLOB, qos INTEGER, retained TEXT, duplicate TEXT, mtimestamp INTEGER);";
            sb.append(str2);
            sb.append("}");
            mqttTraceHandler.traceDebug(str, sb.toString());
            try {
                sQLiteDatabase.execSQL(str2);
                this.traceHandler.traceDebug(TAG, "created the table");
            } catch (SQLException e) {
                this.traceHandler.traceException(TAG, "onCreate", e);
                throw e;
            }
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            String str = "onUpgrade";
            this.traceHandler.traceDebug(TAG, str);
            try {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS MqttArrivedMessageTable");
                onCreate(sQLiteDatabase);
                this.traceHandler.traceDebug(TAG, "onUpgrade complete");
            } catch (SQLException e) {
                this.traceHandler.traceException(TAG, str, e);
                throw e;
            }
        }
    }

    private class MqttMessageHack extends MqttMessage {
        public MqttMessageHack(byte[] bArr) {
            super(bArr);
        }

        /* access modifiers changed from: protected */
        public void setDuplicate(boolean z) {
            super.setDuplicate(z);
        }
    }

    public DatabaseMessageStore(MqttService mqttService, Context context) {
        this.traceHandler = mqttService;
        this.mqttDb = new MQTTDatabaseHelper(this.traceHandler, context);
        this.traceHandler.traceDebug(TAG, "DatabaseMessageStore<init> complete");
    }

    public String storeArrived(String str, String str2, MqttMessage mqttMessage) {
        this.f95db = this.mqttDb.getWritableDatabase();
        MqttTraceHandler mqttTraceHandler = this.traceHandler;
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("storeArrived{");
        sb.append(str);
        sb.append("}, {");
        sb.append(mqttMessage.toString());
        sb.append("}");
        mqttTraceHandler.traceDebug(str3, sb.toString());
        byte[] payload = mqttMessage.getPayload();
        int qos = mqttMessage.getQos();
        boolean isRetained = mqttMessage.isRetained();
        boolean isDuplicate = mqttMessage.isDuplicate();
        ContentValues contentValues = new ContentValues();
        String uuid = UUID.randomUUID().toString();
        contentValues.put(MqttServiceConstants.MESSAGE_ID, uuid);
        contentValues.put(MqttServiceConstants.CLIENT_HANDLE, str);
        contentValues.put(MqttServiceConstants.DESTINATION_NAME, str2);
        contentValues.put(MqttServiceConstants.PAYLOAD, payload);
        contentValues.put(MqttServiceConstants.QOS, Integer.valueOf(qos));
        contentValues.put(MqttServiceConstants.RETAINED, Boolean.valueOf(isRetained));
        contentValues.put(MqttServiceConstants.DUPLICATE, Boolean.valueOf(isDuplicate));
        contentValues.put(MTIMESTAMP, Long.valueOf(System.currentTimeMillis()));
        try {
            this.f95db.insertOrThrow(ARRIVED_MESSAGE_TABLE_NAME, null, contentValues);
            int arrivedRowCount = getArrivedRowCount(str);
            MqttTraceHandler mqttTraceHandler2 = this.traceHandler;
            String str4 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("storeArrived: inserted message with id of {");
            sb2.append(uuid);
            sb2.append("} - Number of messages in database for this clientHandle = ");
            sb2.append(arrivedRowCount);
            mqttTraceHandler2.traceDebug(str4, sb2.toString());
            return uuid;
        } catch (SQLException e) {
            this.traceHandler.traceException(TAG, "onUpgrade", e);
            throw e;
        }
    }

    private int getArrivedRowCount(String str) {
        String[] strArr = {"COUNT(*)"};
        SQLiteDatabase sQLiteDatabase = this.f95db;
        StringBuilder sb = new StringBuilder();
        sb.append("clientHandle='");
        sb.append(str);
        sb.append("'");
        Cursor query = sQLiteDatabase.query(ARRIVED_MESSAGE_TABLE_NAME, strArr, sb.toString(), null, null, null, null);
        int i = 0;
        if (query.moveToFirst()) {
            i = query.getInt(0);
        }
        query.close();
        return i;
    }

    public boolean discardArrived(String str, String str2) {
        this.f95db = this.mqttDb.getWritableDatabase();
        MqttTraceHandler mqttTraceHandler = this.traceHandler;
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("discardArrived{");
        sb.append(str);
        sb.append("}, {");
        sb.append(str2);
        sb.append("}");
        mqttTraceHandler.traceDebug(str3, sb.toString());
        try {
            SQLiteDatabase sQLiteDatabase = this.f95db;
            String str4 = ARRIVED_MESSAGE_TABLE_NAME;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("messageId='");
            sb2.append(str2);
            sb2.append("' AND ");
            sb2.append(MqttServiceConstants.CLIENT_HANDLE);
            sb2.append("='");
            sb2.append(str);
            sb2.append("'");
            int delete = sQLiteDatabase.delete(str4, sb2.toString(), null);
            if (delete != 1) {
                MqttTraceHandler mqttTraceHandler2 = this.traceHandler;
                String str5 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("discardArrived - Error deleting message {");
                sb3.append(str2);
                sb3.append("} from database: Rows affected = ");
                sb3.append(delete);
                mqttTraceHandler2.traceError(str5, sb3.toString());
                return false;
            }
            int arrivedRowCount = getArrivedRowCount(str);
            MqttTraceHandler mqttTraceHandler3 = this.traceHandler;
            String str6 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("discardArrived - Message deleted successfully. - messages in db for this clientHandle ");
            sb4.append(arrivedRowCount);
            mqttTraceHandler3.traceDebug(str6, sb4.toString());
            return true;
        } catch (SQLException e) {
            this.traceHandler.traceException(TAG, "discardArrived", e);
            throw e;
        }
    }

    public Iterator<StoredMessage> getAllArrivedMessages(final String str) {
        return new Iterator<StoredMessage>() {

            /* renamed from: c */
            private Cursor f96c;
            private boolean hasNext;

            {
                DatabaseMessageStore databaseMessageStore = DatabaseMessageStore.this;
                databaseMessageStore.f95db = databaseMessageStore.mqttDb.getWritableDatabase();
                if (str == null) {
                    this.f96c = DatabaseMessageStore.this.f95db.query(DatabaseMessageStore.ARRIVED_MESSAGE_TABLE_NAME, null, null, null, null, null, "mtimestamp ASC");
                } else {
                    SQLiteDatabase access$000 = DatabaseMessageStore.this.f95db;
                    StringBuilder sb = new StringBuilder();
                    sb.append("clientHandle='");
                    sb.append(str);
                    sb.append("'");
                    this.f96c = access$000.query(DatabaseMessageStore.ARRIVED_MESSAGE_TABLE_NAME, null, sb.toString(), null, null, null, "mtimestamp ASC");
                }
                this.hasNext = this.f96c.moveToFirst();
            }

            public boolean hasNext() {
                if (!this.hasNext) {
                    this.f96c.close();
                }
                return this.hasNext;
            }

            public StoredMessage next() {
                Cursor cursor = this.f96c;
                String string = cursor.getString(cursor.getColumnIndex(MqttServiceConstants.MESSAGE_ID));
                Cursor cursor2 = this.f96c;
                String string2 = cursor2.getString(cursor2.getColumnIndex(MqttServiceConstants.CLIENT_HANDLE));
                Cursor cursor3 = this.f96c;
                String string3 = cursor3.getString(cursor3.getColumnIndex(MqttServiceConstants.DESTINATION_NAME));
                Cursor cursor4 = this.f96c;
                byte[] blob = cursor4.getBlob(cursor4.getColumnIndex(MqttServiceConstants.PAYLOAD));
                Cursor cursor5 = this.f96c;
                int i = cursor5.getInt(cursor5.getColumnIndex(MqttServiceConstants.QOS));
                Cursor cursor6 = this.f96c;
                boolean parseBoolean = Boolean.parseBoolean(cursor6.getString(cursor6.getColumnIndex(MqttServiceConstants.RETAINED)));
                Cursor cursor7 = this.f96c;
                boolean parseBoolean2 = Boolean.parseBoolean(cursor7.getString(cursor7.getColumnIndex(MqttServiceConstants.DUPLICATE)));
                MqttMessageHack mqttMessageHack = new MqttMessageHack(blob);
                mqttMessageHack.setQos(i);
                mqttMessageHack.setRetained(parseBoolean);
                mqttMessageHack.setDuplicate(parseBoolean2);
                this.hasNext = this.f96c.moveToNext();
                DbStoredData dbStoredData = new DbStoredData(string, string2, string3, mqttMessageHack);
                return dbStoredData;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            /* access modifiers changed from: protected */
            public void finalize() throws Throwable {
                this.f96c.close();
                super.finalize();
            }
        };
    }

    public void clearArrivedMessages(String str) {
        int i;
        this.f95db = this.mqttDb.getWritableDatabase();
        String str2 = ARRIVED_MESSAGE_TABLE_NAME;
        if (str == null) {
            this.traceHandler.traceDebug(TAG, "clearArrivedMessages: clearing the table");
            i = this.f95db.delete(str2, null, null);
        } else {
            MqttTraceHandler mqttTraceHandler = this.traceHandler;
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("clearArrivedMessages: clearing the table of ");
            sb.append(str);
            sb.append(" messages");
            mqttTraceHandler.traceDebug(str3, sb.toString());
            SQLiteDatabase sQLiteDatabase = this.f95db;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("clientHandle='");
            sb2.append(str);
            sb2.append("'");
            i = sQLiteDatabase.delete(str2, sb2.toString(), null);
        }
        MqttTraceHandler mqttTraceHandler2 = this.traceHandler;
        String str4 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("clearArrivedMessages: rows affected = ");
        sb3.append(i);
        mqttTraceHandler2.traceDebug(str4, sb3.toString());
    }

    public void close() {
        SQLiteDatabase sQLiteDatabase = this.f95db;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
    }
}
