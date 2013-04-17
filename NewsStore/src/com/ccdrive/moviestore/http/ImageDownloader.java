package com.ccdrive.moviestore.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.HttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ccdrive.moviestore.R;


/**
 * 
 *
 */
public class ImageDownloader {
	
	private Context mContext;
	private ImageCache imageCache;
	private FileCache fileCache;
	
	public ImageDownloader(Context context) {
		this.mContext = context;
		imageCache = new ImageCache();
		fileCache = new FileCache(context);
	}
	
	/**
     * Download the specified image from the Internet and binds it to the provided ImageView. The
     * binding is immediate if the image is found in the cache and will be done asynchronously
     * otherwise. A null bitmap will be associated to the ImageView if an error occurs.
     *
     * @param url The URL of the image to download.
     * @param imageView The ImageView to bind the downloaded image to.
     */
	public void download(String url, ImageView imageView) {
		Bitmap bitmap = imageCache.getBitmapFromCache(url);
		imageCache.resetPurgeTimer();
		if(bitmap == null) {
			forceDownload(url, imageView);
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setBackgroundDrawable(null);
            imageView.setImageBitmap(bitmap);
		}
	}
	
	 /**
     * Same as download but the image is always downloaded and the cache is not used.
     * Kept private at the moment as its interest is not clear.
     */
	private void forceDownload(String url, ImageView imageView) {
		if (url == null) {
//            imageView.setImageDrawable(null);   //TODO 鏇存敼涓洪粯璁ゅ浘鐗�?           return;
        }
		if(cancelPotentialDownload(url, imageView)) {
			 BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
//			 task = new BitmapDownloaderTask(imageView);
             DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
             imageView.setImageDrawable(downloadedDrawable);
             if(downloadedDrawable != null){
            	 imageView.setBackgroundDrawable(null);
             }
             task.execute(url);
		}
	}
	
	/**
	 * @param url
	 * @param imageView
	 * @return
	 */
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
		
		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
	}
	
	
	/**
	 * @param url
	 * @return
	 */
	public Bitmap downloadBitmap(String url) {
		
		//try to get image from file cache
		File file = fileCache.getFromFileCache(url);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new FlushedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e1) {
			bitmap = null;
		}
		if(bitmap != null) {
			return bitmap;
		}
		//end of try
		try {
			  URL url_ = new URL(url);
			  HttpURLConnection conn = (HttpURLConnection) url_.openConnection();
			  conn.setRequestMethod("GET");
			  conn.setConnectTimeout(6 * 1000);
			  if(conn.getResponseCode()==200){
			   InputStream inputStream=conn.getInputStream();
			   fileCache.addToFileCache(url, inputStream);
			   return BitmapFactory.decodeStream(new FlushedInputStream(new FlushedInputStream(new FileInputStream(file))));
			  }
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		final HttpClient httpClient = AndroidHttpClient.newInstance("image_downloader"); 
		return null;
	} // end of downloadBitmap
	
	
	/*
     * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
     */
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    } // end of FlushedInputStream
    
    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

		private String url;
		private WeakReference<ImageView> imageViewWeakReference;
		
	    public BitmapDownloaderTask(ImageView imageView) {
			imageViewWeakReference = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0]; 
			return downloadBitmap(url);
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if(isCancelled()) {
				bitmap = null;
			}
		
			//add bitmap to cache
			imageCache.addBitmapToCache(url, bitmap);
			
			if(imageViewWeakReference != null) {
				ImageView imageView = imageViewWeakReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				if(this == bitmapDownloaderTask) {
					if(bitmap != null){
						imageView.setImageBitmap(bitmap);
					}else{
						imageView.setImageResource(R.drawable.grid_item);
					}
				}
			}
		}
	} // end of BitmapDownloaderTask
	
	/**
     * A fake Drawable that will be attached to the imageView while the download is in progress.
     *
     * <p>Contains a reference to the actual download task, so that a download task can be stopped
     * if a new binding is required, and makes sure that only the last started download process can
     * bind its result, independently of the download finish order.</p>
     */
    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
//            super(Color.BLACK);
            bitmapDownloaderTaskReference =
                new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    } // end of DownloadedDrawable

}
