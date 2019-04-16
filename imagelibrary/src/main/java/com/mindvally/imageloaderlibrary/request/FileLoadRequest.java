package com.mindvally.imageloaderlibrary.request;

import android.support.annotation.IntDef;

import com.mindvally.imageloaderlibrary.listener.FileRequestListener;
import com.mindvally.imageloaderlibrary.library.FileType;


public class FileLoadRequest {
    //Return File type
    public static final int TYPE_FILE = 1;
    public static final int TYPE_BITMAP = 2;


    @IntDef({TYPE_FILE, TYPE_BITMAP})
    public @interface ReturnFileType {
    }

    private String uri;
    private String directoryName;
    private int directoryType;
    @ReturnFileType
    private int fileType;
    private String fileExtension = FileType.UNKNOWN;
    private Class requestClass;
    private boolean forceLoadFromNetwork;
    private boolean autoRefresh;
    private boolean checkIntegrity;
    private String fileNamePrefix = "";
    private FileRequestListener requestListener;

    public FileLoadRequest(String uri, String directoryName, int directoryType, int fileType, Class requestClass, String fileExtension, boolean forceLoadFromNetwork, FileRequestListener listener) {
        this.uri = uri;
        this.directoryName = directoryName;
        this.directoryType = directoryType;
        this.fileType = fileType;
        this.requestClass = requestClass;
        this.fileExtension = fileExtension;
        this.forceLoadFromNetwork = forceLoadFromNetwork;
        this.requestListener = listener;
    }

    public FileLoadRequest(String uri, String directoryName, int directoryType, int fileType, Class requestClass, String fileExtension, boolean forceLoadFromNetwork, boolean autoRefresh, boolean checkIntegrity, FileRequestListener listener, String fileNamePrefix) {
        this(uri, directoryName, directoryType, fileType, requestClass, fileExtension, forceLoadFromNetwork, listener);
        this.autoRefresh = autoRefresh;
        this.checkIntegrity = checkIntegrity;
        this.fileNamePrefix = fileNamePrefix;
    }

    public FileLoadRequest(String uri, String directoryName, int directoryType) {
        this.uri = uri;
        this.directoryName = directoryName;
        this.directoryType = directoryType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public int getDirectoryType() {
        return directoryType;
    }

    public void setDirectoryType(int directoryType) {
        this.directoryType = directoryType;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public FileRequestListener getRequestListener() {
        return requestListener;
    }

    public void setRequestListener(FileRequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public Class getRequestClass() {
        return requestClass;
    }

    public void setRequestClass(Class requestClass) {
        this.requestClass = requestClass;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public boolean isForceLoadFromNetwork() {
        return forceLoadFromNetwork;
    }

    public void setForceLoadFromNetwork(boolean forceLoadFromNetwork) {
        this.forceLoadFromNetwork = forceLoadFromNetwork;
    }

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public boolean isCheckIntegrity() {
        return checkIntegrity;
    }

    public void setCheckIntegrity(boolean checkIntegrity) {
        this.checkIntegrity = checkIntegrity;
    }

    public String getFileNamePrefix() {
        return fileNamePrefix;
    }

    public void setFileNamePrefix(String fileNamePrefix) {
        this.fileNamePrefix = fileNamePrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileLoadRequest that = (FileLoadRequest) o;

        if (directoryType != that.directoryType) return false;
        if (fileType != that.fileType) return false;
        if (!uri.equals(that.uri)) return false;
        return directoryName.equals(that.directoryName);
    }
    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + directoryName.hashCode();
        result = 31 * result + directoryType;
        result = 31 * result + fileType;
        return result;
    }
}
