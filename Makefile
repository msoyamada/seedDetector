#CFLAGS = -I/usr/local/include/opencv -I/usr/local/include/opencv2 -std=c++11
#LIBS = -L/usr/local/lib/ -lopencv_core -lopencv_imgproc -lopencv_imgcodecs -lopencv_highgui -lopencv_ml -lopencv_video -lopencv_features2d -lopencv_calib3d -lopencv_objdetect -lopencv_contrib -lopencv_legacy -lopencv_stitching
CFLAGS = $(shell pkg-config  --cflags opencv) -std=c++11
LIBS = $(shell pkg-config  --libs opencv) 


% : %.cpp
	g++ $(CFLAGS) -o binary  $< $(LIBS)
