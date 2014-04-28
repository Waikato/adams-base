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
 * AddBookmark.java
 * Copyright (C) 2012 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.flow.tree.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.tree.TreePath;

import adams.gui.flow.tree.Node;
import adams.gui.flow.tree.StateContainer;

/**
 * For adding a Breakpoint after the current position.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class AddBookmark
  extends AbstractTreePopupMenuItem {

  /** for serialization. */
  private static final long serialVersionUID = -1592828498173394415L;

  /**
   * Creates the menuitem to add to the menus.
   * 
   * @param state	the current state of the tree
   * @return		the menu item, null if not possible to use
   */
  @Override
  protected JMenuItem getMenuItem(final StateContainer state) {
    JMenuItem	result;
    
    result = new JMenuItem("Add bookmark");
    result.setEnabled(getShortcut().stateApplies(state));
    result.setAccelerator(getShortcut().getKeyStroke());
    result.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	getShortcut().execute(state);
      }
    });
    
    return result;
  }

  /**
   * Creates the associated shortcut.
   * 
   * @return		the shortcut, null if not used
   */
  @Override
  protected AbstractTreeShortcut newShortcut() {
    return new AbstractTreeShortcut() {
      private static final long serialVersionUID = -7897333416159785241L;
      @Override
      protected String getTreeShortCutKey() {
	return "Bookmark.Add";
      }
      @Override
      public boolean stateApplies(StateContainer state) {
	return (state.numSel > 0);
      }
      @Override
      protected void doExecute(StateContainer state) {
	for (TreePath path: state.selPaths) {
	  Node node = (Node) path.getLastPathComponent();
	  node.setBookmarked(true);
	  state.tree.nodeStructureChanged(node);
	}
      }
    };
  }
}
