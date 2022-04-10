package com.example.mealapp;

import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;

import androidx.annotation.NonNull;

public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source;
        }

        @Override
        public int length() {
            return mSource.length();
        }

        @Override
        public char charAt(int i) {
            return '*';
        }

        @NonNull
        @Override
        public CharSequence subSequence(int i, int i1) {
            return mSource.subSequence(i,i1);
        }
    }
}
