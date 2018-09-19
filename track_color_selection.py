#!/usr/bin/python


from imutils import contours
import imutils
import numpy as np
import cv2
import datetime
from matplotlib import pyplot as plt
import sys




if __name__ == "__main__":
	fileName='./image04.jpg'
	blurSize= 17
	args= str(sys)
	if len(sys.argv) ==2:
		fileName= sys.argv[1]
	elif (len(sys.argv) ==3):
		fileName= sys.argv[1]
		blurSize= int(sys.argv[2])		
		

	rgb= cv2.imread(fileName,1)
	hsv= cv2.cvtColor(rgb,cv2.COLOR_BGR2HSV)
	lower= np.array([80, 50, 50])
	upper= np.array([115, 255, 255])
	grayD= cv2.inRange(hsv, lower, upper)
	#(T, thresh) = cv2.threshold(grayD, 127, 255, cv2.THRESH_BINARY)
        
	grayM= cv2.medianBlur(grayD, blurSize);
	#lets threshold



	#filegray="grayM.png"
	#cv2.imwrite(filegray, grayM)
	plt.subplot(131),plt.imshow(imutils.opencv2matplotlib(rgb)),plt.title('Original')
	plt.subplot(132),plt.imshow(grayM, 'gray'),plt.title('Segmented')
	#cv2.imshow('original',rgb)
	#cv2.imshow('segmented', grayM)
	
	# find contours in the image
        _, contours0, hierarchy = cv2.findContours(grayM.copy(), cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
        print len(contours0)
	(contours0, boundbox)= contours.sort_contours(contours0, method="top-to-bottom")	
	rgbc= rgb.copy()
	for pic, contour in enumerate(contours0):
	        area = cv2.contourArea(contour) #funcion de opencv que obtiene los contornos
		print area
        	if(area< 500):
			(x,y),radius = cv2.minEnclosingCircle(contour)
			center = (int(x),int(y))
		 	radius = int(radius)
			print center
			
			cv2.circle(rgbc,center,radius,(0,255,0),5)
			if (pic < len(contours0) -1):
				(x1, y1), radius= cv2.minEnclosingCircle(contours0[pic+1])
				cv2.arrowedLine(rgbc, (int(x),int(y)), (int(x), int(y1)), (0,0,0), 3) 
				cv2.putText(rgbc, str(int(y1-y)), (int(x+20), int(y + (y1-y)/2)), 
						cv2.FONT_HERSHEY_PLAIN, 2, (0,0,0),4)
			


	plt.subplot(133),plt.imshow(imutils.opencv2matplotlib(rgbc)),plt.title('Detection')
	plt.show()

	#cv2.imshow('countours',rgbc)
	#cv2.waitKey()
	#cv2.destroyAllWindows()
	


	
	
