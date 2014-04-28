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
 * AbstractSymbolEvaluator.java
 * Copyright (C) 2010-2013 University of Waikato, Hamilton, New Zealand
 */
package adams.parser;

import java.util.HashMap;

import adams.core.base.BaseString;

/**
 * Abstract ancestor for classes that evaluate expressions with a parser
 * generated by JFlex/CUP. Also supports supplied symbols.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 * @param <T> the return type of the parser
 */
public abstract class AbstractSymbolEvaluator<T>
  extends AbstractExpressionEvaluator<T> {

  /** for serialization. */
  private static final long serialVersionUID = -5532014159307231647L;

  /** the symbols (key-value pairs). */
  protected BaseString[] m_Symbols;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	    "symbol", "symbols",
	    new BaseString[0]);
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Symbols = new BaseString[0];
  }

  /**
   * Sets the symbols to use for evaluation.
   *
   * @param value	the symbols
   */
  public void setSymbols(BaseString[] value) {
    m_Symbols = value;
  }

  /**
   * Returns the symbols to use for evaluation.
   *
   * @return		the symbols
   */
  public BaseString[] getSymbols() {
    return m_Symbols;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String symbolsTipText() {
    return "The symbols to initialize the parser with, key-value pairs: name=value.";
  }

  /**
   * Initializes the symbol.
   *
   * @param name	the name of the symbol
   * @param value	the string representation of the symbol
   * @return		the object representation of the symbol
   */
  protected abstract Object initializeSymbol(String name, String value);

  /**
   * Initializes the symbols.
   *
   * @return		the generated symbols
   */
  protected HashMap initializeSymbols() {
    HashMap	result;
    int		i;
    int		pos;
    String	pair;
    String	name;
    String	value;
    Object	obj;

    result = new HashMap();

    for (i = 0; i < m_Symbols.length; i++) {
      pair = m_Symbols[i].stringValue();
      pos  = pair.indexOf('=');
      if (pos > -1) {
	name  = pair.substring(0, pos);
	value = pair.substring(pos + 1);
	obj   = initializeSymbol(name, value);
	if (obj != null)
	  result.put(name, obj);
	else
	  getLogger().severe(
	      "Failed to initialize symbol '" + name + "' with value '" + value + "'!");
      }
      else {
	getLogger().severe(
	    "Symbol #" + (i+1) + " is not of format 'name=value': " + m_Symbols[i]);
      }
    }

    if (isLoggingEnabled())
      getLogger().info("Generated symbols: " + result);

    return result;
  }

  /**
   * Performs the actual evaluation.
   *
   * @param symbols	the symbols to use
   * @return		the evaluation, or null in case of error
   * @throws Exception	if evaluation fails
   */
  protected abstract T doEvaluate(HashMap symbols) throws Exception;

  /**
   * Performs the evaluation.
   *
   * @return		the evaluation, or null in case of error
   * @throws Exception	if evaluation fails
   */
  @Override
  public T evaluate() throws Exception {
    return doEvaluate(initializeSymbols());
  }
}
