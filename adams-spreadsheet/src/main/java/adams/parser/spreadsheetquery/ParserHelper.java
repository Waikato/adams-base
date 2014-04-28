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
 * ParserHelper.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */
package adams.parser.spreadsheetquery;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import adams.core.Range;
import adams.core.Utils;
import adams.core.base.BaseBoolean;
import adams.data.conversion.Conversion;
import adams.data.conversion.MultiConversion;
import adams.data.conversion.RenameSpreadSheetColumn;
import adams.data.spreadsheet.SpreadSheet;
import adams.data.spreadsheet.SpreadSheetColumnIndex;
import adams.data.spreadsheet.SpreadSheetColumnRange;
import adams.data.spreadsheet.rowfinder.ByIndex;
import adams.data.spreadsheet.rowfinder.RowFinder;
import adams.flow.control.SubProcess;
import adams.flow.core.Token;
import adams.flow.transformer.Convert;
import adams.flow.transformer.SpreadSheetReorderColumns;
import adams.flow.transformer.SpreadSheetRowFilter;
import adams.flow.transformer.SpreadSheetSetCell;
import adams.flow.transformer.SpreadSheetSort;
import adams.flow.transformer.SpreadSheetSubset;

/**
 * Helper class for spreadsheet formulas.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class ParserHelper
  extends adams.parser.ParserHelper {

  /** for serialization. */
  private static final long serialVersionUID = 8273216839178554659L;

  /** the underlying spreadsheet. */
  protected SpreadSheet m_Sheet;

  /** whether all columns are retrieved. */
  protected boolean m_AllColumns;

  /** whether to create subset. */
  protected boolean m_Select;

  /** whether to delete rows. */
  protected boolean m_Delete;

  /** whether to update cells. */
  protected boolean m_Update;

  /** the columns to retrieve. */
  protected List<String> m_Columns;

  /** the columns to rename. */
  protected HashMap<String,String> m_RenameColumns;

  /** the columns to sort on. */
  protected List<String> m_SortColumns;

  /** the columns to update (column - new value). */
  protected HashMap<String,Object> m_UpdateColumns;

  /** the columns to sort on. */
  protected List<Boolean> m_SortAsc;

  /** the row finders to use. */
  protected List<RowFinder> m_RowFinders;
  
  /** the partial flow for converting the spreadsheet. */
  protected SubProcess m_SubProcess;
  
  /** the rows to select. */
  protected int[] m_Rows;
  
  /** the limit. */
  protected int m_LimitMax;
  
  /** the offset for the limit. */
  protected int m_LimitOffset;
  
  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();
    
    m_Sheet         = null;
    m_AllColumns    = false;
    m_Select        = false;
    m_Delete        = false;
    m_Update        = false;
    m_Columns       = new ArrayList<String>();
    m_RenameColumns = new HashMap<String,String>();
    m_UpdateColumns = new HashMap<String,Object>();
    m_SortColumns   = new ArrayList<String>();
    m_SortAsc       = new ArrayList<Boolean>();
    m_RowFinders    = new ArrayList<RowFinder>();
    m_SubProcess    = null;
    m_Rows          = null;
    m_LimitOffset   = 0;
    m_LimitMax      = -1;
  }

  /**
   * Sets the spreadsheet to use.
   *
   * @param value 	the spreadsheet
   */
  public void setSheet(SpreadSheet value) {
    m_Sheet = value;
  }

  /**
   * Returns the current spreadsheet in use.
   *
   * @return 		the spreadsheet
   */
  public SpreadSheet getSheet() {
    return m_Sheet;
  }

  /**
   * Sets to return all columns.
   */
  public void useAllColumns() {
    m_AllColumns = true;
    if (isLoggingEnabled())
      getLogger().fine("all columns");
  }

  /**
   * Adds the name of a column to return.
   *
   * @param col the column name
   */
  public void addColumn(String col) {
    m_Columns.add(SpreadSheetColumnRange.escapeColumnName(col));
    if (isLoggingEnabled())
      getLogger().fine("column: " + col);
  }

  /**
   * Adds the name of a column to rename.
   *
   * @param col the current column name
   * @param newCol the new column name
   */
  public void renameColumn(String col, String newCol) {
    m_RenameColumns.put(col, newCol);
    
    if (isLoggingEnabled())
      getLogger().fine("rename: " + col + " -> " + newCol);
  }

  /**
   * Adds the name of a sort column.
   *
   * @param col the column name
   * @param asc whether to sort ascending
   */
  public void addSortColumn(String col, boolean asc) {
    m_SortColumns.add(col);
    m_SortAsc.add(asc);
    
    if (isLoggingEnabled())
      getLogger().fine("sort: " + col + ", asc: " + asc);
  }

  /**
   * Adds the name of a column to update with a new value.
   *
   * @param col the column name
   * @param value the new value
   */
  public void addUpdateColumn(String col, Object value) {
    m_UpdateColumns.put(col, value);
    
    if (isLoggingEnabled())
      getLogger().fine("update: " + col + " = " + value);
  }

  /**
   * Applies the row finder.
   *
   * @param finder the row finder to apply
   * @param log a logging message
   * @return the selected rows
   */
  public int[] applyRowFinder(RowFinder finder, String log) {
    int[]	result;
    
    result = finder.findRows(m_Sheet);
    if (isLoggingEnabled())
      getLogger().fine(log + ": " + Utils.arrayToString(result));
    
    return result;
  }

  /**
   * Sets to create subset.
   */
  public void select() {
    m_Select = true;
    if (isLoggingEnabled())
      getLogger().fine("select");
  }

  /**
   * Sets to delete rows.
   */
  public void delete() {
    m_Delete = true;
    if (isLoggingEnabled())
      getLogger().fine("delete");
  }

  /**
   * Sets to update cells.
   */
  public void update() {
    m_Update = true;
    if (isLoggingEnabled())
      getLogger().fine("update");
  }
  
  /**
   * Combines the row finders with logical AND.
   * 
   * @param c1 the first set of rows
   * @param c2 the second set of rows
   * @return the combined rows
   */
  public int[] combineWithAnd(int[] c1, int[] c2) {
    int[]	result;
    TIntHashSet	set;

    set = new TIntHashSet(c1);
    set.retainAll(c2);
    result = set.toArray();
    Arrays.sort(result);
    
    if (isLoggingEnabled())
      getLogger().fine("and: " + Utils.arrayToString(result));
    
    return result;
  }

  /**
   * Combines the row finders with logical OR.
   * 
   * @param c1 the first set of rows
   * @param c2 the second set of rows
   * @return the combined rows
   */
  public int[] combineWithOr(int[] c1, int[] c2) {
    int[]	result;
    TIntHashSet	set;

    set = new TIntHashSet(c1);
    set.addAll(c2);
    result = set.toArray();
    Arrays.sort(result);

    if (isLoggingEnabled())
      getLogger().fine("or: " + Utils.arrayToString(result));

    return result;
  }

  /**
   * Inverts the row finders.
   * 
   * @param c the rows to invert
   * @return the inverted rows
   */
  public int[] invert(int[] c) {
    int[]		result;
    TIntHashSet		set;
    TIntArrayList	list;
    int			i;
    
    set  = new TIntHashSet(c);
    list = new TIntArrayList(m_Sheet.getRowCount() - c.length);
    for (i = 0; i < m_Sheet.getRowCount(); i++) {
      if (!set.contains(i))
	list.add(i);
    }
    result = list.toArray();

    if (isLoggingEnabled())
      getLogger().fine("not: " + Utils.arrayToString(result));
    
    return result;
  }
  
  /**
   * Sets the limit.
   * 
   * @param offset	the offset (0 is offset for first row)
   * @param max		the maximum number of rows (>= 1)
   */
  public void setLimit(int offset, int max) {
    if (offset < 0)
      offset = 0;
    if (max < 1)
      max = 1;
    m_LimitOffset = offset;
    m_LimitMax    = max;
  }
  
  /**
   * Sets the rows to use.
   * 
   * @param value the rows
   */
  public void setRows(int[] value) {
    m_Rows = value;
  }
  
  /**
   * Returns the rows to use.
   * 
   * @return the rows
   */
  public int[] getRows() {
    return m_Rows;
  }

  /**
   * Returns the partial flow that was generated to process the spreadsheet.
   *
   * @return the partial flow, null if none available
   */
  public SubProcess getSubProcess() {
    return m_SubProcess;
  }

  /**
   * Returns the result of the evaluation.
   *
   * @return the result
   */
  public SpreadSheet getResult() {
    SpreadSheet result;
    String msg;
    Range rows;
  
    result       = null;
    m_SubProcess = null;
    
    // final rows selection
    SpreadSheetRowFilter rowFilter = new SpreadSheetRowFilter();
    ByIndex byIndex = new ByIndex();
    rows = new Range();
    if (m_Rows == null)
      rows.setRange(Range.ALL);
    else
      rows.setIndices(m_Rows);
    byIndex.setRows(rows);
    rowFilter.setFinder(byIndex);
    
    SubProcess sub = new SubProcess();
    if (m_Select) {
      // sorting?
      if (m_SortColumns.size() > 0) {
        SpreadSheetSort sort = new SpreadSheetSort();
        SpreadSheetColumnIndex[] cols = new SpreadSheetColumnIndex[m_SortColumns.size()];
        BaseBoolean[] order = new BaseBoolean[m_SortColumns.size()];
        for (int i = 0; i < m_SortColumns.size(); i++) {
          cols[i]  = new SpreadSheetColumnIndex(m_SortColumns.get(i));
          order[i] = new BaseBoolean("" + m_SortAsc.get(i));
        }
        sort.setSortColumn(cols);
        sort.setSortOrder(order);
        sub.add(sort);
      }
      // subset of columns?
      if (!m_AllColumns) {
        SpreadSheetReorderColumns reorder = new SpreadSheetReorderColumns();
        reorder.setOrder(Utils.flatten(m_Columns, ","));
        sub.add(reorder);
      }
      // rename columns?
      if (m_RenameColumns.size() > 0) {
	Convert conv = new Convert();
	MultiConversion multi = new MultiConversion();
	List<Conversion> list = new ArrayList<Conversion>();
	for (String col: m_RenameColumns.keySet()) {
	  RenameSpreadSheetColumn ren = new RenameSpreadSheetColumn();
	  ren.setColumn(new SpreadSheetColumnIndex(col));
	  ren.setNewName(m_RenameColumns.get(col));
          list.add(ren);
        }
        multi.setSubConversions(list.toArray(new Conversion[list.size()]));
        conv.setConversion(multi);
        sub.add(conv);
      }
      sub.add(rowFilter);
      // limit?
      if (m_LimitMax > 0) {
	Range limit = new Range((m_LimitOffset + 1) + Range.RANGE + (m_LimitOffset + m_LimitMax));
	SpreadSheetSubset subset = new SpreadSheetSubset();
	subset.setRows(limit);
	subset.setColumns(new SpreadSheetColumnRange(SpreadSheetColumnRange.ALL));
	sub.add(subset);
      }
    }
    else if (m_Update) {
      for (String col: m_UpdateColumns.keySet()) {
	SpreadSheetSetCell setcell = new SpreadSheetSetCell();
	setcell.setRow(rows);
	setcell.setColumn(new SpreadSheetColumnRange(col));
	setcell.setValue(m_UpdateColumns.get(col).toString());
	sub.add(setcell);
      }
    }
    else if (m_Delete) {
      rows.setInverted(true);
      byIndex.setRows(rows);
      sub.add(rowFilter);
    }
    
    // transform spreadsheet
    m_SubProcess = (SubProcess) sub.shallowCopy();
    msg = sub.setUp();
    if (msg == null) {
      sub.input(new Token(getSheet()));
      msg = sub.execute();
      if ((msg == null) && (sub.hasPendingOutput()))
        result = (SpreadSheet) sub.output().getPayload();
    }

    if (msg != null)
      throw new IllegalStateException(msg);

    return result;
  }
}
