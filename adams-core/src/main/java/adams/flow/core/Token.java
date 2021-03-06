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
 * Token.java
 * Copyright (C) 2009-2020 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.core;


import adams.core.CloneHandler;
import adams.core.ObjectCopyHelper;
import adams.core.Utils;
import adams.data.textrenderer.AbstractTextRenderer;
import nz.ac.waikato.cms.locator.ClassLocator;

import java.io.Serializable;

/**
 * A wrapper object for passing data through the flow.
 * <br><br>
 * If enabled, provenance information can be stored as well.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class Token
  implements Serializable, CloneHandler<Token> {

  /** for serialization. */
  private static final long serialVersionUID = -619575965753741493L;

  /** the payload. */
  protected Object m_Payload;

  /**
   * Initializes the token.
   */
  public Token() {
    this(null);
  }

  /**
   * Initializes the token.
   *
   * @param payload	the payload for this token
   */
  public Token(Object payload) {
    super();

    setPayload(payload);
  }

  /**
   * Sets the payload.
   *
   * @param value	the new payload
   */
  public void setPayload(Object value) {
    m_Payload = value;
  }

  /**
   * Checks whether the payload is not null and an array.
   *
   * @return		true if an array
   */
  public boolean isArray() {
    return !isNull() && m_Payload.getClass().isArray();
  }

  /**
   * Checks whether the payload is of the specified type (or a subclass;
   * or implements this interface).
   *
   * @param cls		the super class to check
   * @return		true if a match
   * @see		ClassLocator#matches(Class, Class)
   */
  public boolean hasPayload(Class cls) {
    return !isNull() && ClassLocator.matches(cls, m_Payload.getClass());
  }

  /**
   * Returns the payload.
   *
   * @return		the payload
   */
  public Object getPayload() {
    return m_Payload;
  }

  /**
   * Returns the payload properly casted.
   *
   * @return		the payload
   */
  public <T> T getPayload(Class<T> cls) {
    return (T) m_Payload;
  }

  /**
   * Checks whether the payload is null.
   *
   * @return		true if payload is null
   */
  public boolean isNull() {
    return (m_Payload == null);
  }

  /**
   * Returns a clone of itself.
   *
   * @return		the clone
   * @see		ObjectCopyHelper#copyObject(Object)
   */
  public Token getClone() {
    Token	result;

    result = new Token();

    if (!isNull())
      result.setPayload(ObjectCopyHelper.copyObject(m_Payload));

    return result;
  }

  /**
   * Returns the hashcode for the object.
   *
   * @return		the hash code
   */
  @Override
  public int hashCode() {
    if (isNull())
      return "".hashCode();
    else
      return m_Payload.hashCode();
  }

  /**
   * Returns an error string with the class type of the payload.
   *
   * @return		the error message
   */
  public String unhandledData() {
    return "Unhandled data: " + Utils.classToString(m_Payload);
  }

  /**
   * Returns a string representation of the payload.
   *
   * @return		the string representation
   */
  @Override
  public String toString() {
    StringBuilder result;

    result = new StringBuilder();
    result.append("Token #" + hashCode() + ":\n");
    result.append(AbstractTextRenderer.renderObject(m_Payload));

    return result.toString();
  }
}
