package com.nxtlife.efkon.enforcementconfigurator.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateResponse;

public class MathUtil {

	static class Point {
		BigDecimal x;
		BigDecimal y;

		public Point(BigDecimal x, BigDecimal y) {
			this.x = x;
			this.y = y;
		}
	}

	private static boolean onSegment(Point p1, Point p2, Point p3) {
		if (p2.x.compareTo(p1.x.max(p3.x)) <= 0 && p2.x.compareTo(p1.x.min(p3.x)) >= 0
				&& p2.y.compareTo(p1.y.max(p3.y)) <= 0 && p2.y.compareTo(p1.y.min(p3.y)) >= 0) {
			return true;
		}
		return false;
	}

	/*
	 * To find direction of ordered triplet (p, q, r). The function returns
	 * following values 0 --> p, q and r are collinear 1 --> Clockwise 2 -->
	 * Counterclockwise
	 */

	private static int direction(Point p1, Point p2, Point p3) {
		BigDecimal val = ((p2.y.subtract(p1.y)).multiply(p3.x.subtract(p2.x)))
				.subtract((p2.x.subtract(p1.x)).multiply(p3.y.subtract(p2.y)));

		if (val.compareTo(BigDecimal.ZERO) == 0) {
			return 0; // collinear
		}
		return (val.compareTo(BigDecimal.ZERO)) > 0 ? 1 : 2; // clock or counterclock wise
	}

	// The function that returns true if
	// line segment 'pq' and 'rs' intersect.
	private static boolean doIntersect(Point p, Point q, Point r, Point s) {
		// Find the four direction needed for
		// general and special cases
		int d1 = direction(p, q, r);
		int d2 = direction(p, q, s);
		int d3 = direction(r, s, p);
		int d4 = direction(r, s, q);

		// General case
		if (d1 != d2 && d3 != d4) {
			return true;
		}

		// Special Cases
		// p, q and r are collinear and
		// r lies on segment pq
		if (d1 == 0 && onSegment(p, r, q)) {
			return true;
		}

		// p, q and s are collinear and
		// s lies on segment pq
		if (d2 == 0 && onSegment(p, s, q)) {
			return true;
		}

		// r, s and p are collinear and
		// p lies on segment rs
		if (d3 == 0 && onSegment(r, p, s)) {
			return true;
		}

		// r, s and q are collinear and
		// q lies on segment rs
		if (d4 == 0 && onSegment(r, q, s)) {
			return true;
		}

		// Doesn't fall in any of the above cases
		return false;
	}

	public static boolean isInside(CameraImageCoordinateResponse mainCoordinate, BigDecimal x1, BigDecimal x2) {
		List<Point> mainPoints = new ArrayList<>();
		mainPoints.add(new Point(mainCoordinate.getaPointXCoordinate(), mainCoordinate.getaPointYCoordinate()));
		mainPoints.add(new Point(mainCoordinate.getbPointXCoordinate(), mainCoordinate.getbPointYCoordinate()));
		mainPoints.add(new Point(mainCoordinate.getcPointXCoordinate(), mainCoordinate.getcPointYCoordinate()));
		mainPoints.add(new Point(mainCoordinate.getdPointXCoordinate(), mainCoordinate.getdPointYCoordinate()));
		Point p = new Point(x1, x2);
		Point extreme = new Point(BigDecimal.valueOf(1000000), x2);
		int count = 0, i = 0;
		do {
			int next = (i + 1) % 4;
			if (doIntersect(mainPoints.get(i), mainPoints.get(next), p, extreme)) {
				if (direction(mainPoints.get(i), p, mainPoints.get(next)) == 0) {
					return onSegment(mainPoints.get(i), p, mainPoints.get(next));
				}

				count++;
			}
			i = next;
		} while (i != 0);

		return (count % 2 == 1);

	}

}
