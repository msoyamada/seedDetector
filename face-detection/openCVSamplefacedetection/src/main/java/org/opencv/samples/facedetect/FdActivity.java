package org.opencv.samples.facedetect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import static org.opencv.core.Core.FONT_HERSHEY_PLAIN;
import static org.opencv.core.Core.inRange;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2HSV;
import static org.opencv.imgproc.Imgproc.RETR_LIST;
import static org.opencv.imgproc.Imgproc.arrowedLine;
import static org.opencv.imgproc.Imgproc.circle;
import static org.opencv.imgproc.Imgproc.contourArea;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.findContours;
import static org.opencv.imgproc.Imgproc.medianBlur;
import static org.opencv.imgproc.Imgproc.minEnclosingCircle;
import static org.opencv.imgproc.Imgproc.putText;


class CustomComparator implements Comparator<MatOfPoint> {
    public int compare(MatOfPoint object1, MatOfPoint object2) {
        if (object1.toArray()[0].x < object2.toArray()[0].x)
            return 1;
        else
            return -1;
    }

}


public class FdActivity extends Activity implements CvCameraViewListener2 {

    private static final String    TAG                 = "OCVSample::Activity";
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    public static final int        JAVA_DETECTOR       = 0;
    public static final int        NATIVE_DETECTOR     = 1;

    private MenuItem               increasemaxSeedSize;
    private MenuItem               decreasemaxSeedSize;
    private MenuItem               increaseminSeedSize;;
    private MenuItem               decreaseminSeedSize;
    private MenuItem               mItemType;
    private MenuItem               config;

    private Mat                    mRgba;
    private Mat                    mGray;
    private Mat                    hsv;
    private Mat                    grayD;


    private File                   mCascadeFile;
    private CascadeClassifier      mJavaDetector;
    private DetectionBasedTracker  mNativeDetector;

    private int                    mDetectorType       = JAVA_DETECTOR;
    private String[]               mDetectorName;

    //private float                  mRelativeFaceSize   = 0.2f;
    //private int                    mAbsoluteFaceSize   = 0;
    private int minSeedSize;
    private int maxSeedSize;
    private long begin;
    private long end;
    private int fps;

    static int lHSV[]= {80, 50,50};
    static int uHSV[]= {115,255,255};

