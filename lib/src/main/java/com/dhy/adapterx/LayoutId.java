package com.dhy.adapterx;

import androidx.annotation.LayoutRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * use {@link IViewHolder} then set value to parameter.
 * eg: IViewHolder(v, R.layout.item)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
public @interface LayoutId {
    @LayoutRes
    int value();
}
