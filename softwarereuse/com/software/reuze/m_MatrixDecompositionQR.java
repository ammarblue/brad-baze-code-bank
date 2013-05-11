package com.software.reuze;
//package com.example.android.notepad2;
/** QR Decomposition.

<P>

   For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n

   orthogonal matrix Q and an n-by-n upper triangular matrix R so that

   A = Q*R.

<P>

   The QR decompostion always exists, even if the matrix does not have

   full rank, so the constructor will never fail.  The primary use of the

   QR decomposition is in the least squares solution of nonsquare systems

   of simultaneous linear equations.  This will fail if isFullRank()

   returns false.

*/



public class m_MatrixDecompositionQR extends m_Matrix implements java.io.Serializable {



/* ------------------------

   Class variables

 * ------------------------ */



   /** Array for internal storage of decomposition.

   @serial internal array storage.

   */

   //private double[][] QR;



   /** Row and column dimensions.

   @serial column dimension.

   @serial row dimension.

   */

   //private int m, n;



   /** Array for internal storage of diagonal of R.

   @serial diagonal of R.

   */

   private double[] Rdiag;



/* ------------------------

   Constructor

 * ------------------------ */



   /** QR Decomposition, computed by Householder reflections.

   @param A    Rectangular matrix

   @return     Structure to access R and the Householder vectors and compute Q.

   */



   public m_MatrixDecompositionQR (m_Matrix X) {

      // Initialize.

      //QR = A.getArrayCopy();

      //m = A.getRowDimension();

      //n = A.getColumnDimension();
      super(X.A);
      Rdiag = new double[n];



      // Main loop.

      for (int k = 0; k < n; k++) {

         // Compute 2-norm of k-th column without under/overflow.

         double nrm = 0;

         for (int i = k; i < m; i++) {

            nrm = m_Matrix.hypot(nrm,A[i][k]);

         }



         if (nrm != 0.0) {

            // Form k-th Householder vector.

            if (A[k][k] < 0) {

               nrm = -nrm;

            }

            for (int i = k; i < m; i++) {

               A[i][k] /= nrm;

            }

            A[k][k] += 1.0;



            // Apply transformation to remaining columns.

            for (int j = k+1; j < n; j++) {

               double s = 0.0; 

               for (int i = k; i < m; i++) {

                  s += A[i][k]*A[i][j];

               }

               s = -s/A[k][k];

               for (int i = k; i < m; i++) {

                  A[i][j] += s*A[i][k];

               }

            }

         }

         Rdiag[k] = -nrm;

      }

   }



/* ------------------------

   Public Methods

 * ------------------------ */



   /** Is the matrix full rank?

   @return     true if R, and hence A, has full rank.

   */



   public boolean isFullRank () {

      for (int j = 0; j < n; j++) {

         if (Rdiag[j] == 0)

            return false;

      }

      return true;

   }



   /** Return the Householder vectors

   @return     Lower trapezoidal matrix whose columns define the reflections

   */



   public m_Matrix getH () {

      m_Matrix X = new m_Matrix(m,n);

      double[][] H = X.getArray();

      for (int i = 0; i < m; i++) {

         for (int j = 0; j < n; j++) {

            if (i >= j) {

               H[i][j] = A[i][j];

            } else {

               H[i][j] = 0.0;

            }

         }

      }

      return X;

   }



   /** Return the upper triangular factor

   @return     R

   */



   public m_Matrix getR () {

      m_Matrix X = new m_Matrix(n,n);

      double[][] R = X.getArray();

      for (int i = 0; i < n; i++) {

         for (int j = 0; j < n; j++) {

            if (i < j) {

               R[i][j] = A[i][j];

            } else if (i == j) {

               R[i][j] = Rdiag[i];

            } else {

               R[i][j] = 0.0;

            }

         }

      }

      return X;

   }



   /** Generate and return the (economy-sized) orthogonal factor

   @return     Q

   */



   public m_Matrix getQ () {

      m_Matrix X = new m_Matrix(m,n);

      double[][] Q = X.getArray();

      for (int k = n-1; k >= 0; k--) {

         for (int i = 0; i < m; i++) {

            Q[i][k] = 0.0;

         }

         Q[k][k] = 1.0;

         for (int j = k; j < n; j++) {

            if (A[k][k] != 0) {

               double s = 0.0;

               for (int i = k; i < m; i++) {

                  s += A[i][k]*Q[i][j];

               }

               s = -s/A[k][k];

               for (int i = k; i < m; i++) {

                  Q[i][j] += s*A[i][k];

               }

            }

         }

      }

      return X;

   }



   /** Least squares solution of A*X = B

   @param B    A Matrix with as many rows as A and any number of columns.

   @return     X that minimizes the two norm of Q*R*X-B.

   @exception  IllegalArgumentException  Matrix row dimensions must agree.

   @exception  RuntimeException  Matrix is rank deficient.

   */



   public m_Matrix solve (m_Matrix B) {

      if (B.getRowDimension() != m) {

         throw new IllegalArgumentException("Matrix row dimensions must agree.");

      }

      if (!this.isFullRank()) {

         throw new RuntimeException("Matrix is rank deficient.");

      }

      

      // Copy right hand side

      int nx = B.getColumnDimension();

      double[][] X = B.getArrayCopy();



      // Compute Y = transpose(Q)*B

      for (int k = 0; k < n; k++) {

         for (int j = 0; j < nx; j++) {

            double s = 0.0; 

            for (int i = k; i < m; i++) {

               s += A[i][k]*X[i][j];

            }

            s = -s/A[k][k];

            for (int i = k; i < m; i++) {

               X[i][j] += s*A[i][k];

            }

         }

      }

      // Solve R*X = Y;

      for (int k = n-1; k >= 0; k--) {

         for (int j = 0; j < nx; j++) {

            X[k][j] /= Rdiag[k];

         }

         for (int i = 0; i < k; i++) {

            for (int j = 0; j < nx; j++) {

               X[i][j] -= X[k][j]*A[i][k];

            }

         }

      }

      return (new m_Matrix(X,n,nx).getMatrix(0,n-1,0,nx-1));

   }

}

