#!/usr/bin/python


from imutils import contours
import imutils
import numpy as np
import cv2
import datetime
from matplotlib import pyplot as plt


if __name__ == "__main__":
   cap= cv2.VideoCapture('videosemente.mp4')
   
   while (cap.isOpened()):
	
        ret, rgb= cap.read()
	s= rgb.shape
	rgb= rgb[0:171, 0:s[1]]
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

	(T, thresh) = cv2.threshold(grayD, 100, 255, cv2.THRESH_BINARY)
        
	grayM= cv2.medianBlur(thresh, 9);

	# find contours in the image
        _, contours0, hierarchy = cv2.findContours(grayM.copy(), cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
        print len(contours0)
	(contours0, boundbox)= contours.sort_contours(contours0, method="right-to-left")	
	rgbc= rgb.copy()
	for pic, contour in enumerate(contours0):
	        area = cv2.contourArea(contour) #funcion de opencv que obtiene los contornos
		print area
        	if(area< 500) and (area > 30):
			(x,y),radius = cv2.minEnclosingCircle(contour)
			center = (int(x),int(y))
		 	radius = int(radius)
			print center
			cv2.putText(rgbc, str(int(pic)), (int(x),int(y)), 
			cv2.FONT_HERSHEY_PLAIN, 2, (0,0,0),2)
			
			#cv2.circle(rgbc,center,radius,(0,255,0),5)
			#if (pic < len(contours0) -1):
			#	(x1, y1), radius= cv2.minEnclosingCircle(contours0[pic+1])
			#	cv2.arrowedLine(rgbc, (int(x),int(y)), (int(x1), int(y)), (0,0,0), 3) 
			



	cv2.imshow('countours',rgbc)
	cv2.imshow('segmentation',grayM)
	if cv2.waitKey(1) & 0xFF == ord('q'):
	   break
	

   cap.release()
   cv2.destroyAllWindows()
	
	
