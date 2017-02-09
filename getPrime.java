import java.math.BigInteger;
import java.util.Random;
import java.io.*;

/**
 * @author anyguo
 */
public class getPrime {
    public static BigInteger[] m_p;
    public static BigInteger[] m_q;
    public static BigInteger[] m_e;
    public static BigInteger[] m_d;
    public static BigInteger[] phi_n;
    public static BigInteger[] m_n;
    
    public getPrime() {
    }
    
    public static void getprime()throws Exception
    {
        m_p = new BigInteger[12];
        m_q = new BigInteger[12];
        m_e = new BigInteger[12];
        m_d = new BigInteger[12];
        phi_n = new BigInteger[12];
        m_n = new BigInteger[12];
        BufferedReader filein=new BufferedReader(new InputStreamReader(new FileInputStream("prime.txt")));
        String line="";
        for(int i=0;i<12;i++)
            for(int j=0;j<6;j++)
            {
                line=(String)filein.readLine();
                if	(j==0)    m_p[i]=new BigInteger(line);
                else if	(j==1)m_q[i]=new BigInteger(line);
                else if (j==2)m_n[i]=new BigInteger(line);
                else if (j==3)phi_n[i]=new BigInteger(line);
                else if (j==4)m_d[i]=new BigInteger(line);
                else	      m_e[i]=new BigInteger(line);
            }
    }
    
}
