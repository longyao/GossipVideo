package com.hero.gossipvideo.store;

import java.io.*;

/**
 * Created by Administrator on 2015/4/18.
 */
 class FileStore<T> {

    protected void saveObjToFile(T obj, String fileName) {
        saveObjToFile(obj, new File(fileName));
    }

    protected void saveObjToFile(T obj, File f) {

        if (f == null) {
            throw new NullPointerException("the file is null");
        }

        ObjectOutputStream out = null;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(obj);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeStream(out);
        }
    }

    protected T getObjFromFile(String fileName) {
        return getObjFromFile(new File(fileName));
    }

    protected T getObjFromFile(File f) {

        if (f == null) {
            throw new NullPointerException("the file is null");
        }

        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(f));
            return (T)in.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeStream(in);
        }

        return null;
    }

    protected void deleteObjFromFile(String fileName) {
        deleteObjFromFile(new File(fileName));
    }

    protected void deleteObjFromFile(File file) {

        if (file == null) {
            throw new NullPointerException("the file is null");
        }

        if (file.exists()) {
            file.delete();
        }
    }

    protected boolean isObjExists(File f) {
        if (f == null) {
            throw new NullPointerException("the file is null");
        }
        return f.exists();
    }

    protected void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                closeable = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
