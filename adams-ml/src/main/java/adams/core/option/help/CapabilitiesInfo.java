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

/*
 * CapabilitiesInfo.java
 * Copyright (C) 2018 University of Waikato, Hamilton, NZ
 */

package adams.core.option.help;

import adams.core.Utils;
import adams.ml.capabilities.Capabilities;
import adams.ml.capabilities.CapabilitiesHandler;
import nz.ac.waikato.cms.locator.ClassLocator;

/**
 <!-- globalinfo-start -->
 * Generates help for classes that implement the adams.ml.capabilities.CapabilitiesHandler interface.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @see CapabilitiesHandler
 */
public class CapabilitiesInfo
  extends AbstractHelpGenerator {

  private static final long serialVersionUID = -3885494293535045819L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
      "Generates help for classes that implement the "
	+ Utils.classToString(CapabilitiesHandler.class) + " interface.";
  }

  /**
   * Checks whether the generator handles this class.
   *
   * @param cls		the class to check
   * @return		true if it can handle the class
   */
  @Override
  public boolean handles(Class cls) {
    return ClassLocator.matches(CapabilitiesHandler.class, cls);
  }

  /**
   * Generates the help for the object in the requested format.
   *
   * @param obj		the object to generate the help for
   * @param format	the format of the output
   * @return		the generated help
   */
  @Override
  public String generate(Object obj, HelpFormat format) {
    StringBuilder	result;
    Capabilities 	caps;

    result = new StringBuilder();

    caps = ((CapabilitiesHandler) obj).getCapabilities();
    if (caps != null) {
      switch (format) {
	case PLAIN_TEXT:
	  result.append("Capabilities\n");
	  result.append(caps.toString());
	  result.append("\n\n");
	  break;

	case HTML:
	  result.append("<h2>Capabilities</h2>\n");
	  result.append("<p>" + toHTML(caps.toString(), true) + "</p>\n");
	  result.append("<br>\n");
	  result.append("\n");
	  break;

	default:
	  throw new IllegalStateException("Unhandled format: " + format);
      }
    }

    return result.toString();
  }
}
