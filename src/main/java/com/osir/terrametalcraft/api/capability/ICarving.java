package com.osir.terrametalcraft.api.capability;

public interface ICarving {
	long getCarveData(int index);

	long[] getAllCarveData();

	void setCarveData(int index, long data);

	void setAllCarveData(long[] data);

	boolean isAreaValid(int index, float require, float tolerance);

	boolean isEmpty();
}