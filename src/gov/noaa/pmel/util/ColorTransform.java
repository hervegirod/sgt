/*
 * $Id: ColorTransform.java,v 1.1 2002/11/26 23:34:54 oz Exp $
 *
 * This software is provided by NOAA for full, free and open release.  It is
 * understood by the recipient/user that NOAA assumes no liability for any
 * errors contained in the code.  Although this software is released without
 * conditions or restrictions in its use, it is expected that appropriate
 * credit be given to its author and to the National Oceanic and Atmospheric
 * Administration should the software be included by the recipient as an
 * element in other product development.
 */
package gov.noaa.pmel.util;

import gov.noaa.pmel.sgt.Transform;
import gov.noaa.pmel.sgt.TransformColor;

/**
 * <code>Transform</code> defines an interface for transformations between
 * user and physical coordinates.
 *
 * @author Donald Denbo
 * @version $Revision: 1.1 $, $Date: 2002/11/26 23:34:54 $
 * @since 1.0
 */
public class ColorTransform implements TransformColor {
   Transform mRedTransform;
   Transform mGreenTransform;
   Transform mBlueTransform;

   ;

	public ColorTransform() {
   }

   public ColorTransform(Transform redTransform, Transform greenTransform, Transform blueTransform) {
      mRedTransform = redTransform;
      mGreenTransform = greenTransform;
      mBlueTransform = blueTransform;
   }

   @Override
   public Transform getRedTransform() {
      return mRedTransform;
   }

   @Override
   public void setRedTransform(Transform redTransform) {
      mRedTransform = redTransform;
   }

   @Override
   public Transform getGreenTransform() {
      return mGreenTransform;
   }

   @Override
   public void setGreenTransform(Transform greenTransform) {
      mGreenTransform = greenTransform;
   }

   @Override
   public Transform getBlueTransform() {
      return mBlueTransform;
   }

   @Override
   public void setBlueTransform(Transform blueTransform) {
      mBlueTransform = blueTransform;
   }

   @Override
   public void setColorTransforms(Transform red, Transform green, Transform blue) {
      mRedTransform = red;
      mGreenTransform = green;
      mBlueTransform = blue;
   }
}
