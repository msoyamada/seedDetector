#!/usr/bin/python

import numpy as np
import cv2
import datetime
import time
#just for desktop execution
from matplotlib import pyplot as plt

if __name__ == "__main__":
	rgb= cv2.imread('./image01.jpeg')

	s= rgb.shape
	gray= np.ndarray(shape=(s[0], s[1]), dtype=np.uint8)

	print gray.shape
	t0 = time.clock()
	#for i in range (0, s[0]-1):
	#	for j in range (0,s[1]-1):        
	of = [0.1,0.1,0.89]
	m = np.array(of).reshape((1,3))
	gray = cv2.transform(rgb,m) 
		#	r= 0.1*rgb[i,j, 0] + 0.1*rgb[i,j, 1] + 0.9*rgb[i,j, 2]
			
	#		if (r > 255) :
	#			r=255			
	#		gray[i,j]= r;
			#print r
	print(time.clock() - t0)

	#noise reduction
	t0 = time.clock()
	(T, thresh) = cv2.threshold(gray, 220, 255, cv2.THRESH_BINARY)
	print(time.clock() - t0)
	
	t0 = time.clock()
	grayM= cv2.medianBlur(thresh, 5);
	print(time.clock() - t0)
	#lets threshold
	t0 = time.clock()
	cont = 0
	for i in range (10, s[0]-10):
		for j in range (10,s[1]-10):
			if grayM[i,j] == 0: 
				cont = cont+1
	print(time.clock() - t0)
	
	print cont
	filegray= "./grayM.png"
	cv2.imwrite(filegray, grayM)
	plt.subplot(131),plt.imshow(cv2.cvtColor(rgb, cv2.COLOR_BGR2RGB)),plt.title('RGB')
	plt.subplot(132),plt.imshow(gray,'gray'),plt.title('CUSTOM_GRAY')
	plt.subplot(133),plt.imshow(grayM,'gray'),plt.title('MedianBlur')
#	plt.subplot(122),plt.imshow(thresh,'gray'),plt.title('MedianBlur')
#	plt.subplot(143),plt.imshow(grayD,'gray'),plt.title('DILATE')
#	plt.subplot(144),plt.imshow(grayN,'gray'),plt.title('Noising')
	plt.show()
	


	
	



