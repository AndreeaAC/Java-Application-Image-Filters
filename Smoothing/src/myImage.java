import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import java.util.Scanner;
public class myImage extends imageSmoothing {
	private static int width;
	private static int height;
	byte[] header;
    byte[] body;
	static int rgb_buffer[][][];
	public myImage(){}
	public myImage(int height, int width, byte[] header, byte[] body) {
		this.height = height;
        this.width = width;
        this.header = header;
        this.body = body;
	}
	public myImage(int height, int width, byte[] header, byte[] body, String path) {
		this.height = height;
        this.width = width;
        this.header = header;
        this.body = body;
        this.path = path;
	}
	public void setImg(int w, int h){
		this.width=w;
		this.height=h;
	}
	public void setWidth(int w){
		this.width=w;
	}
	public void setHeight(int h){
		this.height=h;
	}
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
	public static void main(String args[])throws IOException{
		double startTime = System.nanoTime();
		myImage image = readImage();
		System.out.println("Rata de smooth pentru imagine:");
		Scanner keyboard = new Scanner(System.in);
		int n = (keyboard.nextInt());
		for(int i=1; i<=n; i++)
			image.smoothImage();
		writeImage(image);
		double stopTime = System.nanoTime();
		double elapsedTime = (stopTime - startTime) / 1000000000.0;
        System.out.println("Timpul de executie pentru main: " + elapsedTime + " secunde");
	}
	public static myImage readImage() throws FileNotFoundException, IOException{
		double startTime = System.nanoTime();
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Calea imaginii care va fi procesata:");
		path=keyboard.next();
		File f = new File(path);
		while ( !f.exists() ){
            System.out.println("Imaginea nu a fost gasita! Mai incercati!");
            path=keyboard.next();
            f = new File(path);
        }
		byte[] b = Files.readAllBytes(Paths.get(path));
        
        byte[] header = new byte[54];
        for ( int i = 0 ; i < 54 ; i ++ )
            header[i] = b[i];
        
        // Formarea dimensiunilor folosind cei 8 biti din headerul imaginii
        width = ((header[21]&0xFF)<<24)|((header[20]&0xFF)<<16)|((header[19]&0xFF)<<8)|((header[18]&0xFF));//x
        height= ((header[25]&0xFF)<<24)|((header[24]&0xFF)<<16)|((header[23]&0xFF)<<8)|((header[22]&0xFF));//y
        // Pixelii se retin in body
        byte[] body = new byte[3*width*height];
        for ( int i = 54 ; i < 3*width*height ; i++ )
            body[i-54] = b[i];
        
        myImage Img = new myImage(height, width, header, body); 
		try{
			f = new File(path);
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			img = ImageIO.read(f);
			rgb_buffer = new int[3][img.getHeight()][img.getWidth()];
			System.out.println("Citirea imaginii s-a incheiat fara erori.");
			}catch(IOException e){
			System.out.println("Error: "+e);
			}
		double stopTime = System.nanoTime();
		double elapsedTime = (stopTime - startTime) / 1000000000.0;
        System.out.println("Timpul de executie pentru read: " + elapsedTime + " secunde");
        return Img;
	}
	public void readPixels(){
		double startTime = System.nanoTime();
		for(int row = 0; row < img.getHeight(); row++){
			for(int col = 0; col < img.getWidth(); col++){
				Color c = new Color(img.getRGB(col,row));
				rgb_buffer[0][row][col] = c.getRed();
				rgb_buffer[1][row][col] = c.getGreen();
				rgb_buffer[2][row][col] = c.getBlue();	   
	       }
		}
		double stopTime = System.nanoTime();
		double elapsedTime = (stopTime - startTime) / 1000000000.0;
        System.out.println("Timpul de executie pentru readpixels: " + elapsedTime + " secunde");
	}
	public void smoothImage(){
		double startTime = System.nanoTime();
		readPixels();
		for(int row = 1; row < img.getHeight()-1; row++){
			for(int col = 1; col < img.getWidth()-1; col++){
				int r = rgb_buffer[0][row-1][col-1]+
					    rgb_buffer[0][row-1][col]+
					    rgb_buffer[0][row-1][col+1]+
						   
					    rgb_buffer[0][row][col-1]+
					    rgb_buffer[0][row][col]+
					    rgb_buffer[0][row][col+1]+
						   
					    rgb_buffer[0][row+1][col-1]+
					    rgb_buffer[0][row+1][col]+
					    rgb_buffer[0][row+1][col+1];
						
				int g = rgb_buffer[1][row-1][col-1]+
					    rgb_buffer[1][row-1][col]+
					    rgb_buffer[1][row-1][col+1]+
								   
					    rgb_buffer[1][row][col-1]+
					    rgb_buffer[1][row][col]+
					    rgb_buffer[1][row][col+1]+
						   
					    rgb_buffer[1][row+1][col-1]+
					    rgb_buffer[1][row+1][col]+
					    rgb_buffer[1][row+1][col+1];
						
				int b = rgb_buffer[2][row-1][col-1]+
					    rgb_buffer[2][row-1][col]+
					    rgb_buffer[2][row-1][col+1]+
								   
					    rgb_buffer[2][row][col-1]+
					    rgb_buffer[2][row][col]+
					    rgb_buffer[2][row][col+1]+
								   
					    rgb_buffer[2][row+1][col-1]+
					    rgb_buffer[2][row+1][col]+
					    rgb_buffer[2][row+1][col+1];
						
				Color c=new Color(r/9,g/9,b/9);
				img.setRGB(col,row,c.getRGB());
			}
		}
		double stopTime = System.nanoTime();
		double elapsedTime = (stopTime - startTime) / 1000000000.0;
        System.out.println("Timpul de executie pentru smoothing: " + elapsedTime + " secunde");
	}
	public static void writeImage(myImage modifiedImage) throws IOException{
		double startTime = System.nanoTime();
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Calea unde se va salva imaginea procesata:");
		path=keyboard.next();
		keyboard.close();
		File f = new File(path);
		try{
		      f = new File(path);
		      ImageIO.write(img, "bmp", f);
		      System.out.println("Scrierea noii imagini s-a incheiat fara erori.");
		      }catch(IOException e){
		      System.out.println("Error: "+e);
		}
		double stopTime = System.nanoTime();
		double elapsedTime = (stopTime - startTime) / 1000000000.0;
        System.out.println("Timpul de executie pentru write: " + elapsedTime + " secunde");
	}
}

