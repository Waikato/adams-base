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
 * OptionalStopRestrictor.java
 * Copyright (C) 2021 University of Waikato, Hamilton, NZ
 */

package adams.flow.core;

/**
 * Interface for stop restrictors that can turn on/off stop restrictions.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface OptionalStopRestrictor
  extends StopRestrictor {

  /**
   * Sets whether to restrict stops or not.
   *
   * @param value	true if to restrict
   */
  public void setRestrictingStops(boolean value);
}
