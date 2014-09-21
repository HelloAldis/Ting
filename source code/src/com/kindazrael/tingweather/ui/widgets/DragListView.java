
package com.kindazrael.tingweather.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.kindazrael.tingweather.R;

public class DragListView extends ListView {

    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;


    private ImageView dragImageView;
    private int startPosition;
    private int dragPosition;
    private int lastPosition;


    private int dragPoint;
    private int dragOffset;

    private int upScrollBounce;
    private int downScrollBounce;

    private boolean isLock;

    private final ItemInfo mDragItemInfo;
    private boolean isMoving = false;
    private final boolean isDragItemMoving = false;

    private int mItemVerticalSpacing = 0;

    private boolean bHasGetSapcing = false;

    public static final int MSG_DRAG_STOP = 0x1001;
    public static final int MSG_DRAG_MOVE = 0x1002;
    private static final int ANIMATION_DURATION = 200;

    Handler mHandler = new Handler() {

        @Override
		public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DRAG_STOP:
                    stopDrag();
                    onDrop(msg.arg1);
                    break;
                case MSG_DRAG_MOVE:
                    onDrag(msg.arg1);
                    break;
                default:
                    break;
            }
        };
    };

    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    public DragListView (Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mDragItemInfo = new ItemInfo();
        init();
    }

    private void init() {
        windowManager = (WindowManager) getContext().getSystemService("window");
    }

    private void getSpacing() {
        bHasGetSapcing = true;

        upScrollBounce = getHeight() / 3;
        downScrollBounce = getHeight() * 2 / 3;

        int [] tempLocation0 = new int [2];
        int [] tempLocation1 = new int [2];

        ViewGroup itemView0 = (ViewGroup) getChildAt(0);
        ViewGroup itemView1 = (ViewGroup) getChildAt(1);

        if (itemView0 != null) {
            itemView0.getLocationOnScreen(tempLocation0);
        } else {
            return;
        }

        if (itemView1 != null) {
            itemView1.getLocationOnScreen(tempLocation1);
            mItemVerticalSpacing = Math.abs(tempLocation1[1] - tempLocation0[1]);
        } else {
            return;
        }
    }

    @ Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && !isLock && !isMoving && !isDragItemMoving) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            lastPosition = startPosition = dragPosition = pointToPosition(x, y);
            if (dragPosition == AdapterView.INVALID_POSITION) {
                return super.onInterceptTouchEvent(ev);
            }
            if (false == bHasGetSapcing) {
                getSpacing();
            }

            ViewGroup dragger = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());

            DragListAdapter adapter = (DragListAdapter) getAdapter();

            mDragItemInfo.obj = adapter.getItem(dragPosition - getFirstVisiblePosition());

            dragPoint = y - dragger.getTop();
            dragOffset = (int) (ev.getRawY() - y);

            View draggerIcon = dragger.findViewById(R.id.drag_list_item_image);

            if (draggerIcon != null && x > draggerIcon.getLeft() - 20) {

                dragger.destroyDrawingCache();
                dragger.setDrawingCacheEnabled(true);
                dragger.setBackgroundColor(0x55555555);
                Bitmap bm = Bitmap.createBitmap(dragger.getDrawingCache(true));
                hideDropItem();
                adapter.setInvisiblePosition(startPosition);
                adapter.notifyDataSetChanged();
                startDrag(bm, y);
                isMoving = false;
            }
            adapter.copyList();
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public Animation getScaleAnimation() {
        Animation scaleAnimation = new ScaleAnimation(0.0f, 0.0f, 0.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    private void hideDropItem() {
        final DragListAdapter adapter = (DragListAdapter) this.getAdapter();
        adapter.showDropItem(false);
    }

    private float xPostion = 0;
    @ Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (dragImageView != null && dragPosition != INVALID_POSITION && !isLock) {

            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    int upY = (int) ev.getY();
                    stopDrag();
                    Log.i("milo", " " + (ev.getX() - xPostion));
                    if (ev.getX() - xPostion >= 50) {
                        onDelete(startPosition);
                    }
                    onDrop(upY);
                    xPostion = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveY = (int) ev.getY();
                    onDrag(moveY);
                    testAnimation(moveY);
                    break;
                case MotionEvent.ACTION_DOWN:
                    xPostion = ev.getX();
                    break;
                default:
                    break;
            }
            return true;
        }

        return super.onTouchEvent(ev);
    }

    private boolean isSameDragDirection = true;
    private int lastFlag = -1; // -1,0 == down,1== up

    private void onChangeCopy(int last, int current) {
        DragListAdapter adapter = (DragListAdapter) getAdapter();
        if (last != current) {
            adapter.exchangeCopy(last, current);
        }

    }
    
    private void onDelete(int postion) {
        DragListAdapter adapter = (DragListAdapter) getAdapter();
        if (postion > 0) {
            adapter.delete(postion);
            this.setAdapter(adapter);
        }
    }

    private void testAnimation(int y) {
        final DragListAdapter adapter = (DragListAdapter) getAdapter();
        int tempPosition = pointToPosition(0, y);
        if (tempPosition == INVALID_POSITION || tempPosition == lastPosition) {
            return;
        }
        dragPosition = tempPosition;

        onChangeCopy(lastPosition, dragPosition);
        int MoveNum = tempPosition - lastPosition;
        int count = Math.abs(MoveNum);
        for (int i = 1; i <= count; i++) {
            int xAbsOffset, yAbsOffset;
            if (MoveNum > 0) {
                if (lastFlag == -1) {
                    lastFlag = 0;
                    isSameDragDirection = true;
                }
                if (lastFlag == 1) {
                    lastFlag = 0;
                    isSameDragDirection = !isSameDragDirection;
                }
                if (isSameDragDirection) {
                    holdPosition = lastPosition + 1;
                } else {
                    if (startPosition < tempPosition) {
                        holdPosition = lastPosition + 1;
                        isSameDragDirection = !isSameDragDirection;
                    } else {
                        holdPosition = lastPosition;
                    }
                }
                xAbsOffset = 0;
                yAbsOffset = -mItemVerticalSpacing;
                lastPosition++;
            } else {
                if (lastFlag == -1) {
                    lastFlag = 1;
                    isSameDragDirection = true;
                }
                if (lastFlag == 0) {
                    lastFlag = 1;
                    isSameDragDirection = !isSameDragDirection;
                }
                if (isSameDragDirection) {
                    holdPosition = lastPosition - 1;
                } else {
                    if (startPosition > tempPosition) {
                        holdPosition = lastPosition - 1;
                        isSameDragDirection = !isSameDragDirection;
                    } else {
                        holdPosition = lastPosition;
                    }
                }
                xAbsOffset = 0;
                yAbsOffset = mItemVerticalSpacing;
                lastPosition--;
            }

            adapter.setHeight(mItemVerticalSpacing);
            adapter.setLastFlag(lastFlag);

            ViewGroup moveView = (ViewGroup) getChildAt(holdPosition - getFirstVisiblePosition());

            Animation animation;
            if (isSameDragDirection) {
                animation = getFromSelfAnimation(xAbsOffset, yAbsOffset);
            } else {
                animation = getToSelfAnimation(xAbsOffset, -yAbsOffset);
            }
            moveView.startAnimation(animation);
        }
    }

    private void onDrop(int x, int y) {
        final DragListAdapter adapter = (DragListAdapter) getAdapter();
        adapter.setInvisiblePosition(-1);
        adapter.showDropItem(true);
        adapter.notifyDataSetChanged();

    }

    private void startDrag(Bitmap bm, int y) {

        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = y - dragPoint + dragOffset;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        windowParams.windowAnimations = 0;

        windowParams.alpha = 0.8f;
        windowParams.format = PixelFormat.TRANSLUCENT;

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bm);

        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;
    }

    /**
     * �϶�ִ�У���Move������ִ��
     * 
     * @param y
     */
    public void onDrag(int y) {
        int drag_top = y - dragPoint;
        if (dragImageView != null && drag_top >= 0) {
            windowParams.alpha = 1.0f;
            windowParams.y = y - dragPoint + dragOffset;
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
        doScroller(y);

    }

    public void doScroller(int y) {
        if (y < upScrollBounce) {
        } else if (y > downScrollBounce) {
        } else {
        }

        //View view = getChildAt(dragPosition - getFirstVisiblePosition());
        //setSelectionFromTop(dragPosition, view.getTop() + current_Step);

    }

    public void stopDrag() {
        isMoving = false;
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
        isSameDragDirection = true;
        lastFlag = -1;
        DragListAdapter adapter = (DragListAdapter) getAdapter();
        adapter.setLastFlag(lastFlag);
        adapter.pastList();
    }

    public void onDrop(int y) {
        onDrop(0, y);
    }

    private int holdPosition;

    public Animation getFromSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getToSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.ABSOLUTE, x, Animation.RELATIVE_TO_SELF, 0,
                Animation.ABSOLUTE, y, Animation.RELATIVE_TO_SELF, 0);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getAbsMoveAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getAnimation(int fromY, int toY) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, fromY, Animation.ABSOLUTE, toY);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getAbsMoveAnimation2(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.ABSOLUTE, x, Animation.RELATIVE_TO_SELF, 0,
                Animation.ABSOLUTE, y, Animation.RELATIVE_TO_SELF, 0);
        go.setInterpolator(new AccelerateDecelerateInterpolator());

        go.setFillAfter(true);
        go.setDuration(ANIMATION_DURATION);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

}
