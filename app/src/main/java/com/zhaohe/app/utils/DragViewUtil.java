package com.zhaohe.app.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 这个工具可以使任何一个view进行拖动。
 */

public class DragViewUtil {
    public static void drag(View v, String name) {
        drag(v, 0, name);
    }

    /**
     * 拖动View方法
     *
     * @param v     view
     * @param delay 延迟
     */
    public static void drag(View v, long delay, String name) {

        v.setOnTouchListener(new TouchListener(delay, name));
    }

    private static class TouchListener implements View.OnTouchListener {
        private float downX;
        private float downY;
        private long downTime;
        private long delay;
        private String name;
        int l, t, r, b;

        private TouchListener() {
        }

        private TouchListener(long delay, String name) {
            this.delay = delay;
            this.name = name;

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    downTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (System.currentTimeMillis() - downTime >= delay) {
                        final float xDistance = event.getX() - downX;
                        final float yDistance = event.getY() - downY;
                        if (xDistance != 0 && yDistance != 0) {
                            l = (int) (v.getLeft() + xDistance);
                            r = (int) (v.getRight() + xDistance);
                            t = (int) (v.getTop() + yDistance);
                            b = (int) (v.getBottom() + yDistance);
                            v.layout(l, t, r, b);
                            ViewGroup.LayoutParams p = v.getLayoutParams();
//                            ToastUtil.print(name+"左边" + l + "右边" + r + "上边" + r + "下边" + b);

                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    ToastUtil.print(name + "左边" + l + "右边" + r + "上边" + r + "下边" + b);
                    SPUtils.put(v.getContext(), name + "location", l + "," + t + "," + r + "," + b);

                    break;
            }

            return false;
        }

    }

    public static void set(View v, String name) {
        v.requestFocus();
        String Option = (String) SPUtils.get(v.getContext(), name + "location", "100,100,100,100");
        String[] s = Option.split("\\,");

        if (SPUtils.contains(v.getContext(), name + "location"))

            v.layout(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]));

    }
}
