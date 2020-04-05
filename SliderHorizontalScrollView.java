package com.ownersbox.fsp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import java.util.ArrayList;

/**
 * created by Jason Ji
 * on 2020-02-13
 */
public class SliderHorizontalScrollView extends HorizontalScrollView {


    private int subChildCount = 0;
    private ViewGroup firstChild = null;  
    private int downX = 0; 
    private int currentPage = 0; 
    private ArrayList<Integer> viewList = new ArrayList<Integer>(); 


    public SliderHorizontalScrollView(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SliderHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SliderHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildInfo();
    }


    public void getChildInfo() {

        firstChild = (ViewGroup) getChildAt(0);
        if (firstChild != null) {
            subChildCount = firstChild.getChildCount();
            for (int i = 0; i < subChildCount; i++) {
                if (((View) firstChild.getChildAt(i)).getWidth() > 0) {
                    viewList.add(((View) firstChild.getChildAt(i)).getLeft());
                }
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                
                // if slided lager than 1/6 screen width, then slide to next page, or else slide back
            case MotionEvent.ACTION_CANCEL: {
                if (Math.abs((ev.getX() - downX)) > getWidth() / 6) {
                    if (ev.getX() - downX > 0) {
                        smoothScrollToPrePage();
                    } else {
                        smoothScrollToNextPage();
                    }
                } else {
                    smoothScrollToCurrent();
                }
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }


    private void smoothScrollToCurrent() {
        smoothScrollTo(viewList.get(currentPage)-10, 0);
    }


    private void smoothScrollToNextPage() {
        if (currentPage < subChildCount - 1) {
            currentPage++;
            smoothScrollTo(viewList.get(currentPage)-10, 0);
            if(pageIndexChangeListener!=null){
                pageIndexChangeListener.OnPageChange(currentPage);
            }
        }
    }

    private void smoothScrollToPrePage() {
        if (currentPage > 0) {
            currentPage--;
            smoothScrollTo(viewList.get(currentPage)-10, 0);
            if(pageIndexChangeListener!=null){
                pageIndexChangeListener.OnPageChange(currentPage);
            }
        }
    }

    // used for page changing call back
    public interface PageIndexChangeListener {

        void OnPageChange(int index);
    }

    PageIndexChangeListener pageIndexChangeListener;

    public void setPageIndexChangeListener(PageIndexChangeListener pageIndexChangeListener){
        this.pageIndexChangeListener = pageIndexChangeListener;
    }


    public void nextPage() {
        smoothScrollToNextPage();
    }


    public void prePage() {
        smoothScrollToPrePage();
    }

    
    public boolean goToPage(int page) {
        if (page >= 0 && page <= subChildCount - 1) {
            smoothScrollTo(viewList.get(page), 0);
            if(pageIndexChangeListener!=null){
                pageIndexChangeListener.OnPageChange(page);
            }
            currentPage = page;
            return true;
        }
        return false;
    }

}
