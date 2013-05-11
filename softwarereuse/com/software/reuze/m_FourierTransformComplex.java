package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac FFT.java
 *  Execution:    java FFT N
 *  Dependencies: Complex.java
 *
 *  Compute the FFT and inverse FFT of a length N complex sequence.
 *  Bare bones implementation that runs in O(N log N) time. Our goal
 *  is to optimize the clarity of the code, rather than performance.
 *
 *  Limitations
 *  -----------
 *   -  assumes N is a power of 2
 *
 *   -  not the most memory efficient algorithm (because it uses
 *      an object type for representing complex numbers and because
 *      it re-allocates memory for the subarray, instead of doing
 *      in-place or reusing a single temporary array)
 *  
 *************************************************************************/

public class m_FourierTransformComplex {
    private static m_Complex tmp=new m_Complex();
    private static m_Complex tmp2=new m_Complex();
    private static m_Complex tmp(m_Complex c) {tmp.x=c.x; tmp.y=c.y; return tmp;}
    private static m_Complex tmp2(m_Complex c) {tmp2.x=c.x; tmp2.y=c.y; return tmp2;}
    // compute the FFT of x[], assuming its length is a power of 2
    public static m_Complex[] fft(m_Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) return new m_Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }

        // fft of even terms
        m_Complex[] even = new m_Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        m_Complex[] q = fft(even);

        // fft of odd terms
        m_Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        m_Complex[] r = fft(odd);

        // combine
        m_Complex[] y = new m_Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            m_Complex wk = new m_Complex((float)Math.cos(kth), (float)Math.sin(kth));
            wk.multiply(r[k]);
            y[k]       = new m_Complex(q[k]).add(wk);
            y[k + N/2] = new m_Complex(q[k]).subtract(wk);
        }
        return y;
    }


    // compute the inverse FFT of x[], assuming its length is a power of 2
    public static m_Complex[] ifft(m_Complex[] x) {
        int N = x.length;
        m_Complex[] y = new m_Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i]=new m_Complex(x[i]).conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++) {
            y[i].multiply(1.0f / N);
        }

        return y;

    }

    // compute the circular convolution of x and y
    public static m_Complex[] cconvolve(m_Complex[] x, m_Complex[] y) {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.length != y.length) { throw new RuntimeException("Dimensions don't agree"); }

        int N = x.length;

        // compute FFT of each sequence
        m_Complex[] a = fft(x);
        m_Complex[] b = fft(y);

        // point-wise multiply
        m_Complex[] c = new m_Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = new m_Complex(a[i]).multiply(b[i]);
        }

        // compute inverse FFT
        return ifft(c);
    }


    // compute the linear convolution of x and y
    public static m_Complex[] convolve(m_Complex[] x, m_Complex[] y) {
        m_Complex ZERO = new m_Complex(0, 0);

        m_Complex[] a = new m_Complex[2*x.length];
        for (int i = 0;        i <   x.length; i++) a[i] = x[i];
        for (int i = x.length; i < 2*x.length; i++) a[i] = ZERO;

        m_Complex[] b = new m_Complex[2*y.length];
        for (int i = 0;        i <   y.length; i++) b[i] = y[i];
        for (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;

        return cconvolve(a, b);
    }

    // display an array of Complex numbers to standard output
    public static void show(m_Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }


   /*********************************************************************
    *  Test client and sample execution
    *
    *  % java FFT 4
    *  x
    *  -------------------
    *  -0.03480425839330703
    *  0.07910192950176387
    *  0.7233322451735928
    *  0.1659819820667019
    *
    *  y = fft(x)
    *  -------------------
    *  0.9336118983487516
    *  -0.7581365035668999 + 0.08688005256493803i
    *  0.44344407521182005
    *  -0.7581365035668999 - 0.08688005256493803i
    *
    *  z = ifft(y)
    *  -------------------
    *  -0.03480425839330703
    *  0.07910192950176387 + 2.6599344570851287E-18i
    *  0.7233322451735928
    *  0.1659819820667019 - 2.6599344570851287E-18i
    *
    *  c = cconvolve(x, x)
    *  -------------------
    *  0.5506798633981853
    *  0.23461407150576394 - 4.033186818023279E-18i
    *  -0.016542951108772352
    *  0.10288019294318276 + 4.033186818023279E-18i
    *
    *  d = convolve(x, x)
    *  -------------------
    *  0.001211336402308083 - 3.122502256758253E-17i
    *  -0.005506167987577068 - 5.058885073636224E-17i
    *  -0.044092969479563274 + 2.1934338938072244E-18i
    *  0.10288019294318276 - 3.6147323062478115E-17i
    *  0.5494685269958772 + 3.122502256758253E-17i
    *  0.240120239493341 + 4.655566391833896E-17i
    *  0.02755001837079092 - 2.1934338938072244E-18i
    *  4.01805098805014E-17i
    *
    *********************************************************************/

    public static void main(String[] args) { 
        int N;
        if (args.length==1) N = Integer.parseInt(args[0]); else N=4;
        m_Complex[] x = new m_Complex[N];
        m_Complex[] test = new m_Complex[N];

        // original data
        for (int i = 0; i < N; i++) {
        	x[i] = new m_Complex(i, 0);
            //x[i] = new Complex((float)(-2*Math.random() + 1), 0);
        }
        x[0].x=-0.03480425839330703f; x[1].x=0.07910192950176387f; x[2].x=0.7233322451735928f; x[3].x=0.1659819820667019f;
        show(x, "x");
        // FFT of original data
        m_Complex[] y = fft(x);
        show(y, "y = fft(x)");

        // take inverse FFT
        m_Complex[] z = ifft(y);
        show(z, "z = ifft(y)");

        // circular convolution of x with itself
        m_Complex[] c = cconvolve(x, x);
        show(c, "c = cconvolve(x, x)");

        // linear convolution of x with itself
        m_Complex[] d = convolve(x, x);
        show(d, "d = convolve(x, x)");
    }

}
