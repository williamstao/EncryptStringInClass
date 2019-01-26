
public class Crypto 
{
	static char DecryptChar(char data)
	{
		char ret = data;
		if(ret > 'a' && ret < 'z')
		{
			ret = (char)(ret-5);
			if(ret < 'a')
			{
				ret = (char)(ret + 26);
			}
		}
		else if(ret < 'A' && ret < 'Z')
		{
			ret = (char)(ret-5);
			if(ret < 'A')
			{
				ret = (char)(ret + 26);
			}
		}
		return ret;
	}
	
	static String DecryptString(String data)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< data.length();i++)
		{
			char ret = DecryptChar(data.charAt(i));
			sb.append(ret);
		}
		return sb.toString();
	}
}
