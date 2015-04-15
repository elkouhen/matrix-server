package com.softeam.formations.datalayer.dto;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by elkouhen on 16/01/15.
 */
public class Matrix implements Serializable {

	@Id
	@JsonProperty("id")
	private String id;

	@JsonProperty("nx")
	private int nx;

	@JsonProperty("data")
	private float data[];

	public Matrix() {
	}

	public Matrix(int nx) {
		this.nx = nx;
		this.data = new float[nx * nx];
	}

	public Matrix(int nx, float data[]) {
		this.nx = nx;
		this.data = data;
	}

	public float get(int i, int j) {
		return data[i * nx + j];
	}

	public float[] getData() {
		return data;
	}

	public int getNx() {
		return nx;
	}

	public void set(int i, int j, float val) {
		data[i * nx + j] = val;
	}

	public void setData(float[] data) {
		this.data = data;
	}

	public void setNx(int nx) {
		this.nx = nx;
	}
}
