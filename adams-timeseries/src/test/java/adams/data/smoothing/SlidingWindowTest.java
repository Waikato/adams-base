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
 * SlidingWindowTest.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */
package adams.data.smoothing;

import junit.framework.Test;
import junit.framework.TestSuite;
import adams.data.smoothing.AbstractSlidingWindow.Measure;
import adams.env.Environment;

/**
 * Test class for the SlidingWindow smoother. Run from the command line with: <p/>
 * java adams.data.smoothing.SlidingWindowTest
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class SlidingWindowTest
  extends AbstractTimeseriesSmootherTestCase {

  /**
   * Constructs the test case. Called by subclasses.
   *
   * @param name 	the name of the test
   */
  public SlidingWindowTest(String name) {
    super(name);
  }

  /**
   * Returns the filenames (without path) of the input data files to use
   * in the regression test.
   *
   * @return		the filenames
   */
  @Override
  protected String[] getRegressionInputFiles() {
    return new String[]{
	"wine.sts",
	"wine.sts",
	"wine.sts",
	"wine.sts",
    };
  }

  /**
   * Returns the setups to use in the regression test.
   *
   * @return		the setups
   */
  @Override
  protected AbstractSmoother[] getRegressionSetups() {
    SlidingWindow[]	result;

    result = new SlidingWindow[4];

    result[0] = new SlidingWindow();

    result[1] = new SlidingWindow();
    result[1].setMeasure(Measure.MEAN);

    result[2] = new SlidingWindow();
    result[2].setWindowSize(40);

    result[3] = new SlidingWindow();
    result[3].setWindowSize(40);
    result[3].setMeasure(Measure.MEAN);

    return result;
  }

  /**
   * Returns the test suite.
   *
   * @return		the suite
   */
  public static Test suite() {
    return new TestSuite(SlidingWindowTest.class);
  }

  /**
   * Runs the test from commandline.
   *
   * @param args	ignored
   */
  public static void main(String[] args) {
    Environment.setEnvironmentClass(Environment.class);
    runTest(suite());
  }
}
