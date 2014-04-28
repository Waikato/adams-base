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
 * Unique.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */

package adams.data.spreadsheet.statistic;

import java.util.HashMap;

import adams.data.spreadsheet.Cell;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;

/**
 <!-- globalinfo-start -->
 * Counts the unique occurrences of numeric&#47;string values.
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class Unique
  extends AbstractColumnStatistic {

  /** for serialization. */
  private static final long serialVersionUID = 4899075284716702404L;
  
  /** for counting the occurences of numeric values. */
  protected HashMap<Double,Integer> m_Numbers;
  
  /** for counting the occurences of labels. */
  protected HashMap<String,Integer> m_Labels;
  
  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Counts the unique occurrences of numeric/string values.";
  }

  /**
   * Performs initialization before the cells are being visited.
   * 
   * @param sheet	the spreadsheet to generate the stats for
   * @param colIndex	the column index
   */
  @Override
  protected void preVisit(SpreadSheet sheet, int colIndex) {
    m_Numbers = new HashMap<Double,Integer>();
    m_Labels  = new HashMap<String,Integer>();
  }

  /**
   * Gets called with every row in the spreadsheet for generating the stats.
   * 
   * @param row		the current row
   * @param colIndex	the column index
   */
  @Override
  protected void doVisit(Row row, int colIndex) {
    Cell	cell;
    double	number;
    String	label;
    
    if (row.hasCell(colIndex)) {
      cell = row.getCell(colIndex);
      if (cell.isNumeric()) {
	number = cell.toDouble();
	if (!m_Numbers.containsKey(number))
	  m_Numbers.put(number, 1);
	else
	  m_Numbers.put(number, m_Numbers.get(number) + 1);
      }
      else if (!cell.isMissing()) {
	label = cell.getContent();
	if (!m_Labels.containsKey(label))
	  m_Labels.put(label, 1);
	else
	  m_Labels.put(label, m_Labels.get(label) + 1);
      }
    }
  }

  /**
   * Finishes up the stats generation after all the cells have been visited.
   * 
   * @param sheet	the spreadsheet to generate the stats for
   * @param colIndex	the column index
   * @return		the generated stats
   */
  @Override
  protected SpreadSheet postVisit(SpreadSheet sheet, int colIndex) {
    SpreadSheet	result;
    Row		row;
    int		count;

    result = createOutputHeader();

    row = result.addRow();
    if (m_Numbers.size() > 0) {
      count = 0;
      for (Double d: m_Numbers.keySet()) {
	if (m_Numbers.get(d) == 1)
	  count++;
      }
      row.addCell(0).setContent("Unique numbers");
      row.addCell(1).setContent(count);
    }
    else if (m_Labels.size() > 0) {
      count = 0;
      for (String s: m_Labels.keySet()) {
	if (m_Labels.get(s) == 1)
	  count++;
      }
      row.addCell(0).setContent("Unique labels");
      row.addCell(1).setContent(count);
    }

    m_Numbers = null;
    m_Labels  = null;
    
    return result;
  }
}
