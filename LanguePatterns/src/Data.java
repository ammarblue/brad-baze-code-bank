//This is a program ment to determin langue of a text
import java.util.*;
public class Data {
	String text;
	String langue;
	int length;
	int[][] letters=new int[52][2];
	double[] rate=new double[26];
	Data(){
		System.out.println("\nWhat is the langue");
		Scanner lan=new Scanner(System.in);
		langue=lan.next();
		for(int i=0;i<26;i++){
			letters[i][0]=i+'A';
		}
		for(int j=0;j<26;j++){
			letters[j+26][0]='a'+j;
		}
	}
	public void parse(){
		System.out.println("Enter the "+langue+" text");
		Scanner parse=new Scanner(System.in);
		text=parse.nextLine();
		System.out.println(text);
	}
	public void length(){
		length=text.length();
	}
	public void Num_of_letters(){
		for(int i=0;i<length;i++){
			for(int j=65;j<91;j++){
				if(text.charAt(i)!=j||text.charAt(i)=='\0'){
					continue;
				}else if(text.charAt(i)==j){
					letters[j-65][1]++;
				}
			}
			for(int k=97;k<123;k++){
				if(text.charAt(i)!=k||text.charAt(i)=='\0'){
					continue;
				}else if(text.charAt(i)==k){
					letters[k-71][1]++;
				}
			}
		}
	}
	public void printOut(){
		System.out.println("The number of char "+length);
		for(int i=0;i<2;i++){
			System.out.print("\n");
			for(int j=0;j<52;j++){
				if(i==1){
					System.out.print(letters[j][i]+" ");
				}else if(i==0){
					System.out.print((char)letters[j][i]+" ");
				}

			}
		}
	}
	public void Ratio(){
		for(int i=26;i<52;i++){
			rate[i-26]=((double)letters[i][1]/length)*100;
		}
	}
	public void RatePrint(){
		System.out.println("\nRatios:");
		for(int i=0;i<26;i++){
			System.out.print(rate[i]+" ");
		}
	}
	public void WhatLangue(){
		if(rate[4]<17&&rate[4]>11){
			System.out.println("\nGerman");
		}else{
			System.out.println("\nEnglish");
		}
	}
}
