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
 * LocalScopeTrigger.java
 * Copyright (C) 2012-2014 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.control;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import adams.core.QuickInfoHelper;
import adams.core.Variables;
import adams.core.VariablesHandler;
import adams.core.base.BaseRegExp;
import adams.flow.core.FlowVariables;

/**
 <!-- globalinfo-start -->
 * Executes the sub-actors whenever a token gets passed through, just like the adams.flow.control.Trigger actor, but also provides its own scope for variables and internal storage.<br/>
 * It is possible to 'propagate' or 'leak' variables and storage items from within the local scope back to the output scope. However, storage items from caches cannot be propagated.
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br/>
 * - accepts:<br/>
 * &nbsp;&nbsp;&nbsp;adams.flow.core.Unknown<br/>
 * - generates:<br/>
 * &nbsp;&nbsp;&nbsp;adams.flow.core.Unknown<br/>
 * <p/>
 * Conditional equivalent:<br/>
 * &nbsp;&nbsp;&nbsp;adams.flow.control.ConditionalTrigger
 * <p/>
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
 * &nbsp;&nbsp;&nbsp;default: LocalScopeTrigger
 * </pre>
 * 
 * <pre>-annotation &lt;adams.core.base.BaseAnnotation&gt; (property: annotations)
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
 * <pre>-finish-before-stopping &lt;boolean&gt; (property: finishBeforeStopping)
 * &nbsp;&nbsp;&nbsp;If enabled, actor first finishes processing all data before stopping.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-asynchronous &lt;boolean&gt; (property: asynchronous)
 * &nbsp;&nbsp;&nbsp;If enabled, the sub-actors get executed asynchronously rather than the flow 
 * &nbsp;&nbsp;&nbsp;waiting for them to finish before proceeding with execution.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-tee &lt;adams.flow.core.AbstractActor&gt; [-tee ...] (property: actors)
 * &nbsp;&nbsp;&nbsp;The actors to siphon-off the tokens to.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-copy-variables &lt;boolean&gt; (property: copyVariables)
 * &nbsp;&nbsp;&nbsp;If enabled, at execution time a copy of the current variables is made and 
 * &nbsp;&nbsp;&nbsp;used in the local scope.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-copy-storage &lt;boolean&gt; (property: copyStorage)
 * &nbsp;&nbsp;&nbsp;If enabled, a deep copy of the current storage state is made and made available 
 * &nbsp;&nbsp;&nbsp;in the local scope.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-propagate-variables &lt;boolean&gt; (property: propagateVariables)
 * &nbsp;&nbsp;&nbsp;If enabled, variables that match the specified regular expression get propagated 
 * &nbsp;&nbsp;&nbsp;to the outer scope.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-variables-regexp &lt;adams.core.base.BaseRegExp&gt; (property: variablesRegExp)
 * &nbsp;&nbsp;&nbsp;The regular expression that variable names must match in order to get propagated.
 * &nbsp;&nbsp;&nbsp;default: .*
 * </pre>
 * 
 * <pre>-propagate-storage &lt;boolean&gt; (property: propagateStorage)
 * &nbsp;&nbsp;&nbsp;If enabled, storage items which names match the specified regular expression 
 * &nbsp;&nbsp;&nbsp;get propagated to the outer scope.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-storage-regexp &lt;adams.core.base.BaseRegExp&gt; (property: storageRegExp)
 * &nbsp;&nbsp;&nbsp;The regular expression that the names of storage items must match in order 
 * &nbsp;&nbsp;&nbsp;to get propagated.
 * &nbsp;&nbsp;&nbsp;default: .*
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class LocalScopeTrigger
  extends Trigger 
  implements VariablesHandler, StorageHandler, ScopeHandler {

  /** for serialization. */
  private static final long serialVersionUID = -8344934611549310497L;

  /** the storage for temporary data. */
  protected transient Storage m_LocalStorage;

  /** the variables manager. */
  protected FlowVariables m_LocalVariables;
  
  /** the callable names. */
  protected HashSet<String> m_CallableNames;
  
  /** whether the callable name check is enforced. */
  protected boolean m_EnforceCallableNameCheck;

  /** whether to initialize the local variables with the current ones. */
  protected boolean m_CopyVariables;

  /** whether to initialize the local storage with the current one. */
  protected boolean m_CopyStorage;

  /** whether to propagate variables from the local scope to the outer scope. */
  protected boolean m_PropagateVariables;

  /** the regular expression of the variables to propagate. */
  protected BaseRegExp m_VariablesRegExp;

  /** whether to propagate variables from the local scope to the outer scope. */
  protected boolean m_PropagateStorage;

  /** the regular expression of the variables to propagate. */
  protected BaseRegExp m_StorageRegExp;
  
  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
        "Executes the sub-actors whenever a token gets passed through, just " 
        + "like the " + Trigger.class.getName() + " actor, but also provides "
        + "its own scope for variables and internal storage.\n"
        + "It is possible to 'propagate' or 'leak' variables and storage items "
        + "from within the local scope back to the output scope. However, "
        + "storage items from caches cannot be propagated.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	    "copy-variables", "copyVariables",
	    false);

    m_OptionManager.add(
	    "copy-storage", "copyStorage",
	    false);

    m_OptionManager.add(
	    "propagate-variables", "propagateVariables",
	    false);

    m_OptionManager.add(
	    "variables-regexp", "variablesRegExp",
	    new BaseRegExp(BaseRegExp.MATCH_ALL));

    m_OptionManager.add(
	    "propagate-storage", "propagateStorage",
	    false);

    m_OptionManager.add(
	    "storage-regexp", "storageRegExp",
	    new BaseRegExp(BaseRegExp.MATCH_ALL));
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String		result;
    List<String>	options;

    result = null;

    options = new ArrayList<String>();
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "copy vars", m_CopyVariables));
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "copy storage", m_CopyStorage));
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "propagate vars", m_PropagateVariables));
    QuickInfoHelper.add(options, QuickInfoHelper.toString(this, "propagate storage", m_PropagateStorage));
    result = QuickInfoHelper.flatten(options);
    
    if (QuickInfoHelper.hasVariable(this, "propagateVariables") || m_PropagateVariables) {
      if (!result.isEmpty())
	result += ", ";
      result = QuickInfoHelper.toString(this, "variablesRegExp", m_VariablesRegExp, "var: ");
    }
    if (QuickInfoHelper.hasVariable(this, "propagateStorage") || m_PropagateStorage) {
      if (!result.isEmpty())
	result += ", ";
      result += QuickInfoHelper.toString(this, "storageRegExp", m_StorageRegExp, "storage: ");
    }

    return result;
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_CallableNames            = new HashSet<String>();
    m_EnforceCallableNameCheck = true;
  }

  /**
   * Resets the actor.
   */
  @Override
  protected void reset() {
    super.reset();
    m_CallableNames.clear();
  }

  /**
   * Sets whether to copy variables into the local scope.
   * 
   * @param value	if true then variables get copied
   */
  public void setCopyVariables(boolean value) {
    m_CopyVariables = value;
    reset();
  }
  
  /**
   * Returns whether to copy variables into the local scope.
   * 
   * @return		true if variables get copied
   */
  public boolean getCopyVariables() {
    return m_CopyVariables;
  }
  
  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String copyVariablesTipText() {
    return "If enabled, at execution time a copy of the current variables is made and used in the local scope.";
  }

  /**
   * Sets whether to use copy of storage in local scope.
   * 
   * @param value	if true then storage gets copied
   */
  public void setCopyStorage(boolean value) {
    m_CopyStorage = value;
    reset();
  }
  
  /**
   * Returns whether to use copy of storage in local scope.
   * 
   * @return		true if storage gets copied
   */
  public boolean getCopyStorage() {
    return m_CopyStorage;
  }
  
  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String copyStorageTipText() {
    return "If enabled, a deep copy of the current storage state is made and made available in the local scope.";
  }

  /**
   * Sets whether to propagate variables from the local to the outer scope.
   * 
   * @param value	if true then variables get propagated
   */
  public void setPropagateVariables(boolean value) {
    m_PropagateVariables = value;
    reset();
  }
  
  /**
   * Returns whether to propagate variables from the local to the outer scope.
   * 
   * @return		true if variables get propagated
   */
  public boolean getPropagateVariables() {
    return m_PropagateVariables;
  }
  
  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String propagateVariablesTipText() {
    return "If enabled, variables that match the specified regular expression get propagated to the outer scope.";
  }

  /**
   * Sets the regular expression that variable names must match to get
   * propagated.
   * 
   * @param value	the expression
   */
  public void setVariablesRegExp(BaseRegExp value) {
    m_VariablesRegExp = value;
    reset();
  }
  
  /**
   * Returns the regular expression that variable names must match to get
   * propagated.
   * 
   * @return		the expression
   */
  public BaseRegExp getVariablesRegExp() {
    return m_VariablesRegExp;
  }
  
  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String variablesRegExpTipText() {
    return "The regular expression that variable names must match in order to get propagated.";
  }

  /**
   * Sets whether to propagate storage items from the local to the outer scope.
   * 
   * @param value	if true then storage items get propagated
   */
  public void setPropagateStorage(boolean value) {
    m_PropagateStorage = value;
    reset();
  }
  
  /**
   * Returns whether to propagate storage items from the local to the outer scope.
   * 
   * @return		true if storage items get propagated
   */
  public boolean getPropagateStorage() {
    return m_PropagateStorage;
  }
  
  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String propagateStorageTipText() {
    return "If enabled, storage items which names match the specified regular expression get propagated to the outer scope.";
  }

  /**
   * Sets the regular expression that storage item names must match to get
   * propagated.
   * 
   * @param value	the expression
   */
  public void setStorageRegExp(BaseRegExp value) {
    m_StorageRegExp = value;
    reset();
  }
  
  /**
   * Returns the regular expression that storage item names must match to get
   * propagated.
   * 
   * @return		the expression
   */
  public BaseRegExp getStorageRegExp() {
    return m_StorageRegExp;
  }
  
  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String storageRegExpTipText() {
    return "The regular expression that the names of storage items must match in order to get propagated.";
  }

  /**
   * Sets whether to enforce the callable name check.
   * 
   * @param value	true if to enforce check
   */
  public void setEnforceCallableNameCheck(boolean value) {
    m_EnforceCallableNameCheck = value;
  }
  
  /**
   * Returns whether the check of callable names is enforced.
   * 
   * @return		true if check enforced
   */
  public boolean getEnforceCallableNameCheck() {
    return m_EnforceCallableNameCheck;
  }

  /**
   * Checks whether a callable name is already in use.
   * 
   * @param name	the name to check
   * @see		#getEnforceCallableNameCheck()
   */
  public boolean isCallableNameUsed(String name) {
    if (!getEnforceCallableNameCheck())
      return false;
    else
      return m_CallableNames.contains(name);
  }

  /**
   * Adds the global name to the list of used ones.
   * 
   * @param name	the name to add
   * @return		null if successfully added, otherwise error message
   * @see		#getEnforceCallableNameCheck()
   */
  public String addCallableName(String name) {
    if (!getEnforceCallableNameCheck())
      return null;
    
    if (isCallableNameUsed(name))
      return "Callable name '" + name + "' is already used in this scope ('" + getFullName() + "')!";
    
    m_CallableNames.add(name);
    return null;
  }
  
  /**
   * Returns the storage container.
   *
   * @return		the container
   */
  public synchronized Storage getStorage() {
    if (m_LocalStorage == null) {
      if (m_CopyStorage)
	m_LocalStorage = getParent().getStorageHandler().getStorage().getClone();
      else
	m_LocalStorage = new Storage();
    }

    return m_LocalStorage;
  }

  /**
   * Returns the Variables instance to use.
   *
   * @return		the local variables
   */
  public synchronized Variables getLocalVariables() {
    if (m_LocalVariables == null) {
      m_LocalVariables = new FlowVariables();
      if (m_CopyVariables)
	m_LocalVariables.assign(getParent().getVariables());
      m_LocalVariables.setFlow(this);
    }
    
    return m_LocalVariables;
  }

  /**
   * Returns the Variables instance to use.
   *
   * @return		the scope handler
   */
  @Override
  public synchronized Variables getVariables() {
    return getLocalVariables();
  }
  
  /**
   * Updates the Variables instance in use.
   *
   * @param value	ignored
   */
  @Override
  protected void forceVariables(Variables value) {
    super.forceVariables(getLocalVariables());
  }

  /**
   * Post-execute hook.
   *
   * @return		null if everything is fine, otherwise error message
   * @see		#m_Executed
   */
  @Override
  protected String postExecute() {
    String	result;
    
    result = super.postExecute();

    if (!m_Stopped) {
      if (m_PropagateVariables) {
	for (String name: m_LocalVariables.nameSet()) {
	  if (m_VariablesRegExp.isMatch(name)) {
	    getParent().getVariables().set(name, m_LocalVariables.get(name));
	    if (isLoggingEnabled())
	      getLogger().fine("Propagating variable '" + name + "': " + m_LocalVariables.get(name));
	  }
	}
      }
      
      if (m_PropagateStorage) {
	for (StorageName name: m_LocalStorage.keySet()) {
	  if (m_StorageRegExp.isMatch(name.getValue())) {
	    getParent().getStorageHandler().getStorage().put(name, m_LocalStorage.get(name));
	    if (isLoggingEnabled())
	      getLogger().fine("Propagating storage '" + name + "': " + m_LocalStorage.get(name));
	  }
	}
      }
    }
    
    return result;
  }
  
  /**
   * Cleans up after the execution has finished. Also removes graphical
   * components.
   */
  @Override
  public void cleanUp() {
    if (m_LocalVariables != null) {
      m_LocalVariables.cleanUp();
      m_LocalVariables = null;
    }

    m_CallableNames.clear();

    super.cleanUp();
  }
}
