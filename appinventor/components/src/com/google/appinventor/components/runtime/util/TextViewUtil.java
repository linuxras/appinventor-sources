// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.Component;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.text.Html;
import android.content.Context;

/**
 * Helper methods for manipulating {@link TextView} objects.
 *
 */
public class TextViewUtil {

  private TextViewUtil() {
  }

  /**
   * TextView alignment setter.
   *
   * @param alignment  one of {@link Component#ALIGNMENT_NORMAL},
   *                   {@link Component#ALIGNMENT_CENTER} or
   *                   {@link Component#ALIGNMENT_OPPOSITE}
   * @param centerVertically whether the text should be centered vertically
   */
  public static void setAlignment(TextView textview, int alignment, boolean centerVertically) {
    int horizontalGravity;
    switch (alignment) {
      default:
        throw new IllegalArgumentException();

      case Component.ALIGNMENT_NORMAL:
        horizontalGravity = Gravity.LEFT;
        break;

      case Component.ALIGNMENT_CENTER:
        horizontalGravity = Gravity.CENTER_HORIZONTAL;
        break;

      case Component.ALIGNMENT_OPPOSITE:
        horizontalGravity = Gravity.RIGHT;
        break;
    }
    int verticalGravity = centerVertically ? Gravity.CENTER_VERTICAL : Gravity.TOP;
    textview.setGravity(horizontalGravity | verticalGravity);
    textview.invalidate();
  }

  /**
   * {@link TextView} background color setter.  Generally, the caller will
   * not pass {@link Component#COLOR_DEFAULT}, instead substituting in the
   * appropriate color.
   *
   * @param textview   text view instance
   * @param argb  background RGB color with alpha
   */
  public static void setBackgroundColor(TextView textview, int argb) {
    textview.setBackgroundColor(argb);
    textview.invalidate();
  }

  /**
   * Returns the enabled state a {@link TextView}.
   *
   * @param textview   text view instance
   * @return  {@code true} for enabled, {@code false} disabled
   */
  public static boolean isEnabled(TextView textview) {
    return textview.isEnabled();
  }

  /**
   * Enables a {@link TextView}.
   *
   * @param textview   text view instance
   * @param enabled  {@code true} for enabled, {@code false} disabled
   */
  public static void setEnabled(TextView textview, boolean enabled) {
    textview.setEnabled(enabled);
    textview.invalidate();
  }

  /**
   * Returns the font size for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param context   Context in the screen to get the density of
   * @return  font size in pixel
   */
  public static float getFontSize(TextView textview, Context context) {
    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
    return textview.getTextSize()/scaledDensity;
  }

  /**
   * Sets the font size for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param size  font size in pixel
   */
  public static void setFontSize(TextView textview, float size) {
    textview.setTextSize(size);
    textview.requestLayout();
  }

  /**
   * Sets the font typeface for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param typeface  one of @link Component#TYPEFACE_DEFAULT},
   *                  {@link Component#TYPEFACE_SERIF},
   *                  {@link Component#TYPEFACE_SANSSERIF} or
   *                  {@link Component#TYPEFACE_MONOSPACE}
   * @param bold true for bold, false for not bold
   * @param italic true for italic, false for not italic
   */
  public static void setFontTypeface(TextView textview, int typeface,
      boolean bold, boolean italic) {
    Typeface tf;
    switch (typeface) {
      default:
        throw new IllegalArgumentException();

      case Component.TYPEFACE_DEFAULT:
        tf = Typeface.DEFAULT;
        break;

      case Component.TYPEFACE_SERIF:
        tf = Typeface.SERIF;
        break;

      case Component.TYPEFACE_SANSSERIF:
        tf = Typeface.SANS_SERIF;
        break;

      case Component.TYPEFACE_MONOSPACE:
        tf = Typeface.MONOSPACE;
        break;
    }

    int style = 0;
    if (bold) {
      style |= Typeface.BOLD;
    }
    if (italic) {
      style |= Typeface.ITALIC;
    }
    textview.setTypeface(Typeface.create(tf, style));
    textview.requestLayout();
  }

