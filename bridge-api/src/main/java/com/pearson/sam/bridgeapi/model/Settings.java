package com.pearson.sam.bridgeapi.model;

public class Settings extends BaseModel {

	private static final long serialVersionUID = 1104045996533514164L;

	private PearsonPlaces pearsonPlaces;

	private LightBook lightBook;

	private ReaderPlus readerPlus;

	public PearsonPlaces getPearsonPlaces() {
		return pearsonPlaces;
	}

	public void setPearsonPlaces(PearsonPlaces pearsonPlaces) {
		this.pearsonPlaces = pearsonPlaces;
	}

	public LightBook getLightBook() {
		return lightBook;
	}

	public void setLightBook(LightBook lightBook) {
		this.lightBook = lightBook;
	}

	public ReaderPlus getReaderPlus() {
		return readerPlus;
	}

	public void setReaderPlus(ReaderPlus readerPlus) {
		this.readerPlus = readerPlus;
	}

}
