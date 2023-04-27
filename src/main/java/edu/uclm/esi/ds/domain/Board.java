package edu.uclm.esi.ds.domain;

import java.security.SecureRandom;

public class Board {
	private int[][] digits;

	public Board() {
		this.digits = new int[9][9];
		SecureRandom dado = new SecureRandom();
		for(int i=0;i<3;i++) {
			for(int j=0;j<9;j++) {
				this.digits[i][j]= dado.nextInt(1,10);
			}
		}
	}
	
	public void addRow() {
		int contador = 0;
		SecureRandom dado = new SecureRandom();
	    for (int i = 0; i < this.digits.length && contador < 2; i++) {
	        boolean allZeros = true;
	        for (int j = 0; j < this.digits[i].length; j++) {
	            if (this.digits[i][j] != 0) {
	                allZeros = false;
	                break;
	            }
	        }
	        if (allZeros) {
	        	contador++;
	            for (int j = 0; j < this.digits[i].length; j++) {
	            	this.digits[i][j] = dado.nextInt(1,10);
	            }
	        }
	    }
	}

	public Board copy() {
		
		Board result = new Board();
		
		for(int i=0;i<3;i++) {
			for(int j=0;j<9;j++) {
				result.digits[i][j]=this.digits[i][j];
			}
		}
		return result;
	}
	
	public int[][] getDigits(){
		return this.digits;
	}
	public void setDigits(int[][] digits) {
		this.digits=digits;
	}
}
