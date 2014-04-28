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
 * TimeseriesToWekaInstancesTest.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */

package adams.data.conversion;

import adams.core.Constants;
import adams.core.DateUtils;
import adams.data.DateFormatString;
import adams.data.timeseries.Timeseries;
import adams.data.timeseries.TimeseriesPoint;

/**
 * Tests the TimeseriesToWekaInstances conversion.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class TimeseriesToWekaInstancesTest
  extends AbstractConversionTestCase {

  /**
   * Constructs the test case. Called by subclasses.
   *
   * @param name 	the name of the test
   */
  public TimeseriesToWekaInstancesTest(String name) {
    super(name);
  }

  /**
   * Returns the input data to use in the regression test.
   *
   * @return		the objects
   */
  @Override
  protected Object[] getRegressionInput() {
    Timeseries[]	result;
    
    result    = new Timeseries[1];
    result[0] = new Timeseries();
    result[0].setID("simple test");
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2000-01-01", Constants.DATE_FORMAT), 0.1));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2000-01-02", Constants.DATE_FORMAT), 0.2));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2000-01-03", Constants.DATE_FORMAT), 0.3));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2000-01-04", Constants.DATE_FORMAT), 0.4));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2001-01-01", Constants.DATE_FORMAT), 1.1));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2001-01-02", Constants.DATE_FORMAT), 1.2));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2001-01-03", Constants.DATE_FORMAT), 1.3));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2001-01-04", Constants.DATE_FORMAT), 1.4));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2002-01-01", Constants.DATE_FORMAT), 2.1));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2002-01-02", Constants.DATE_FORMAT), 2.2));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2002-01-03", Constants.DATE_FORMAT), 2.3));
    result[0].add(new TimeseriesPoint(DateUtils.parseString("2002-01-04", Constants.DATE_FORMAT), 2.4));
    
    return result;
  }

  /**
   * Returns the setups to use in the regression test.
   *
   * @return		the setups
   */
  @Override
  protected Conversion[] getRegressionSetups() {
    TimeseriesToWekaInstances[]	result;
    
    result = new TimeseriesToWekaInstances[2];
    result[0] = new TimeseriesToWekaInstances();
    result[1] = new TimeseriesToWekaInstances();
    result[1].setFormat(new DateFormatString("dd MMM yy hh:mm a"));
    
    return result;
  }

  /**
   * Returns the ignored line indices to use in the regression test.
   *
   * @return		the setups
   */
  @Override
  protected int[] getRegressionIgnoredLineIndices() {
    return new int[0];
  }
}
