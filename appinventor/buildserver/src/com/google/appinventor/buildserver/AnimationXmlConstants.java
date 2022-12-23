// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2021 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0
package com.google.appinventor.buildserver;

public class AnimationXmlConstants {

  private AnimationXmlConstants() {
  }

  public final static String FADE_IN_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<alpha xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:fromAlpha=\"0.0\" android:toAlpha=\"1.0\"\n" +
      "\tandroid:duration=\"@android:integer/config_longAnimTime\" />";

  public final static String FADE_OUT_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<alpha xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:fromAlpha=\"1.0\" android:toAlpha=\"0.0\"\n" +
      "\tandroid:duration=\"@android:integer/config_longAnimTime\" />";

  public final static String HOLD_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<translate xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:interpolator=\"@android:anim/accelerate_interpolator\"\n" +
      "\tandroid:fromXDelta=\"0\" android:toXDelta=\"0\"\n" +
      "\tandroid:duration=\"@android:integer/config_longAnimTime\" />";

  public final static String SLIDE_EXIT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<translate xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:interpolator=\"@android:anim/overshoot_interpolator\"\n" +
      "\tandroid:fromXDelta=\"0%\" android:toXDelta=\"-100%\"\n" +
      "\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />";

  public final static String SLIDE_ENTER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<translate xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:interpolator=\"@android:anim/overshoot_interpolator\"\n" +
      "\t\tandroid:fromXDelta=\"100%\" android:toXDelta=\"0%\"\n" +
      "\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />";

  public final static String SLIDE_EXIT_REVERSE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<translate xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:interpolator=\"@android:anim/overshoot_interpolator\"\n" +
      "\t\tandroid:fromXDelta=\"0%\" android:toXDelta=\"100%\"\n" +
      "\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />";

  public final static String SLIDE_ENTER_REVERSE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<translate xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:interpolator=\"@android:anim/overshoot_interpolator\"\n" +
      "\t\tandroid:fromXDelta=\"-100%\" android:toXDelta=\"0%\"\n" +
      "\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />";

  public final static String SLIDE_V_EXIT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<translate xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:interpolator=\"@android:anim/decelerate_interpolator\"\n" +
      "\tandroid:fromYDelta=\"0%\" android:toYDelta=\"100%\"\n" +
      "\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />";

  public final static String SLIDE_V_ENTER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<translate xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:interpolator=\"@android:anim/decelerate_interpolator\"\n" +
      "\t\tandroid:fromYDelta=\"-100%\" android:toYDelta=\"0%\"\n" +
      "\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />";

  public final static String SLIDE_V_EXIT_REVERSE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<translate xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:interpolator=\"@android:anim/decelerate_interpolator\"\n" +
      "\t\tandroid:fromYDelta=\"0%\" android:toYDelta=\"-100%\"\n" +
      "\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />";

  public final static String SLIDE_V_ENTER_REVERSE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<translate xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\tandroid:interpolator=\"@android:anim/decelerate_interpolator\"\n" +
      "\t\tandroid:fromYDelta=\"100%\" android:toYDelta=\"0%\"\n" +
      "\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />";

  public final static String ZOOM_ENTER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\t\tandroid:interpolator=\"@android:anim/decelerate_interpolator\">\n" +
      "\t<scale android:fromXScale=\"2.0\" android:toXScale=\"1.0\"\n" +
      "\t\t\tandroid:fromYScale=\"2.0\" android:toYScale=\"1.0\"\n" +
      "\t\t\tandroid:pivotX=\"50%p\" android:pivotY=\"50%p\"\n" +
      "\t\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />\n" +
      "</set>";

  public final static String ZOOM_ENTER_REVERSE ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n"+
      "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\t\tandroid:interpolator=\"@android:anim/decelerate_interpolator\">\n" +
      "\t<scale android:fromXScale=\"0.5\" android:toXScale=\"1.0\"\n" +
      "\t\t\tandroid:fromYScale=\"0.5\" android:toYScale=\"1.0\"\n" +
      "\t\t\tandroid:pivotX=\"50%p\" android:pivotY=\"50%p\"\n" +
      "\t\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />\n" +
      "</set>";

