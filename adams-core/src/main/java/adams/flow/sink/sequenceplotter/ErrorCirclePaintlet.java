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
 * ErrorCirclePaintlet.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.sink.sequenceplotter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import adams.data.sequence.XYSequence;
import adams.data.sequence.XYSequencePoint;
import adams.gui.core.GUIHelper;
import adams.gui.event.PaintEvent.PaintMoment;
import adams.gui.visualization.core.AxisPanel;
import adams.gui.visualization.core.plot.Axis;
import adams.gui.visualization.sequence.AbstractXYSequencePointHitDetector;
import adams.gui.visualization.sequence.CircleHitDetector;
import adams.gui.visualization.sequence.CirclePaintlet;

/**
 <!-- globalinfo-start -->
 * Paintlet for painting circles with diameters based on the error at the specified X-Y position. Prefers X errors over Y errors.
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-stroke-thickness &lt;float&gt; (property: strokeThickness)
 * &nbsp;&nbsp;&nbsp;The thickness of the stroke.
 * &nbsp;&nbsp;&nbsp;default: 1.0
 * &nbsp;&nbsp;&nbsp;minimum: 0.01
 * </pre>
 * 
 * <pre>-diameter &lt;int&gt; (property: diameter)
 * &nbsp;&nbsp;&nbsp;The diameter of the circle in pixels.
 * &nbsp;&nbsp;&nbsp;default: 7
 * &nbsp;&nbsp;&nbsp;minimum: 1
 * </pre>
 * 
 * <pre>-anti-aliasing-enabled &lt;boolean&gt; (property: antiAliasingEnabled)
 * &nbsp;&nbsp;&nbsp;If enabled, uses anti-aliasing for drawing circles.
 * &nbsp;&nbsp;&nbsp;default: true
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 8902 $
 */
public class ErrorCirclePaintlet
  extends CirclePaintlet {

  /** for serialization. */
  private static final long serialVersionUID = -8772546156227148237L;
  
  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return 
	"Paintlet for painting circles with diameters based on the error at "
	+ "the specified X-Y position. Prefers X errors over Y errors.";
  }

  /**
   * Returns a new instance of the hit detector to use.
   *
   * @return		the hit detector
   */
  @Override
  public AbstractXYSequencePointHitDetector newHitDetector() {
    return new CircleHitDetector(getSequencePanel());
  }

  /**
   * Draws the custom data with the given color.
   *
   * @param g		the graphics context
   * @param moment	the paint moment
   * @param data	the data to draw
   * @param color	the color to draw in
   */
  @Override
  public void drawCustomData(Graphics g, PaintMoment moment, XYSequence data, Color color) {
    List<XYSequencePoint>	points;
    XYSequencePoint		curr;
    int				currX;
    int				currY;
    AxisPanel			axisX;
    AxisPanel			axisY;
    int				i;
    int				diameter;
    SequencePlotPoint		ppoint;
    Double[]			errors;

    points = data.toList();
    axisX  = getPanel().getPlot().getAxis(Axis.BOTTOM);
    axisY  = getPanel().getPlot().getAxis(Axis.LEFT);

    // paint all points
    g.setColor(color);
    GUIHelper.configureAntiAliasing(g, m_AntiAliasingEnabled);

    currX = Integer.MIN_VALUE;
    currY = Integer.MIN_VALUE;

    for (i = 0; i < data.size(); i++) {
      curr = (XYSequencePoint) points.get(i);

      // determine coordinates
      currX = axisX.valueToPos(XYSequencePoint.toDouble(curr.getX()));
      currY = axisY.valueToPos(XYSequencePoint.toDouble(curr.getY()));

      diameter = m_Diameter;
      if (curr instanceof SequencePlotPoint) {
	ppoint = (SequencePlotPoint) curr;
	if (ppoint.hasErrorX()) {
	  errors = ppoint.getErrorX();
	  if (errors.length == 1)
	    diameter = Math.max(diameter, axisX.valueToPos(currX - errors[0]) - axisX.valueToPos(currX + errors[0]));
	  else
	    diameter = Math.max(diameter, axisX.valueToPos(errors[1]) - axisX.valueToPos(errors[0]));
	}
	else if (ppoint.hasErrorY()) {
	  errors = ppoint.getErrorY();
	  if (errors.length == 1)
	    diameter = Math.max(diameter, axisY.valueToPos(currY - errors[0]) - axisY.valueToPos(currY + errors[0]));
	  else
	    diameter = Math.max(diameter, axisY.valueToPos(errors[1]) - axisY.valueToPos(errors[0]));
	}
      }
      
      currX -= (diameter / 2);
      currY -= (diameter / 2);
      
      // draw circle
      g.drawOval(currX, currY, diameter - 1, diameter - 1);
    }
  }
}
