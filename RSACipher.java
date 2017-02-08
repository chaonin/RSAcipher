import java.math.BigInteger;
import java.util.Random;
import java.io.*;
/*
 * @author anyguo
 */
public class RSACipher{
	
    public static int pq_length=335;	// 335
	public static BigInteger m_p;
	public static BigInteger m_q;
	public static BigInteger m_e;
	public static BigInteger m_d;
	public static BigInteger temp;
	public static BigInteger m_n;	
	public static int text_length;
    public static String file_en="";
    public static String file_de="";
    
    /** Creates a new instance of Main */
    public static void main(String[] param) {   
	    /*   	    
	    /////////////////////////////////////////////////////////////////////////////////
		//方法1:重新计算p,q,n,tmp,d,e
		//1.计算p和q   
		get_pq_prime();         
		//2.计算n = p*q  
		m_n=m_p.multiply(m_q);
		//3.计算p-1
		BigInteger p_1=m_p.subtract(new BigInteger("1"));
		//4.计算q-1
		BigInteger q_1=m_q.subtract(new BigInteger("1"));
		//5.计算（p-1）×（q-1）
		temp =p_1.multiply(q_1); 
		//6.随机生成解密密钥d
		get_m_d();
		//7.根据d计算加密密钥e(e为d的模（p-1）*(q-1)逆)
		get_m_e();
		*/
    	/////////////////////////////////////////////////////////////////////////////////

    	/////////////////////////////////////////////////////////////////////////////////
    	//方法2：已经计算好几组p,q,n,tmp,d,e,直接拿来用:
	    getPrime gp=new getPrime();
	    try{
	    	gp.getprime();	
	    }
	    catch(Exception e){}
	    //到目前为止，第五组是比较好的!
		int i=5; 
		m_p=gp.m_p[i];
		m_q=gp.m_q[i];
		m_n=gp.m_n[i];
		temp=gp.tmp[i];
		m_d=gp.m_d[i];
		m_e=gp.m_e[i];
    	/////////////////////////////////////////////////////////////////////////////////
		
	    System.out.println("m_p="+m_p);
	    System.out.println("m_q="+m_q);               
	    System.out.println("m_n="+m_n); 
	    System.out.println("m_d="+m_d);
	    System.out.println("m_e="+m_e);
	    try{
			readfile_and_crypt("plain.cpp.pt");
			System.out.println("encryption done!");
			readfile_and_decrypt();
			System.out.println("decryption done!");
		}
		catch(Exception e){}
    }

    public static void readfile_and_crypt(String filename)throws Exception{
    	//必须以0初始化
    	BigInteger encrypted_text=new BigInteger("0");
		BigInteger plain_text	 =new BigInteger("0");	
		//对于670位左右的m_n,读80字节的文件；差不多满足p<670,p为明文长度
		byte []buffer=new byte[80];	
		byte []buffer_read;	
		int size=80,count;
		byte []encrypted_text_byte;

		FileInputStream	filein=new FileInputStream(filename);
		file_en=filename+"_en.txt";
		RandomAccessFile fileout=new RandomAccessFile(file_en,"rw");
		while((count=filein.read(buffer,0,size))!=-1)
		{
			buffer_read=new byte[count];
			for(int i=0;i<count;i++)
				buffer_read[i]=buffer[i];	
			plain_text=new BigInteger(buffer_read);			
			encrypted_text=plain_text.modPow(m_e,m_n);		
			encrypted_text_byte=encrypted_text.toByteArray();
			text_length=encrypted_text_byte.length;
			fileout.write(encrypted_text_byte,0,text_length);
		}
		filein.close();
		fileout.close();
	}
    
	public static void readfile_and_decrypt()throws Exception{
		//加密时是84字节,而且经过toByteArray的方法，大小可能发生变化,所以这里用90来预防.
		byte [] buffer2=new byte[90];
		byte [] buffer3;	
		byte [] buffer4;
		FileInputStream filein=new FileInputStream(file_en);
        int k=file_en.indexOf(".");
        int h=file_en.lastIndexOf("_");
        String file_en_begin=file_en.substring(0,k);
        String file_en_end=file_en.substring(k+1,h);
        file_de=file_en_begin+"_de.";
        file_de=file_de+file_en_end;
		RandomAccessFile fileout=new RandomAccessFile(file_de,"rw");
		int count,text_length_de;
		BigInteger encry_read,encry_read_decry;
		while((count=filein.read(buffer2,0,text_length))!=-1)
		{
			buffer3=new byte[count];	
			for(int i=0;i<count;i++)
				buffer3[i]=buffer2[i];
			encry_read=new BigInteger(buffer3);
			encry_read_decry=encry_read.modPow(m_d,m_n);
			buffer4=encry_read_decry.toByteArray();
			text_length_de=buffer4.length;
			fileout.write(buffer4,0,text_length_de);
		}
		filein.close();
		fileout.close();
	}
	
	public static void get_m_d()
	{
		//必须小于temp
		m_d=new BigInteger(pq_length-5,5,new Random());
	}
	
	public static void get_m_e()
	{
		m_e=m_d.modInverse(temp);
	}
	
	public static void get_pq_prime()
	{
		System.out.println("随机选择两个大素数(100位左右):");	
		//以1－2^20的概率产生素数
		m_p=new BigInteger(pq_length,20,new Random());		
		m_q=new BigInteger(pq_length,20,new Random());
		boolean is_p_prime=false;
		boolean is_q_prime=false;
		int i=0,count=50;
		//每次如果产生的不是素数，提高产生素数的概率重新生成
		while(!is_p_prime&&i<count)	 	 
		{
			i++;
			is_p_prime=m_p.isProbablePrime(count--);
			m_p=new BigInteger(pq_length,20+i,new Random());
		}
		i=0;count=50;
		while(!is_q_prime&&i<count)
		{
			i++;
			is_q_prime=m_q.isProbablePrime(count--);
			m_q=new BigInteger(pq_length,20+i,new Random());
		}
	}
}
