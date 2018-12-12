package com.lanmei.screenshot.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.lanmei.screenshot.R;
import com.lanmei.screenshot.adapter.GiveUpReasonAdapter;
import com.lanmei.screenshot.bean.GiveUpReasonListBean;
import com.lanmei.screenshot.view.ChangePhoneView;
import com.xson.common.widget.FormatTextView;

import java.util.List;

/**
 * Dialog工具类
 * Created by benio on 2015/10/11.
 */
public class AKDialog {

    public static AlertDialog.Builder getDialog(Context context) {
        return new AlertDialog.Builder(context);
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg) {
        return getMessageDialog(context, null, msg, null);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg) {
        return getMessageDialog(context, title, msg, null);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg, DialogInterface.OnClickListener okListener) {
        return getMessageDialog(context, null, msg, okListener);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton(R.string.sure, okListener);
        return builder;
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, null, msg, okListener, null);
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, title, msg, okListener, null);
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton(R.string.sure, okListener);
        builder.setNegativeButton(R.string.cancel, cancelListener);
        return builder;
    }

    /**
     * 列表对话框
     */
    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, null, arrays, onClickListener);
    }

    /**
     * 列表对话框
     */
    public static AlertDialog.Builder getSelectDialog(Context context, String title,
                                                      String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        return builder;
    }

    /**
     * 单选对话框
     */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, null, arrays, selectIndex, onClickListener);
    }

    /**
     * 单选对话框
     */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        // builder.setNegativeButton("取消", null);
        return builder;
    }

    /**
     * 拍照、选择相册底部弹框提示
     *
     * @param context
     * @param activity
     * @param listener
     */
    public static void showBottomListDialog(Context context, Activity activity, final AlbumDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(context).inflate(R.layout.album_dialog_layout, null);
        Button choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        Button takePhoto = (Button) inflate.findViewById(R.id.takePhoto);
        Button cancel = (Button) inflate.findViewById(R.id.btn_cancel);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {//拍照
                    listener.photograph();
                }
                dialog.cancel();
            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//相册
                if (listener != null) {
                    listener.photoAlbum();
                }
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//取消
                dialog.cancel();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        lp.y = 20;
        lp.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    public interface AlbumDialogListener {

        void photograph();//拍照

        void photoAlbum();//相册
    }


    public static void getAlertDialog(Context context, String content, DialogInterface.OnClickListener l) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(content)
                .setPositiveButton(R.string.sure, l)
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(false).create();
        dialog.show();
    }


    public static AlertDialog getChangePhoneDialog(Context context, String title, String phone, String type, final ChangePhoneListener l) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ChangePhoneView view = (ChangePhoneView) View.inflate(context, R.layout.dialog_change_phone, null);
        view.setTitle(title);
        view.setPhone(phone);
        view.setType(type);
        view.setChangePhoneListener(new ChangePhoneView.ChangePhoneListener() {
            @Override
            public void succeed(String newPhone) {
                if (l != null) {
                    l.succeed(newPhone);
                }
            }

            @Override
            public void unBound() {
                if (l != null) {
                    l.unBound();
                }
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        return dialog;
    }


    public interface ChangePhoneListener {

        void succeed(String newPhone);//更换手机号

        void unBound();//解绑银行卡
    }


    public static void getGiveUpListDialog(Context context, List<GiveUpReasonListBean.ReasonBean> list, final GiveUpReasonAdapter.GiveUpReasonListener l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_give_up_reason, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        GiveUpReasonAdapter adapter = new GiveUpReasonAdapter(context);
        adapter.setData(list);
        recyclerView.setAdapter(adapter);
        adapter.setGiveUpReasonListener(new GiveUpReasonAdapter.GiveUpReasonListener() {
            @Override
            public void giveUpReason(GiveUpReasonListBean.ReasonBean bean) {
                if (l != null) {
                    l.giveUpReason(bean);
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }


    /**
     * @param context
     * @param price   佣金
     * @param l
     * @return
     */
    public static void getOrderDialog(Context context, String price, final String orderNum, final ReceiveOrderListener l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.receive_order_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        TextView noTv = (TextView) view.findViewById(R.id.no_tv);
        TextView yesTv = (TextView) view.findViewById(R.id.yes_tv);
        FormatTextView commissionTv = (FormatTextView) view.findViewById(R.id.commission_tv);//佣金
        final AlertDialog dialog = builder.create();
        noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.no(orderNum);
                }
                dialog.cancel();
            }
        });
        yesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.yes(orderNum);
                }
                dialog.cancel();
            }
        });
        commissionTv.setFormatText(price);
        dialog.show();
    }


    public interface ReceiveOrderListener {

        void yes(String order_num);//接单

        void no(String order_num);//巨单
    }

}
