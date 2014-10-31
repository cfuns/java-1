package de.benjaminborbe.poker.table.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.vecmath.Vector2d;
import java.io.Serializable;

public class Point implements Serializable, IsSerializable {

	private static final long serialVersionUID = 4758595814923081533L;

	private Vector2d Vector2d = new Vector2d();

	public Point() {
		Vector2d.set(1, 1);
	}

}
