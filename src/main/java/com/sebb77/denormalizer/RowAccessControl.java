package com.sebb77.denormalizer;

class RowAccessControl {

	private int current;
	private boolean currentRowUsed;

	public RowAccessControl() {
		this(0);
	}

	public RowAccessControl(int firstRow) {
		this.current = firstRow;
		this.currentRowUsed = false;
	}

	public int currentRow() {
		currentRowUsed = true;
		return current;
	}

	public int newRow() {
		int result = current;
		if (currentRowUsed) {
			result = current++;
		}
		return result;
	}
}