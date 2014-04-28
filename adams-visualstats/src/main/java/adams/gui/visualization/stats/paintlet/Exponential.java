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
 * Exponential.java
 * Copyright (C) 2011-2013 University of Waikato, Hamilton, New Zealand
 */

package adams.gui.visualization.stats.paintlet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ExponentialDistributionImpl;

import adams.data.statistics.StatUtils;
import adams.gui.core.GUIHelper;
import adams.gui.event.PaintEvent.PaintMoment;
import adams.gui.visualization.core.axis.Type;
import adams.gui.visualization.core.plot.Axis;

/**
 <!-- globalinfo-start -->
 * Paints the transformed exponential distribution
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * Valid options are: <p/>
 *
 * <pre>-D &lt;int&gt; (property: debugLevel)
 * &nbsp;&nbsp;&nbsp;The greater the number the more additional info the scheme may output to
 * &nbsp;&nbsp;&nbsp;the console (0 = off).
 * &nbsp;&nbsp;&nbsp;default: 0
 * &nbsp;&nbsp;&nbsp;minimum: 0
 * </pre>
 *
 * <pre>-stroke-thickness &lt;float&gt; (property: strokeThickness)
 * &nbsp;&nbsp;&nbsp;The thickness of the stroke.
 * &nbsp;&nbsp;&nbsp;default: 1.0
 * &nbsp;&nbsp;&nbsp;minimum: 0.01
 * </pre>
 *
 * <pre>-color &lt;java.awt.Color&gt; (property: color)
 * &nbsp;&nbsp;&nbsp;Stroke color for the paintlet
 * &nbsp;&nbsp;&nbsp;default: #000000
 * </pre>
 *
 * <pre>-size &lt;int&gt; (property: size)
 * &nbsp;&nbsp;&nbsp;Size of the data points
 * &nbsp;&nbsp;&nbsp;default: 5
 * </pre>
 *
 * <pre>-fill-point (property: fillPoint)
 * &nbsp;&nbsp;&nbsp;Whether to fill the data point with solid color
 * </pre>
 *
 * <pre>-fill-color &lt;java.awt.Color&gt; (property: fillColor)
 * &nbsp;&nbsp;&nbsp;Color for filling data point
 * &nbsp;&nbsp;&nbsp;default: #ff0000
 * </pre>
 *
 <!-- options-end -->
 *
 * @author msf8
 * @version $Revision$
 */
public class Exponential
extends AbstractProbabilityPaintlet{

  /** for serialization */
  private static final long serialVersionUID = -3000060668170218515L;

  @Override
  public void configureAxes() {
    m_AxisBottom = getPanel().getPlot().getAxis(Axis.BOTTOM);
    m_AxisLeft = getPanel().getPlot().getAxis(Axis.LEFT);

    m_AxisBottom.setType(Type.LOG_ABSOLUTE);
    m_AxisBottom.setNumberFormat("#.##");
    m_AxisLeft.setType(Type.LOG_ABSOLUTE);
    m_AxisLeft.setNumberFormat("#.##");
  }

  /**
   * For calculating the dimensions of the plot area.
   */
  @Override
  public void calculateDimensions() {
    double median;
    m_Data = m_Instances.attributeToDoubleArray(m_Index);
    m_TransformedY = new double[m_Data.length];
    Arrays.sort(m_Data);
    for(int i = 0; i< m_Data.length; i++) {
      median = ((i+1)-0.3)/(m_Data.length +0.4);
      //calculate the transformed y values using inverse exponential
      m_TransformedY[i] = -1*(Math.log10(1-median));
    }
    //If the axis can handle the data
    if(m_AxisBottom.getType().canHandle(m_Data[0], m_Data[m_Data.length-1])) {
      m_AxisBottom.setMinimum(m_Data[0]);
      m_AxisBottom.setMaximum(m_Data[m_Data.length-1]);
    }
    else {
      getLogger().severe("errors in plotting");
    }
    if(m_AxisLeft.getType().canHandle(m_TransformedY[0], m_TransformedY[m_TransformedY.length -1])) {
      m_AxisLeft.setMinimum(m_TransformedY[0]);
      m_AxisLeft.setMaximum(m_TransformedY[m_TransformedY.length -1]);
    }
    else {
      getLogger().severe("errors in plotting");
    }
    m_AxisBottom.setAxisName(m_Instances.attribute(m_Index).name() + ")");
    m_AxisLeft.setAxisName("Inverse Exponential");
  }
  
  /**
   * The paint routine of the paintlet.
   *
   * @param g		the graphics context to use for painting
   * @param moment	what {@link PaintMoment} is currently being painted
   */
  @Override
  public void performPaint(Graphics g, PaintMoment moment) {
    if ((m_Instances != null) && (m_Data != null)) {
      GUIHelper.configureAntiAliasing(g, m_AntiAliasingEnabled);
      
      for(int i = 0; i< m_Data.length; i++) {
	Graphics2D g2d = (Graphics2D)g;
	//If data points are to be filled
	if(m_Fill) {
	  g2d.setColor(m_FillColor);
	  g2d.setStroke(new BasicStroke(0));
	  g2d.fillOval(m_AxisBottom.valueToPos(m_Data[i])-m_Size/2, m_AxisLeft.valueToPos(m_TransformedY[i])-m_Size/2, m_Size, m_Size);
	}
	//outline of data point
	g2d.setStroke(new BasicStroke(m_StrokeThickness));
	g2d.setColor(m_Color);
	g2d.drawOval(m_AxisBottom.valueToPos(m_Data[i])-m_Size/2, m_AxisLeft.valueToPos(m_TransformedY[i])-m_Size/2, m_Size, m_Size);
      }
      //if drawing regression fit diagonal
      if(m_RegressionLine) {
	g.setColor(Color.BLACK);
	double[] newData = new double[m_Data.length];
	for(int i = 0; i < m_Data.length; i++) {
	  newData[i] = Math.log(m_Data[i]);
	}
	ExponentialDistributionImpl ex = new ExponentialDistributionImpl(StatUtils.mean(newData));
	//draw the expected diagonal line using the exponential distribution
	for(int i = 0; i< m_Data.length-1; i++) {
	  double prob1;
	  try {
	    prob1 = ex.cumulativeProbability(newData[i]);
	  } catch (MathException e) {
	    prob1 = 0;
	  }
	  double prob2;
	  try {
	    prob2 = ex.cumulativeProbability(newData[i+1]);
	  } catch (MathException e) {
	    prob2 = 0;
	  }
	  double p1 = -Math.log(1-prob1);
	  double p2 = -Math.log(1-prob2);
	  g.drawLine(m_AxisBottom.valueToPos(m_Data[i]), m_AxisLeft.valueToPos(p1), m_AxisBottom.valueToPos(m_Data[i+1]), m_AxisLeft.valueToPos(p2));
	}
      }
    }
  }

  @Override
  public String globalInfo() {
    return "Paints the transformed exponential distribution";
  }

  @Override
  public boolean hasFitLine() {
    return false;

  }
}