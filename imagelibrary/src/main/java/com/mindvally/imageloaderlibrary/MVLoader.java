package com.mindvally.imageloaderlibrary;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.mindvally.imageloaderlibrary.builder.MvBuilder;
import com.mindvally.imageloaderlibrary.listener.FileRequestListener;
import com.mindvally.imageloaderlibrary.network.Downloader;
import com.mindvally.imageloaderlibrary.dao.Response;
import com.mindvally.imageloaderlibrary.dao.FileResponse;
import com.mindvally.imageloaderlibrary.request.FileLoadRequest;
import com.mindvally.imageloaderlibrary.library.AndroidFileManager;
import com.mindvally.imageloaderlibrary.library.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;


public class MVLoader {


    private static final String TAG = "MVLoader";
    public static final int LOCAL_SEARCH = 100;
    public static final int STATUS_START_DOWNLOADING = 101;
    public static final int STATUS_DOWNLOAD_END = 102;

    public static final int DIR_INTERNAL = 1;
    public static final int DIR_CACHE = 2;


    //Defaults
    public static final int DEFAULT_DIR_TYPE = DIR_CACHE;
    public static final String DEFAULT_DIR_NAME = "file_loader";

    @IntDef({DIR_INTERNAL, DIR_CACHE})
    public @interface DirectoryType {
    }

    private static Map<FileLoadRequest, Boolean> backingMap = new WeakHashMap<>();
    private static Set<FileLoadRequest> fileLoadRequestSet = Collections.newSetFromMap(backingMap);
    private static Map<FileLoadRequest, List<FileRequestListener>> requestListenersMap = new WeakHashMap<>();
    private static Map<FileLoadRequest, FileResponse> requestResponseMap = new WeakHashMap<>();
    private static final Object REQUEST_QUEUE_LOCK = new Object();
    private static final Object REQUEST_LISTENER_QUEUE_LOCK = new Object();

    private Context context;
    private FileLoadRequest fileLoadRequest;

    public MVLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    public static MvBuilder with(Context context) {
        return new MvBuilder(context);
    }




    public void loadAsyncData() {
        addRequestListenerToQueue();
        try {
            validateAllParameters();
            if (fileLoadRequest.getRequestListener() == null)
                throw new NullPointerException("File Request listener should not be null");
        } catch (Exception e) {
            callFailureMethodsOfListeners(e);
            return;
        }


        if (!fileLoadRequestSet.contains(fileLoadRequest)) {
            synchronized (REQUEST_QUEUE_LOCK) {
                fileLoadRequestSet.add(fileLoadRequest);
            }
            getFileLoaderAsyncTask().executeOnExecutor(Utils.getThreadPoolExecutor());
        } else if (requestResponseMap.get(fileLoadRequest) != null) {
            sendFileResponseToListeners(requestResponseMap.get(fileLoadRequest));
        }
    }


    private void validateAllParameters() throws Exception {
        if (TextUtils.isEmpty(fileLoadRequest.getDirectoryName()))
            throw new NullPointerException("Directory name should not be null or empty");
        if (TextUtils.isEmpty(fileLoadRequest.getUri()))
            throw new NullPointerException("File uri should not be null or empty");
        if (fileLoadRequest.getFileExtension() == null)
            throw new NullPointerException("File extension should not be null");
    }

    private void addRequestListenerToQueue() {
        if (requestListenersMap.containsKey(fileLoadRequest)) {
            synchronized (REQUEST_LISTENER_QUEUE_LOCK) {
                requestListenersMap.get(fileLoadRequest).add(fileLoadRequest.getRequestListener());
            }
        } else {
            List<FileRequestListener> listenersList = new ArrayList<>();
            listenersList.add(fileLoadRequest.getRequestListener());
            requestListenersMap.put(fileLoadRequest, listenersList);
        }
    }

    private void callFailureMethodsOfListeners(Throwable t) {
        if (!requestListenersMap.isEmpty()) {
            synchronized (REQUEST_LISTENER_QUEUE_LOCK) {
                List<FileRequestListener> listenerList = requestListenersMap.get(fileLoadRequest);
                if (listenerList != null) {
                    Iterator<FileRequestListener> it = listenerList.iterator();
                    while (it.hasNext()) {
                        try {
                            FileRequestListener listener = it.next();
                            it.remove();
                            listener.onError(fileLoadRequest, t);
                        } catch (Exception e) {
                            //ignore
                        }
                    }
                    requestListenersMap.remove(fileLoadRequest);
                }
            }
            synchronized (REQUEST_QUEUE_LOCK) {
                fileLoadRequestSet.remove(fileLoadRequest);
            }
        }
    }

