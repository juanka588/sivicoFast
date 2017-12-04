package com.rocket.sivico.GUI;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocket.sivico.R;

/**
 * Created by Camilo on 4/12/2017.
 */

public class SivicoDialog extends Dialog {
    public static final int TYPE_OK = 101, TYPE_TWO = 102, TYPE_INPUT = 103, TYPE_SERVICE = 104;
    public static final int SELECTION_POSITIVE = 1, SELECTION_NEGATIVE = 2;
    private TextView header;
    private TextView content;
    private ImageView icon;
    private Button okButton;
    private Button cancelButton;
    private EditText input;
    private int selection;

    public SivicoDialog(Context context, int iconID, String headerText,
                         String contentText, String okButtonText, int type) {
        this(context, iconID, headerText, contentText, okButtonText, "", R.style.ThemeSivicoDialog, type);
    }

    public SivicoDialog(Context context, int iconID, String headerText,
                         String contentText, String okButtonText, String cancelButton) {
        this(context, iconID, headerText, contentText, okButtonText, cancelButton, R.style.ThemeSivicoDialog, TYPE_TWO);
    }

    public SivicoDialog(Context context, int iconID, String headerText,
                         String contentText, String okButtonText,
                         String cancelButtonText, int themeId, int type) {
        super(context, themeId);
        switch (type) {
            case TYPE_TWO:
                this.setContentView(R.layout.dialog_two_options);
                break;
        }

        content = (TextView) this.findViewById(R.id.content);
        icon = (ImageView) this.findViewById(R.id.icon);
        okButton = (Button) this.findViewById(R.id.ok_button);
        content.setText(contentText);
        icon.setImageResource(iconID);
        okButton.setText(okButtonText);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SivicoDialog.this.selection = SELECTION_POSITIVE;
                SivicoDialog.this.dismiss();
            }
        });
        header = (TextView) this.findViewById(R.id.header);
        if (headerText == null || headerText.isEmpty()) {
            header.setVisibility(View.GONE);
        } else {
            header.setText(headerText);
        }
        switch (type) {
            case TYPE_TWO:
                cancelButton = (Button) this.findViewById(R.id.cancel_button);
                cancelButton.setText(cancelButtonText);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SivicoDialog.this.selection = SELECTION_NEGATIVE;
                        SivicoDialog.this.cancel();
                    }
                });
                break;
        }

    }

    public String getInput() {
        return input.getText().toString();
    }

    public int getSelection() {
        return selection;
    }

    public EditText getInputView() {
        return input;
    }
}
