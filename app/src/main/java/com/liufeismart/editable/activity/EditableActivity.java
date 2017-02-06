package com.liufeismart.editable.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.liufeismart.editable.EditFramePanel;
import com.liufeismart.editable.R;

/**
 *
 * 首先三层 底层:图片 , 中层:EditFramePanel, 上层:按钮区
 * EditFramePanel
 * EditableLayout 是 包括了一个EditTextLayout, 两个EditableImageButton(一个负责删除,一个负责旋转)
 * EditFramePanel 有个mCurrentEdit(EditableLayout对象, 选中的Editablelayout对象
 * EditableLayout的几种状态
 * init(虚线, X Button, Drag Button) invalidate(无效), move, drag, editable
 * invalidate--> init 双击EditTextLayout
 * init-->move EditTextLayout ACTION_MOVE 时发生, EditFramePanel GestureDetector.onScroll move状态下
 * init-->drag EditableImageButton ACTION_DOWN 时发生, EditFramePanel GestureDetector.onScroll drag状态下
 * init-->delete EditableImageButton ACTION_DOWN 时发生, EditableLayout ACTION_DOWN 时 自己从父控件中remove掉
 * init-->editable
 * 状态模式:
 * ----状态的转换:
 * ----move/drag 状态下 EditableFramePanel.onScroll 改变
 *
 * 怎样实现EditableFramePanel.mCurrentEdit 重叠的时候在最上层???
 *       onDraw(Canvas不起作用) 
 * 怎样实现虚线方法不跟着缩放,缩放???
 * 怎样实现editable状态下,打字,会影响EditableLayout的位置???
 *      EditFramePanel.onLayout:两次layout才能实现
 * Created by liufeismart on 16/10/14.
 */

public class EditableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable);
        buildEidtable();
    }

    private void buildEidtable() {
        final EditFramePanel editFramePanel = (EditFramePanel)findViewById(R.id.layout_area);

        ImageButton ibn_up = (ImageButton)findViewById(R.id.ibn_up);
        ImageButton ibn_down = (ImageButton)findViewById(R.id.ibn_down);
        findViewById(R.id.ibn_delete).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });
        findViewById(R.id.ibn_edit).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        editFramePanel.addView(EditableActivity.this);
                    }
                });


    }
}
