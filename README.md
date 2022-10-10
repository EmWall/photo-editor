Hello! 

I'm Emmett Wall and this is my solution for the BlockApps' Backend Developer Interview Task. 

REQUIREMENTS/LIMITATIONS: 
jdk must be installed
file must be .bmp
bmp file must be 24 bits per pixel
file must be uncompressed

INSTRUCTIONS:
 - Given Java is installed, photoNegative can be run on command line
   Starting from this directory(blockapps), navigate to src\main\java\com\interview\task and enter

javac photoNegative.java
java photoNegative C:\Users\Emmett\.vscode\blockapps\src\main\resources\com\interview\task\images\snail.bmp

 - You must replace the file path "C:...snail.bmp" with the path to any bmp image you like 
   or you can copy the path to one of the 4 images I provided.
   A folder of images and a folder of their negatives (expectedResults) can be found in src\resources\com\interview\task.

EXPECTED RESULTS:
The program prints the absolute path to the photo negative of the provided image.
Additionally, one can navigate to src\results and look for the image of the same name as the one provided
(if you use snail.bmp as in my example above, the program creates snail_Negated.bmp)

POSSIBLE EXTENSIONS: 
With more time I could implement a cache to minimize the number of times I invert the same byte, extend the program to work for compressed image files or any amount of bytes per pixel. 
With more libraries, I could easily extend the program to work on any image format. 