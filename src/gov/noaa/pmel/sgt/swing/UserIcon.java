/*
 * $Id: UserIcon.java,v 1.18 2003/08/22 23:02:39 dwd Exp $
 *
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */

package gov.noaa.pmel.sgt.swing;


import gov.noaa.pmel.sgt.LayerChild;
import gov.noaa.pmel.sgt.Layer;
import gov.noaa.pmel.sgt.CartesianGraph;
import gov.noaa.pmel.sgt.AbstractPane;
import gov.noaa.pmel.util.Point2D;
import gov.noaa.pmel.util.Debug;
import gov.noaa.pmel.util.SoTPoint;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Image;
import java.net.URL;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.FontMetrics;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;
import java.beans.VetoableChangeListener;

/**
 * <code>UserIcon</code> extends <code>ImageIcon</code> to create a
 * icon than can be dragged on a <code>sgt</code> plot displaying a
 * user defined text string along with the image.
 *
 * @author Donald Denbo
 * @version $Revision: 1.18 $, $Date: 2003/08/22 23:02:39 $
 * @since 2.0
 * @see ValueIcon
 */
public class UserIcon extends ImageIcon implements LayerChild, Draggable {
  private boolean selected_ = false;
  private boolean selectable_ = true;
  protected boolean moved_ = false;
  protected Layer layer_ = null;
  private String id_ = null;
  private boolean visible_ = true;
  protected Rectangle bounds_ = new Rectangle();
  protected Point2D.Double loc_ = new Point2D.Double();
  protected SoTPoint uLoc_ = new SoTPoint(0.0, 0.0);
  private Font font_ = new Font("Dialog", Font.PLAIN, 10);
  private Color textColor_ = Color.black;
  protected PropertyChangeSupport changes_ = new PropertyChangeSupport(this);
  protected VetoableChangeSupport vetos_ = new VetoableChangeSupport(this);

  /**
   * Construct a <code>UserIcon</code> using an image from a
   * specified file.
   *
   * @param filename name of image file
   * @param description brief textual description of the image
   */
  public UserIcon(String filename, String description) {
    super(filename, description);
  }
  /**
   * Construct a <code>UserIcon</code> using an image from a
   * specified <code>URL</code>.
   *
   * @param location URL of image file
   * @param description brief textual description of the image
   */
  public UserIcon(URL location, String description) {
    super(location, description);
  }
  /**
   * Construct a <code>UserIcon</code> using an <code>Image</code>.
   *
   * @param image the image
   * @param description brief textual description of the image
   */
  public UserIcon(Image image, String description) {
    super(image, description);
  }
  /**
   * Paint the icon at the specified location.
   */
  public void paintIcon(Component c, Graphics g, int x, int y) {
    computeBounds(g);
    bounds_.x = x;
    bounds_.y = y;
    if(visible_) {
    g.drawImage(getImage(), bounds_.x, bounds_.y, layer_.getPane().getComponent());
    int xl = bounds_.x + super.getIconWidth();
    int yl = bounds_.y + super.getIconHeight();
    g.setFont(font_);
    g.setColor(textColor_);
    g.drawString(getDescription(), xl, yl);
    }
  }
  /**
   * Set the font for the value label.
   *
   * @param font the font
   */
  public void setFont(Font font) {
    font_ = font;
  }
  /**
   * Get the value label font
   */
  public Font getFont() {
    return font_;
  }
  /**
   * Get the total width, icon + label.
   */
  public int getIconWidth() {
    return bounds_.width;
  }
  /**
   * Get the total heigth.
   */
  public int getIconHeight() {
    return bounds_.height;
  }
  private void computeBounds(Graphics g) {
    int wid = super.getIconWidth();
    int hgt = super.getIconHeight();
    g.setFont(font_);
    FontMetrics fmet = g.getFontMetrics();
    bounds_.width = wid + fmet.stringWidth(getDescription());
    bounds_.height = hgt;
  }

  public LayerChild copy() {
    return null;
  }

  public void setVisible(boolean vis) {
    if(visible_ != vis) {
      visible_ = vis;
//        modified("UserIcon: setVisible(" + vis + ")");
    }
  }

  public boolean isVisible() {
    return visible_;
  }

  public void draw(Graphics g) {
    int x = ((CartesianGraph)layer_.getGraph()).getXUtoD(uLoc_.getX());
    int y = ((CartesianGraph)layer_.getGraph()).getYUtoD(uLoc_.getY());
    paintIcon(layer_.getPane().getComponent(), g, x, y);
  }

  public String getId() {
    return id_;
  }

  public Layer getLayer() {
    return layer_;
  }

  public AbstractPane getPane() {
    return layer_.getPane();
  }

  public void modified(String mess) {
    if(layer_ != null)
      layer_.modified(mess);
  }

  public void setId(String id) {
    id_ = id;
  }

  public void setLayer(Layer l) {
    layer_ = l;
  }
  public String toString() {
    return "UserIcon: " + id_;
  }

  public Rectangle getBounds() {
    return bounds_;
  }

  public boolean isSelected() {
    return selected_;
  }

