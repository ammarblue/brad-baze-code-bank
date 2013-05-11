package com.software.reuze;
//package com.example.android.notepad2;

   /** LU Decomposition.

   <P>

   For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n

   unit lower triangular matrix L, an n-by-n upper triangular matrix U,

   and a permutation vector piv of length m so that A(piv,:) = L*U.

   If m < n, then L is m-by-m and U is m-by-n.

   <P>

   The LU decomposition with pivoting always exists, even if the matrix is

   singular, so the constructor will never fail.  The primary use of the

   LU decomposition is in the solution of square systems of simultaneous

   linear equations.  This will fail if isNonsingular() returns false.

   */



public class m_MatrixDecompositionLU extends m_Matrix implements java.io.Serializable {



/* ------------------------

   Class variables

 * ------------------------ */



   /** Array for internal storage of decomposition.

   @serial internal array storage.

   */

   //private double[][] A;



   /** Row and column dimensions, and pivot sign.

   @serial column dimension.

   @serial row dimension.

   @serial pivot sign.

   */

   private int /*m, n,*/ pivsign; 



   /** Internal storage of pivot vector.

   @serial pivot vector.

   */

   private int[] piv;



/* ------------------------

   Constructor

 * ------------------------ */



   /** LU Decomposition

   @param  A   Rectangular matrix

   @return     Structure to access L, U and piv.

   */



   public m_MatrixDecompositionLU (m_Matrix X) {



   // Use a "left-looking", dot-product, Crout/Doolittle algorithm.

      super(X.A);

      //this.A = X.getArrayCopy();

      //m = X.getRowDimension();

      //n = X.getColumnDimension();

      piv = new int[m];

      for (int i = 0; i < m; i++) {

         piv[i] = i;

      }

      pivsign = 1;

      double[] LUrowi;

      double[] LUcolj = new double[m];



      // Outer loop.



      for (int j = 0; j < n; j++) {



         // Make a copy of the j-th column to localize references.



         for (int i = 0; i < m; i++) {

            LUcolj[i] = A[i][j];

         }



         // Apply previous transformations.



         for (int i = 0; i < m; i++) {

            LUrowi = A[i];



            // Most of the time is spent in the following dot product.



            int kmax = Math.min(i,j);

            double s = 0.0;

            for (int k = 0; k < kmax; k++) {

               s += LUrowi[k]*LUcolj[k];

            }



            LUrowi[j] = LUcolj[i] -= s;

         }

   

         // Find pivot and exchange if necessary.



         int p = j;

         for (int i = j+1; i < m; i++) {

            if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p])) {

               p = i;

            }

         }

         if (p != j) {

            for (int k = 0; k < n; k++) {

               double t = A[p][k]; A[p][k] = A[j][k]; A[j][k] = t;

            }

            int k = piv[p]; piv[p] = piv[j]; piv[j] = k;

            pivsign = -pivsign;

         }



         // Compute multipliers.

         

         if (j < m & A[j][j] != 0.0) {

            for (int i = j+1; i < m; i++) {

               A[i][j] /= A[j][j];

            }

         }

      }

   }



/* ------------------------

   Temporary, experimental code.

   ------------------------ *\



   \** LU Decomposition, computed by Gaussian elimination.

   <P>

   This constructor computes L and U with the "daxpy"-based elimination

   algorithm used in LINPACK and MATLAB.  In Java, we suspect the dot-product,

   Crout algorithm will be faster.  We have temporarily included this

   constructor until timing experiments confirm this suspicion.

   <P>

   @param  A             Rectangular matrix

   @param  linpackflag   Use Gaussian elimination.  Actual value ignored.

   @return               Structure to access L, U and piv.

   *\



   public LUDecomposition (Matrix A, int linpackflag) {

      // Initialize.

      LU = A.getArrayCopy();

      m = A.getRowDimension();

      n = A.getColumnDimension();

      piv = new int[m];

      for (int i = 0; i < m; i++) {

         piv[i] = i;

      }

      pivsign = 1;

      // Main loop.

      for (int k = 0; k < n; k++) {

         // Find pivot.

         int p = k;

         for (int i = k+1; i < m; i++) {

            if (Math.abs(A[i][k]) > Math.abs(A[p][k])) {

               p = i;

            }

         }

         // Exchange if necessary.

         if (p != k) {

            for (int j = 0; j < n; j++) {

               double t = A[p][j]; A[p][j] = A[k][j]; A[k][j] = t;

            }

            int t = piv[p]; piv[p] = piv[k]; piv[k] = t;

            pivsign = -pivsign;

         }

         // Compute multipliers and eliminate k-th column.

         if (A[k][k] != 0.0) {

            for (int i = k+1; i < m; i++) {

               A[i][k] /= A[k][k];

               for (int j = k+1; j < n; j++) {

                  A[i][j] -= A[i][k]*A[k][j];

               }

            }

         }

      }

   }



\* ------------------------

   End of temporary code.

 * ------------------------ */



