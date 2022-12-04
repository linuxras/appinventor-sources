// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2020 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.wizards;

import com.google.appinventor.client.ErrorReporter;
import com.google.appinventor.client.Ode;
import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.OdeAsyncCallback;
import com.google.appinventor.client.explorer.project.Project;
import com.google.appinventor.client.utils.Uploader;
import com.google.appinventor.client.youngandroid.TextValidators;
import com.google.appinventor.client.widgets.LabeledTextBox;
import com.google.appinventor.client.widgets.Validator;
import com.google.appinventor.shared.rpc.ServerLayout;
import com.google.appinventor.shared.rpc.UploadResponse;
import com.google.appinventor.shared.rpc.project.UserProject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * Wizard for uploading previously archived (downloaded) projects.
 *
 */
public class ProjectUploadWizard extends Wizard {
  // Project archive extension
  private static final String PROJECT_ARCHIVE_EXTENSION = ".aia";

  /**
   * Creates a new project upload wizard.
   */
  public ProjectUploadWizard() {
    super(MESSAGES.projectUploadWizardCaption(), true, false);

    // Initialize UI
    final FileUpload upload = new FileUpload();
    upload.setName(ServerLayout.UPLOAD_PROJECT_ARCHIVE_FORM_ELEMENT);
    upload.getElement().setAttribute("accept", PROJECT_ARCHIVE_EXTENSION);
    setStylePrimaryName("ode-DialogBox");
    LabeledTextBox projectPackageNameTextBox = new LabeledTextBox(MESSAGES.projectPackageNameLabel(), new Validator() {
      @Override
      public boolean validate(String value) {
        errorMessage = TextValidators.getPackageErrorMessage(value);
        if (errorMessage.length()>0){
          disableOkButton();
          return false;
        }
        errorMessage = "";
        enableOkButton();
        return true;
      }

      @Override
      public String getErrorMessage() {
        return errorMessage;
      }
    });
  //Set initially read-only so user can proceed with default behavior without interaction
    projectPackageNameTextBox.setReadOnly(true);

    //Handle the Enter and Esc keys in the textbox
    projectPackageNameTextBox.getTextBox().addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        int keyCode = event.getNativeKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
          handleOkClick();
        } else if (keyCode == KeyCodes.KEY_ESCAPE) {
          handleCancelClick();
        }
      }
    });

    //Handle validation of the given packageName
    projectPackageNameTextBox.getTextBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) { //Validate the text each time a key is lifted
        projectPackageNameTextBox.validate();
      }
    });

    CheckBox projectDefaultPackageName = new CheckBox(MESSAGES.projectDefaultPackageNameLabel());
    projectDefaultPackageName.setValue(true);
    projectDefaultPackageName.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        boolean useDefaultPackageName = ((CheckBox) event.getSource()).getValue();
        projectPackageNameTextBox.setReadOnly(useDefaultPackageName);
        if(useDefaultPackageName) {
          enableOkButton();
        } else {
          projectPackageNameTextBox.validate();
        }
      }
    });
    VerticalPanel panel = new VerticalPanel();
    panel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
    panel.add(upload);
    panel.add(projectDefaultPackageName);
    panel.add(projectPackageNameTextBox);
    addPage(panel);

    // Create finish command (upload a project archive)
    initFinishCommand(new Command() {
      @Override
      public void execute() {
        String filename = upload.getFilename();
        boolean useDefault = projectDefaultPackageName.getValue();
        String tmpPkg = projectPackageNameTextBox.getText();
        final String packageName = !useDefault && 
            TextValidators.isValidAppPackageName(tmpPkg) ? tmpPkg : "";
        if (filename.endsWith(PROJECT_ARCHIVE_EXTENSION)) {
          // Strip extension and leading path off filename. We need to support both Unix ('/') and
          // Windows ('\\') path separators. File.pathSeparator is not available in GWT.
          filename = filename.substring(0, filename.length() - PROJECT_ARCHIVE_EXTENSION.length()).
              substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')) + 1);

          // Make sure the project name is legal and unique.
          if (TextValidators.checkNewProjectName(filename, true) 
                      != TextValidators.ProjectNameStatus.SUCCESS) {
         
            // Show Dialog Box and rename the project
            new RequestNewProjectNameWizard(new RequestProjectNewNameInterface() {
              @Override
              public void getNewName(String name) {
                upload(upload, name, packageName);
              }
            }, filename, true);
  
          } else {
            upload(upload, filename, packageName);
          }
        } else {
          Window.alert(MESSAGES.notProjectArchiveError());
          center();
        }
      }
    });
  }
  
  private void upload(FileUpload upload, String filename, String packageName) {
    String uploadUrl = GWT.getModuleBaseURL() + ServerLayout.UPLOAD_SERVLET + "/"
        + ServerLayout.UPLOAD_PROJECT + "/" + filename;
    Uploader.getInstance().upload(upload, uploadUrl, packageName,
        new OdeAsyncCallback<UploadResponse>(
        // failure message
        MESSAGES.projectUploadError()) {
          @Override
          public void onSuccess(UploadResponse uploadResponse) {
            switch (uploadResponse.getStatus()) {
              case SUCCESS:
                String info = uploadResponse.getInfo();
                UserProject userProject = UserProject.valueOf(info);
                Ode ode = Ode.getInstance();
                Project uploadedProject = ode.getProjectManager().addProject(userProject);
                ode.openYoungAndroidProjectInDesigner(uploadedProject);
                break;
              case NOT_PROJECT_ARCHIVE:
                // This may be a "severe" error; but in the
                // interest of reducing the number of red errors, the 
                // line has been changed to report info not an error.
                // This error is triggered when the user attempts to
                // upload a zip file that is not a project.
                ErrorReporter.reportInfo(MESSAGES.notProjectArchiveError());
                break;
              default:
                ErrorReporter.reportError(MESSAGES.projectUploadError());
                break;
            }
          }
      });
  }

  @Override
  public void show() {
    super.show();
    // Wizard size (having it resize between page changes is quite annoying)
    int width = 340;
    int height = 120;
    this.center();

    setPixelSize(width, height);
    super.setPagePanelHeight(160);
  }
}
