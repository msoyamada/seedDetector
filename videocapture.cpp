#include "opencv2/opencv.hpp"
#include "opencv2/videoio.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/highgui.hpp"


using namespace cv;
using namespace std;

bool SortRightToLeft(std::vector<cv::Point> a, std::vector<cv::Point> b) 
{
    return a[0].x > b[0].x;
}

/*bool SortbyXaxis(const Point &a, const Point &b) 

    return a.x < b.x;
}
bool SortbyYaxis(const Point &a, const Point &b) 
{
    return a.y < b.y;
}
*/
void processFrame(Mat  frame){
    Mat hsv;
    Mat grayD;
    cvtColor(frame, hsv, COLOR_BGR2HSV);
    Scalar lower= Scalar(80, 50, 50);
    Scalar upper= Scalar(115, 255, 255);
    inRange(hsv, lower, upper, grayD);
    imshow("hsv",grayD);
    waitKey(100);
    medianBlur(grayD, grayD, 11);

    vector<vector<Point> > contours;
    vector<Vec4i> hierarchy;

    findContours( grayD, contours, hierarchy, RETR_LIST, CHAIN_APPROX_SIMPLE, Point(0, 0) );
    //sort contours

    sort( contours.begin(), contours.end(), SortRightToLeft);


    /// Draw contours
    for( int i = 0; i< contours.size(); i++ )
     {
       Scalar color = Scalar( 255, 0, 0 );
       //drawContours( drawing, contours, i, color, 2, 8, hierarchy, 0, Point() );
       double area= contourArea(contours[i]);
       if ((area < 500) && (area >20)) {
            Point2f center;
            float radius;
            minEnclosingCircle(contours[i], center, radius);
            cout << center << endl ;
            circle(frame,center,radius,(0,255,0),5);
            if (i < contours.size()-1)
            {
                Point2f Ncenter;
                float Nradius;
                minEnclosingCircle(contours[i+1], Ncenter, Nradius);

                arrowedLine(frame , center, Point2f(Ncenter.x, center.y), Scalar(0,0,0), 3) ;
                String s;
                s = to_string((int)(Ncenter.x-center.x)*10);
                s += " mm";

                putText(frame, s.c_str() , Ncenter, FONT_HERSHEY_PLAIN, 1, Scalar(0,0,0),1);

            }


       }

     }
     imshow("frame", frame);
    waitKey(100);

        //     area = cv2.contourArea(contour) #funcion de opencv que obtiene los contornos
        // print area
        //     if(area< 500) and (area > 20):
        //     (x,y),radius = cv2.minEnclosingCircle(contour)
        //     center = (int(x),int(y))
        //     radius = int(radius)
        //     print center
            
        //     cv2.circle(rgbc,center,radius,(0,255,0),5)
        //     if (pic < len(contours0) -1):
        //         (x1, y1), radius= cv2.minEnclosingCircle(contours0[pic+1])
        //         cv2.arrowedLine(rgbc, (int(x),int(y)), (int(x1), int(y)), (0,0,0), 3) 
        //         cv2.putText(rgbc, str(int(x-x1)*10)+'mm', (int(x1 + 10),int(y-10)), 
        //             cv2.FONT_HERSHEY_PLAIN, 1, (0,0,0),1)
            
    





}



int main(int, char**)
{
  cout << "OpenCV version : " << CV_VERSION << endl;
  cout << "Major version : " << CV_MAJOR_VERSION << endl;
  cout << "Minor version : " << CV_MINOR_VERSION << endl;
  cout << "Subminor version : " << CV_SUBMINOR_VERSION << endl;

    VideoCapture cap("videosemente.mp4"); // open the default camera
    if(!cap.isOpened())  // check if we succeeded
        return -1;


    for(;;)
    {
        Mat frame;
        cap >> frame; // get a new frame from camera
        processFrame(frame);
/*        cvtColor(frame, edges, COLOR_BGR2GRAY);
        GaussianBlur(edges, edges, Size(7,7), 1.5, 1.5);
        Canny(edges, edges, 0, 30, 3);
        imshow("edges", edges);
        if(waitKey(30) >= 0) break;
 */    }
    // the camera will be deinitialized automatically in VideoCapture destructor
    return 0;
}