/* ------------------------

   Public Methods

 * ------------------------ */



   /** Is the matrix nonsingular?

   @return     true if U, and hence A, is nonsingular.

   */



   public boolean isNonsingular () {

      for (int j = 0; j < n; j++) {

         if (A[j][j] == 0)

            return false;

      }

      return true;

   }



   /** Return lower triangular factor

   @return     L

   */



   public m_Matrix getL () {

      m_Matrix X = new m_Matrix(m,n);

      double[][] L = X.getArray();

      for (int i = 0; i < m; i++) {

         for (int j = 0; j < n; j++) {

            if (i > j) {

               L[i][j] = A[i][j];

            } else if (i == j) {

               L[i][j] = 1.0;

            } else {

               L[i][j] = 0.0;

            }

         }

      }

      return X;

   }



   /** Return upper triangular factor

   @return     U

   */



   public m_Matrix getU () {

      m_Matrix X = new m_Matrix(n,n);

      double[][] U = X.getArray();

      for (int i = 0; i < n; i++) {

         for (int j = 0; j < n; j++) {

            if (i <= j) {

               U[i][j] = A[i][j];

            } else {

               U[i][j] = 0.0;

            }

         }

      }

      return X;

   }



   /** Return pivot permutation vector

   @return     piv

   */



   public int[] getPivot () {

      int[] p = new int[m];

      for (int i = 0; i < m; i++) {

         p[i] = piv[i];

      }

      return p;

   }



   /** Return pivot permutation vector as a one-dimensional double array

   @return     (double) piv

   */



   public double[] getDoublePivot () {

      double[] vals = new double[m];

      for (int i = 0; i < m; i++) {

         vals[i] = (double) piv[i];

      }

      return vals;

   }



   /** Determinant

   @return     det(A)

   @exception  IllegalArgumentException  Matrix must be square

   */



   public double det () {

      if (m != n) {

         throw new IllegalArgumentException("Matrix must be square.");

      }

      double d = (double) pivsign;

      for (int j = 0; j < n; j++) {

         d *= A[j][j];

      }

      return d;

   }



   /** Solve A*X = B

   @param  B   A Matrix with as many rows as A and any number of columns.

   @return     X so that L*U*X = B(piv,:)

   @exception  IllegalArgumentException Matrix row dimensions must agree.

   @exception  RuntimeException  Matrix is singular.

   */



   public m_Matrix solve (m_Matrix B) {

      if (B.getRowDimension() != m) {

         throw new IllegalArgumentException("Matrix row dimensions must agree.");

      }

      if (!this.isNonsingular()) {

         throw new RuntimeException("Matrix is singular.");

      }



      // Copy right hand side with pivoting

      int nx = B.getColumnDimension();

      m_Matrix Xmat = B.getMatrix(piv,0,nx-1);

      double[][] X = Xmat.getArray();



      // Solve L*Y = B(piv,:)

      for (int k = 0; k < n; k++) {

         for (int i = k+1; i < n; i++) {

            for (int j = 0; j < nx; j++) {

               X[i][j] -= X[k][j]*A[i][k];

            }

         }

      }

      // Solve U*X = Y;

      for (int k = n-1; k >= 0; k--) {

         for (int j = 0; j < nx; j++) {

            X[k][j] /= A[k][k];

         }

         for (int i = 0; i < k; i++) {

            for (int j = 0; j < nx; j++) {

               X[i][j] -= X[k][j]*A[i][k];

            }

         }

      }

      return Xmat;

   }

}

