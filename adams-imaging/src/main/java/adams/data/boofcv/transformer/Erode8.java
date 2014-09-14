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
 * Erode8.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package adams.data.boofcv.transformer;

import adams.core.BoofCVHelper;
import adams.data.boofcv.BoofCVImageContainer;
import adams.data.boofcv.BoofCVImageType;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.struct.image.ImageUInt8;

/**
 <!-- globalinfo-start -->
 * Applies the BoofCV erode8 algorithm to a binary image.
 * <p/>
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
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 78 $
 */
public class Erode8
  extends AbstractBoofCVTransformer {

  /** for serialization. */
  private static final long serialVersionUID = -95759083210389706L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Applies the BoofCV erode8 algorithm to a binary image.";
  }
  
  /**
   * Performs no transformation at all, just returns the input.
   *
   * @param img		the image to process (can be modified, since it is a copy)
   * @return		the copy of the image
   */
  @Override
  protected BoofCVImageContainer[] doTransform(BoofCVImageContainer img) {
    BoofCVImageContainer[]	result;
    ImageUInt8	 		input;
    ImageUInt8 			filtered;
    
    input     = (ImageUInt8) BoofCVHelper.toBoofCVImage(img.getImage(), BoofCVImageType.UNSIGNED_INT_8);
    filtered  = BinaryImageOps.erode8(input, 1, null);
    result    = new BoofCVImageContainer[1];
    result[0] = (BoofCVImageContainer) img.getHeader();
    result[0].setImage(filtered);
    
    return result;
  }
}
