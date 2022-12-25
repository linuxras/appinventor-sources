// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.simple.components;

import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.appinventor.client.output.OdeLog;

import com.google.appinventor.components.common.ComponentConstants;

import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;

import com.google.gwt.user.client.ui.Image;

/**
 * Mock Label component.
 *
 */
public final class MockLabel extends MockVisibleComponent implements FormChangeListener{

  /**
   * Component type name.
   */
  public static final String TYPE = "Label";

  // Property names
  private static final String PROPERTY_NAME_IMAGE = "BackgroundImage";

  private boolean hasImage;

  //Maintain the background image so we can reset if image removed
  private String backgroundColor;
  private final Image image;
  private String imagePropValue;

  // GWT label widget used to mock a Simple Label
  private InlineHTML labelWidget;

  private String savedText = "";     // Saved text, so if we change from
                                     // text to/from html we have the text
                                     // to set

  /**
   * Creates a new MockLabel component.
   *
   * @param editor  editor of source file the component belongs to
   */
  public MockLabel(SimpleEditor editor) {
    super(editor, TYPE, images.label());

    // Initialize mock label UI
    labelWidget = new InlineHTML();
    labelWidget.setStylePrimaryName("ode-SimpleMockComponent");

    image = new Image();
    image.addErrorHandler(new ErrorHandler() {
      @Override
      public void onError(ErrorEvent event) {
        if (imagePropValue != null && !imagePropValue.isEmpty()) {
          OdeLog.elog("Error occurred while loading image " + imagePropValue);
        }
        refreshForm();
      }
    });
    image.addLoadHandler(new LoadHandler() {
      @Override
      public void onLoad(LoadEvent event) {
        refreshForm();
      }
    });
    initComponent(labelWidget);

  }

  @Override
  protected void onAttach() {
    super.onAttach();
    ((YaFormEditor) editor).getForm().addFormChangeListener(this);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    ((YaFormEditor) editor).getForm().removeFormChangeListener(this);
  }


  @Override
  public void onCreateFromPalette() {
    // Change label text to component name
    changeProperty(PROPERTY_NAME_TEXT, MESSAGES.textPropertyValue(getName()));
  }

  /*
   * Sets the label's TextAlignment property to a new value.
   */
  private void setTextAlignmentProperty(String text) {
    MockComponentsUtil.setWidgetTextAlign(labelWidget, text);
  }

  /*
   * Sets the label's BackgroundColor property to a new value.
   */
  private void setBackgroundColorProperty(String text) {
    backgroundColor = text;
    if(hasImage) {
      return;
    }
    if (MockComponentsUtil.isDefaultColor(text)) {
      text = "&HFFFFFFFF";  // white
    }
    MockComponentsUtil.setWidgetBackgroundColor(labelWidget, text);
  }

  /*
   * Sets the label's FontBold property to a new value.
   */
  private void setFontBoldProperty(String text) {
    MockComponentsUtil.setWidgetFontBold(labelWidget, text);
  }

  /*
   * Sets the label's FontItalic property to a new value.
   */
  private void setFontItalicProperty(String text) {
    MockComponentsUtil.setWidgetFontItalic(labelWidget, text);
  }

  /*
   * Sets the label's FontSize property to a new value.
   */
  private void setFontSizeProperty(String text) {
    float convertedText = Float.parseFloat(text);
    MockForm form = ((YaFormEditor) editor).getForm();
    if (convertedText == FONT_DEFAULT_SIZE && form != null
        && form.getPropertyValue("BigDefaultText").equals("True")) {
      MockComponentsUtil.setWidgetFontSize(labelWidget, "24");
    } else {
      MockComponentsUtil.setWidgetFontSize(labelWidget, text);
    }
  }

  /*
   * Sets the label's FontTypeface property to a new value.
   */
  private void setFontTypefaceProperty(String text) {
    MockComponentsUtil.setWidgetFontTypeface(labelWidget, text);
  }

  /*
   * Sets the label's Text property to a new value.
   */
  private void setTextProperty(String text) {
    savedText = text;
    if (getPropertyValue(PROPERTY_NAME_HTMLFORMAT).equals("True")) {
      String sanitizedText = SimpleHtmlSanitizer.sanitizeHtml(text).asString();
      if (sanitizedText != null) {
        sanitizedText = sanitizedText.replaceAll("\\\\n", " ");
      }
      labelWidget.setHTML(sanitizedText);
    } else {
      labelWidget.setHTML(text.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
          .replaceAll(">", "&gt;").replaceAll("\\\\n", "<br>")
      );
    }
  }

