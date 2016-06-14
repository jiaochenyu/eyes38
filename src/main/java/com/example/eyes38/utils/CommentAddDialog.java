package com.example.eyes38.utils;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.eyes38.R;

/**
 * Created by jqchen on 2016/6/13.
 */
public class CommentAddDialog extends Dialog {

    public CommentAddDialog(Context context) {
        super(context);
    }

    public CommentAddDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private EditText mEditText;
        private RatingBar mRatingBar;
        private Context mContext;
        private View mView;
        private String cancelButtonText, commitButtonText;
        private DialogInterface.OnClickListener cancelOnClickListener, commitOnClickListener;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setContentView(View view) {
            this.mView = view;
            return this;
        }

        public String getmEditText() {
            return mEditText.getText().toString();
        }

        public int getmRatingBar() {
            return (int) mRatingBar.getRating();
        }

        public Builder setCancelOnClickListener(String cancelButtonText, DialogInterface.OnClickListener listener) {
            this.cancelButtonText = cancelButtonText;
            this.cancelOnClickListener = listener;
            return this;
        }

        public Builder setCommitOnClickListener(String commitButtonText, DialogInterface.OnClickListener listener) {
            this.commitButtonText = commitButtonText;
            this.commitOnClickListener = listener;
            return this;
        }

        public CommentAddDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CommentAddDialog dialog = new CommentAddDialog(mContext, R.style.comment_Dialog);
            View layout = inflater.inflate(R.layout.comment_add_item, null);
            dialog.addContentView(layout, new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            if (cancelButtonText != null) {
                ((TextView) layout.findViewById(R.id.comment_add_cancel)).setText(cancelButtonText);
                if (cancelOnClickListener != null) {
                    ((TextView) layout.findViewById(R.id.comment_add_cancel))
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cancelOnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                } else {
                    layout.findViewById(R.id.comment_add_cancel).setVisibility(View.GONE);
                }
            }
            if (commitButtonText != null) {
                ((TextView) layout.findViewById(R.id.comment_add_commit)).setText(commitButtonText);
                if (commitOnClickListener != null) {
                    ((TextView) layout.findViewById(R.id.comment_add_commit))
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    commitOnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                } else {
                    layout.findViewById(R.id.comment_add_commit).setVisibility(View.GONE);
                }
            }
            this.mEditText = (EditText) layout.findViewById(R.id.comment_add_content);
            this.mRatingBar = (RatingBar) layout.findViewById(R.id.comment_add_rating);
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
