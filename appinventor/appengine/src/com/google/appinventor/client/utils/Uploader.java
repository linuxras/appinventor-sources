// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.utils;

import com.google.appinventor.shared.rpc.ServerLayout;
import com.google.appinventor.shared.rpc.UploadResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Hidden;

/**
 * Utility class to upload files to the server.
 *
 */
public final class Uploader {

  // Singleton Uploader instance
  public static final Uploader INSTANCE = new Uploader();

  // Uploader form
  private final FormPanel form;

  // Callback upon completion of upload
  private AsyncCallback<UploadResponse> callback;

  /**
   * Returns the uploader instance.
   *
   * @return  uploader instance
   */
  public static Uploader getInstance() {
    return INSTANCE;
  }

  /**
   * Creates a new uploader.
   */
  private Uploader() {
    // Because we're going to add a FileUpload widget, we'll need to set the
    // form to use the POST method, and multipart MIME encoding.
    form = new FormPanel();
    form.setEncoding(FormPanel.ENCODING_MULTIPART);
    form.setMethod(FormPanel.METHOD_POST);
    form.setSize("0px", "0px");
    form.setVisible(false);

    // Add an event handler to the form.
    form.addFormHandler(new FormHandler() {
      @Override
      public void onSubmit(FormSubmitEvent event) {
        // nothing
      }

      @Override
      public void onSubmitComplete(FormSubmitCompleteEvent event) {
        // When the form submission is successfully completed, this event is
        // fired. Assuming the service returned a response of type text/html,
        // we can get the result text here (see the FormPanel documentation for
        // further explanation).
        String results = event.getResults();
        // If the submit completely failed, results will be null.
        if (results == null) {
          callback.onFailure(new RuntimeException("Upload error"));
        } else {
          // results contains the UploadResponse value as a String. It was written on the server
          // side in the doPost method in ode/server/UploadServlet.java.
          UploadResponse uploadResponse = UploadResponse.extractUploadResponse(results);
          if (uploadResponse != null) {
            callback.onSuccess(uploadResponse);
          } else {
            callback.onFailure(new RuntimeException("Upload error"));
          }
        }
      }
    });

    RootPanel.get().add(form);
  }

  /**
   * Uploads the given file.
   *
   * @param upload  file upload widget containing file information
   * @param uploadUrl  URL to upload the file to
   */
  public final void upload(FileUpload upload, String uploadUrl,
      AsyncCallback<UploadResponse> callback) {
    this.callback = callback;

    form.setWidget(upload);
    form.setAction(uploadUrl);
    form.submit();
  }
  
  /**
   * Uploads the given file with user defined package.
   *
   * @param upload  file upload widget containing file information
   * @param uploadUrl  URL to upload the file to
   * @param packageName  Package to use for new project
   */
  public final void upload(FileUpload upload, String uploadUrl,
      String packageName, AsyncCallback<UploadResponse> callback) {
    this.callback = callback;
    //If we have a none empty packageName we send it along to the server
    if(packageName != null && !packageName.isEmpty()) {
      VerticalPanel hiddenPanel = new VerticalPanel();
      hiddenPanel.setSize("0px", "0px");
      hiddenPanel.setVisible(false);
      hiddenPanel.add(upload);
      Hidden pn = new Hidden(ServerLayout.UPLOAD_PROJECT_PACKAGE_FORM_ELEMENT);
      pn.setValue(packageName);
      hiddenPanel.add(pn);
    
      form.setWidget(hiddenPanel);
    } else {
      //Default behavior
      form.setWidget(upload);
    }
    form.setAction(uploadUrl);
    form.submit();
  }  
}
