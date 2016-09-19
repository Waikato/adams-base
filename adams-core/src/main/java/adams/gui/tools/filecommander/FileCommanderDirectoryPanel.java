/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * FileCommanderDirectoryPanel.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package adams.gui.tools.filecommander;

import adams.core.ClassLister;
import adams.core.base.BaseRegExp;
import adams.core.io.PlaceholderFile;
import adams.gui.chooser.AbstractChooserPanelWithIOSupport;
import adams.gui.chooser.DirectoryChooserPanel;
import adams.gui.core.BasePanel;
import adams.gui.core.ConsolePanel;
import adams.gui.core.FilePanel;
import adams.gui.core.FilePanel.FileDoubleClickEvent;
import adams.gui.core.GUIHelper;
import adams.gui.dialog.SimplePreviewBrowserDialog;
import adams.gui.tools.FileCommanderPanel;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;

/**
 * One side of the FileCommander.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class FileCommanderDirectoryPanel
  extends BasePanel {

  private static final long serialVersionUID = 8639721023952554657L;

  /** the owner. */
  protected FileCommanderPanel m_Owner;

  /** the left side. */
  protected FilePanel m_Files;

  /** the panel for the combox of choosers and current chooser. */
  protected JPanel m_PanelChooser;

  /** the combobox with the available directory chooser panels. */
  protected JComboBox<AbstractChooserPanelWithIOSupport> m_Choosers;

  /** the left dir panel. */
  protected AbstractChooserPanelWithIOSupport m_Dir;

  /** the change listener for the left dir. */
  protected ChangeListener m_DirChangeListener;

  /** whether to ignore changes. */
  protected boolean m_IgnoreChanges;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_IgnoreChanges = false;
  }

  /**
   * Initializes the widgets.
   */
  @Override
  protected void initGUI() {
    super.initGUI();

    setLayout(new BorderLayout());

    // left
    m_Files = new FilePanel(true);
    m_Files.startUpdate();
    m_Files.setSearchVisible(true);
    m_Files.setListDirs(true);
    m_Files.setMultiSelection(true);
    m_Files.finishUpdate();
    m_Files.addSelectionChangeListener((ChangeEvent e) -> setActive());
    m_Files.addDirectoryChangeListener((ChangeEvent e) -> {
      m_IgnoreChanges = true;
      m_Dir.setCurrentDirectory(m_Files.getCurrentDir());
      m_IgnoreChanges = false;
    });
    m_Files.addFileDoubleClickListener((FileDoubleClickEvent e) -> view(e.getFile()));

    m_Choosers = new JComboBox<>();
    addChooserPanels(m_Choosers);
    m_Choosers.addActionListener((ActionEvent e) -> updateChooser());
    m_Choosers.setVisible(m_Choosers.getItemCount() > 1);

    m_Dir = new DirectoryChooserPanel();

    m_PanelChooser = new JPanel(new BorderLayout(5, 5));
    m_PanelChooser.add(m_Choosers, BorderLayout.WEST);
    m_PanelChooser.add(m_Dir, BorderLayout.CENTER);
    setLayout(new BorderLayout(5, 5));

    add(m_Files, BorderLayout.CENTER);
    add(m_PanelChooser, BorderLayout.NORTH);
  }

  /**
   * Adds the choosers to the combobox.
   *
   * @param combobox	the combobox to fil
   */
  protected void addChooserPanels(JComboBox<AbstractChooserPanelWithIOSupport> combobox) {
    AbstractChooserPanelWithIOSupport				chooser;
    AbstractChooserPanelWithIOSupport				selected;
    DefaultComboBoxModel<AbstractChooserPanelWithIOSupport> 	model;

    selected = null;
    model    = new DefaultComboBoxModel<>();
    for (Class cls : ClassLister.getSingleton().getClasses(AbstractChooserPanelWithIOSupport.class)) {
      try {
	chooser = (AbstractChooserPanelWithIOSupport) cls.newInstance();
	model.addElement(chooser);
	if (cls == DirectoryChooserPanel.class)
	  selected = chooser;
      }
      catch (Exception e) {
	ConsolePanel.getSingleton().append(Level.SEVERE, "Failed to instantiate: " + cls.getName(), e);
      }
    }

    combobox.setModel(model);
    if (selected == null)
      combobox.setSelectedIndex(0);
    else
      combobox.setSelectedItem(selected);
  }

  /**
   * Updates the chooser (using currently selected one in combobox) and
   * dependent widgets.
   */
  protected void updateChooser() {
    m_Dir.getParent().remove(m_Dir);
    m_Dir.removeChangeListener(m_DirChangeListener);
    m_Dir = (AbstractChooserPanelWithIOSupport) m_Choosers.getSelectedItem();
    m_Dir.addChangeListener(m_DirChangeListener);
    m_PanelChooser.add(m_Dir, BorderLayout.CENTER);
    m_Files.setDirectoryLister(m_Dir.getDirectoryLister());
    m_PanelChooser.invalidate();
    m_PanelChooser.validate();
    m_PanelChooser.doLayout();
  }

  /**
   * Sets the owner.
   *
   * @param value	the owner
   */
  public void setOwner(FileCommanderPanel value) {
    m_Owner = value;
  }

  /**
   * Returns the owner.
   *
   * @return		the owner, null if none set
   */
  public FileCommanderPanel getOwner() {
    return m_Owner;
  }

  /**
   * Sets the listener to use.
   *
   * @param value	the listener
   */
  public void setDirChangeListener(ChangeListener value) {
    if (m_DirChangeListener != null)
      m_Dir.removeChangeListener(m_DirChangeListener);
    m_DirChangeListener = value;
    m_Dir.addChangeListener(m_DirChangeListener);
  }

  /**
   * Returns the current listener in use.
   *
   * @return		the listener, null if none set
   */
  public ChangeListener getDirChangeListener() {
    return m_DirChangeListener;
  }

  /**
   * Sets this panel active in the FileCommander.
   */
  public void setActive() {
    m_Owner.setActive(getFilePanel());
  }

  /**
   * Views the file.
   *
   * @param file	the file to view, ignored if null
   */
  public void view(File file) {
    SimplePreviewBrowserDialog dialog;

    if (file == null)
      return;

    if (getParentDialog() != null)
      dialog = new SimplePreviewBrowserDialog(getParentDialog(), ModalityType.MODELESS);
    else
      dialog = new SimplePreviewBrowserDialog(getParentFrame(), false);
    dialog.setDefaultCloseOperation(SimplePreviewBrowserDialog.DISPOSE_ON_CLOSE);
    dialog.open(new PlaceholderFile(m_Files.getSelectedFile()));
    dialog.setSize(GUIHelper.getDefaultDialogDimension());
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  /**
   * Reloads the files.
   */
  public void reload() {
    m_Files.reload();
  }

  /**
   * Sets the current directory.
   *
   * @param dir		the directory to use
   */
  public void setDirectory(File dir) {
    m_Files.setCurrentDir(dir.getAbsolutePath());
    m_Dir.setCurrentDirectory(dir.getAbsolutePath());
  }

  /**
   * Returns the current directory.
   *
   * @return		the current directory
   */
  public File getDirectory() {
    return new File(m_Files.getCurrentDir());
  }

  /**
   * Prompts the user to enter a regular expression as filter.
   */
  public void setFilter() {
    BaseRegExp regExp;
    String	input;

    regExp = m_Files.getFilter();

    input = GUIHelper.showInputDialog(this, "Please enter the filter (regular expression)", regExp.getValue());
    if (input == null)
      return;
    if (!regExp.isValid(input)) {
      GUIHelper.showErrorMessage(this, "Invalid regular expression:\n" + input);
      return;
    }

    regExp.setValue(input);
    m_Files.setFilter(regExp);
  }

  /**
   * Returns the panel with the files.
   *
   * @return		the panel
   */
  public FilePanel getFilePanel() {
    return m_Files;
  }

  /**
   * Returns the chooser panel.
   *
   * @return		the panel
   */
  public AbstractChooserPanelWithIOSupport getChooserPanel() {
    return m_Dir;
  }
}
