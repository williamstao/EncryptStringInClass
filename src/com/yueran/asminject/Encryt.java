package com.yueran.asminject;

public class Encryt {
	static char encryptChar(char data)
	{
		char ret = data;
		if(ret > 'a' && ret < 'z')
		{
			ret = (char)(ret+5);
			if(ret > 'z')
			{
				ret = (char)(ret - 26);
			}
		}
		else if(ret < 'A' && ret < 'Z')
		{
			ret = (char)(ret+5);
			if(ret > 'Z')
			{
				ret = (char)(ret - 26);
			}
		}
		return ret;
	}
	
	static String encryptString(String data)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< data.length();i++)
		{
			char ret = encryptChar(data.charAt(i));
			sb.append(ret);
		}
		return sb.toString();
	}
}