  public final static String ZOOM_EXIT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\t\tandroid:interpolator=\"@android:anim/decelerate_interpolator\"\n" +
      "\t\tandroid:zAdjustment=\"top\">\n" +
      "\t<scale android:fromXScale=\"1.0\" android:toXScale=\".5\"\n" +
      "\t\t\tandroid:fromYScale=\"1.0\" android:toYScale=\".5\"\n" +
      "\t\t\tandroid:pivotX=\"50%p\" android:pivotY=\"50%p\"\n" +
      "\t\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />\n" +
      "\t<alpha android:fromAlpha=\"1.0\" android:toAlpha=\"0\"\n" +
      "\t\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\"/>\n" +
      "</set>";

  public final static String ZOOM_EXIT_REVERSE ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n" +
      "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
      "\t\tandroid:interpolator=\"@android:anim/decelerate_interpolator\"\n" +
      "\t\tandroid:zAdjustment=\"top\">\n" +
      "\t<scale android:fromXScale=\"1.0\" android:toXScale=\"2.0\"\n" +
      "\t\t\tandroid:fromYScale=\"1.0\" android:toYScale=\"2.0\"\n" +
      "\t\t\tandroid:pivotX=\"50%p\" android:pivotY=\"50%p\"\n" +
      "\t\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\" />\n" +
      "\t<alpha android:fromAlpha=\"1.0\" android:toAlpha=\"0\"\n" +
      "\t\t\tandroid:duration=\"@android:integer/config_mediumAnimTime\"/>\n" +
      "</set>";
  
  // Animations for using with Label StartAnimation function
  public final static String FADE_IN = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\tandroid:fillAfter=\"true\" >\n"
      + "\t<alpha\n"
      + "\t\tandroid:duration=\"1000\"\n"
      + "\t\tandroid:fromAlpha=\"0.0\"\n"
      + "\t\tandroid:interpolator=\"@android:anim/accelerate_interpolator\"\n"
      + "\t\tandroid:toAlpha=\"1.0\" />\n"
      + "</set>";
  
  public final static String FADE_OUT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\tandroid:fillAfter=\"true\" >\n"
      + "\t<alpha\n"
      + "\t\tandroid:duration=\"1000\"\n"
      + "\t\tandroid:fromAlpha=\"1.0\"\n"
      + "\t\tandroid:interpolator=\"@android:anim/accelerate_interpolator\"\n"
      + "\t\tandroid:toAlpha=\"0.0\" />\n"
      + "</set>";
  
  public final static String BLINK = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
      + "\t<alpha android:fromAlpha=\"0.0\"\n"
      + "\t\tandroid:toAlpha=\"1.0\"\n"
      + "\t\tandroid:interpolator=\"@android:anim/accelerate_interpolator\"\n"
      + "\t\tandroid:duration=\"600\"\n"
      + "\t\tandroid:repeatMode=\"reverse\"\n"
      + "\t\tandroid:repeatCount=\"infinite\"/>\n"
      + "</set>";
  
  public final static String ZOOM_IN = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\tandroid:fillAfter=\"true\" >\n"
      + "\t<scale\n"
      + "\t\txmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\t\tandroid:duration=\"1000\"\n"
      + "\t\tandroid:fromXScale=\"1\"\n"
      + "\t\tandroid:fromYScale=\"1\"\n"
      + "\t\tandroid:pivotX=\"50%\"\n"
      + "\t\tandroid:pivotY=\"50%\"\n"
      + "\t\tandroid:toXScale=\"3\"\n"
      + "\t\tandroid:toYScale=\"3\" >\n"
      + "\t</scale>\n"
      + "</set>";
  
