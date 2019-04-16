package com.mindvally.imageloaderlibrary.dao;

import java.io.File;


public class Response {
    private File downloadedFile;
    private Exception e;

    public File getDownloadedFile() {
        return downloadedFile;
    }

    public void setDownloadedFile(File downloadedFile) {
        this.downloadedFile = downloadedFile;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }
}
