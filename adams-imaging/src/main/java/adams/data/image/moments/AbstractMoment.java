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
 * AbstractMoment.java
 * Copyright (C) 2016 University of Waikato, Hamilton, New Zealand
 */

package adams.data.image.moments;

import adams.core.option.AbstractOptionHandler;
import sun.plugin.dom.exception.InvalidStateException;

/**
 * Top level interface for moment classes
 *
 * @author sjb90
 * @version $Revision$
 */
public abstract class AbstractMoment<T> extends AbstractOptionHandler {
  /**
   * Takes an image of type T and returns a boolean matrix that can be used for moments
   * @param img
   * @return the boolean matrix representing the image
   */
  protected abstract boolean[][] imageToMatrix(T img);

  public double calculate(T img) {
    if (check(img))
    	return doCalculate(imageToMatrix(img));
    else
      throw new InvalidStateException("Image is invalid");
  }

  protected abstract boolean check(T img);

  protected abstract double doCalculate(boolean[][] img);
}
