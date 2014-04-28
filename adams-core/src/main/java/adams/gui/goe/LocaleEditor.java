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
 *    LocaleEditor.java
 *    Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 *
 */

package adams.gui.goe;

import java.awt.Container;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;

import adams.core.Utils;
import adams.core.management.LocaleHelper;
import adams.core.option.AbstractOption;
import adams.gui.dialog.ApprovalDialog;

/**
 * A PropertyEditor that displays locales. {@link LocaleHelper#LOCALE_DEFAULT}
 * is a placeholder for the system's default locale.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class LocaleEditor
  extends PropertyEditorSupport
  implements MultiSelectionEditor {
  
  /**
   * Returns the locale as string.
   *
   * @param option	the current option
   * @param object	the locale object to convert
   * @return		the generated string
   */
  public static String toString(AbstractOption option, Object object) {
    return LocaleHelper.toString((Locale) object);
  }

  /**
   * Returns a locale generated from the string.
   *
   * @param option	the current option
   * @param str		the string to convert to a locale
   * @return		the generated locale
   */
  public static Object valueOf(AbstractOption option, String str) {
    return LocaleHelper.valueOf(str);
  }

  /**
   * Returns a description of the property value as java source.
   *
   * @return 		a value of type 'String'
   */
  @Override
  public String getJavaInitializationString() {
    String	result;

    result = LocaleHelper.class.getName() + ".valueOf(" + ((Locale) getValue()).toString() + ")";

    return result;
  }

  /**
   * Gets the current value as text.
   *
   * @return 		a value of type 'String'
   */
  @Override
  public String getAsText() {
    return LocaleHelper.toString((Locale) getValue());
  }

  /**
   * Sets the current property value as text.
   *
   * @param text 	the text of the selected tag.
   */
  @Override
  public void setAsText(String text) {
    setValue(LocaleHelper.valueOf(text));
  }

  /**
   * Gets the list of tags that can be selected from.
   *
   * @return 		an array of string tags.
   */
  @Override
  public String[] getTags() {
    return LocaleHelper.getIDs();
  }

  /**
   * Returns the selected objects.
   *
   * @param parent	the parent container
   * @return		the objects
   */
  @Override
  public Object[] getSelectedObjects(Container parent) {
    Object[]			result;
    MultiLineValueDialog	dialog;
    Vector<String>		lines;
    int				i;

    dialog = new MultiLineValueDialog();
    dialog.setInfoText("Enter the string representations, one per line:");
    dialog.setLocationRelativeTo(parent);
    dialog.setVisible(true);

    if (dialog.getOption() == ApprovalDialog.APPROVE_OPTION) {
      lines = new Vector<String>(Arrays.asList(dialog.getContent().split("\n")));
      Utils.removeEmptyLines(lines);
      result = (Object[]) Array.newInstance(Locale.class, lines.size());
      for (i = 0; i < lines.size(); i++)
	Array.set(result, i, LocaleHelper.valueOf(lines.get(i)));
    }
    else {
      result = (Object[]) Array.newInstance(Locale.class, 0);
    }

    return result;
  }
}
