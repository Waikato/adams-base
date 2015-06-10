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
 * WekaInstancesRenderer.java
 * Copyright (C) 2015 University of Waikato, Hamilton, NZ
 */

package adams.gui.visualization.debug.objectrenderer;

import adams.core.ClassLocator;
import adams.gui.core.BaseScrollPane;
import weka.core.Instance;
import weka.core.Instances;
import weka.gui.arffviewer.ArffSortedTableModel;
import weka.gui.arffviewer.ArffTable;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Renders Weka Instances/Instance objects.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class WekaInstancesRenderer
  extends AbstractObjectRenderer {

  private static final long serialVersionUID = -3528006886476495175L;

  /**
   * Checks whether the renderer can handle the specified class.
   *
   * @param cls		the class to check
   * @return		true if the renderer can handle this type of object
   */
  @Override
  public boolean handles(Class cls) {
    return ClassLocator.isSubclass(Instances.class, cls)
      || ClassLocator.hasInterface(Instance.class, cls);
  }

  /**
   * Performs the actual rendering.
   *
   * @param obj		the object to render
   * @param panel	the panel to render into
   * @return		null if successful, otherwise error message
   */
  @Override
  protected String doRender(Object obj, JPanel panel) {
    Instance 		inst;
    Instances 		data;
    ArffTable		table;
    BaseScrollPane	scrollPane;
    PlainTextRenderer	plain;

    if (obj instanceof Instances) {
      data = (Instances) obj;
      table = new ArffTable(new ArffSortedTableModel(data));
      scrollPane = new BaseScrollPane(table);
      panel.add(scrollPane, BorderLayout.CENTER);
    }
    else {
      inst = (Instance) obj;
      if (inst.dataset() != null) {
	data = new Instances(inst.dataset());
	data.add((Instance) inst.copy());
	table      = new ArffTable(new ArffSortedTableModel(data));
	scrollPane = new BaseScrollPane(table);
	panel.add(scrollPane, BorderLayout.CENTER);
      }
      else {
	plain = new PlainTextRenderer();
	plain.render(obj, panel);
      }
    }

    return null;
  }
}
