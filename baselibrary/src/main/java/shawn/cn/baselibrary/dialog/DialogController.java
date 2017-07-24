package shawn.cn.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Shawn on 2017/7/10.
 */

 class DialogController {

    private MyDialog mDialog;

    private Window mWindow;

    private DialogViewHelper mViewHelper;

    public void setViewHelper(DialogViewHelper mViewHelper) {
        this.mViewHelper = mViewHelper;
    }

    public DialogController(MyDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public MyDialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public <T extends View> T getView(int id) {
        return mViewHelper.getView(id);
    }

    public void setClickListener(int id, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(id,listener);
    }

    public void setText(int id, CharSequence text) {
        mViewHelper.setText(id,text);
    }

    public static class AlertParams{

        public Context mContext;

        public int mThemeResId;

        public boolean sCancelable = true;

        public Dialog.OnDismissListener mOnDismissListener;

        public Dialog.OnCancelListener mOnCancelListener;

        public Dialog.OnKeyListener mOnKeyListener;

        public View mView;

        public int mViewLayoutResId;

        public SparseArray<CharSequence> mTextArray = new SparseArray<>();

        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();

        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;

        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        public int mGravity = Gravity.NO_GRAVITY;

        public int mAnimationRes = 0;

        public AlertParams(Context mContext, int mThemeResId) {
            this.mContext = mContext;
            this.mThemeResId = mThemeResId;
        }

        public void apply(DialogController dialogController) {
            if(mOnKeyListener != null ) dialogController.getDialog().setOnKeyListener(mOnKeyListener);
            DialogViewHelper viewHelper = null;
            if(mViewLayoutResId != 0 ){
                viewHelper = new DialogViewHelper(mContext,mViewLayoutResId,
                        (ViewGroup) dialogController.getWindow().getDecorView());
            }
            if(mView != null){
                viewHelper = new DialogViewHelper(mView);
            }
            if(viewHelper == null) throw new IllegalArgumentException("please setContentView");
            dialogController.setViewHelper(viewHelper);
            MyDialog dialog = dialogController.getDialog();
            dialog.setContentView(viewHelper.getContentView());
            //
            for (int i = 0; i < mTextArray.size(); i++) {
                dialogController.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
            }
            //
            for (int i = 0; i < mClickArray.size(); i++) {
                dialogController.setClickListener(mClickArray.keyAt(i),mClickArray.valueAt(i));
            }
            //
            Window window = dialogController.getWindow();
            window.setGravity(mGravity);
            if(mAnimationRes != 0 ){
                window.setWindowAnimations(mAnimationRes);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }
}
