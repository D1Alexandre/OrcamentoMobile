package com.br.skysoftmobile.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Tools {
	
	@SuppressLint("SimpleDateFormat")
	public static String DataFB(long data_time){
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");  
		Date data = new Date(data_time);	
		return df.format(data);
	}
	public static String ValorFB(double valor){
		if(Double.isNaN(valor)) valor = 0;
		String val = String.valueOf(valor);
		val.replace(',', '.');
		return val;
	}
	public static String LongtoData(long data_time){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");  
		Date data = new Date(data_time);	
		return df.format(data);
	}
	public static Long DatatoLong(String data, String format) throws Exception{		
		try{
			SimpleDateFormat df = new SimpleDateFormat(format);
			Date dt = df.parse(data);
			return dt.getTime();
		}catch (Exception e) {
			throw e;
		}
		
	}
	public static String DecryptSenha(String Src){
		if (Src == "") return null;
		String Key = "YUQL23KL23DF90WI5E1JAS467NMCXXL6JAOAUWWMCL0AOMM4A4VZYW9KHJUI2347EJHJKDF3424SKL K3LAKDJSL9RTIKJ";
		String Dest = ""; 
		int KeyLen = Key.length();
		char[] KeyArr = Key.toCharArray();
		int KeyPos = -1;
		int SrcAsc = 0;	
		int OffSet = Integer.parseInt(Src.substring(0,2), 16); 
		for(int SrcPos = 2; SrcPos < Src.length(); SrcPos = SrcPos + 2){
			SrcAsc = Integer.parseInt(Src.substring(SrcPos, SrcPos + 2), 16);
			if (KeyPos < KeyLen) KeyPos = KeyPos + 1;  
			else KeyPos = 0; 
			int ord = KeyArr[KeyPos];
			int TmpSrcAsc = SrcAsc ^ ord;
			if (TmpSrcAsc <= OffSet)TmpSrcAsc = 255 + TmpSrcAsc - OffSet;
			else TmpSrcAsc = TmpSrcAsc - OffSet;
			Dest += (char) TmpSrcAsc;
			OffSet = SrcAsc;
		}
		return Dest;
	}
}
