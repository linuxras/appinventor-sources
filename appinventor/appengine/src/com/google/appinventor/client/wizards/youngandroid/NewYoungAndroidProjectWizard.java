// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.wizards.youngandroid;


import com.google.appinventor.client.Ode;

import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.explorer.project.Project;
import com.google.appinventor.client.explorer.youngandroid.ProjectToolbar;
import com.google.appinventor.client.tracking.Tracking;
import com.google.appinventor.client.widgets.LabeledTextBox;
import com.google.appinventor.client.widgets.Validator;
import com.google.appinventor.client.wizards.NewProjectWizard;
import com.google.appinventor.client.youngandroid.TextValidators;
import com.google.appinventor.common.utils.StringUtils;
import com.google.appinventor.shared.rpc.project.youngandroid.NewYoungAndroidProjectParameters;
import com.google.appinventor.shared.rpc.project.youngandroid.YoungAndroidProjectNode;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.CheckBox;


/**
 * Wizard for creating new Young Android projects.
 *
 * @author markf@google.com (Mark Friedman)
 */
public final class NewYoungAndroidProjectWizard extends NewProjectWizard {
  // UI element for project name
  private LabeledTextBox projectNameTextBox;
  private LabeledTextBox projectPackageNameTextBox;
  private CheckBox projectDefaultPackageName;
  private boolean useDefaultPackageName = true;

  /**
   * Creates a new YoungAndroid project wizard.
   */
  public NewYoungAndroidProjectWizard(final ProjectToolbar toolbar) {
    super(MESSAGES.newYoungAndroidProjectWizardCaption());

    // Initialize the UI
    setStylePrimaryName("ode-DialogBox");

    projectNameTextBox = new LabeledTextBox(MESSAGES.projectNameLabel(), new Validator() {
      @Override
      public boolean validate(String value) {
        errorMessage = TextValidators.getErrorMessage(value);
        if (errorMessage.length()>0){
          disableOkButton();
          return false;
        }
          errorMessage = TextValidators.getWarningMessages(value);
          enableOkButton();
          return true;
      }

      @Override
      public String getErrorMessage() {
        return errorMessage;
      }
    });

    projectNameTextBox.getTextBox().addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        int keyCode = event.getNativeKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
          if(useDefaultPackageName) {
            handleOkClick();
          }
        } else if (keyCode == KeyCodes.KEY_ESCAPE) {
          handleCancelClick();
        }
      }
    });

    projectNameTextBox.getTextBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) { //Validate the text each time a key is lifted
        projectNameTextBox.validate();
        if(useDefaultPackageName) {
          projectPackageNameTextBox.setText(StringUtils.getProjectPackage(
              Ode.getInstance().getUser().getUserEmail(), projectNameTextBox.getText()));
        }
      }
    });

    projectPackageNameTextBox = new LabeledTextBox(MESSAGES.projectPackageNameLabel(), new Validator() {
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
    projectPackageNameTextBox.setReadOnly(useDefaultPackageName);

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

    projectPackageNameTextBox.getTextBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) { //Validate the text each time a key is lifted
        projectPackageNameTextBox.validate();
      }
    });

    projectDefaultPackageName = new CheckBox(MESSAGES.projectDefaultPackageNameLabel());
    projectDefaultPackageName.setValue(useDefaultPackageName);
    //Handle clicks on checkbox
    projectDefaultPackageName.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        useDefaultPackageName = ((CheckBox) event.getSource()).getValue();
        projectPackageNameTextBox.setReadOnly(useDefaultPackageName);
        if(useDefaultPackageName) {
          enableOkButton();
        }
        else
        {
          projectNameTextBox.validate();
          projectPackageNameTextBox.validate();
        }
      }
    });
    VerticalPanel page = new VerticalPanel();

    page.add(projectNameTextBox);
    page.add(projectDefaultPackageName);
    page.add(projectPackageNameTextBox);
    addPage(page);

    // Create cancel command handler. This handler
    // arranges to re-enable the project start button
    // Note that toolbar will be null if we are called
    // from the Project menu instead of the Start button
    // on the project toolbar

    if (toolbar != null) {
      initCancelCommand(new Command() {
        @Override
        public void execute() {
          toolbar.enableStartButton();
        }
      });
    }

    // Create finish command (create a new Young Android project)
    initFinishCommand(new Command() {
      @Override
      public void execute() {
        String projectName = projectNameTextBox.getText().trim();
        projectName = projectName.replaceAll("( )+", " ").replace(" ", "_");
        String packageName = projectPackageNameTextBox.getText().trim();
        packageName = packageName.replaceAll("( )+", " ").replace(" ", "_");
        if (TextValidators.checkNewProjectName(projectName) 
              == TextValidators.ProjectNameStatus.SUCCESS) {
          if(useDefaultPackageName) {
            packageName = StringUtils.getProjectPackage(
                Ode.getInstance().getUser().getUserEmail(), projectName);
          }
          if(TextValidators.isValidAppPackageName(packageName)) {
            NewYoungAndroidProjectParameters parameters = new NewYoungAndroidProjectParameters(
              packageName);
            NewProjectCommand callbackCommand = new NewProjectCommand() {
              @Override
              public void execute(final Project project) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                      if (Ode.getInstance().screensLocked()) { // Wait until I/O finished
                        Scheduler.get().scheduleDeferred(this); // on other project
                      } else {
                        Ode.getInstance().openYoungAndroidProjectInDesigner(project);
                      }
                    }
                  });
              }
            };

            createNewProject(YoungAndroidProjectNode.YOUNG_ANDROID_PROJECT_TYPE, projectName,
              parameters, callbackCommand);
            Tracking.trackEvent(Tracking.PROJECT_EVENT, Tracking.PROJECT_ACTION_NEW_YA, projectName);          
          } else {
            show();
            center();
            return;
          }
        } else {
          show();
          center();
          return;
        }
      }
    });
  }

  @Override
  public void show() {
    super.show();
    // Wizard size (having it resize between page changes is quite annoying)
    int width = 340;
    int height = 180;
    this.center();

    setPixelSize(width, height);
    super.setPagePanelHeight(200);

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        projectNameTextBox.setFocus(true);
        projectNameTextBox.selectAll();
      }
    });
  }
}
