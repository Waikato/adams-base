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
 * BlueChannelImageSegmentationReader.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package adams.data.io.input;

import adams.core.Utils;
import adams.core.base.BaseString;
import adams.core.io.FileUtils;
import adams.core.io.PlaceholderFile;
import adams.data.image.BufferedImageHelper;
import adams.data.io.output.AbstractImageSegmentationAnnotationWriter;
import adams.data.io.output.BlueChannelImageSegmentationWriter;
import adams.data.statistics.StatUtils;
import adams.flow.container.ImageSegmentationContainer;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The layers are stored in the blue channel, with 0 being the background.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class BlueChannelImageSegmentationReader
  extends AbstractImageSegmentationAnnotationReader {

  private static final long serialVersionUID = -5567473437385041915L;

  /** whether to skip the first layer (usually background). */
  protected boolean m_SkipFirstLayer;

  /** the layer names. */
  protected BaseString[] m_LayerNames;

  /**
   * Returns a string describing the object.
   *
   * @return a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "The layers are stored in the blue channel, with 0 being the background.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "skip-first-layer", "skipFirstLayer",
      true);

    m_OptionManager.add(
      "layer-name", "layerNames",
      new BaseString[0]);
  }

  /**
   * Sets whether to skip the first layer.
   *
   * @param value	true if to skip
   */
  public void setSkipFirstLayer(boolean value) {
    m_SkipFirstLayer = value;
    reset();
  }

  /**
   * Returns whether to skip the first layer.
   *
   * @return		true if to skip
   */
  public boolean getSkipFirstLayer() {
    return m_SkipFirstLayer;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String skipFirstLayerTipText() {
    return "If enabled, the first layer gets skipped (usually the background).";
  }

  /**
   * Sets the names for the layers to use.
   *
   * @param value	the names
   */
  public void setLayerNames(BaseString[] value) {
    m_LayerNames = value;
    reset();
  }

  /**
   * Returns the names for the layers to use.
   *
   * @return		the names
   */
  public BaseString[] getLayerNames() {
    return m_LayerNames;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String layerNamesTipText() {
    return "The names to use for the layers; if additional layers should be present in the data, names get assigned automatically.";
  }

  /**
   * Returns, if available, the corresponding writer.
   *
   * @return the writer, null if none available
   */
  @Override
  public AbstractImageSegmentationAnnotationWriter getCorrespondingWriter() {
    return new BlueChannelImageSegmentationWriter();
  }

  /**
   * Returns a string describing the format (used in the file chooser).
   *
   * @return a description suitable for displaying in the
   * file chooser
   */
  @Override
  public String getFormatDescription() {
    return "Blue channel image segmentation";
  }

  /**
   * Returns the extension(s) of the format.
   *
   * @return the extension (without the dot!)
   */
  @Override
  public String[] getFormatExtensions() {
    return new String[]{"jpg"};
  }

  /**
   * Returns the default extension of the format.
   *
   * @return the default extension (without the dot!)
   */
  @Override
  public String getDefaultFormatExtension() {
    return "jpg";
  }

  /**
   * Hook method for performing checks before reading the data.
   *
   * @param file	the file to check
   * @return		null if no errors, otherwise error message
   */
  @Override
  protected String check(PlaceholderFile file) {
    String	result;
    File	png;

    result = super.check(file);

    if (result == null) {
      png = FileUtils.replaceExtension(file, ".png");
      if (!png.exists())
        result = "Associated PNG file with annotations is missing!";
    }

    return result;
  }

  /**
   * Reads the image segmentation annotations.
   *
   * @param file the file to read from
   * @return the annotations
   */
  @Override
  protected ImageSegmentationContainer doRead(PlaceholderFile file) {
    ImageSegmentationContainer	result;
    File			png;
    BufferedImage 		baseImage;
    BufferedImage 		pngImage;
    int[] 			pngPixels;
    int[]			unique;
    int[]			uniqueBlue;
    TIntSet			uniqueBlueSet;
    Map<String,BufferedImage> 	layerImages;
    int[][]			layerPixels;
    int				i;
    int				n;
    int				white;
    int				maxLayer;
    String			layerName;

    baseImage     = BufferedImageHelper.read(file).toBufferedImage();
    png           = FileUtils.replaceExtension(file, ".png");
    pngImage      = BufferedImageHelper.read(png).toBufferedImage();
    pngPixels     = BufferedImageHelper.getPixels(pngImage);
    unique        = StatUtils.uniqueValues(pngPixels);
    Arrays.sort(unique);
    uniqueBlue    = new int[unique.length];
    maxLayer      = 0;
    for (i = 0; i < unique.length; i++) {
      uniqueBlue[i] = unique[i] & 0xFF;
      maxLayer      = Math.max(maxLayer, uniqueBlue[i]);
    }
    uniqueBlueSet = new TIntHashSet(uniqueBlue);
    if (isLoggingEnabled())
      getLogger().info("Unique colors: #=" + unique.length + ", values=" + Utils.arrayToString(unique) + ", blue=" + Utils.arrayToString(uniqueBlue));

    // separate pixels
    white       = Color.WHITE.getRGB();
    layerPixels = new int[maxLayer + 1][pngPixels.length];
    for (n = 0; n <= maxLayer; n++) {
      for (i = 0; i < pngPixels.length; i++) {
	if ((pngPixels[i] & 0xFF) == n)
	  layerPixels[n][i] = white;
      }
    }

    // create images
    layerImages = new HashMap<>();
    n           = 0;
    for (i = 0; i <= maxLayer; i++) {
      if (m_SkipFirstLayer && (i == 0))
        continue;
      if (n < m_LayerNames.length)
        layerName = m_LayerNames[n].getValue();
      else
        layerName = "layer-" + (n+1);
      if (uniqueBlueSet.contains(i)) {
	layerImages.put(layerName, new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_RGB));
	layerImages.get(layerName).setRGB(0, 0, baseImage.getWidth(), baseImage.getHeight(), layerPixels[i], 0, baseImage.getWidth());
      }
      n++;
    }

    result = new ImageSegmentationContainer(file.getName(), baseImage, layerImages);
    return result;
  }
}