    private CameraBridgeViewBase   mOpenCvCameraView;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization
                    System.loadLibrary("detection_based_tracker");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public FdActivity() {
        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";
        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.face_detect_surface_view);


        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        maxSeedSize= 25;
        minSeedSize= 3;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
        hsv  = new Mat();
        grayD = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
        hsv.release();
        grayD.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        begin = System.currentTimeMillis();
//        if (mAbsoluteFaceSize == 0) {
//            int height = mGray.rows();
//            if (Math.round(height * mRelativeFaceSize) > 0) {
//                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
//            }
//            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
//        }
//
//        MatOfRect faces = new MatOfRect();
//
//        if (mDetectorType == JAVA_DETECTOR) {
//            if (mJavaDetector != null)
//                mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
//                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
//        }
        if (mDetectorType == JAVA_DETECTOR) {


            cvtColor(mRgba, hsv, COLOR_RGB2HSV);
            Scalar lower= new Scalar(lHSV[0], lHSV[1], lHSV[2]);
            Scalar upper= new Scalar(uHSV[0], uHSV[1], uHSV[2]);
            inRange(hsv, lower, upper, grayD);
            medianBlur(grayD, grayD, 11);

            ArrayList<MatOfPoint> contours= new ArrayList<MatOfPoint>();
            Mat hierarchy= new Mat();

            findContours( grayD, contours, hierarchy, RETR_LIST, CHAIN_APPROX_SIMPLE, new Point(0, 0) );
            //sort contours

            Collections.sort(contours, new CustomComparator());


            /// Draw contours
            for( int i = 0; i< contours.size(); i++ )
            {
                Scalar color = new Scalar( 255, 0, 0 );
                //drawContours( drawing, contours, i, color, 2, 8, hierarchy, 0, Point() );
                //double area= contourArea(contours.get(i));
                Point center= new Point();
                float[] radius= new float[10];
                minEnclosingCircle(new MatOfPoint2f(contours.get(i).toArray()), center, radius);
                if ((radius[0] < maxSeedSize) && (radius[0]> minSeedSize )) {

                    circle(mRgba,center, (int)radius[0], new Scalar(0,0,0), 3, 4,0);
                    if (i < contours.size()-1)
                    {
                        Point Ncenter= new Point();
                        float Nradius[]= new float[10];
                        minEnclosingCircle(new MatOfPoint2f(contours.get(i+1).toArray()), Ncenter, Nradius);

                        arrowedLine(mRgba , center, new Point(Ncenter.x, center.y), new Scalar(0,0,0), 3) ;
                        String s;
                        s = String.valueOf((int)(center.x- Ncenter.x));
                        s += " px";

                        putText(mRgba, s , new Point(Ncenter.x + 15, Ncenter.y -10), FONT_HERSHEY_PLAIN, 2, new Scalar(0,0,0),2);

                    }


                }

            }


        }
        else if (mDetectorType == NATIVE_DETECTOR) {
            if (mNativeDetector != null) {
                Mat dists = new Mat();

//                cvtColor(mRgba, hsv, COLOR_BGR2HSV);
//                Scalar lower= new Scalar(80, 50, 50);
//                Scalar upper= new Scalar(115, 255, 255);
//                inRange(hsv, lower, upper, grayD);
//                medianBlur(grayD, grayD, 11);

                mNativeDetector.detectSeed(mRgba, dists);

              //  mNativeDetector.detect(mGray, faces);
            }

        }
        else {
            Log.e(TAG, "Detection method is not selected!");
        }

//        Rect[] facesArray = faces.toArray();
//        for (int i = 0; i < facesArray.length; i++)
//            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
        end= System.currentTimeMillis();
        fps= (int)(1000/(end-begin));

        return mRgba;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Log.i(TAG, "called onCreateOptionsMenu");
        this.setTitle("OpenCV Seed Detector MinSeedSize "+minSeedSize + " MaxSeedSize="+ maxSeedSize + " FPS="+fps);
        increasemaxSeedSize = menu.add("Increase Max Seed Size");
        decreasemaxSeedSize= menu.add("Decrease Max Seed Size");
        increaseminSeedSize = menu.add("Increase Min Seed Size");
        decreaseminSeedSize = menu.add("Decrease Min Seed Size");
        mItemType   = menu.add(mDetectorName[mDetectorType]);
        config = menu.add("Config");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == increasemaxSeedSize)
            maxSeedSize+=1;
        else if (item == decreasemaxSeedSize)
            maxSeedSize-=1;
        else if (item == increaseminSeedSize)
            minSeedSize+=1;
        else if (item == decreaseminSeedSize)
            minSeedSize-=1;
        else if (item == mItemType) {
            int tmpDetectorType = (mDetectorType + 1) % mDetectorName.length;
            item.setTitle(mDetectorName[tmpDetectorType]);
            setDetectorType(tmpDetectorType);
        }
        else if (item == config){
            //open the Config Activity
            Intent intent = new Intent(this, Config.class);
            startActivity(intent);

        }
        this.setTitle("OpenCV Seed Detector MinSeedSize "+minSeedSize + " MaxSeedSize="+ maxSeedSize + " FPS= "+fps);
        return true;
    }


    private void setDetectorType(int type) {
        if (mDetectorType != type) {
            mDetectorType = type;

            if (type == NATIVE_DETECTOR) {
                Log.i(TAG, "Detection Based Tracker enabled");
                mNativeDetector.start();
            } else {
                Log.i(TAG, "Cascade detector enabled");
                mNativeDetector.stop();
            }
        }
    }
}
