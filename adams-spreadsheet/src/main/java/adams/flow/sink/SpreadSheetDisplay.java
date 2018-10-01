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
 * SpreadSheetDisplay.java
 * Copyright (C) 2009-2018 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.sink;

import adams.core.DateUtils;
import adams.core.QuickInfoHelper;
import adams.core.io.ConsoleHelper;
import adams.core.option.OptionUtils;
import adams.data.io.output.NullWriter;
import adams.data.spreadsheet.DefaultSpreadSheet;
import adams.data.spreadsheet.SpreadSheet;
import adams.data.spreadsheet.SpreadSheetSupporter;
import adams.flow.core.Token;
import adams.gui.core.BasePanel;
import adams.gui.core.BaseScrollPane;
import adams.gui.core.ExtensionFileFilter;
import adams.gui.core.SearchPanel;
import adams.gui.core.SearchPanel.LayoutType;
import adams.gui.core.SpreadSheetColumnComboBox;
import adams.gui.core.SpreadSheetTable;
import adams.gui.core.SpreadSheetTableModel;
import adams.gui.core.TableRowRange;
import adams.gui.core.spreadsheettable.CellRenderingCustomizer;
import adams.gui.core.spreadsheettable.DefaultCellRenderingCustomizer;
import adams.gui.core.spreadsheettable.ProcessSelectedRows;
import adams.gui.core.spreadsheettable.SpreadSheetTablePopupMenuItemHelper;
import adams.gui.event.SearchEvent;
import adams.gui.event.SearchListener;
import adams.gui.sendto.SendToActionUtils;
import adams.gui.visualization.core.PopupMenuCustomizer;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 <!-- globalinfo-start -->
 * Actor for displaying a spreadsheet.<br>
 * Custom background for negative&#47;positive values can be specified as well.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;adams.data.spreadsheet.SpreadSheet<br>
 * <br><br>
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
 * &nbsp;&nbsp;&nbsp;default: SpreadSheetDisplay
 * </pre>
 *
 * <pre>-annotation &lt;adams.core.base.BaseText&gt; (property: annotations)
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
 * <pre>-short-title &lt;boolean&gt; (property: shortTitle)
 * &nbsp;&nbsp;&nbsp;If enabled uses just the name for the title instead of the actor's full 
 * &nbsp;&nbsp;&nbsp;name.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-width &lt;int&gt; (property: width)
 * &nbsp;&nbsp;&nbsp;The width of the dialog.
 * &nbsp;&nbsp;&nbsp;default: 640
 * &nbsp;&nbsp;&nbsp;minimum: -1
 * </pre>
 *
 * <pre>-height &lt;int&gt; (property: height)
 * &nbsp;&nbsp;&nbsp;The height of the dialog.
 * &nbsp;&nbsp;&nbsp;default: 480
 * &nbsp;&nbsp;&nbsp;minimum: -1
 * </pre>
 *
 * <pre>-x &lt;int&gt; (property: x)
 * &nbsp;&nbsp;&nbsp;The X position of the dialog (&gt;=0: absolute, -1: left, -2: center, -3: right
 * &nbsp;&nbsp;&nbsp;).
 * &nbsp;&nbsp;&nbsp;default: -1
 * &nbsp;&nbsp;&nbsp;minimum: -3
 * </pre>
 *
 * <pre>-y &lt;int&gt; (property: y)
 * &nbsp;&nbsp;&nbsp;The Y position of the dialog (&gt;=0: absolute, -1: top, -2: center, -3: bottom
 * &nbsp;&nbsp;&nbsp;).
 * &nbsp;&nbsp;&nbsp;default: -1
 * &nbsp;&nbsp;&nbsp;minimum: -3
 * </pre>
 *
 * <pre>-font &lt;java.awt.Font&gt; (property: font)
 * &nbsp;&nbsp;&nbsp;The font of the dialog.
 * &nbsp;&nbsp;&nbsp;default: Monospaced-PLAIN-12
 * </pre>
 *
 * <pre>-num-decimals &lt;int&gt; (property: numDecimals)
 * &nbsp;&nbsp;&nbsp;The number of decimals for numeric values.
 * &nbsp;&nbsp;&nbsp;default: 3
 * &nbsp;&nbsp;&nbsp;minimum: -1
 * </pre>
 *
 * <pre>-use-custom-negative-background &lt;boolean&gt; (property: useCustomNegativeBackground)
 * &nbsp;&nbsp;&nbsp;Whether to use a custom background color for negative values.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-negative-background &lt;java.awt.Color&gt; (property: negativeBackground)
 * &nbsp;&nbsp;&nbsp;The custom background for negative values (must be enabled).
 * &nbsp;&nbsp;&nbsp;default: #ffffff
 * </pre>
 *
 * <pre>-use-custom-positive-background &lt;boolean&gt; (property: useCustomPositiveBackground)
 * &nbsp;&nbsp;&nbsp;Whether to use a custom background color for positive values.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-positive-background &lt;java.awt.Color&gt; (property: positiveBackground)
 * &nbsp;&nbsp;&nbsp;The custom background for positive values (must be enabled).
 * &nbsp;&nbsp;&nbsp;default: #ffffff
 * </pre>
 *
 * <pre>-show-formulas &lt;boolean&gt; (property: showFormulas)
 * &nbsp;&nbsp;&nbsp;Whether to show the formulas or the calculated values.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-allow-search &lt;boolean&gt; (property: allowSearch)
 * &nbsp;&nbsp;&nbsp;Whether to allow the user to search the table.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-optimal-column-width &lt;boolean&gt; (property: optimalColumnWidth)
 * &nbsp;&nbsp;&nbsp;Whether to calculate the optimal column width whenever a token is displayed 
 * &nbsp;&nbsp;&nbsp;(= enabled) or only when flow finishes.
 * &nbsp;&nbsp;&nbsp;default: true
 * </pre>
 *
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class SpreadSheetDisplay
  extends AbstractTextualDisplay
  implements DisplayPanelProvider, SpreadSheetSupporter {

  /**
   * Custom {@link DisplayPanel}.
   *
   * @author  fracpete (fracpete at waikato dot ac dot nz)
   * @version $Revision$
   */
  public static class SpreadSheetDisplayPanel
    extends AbstractTextDisplayPanel
    implements UpdateableDisplayPanel {

    private static final long serialVersionUID = 3524967045456783678L;

    /** the owner. */
    protected SpreadSheetDisplay m_Owner;

    /** the table for displaying the spreadsheet. */
    protected SpreadSheetTable m_Table;

    /** the spreadsheet model. */
    protected SpreadSheetTableModel m_TableModel;

    /** for searching the spreadsheet. */
    protected SearchPanel m_PanelSearch;

    /**
     * Initializes the panel.
     *
     * @param owner	the display this panel belongs to
     */
    public SpreadSheetDisplayPanel(SpreadSheetDisplay owner) {
      super(owner.getClass().getName());
      m_Owner = owner;
      initGUI();
    }

    /**
     * Initializes the widgets.
     */
    @Override
    protected void initGUI() {
      if (m_Owner == null)
	return;

      super.initGUI();

      setLayout(new BorderLayout());
      m_TableModel = new SpreadSheetTableModel(new DefaultSpreadSheet());
      m_TableModel.setReadOnly(m_Owner.getReadOnly());
      m_Table      = new SpreadSheetTable(m_TableModel);
      m_Table.setUseOptimalColumnWidths(m_Owner.getOptimalColumnWidth());
      m_Table.setFont(m_Owner.getFont());
      add(new BaseScrollPane(m_Table), BorderLayout.CENTER);
      JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      SpreadSheetColumnComboBox columnCombo = new SpreadSheetColumnComboBox(m_Table);
      panel.add(columnCombo);
      add(panel, BorderLayout.NORTH);
      m_PanelSearch = null;
      if (m_Owner.getAllowSearch()) {
	m_PanelSearch = new SearchPanel(LayoutType.HORIZONTAL, true);
	m_PanelSearch.addSearchListener(new SearchListener() {
	  @Override
	  public void searchInitiated(SearchEvent e) {
	    m_Table.search(e.getParameters().getSearchString(), e.getParameters().isRegExp());
	  }
	});
	add(m_PanelSearch, BorderLayout.SOUTH);
      }

      if (m_Owner.getSelectedRowsProcessors().length > 0) {
	PopupMenuCustomizer customizer = new PopupMenuCustomizer() {
	  @Override
	  public void customizePopupMenu(MouseEvent e, JPopupMenu menu) {
	    int[] rows;
	    int[] actRows;
	    if (m_Table.getSelectedRows().length == 0)
	      rows = new int[]{m_Table.rowAtPoint(e.getPoint())};
	    else
	      rows = m_Table.getSelectedRows();
	    actRows = new int[rows.length];
	    for (int i = 0; i < rows.length; i++)
	      actRows[i] = m_Table.getActualRow(rows[i]);
	    SpreadSheetTablePopupMenuItemHelper.addProcessSelectedRowsToPopupMenu(m_Table, menu, actRows, rows, Arrays.asList(m_Owner.getSelectedRowsProcessors()));
	  }
	};
	m_Table.setCellPopupMenuCustomizer(customizer);
      }
    }

    /**
     * Displays the token.
     *
     * @param token        the token to display
     */
    @Override
    public void display(Token token) {
      m_TableModel = new SpreadSheetTableModel((SpreadSheet) token.getPayload());
      m_TableModel.setReadOnly(m_Owner.getReadOnly());
      m_Table.setModel(m_TableModel);
      m_Table.setNumDecimals(m_Owner.getNumDecimals());
      m_Table.setCellRenderingCustomizer((CellRenderingCustomizer) OptionUtils.shallowCopy(m_Owner.getCellRenderingCustomizer()));
      m_Table.setShowFormulas(m_Owner.getShowFormulas());
      m_Table.setUseOptimalColumnWidths(m_Owner.getOptimalColumnWidth());
    }

    /**
     * Returns a potentially updated token. Uses {@link #supplyText()} to
     * return a textual token.
     *
     * @return		the token, null if not available
     * @see		#supplyText()
     */
    @Override
    public Token getUpdatedToken() {
      return new Token(m_TableModel.toSpreadSheet());
    }

    /**
     * Returns the custom file filter.
     *
     * @return		the file filter
     */
    @Override
    public ExtensionFileFilter getCustomTextFileFilter() {
      return ExtensionFileFilter.getCsvFileFilter();
    }

    /**
     * Returns the spreadsheet as text (CSV format).
     *
     * @return		the spreadsheet as text
     */
    @Override
    public String supplyText() {
      return m_TableModel.toSpreadSheet().toString();
    }

    /**
     * Clears the panel.
     */
    @Override
    public void clearPanel() {
      m_TableModel = new SpreadSheetTableModel();
      m_Table.setModel(m_TableModel);
    }

    /**
     * Performs clean up operations.
     */
    public void cleanUp() {
    }
  }

  /** for serialization. */
  private static final long serialVersionUID = 3247255046513744115L;

  /** the table. */
  protected SpreadSheetTable m_Table;

  /** the table model. */
  protected SpreadSheetTableModel m_TableModel;

  /** the search panel. */
  protected SearchPanel m_PanelSearch;

  /** the number of decimals for numeric values. */
  protected int m_NumDecimals;

  /** the custom cell renderer. */
  protected CellRenderingCustomizer m_CellRenderingCustomizer;

  /** whether to show the formulas instead of the calculated values. */
  protected boolean m_ShowFormulas;

  /** whether to allow searching. */
  protected boolean m_AllowSearch;

  /** whether to optimize the column width whenever a token is displayed. */
  protected boolean m_OptimalColumnWidth;

  /** whether the table is read only. */
  protected boolean m_ReadOnly;

  /** for processing the selected rows. */
  protected ProcessSelectedRows[] m_SelectedRowsProcessors;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
      "Actor for displaying a spreadsheet.\n"
	+ "Custom background for negative/positive values can be specified as well.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "num-decimals", "numDecimals",
      3, -1, null);

    m_OptionManager.add(
      "cell-rendering-customizer", "cellRenderingCustomizer",
      new DefaultCellRenderingCustomizer());

    m_OptionManager.add(
      "show-formulas", "showFormulas",
      false);

    m_OptionManager.add(
      "allow-search", "allowSearch",
      false);

    m_OptionManager.add(
      "optimal-column-width", "optimalColumnWidth",
      true);

    m_OptionManager.add(
      "read-only", "readOnly",
      true);

    m_OptionManager.add(
      "writer", "writer",
      new NullWriter());

    m_OptionManager.add(
      "selected-rows-processor", "selectedRowsProcessors",
      new ProcessSelectedRows[0]);
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = super.getQuickInfo();

    result += QuickInfoHelper.toString(this, "numDecimals", m_NumDecimals, ", decimals: ");
    result += QuickInfoHelper.toString(this, "cellRenderingCustomizer", m_CellRenderingCustomizer, ", rendering: ");
    result += QuickInfoHelper.toString(this, "showFormulas", m_ShowFormulas, "formulas", ", ");
    result += QuickInfoHelper.toString(this, "allowSearch", m_AllowSearch, "searchable", ", ");
    result += QuickInfoHelper.toString(this, "optimalColumnWidth", m_OptimalColumnWidth, "optimal", ", ");
    result += QuickInfoHelper.toString(this, "readOnly", m_ReadOnly, "read-only", ", ");

    return result;
  }

  /**
   * Sets the number of decimals to display.
   *
   * @param value 	the number of decimals
   */
  public void setNumDecimals(int value) {
    m_NumDecimals = value;
    reset();
  }

  /**
   * Returns the currently set number of decimals to display.
   *
   * @return 		the number of decimals
   */
  public int getNumDecimals() {
    return m_NumDecimals;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String numDecimalsTipText() {
    return "The number of decimals for numeric values.";
  }

  /**
   * Sets the cell rendering customizer.
   *
   * @param value 	the customizer
   */
  public void setCellRenderingCustomizer(CellRenderingCustomizer value) {
    m_CellRenderingCustomizer = value;
    reset();
  }

  /**
   * Returns the cell rendering customizer.
   *
   * @return 		the customizer
   */
  public CellRenderingCustomizer getCellRenderingCustomizer() {
    return m_CellRenderingCustomizer;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String cellRenderingCustomizerTipText() {
    return "The customizer for the cell rendering.";
  }

  /**
   * Sets whether to show the formulas or the calculated values.
   *
   * @param value 	true if to show formulas
   */
  public void setShowFormulas(boolean value) {
    m_ShowFormulas = value;
    reset();
  }

  /**
   * Returns whether to show the formulas or the calculated values.
   *
   * @return 		true if to show formulas
   */
  public boolean getShowFormulas() {
    return m_ShowFormulas;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String showFormulasTipText() {
    return "Whether to show the formulas or the calculated values.";
  }

  /**
   * Sets whether to allow the user to search the table.
   *
   * @param value 	true if to allow search
   */
  public void setAllowSearch(boolean value) {
    m_AllowSearch = value;
    reset();
  }

  /**
   * Returns whether to allow the user to search the table.
   *
   * @return 		true if to allow search
   */
  public boolean getAllowSearch() {
    return m_AllowSearch;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String allowSearchTipText() {
    return "Whether to allow the user to search the table.";
  }

  /**
   * Sets whether calculate the optimal column widht whenever a token is 
   * displayed (= true) or just when the flow finishes.
   *
   * @param value 	true if to always recaculate
   */
  public void setOptimalColumnWidth(boolean value) {
    m_OptimalColumnWidth = value;
    reset();
  }

  /**
   * Returns whether calculate the optimal column widht whenever a token is 
   * displayed (= true) or just when the flow finishes.
   *
   * @return 		true if to always recalculate
   */
  public boolean getOptimalColumnWidth() {
    return m_OptimalColumnWidth;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String optimalColumnWidthTipText() {
    return "Whether to calculate the optimal column width whenever a token is displayed (= enabled) or only when flow finishes.";
  }

  /**
   * Sets whether cells are readonly or editable.
   *
   * @param value 	true if read only
   */
  public void setReadOnly(boolean value) {
    m_ReadOnly = value;
    reset();
  }

  /**
   * Returns whether cells are readonly or editable.
   *
   * @return 		true if read only
   */
  public boolean getReadOnly() {
    return m_ReadOnly;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String readOnlyTipText() {
    return "Whether cells are read-only or editable.";
  }

  /**
   * Sets the processors for the selected rows.
   *
   * @param value 	the processors
   */
  public void setSelectedRowsProcessors(ProcessSelectedRows[] value) {
    m_SelectedRowsProcessors = value;
    reset();
  }

  /**
   * Returns the processors for the selected rows.
   *
   * @return 		the processors
   */
  public ProcessSelectedRows[] getSelectedRowsProcessors() {
    return m_SelectedRowsProcessors;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String selectedRowsProcessorsTipText() {
    return "The schemes that allow processing of the selected rows.";
  }

  /**
   * Returns the default width for the dialog.
   *
   * @return		the default width
   */
  @Override
  protected int getDefaultWidth() {
    return 640;
  }

  /**
   * Returns the default height for the dialog.
   *
   * @return		the default height
   */
  @Override
  protected int getDefaultHeight() {
    return 480;
  }

  /**
   * Clears the content of the panel.
   */
  @Override
  public void clearPanel() {
    m_TableModel = new SpreadSheetTableModel(new DefaultSpreadSheet());
    m_Table      = new SpreadSheetTable(m_TableModel);
  }

  /**
   * Creates the panel to display in the dialog.
   *
   * @return		the panel
   */
  @Override
  public BasePanel newPanel() {
    BasePanel			result;
    JPanel			panel;
    SpreadSheetColumnComboBox	columnCombo;
    PopupMenuCustomizer		customizer;

    result       = new BasePanel(new BorderLayout());
    m_TableModel = new SpreadSheetTableModel(new DefaultSpreadSheet());
    m_TableModel.setReadOnly(m_ReadOnly);
    m_Table      = new SpreadSheetTable(m_TableModel);
    m_Table.setFont(m_Font);
    m_Table.setUseOptimalColumnWidths(m_OptimalColumnWidth);
    result.add(new BaseScrollPane(m_Table), BorderLayout.CENTER);

    panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    columnCombo = new SpreadSheetColumnComboBox(m_Table);
    panel.add(columnCombo);
    result.add(panel, BorderLayout.NORTH);

    m_PanelSearch = null;
    if (m_AllowSearch) {
      m_PanelSearch = new SearchPanel(LayoutType.HORIZONTAL, true);
      m_PanelSearch.addSearchListener(new SearchListener() {
	@Override
	public void searchInitiated(SearchEvent e) {
	  m_Table.search(e.getParameters().getSearchString(), e.getParameters().isRegExp());
	}
      });
      result.add(m_PanelSearch, BorderLayout.SOUTH);
    }

    if (m_SelectedRowsProcessors.length > 0) {
      customizer = new PopupMenuCustomizer() {
	@Override
	public void customizePopupMenu(MouseEvent e, JPopupMenu menu) {
	  int[] rows;
	  int[] actRows;
	  if (m_Table.getSelectedRows().length == 0)
	    rows = new int[]{m_Table.rowAtPoint(e.getPoint())};
	  else
	    rows = m_Table.getSelectedRows();
	  actRows = new int[rows.length];
	  for (int i = 0; i < rows.length; i++)
	    actRows[i] = m_Table.getActualRow(rows[i]);
	  SpreadSheetTablePopupMenuItemHelper.addProcessSelectedRowsToPopupMenu(m_Table, menu, actRows, rows, Arrays.asList(m_SelectedRowsProcessors));
	}
      };
      m_Table.setCellPopupMenuCustomizer(customizer);
    }

    return result;
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		<!-- flow-accepts-start -->adams.data.spreadsheet.SpreadSheet.class<!-- flow-accepts-end -->
   */
  public Class[] accepts() {
    return new Class[]{SpreadSheet.class};
  }

  /**
   * Displays the token (the panel and dialog have already been created at
   * this stage).
   *
   * @param token	the token to display
   */
  @Override
  protected void display(Token token) {
    m_TableModel = new SpreadSheetTableModel((SpreadSheet) token.getPayload());
    m_TableModel.setReadOnly(m_ReadOnly);
    m_Table.setModel(m_TableModel);
    m_Table.setNumDecimals(m_NumDecimals);
    m_Table.setCellRenderingCustomizer((CellRenderingCustomizer) OptionUtils.shallowCopy(m_CellRenderingCustomizer));
    m_Table.setShowFormulas(m_ShowFormulas);
    m_Table.setUseOptimalColumnWidths(m_OptimalColumnWidth);
  }

  /**
   * Creates a new panel for the token.
   *
   * @param token	the token to display in a new panel, can be null
   * @return		the generated panel
   */
  public AbstractDisplayPanel createDisplayPanel(Token token) {
    SpreadSheetDisplayPanel	result;

    result = new SpreadSheetDisplayPanel(this);
    if (token != null)
      result.display(token);

    return result;
  }

  /**
   * Returns whether the created display panel requires a scroll pane or not.
   *
   * @return		true if the display panel requires a scroll pane
   */
  public boolean displayPanelRequiresScrollPane() {
    return false;
  }

  /**
   * Executes the flow item. 
   * <br><br>
   * Outputs the token on the command-line in headless mode.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute() {
    String	result;

    result = null;

    if (isHeadless()) {
      ConsoleHelper.printlnOut("\n--> " + DateUtils.getTimestampFormatterMsecs().format(new Date()) + "\n");
      ConsoleHelper.printlnOut("" + m_InputToken.getPayload());
    }
    else {
      result = super.doExecute();
    }

    return result;
  }

  /**
   * Returns a custom file filter for the file chooser.
   *
   * @return		the file filter, null if to use default one
   */
  @Override
  public ExtensionFileFilter getCustomTextFileFilter() {
    return ExtensionFileFilter.getCsvFileFilter();
  }

  /**
   * Returns the text to save.
   *
   * @return		the text, null if no text available
   */
  @Override
  public String supplyText() {
    String	result;

    result = null;

    if (m_TableModel != null)
      result = m_TableModel.toSpreadSheet().toString();

    return result;
  }

  /**
   * Returns the classes that the supporter generates.
   *
   * @return		the classes
   */
  @Override
  public Class[] getSendToClasses() {
    List<Class> 	result;

    result = new ArrayList<Class>(Arrays.asList(super.getSendToClasses()));
    if (!result.contains(JTable.class))
      result.add(JTable.class);
    if (!result.contains(SpreadSheetTable.class))
      result.add(SpreadSheetTable.class);

    return result.toArray(new Class[result.size()]);
  }

  /**
   * Returns the object to send.
   *
   * @param cls		the classes to retrieve the item for
   * @return		the item to send
   */
  @Override
  public Object getSendToItem(Class[] cls) {
    Object	result;

    result = null;

    if (SendToActionUtils.isAvailable(SpreadSheetTable.class, cls)) {
      result = m_Table;
      if (m_Table.getRowCount() == 0)
	result = null;
    }
    else if (SendToActionUtils.isAvailable(JTable.class, cls)) {
      result = m_Table;
      if (m_Table.getRowCount() == 0)
	result = null;
    }
    else {
      result = super.getSendToItem(cls);
    }

    return result;
  }

  /**
   * Returns the content as spreadsheet.
   *
   * @return		the content, null if not available
   */
  public SpreadSheet toSpreadSheet() {
    if (m_Table != null)
      return m_Table.toSpreadSheet(TableRowRange.VISIBLE);
    return null;
  }

  /**
   * Cleans up after the execution has finished.
   */
  @Override
  public void wrapUp() {
    if ((m_Table != null) && !m_OptimalColumnWidth)
      m_Table.setUseOptimalColumnWidths(true);

    super.wrapUp();
  }
}
