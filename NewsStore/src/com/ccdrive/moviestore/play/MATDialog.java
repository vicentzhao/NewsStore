package com.ccdrive.moviestore.play;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdrive.moviestore.R;

class MATDialog extends Dialog {

		TextView msgText, progressText;
		ImageView logo;

		public MATDialog(Context context, int theme) {
			super(context, theme);
			initContentView();
		}

		private View initContentView() {
			View v = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog, null);
			msgText = (TextView) v.findViewById(R.id.msg);
			progressText = (TextView) v.findViewById(R.id.progress);
			logo = (ImageView) v.findViewById(R.id.dialoglogo);
			setContentView(v);
			return v;
		}

		@Override
		public void hide() {
			if (isShowing()) {
				dismiss();
			}
		}

		void setMessage(String msg) {
			if (msg == null)
				return;
			msgText.setText(msg);
		}

		void setProgress(int progress) {
			if (progress < 0)
				return;
			progressText.setText(progress + "%");
		}

		void setLogo(int resId) {
			logo.setImageResource(resId);
		}

		void setLogo(Bitmap bm) {
			if (bm == null)
				return;
			logo.setImageBitmap(bm);
		}

		void setLogo(Drawable drawable) {
			if (drawable == null)
				return;
			logo.setImageDrawable(drawable);
		}

		public void showMessage(String msg) {
			setMessage(msg);
			show();
		}

	}