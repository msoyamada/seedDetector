I = imread('teste12.png');
%E = edge(rgb2gray(I),'canny', 0.60)
IgrayRG = 0.9*I(:,:,1)+ 0.1*I(:,:,2) + 0.1*I(:,:,3);
%IgrayRG= rgb2gray(I);
hsv= rgb2hsv(I);


figure, colormap default, image(I);
figure; image(IgrayRG,'CDataMapping','scaled'); colormap('gray');
figure, colormap hsv, image(hsv);

s= size(IgrayRG)
for i= 1:s(1)
    for j= 1:s(2)
         
         if IgrayRG(i,j) < 255  && IgrayRG(i,j) > 200
             if (i > 1) && (j < s(2)-1) && (i < s(1)) && (j > 1)
                 if (IgrayRG(i-1, j) == 255) || (IgrayRG(i+1, j) == 255) || (IgrayRG(i, j-1) == 255) || (IgrayRG(i, j+1) ==255)
                     IgrayRG(i+1, j) =255;
                     IgrayRG(i-1, j) =255;
                     IgrayRG(i, j-1) =255;
                     IgrayRG(i, j+1) =255;
                     IgrayRG(i, j) =255;
                 end 
             end
         end
         
          if IgrayRG(i,j) <  20 
             if (i > 1) && (j < s(2)-1) && (i < s(1)) && (j > 1)
                 if (IgrayRG(i-1, j) ==0) || (IgrayRG(i+1, j) ==0) || (IgrayRG(i, j-1) ==0) || (IgrayRG(i, j+1)==0)
                     IgrayRG(i+1, j) =0;
                     IgrayRG(i-1, j) =0;
                     IgrayRG(i, j-1) =0;
                     IgrayRG(i, j+1) =0;
                     IgrayRG(i, j) =0;
                 end 
             end
         end
         
    end
end

Kmedian = medfilt2(IgrayRG);
imshowpair(I,Kmedian,'montage')



E= edge(Kmedian, 'canny', 0.68);
figure, colormap colorcube, image(E);
% 
% % override some default parameters
params.minMajorAxis = 10;
params.maxMajorAxis = 30;
params.numBest = 30;
% 
bestFits = ellipseDetection(E, params);
% 
fprintf('Output %d best fits.\n', size(bestFits,1));
% 
figure, colormap flag, image(E);
 %ellipse drawing implementation: http://www.mathworks.com/matlabcentral/fileexchange/289 
ellipse(bestFits(:,3),bestFits(:,4),bestFits(:,5)*pi/180,bestFits(:,1),bestFits(:,2),'k');

