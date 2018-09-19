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
	#cv2.imwrite('terra.png', rgb)
	s= rgb.shape
	#rgb= rgb[150:232, 0:s[1]]
	#s= rgb.shape
	#print s
	hsv= cv2.cvtColor(rgb,cv2.COLOR_BGR2HSV)
	lower= np.array([80, 50, 50])
	upper= np.array([115, 255, 255])
#	lower= np.array([16, 10, 10])
#	upper= np.array([60, 255, 255])

	grayD= cv2.inRange(hsv, lower, upper)

	

        
	grayM= cv2.medianBlur(grayD, 11);

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
			
			cv2.circle(rgbc,center,radius,(0,255,0),5)
			if (pic < len(contours0) -1):
				(x1, y1), radius= cv2.minEnclosingCircle(contours0[pic+1])
				cv2.arrowedLine(rgbc, (int(x),int(y)), (int(x1), int(y)), (0,0,0), 3) 
				cv2.putText(rgbc, str(int(x-x1)*10)+'mm', (int(x1 + 10),int(y-10)), 
					cv2.FONT_HERSHEY_PLAIN, 1, (0,0,0),1)
			
			



	cv2.imshow('countours',rgbc)
	cv2.imshow('segmentation',grayM)
	if cv2.waitKey(3000) & 0xFF == ord('q'):
	   break
	

   cap.release()
   cv2.destroyAllWindows()
	
	
