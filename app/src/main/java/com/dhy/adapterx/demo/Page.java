package com.dhy.adapterx.demo;

import androidx.annotation.NonNull;

import com.dhy.adapterx.IDiff;

public class Page implements IDiff<Page> {
    public int id;

    public Page(int id) {
        this.id = id;
    }

    @Override
    public boolean isSame(@NonNull Page other) {
        return id == other.id;
    }
}
