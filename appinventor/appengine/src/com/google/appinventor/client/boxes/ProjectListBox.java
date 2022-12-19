// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.boxes;

import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.explorer.youngandroid.ProjectList;
import com.google.appinventor.client.widgets.boxes.Box;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.appinventor.client.explorer.project.Project;


/**
 * Box implementation for project list.
 *
 */
public final class ProjectListBox extends Box {

  // Singleton project explorer box instance (only one project explorer allowed)
  private static final ProjectListBox INSTANCE = new ProjectListBox();

  // Project list for young android
  private final ProjectList plist;

  /**
   * Returns the singleton projects list box.
   *
   * @return  project list box
   */
  public static ProjectListBox getProjectListBox() {
    return INSTANCE;
  }

  /**
   * Creates new project list box.
   */
  private ProjectListBox() {
    super(MESSAGES.projectListBoxCaption(),
        300,    // height
        false,  // minimizable
        false); // removable

    plist = new ProjectList() {
      //Monitor add/remove on the project list to keep track of height
      @Override
      public void onProjectAdded(Project proj) {
        super.onProjectAdded(proj);
        checkUpdateHeight();
      }
      @Override
      public void onProjectDeleted(Project proj) {
        super.onProjectDeleted(proj);
        checkUpdateHeight();
      }
      @Override
      public void onProjectsLoaded() {
        super.onProjectsLoaded();
        checkUpdateHeight();
      }
      @Override
      public void onTrashProjectRestored(Project proj) {
        super.onTrashProjectRestored(proj);
        checkUpdateHeight();
      }
    };
    //Monitor window resizing the size this panel
    Window.addResizeHandler(new ResizeHandler() {
      public void onResize(ResizeEvent event) {
        checkUpdateHeight();
      }
    });
    setContent(plist);
    setStyleName("ode-ProjectListBox");
  }
  private final void checkUpdateHeight() {
    int docHeight = Document.get().getScrollHeight();
    int myHeight = getBody().getOffsetHeight();
    int maxPHeight = (docHeight - (105 + 43));
    //log("Resize triggered docHeight: "+docHeight+", maxHeight: "+maxPHeight+", myHeight: "+myHeight);
    if(myHeight >= maxPHeight) {
      addStyleName("ode-ProjectListBox-overflow");
    }
    else
    {
      removeStyleName("ode-ProjectListBox-overflow");
    }
  }
  
  public static native void log(String msg) /*-{
    $wnd.console.log(msg);
  }-*/;

  /**
   * Returns project list associated with projects explorer box.
   *
   * @return  project list
   */
  public ProjectList getProjectList() {
     return plist;
  }

  public void loadProjectList () {
    plist.getSelectedProjects().clear();
    plist.refreshTable(false, false);
    this.setCaption(MESSAGES.projectListBoxCaption());
  }

  public void loadTrashList() {
    plist.getSelectedProjects().clear();
    plist.refreshTable(false, true);
    this.setCaption(MESSAGES.trashprojectlistbox());
  }
}
