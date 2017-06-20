package ru.alexangan.developer.geatech.Interfaces;

import ru.alexangan.developer.geatech.ViewOverrides.ScrollViewEx;

// Created by user on 31.05.2017.

public interface ScrollViewListener {
    void onScrollChanged(ScrollViewEx scrollView,
                         int x, int y, int oldx, int oldy);
}