    private FileResponse createFileResponse(File downloadedFile) {
        FileResponse response;
        if (fileLoadRequest.getFileType() == FileLoadRequest.TYPE_BITMAP) {
            response = new FileResponse(200, AndroidFileManager.getBitmap(downloadedFile), downloadedFile.length());
        }
        else {
            response = new FileResponse(200, downloadedFile, downloadedFile.length());
        }
        response.setDownloadedFile(downloadedFile);
        return response;
    }

    private void sendFileResponseToListeners(FileResponse fileResponse) {
        if (!requestListenersMap.isEmpty()) {
            synchronized (REQUEST_LISTENER_QUEUE_LOCK) {
                List<FileRequestListener> listenerList = requestListenersMap.get(fileLoadRequest);
                if (listenerList != null) {
                    Iterator<FileRequestListener> it = listenerList.iterator();
                    while (it.hasNext()) {
                        try {
                            FileRequestListener listener = it.next();
                            it.remove();
                            listener.onLoad(fileLoadRequest, fileResponse);
                        } catch (Exception e) {
                            callFailureMethodsOfListeners(e);
                        }
                    }
                    requestListenersMap.remove(fileLoadRequest);
                }
            }
            synchronized (REQUEST_QUEUE_LOCK) {
                fileLoadRequestSet.remove(fileLoadRequest);
                requestResponseMap.remove(fileLoadRequest);
            }
        }
    }

    private void sendStatusToListeners(int status) {
        if (!requestListenersMap.isEmpty()) {
            synchronized (REQUEST_LISTENER_QUEUE_LOCK) {
                List<FileRequestListener> listenerList = requestListenersMap.get(fileLoadRequest);
                if (listenerList != null) {
                    for (FileRequestListener listener : listenerList) {
                        listener.onStatusChange(status);
                    }
                }
            }
        }
    }

    @NonNull
    private AsyncTask<Void, Integer, Response> getFileLoaderAsyncTask() {
        return new AsyncTask<Void, Integer, Response>() {

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (values.length > 0)
                    sendStatusToListeners(values[0]);
            }

            @Override
            protected Response doInBackground(Void... voids) {
                Response downloadResponse = new Response();
                File loadedFile = null;
                try {
                    if (!fileLoadRequest.isForceLoadFromNetwork()) {
                        //search file locally
                        publishProgress(LOCAL_SEARCH);
                        loadedFile = AndroidFileManager.searchAndGetLocalFile(context, fileLoadRequest.getUri(), fileLoadRequest.getFileNamePrefix(),
                                fileLoadRequest.getDirectoryName(), fileLoadRequest.getDirectoryType());
                    }
                    if (loadedFile == null || !loadedFile.exists() || fileLoadRequest.isAutoRefresh()) {
                        //download from internet
                        publishProgress(STATUS_START_DOWNLOADING);
                        Downloader downloader = new Downloader(context, fileLoadRequest.getUri(),fileLoadRequest.getFileNamePrefix(), fileLoadRequest.getDirectoryName(), fileLoadRequest.getDirectoryType());
                        loadedFile = downloader.download(fileLoadRequest.isAutoRefresh(), fileLoadRequest.isCheckIntegrity());
                        publishProgress(STATUS_DOWNLOAD_END);
                    }
                    downloadResponse.setDownloadedFile(loadedFile);
                } catch (Exception e) {
                    Log.d(TAG, "doInBackground: " + e.getMessage());
                    downloadResponse.setE(e);
                }
                return downloadResponse;
            }

            @Override
            protected void onPostExecute(Response downloadResponse) {
                super.onPostExecute(downloadResponse);
                //if task is synchronous then simply return
                if (fileLoadRequest.getRequestListener() == null) return;

                //if task is asynchronous, notify results to listeners
                File downloadedFile = downloadResponse.getDownloadedFile();
                if (downloadedFile != null && downloadResponse.getE() == null) {
                    FileResponse fileResponse = createFileResponse(downloadedFile);
                    requestResponseMap.put(fileLoadRequest, fileResponse);
                    sendFileResponseToListeners(fileResponse);
                } else {
                    callFailureMethodsOfListeners(downloadResponse.getE());
                }
            }
        };
    }

    public void setFileLoadRequest(FileLoadRequest fileLoadRequest) {
        this.fileLoadRequest = fileLoadRequest;
    }

}
