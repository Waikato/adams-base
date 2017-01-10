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
 * SpatialPyramidAutoColorCorrelogram.java
 * Copyright (C) 2017 University of Waikato, Hamilton, New Zealand
 */

package adams.data.lire.features;

import adams.core.TechnicalInformation;
import adams.core.TechnicalInformationHandler;
import adams.data.featureconverter.HeaderDefinition;
import adams.data.image.BufferedImageContainer;
import adams.data.image.BufferedImageHelper;
import adams.data.image.features.AbstractBufferedImageFeatureGenerator;
import adams.data.report.DataType;
import adams.data.statistics.StatUtils;
import net.semanticmetadata.lire.imageanalysis.features.global.spatialpyramid.SPACC;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 <!-- globalinfo-start -->
 * Generates features using net.semanticmetadata.lire.imageanalysis.features.global.spatialpyramid.SPACC.<br>
 * For more information, see:<br>
 * Mathias Lux, Savvas A. Chatzichristofis: LIRE: Lucene Image Retrieval - An Extensible Java CBIR Library. In: 16th ACM International Conference on Multimedia, 1085-1088, 2008.<br>
 * <br>
 * Lux, Mathias: Content Based Image Retrieval with LIRe. In: 19th ACM International Conference on Multimedia, 735-738, 2011.<br>
 * <br>
 * Mathias Lux, Oge Marques (2013). . Morgan Claypool.<br>
 * For more information on the LIRE project, see:<br>
 * http:&#47;&#47;www.lire-project.net&#47;
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- technical-bibtex-start -->
 * <pre>
 * &#64;inproceedings{Lux2008,
 *    author = {Mathias Lux and Savvas A. Chatzichristofis},
 *    booktitle = {16th ACM International Conference on Multimedia},
 *    pages = {1085-1088},
 *    publisher = {ACM},
 *    title = {LIRE: Lucene Image Retrieval - An Extensible Java CBIR Library},
 *    year = {2008},
 *    URL = {http:&#47;&#47;doi.acm.org&#47;10.1145&#47;1459359.1459577}
 * }
 * 
 * &#64;inproceedings{Lux2011,
 *    author = {Lux, Mathias},
 *    booktitle = {19th ACM International Conference on Multimedia},
 *    pages = {735-738},
 *    publisher = {ACM},
 *    title = {Content Based Image Retrieval with LIRe},
 *    year = {2011},
 *    URL = {http:&#47;&#47;doi.acm.org&#47;10.1145&#47;2072298.2072432}
 * }
 * 
 * &#64;book{Lux2013,
 *    author = {Mathias Lux and Oge Marques},
 *    booktitle = {Visual Information Retrieval using Java and LIRE},
 *    publisher = {Morgan Claypool},
 *    year = {2013},
 *    ISBN = {9781608459186}
 * }
 * </pre>
 * <br><br>
 <!-- technical-bibtex-end -->
 * 
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-converter &lt;adams.data.featureconverter.AbstractFeatureConverter&gt; (property: converter)
 * &nbsp;&nbsp;&nbsp;The feature converter to use to produce the output data.
 * &nbsp;&nbsp;&nbsp;default: adams.data.featureconverter.SpreadSheet -data-row-type adams.data.spreadsheet.DenseDataRow -spreadsheet-type adams.data.spreadsheet.DefaultSpreadSheet
 * </pre>
 * 
 * <pre>-field &lt;adams.data.report.Field&gt; [-field ...] (property: fields)
 * &nbsp;&nbsp;&nbsp;The fields to add to the output.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-notes &lt;adams.core.base.BaseString&gt; [-notes ...] (property: notes)
 * &nbsp;&nbsp;&nbsp;The notes to add as attributes to the generated data, eg 'PROCESS INFORMATION'
 * &nbsp;&nbsp;&nbsp;.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 9196 $
 */
public class SpatialPyramidAutoColorCorrelogram
  extends AbstractBufferedImageFeatureGenerator
  implements TechnicalInformationHandler {

  /** for serialization. */
  private static final long serialVersionUID = -218939305183964139L;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
        "Generates features using " + SPACC.class.getName() + ".\n"
        + "For more information, see:\n"
        + getTechnicalInformation().toString() + "\n"
        + "For more information on the LIRE project, see:\n"
        + "http://www.lire-project.net/";
  }

  /**
   * Returns an instance of a TechnicalInformation object, containing detailed
   * information about the technical background of this class, e.g., paper
   * reference or book this class is based on.
   * 
   * @return the technical information about this class
   */
  @Override
  public TechnicalInformation getTechnicalInformation() {
    return Citation.getTechnicalInformation();
  }

  /**
   * Creates the header from a template image.
   *
   * @param img		the image to act as a template
   * @return		the generated header
   */
  @Override
  public HeaderDefinition createHeader(BufferedImageContainer img) {
    HeaderDefinition		result;
    BufferedImage		image;
    double[]			histo;
    int				i;
    SPACC	features;

    image    = BufferedImageHelper.convert(img.getImage(), BufferedImage.TYPE_3BYTE_BGR);
    features = new SPACC();
    features.extract(image);
    histo    = features.getFeatureVector();

    result   = new HeaderDefinition();
    for (i = 0; i < histo.length; i++)
      result.add("SPACC-" + (i+1), DataType.NUMERIC);

    return result;
  }

  /**
   * Performs the actual feature generation.
   *
   * @param img		the image to process
   * @return		the generated features
   */
  @Override
  public List<Object>[] generateRows(BufferedImageContainer img) {
    List<Object>[]		result;
    BufferedImage		image;
    double[]			histo;
    SPACC	features;

    image     = BufferedImageHelper.convert(img.getImage(), BufferedImage.TYPE_3BYTE_BGR);
    features  = new SPACC();
    features.extract(image);
    histo     = features.getFeatureVector();
    result    = new List[1];
    result[0] = new ArrayList<>();
    result[0].addAll(Arrays.asList(StatUtils.toNumberArray(histo)));

    return result;
  }
}
