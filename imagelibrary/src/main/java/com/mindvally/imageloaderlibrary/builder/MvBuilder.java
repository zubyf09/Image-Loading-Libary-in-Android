package com.mindvally.imageloaderlibrary.builder;

import android.content.Context;

import com.mindvally.imageloaderlibrary.MVLoader;
import com.mindvally.imageloaderlibrary.listener.FileRequestListener;
import com.mindvally.imageloaderlibrary.request.FileLoadRequest;
import com.mindvally.imageloaderlibrary.library.FileType;

import java.io.File;


public class MvBuilder {
    private Context context;
    private String uri;
    private String directoryName = MVLoader.DEFAULT_DIR_NAME;
    private int directoryType = MVLoader.DEFAULT_DIR_TYPE;
    private String fileExtension = FileType.UNKNOWN;

    private FileRequestListener listener;
    @FileLoadRequest.ReturnFileType
    private int returnFileType;
    private Class requestClass;
    private MVLoader fileLoader;
    private boolean forceLoadFromNetwork;
    private boolean autoRefresh;
    private boolean checkIntegrity;
    private String fileNamePrefix = "";


    public MvBuilder(Context context) {
        this.context = context;
    }

    public MvBuilder(Context context, boolean autoRefresh) {
        this.context = context;
        this.autoRefresh = autoRefresh;
    }

    public MvBuilder load(String uri) {
        this.uri = uri;
        return this;
    }

    public MvBuilder load(String uri, boolean forceLoadFromNetwork) {
        this.forceLoadFromNetwork = forceLoadFromNetwork;
        return load(uri);
    }


    public MvBuilder fromDirectory(String directoryName, @MVLoader.DirectoryType int directoryType) {
        this.directoryName = directoryName;
        this.directoryType = directoryType;
        return this;
    }


    public void asFile(FileRequestListener<File> listener) {
        returnFileType = FileLoadRequest.TYPE_FILE;
        this.listener = listener;
        buildFileLoader();
        fileLoader.loadAsyncData();
    }


    private void buildFileLoader() {
        fileLoader = new MVLoader(context);
        fileLoader.setFileLoadRequest(new FileLoadRequest(uri, directoryName, directoryType, returnFileType, requestClass, fileExtension, forceLoadFromNetwork, autoRefresh, checkIntegrity, listener, fileNamePrefix));
    }
}
