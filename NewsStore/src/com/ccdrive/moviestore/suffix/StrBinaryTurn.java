package com.ccdrive.moviestore.suffix;

public class StrBinaryTurn {
    //将Unicode字符串转换成bool型数组
    public boolean[] StrToBool(String input){
        boolean[] output=Binstr16ToBool(BinstrToBinstr16(StrToBinstr(input)));
        return output;
    }
    //将bool型数组转换成Unicode字符串
    public String BoolToStr(boolean[] input){
        String output=BinstrToStr(Binstr16ToBinstr(BoolToBinstr16(input)));
        return output;
    }
    //将字符串转换成二进制字符串，以空格相隔
    public String StrToBinstr(String str) {
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            result +=Integer.toBinaryString(strChar[i])+ " ";           
        }
        return result;
    }
    //将二进制字符串转换成Unicode字符串
    public String BinstrToStr(String binStr) {
        String[] tempStr=StrToStrArray(binStr);
        char[] tempChar=new char[tempStr.length];
        for(int i=0;i<tempStr.length;i++) {
            tempChar[i]=BinstrToChar(tempStr[i]);
        }
        return String.valueOf(tempChar);
    }
    //将二进制字符串格式化成全16位带空格的Binstr
    private String BinstrToBinstr16(String input){
        StringBuffer output=new StringBuffer();
        String[] tempStr=StrToStrArray(input);
        for(int i=0;i<tempStr.length;i++){
            for(int j=16-tempStr[i].length();j>0;j--)
                output.append('0');
            output.append(tempStr[i]+" ");
        }
        return output.toString();
    }
    //将全16位带空格的Binstr转化成去0前缀的带空格Binstr
    public static String Binstr16ToBinstr(String input){
        StringBuffer output=new StringBuffer();
        String[] tempStr=StrToStrArray(input);
        for(int i=0;i<tempStr.length;i++){
            for(int j=0;j<16;j++){
                if(tempStr[i].charAt(j)=='1'){
                    output.append(tempStr[i].substring(j)+" ");
                    break;
                }
                if(j==15&&tempStr[i].charAt(j)=='0')
                    output.append("0"+" ");
            }
        }
        return output.toString();
    }   
    //二进制字串转化为boolean型数组  输入16位有空格的Binstr
    private boolean[] Binstr16ToBool(String input){
        String[] tempStr=StrToStrArray(input);
        boolean[] output=new boolean[tempStr.length*16];
        for(int i=0,j=0;i<input.length();i++,j++)
            if(input.charAt(i)=='1')
                output[j]=true;
            else if(input.charAt(i)=='0')
                output[j]=false;
            else
                j--;
        return output;
    }
    //boolean型数组转化为二进制字串  返回带0前缀16位有空格的Binstr
    private String BoolToBinstr16(boolean[] input){ 
        StringBuffer output=new StringBuffer();
        for(int i=0;i<input.length;i++){
            if(input[i])
                output.append('1');
            else
                output.append('0');
            if((i+1)%16==0)
                output.append(' ');           
        }
        output.append(' ');
        return output.toString();
    }
    //将二进制字符串转换为char
    private char BinstrToChar(String binStr){
        int[] temp=BinstrToIntArray(binStr);
        int sum=0;   
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;
        }   
        return (char)sum;
    }
    //将初始二进制字符串转换成字符串数组，以空格相隔
    private static String[] StrToStrArray(String str) {
        return str.split(" ");
    }
    //将二进制字符串转换成int数组
    private int[] BinstrToIntArray(String binStr) {       
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];   
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }
   
    public static String StrToHex(String str)
	{
	   if(str == null) return null;
	   String content = str;
	   String   Digital="0123456789ABCDEF";   
	        StringBuffer   sb=new   StringBuffer("");   
	        byte[]   bs=content.getBytes();   
	        int   bit;   
	        for(int   i=0;i<bs.length;i++){   
	            bit=(bs[i]&0x0f0)>>4;   
	            sb.append(Digital.substring(bit,bit+1));   
	            bit=bs[i]&0x0f;   
	            sb.append(Digital.substring(bit,bit+1));   
	        }   
	   return sb.toString();
	}

	public static String HexToStr(String str)
	{
	   if(str == null) return null;
	        String   b= str;   
	   String   Digital="0123456789ABCDEF";   
	        byte[]   bytes=new   byte[b.length()/2];   
	        int   temp;   
	        for(int   i=0;i<bytes.length;i++){   
	            temp=Digital.indexOf(b.substring(2*i,2*i+1))*16;   
	            temp+=Digital.indexOf(b.substring(2*i+1,2*i+2));   
	            bytes[i]=(byte)(temp&0xff);   
	        }   
	        return (new  String(bytes)); 
	}

   
}

 

