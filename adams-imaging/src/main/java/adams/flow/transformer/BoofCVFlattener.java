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
 * BoofCVFlattener.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import weka.core.Instance;
import adams.core.QuickInfoHelper;
import adams.data.boofcv.BoofCVImageContainer;
import adams.data.boofcv.flattener.AbstractBoofCVFlattener;
import adams.data.boofcv.flattener.Pixels;
import adams.flow.core.Token;
import adams.flow.provenance.ActorType;
import adams.flow.provenance.Provenance;
import adams.flow.provenance.ProvenanceContainer;
import adams.flow.provenance.ProvenanceInformation;
import adams.flow.provenance.ProvenanceSupporter;

/**
 <!-- globalinfo-start -->
 * Applies a BoofCV flattener to the incoming image and outputs the generated WEKA Instance(s).
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br/>
 * - accepts:<br/>
 * &nbsp;&nbsp;&nbsp;adams.data.boofcv.BoofCVImageContainer<br/>
 * - generates:<br/>
 * &nbsp;&nbsp;&nbsp;weka.core.Instance<br/>
 * <p/>
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * Valid options are: <p/>
 * 
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: BoofCVFlattener
 * </pre>
 * 
 * <pre>-annotation &lt;adams.core.base.BaseText&gt; (property: annotations)
 * &nbsp;&nbsp;&nbsp;The annotations to attach to this actor.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-skip (property: skip)
 * &nbsp;&nbsp;&nbsp;If set to true, transformation is skipped and the input token is just forwarded 
 * &nbsp;&nbsp;&nbsp;as it is.
 * </pre>
 * 
 * <pre>-stop-flow-on-error (property: stopFlowOnError)
 * &nbsp;&nbsp;&nbsp;If set to true, the flow gets stopped in case this actor encounters an error;
 * &nbsp;&nbsp;&nbsp; useful for critical actors.
 * </pre>
 * 
 * <pre>-flattener &lt;adams.data.boofcv.flattener.AbstractBoofCVFlattener&gt; (property: flattenAlgorithm)
 * &nbsp;&nbsp;&nbsp;The flattener to use for flattening the image.
 * &nbsp;&nbsp;&nbsp;default: adams.data.boofcv.flattener.Pixels
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class BoofCVFlattener
  extends AbstractTransformer
  implements ProvenanceSupporter {

  /** for serialization. */
  private static final long serialVersionUID = -1998955116780561587L;

  /** the key for storing the current instances in the backup. */
  public final static String BACKUP_QUEUE = "queue";

  /** the transformer to apply to the image. */
  protected AbstractBoofCVFlattener m_FlattenAlgorithm;

  /** the generated Instance objects. */
  protected ArrayList<Instance> m_Queue;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
        "Applies a BoofCV flattener to the incoming image and outputs "
      + "the generated WEKA Instance(s).";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	    "flattener", "flattenAlgorithm",
	    new Pixels());
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();
    
    m_Queue = new ArrayList<Instance>();
  }
  
  /**
   * Resets the scheme.
   */
  @Override
  protected void reset() {
    super.reset();
    
    m_Queue.clear();
  }

  /**
   * Sets the flattener to use.
   *
   * @param value	the flattener
   */
  public void setFlattenAlgorithm(AbstractBoofCVFlattener value) {
    m_FlattenAlgorithm = value;
    reset();
  }

  /**
   * Returns the flattener in use.
   *
   * @return		the flattener
   */
  public AbstractBoofCVFlattener getFlattenAlgorithm() {
    return m_FlattenAlgorithm;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String flattenAlgorithmTipText() {
    return "The flattener to use for flattening the image.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "flattenAlgorithm", m_FlattenAlgorithm);
  }

  /**
   * Removes entries from the backup.
   */
  @Override
  protected void pruneBackup() {
    super.pruneBackup();

    pruneBackup(BACKUP_QUEUE);
  }

  /**
   * Backs up the current state of the actor before update the variables.
   *
   * @return		the backup
   */
  @Override
  protected Hashtable<String,Object> backupState() {
    Hashtable<String,Object>	result;

    result = super.backupState();

    result.put(BACKUP_QUEUE, m_Queue);

    return result;
  }

  /**
   * Restores the state of the actor before the variables got updated.
   *
   * @param state	the backup of the state to restore from
   */
  @Override
  protected void restoreState(Hashtable<String,Object> state) {
    if (state.containsKey(BACKUP_QUEUE)) {
      m_Queue = (ArrayList<Instance>) state.get(BACKUP_QUEUE);
      state.remove(BACKUP_QUEUE);
    }

    super.restoreState(state);
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		the Class of objects that can be processed
   */
  public Class[] accepts() {
    return new Class[]{BoofCVImageContainer.class};
  }

  /**
   * Returns the class of objects that it generates.
   *
   * @return		<!-- flow-generates-start -->weka.core.Instance.class<!-- flow-generates-end -->
   */
  public Class[] generates() {
    return new Class[]{Instance.class};
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute() {
    String	result;

    result = null;

    m_Queue.clear();
    try {
      m_Queue.addAll(Arrays.asList(m_FlattenAlgorithm.flatten((BoofCVImageContainer) m_InputToken.getPayload())));
    }
    catch (Exception e) {
      result = handleException("Failed to flatten image: ", e);
    }

    return result;
  }

  /**
   * Updates the provenance information in the provided container.
   *
   * @param cont	the provenance container to update
   */
  public void updateProvenance(ProvenanceContainer cont) {
    if (Provenance.getSingleton().isEnabled()) {
      if (m_InputToken.hasProvenance())
	cont.setProvenance(m_InputToken.getProvenance().getClone());
      cont.addProvenance(new ProvenanceInformation(ActorType.PREPROCESSOR, m_InputToken.getPayload().getClass(), this, ((Token) cont).getPayload().getClass()));
    }
  }

  /**
   * Checks whether there is pending output to be collected after
   * executing the flow item.
   *
   * @return		true if there is pending output
   */
  @Override
  public boolean hasPendingOutput() {
    return (m_Queue.size() > 0);
  }

  /**
   * Returns the generated token.
   *
   * @return		the generated token
   */
  @Override
  public Token output() {
    Token	result;

    result = new Token(m_Queue.get(0));
    m_Queue.remove(0);

    updateProvenance(result);

    return result;
  }

  /**
   * Cleans up after the execution has finished.
   */
  @Override
  public void wrapUp() {
    m_Queue.clear();

    super.wrapUp();
  }
}
