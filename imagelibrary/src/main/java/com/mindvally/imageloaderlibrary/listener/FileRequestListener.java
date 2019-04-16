package com.mindvally.imageloaderlibrary.listener;

import com.mindvally.imageloaderlibrary.dao.FileResponse;
import com.mindvally.imageloaderlibrary.request.FileLoadRequest;

public abstract class FileRequestListener<T> {
    public void onStatusChange(int status) { }

    public abstract void onLoad(FileLoadRequest request, FileResponse<T> response);

    public abstract void onError(FileLoadRequest request, Throwable t);
}
