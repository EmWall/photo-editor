import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class photoNegative {
    
	public static void main(String[] args) {
        //handle the case of 0 argument or too many arguments
        if(args.length != 1){
            System.out.println("Please try again with one file path to a bmp image");
        }
        //handle the case of incorrect file format
        else if(!args[0].contains(".bmp")){
            System.out.println("Please try again with a BMP file");
        }
        else{
            //get path to image from arguments
            String ogPath = args[0];
            //call negate to invert photo
            String newPath = negate(ogPath);
            //print path to inverted photo
            System.out.println(newPath);
        }
	}

    //get image from path, invert, save in results, and return path to new image
    private static String negate(String ogPath){

        //get current working directory, back up to src, and concatenate with \results to create newPath
        String userDirectory = new File("").getAbsolutePath();
        String newPath = userDirectory.substring(0, userDirectory.length() - "\\main\\java\\com\\interview\\task".length()) + "\\results\\";
        
        try{
            //Create original file and file stream
            File ogFile = new File(ogPath);
            FileInputStream ogFileStream = new FileInputStream(ogFile);
            
            //Create new bmp file and file stream
            String fileName = ogFile.getName().replace(".bmp", "");
            newPath += fileName + "_Negated.bmp";
            File newFile = new File(newPath);
            FileOutputStream newFileStream = new FileOutputStream(newFile);
           
            //Get and parse headers
            int fileHeaderLength = 14;
            byte[] fileHeader = new byte[fileHeaderLength];
            ogFileStream.read(fileHeader, 0, fileHeaderLength);
            int infoHeaderLength = 40;
            byte[] infoHeader = new byte[infoHeaderLength];
            ogFileStream.read(infoHeader, 0, infoHeaderLength);
            int width = (((int)infoHeader[7]&0xff)<<24) | (((int)infoHeader[6]&0xff)<<16) | (((int)infoHeader[5]&0xff)<<8) | (int)infoHeader[4]&0xff;
            int height = (((int)infoHeader[11]&0xff)<<24) | (((int)infoHeader[10]&0xff)<<16) | (((int)infoHeader[9]&0xff)<<8) | (int)infoHeader[8]&0xff;
            int bitCount = (((int)infoHeader[15]&0xff)<<8) | (int)infoHeader[14]&0xff;
            int pad = 4 - ((width*3) % 4);
            if(pad == 4){
                pad = 0;
            }
            
            //Make sure image uses 24 bits per pixel
            if(bitCount != 24){
                System.out.println(bitCount);
                ogFileStream.close();
                newFileStream.close();
                return "Sorry this file does not have 24 bits per pixel. Please try again with a new bmp file.";
            }            

            //Write File Header and Info Header
            for(byte b : fileHeader){
                newFileStream.write(b);
            }
            for(int i = 0; i < infoHeaderLength - 4; i++){
                newFileStream.write(infoHeader[i]);
            }
                //Set Total Colors and Important Colors to 0
                //This prompts the renderer to use 2^24 colors and prioritize all colors equally
            for(int i = 0; i < 4; i++){        
                newFileStream.write(0x00);
            }

            //Get array of ogPixels 
            byte[] ogPixels = new byte [(width + pad) * 3 * height];
            ogFileStream.read(ogPixels, 0, (width + pad) * 3 * height);
            
            //Invert colors and save as int
            int index = 0;
            int[] inverts = new int[height * width];
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    int color = (255&0xff)<<24 | (((int)ogPixels[index+2]&0xff)<<16) | (((int)ogPixels[index+1]&0xff)<<8) | (int)ogPixels[index]&0xff;
                    int r = 255 - (color >> 16) & 0xff;
                    int g = 255 - (color >> 8) & 0xff;
                    int b = 255 - color & 0xff;
                    inverts[width*(height - i - 1) + j] = (r<< 16) | (g<<8) | b;
                    index += 3;
                }
                index += pad;       //0 padded to the end of the row
            }

            //Convert color from int to bytes and write to new file
            //NOTE: bmps are read from the bottom-up and left-to-right
            int rowCount = 1;
            int limit = (width * height) - 1;
            int rowIndex = limit - width;
            int lastRow = rowIndex;
            for(int i = 0; i < limit; i++){
                int color = inverts[rowIndex];
                newFileStream.write((byte)(color & 0xff));
                newFileStream.write((byte)((color >> 8) & 0xff));
                newFileStream.write((byte)(color >> 16) & 0xff);
                if(rowCount == width){
                    for(int j = 1; j <= pad; j++){
                        newFileStream.write(0x00);
                    }
                    rowCount = 1;
                    rowIndex = lastRow - width;
                    lastRow = rowIndex;
                }
                else{
                    rowCount++;
                }
                rowIndex++;
            }
            //clean up
            ogFileStream.close();
            newFileStream.close();
        }
        //catch exceptions associated with FileInputStream and FileOutputStream
        catch(Exception e){
            e.printStackTrace();
        }
        //return path to new file
        return newPath;
    }
}
