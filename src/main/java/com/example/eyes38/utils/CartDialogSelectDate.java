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
public class CartDialogSelectDate extends Dialog {
    public CartDialogSelectDate(Context context) {
        super(context);
    }

    public CartDialogSelectDate(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context mContext;
        private String message;
        private View mView;
        private String day1, day2, day3, day4, day5, day6, day7;
        private OnClickListener Day1ButtonClikListener;
        private OnClickListener Day2ButtonClikListener;
        private OnClickListener Day3ButtonClikListener;
        private OnClickListener Day4ButtonClikListener;
        private OnClickListener Day5ButtonClikListener;
        private OnClickListener Day6ButtonClikListener;
        private OnClickListener Day7ButtonClikListener;


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

        public Builder setDay1ButtonClick(String day1, DialogInterface.OnClickListener listener) {
            this.day1 = day1;
            this.Day1ButtonClikListener = listener;
            return this;
        }

        public Builder setDay2ButtonClick(String day2, DialogInterface.OnClickListener listener) {
            this.day2 = day2;
            this.Day2ButtonClikListener = listener;
            return this;
        }

        public Builder setDay3ButtonClick(String day3, DialogInterface.OnClickListener listener) {
            this.day3 = day3;
            this.Day3ButtonClikListener = listener;
            return this;
        }

        public Builder setDay4ButtonClick(String day4, DialogInterface.OnClickListener listener) {
            this.day4 = day4;
            this.Day4ButtonClikListener = listener;
            return this;
        }

        public Builder setDay5ButtonClick(String day5, DialogInterface.OnClickListener listener) {
            this.day5 = day5;
            this.Day5ButtonClikListener = listener;
            return this;
        }

        public Builder setDay6ButtonClick(String day6, OnClickListener listener) {
            this.day6 = day6;
            this.Day6ButtonClikListener = listener;
            return this;
        }

        public Builder setDay7ButtonClick(String day7, OnClickListener listener) {
            this.day7 = day7;
            this.Day7ButtonClikListener = listener;
            return this;
        }


        public CartDialogSelectDate create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CartDialogSelectDate dialog = new CartDialogSelectDate(mContext, R.style.Cart_Dialog);
            View layout = inflater.inflate(R.layout.cart_dialog_selectdate, null);
            dialog.addContentView(layout, new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            //
            //((TextView)layout.findViewById(R.id.dialog_text)).setText();
            //设置第一天的按钮
            if (day1 != null) {
                ((TextView) layout.findViewById(R.id.dialog_day1)).setText(day1);
                if (Day1ButtonClikListener != null) {
                    ((TextView) layout.findViewById(R.id.dialog_day1))
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Day1ButtonClikListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.dialog_day1).setVisibility(
                            View.GONE);
                }
            }
            // 设置第二天的按钮
            if (day2 != null) {
                ((TextView) layout.findViewById(R.id.dialog_day2))
                        .setText(day2);
                if (Day2ButtonClikListener != null) {
                    ((TextView) layout.findViewById(R.id.dialog_day2))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Day2ButtonClikListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.dialog_day2).setVisibility(
                            View.GONE);
                }
            }
            // 设置第三天的按钮
            if (day3 != null) {
                ((TextView) layout.findViewById(R.id.dialog_day3))
                        .setText(day3);
                if (Day3ButtonClikListener != null) {
                    ((TextView) layout.findViewById(R.id.dialog_day3))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Day3ButtonClikListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.dialog_day3).setVisibility(
                            View.GONE);
                }
            }
            // 设置第四天的按钮
            if (day4 != null) {
                ((TextView) layout.findViewById(R.id.dialog_day4))
                        .setText(day4);
                if (Day4ButtonClikListener != null) {
                    ((TextView) layout.findViewById(R.id.dialog_day4))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Day4ButtonClikListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.dialog_day4).setVisibility(
                            View.GONE);
                }
            }
            // 设置第五天的按钮
            if (day5 != null) {
                ((TextView) layout.findViewById(R.id.dialog_day5))
                        .setText(day5);
                if (Day5ButtonClikListener != null) {
                    ((TextView) layout.findViewById(R.id.dialog_day5))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Day5ButtonClikListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.dialog_day5).setVisibility(
                            View.GONE);
                }
            }
            // 设置第六天的按钮
            if (day6 != null) {
                ((TextView) layout.findViewById(R.id.dialog_day6))
                        .setText(day2);
                if (Day6ButtonClikListener != null) {
                    ((TextView) layout.findViewById(R.id.dialog_day6))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Day6ButtonClikListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.dialog_day6).setVisibility(
                            View.GONE);
                }
            }
            // 设置第七天的按钮
            if (day7 != null) {
                ((TextView) layout.findViewById(R.id.dialog_day7))
                        .setText(day2);
                if (Day7ButtonClikListener != null) {
                    ((TextView) layout.findViewById(R.id.dialog_day7))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Day7ButtonClikListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.dialog_day7).setVisibility(
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
                        .addView(mView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
