package reuze.awt;

import com.software.reuze.z_BufferedImage;

// *******************************************
// Sobel Edge Detection
// Credits to: http://www.pages.drexel.edu/~weg22/edge.html for description and source
// sobel = new SobelEdgeDetection();
// BufferedImage g_img = sobel.findEdgesAll(img, 90);
// g_img = sobel.noiseReduction(g_img, 1);
// drawImage(g_img, ......
// *******************************************
public class ib_EdgeDetectionSobel
{

  // Sobel Edge Detection standard, this applies the edge detection algorithm across the entire image and returns the edge image 
  public z_BufferedImage findEdgesAll(z_BufferedImage img, int tolerance)
  {
    z_BufferedImage buf = new z_BufferedImage( img.getWidth(), img.getHeight(), z_BufferedImage.TYPE_INT_ARGB );

    int GX[][] = new int[3][3];
    int GY[][] = new int[3][3];
    int sumRx = 0;
    int sumGx = 0;
    int sumBx = 0;
    int sumRy = 0;
    int sumGy = 0;
    int sumBy = 0;
    int finalSumR = 0;
    int finalSumG = 0;
    int finalSumB = 0;

    // 3x3 Sobel Mask for X
    GX[0][0] = -1; 
    GX[0][1] = 0; 
    GX[0][2] = 1;
    GX[1][0] = -2; 
    GX[1][1] = 0; 
    GX[1][2] = 2;
    GX[2][0] = -1; 
    GX[2][1] = 0; 
    GX[2][2] = 1;

    // 3x3 Sobel Mask for Y
    GY[0][0] =  1; 
    GY[0][1] =  2; 
    GY[0][2] =  1;
    GY[1][0] =  0; 
    GY[1][1] =  0; 
    GY[1][2] =  0;
    GY[2][0] = -1; 
    GY[2][1] = -2; 
    GY[2][2] = -1;   

    for(int y = 0; y < img.getHeight(); y++)
    {
      for(int x = 0; x < img.getWidth(); x++)
      {
        if(y==0 || y==img.getHeight()-1) {
        }
        else if( x==0 || x == img.getWidth()-1 ) {
        }
        else
        {

          // Convolve across the X axis and return gradient approximation
          for(int i = -1; i <= 1; i++)
            for(int j = -1; j <= 1; j++)
            {
              int col =  img.getRGB(x + i, y + j);
              float r = (col>>16)&0xff;
              float g = (col>>8)&0xff;
              float b = col&0xff;


              sumRx += r * GX[ i + 1][ j + 1];
              sumGx += g * GX[ i + 1][ j + 1];
              sumBx += b * GX[ i + 1][ j + 1];

            }

          // Convolve across the Y axis and return gradient approximation
          for(int i = -1; i <= 1; i++)
            for(int j = -1; j <= 1; j++)
            {
            	int col =  img.getRGB(x + i, y + j);
                float r = (col>>16)&0xff;
                float g = (col>>8)&0xff;
                float b = col&0xff;


              sumRy += r * GY[ i + 1][ j + 1];
              sumGy += g * GY[ i + 1][ j + 1];
              sumBy += b * GY[ i + 1][ j + 1];

            }

          finalSumR = Math.abs(sumRx) + Math.abs(sumRy);
          finalSumG = Math.abs(sumGx) + Math.abs(sumGy);
          finalSumB = Math.abs(sumBx) + Math.abs(sumBy);


          // I only want to return a black or a white value, here I determine the greyscale value,
          // and if it is above a tolerance, then set the colour to white

          float gray = (finalSumR + finalSumG + finalSumB) / 3;
          if(gray > tolerance)
          {
            finalSumR = 0xff000000;
            //finalSumG = 0;
            //finalSumB = 0;
          }
          else
          {
            finalSumR = 0xffffffff;
            //finalSumG = 255;
            //finalSumB = 255;
          }



          buf.setRGB(x,y, finalSumR);

          sumRx=0;
          sumGx=0;
          sumBx=0;
          sumRy=0;
          sumGy=0;
          sumBy=0;

        }

      }

    }

    return buf;
  }



  public z_BufferedImage noiseReduction(z_BufferedImage img, int kernel)
  {
    z_BufferedImage buf = new z_BufferedImage( img.getWidth(), img.getHeight(), z_BufferedImage.TYPE_INT_ARGB );

    for(int y = kernel; y < img.getHeight()-kernel; y++)
    {
      for(int x = kernel; x < img.getWidth()-kernel; x++)
      {

        int sumR = 0;
        int sumG = 0;
        int sumB = 0;

        // Convolute across the image, averages out the pixels to remove noise
        for(int i = -kernel; i <= kernel; i++)
        {
          for(int j = -kernel; j <= kernel; j++)
          {
        	  int col =  img.getRGB(x + i, y + j);
              float r = (col>>16)&0xff;
              float g = (col>>8)&0xff;
              float b = col&0xff;

            if(r==255) sumR++;
            if(g==255) sumG++;
            if(b==255) sumB++;
          }
        }

        int halfKernel = (((kernel*2)+1) * ((kernel*2)+1)) / 2 ;

        if(sumR > halfKernel  ) sumR=255; 
        else sumR= 0;
        if(sumG > halfKernel  ) sumG=255; 
        else sumG= 0;
        if(sumB > halfKernel  ) sumB=255; 
        else sumB= 0;


        buf.setRGB(x, y, 0xff000000+(sumR<<16)+(sumG<<8)+sumB);

      }


    }

    return buf;
  }


}