  /**
   * Returns the text for a {@link TextView}.
   *
   * @param textview   text view instance
   * @return  text shown in text view
   */
  public static String getText(TextView textview) {
    return textview.getText().toString();
  }

  /**
   * Sets the text for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param text  new text to be shown
   */
  public static void setTextHTML(TextView textview, String text) {
    textview.setText(Html.fromHtml(text));
    textview.requestLayout();
  }

  /**
   * Sets the text for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param text  new text to be shown
   */
  public static void setText(TextView textview, String text) {
    textview.setText(text);
    textview.requestLayout();
  }

  /**
   * Sets the padding for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param padding  left and right padding to be set
   */
  public static void setPadding(TextView textview, int padding) {
    textview.setPadding(padding, padding, 0, 0);
    textview.requestLayout();
  }

  /**
   * Sets the text color for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param argb  text RGB color with alpha
   */
  public static void setTextColor(TextView textview, int argb) {
    textview.setTextColor(argb);
    textview.invalidate();
  }

  public static void setTextColors(TextView textview, ColorStateList colorStateList) {
    textview.setTextColor(colorStateList);
  }

  /**
   * Sets the minimum width of a text view.
   *
   * @param textview text view instance
   * @param minWidth minimum width of the text view in pixels
   */
  public static void setMinWidth(TextView textview, int minWidth) {
    // According to https://developer.android.com/reference/android/widget/TextView.html#setMinWidth(int), the minimum
    // width of TextView is the maximum of setMinWidth and setMinimumWidth. Talk about NIH syndrome!
    textview.setMinWidth(minWidth);
    textview.setMinimumWidth(minWidth);
  }

  /**
   * Sets the minimum height of a text view.
   *
   * @param textview text view instance
   * @param minHeight minimum height of the text view in pixels
   */
  public static void setMinHeight(TextView textview, int minHeight) {
    // According to https://developer.android.com/reference/android/widget/TextView.html#setMinHeight(int), the minimum
    // height of TextView is the maximum of setMinHeight and setMinimumHeight. Talk about NIH syndrome!
    textview.setMinHeight(minHeight);
    textview.setMinimumHeight(minHeight);
  }

  /**
   * Sets the minimum size for a text view.
   *
   * @param textview text view instance
   * @param minWidth minimum width of the text view in pixels
   * @param minHeight minimum height of the text view in pixels
   */
  public static void setMinSize(TextView textview, int minWidth, int minHeight) {
    TextViewUtil.setMinWidth(textview, minWidth);
    TextViewUtil.setMinHeight(textview, minHeight);
  }
  
  public static void setShadowLayer(TextView view, float radius, float x, float y, int color) {
    view.setShadowLayer(radius, x, y, color);
    view.invalidate();
  }

  public static boolean setAnimation(TextView view, Activity activity, String style) {
    boolean rv = true;
    Animation animation;
    if(style.equalsIgnoreCase("FadeIn")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("fade_in", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("FadeOut")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("fade_out", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("Flip")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("flip", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("Bounce")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("bounce", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("Blink")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("blink", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("ZoomIn")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("zoom_in", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("ZoomOut")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("zoom_out", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("Rotate")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("rotate", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("Move")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("move", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("SlideDown")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("slide_down", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else if (style.equalsIgnoreCase("SlideUp")) {
      animation = AnimationUtils.loadAnimation(activity, 
          activity.getResources().getIdentifier("slide_up", "anim", activity.getPackageName()));
      view.startAnimation(animation);
    } else {
      rv = false;
    }
    return rv;
  }
 
  public static void stopAnimation(TextView view) {
    Animation anim = view.getAnimation();
    if(anim != null) {
      anim.cancel();
    }
    view.setAnimation(null);
    view.invalidate();
  }
}
