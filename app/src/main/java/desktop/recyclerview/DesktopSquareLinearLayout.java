package desktop.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by ening on 30.01.18.
 */

public class DesktopSquareLinearLayout extends LinearLayout {
    public DesktopSquareLinearLayout(Context context) {
        super(context);
    }

    public DesktopSquareLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DesktopSquareLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,widthMeasureSpec+60);
    }
}
