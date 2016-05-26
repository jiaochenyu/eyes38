package com.example.eyes38.utils;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eyes38.R;

/**
 * Created by jqchen on 2016/5/26.
 */
public class CartDialog extends Dialog {
    public CartDialog(Context context) {
        super(context);
    }

    public CartDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context mContext;
        private String message;
        private View mView;
        private String noButtonText;
        private String yesButtonText;
        private DialogInterface.OnClickListener NoButtonClickListener;
        private DialogInterface.OnClickListener YesButtonClikListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMesssage(int messsage) {
            this.message = (String) mContext.getText(messsage);
            return this;
        }

        public Builder setContentView(View view) {
            this.mView = view;
            return this;
        }

        public Builder setNoButtonClick(int noButtonText, DialogInterface.OnClickListener listener) {
            this.noButtonText = (String) mContext.getText(noButtonText);
            this.NoButtonClickListener = listener;
            return this;
        }

        public Builder setYesButtonClick(int yesButtonText, DialogInterface.OnClickListener listener) {
            this.yesButtonText = (String) mContext.getText(yesButtonText);
            this.YesButtonClikListener = listener;
            return this;
        }

        public Builder setNoButtonClick(String noButtonText, DialogInterface.OnClickListener listener) {
            this.noButtonText = noButtonText;
            this.NoButtonClickListener = listener;
            return this;
        }

        public Builder setYesButtonClick(String yesButtonText, DialogInterface.OnClickListener listener) {
            this.yesButtonText = yesButtonText;
            this.YesButtonClikListener = listener;
            return this;
        }

        public CartDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CartDialog dialog = new CartDialog(mContext, R.style.Cart_Dialog);
            View layout = inflater.inflate(R.layout.cart_dialog_delete, null);
            dialog.addContentView(layout, new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            //
            //((TextView)layout.findViewById(R.id.dialog_text)).setText();
            if (yesButtonText != null) {
                ((TextView) layout.findViewById(R.id.dialog_yes)).setText(yesButtonText);
                if (YesButtonClikListener != null) {
                    ((TextView) layout.findViewById(R.id.dialog_yes))
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    YesButtonClikListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.dialog_yes).setVisibility(
                            View.GONE);
                }
                // set the no button
                if (noButtonText != null) {
                    ((TextView) layout.findViewById(R.id.dialog_no))
                            .setText(noButtonText);
                    if (NoButtonClickListener != null) {
                        ((TextView) layout.findViewById(R.id.dialog_no))
                                .setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        NoButtonClickListener.onClick(dialog,
                                                DialogInterface.BUTTON_NEGATIVE);
                                    }
                                });
                    }
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.dialog_no).setVisibility(
                            View.GONE);
                }
            }

            if (message != null) {
                ((TextView) layout.findViewById(R.id.dialog_text)).setText(message);
            } else if (mView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((RelativeLayout) layout.findViewById(R.id.dialog_content))
                        .removeAllViews();
                ((RelativeLayout) layout.findViewById(R.id.dialog_content))
                        .addView(mView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
