package com.example.aynetyaga.maskedtextwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Andrey Netyaga
 * On 18/06/15
 */
public final class MaskedTextWatcher implements TextWatcher {

    private final EditText mEditText;
    private final String mMask;
    private char charRepresentation = '*';
    //Строка используемых декорирующих символов
    private String notCharRepresentations = null;
    private boolean allow = true;
    private String previosValue;
    private boolean pushToEnd = false;
    private boolean decorateCharAfterType = false;

    public MaskedTextWatcher(EditText editText, String mask) {
        mEditText = editText;
        mMask = mask;
        editText.addTextChangedListener(this);
        notCharRepresentations = findNotCharRepresentation();
    }

    //Получаем строку, состояющую из неповторящихся декорирущих символов например "+()-"
    String findNotCharRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mMask.length(); i++) {
            if (mMask.charAt(i) != charRepresentation && !sb.toString().contains(mMask.charAt(i) + ""))
                sb.append(mMask.charAt(i));
        }
        return sb.toString();
    }


    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (allow) {
            decorateCharAfterType = (start < mMask.length() && before == 0 && mMask.charAt(start) != '*');
            insertStringWithDecorate(getStringWithoutDecorateChar());
        } else {
            allow = true;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        if (allow) {
            pushToEnd = (mEditText.length() == mEditText.getSelectionEnd());
            if (after == 0) pushToEnd = false;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (pushToEnd) {
            mEditText.setSelection(mEditText.length());
        } else if (decorateCharAfterType) {
            mEditText.setSelection(mEditText.getSelectionStart() + 1);
        }
    }

    //вставка данных в поле с учетом декорирующих символов
    private void insertStringWithDecorate(String s) {
        StringBuilder temp = new StringBuilder();

        int j = 0;
        for (int i = 0; i < mMask.length(); i++) {
            if (j >= s.length()) {
                break;
            }
            if (mMask.charAt(i) != charRepresentation) {
                temp.append(mMask.charAt(i)).append("");
            } else {
                temp.append(s.charAt(j++)).append("");
            }
        }
        allow = false;
        mEditText.setTextKeepState(temp.toString());
    }

    //Получаем введенную строку без декорирующих символов, например "79001234567"
    private String getStringWithoutDecorateChar() {
        if (mEditText.length() > mMask.length()) return previosValue;
        StringBuilder sb = new StringBuilder();
        a:
        for (int i = 0; i < mEditText.getEditableText().toString().length(); i++) {
            for (int j = 0; j < notCharRepresentations.length(); j++) {
                if (mEditText.getEditableText().toString().charAt(i) == notCharRepresentations.charAt(j))
                    continue a;
            }
            sb.append(mEditText.getEditableText().toString().charAt(i));
            previosValue = sb.toString();
        }
        return sb.toString();
    }
}
