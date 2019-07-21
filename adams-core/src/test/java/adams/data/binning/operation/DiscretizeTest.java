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
 * DiscretizeTest.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package adams.data.binning.operation;

import adams.data.binning.Bin;
import adams.data.binning.Binnable;
import adams.data.binning.algorithm.ManualBinning;
import adams.data.binning.operation.Copy.CopyType;
import adams.env.Environment;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests Discretize.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DiscretizeTest
  extends AbstractOperationTestCase<Integer> {

  /**
   * Constructs the test case. Called by subclasses.
   *
   * @param name the name of the test
   */
  public DiscretizeTest(String name) {
    super(name);
  }

  /**
   * Generates the output data and returns the filenames.
   *
   * @return		the filenames of the generated output, no path
   */
  @Override
  protected List<String> generateOutput() {
    List<String> 		result;
    String			fname;
    List<Binnable<Integer>> 	data;
    List<Binnable<Integer>> 	sorted;
    List<Bin<Integer>> 		bins;
    ManualBinning		manual;

    result = new ArrayList<>();

    fname = createOutputFilename(0);
    data = generateDoubles(20);
    saveData(data, fname);
    result.add(fname);

    fname = createOutputFilename(1);
    sorted = Copy.copyData(data, CopyType.LIST);
    Sort.sort(sorted);
    saveData(sorted, fname);
    result.add(fname);

    fname = createOutputFilename(2);
    manual = new ManualBinning();
    manual.setNumBins(5);
    bins = generateBins(data, manual);
    saveBins(bins, fname);
    result.add(fname);

    fname = createOutputFilename(3);
    saveData(Discretize.discretize(bins), fname);
    result.add(fname);

    return result;
  }

  /**
   * Returns the test suite.
   *
   * @return		the suite
   */
  public static Test suite() {
    return new TestSuite(DiscretizeTest.class);
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
