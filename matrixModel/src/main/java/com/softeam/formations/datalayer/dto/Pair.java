package com.softeam.formations.datalayer.dto;

import java.io.Serializable;

/**
 * Created by elkouhen on 25/03/15.
 */
public class Pair<Left, Right> implements Serializable {
	private Left left;
	private Right right;

	public Pair() {

	}

	public Pair(Left left, Right right) {
		this.left = left;
		this.right = right;
	}

	public Left getLeft() {

		return left;
	}

	public Right getRight() {
		return right;
	}

	public void setLeft(Left left) {
		this.left = left;
	}

	public void setRight(Right right) {
		this.right = right;
	}
}
