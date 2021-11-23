package com.osir.terrametalcraft.api.capability;

public interface IPottery {
	int getData(int index);

	int[] getAllData();

	void setData(int index, int data);

	void setAllData(int[] data);

	void potteryWork(int index, int count);

	boolean isAreaValid(int index, int min, int max);

	boolean isEmpty();
}