  /*
   * Sets the label's TextColor property to a new value.
   */
  private void setTextColorProperty(String text) {
    if (MockComponentsUtil.isDefaultColor(text)) {
      text = "&HFF000000";  // black
    }
    MockComponentsUtil.setWidgetTextColor(labelWidget, text);
  }
  
  private void setMarqueeProperty(String text) {
    if(Boolean.parseBoolean(text)) {
      labelWidget.addStyleName("ode-SimpleMockComponent-marquee");
    } else {
      labelWidget.removeStyleName("ode-SimpleMockComponent-marquee");
    }
  }
  
  private void setMarqueeRepeatLimit(String limit) {
    //Just do nothing with this
  }

  private void setBackgroundImageProperty(String text) {
    if(text.endsWith(".9.png")) { //If its a 9-patch ignore it here
      hasImage = false;
      setBackgroundColorProperty(backgroundColor);
      return;
    }
    imagePropValue = text;
    String url = convertImagePropertyValueToUrl(text);
    if (url == null) {
      hasImage = false;
      url = "";
      setBackgroundColorProperty(backgroundColor);
    } else {
      hasImage = true;
      // Layouts do not show a background color if they have an image.
      // The container's background color shows through any transparent
      // portions of the Image, an effect we can get in the browser by
      // setting the widget's background color to COLOR_NONE.
      MockComponentsUtil.setWidgetBackgroundColor(labelWidget,
              "&H" + COLOR_NONE);
    }
    MockComponentsUtil.setWidgetBackgroundImage(labelWidget, url);
    image.setUrl(url);
  }

  // PropertyChangeListener implementation

  @Override
  public void onPropertyChange(String propertyName, String newValue) {
    super.onPropertyChange(propertyName, newValue);

    // Apply changed properties to the mock component
    if (propertyName.equals(PROPERTY_NAME_TEXTALIGNMENT)) {
      setTextAlignmentProperty(newValue);
    } else if (propertyName.equals(PROPERTY_NAME_BACKGROUNDCOLOR)) {
      setBackgroundColorProperty(newValue);
    } else if (propertyName.equals(PROPERTY_NAME_FONTBOLD)) {
      setFontBoldProperty(newValue);
      refreshForm();
    } else if (propertyName.equals(PROPERTY_NAME_FONTITALIC)) {
      setFontItalicProperty(newValue);
      refreshForm();
    } else if (propertyName.equals(PROPERTY_NAME_FONTSIZE)) {
      setFontSizeProperty(newValue);
      refreshForm();
    } else if (propertyName.equals(PROPERTY_NAME_FONTTYPEFACE)) {
      setFontTypefaceProperty(newValue);
      refreshForm();
    } else if (propertyName.equals(PROPERTY_NAME_TEXT)) {
      setTextProperty(newValue);
      refreshForm();
    } else if (propertyName.equals(PROPERTY_NAME_TEXTCOLOR)) {
      setTextColorProperty(newValue);
    } else if (propertyName.equals(PROPERTY_NAME_HTMLFORMAT)) {
      // Just need to re-set the saved text so it is displayed
      // either as HTML or text as appropriate
      setTextProperty(savedText);
      refreshForm();
    } else if (propertyName.equals(PROPERTY_NAME_MARQUEE)) {
      setMarqueeProperty(newValue);
    } else if (propertyName.equals(PROPERTY_NAME_MARQUEEREPEATLIMIT)) {
      setMarqueeRepeatLimit(newValue);
    } else if (propertyName.equals(PROPERTY_NAME_IMAGE)) {
      setBackgroundImageProperty(newValue);
    }
  }


  @Override
  public void onComponentPropertyChanged(MockComponent component, String propertyName, String propertyValue) {
    if (component.getType().equals(MockForm.TYPE) && propertyName.equals("BigDefaultText")) {
      setFontSizeProperty(getPropertyValue(PROPERTY_NAME_FONTSIZE));
      refreshForm();
    }
  }

  @Override
  public void onComponentRemoved(MockComponent component, boolean permanentlyDeleted) {

  }

  @Override
  public void onComponentAdded(MockComponent component) {

  }

  @Override
  public void onComponentRenamed(MockComponent component, String oldName) {

  }

  @Override
  public void onComponentSelectionChange(MockComponent component, boolean selected) {

  }
}
