// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.Asset;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.IsColor;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentConstants;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.EclairUtil;
import com.google.appinventor.components.runtime.util.TextViewUtil;
import com.google.appinventor.components.runtime.util.HoneycombUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.ViewUtil;

//import com.google.appinventor.components.runtime.parameters.BooleanReferenceParameter;
import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.NinePatch;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import ua.anatolii.graphics.ninepatch.NinePatchChunk;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Underlying base class for TextBox, not directly accessible to Simple
 * programmers.
 *
 * @author sharon@google.com (Sharon Perl)
 */

@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
    "android.permission.READ_EXTERNAL_STORAGE")
@UsesLibraries(libraries = "ninepatch.jar, ninepatch.aar")
public abstract class TextBoxBase extends AndroidViewComponent
    implements OnFocusChangeListener, AccessibleComponent, TextWatcher, 
               TextView.OnEditorActionListener {

  protected final EditText view;

  // Backing for text alignment
  private int textAlignment;

  // Backing for background color
  private int backgroundColor;

  // Backing for font typeface
  private int fontTypeface;

  // Backing for font bold
  private boolean bold;

  // Backing for font italic
  private boolean italic;

  // Backing for hint text
  private String hint;

  // Backing for text color
  private int textColor;

  // This is our handle on Android's nice 3-d default textbox.
  private Drawable defaultTextBoxDrawable;

  //Whether or not the button is in high contrast mode
  private boolean isHighContrast = false;

  //Whether or not the button is in big text mode
  private boolean isBigText = false;

  //The default text color of the textbox hint, according to theme
  private int hintColorDefault;

  private double rotationAngle = 0.0;

  // If true, then all text in textbox will be highlighted on focus
  private boolean selectAllOnFocus;

  //The text to show in error popup
  private String errorText;

  private List<Integer> enterActionIds = Arrays.asList(2, 3, 4, 5, 6, 7);

  private String backgroundImage;

  /**
   * Creates a new TextBoxBase component
   *
   * @param container  container that the component will be placed in
   * @param textview   the underlying EditText object that maintains the text
   */
  public TextBoxBase(ComponentContainer container, EditText textview) {
    super(container);
    view = textview;
    hintColorDefault = view.getCurrentHintTextColor();
    // There appears to be an issue where, by default, Android 7+
    // wants to provide suggestions in text boxes. However, we do not
    // compile the necessary layouts for this to work correctly, which
    // results in an application crash. This disables that feature
    // until we include newer Android layouts.
    if (Build.VERSION.SDK_INT >= 24 /* Nougat */ ) {
      EclairUtil.disableSuggestions(textview);
    }

    // Listen to focus changes
    view.setOnFocusChangeListener(this);
    view.setOnEditorActionListener(this);
    // Listen to text changes
    view.addTextChangedListener(this);

    defaultTextBoxDrawable = view.getBackground();

    // Add a transformation method to provide input validation
    /* TODO(user): see comment above)
    setTransformationMethod(new ValidationTransformationMethod());
    */

    // Adds the component to its designated container
    container.$add(this);

    container.setChildWidth(this, ComponentConstants.TEXTBOX_PREFERRED_WIDTH);

    TextAlignment(Component.ALIGNMENT_NORMAL);
    // Leave the nice default background color. Users can change it to "none" if they like
    //
    // TODO(user): if we make a change here we also need to change the default property value.
    // Eventually I hope to simplify this so it has to be changed in one location
    // only). Maybe we need another color value which would be 'SYSTEM_DEFAULT' which
    // will not attempt to explicitly initialize with any of the properties with any
    // particular value.
    Enabled(true);
    fontTypeface = Component.TYPEFACE_DEFAULT;
    TextViewUtil.setFontTypeface(view, fontTypeface, bold, italic);
    FontSize(Component.FONT_DEFAULT_SIZE);
    Hint("");
    if (isHighContrast || container.$form().HighContrast()) {
      view.setHintTextColor(COLOR_YELLOW);
    }
    else {
      view.setHintTextColor(hintColorDefault);
    }

    Text("");
    TextColor(Component.COLOR_DEFAULT);
    BackgroundColor(Component.COLOR_DEFAULT);
    SelectAllOnFocus(false);
    ErrorText("");
    BackgroundImage("");
  }

  @Override
  public View getView() {
    return view;
  }

  /**
   * Event raised when the `%type%` is selected for input, such as by
   * the user touching it.
   */
  @SimpleEvent(description = "Event raised when the %type% is selected for input, such as by "
      + "the user touching it.")
  public void GotFocus() {
    EventDispatcher.dispatchEvent(this, "GotFocus");
  }

  /**
   * Event raised when the `%type%` is no longer selected for input, such
   * as if the user touches a different text box.
   */
  @SimpleEvent(description = "Event raised when the %type% is no longer selected for input, such "
      + "as if the user touches a different text box.")
  public void LostFocus() {
    EventDispatcher.dispatchEvent(this, "LostFocus");
  }

  /**
   * Default Validate event handler.
   */
  /* TODO(markf): Restore event if needed.
  @SimpleEvent
  public void Validate(String text, BooleanReferenceParameter accept) {
    EventDispatcher.dispatchEvent(this, "Validate", text, accept);
  }
  */

  /**
   * Returns the alignment of the `%type%`'s text: center, normal
   * (e.g., left-justified if text is written left to right), or
   * opposite (e.g., right-justified if text is written left to right).
   *
   * @return  one of {@link Component#ALIGNMENT_NORMAL},
   *          {@link Component#ALIGNMENT_CENTER} or
   *          {@link Component#ALIGNMENT_OPPOSITE}
   */
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE,
      description = "Whether the text should be left justified, centered, " +
      "or right justified.  By default, text is left justified.",
      userVisible = false)
  public int TextAlignment() {
    return textAlignment;
  }

  /**
   * Specifies the alignment of the `%type%`'s text. Valid values are:
   * `0` (normal; e.g., left-justified if text is written left to right),
   * `1` (center), or
   * `2` (opposite; e.g., right-justified if text is written left to right).
   *
   * @param alignment  one of {@link Component#ALIGNMENT_NORMAL},
   *                   {@link Component#ALIGNMENT_CENTER} or
   *                   {@link Component#ALIGNMENT_OPPOSITE}
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXTALIGNMENT,
      defaultValue = Component.ALIGNMENT_NORMAL + "")
  @SimpleProperty(
      userVisible = false)
  public void TextAlignment(int alignment) {
    this.textAlignment = alignment;
    TextViewUtil.setAlignment(view, alignment, false);
  }

  /**
   * Returns the background color of the %type% as an alpha-red-green-blue
   * integer.
   *
   * @return  background RGB color with alpha
   */
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE,
      description = "The background color of the input box.  You can choose " +
      "a color by name in the Designer or in the Blocks Editor.  The " +
      "default background color is 'default' (shaded 3-D look).")
  @IsColor
  public int BackgroundColor() {
    return backgroundColor;
  }

  /**
   * The background color of the `%type%``. You can choose a color by name in the Designer or in
   * the Blocks Editor. The default background color is 'default' (shaded 3-D look).
   *
   * @internaldoc
   * Specifies the background color of the `%type%` as an alpha-red-green-blue
   * integer.
   *
   * @param argb  background RGB color with alpha
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR,
      defaultValue = Component.DEFAULT_VALUE_COLOR_DEFAULT)
  @SimpleProperty
  public void BackgroundColor(int argb) {
    backgroundColor = argb;
    if (argb != Component.COLOR_DEFAULT) {
      TextViewUtil.setBackgroundColor(view, argb);
    } else {
      if (isHighContrast || container.$form().$form().HighContrast()) {
        TextViewUtil.setBackgroundColor(view, Component.COLOR_BLACK);
      } else {
        ViewUtil.setBackgroundDrawable(view, defaultTextBoxDrawable);
      }
    }
  }

  /**
   * Returns true if the %type% is active and useable.
   *
   * @return  {@code true} indicates enabled, {@code false} disabled
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR,
      description = "Whether the user can enter text into the %type%.  " +
      "By default, this is true.")
  public boolean Enabled() {
    return TextViewUtil.isEnabled(view);
  }

  /**
   * If set, user can enter text into the `%type%`.
   *
   * @internaldoc
   * Specifies whether the %type% should be active and usable.
   *
   * @param enabled  {@code true} for enabled, {@code false} disabled
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty
  public void Enabled(boolean enabled) {
    TextViewUtil.setEnabled(view, enabled);
  }

  /**
   * Returns true if the text of the %type% should be bold.
   * If bold has been requested, this property will return true, even if the
   * font does not support bold.
   *
   * @return  {@code true} indicates bold, {@code false} normal
   */
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE,
      userVisible = false,
      description = "Whether the font for the text should be bold.  By " +
      "default, it is not.")
  public boolean FontBold() {
    return bold;
  }

  /**
   * Specifies whether the text of the `%type%` should be bold.
   * Some fonts do not support bold.
   *
   * @param bold  {@code true} indicates bold, {@code false} normal
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(
      userVisible = false)
  public void FontBold(boolean bold) {
    this.bold = bold;
    TextViewUtil.setFontTypeface(view, fontTypeface, bold, italic);
  }

  /**
   * Returns true if the text of the %type% should be italic.
   * If italic has been requested, this property will return true, even if the
   * font does not support italic.
   *
   * @return  {@code true} indicates italic, {@code false} normal
   */
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE,
      description = "Whether the text should appear in italics.  By " +
      "default, it does not.",
      userVisible = false)
  public boolean FontItalic() {
    return italic;
  }

  /**
   * Specifies whether the text of the `%type%` should be italic.
   * Some fonts do not support italic.
   *
   * @param italic  {@code true} indicates italic, {@code false} normal
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(userVisible = false)
  public void FontItalic(boolean italic) {
    this.italic = italic;
    TextViewUtil.setFontTypeface(view, fontTypeface, bold, italic);
  }

  /**
   * Returns the text font size of the %type%, measured in sp(scale-independent pixels).
   *
   * @return  font size in sp(scale-independent pixels).
   */
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE,
      description = "The font size for the text.  By default, it is " +
      Component.FONT_DEFAULT_SIZE + " points.")
  public float FontSize() {
    return TextViewUtil.getFontSize(view, container.$context());
  }

  /**
   * Specifies the text font size of the `%type%`, measured in sp(scale-independent pixels).
   *
   * @param size  font size in pixel
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT,
      defaultValue = Component.FONT_DEFAULT_SIZE + "")
  @SimpleProperty
  public void FontSize(float size) {
    if (size == FONT_DEFAULT_SIZE && container.$form().BigDefaultText()) {
      TextViewUtil.setFontSize(view, 24);
    } else {
      TextViewUtil.setFontSize(view, size);
    }
  }

  /**
   * Returns the text font face of the %type% as default, serif, sans
   * serif, or monospace.
   *
   * @return  one of {@link Component#TYPEFACE_DEFAULT},
   *          {@link Component#TYPEFACE_SERIF},
   *          {@link Component#TYPEFACE_SANSSERIF} or
   *          {@link Component#TYPEFACE_MONOSPACE}
   */
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE,
      description = "The font for the text.  The value can be changed in " +
      "the Designer.",
      userVisible = false)
  public int FontTypeface() {
    return fontTypeface;
  }

  /**
   * The text font face of the `%type%`. Valid values are `0` (default), `1` (serif), `2` (sans
   * serif), or `3` (monospace).
   *
   * @param typeface  one of {@link Component#TYPEFACE_DEFAULT},
   *                  {@link Component#TYPEFACE_SERIF},
   *                  {@link Component#TYPEFACE_SANSSERIF} or
   *                  {@link Component#TYPEFACE_MONOSPACE}
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TYPEFACE,
      defaultValue = Component.TYPEFACE_DEFAULT + "")
  @SimpleProperty(
      userVisible = false)
  public void FontTypeface(int typeface) {
    fontTypeface = typeface;
    TextViewUtil.setFontTypeface(view, fontTypeface, bold, italic);
  }

  /**
   * Hint property getter method.
   *
   * @return  hint text
   */
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE,
      description = "Text that should appear faintly in the %type% to " +
      "provide a hint as to what the user should enter.  This can only be " +
      "seen if the Text property is empty.")
  public String Hint() {
    return hint;
  }

  /**
   * `%type%` hint for the user.
   *
   * @internaldoc
   * Hint property setter method.
   *
   * @param hint  hint text
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void Hint(String hint) {
    this.hint = hint;
    view.setHint(hint);
    view.invalidate();
  }

  /**
   * Returns the textbox contents.
   *
   * @return  text box contents
   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR)
  public String Text() {
    return TextViewUtil.getText(view);
  }

  /**
   * The text in the `%type%`, which can be set by the programmer in the Designer or Blocks Editor,
   * or it can be entered by the user (unless the {@link #Enabled(boolean)} property is false).
   *
   * @param text  new text in text box
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXTAREA,
      defaultValue = "")
  @SimpleProperty(
      // This kind of breaks the appearance/behavior dichotomy
      category = PropertyCategory.BEHAVIOR,
      description = "The text in the %type%, which can be set by the " +
      "programmer in the Designer or Blocks Editor, or it can be entered by " +
      "the user (unless the <code>Enabled</code> property is false).")
  public void Text(String text) {
    TextViewUtil.setText(view, text);
  }

  /**
   * Returns the text color of the %type% as an alpha-red-green-blue
   * integer.
   *
   * @return  text RGB color with alpha
   */
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE,
      description = "The color for the text.  You can choose a color by name " +
      "in the Designer or in the Blocks Editor.  The default text color is " +
      "black.")
  @IsColor
  public int TextColor() {
    return textColor;
  }

  /**
   * Specifies the text color of the `%type%` as an alpha-red-green-blue
   * integer.
   *
   * @param argb  text RGB color with alpha
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR,
      defaultValue = Component.DEFAULT_VALUE_COLOR_DEFAULT)
  @SimpleProperty
  public void TextColor(int argb) {
    textColor = argb;
    if (argb != Component.COLOR_DEFAULT) {
      TextViewUtil.setTextColor(view, argb);
    } else {
      if (isHighContrast || container.$form().HighContrast()) {
        TextViewUtil.setTextColor(view, COLOR_WHITE);
      }
      else {
        TextViewUtil.setTextColor(view, container.$form().isDarkTheme() ? COLOR_WHITE : Component.COLOR_BLACK);
      }
    }
  }

  @SimpleProperty(
    category = PropertyCategory.BEHAVIOR,
    description = "Set the %type% so that when it takes focus, all the text is selected."
  )
  public boolean SelectAllOnFocus() {
    return selectAllOnFocus;
  }

  @DesignerProperty(
    editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
    defaultValue = "False"
  )
  @SimpleProperty
  public void SelectAllOnFocus(boolean selectAllOnFocus) {
    this.selectAllOnFocus = selectAllOnFocus;
    view.setSelectAllOnFocus(selectAllOnFocus);
  }
  
  @SimpleProperty(
    category = PropertyCategory.APPEARANCE,
    description = "Sets the right-hand compound drawable of the %type% to the \"error\" icon" +
    " and sets an error message that will be displayed in a popup when the %type% has focus. "
  )
  public String ErrorText() {
    return errorText;
  }

  @DesignerProperty(
    editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXTAREA,
    defaultValue = ""
  )
  @SimpleProperty
  public void ErrorText(String errorText) {
    this.errorText = errorText;
    if(errorText.isEmpty()) {
        this.errorText = null;
    }
    view.setError(this.errorText);
  }

  /**
   * Returns the path of the %type%'s' background image.
   *
   * @return  the path of the %type%'s background image
   */
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE,
      description = "Set background image for %type%")
  public String BackgroundImage() {
    return backgroundImage;
  }

  /**
   * Specifies the path of the `%type%`'s `BackgroundImage`.
   * <br/><b>Note:</b> If your image has the extension `.9.png` it will not be displayed in the Designer
   *
   * @internaldoc
   * <p/>See {@link MediaUtil#determineMediaSource} for information about what
   * a path can be.
   *
   * @param path  the path of the %type%'s background image
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET,
      defaultValue = "")
  @SimpleProperty
  public void BackgroundImage(@Asset final String path) {
    if (MediaUtil.isExternalFile(container.$context(), path)
        && container.$form().isDeniedPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
      container.$form().askPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
          new PermissionResultHandler() {
            @Override
            public void HandlePermissionResponse(String permission, boolean granted) {
              if (granted) {
                BackgroundImage(path);
              } else {
                container.$form().dispatchPermissionDeniedEvent(TextBoxBase.this, "BackgroundImage", permission);
              }
            }
          });
      return;
    }
    backgroundImage = (path == null) ? "" : path;
    Drawable drawable = null;

    //Check for 9-patch image and process with ninepatch library
    if(!backgroundImage.isEmpty() && backgroundImage.endsWith(".9.png")) {
      Bitmap bitmap;
      try {
        bitmap = BitmapFactory.decodeStream(MediaUtil.openMedia(this.container.$form(), backgroundImage));
      } catch(Exception e) {
        bitmap = null;
      }
      if(bitmap != null) {
        byte[] chunk = bitmap.getNinePatchChunk();
        if(NinePatch.isNinePatchChunk(chunk)) {
          //Just use it
          drawable = new NinePatchDrawable(this.container.$form().getResources(), bitmap, chunk, new Rect(), (String) null);
          ViewUtil.setBackgroundImage(view, drawable);
        } else {
          //We know the intent generate it
          drawable = NinePatchChunk.create9PatchDrawable(this.container.$form(), bitmap, (String) null);
        }
      }
    } else {
      try {
        drawable = MediaUtil.getBitmapDrawable(container.$form(), backgroundImage);
      } catch (IOException ioe) {
        Log.e("Label", "Unable to load " + backgroundImage);
        drawable = null;
      }

    }
    ViewUtil.setBackgroundImage(view, drawable);

  }

  /**
   * Request focus to current `%type%`.
   */
  @SimpleFunction(
    description = "Sets the %type% active.")
  public void RequestFocus() {
    view.requestFocus();
  }
  
  @SimpleFunction(description = "Allows you to set animation style. Valid (case-insensitive) values are: "
      + "FadeIn, FadeOut, Flip, Bounce, Blink, ZoomIn, ZoomOut, Rotate, Move, "
      + "SlideDown, SlideUp. If invalid style is used, animation will be removed.")
  public void StartAnimation(String style) {
    if(!TextViewUtil.setAnimation(view, container.$context(), style)) {
      StopAnimation();
    }
  }
  
  @SimpleFunction(description = "Stop currently running animations if any")
  public void StopAnimation() {
    TextViewUtil.stopAnimation(view);
  }

  @SimpleProperty(category = PropertyCategory.APPEARANCE,
      description = "Sets the degrees that the view is rotated around the pivot point. Increasing values result in clockwise rotation.")
  public double RotationAngle() {
    return rotationAngle;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = "0.0")
  @SimpleProperty
  public void RotationAngle(double angle) {
    if(rotationAngle == angle) {
      return;
    }
    if (SdkLevel.getLevel() < SdkLevel.LEVEL_HONEYCOMB) {
      container.$form().dispatchErrorOccurredEvent(this, "RotationAngle",
        ErrorMessages.ERROR_VIEW_CANNOT_ROTATE);
      return;
    }
    HoneycombUtil.viewSetRotate(view, angle);
    rotationAngle = angle;
  }

  @SimpleFunction(description = "Highlights all text in this %type%")
  public void SelectAll() {
      view.selectAll();
  }

  @SimpleFunction(description = "Set the cursor to the given position.")
  public void SetCursorAt(int position) {
    int len = view.getText().length();
    if(len >= (position - 1)) {
      view.setSelection((position - 1));
    } else {
      SetCursorAtEnd();
    }
  }

  @SimpleFunction(description = "Set the cursor position to the end")
  public void SetCursorAtEnd() {
    int len = view.getText().length();
    view.setSelection(len);
  }

  /**
   * Event raised when text has changed in `%type%`.
   */
  @SimpleEvent(description = "Event raised when the text has been changed in %type%.")
  public void TextChanged() {
    EventDispatcher.dispatchEvent(this, "TextChanged");
  }

  /**
   * Event raised when Enter has been pressed in `%type%`.
   */
  @SimpleEvent(description = "Event raised when Enter has been pressed in %type%.")
  public void EnterPressed() {
    EventDispatcher.dispatchEvent(this, "EnterPressed");
  }

  // OnFocusChangeListener implementation

  @Override
  public void onFocusChange(View previouslyFocused, boolean gainFocus) {
    if (gainFocus) {
      // Initialize content backing for input validation
      // TODO(sharon): this field stayed in TextBox. It isn't being used yet,
      // and I'm not sure what to do with this assignment.
      // text = TextViewUtil.getText(view);

      GotFocus();
    } else {
      LostFocus();
    }
  }

  @Override
  public void setHighContrast(boolean isHighContrast) {
    //background of button
    if (backgroundColor == Component.COLOR_DEFAULT) {
      if (isHighContrast) {
        TextViewUtil.setBackgroundColor(view, Component.COLOR_BLACK);
      }
      else {
        ViewUtil.setBackgroundDrawable(view, defaultTextBoxDrawable);
      }
    }

    //color of text
    if (textColor == Component.COLOR_DEFAULT) {
      if (isHighContrast) {
        TextViewUtil.setTextColor(view, COLOR_WHITE);
        view.setHintTextColor(COLOR_YELLOW);

      }
      else {
        TextViewUtil.setTextColor(view, container.$form().isDarkTheme() ? COLOR_WHITE : Component.COLOR_BLACK);
        view.setHintTextColor(hintColorDefault);

      }
    }
  }

  @Override
  public boolean getHighContrast() {
    return isHighContrast;
  }

  @Override
  public void setLargeFont(boolean isLargeFont) {
    if (TextViewUtil.getFontSize(view, container.$context()) == 24.0 || TextViewUtil.getFontSize(view, container.$context()) == Component.FONT_DEFAULT_SIZE) {
      if (isLargeFont) {
        TextViewUtil.setFontSize(view, 24);
      } else {
        TextViewUtil.setFontSize(view, Component.FONT_DEFAULT_SIZE);
      }
    }
  }

  @Override
  public boolean getLargeFont() {
    return isBigText;
  }

  //TextWatcher implementation

  @Override
  public void beforeTextChanged (CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged (CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged (Editable s) {
      container.$form().runOnUiThread(new Runnable() {
          public void run() {
              TextBoxBase.this.TextChanged();
          }
      });
  }

  // Listen for enter key
  public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
      if(enterActionIds.contains(actionId)) {
          container.$form().runOnUiThread(new Runnable() {
              public void run() {
                  TextBoxBase.this.EnterPressed();
              }
          });
          return true;
      }
      return false;
  }
}
