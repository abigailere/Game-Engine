import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Material {
    Color fill, border;
    int borderWidth;
    boolean isShape = true;
    BufferedImage img;

    //create default, black fill and border with zero borderWidth
    Material(){
        fill = Color.black;
        border = Color.BLACK;
        borderWidth = 0;
    }

    public Material(Color fill, Color border, int borderWidth) {
        this.fill = fill;
        this.border = border;
        this.borderWidth = borderWidth;
    }

    public Material(String path){
        setImg(path);
        isShape = false;
    }

    //Getters and Setters
    public Color getFill() {
        return fill;
    }

    public void setFill(Color fill) {
        this.fill = fill;
    }

    public Color getBorder() {
        return border;
    }

    public void setBorder(Color border) {
        this.border = border;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int stroke_width) {
        this.borderWidth = stroke_width;
    }

    public BufferedImage getImg(){return img;}

    public void setImg(String path){
        try{
            img = ImageIO.read(new File(path));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void setImg(BufferedImage img){this.img=img; this.isShape = false;}


}
