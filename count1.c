#include <stdio.h>
#include <stdlib.h>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include "opencv2/imgproc/imgproc.hpp"
#include <iostream>

using namespace cv;
using namespace std;



#define BLACK 0
#define QUEUE_SIZE 3000

// Funciona como um BFS (Breadth First Search), em que cada vertice ou
// pixel tem uma aresta conectando com os quatro vizinhos adjacentes
unsigned count_cluster(int **imagem, int **visited, int row, int col, int N, int M)
{
	int x[] = {-1, 0, 1, 0}, y[] = {0, 1, 0, -1};
	
	int queue_r[QUEUE_SIZE], queue_c[QUEUE_SIZE], head = 0, tail = 1;
	unsigned count = 0, i;
	
	queue_r[head] = row, queue_c[head] = col;
	
	while(head < tail)
	{
		int act_row = queue_r[head], act_col = queue_c[head++];
		
		for(i = 0; i < 4; i++)
		{
			// acha os indices dos vizinhos
			int new_row = act_row + x[i], new_col = act_col + y[i];
			
			// checa se a posicao eh valida
			if(new_row >= 0 && new_row < N && new_col >= 0 && new_col < M)			
			{	
				// se for um pixel preto e nao foi visitado, visita ele
				if(imagem[new_row][new_col] == BLACK && !visited[new_row][new_col]) 
				{
					visited[new_row][new_col] = 1;				
					queue_r[tail] = new_row;
					queue_c[tail++] = new_col;
					count++;
				}
			}
			
		}
		
	}
	
	return count;
}

void count(int **imagem, int N, int M)
{
	int **visited, counter = 0;
	unsigned i, j;
	
	visited = (int**)malloc(N * sizeof(int*));
	
	for(i = 0; i < N; i++)
		visited[i] = (int*)calloc(M, sizeof(int));
	
	for(i = 0; i < N; i++)
	{
		for(j = 0; j < M; j++)
		{
			if(imagem[i][j] == BLACK && !visited[i][j])
			{
				visited[i][j] = 1;
				printf("Cluster(%d) with %d pixels\n", counter++, count_cluster(imagem, visited, i, j, N, M));
			}
		}
	}
	
	free(visited);
}

int main( int argc, char** argv )
{
    if( argc != 2)
    {
     cout <<" Usage: display_image ImageToLoadAndDisplay" << endl;
     return -1;
    }

    Mat image;
    image = imread(argv[1]);   // Read the file

    if(! image.data )                              // Check for invalid input
    {
        cout <<  "Could not open or find the image" << std::endl ;
        return -1;
    }

    int rows = image.rows;
    int cols= image.cols;
    //gray= np.ndarray(shape=(s[0], s[1]), dtype=np.uint8)
    Mat gray(rows, cols, CV_8UC1);
    Mat grayM(rows, cols, CV_8UC1);
    
    Mat m = (Mat_<double>(1,3) << 0.1, 0.1, 0.9);

    //m= m.reshape((1,3));
    transform(image, gray, m); 
    
    //threshold
    threshold(gray, gray, 220, 255, CV_THRESH_BINARY);

    medianBlur(gray, grayM, 15);
    int cont=0;

    for (int i=10; i <(rows-10); i++)
	for (int j=10; j <(cols-10); j++)
			if (grayM.at<unsigned char>(i,j) == 0) cont++;	
		
	


}

/*
int main()
{
         

	return 0;
}*/