  public final static String ZOOM_OUT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\tandroid:fillAfter=\"true\" >\n"
      + "\t<scale\n"
      + "\t\txmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\t\tandroid:duration=\"1000\"\n"
      + "\t\tandroid:fromXScale=\"1.0\"\n"
      + "\t\tandroid:fromYScale=\"1.0\"\n"
      + "\t\tandroid:pivotX=\"50%\"\n"
      + "\t\tandroid:pivotY=\"50%\"\n"
      + "\t\tandroid:toXScale=\"0.5\"\n"
      + "\t\tandroid:toYScale=\"0.5\" >\n"
      + "\t</scale>\n"
      + "</set>";
  
  public final static String ROTATE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
      + "\t<rotate android:fromDegrees=\"0\"\n"
      + "\t\tandroid:toDegrees=\"360\"\n"
      + "\t\tandroid:pivotX=\"50%\"\n"
      + "\t\tandroid:pivotY=\"50%\"\n"
      + "\t\tandroid:duration=\"600\"\n"
      + "\t\tandroid:repeatMode=\"restart\"\n"
      + "\t\tandroid:repeatCount=\"infinite\"\n"
      + "\t\tandroid:interpolator=\"@android:anim/cycle_interpolator\"/>\n"
      + "</set>";
  
  public final static String MOVE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\tandroid:interpolator=\"@android:anim/linear_interpolator\"\n"
      + "\tandroid:fillAfter=\"true\">\n"
      + "\t<translate\n"
      + "\t\tandroid:fromXDelta=\"0%p\"\n"
      + "\t\tandroid:toXDelta=\"75%p\"\n"
      + "\t\tandroid:duration=\"800\" />\n"
      + "</set>";
  
  public final static String SLIDE_UP = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\tandroid:fillAfter=\"true\" >\n"
      + "\t<scale\n"
      + "\t\tandroid:duration=\"500\"\n"
      + "\t\tandroid:fromXScale=\"1.0\"\n"
      + "\t\tandroid:fromYScale=\"1.0\"\n"
      + "\t\tandroid:interpolator=\"@android:anim/linear_interpolator\"\n"
      + "\t\tandroid:toXScale=\"1.0\"\n"
      + "\t\tandroid:toYScale=\"0.0\" />\n"
      + "</set>";
  
  public final static String SLIDE_DOWN = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\tandroid:fillAfter=\"true\">\n"
      + "\t<scale\n"
      + "\t\tandroid:duration=\"500\"\n"
      + "\t\tandroid:fromXScale=\"1.0\"\n"
      + "\t\tandroid:fromYScale=\"0.0\"\n"
      + "\t\tandroid:toXScale=\"1.0\"\n"
      + "\t\tandroid:toYScale=\"1.0\" />\n"
      + "</set>";
  
  public final static String BOUNCE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
      + "\tandroid:fillAfter=\"true\"\n"
      + "\tandroid:interpolator=\"@android:anim/bounce_interpolator\">\n"
      + "\t<scale\n"
      + "\t\tandroid:duration=\"500\"\n"
      + "\t\tandroid:fromXScale=\"1.0\"\n"
      + "\t\tandroid:fromYScale=\"0.0\"\n"
      + "\t\tandroid:toXScale=\"1.0\"\n"
      + "\t\tandroid:toYScale=\"1.0\" />\n"
      + "</set>";
  
  public final static String FLIP = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
      + "<set xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
      + "\t<rotate android:fromDegrees=\"0\"\n"
      + "\t\tandroid:toDegrees=\"180\"\n"
      + "\t\tandroid:pivotX=\"50%\"\n"
      + "\t\tandroid:pivotY=\"50%\"\n"
      + "\t\tandroid:duration=\"600\"\n"
      + "\t\tandroid:interpolator=\"@android:anim/cycle_interpolator\"/>\n"
      + "\t<rotate android:fromDegrees=\"180\"\n"
      + "\t\tandroid:toDegrees=\"0\"\n"
      + "\t\tandroid:pivotX=\"50%\"\n"
      + "\t\tandroid:pivotY=\"50%\"\n"
      + "\t\tandroid:duration=\"600\"\n"
      + "\t\tandroid:interpolator=\"@android:anim/cycle_interpolator\"/>\n"
      + "</set>";

}
