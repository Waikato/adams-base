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
 * WekaPrincipalComponents.java
 * Copyright (C) 2014-2016 Dutch Sprouts, Wageningen, NL
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */
package adams.flow.transformer;

import adams.core.License;
import adams.core.QuickInfoHelper;
import adams.core.annotation.MixedCopyright;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import adams.flow.core.Token;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.PublicPrincipalComponents;

import java.util.ArrayList;

/**
 <!-- globalinfo-start -->
 * Actor that takes an instances object and carries out principal component analysis to build a model. The coefficients for the model are output in a spreadsheet
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;weka.core.Instances<br>
 * - generates:<br>
 * &nbsp;&nbsp;&nbsp;adams.data.spreadsheet.SpreadSheet<br>
 * <br><br>
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 *
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: WekaPrincipalComponents
 * </pre>
 *
 * <pre>-annotation &lt;adams.core.base.BaseText&gt; (property: annotations)
 * &nbsp;&nbsp;&nbsp;The annotations to attach to this actor.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 *
 * <pre>-skip &lt;boolean&gt; (property: skip)
 * &nbsp;&nbsp;&nbsp;If set to true, transformation is skipped and the input token is just forwarded 
 * &nbsp;&nbsp;&nbsp;as it is.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-stop-flow-on-error &lt;boolean&gt; (property: stopFlowOnError)
 * &nbsp;&nbsp;&nbsp;If set to true, the flow gets stopped in case this actor encounters an error;
 * &nbsp;&nbsp;&nbsp; useful for critical actors.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-variance-covered &lt;double&gt; (property: varianceCovered)
 * &nbsp;&nbsp;&nbsp;Retain enough PC attributes to account for this proportion of variance.
 * &nbsp;&nbsp;&nbsp;default: 0.95
 * </pre>
 *
 * <pre>-max-attributes &lt;int&gt; (property: maximumAttributes)
 * &nbsp;&nbsp;&nbsp;The maximum number of PC attributes to retain.
 * &nbsp;&nbsp;&nbsp;default: -1
 * &nbsp;&nbsp;&nbsp;minimum: -1
 * </pre>
 *
 <!-- options-end -->
 *
 * Actor that takes in an instances object containing TGA-MS data and outputs the coefficients from a principal components analysis
 *
 * @author michael.fowke
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
@MixedCopyright(
  author = "Michael Fowke",
  license = License.GPL3,
  copyright = "2014 Dutch Sprouts, Wageningen, NL"
)
public class WekaPrincipalComponents
  extends AbstractTransformer{

  /** for serialization */
  private static final long serialVersionUID = -3079556702775500196L;

  /** the variance to cover. */
  protected double m_CoverVariance;

  /** the maximum number of attributes to keep. */
  protected int m_MaxAttributes;

  protected ArrayList<Integer> m_Kept;

  protected int m_NumAttributes;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
      "Performs principal components analysis on the incoming data and outputs "
	+ "the loadsings as spreadsheet.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "variance-covered", "varianceCovered",
      0.95);

    m_OptionManager.add(
      "max-attributes", "maximumAttributes",
      -1, -1, null);
  }

  /**
   * Sets the amount of variance to account for when retaining
   * principal components.
   *
   * @param value 	the proportion of total variance to account for
   */
  public void setVarianceCovered(double value) {
    m_CoverVariance = value;
    reset();
  }

  /**
   * Gets the proportion of total variance to account for when
   * retaining principal components.
   *
   * @return 		the proportion of variance to account for
   */
  public double getVarianceCovered() {
    return m_CoverVariance;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the explorer/experimenter gui
   */
  public String varianceCoveredTipText() {
    return "Retain enough PC attributes to account for this proportion of variance.";
  }

  /**
   * Sets maximum number of PC attributes to retain.
   *
   * @param value 	the maximum number of attributes
   */
  public void setMaximumAttributes(int value) {
    m_MaxAttributes = value;
    reset();
  }

  /**
   * Gets maximum number of PC attributes to retain.
   *
   * @return 		the maximum number of attributes
   */
  public int getMaximumAttributes() {
    return m_MaxAttributes;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the explorer/experimenter gui
   */
  public String maximumAttributesTipText() {
    return "The maximum number of PC attributes to retain.";
  }

  @Override
  public String getQuickInfo() {
    String	result;

    result  = QuickInfoHelper.toString(this, "varianceCovered", m_CoverVariance, "var: ");
    result += QuickInfoHelper.toString(this, "maxAttributes", m_MaxAttributes, ", max attr: ");

    return result;
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		the Class of objects that can be processed
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Instances.class};
  }

  /**
   * Returns the class of objects that it generates.
   *
   * @return		the Class of the generated tokens
   */
  @Override
  public Class[] generates() {
    return new Class[]{SpreadSheet.class};
  }

  /**
   * Create a spreadsheet to output from the coefficients 2D array
   *
   * @param input	the underlying dataset
   * @param coeff 	The coefficients from the principal components analysis
   * @return		A spreadsheet containing the components
   */
  protected SpreadSheet createSpreadSheet(Instances input, ArrayList<ArrayList<Double>> coeff) {
    SpreadSheet result;
    Row 	row;
    int		i;
    int		n;

    result = new DefaultSpreadSheet();
    row = result.getHeaderRow();
    row.addCell("I").setContent("Index");
    row.addCell("A").setContent("Attribute");

    for (i = 0; i < coeff.size(); i++)
      row.addCell("L" + (i+1)).setContent(i+1);

    //add the first column, which will be just the number of the attribute
    for (n = 0; n < m_NumAttributes; n++) {
      row = result.addRow();
      row.addCell("I").setContent(n+1);
      row.addCell("A").setContent(input.attribute(n).name());
    }

    //each arraylist is a single column
    for (i = 0; i< coeff.size() ; i++) {
      for (n = 0; n < m_NumAttributes; n++) {
	row = result.getRow(n);

	//attribute was kept earlier
	if (m_Kept.contains(n)) {
	  int index = m_Kept.indexOf(n);
	  double value = coeff.get(i).get(index);
	  row.addCell("L" + (i+1)).setContent(value);
	}
	//attribute wasn't kept, coefficient is 0
	else {
	  row.addCell("L" + (i+1)).setContent(0);
	}
      }
    }

    return result;
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute()  {
    String 				result;
    Instances 				input;
    int					i;
    PublicPrincipalComponents 		pca;
    ArrayList<ArrayList<Double>> 	coeff;
    SpreadSheet 			sheet;

    result = null;
    input = (Instances)m_InputToken.getPayload();
    m_NumAttributes = input.numAttributes();
    if (input.classIndex() > -1)
      m_NumAttributes--;

    //the principal components will delete the attributes without any distinct values.
    //this checks which instances will be kept.
    m_Kept = new ArrayList<>();
    for (i = 0; i < input.numAttributes(); i++) {
      if (input.classIndex() == i)
	continue;
      if (input.numDistinctValues(i) > 1)
	m_Kept.add(i);
    }

    //build a model using the PublicPrincipalComponents
    pca = new PublicPrincipalComponents();
    pca.setMaximumAttributes(m_MaxAttributes);
    pca.setVarianceCovered(m_CoverVariance);
    try {
      pca.setInputFormat(input);
    }
    catch(Exception e) {
      result = handleException("Failed to set input format", e);
    }

    if (result == null) {
      try {
	weka.filters.Filter.useFilter(input, pca);
      }
      catch (Exception e) {
	result = handleException("Failed to apply filter", e);
      }
    }

    if (result == null) {
      //get the coeffients from the filter
      coeff = pca.getCoefficients();
      sheet = createSpreadSheet(input, coeff);

      //output a spreadsheet with the coefficients
      m_OutputToken = new Token(sheet);
    }

    return result;
  }
}
