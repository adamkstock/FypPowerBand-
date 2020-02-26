package org.eclipse.paho.client.mqttv3.persist;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.internal.FileLock;
import org.eclipse.paho.client.mqttv3.internal.MqttPersistentData;

public class MqttDefaultFilePersistence implements MqttClientPersistence {
    private static final FilenameFilter FILE_FILTER = new FilenameFilter() {
        public boolean accept(File file, String str) {
            return str.endsWith(MqttDefaultFilePersistence.MESSAGE_FILE_EXTENSION);
        }
    };
    private static final String LOCK_FILENAME = ".lck";
    private static final String MESSAGE_BACKUP_FILE_EXTENSION = ".bup";
    private static final String MESSAGE_FILE_EXTENSION = ".msg";
    private File clientDir;
    private File dataDir;
    private FileLock fileLock;

    public MqttDefaultFilePersistence() {
        this(System.getProperty("user.dir"));
    }

    public MqttDefaultFilePersistence(String str) {
        this.clientDir = null;
        this.fileLock = null;
        this.dataDir = new File(str);
    }

    public void open(String str, String str2) throws MqttPersistenceException {
        if (this.dataDir.exists() && !this.dataDir.isDirectory()) {
            throw new MqttPersistenceException();
        } else if (!this.dataDir.exists() && !this.dataDir.mkdirs()) {
            throw new MqttPersistenceException();
        } else if (this.dataDir.canWrite()) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                if (isSafeChar(charAt)) {
                    stringBuffer.append(charAt);
                }
            }
            stringBuffer.append("-");
            for (int i2 = 0; i2 < str2.length(); i2++) {
                char charAt2 = str2.charAt(i2);
                if (isSafeChar(charAt2)) {
                    stringBuffer.append(charAt2);
                }
            }
            synchronized (this) {
                if (this.clientDir == null) {
                    this.clientDir = new File(this.dataDir, stringBuffer.toString());
                    if (!this.clientDir.exists()) {
                        this.clientDir.mkdir();
                    }
                }
                try {
                    this.fileLock = new FileLock(this.clientDir, LOCK_FILENAME);
                    restoreBackups(this.clientDir);
                } catch (Exception unused) {
                    throw new MqttPersistenceException(32200);
                }
            }
        } else {
            throw new MqttPersistenceException();
        }
    }

    private void checkIsOpen() throws MqttPersistenceException {
        if (this.clientDir == null) {
            throw new MqttPersistenceException();
        }
    }

    public void close() throws MqttPersistenceException {
        synchronized (this) {
            if (this.fileLock != null) {
                this.fileLock.release();
            }
            if (getFiles().length == 0) {
                this.clientDir.delete();
            }
            this.clientDir = null;
        }
    }

    public void put(String str, MqttPersistable mqttPersistable) throws MqttPersistenceException {
        checkIsOpen();
        File file = this.clientDir;
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(str));
        String str2 = MESSAGE_FILE_EXTENSION;
        stringBuffer.append(str2);
        File file2 = new File(file, stringBuffer.toString());
        File file3 = this.clientDir;
        StringBuffer stringBuffer2 = new StringBuffer(String.valueOf(str));
        stringBuffer2.append(str2);
        stringBuffer2.append(MESSAGE_BACKUP_FILE_EXTENSION);
        File file4 = new File(file3, stringBuffer2.toString());
        if (file2.exists() && !file2.renameTo(file4)) {
            file4.delete();
            file2.renameTo(file4);
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write(mqttPersistable.getHeaderBytes(), mqttPersistable.getHeaderOffset(), mqttPersistable.getHeaderLength());
            if (mqttPersistable.getPayloadBytes() != null) {
                fileOutputStream.write(mqttPersistable.getPayloadBytes(), mqttPersistable.getPayloadOffset(), mqttPersistable.getPayloadLength());
            }
            fileOutputStream.getFD().sync();
            fileOutputStream.close();
            if (file4.exists()) {
                file4.delete();
            }
            if (file4.exists() && !file4.renameTo(file2)) {
                file2.delete();
                file4.renameTo(file2);
            }
        } catch (IOException e) {
            throw new MqttPersistenceException((Throwable) e);
        } catch (Throwable th) {
            if (file4.exists() && !file4.renameTo(file2)) {
                file2.delete();
                file4.renameTo(file2);
            }
            throw th;
        }
    }

    public MqttPersistable get(String str) throws MqttPersistenceException {
        checkIsOpen();
        try {
            File file = this.clientDir;
            StringBuffer stringBuffer = new StringBuffer(String.valueOf(str));
            stringBuffer.append(MESSAGE_FILE_EXTENSION);
            FileInputStream fileInputStream = new FileInputStream(new File(file, stringBuffer.toString()));
            int available = fileInputStream.available();
            byte[] bArr = new byte[available];
            for (int i = 0; i < available; i += fileInputStream.read(bArr, i, available - i)) {
            }
            fileInputStream.close();
            MqttPersistentData mqttPersistentData = new MqttPersistentData(str, bArr, 0, bArr.length, null, 0, 0);
            return mqttPersistentData;
        } catch (IOException e) {
            throw new MqttPersistenceException((Throwable) e);
        }
    }

    public void remove(String str) throws MqttPersistenceException {
        checkIsOpen();
        File file = this.clientDir;
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(str));
        stringBuffer.append(MESSAGE_FILE_EXTENSION);
        File file2 = new File(file, stringBuffer.toString());
        if (file2.exists()) {
            file2.delete();
        }
    }

    public Enumeration keys() throws MqttPersistenceException {
        checkIsOpen();
        File[] files = getFiles();
        Vector vector = new Vector(files.length);
        for (File name : files) {
            String name2 = name.getName();
            vector.addElement(name2.substring(0, name2.length() - 4));
        }
        return vector.elements();
    }

    private File[] getFiles() throws MqttPersistenceException {
        checkIsOpen();
        File[] listFiles = this.clientDir.listFiles(FILE_FILTER);
        if (listFiles != null) {
            return listFiles;
        }
        throw new MqttPersistenceException();
    }

    private boolean isSafeChar(char c) {
        return Character.isJavaIdentifierPart(c) || c == '-';
    }

    private void restoreBackups(File file) throws MqttPersistenceException {
        File[] listFiles = file.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith(MqttDefaultFilePersistence.MESSAGE_BACKUP_FILE_EXTENSION);
            }
        });
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                File file2 = new File(file, listFiles[i].getName().substring(0, listFiles[i].getName().length() - 4));
                if (!listFiles[i].renameTo(file2)) {
                    file2.delete();
                    listFiles[i].renameTo(file2);
                }
            }
            return;
        }
        throw new MqttPersistenceException();
    }

    public boolean containsKey(String str) throws MqttPersistenceException {
        checkIsOpen();
        File file = this.clientDir;
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(str));
        stringBuffer.append(MESSAGE_FILE_EXTENSION);
        return new File(file, stringBuffer.toString()).exists();
    }

    public void clear() throws MqttPersistenceException {
        checkIsOpen();
        File[] files = getFiles();
        for (File delete : files) {
            delete.delete();
        }
    }
}