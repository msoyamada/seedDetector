#!/usr/bin/python




import numpy as np
import cv2
import datetime
from matplotlib import pyplot as plt





if __name__ == "__main__":
	rgb= cv2.imread('./image04.jpg')

	s= rgb.shape
	gray= np.ndarray(shape=(s[0], s[1]), dtype=np.uint8)
	print gray.shape
	for i in range (0, s[0]-1):
		for j in range (0,s[1]):        
			r= 0*rgb[i,j, 0] + 0*rgb[i,j, 1] +1*rgb[i,j, 2]
			
			if (r > 255) :
				 r=255
			
			gray[i,j]= r			
			#print r

        #lets try to clean the image, a very naive dilate algorithm
#	for i in range (0, s[0]-1):
#		for j in range (0,s[1]):
#			 if gray[i,j] < 255 and gray[i,j] > 200:
#		             if (i > 1) and (j < s[1]-1) and (i < s[0]-1) and (j > 1):
#		                 if (gray[i-1, j] == 255) or (gray[i+1, j] == 255) or (gray[i, j-1] == 255) or (gray[i, j+1] ==255):
#		                     gray[i+1, j] =255;
#		                     gray[i-1, j] =255;
#		                     gray[i, j-1] =255;
#		                     gray[i, j+1] =255;
#		                     gray[i, j] =255;
#			 if gray[i,j] < 20 :
#		             if (i > 1) and (j < s[1]-1) and (i < s[0]-1) and (j > 1):
#		                 if (gray[i-1, j] == 0) or (gray[i+1, j] == 0) or (gray[i, j-1] == 0) or (gray[i, j+1] ==0):
#		                     gray[i+1, j] =0;
#		                     gray[i-1, j] =0;
#		                     gray[i, j-1] =0;
#		                     gray[i, j+1] =0;
#		                     gray[i, j] =0;               

	#dilate            
        kernel= np.zeros((3,3),np.uint8)
	grayD=cv2.dilate(gray, kernel, 8)

	#noise reduction
#	grayN= cv2.fastNlMeansDenoising(gray)

	(T, thresh) = cv2.threshold(grayD, 127, 255, cv2.THRESH_BINARY)
        
	grayM= cv2.medianBlur(thresh, 17);
	#lets threshold



	filegray= "gray.png"
	cv2.imwrite(filegray, gray)
	#filegray="grayM.png"
	#cv2.imwrite(filegray, grayM)
	plt.subplot(131),plt.imshow(rgb),plt.title('original')
	plt.subplot(132),plt.imshow(thresh, 'gray'),plt.title('threshold')
	plt.subplot(133),plt.imshow(grayM, 'gray'),plt.title('MedianBlur')
	
	plt.show()
	
	


