package edu.uclm.esi.ds.domain;

import java.security.SecureRandom;

public class Board {
	private byte[][] digits;

	public Board() {
		this.digits = new byte[9][9];
		SecureRandom dado = new SecureRandom();
		for(int i=0;i<3;i++) {
			for(int j=0;j<9;j++) {
				this.digits[i][j]=(byte) dado.nextInt(1,10);
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
	
	public byte[][] getDigits(){
		return this.digits;
	}
	
}