  public void setSelected(boolean sel) {
    selected_ = sel;
  }

  public boolean isSelectable() {
    return selectable_;
  }

  public void setSelectable(boolean select) {
    selectable_ = select;
  }
  /**
   * Get the icon location in physical units.
   */
  public Point2D.Double getLocationP() {
    return loc_;
  }
  /**
   * Set the icon location in physical units.
   * <BR><B>Property Change:</B> <code>location</code>.
   */
  public void setLocationP(Point2D.Double loc) {
    SoTPoint pt;
    loc_ = loc;
    bounds_.x = layer_.getXPtoD(loc_.x);
    bounds_.y = layer_.getYPtoD(loc_.y);
    pt = ((CartesianGraph)layer_.getGraph()).getPtoU(loc_);
    if(!pt.equals(uLoc_) || moved_) {
      changes_.firePropertyChange("location",
                                  uLoc_,
                                  pt);
      uLoc_ = pt;
      moved_ = false;
    }
  }
  /**
   * Get the icon location in user units.
   *
   * @since 3.0
   */
  public SoTPoint getLocationU() {
    return uLoc_;
  }
  /**
   * Set the icon location in user units.  Location change can't be
   * vetoed.
   *
   * @since 3.0
   */
  public void setLocationUNoVeto(SoTPoint loc) {
    moved_ = moved_ || !loc.equals(uLoc_);
    uLoc_ = loc;
    loc_.x = ((CartesianGraph)layer_.getGraph()).getXUtoP(uLoc_.getX());
    loc_.y = ((CartesianGraph)layer_.getGraph()).getYUtoP(uLoc_.getY());
    bounds_.x = layer_.getXPtoD(loc_.x);
    bounds_.y = layer_.getYPtoD(loc_.y);
  }
  /**
   * Set the icon location in user units.  Location change can be
   * vetoed.
   * <BR><B>Property Change:</B> <code>location</code>.
   *
   * @since 3.0
   */
  public void setLocationU(SoTPoint loc) throws PropertyVetoException {
    if(!loc.equals(uLoc_) || moved_) {
      vetos_.fireVetoableChange("location", uLoc_, loc);

      changes_.firePropertyChange("location",
                                   uLoc_,
                                   loc);
      uLoc_ = loc;
      moved_ = false;
      loc_.x = ((CartesianGraph)layer_.getGraph()).getXUtoP(uLoc_.getX());
      loc_.y = ((CartesianGraph)layer_.getGraph()).getYUtoP(uLoc_.getY());
      bounds_.x = layer_.getXPtoD(loc_.x);
      bounds_.y = layer_.getYPtoD(loc_.y);
    }
  }
  /**
   * Set icon location in device coordinates. Locatoin change can't be
   * vetoed.
   */
  public void setLocationNoVeto(int x, int y) {
    SoTPoint pt;
    bounds_.x = x;
    bounds_.y = y;
    loc_.x = layer_.getXDtoP(x);
    loc_.y = layer_.getYDtoP(y);
    pt = ((CartesianGraph)layer_.getGraph()).getPtoU(loc_);
    moved_ = moved_ || !pt.equals(uLoc_);
    uLoc_ = pt;
  }
  /**
   * Set icon location in device units
   */
  public void setLocation(Point loc) {
    setLocation(loc, true);
  }
  /**
   * Set icon location in device units and optionally fire a
   * <code>PropertyChangeEvent</code>.
   */
  public void setLocation(Point loc, boolean fireEvent) {
    setBounds(loc.x, loc.y, 0, 0, fireEvent);
  }
  /**
   * Set icon bounds.
   * <BR><B>Property Change:</B> <code>location</code>.
   */
  public void setBounds(int x, int y, int width, int height) {
    setBounds(x, y, width, height, true);
  }

  protected void setBounds(int x, int y, int width, int height, boolean fireEvent) {
    SoTPoint pt;
    bounds_.x = x;
    bounds_.y = y;
    loc_.x = layer_.getXDtoP(x);
    loc_.y = layer_.getYDtoP(y);
    pt = ((CartesianGraph)layer_.getGraph()).getPtoU(loc_);
    moved_ = moved_ || !pt.equals(uLoc_);
    if(moved_) {
      SoTPoint temp = new SoTPoint(pt);
      if(fireEvent) {
        changes_.firePropertyChange("location",
                                    uLoc_,
                                    temp);
        moved_ = false;
      }
      uLoc_ = temp;
    }
  }
  /**
   * Set icon bounds.
   */
  public void setBounds(Rectangle bounds) {
    setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
  }
  public void addVetoableChangeListener(VetoableChangeListener l) {
    vetos_.addVetoableChangeListener(l);
  }
  public void removeVetoableChangeListener(VetoableChangeListener l) {
    vetos_.removeVetoableChangeListener(l);
  }
  public void addPropertyChangeListener(PropertyChangeListener l) {
    changes_.addPropertyChangeListener(l);
  }
  public void removePropertyChangeListener(PropertyChangeListener l) {
    changes_.removePropertyChangeListener(l);
  }
}

