package shawn.cn.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import shawn.cn.baselibrary.R;

/**
 * Created by Shawn on 2017/7/10.
 */

public class MyDialog extends Dialog{

    private DialogController mDialogController;

    public MyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mDialogController = new DialogController(this,getWindow());
    }

    public <T extends View> T getView(int id){
       return mDialogController.getView(id);
    }

    public Dialog setText(int id, CharSequence text){
        mDialogController.setText(id,text);
        return this;
    }

    public Dialog setClickListener(int id, View.OnClickListener listener){
        mDialogController.setClickListener(id,listener);
        return this;
    }


    public static class Builder{

        private DialogController.AlertParams params;

        private boolean sCancelable;

        private Dialog.OnKeyListener mOnKeylistener;

        private Dialog.OnDismissListener mOnDismissListener;

        private Dialog.OnCancelListener mOnCancelListener;

        public Builder(Context context){
            params = new DialogController.AlertParams(context, R.style.MyDialogTheme);
        }

        public Builder(Context context,int themeId){
            params = new DialogController.AlertParams(context, R.style.MyDialogTheme);
        }

        public MyDialog.Builder setView(View view) {
            params.mView = view;
            params.mViewLayoutResId = 0;
            return this;
        }

        public MyDialog.Builder setView(int layoutResId) {
            params.mView = null;
            params.mViewLayoutResId = layoutResId;
            return this;
        }

        public MyDialog.Builder setGravity(int gravity){
            params.mGravity = gravity;
            return this;
        }

        public MyDialog.Builder fillWidth(){
            params.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public MyDialog.Builder fromBottom(boolean sAnimate){
            if(sAnimate){
                params.mAnimationRes = R.style.dialog_from_bottom;
            }
            params.mGravity = Gravity.BOTTOM;
            return this;
        }

        public MyDialog.Builder setWidthAndHeight(int width, int height){
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        public MyDialog.Builder addDefaultAnimation(){
            params.mAnimationRes = R.style.dialog_from_bottom;
            return this;
        }

        public MyDialog.Builder setAnimation(int animations){
            params.mAnimationRes = animations;
            return this;
        }

        public MyDialog.Builder setCancelable(boolean sCancelable) {
            this.sCancelable = sCancelable;
            return this;
        }

        public MyDialog.Builder setOnKeylistener(OnKeyListener mOnKeylistener) {
            this.mOnKeylistener = mOnKeylistener;
            return this;
        }

        public MyDialog.Builder setOnDismissListener(OnDismissListener mOnDismissListener) {
            this.mOnDismissListener = mOnDismissListener;
            return this;
        }

        public MyDialog.Builder setOnCancelListener(OnCancelListener mOnCancelListener) {
            this.mOnCancelListener = mOnCancelListener;
            return this;
        }

        public MyDialog.Builder setText(int viewId,CharSequence text){
            params.mTextArray.put(viewId,text);
            return this;
        }

        public MyDialog.Builder setClickListener(int viewId, View.OnClickListener listener){
            params.mClickArray.put(viewId,listener);
            return this;
        }

        public MyDialog create(){
            MyDialog dialog = new MyDialog(params.mContext, params.mThemeResId);
            params.apply(dialog.mDialogController);
            dialog.setCancelable(params.sCancelable);
            if(params.sCancelable){
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(params.mOnCancelListener);
            dialog.setOnDismissListener(params.mOnDismissListener);
            dialog.setOnKeyListener(params.mOnKeyListener);
            return dialog;
        }

        public MyDialog show(){
            MyDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }
}
