package com.mindvally.mindvellytest.adapters;//package com.mindvally.imagelibrary.views.adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindvally.imageloaderlibrary.MVLoader;

import com.mindvally.imageloaderlibrary.dao.FileResponse;
import com.mindvally.imageloaderlibrary.listener.FileRequestListener;
import com.mindvally.imageloaderlibrary.request.FileLoadRequest;
import com.mindvally.mindvellytest.R;
import com.mindvally.mindvellytest.model.PinDetails;

import java.io.File;
import java.util.List;

import static android.content.ContentValues.TAG;


public class PinAdapter extends RecyclerView.Adapter<PinAdapter.PinView> {

    private Context context;
    List<PinDetails> pinsList;


    public PinAdapter(Context context,List<PinDetails> list) {
        this.context = context;
        this.pinsList = list;

    }

    @Override
    public PinView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_item_view, parent, false);
        PinView masonryView = new PinView(layoutView);
        return masonryView;
    }

    @Override
    public void onBindViewHolder(final PinView holder, int position) {

           PinDetails details = pinsList.get(position);

        MVLoader.with(context)
                .load(details.getUser().getProfileImage().getLarge())
                .fromDirectory("mv", MVLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        Bitmap bitmap = BitmapFactory.decodeFile(response.getDownloadedFile().getPath());
                        holder.imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        Log.d(TAG, "onError: " + t.getMessage());
                    }
                });


    }

    @Override
    public int getItemCount() {
        return pinsList.size();
    }

    class PinView extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public PinView(View itemView) {
            super(itemView);
           imageView = itemView.findViewById(R.id.img);

        }
    }


}
