package com.softeam.formations.resources.dto;

import java.io.Serializable;

/**
 * Created by elkouhen on 16/01/15.
 */
public class Matrix implements Serializable {

    private int nx;
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

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public float[] getData() {
        return data;
    }

    public void setData(float[] data) {
        this.data = data;
    }

    public void set(int i, int j, float val) {
        data[i * nx + j] = val;
    }

    public float get(int i, int j) {
        return data[i * nx + j];
    }
}
