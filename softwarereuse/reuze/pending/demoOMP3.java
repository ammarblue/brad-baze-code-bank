package reuze.pending;

import com.software.reuze.m_Complex;

class demoOMP3 {
static int MandelbrotCalculate(m_Complex c, int maxiter)
{
    // iterates z = z*z + c until |z| >= 2 or maxiter is reached,
    // returns the number of iterations.
    m_Complex z = c;
    int n=0;
    for(; n<maxiter; ++n)
    {
        if( z.abs() >= 2.0) break;
        z.multiply(z).add(c);
    }
    return n;
}
public static void main(String args[])
{
     int width = 178, height = 144, num_pixels = width*height;
    
     m_Complex center=new m_Complex(-.2f, 0), span=new m_Complex(2.7f, -(4/3.0f)*2.7f*height/width);
     m_Complex begin = new m_Complex(center), end = new m_Complex(center);
     center.set(span).divide(2);  begin.subtract(center);  end.add(center);
     int maxiter = 100000;
  
  //#pragma omp parallel for ordered schedule(dynamic)
    for(int pix=0; pix<num_pixels; ++pix)
    {
        int x = pix%width, y = pix/width;
        
        m_Complex c = new m_Complex(begin).add(new m_Complex(x * span.getReal() / (width +1.0f),
                                    y * span.getImaginary() / (height+1.0f)));
        
        int n = MandelbrotCalculate(c, maxiter);
        if(n == maxiter) n = 0;
        
      //#pragma omp ordered
        {
          char cc = ' ';
          if(n > 0)
          {
              String charset = ".,c8M@jawrpogOQEPGJ";
              cc = charset.charAt(n % (charset.length()));
          }
          System.out.print(cc);
          if(x+1 == width) System.out.println("|");
        }
    }
}
}