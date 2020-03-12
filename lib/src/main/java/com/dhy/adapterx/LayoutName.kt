package com.dhy.adapterx

/**
 * use {@link IViewHolder} then set value to parameter.
 * eg: IViewHolder(v, R.layout.item)
 */
@Deprecated("use IViewHolder")
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class LayoutName(val value: String)