import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.image.BufferedImage;
public abstract class imageSmoothing extends imgProcessing
{
    protected static String path;
	protected abstract void smoothImage();
	protected static void writeImage() throws IOException {}
	protected abstract void setWidth(int w);
	protected abstract void setHeight(int h);
	protected void setImg(int w, int h){}
}