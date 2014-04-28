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
 * SpreadSheetMaterializeFormulasTest.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */

package adams.data.conversion;

import adams.data.io.input.CsvSpreadSheetReader;
import adams.data.spreadsheet.SpreadSheet;
import adams.test.TmpFile;

/**
 * Tests the SpreadSheetMaterializeFormulas conversion.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class SpreadSheetMaterializeFormulasTest
  extends AbstractConversionTestCase {

  /**
   * Constructs the test case. Called by subclasses.
   *
   * @param name 	the name of the test
   */
  public SpreadSheetMaterializeFormulasTest(String name) {
    super(name);
  }

  /**
   * Called by JUnit before each test method.
   *
   * @throws Exception if an error occurs.
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();

    m_TestHelper.copyResourceToTmp("formula.csv");
  }

  /**
   * Called by JUnit after each test method.
   *
   * @throws Exception	if tear-down fails
   */
  @Override
  protected void tearDown() throws Exception {
    m_TestHelper.deleteFileFromTmp("formula.csv");

    super.tearDown();
  }

  /**
   * Returns the input data to use in the regression test.
   *
   * @return		the objects
   */
  @Override
  protected Object[] getRegressionInput() {
    SpreadSheet[]		result;
    TmpFile			file;
    CsvSpreadSheetReader	reader;

    file = new TmpFile("formula.csv");
    try {
      reader    = new CsvSpreadSheetReader();
      result    = new SpreadSheet[1];
      result[0] = reader.read(file);
    }
    catch (Exception e) {
      result = new SpreadSheet[0];
      fail("Failed to load data from '" + file + "': " + e);
    }

    return result;
  }

  /**
   * Returns the setups to use in the regression test.
   *
   * @return		the setups
   */
  @Override
  protected Conversion[] getRegressionSetups() {
    SpreadSheetMaterializeFormulas[]	result;

    result    = new SpreadSheetMaterializeFormulas[1];
    result[0] = new SpreadSheetMaterializeFormulas();

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
