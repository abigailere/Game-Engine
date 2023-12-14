import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class GameObject {
    public AffineTransform transform; //the location/scale/rotation of our object
    public Shape shape; //the collider/rendered shape of this object
    public Material material; //data about the fill color, border color, and border thickness
    public ArrayList<ScriptableBehavior> scripts = new ArrayList<>(); //all scripts attached to the object
    public boolean active = true; //whether this gets Updated() and Draw()n
    private int level;
    private int lives;

    public int GetLives(){
        return this.lives;
    }
    public int GetLevel(){
        return this.level;
    }
    public void SetLives(int lives){
        this.lives = lives;
    }
    public void SetLevel(int level){
        this.level = level;
    }

    //create the default GameObject use a default AffineTransform, default Material, and a 10x10 pix rectangle Shape at 0,0
    public GameObject(){
        transform = new AffineTransform();
        material = new Material();
        shape = new Rectangle2D.Float(0,0,10,10);
    }

    //create the default GameObject, but with its AffineTransform translated to the coordinate x,y
    public GameObject(int x, int y){
        transform = new AffineTransform();
        material = new Material();
        shape = new Rectangle2D.Float(0, 0, 10, 10);
        transform.translate(x, y);
    }

    //draw either the styled shape, or the image scaled to the bounds of the shape.
    public void Draw(Graphics2D pen){
        if(active){
            AffineTransform temp = pen.getTransform();
            pen.transform(transform);
            if(material.isShape){
                pen.transform(temp);
                pen.setColor(material.getFill());
                pen.fill(shape);
                pen.setStroke(new BasicStroke(material.getBorderWidth()));
                pen.setColor(material.getBorder());
                pen.draw(shape);
                pen.setTransform(temp);
            }else{
                float width = (float) shape.getBounds().getWidth();
                float height = (float)shape.getBounds().getHeight();
                float imgW = width/material.img.getWidth();
                float imgH = height/material.img.getHeight();
                pen.scale(imgW, imgH);
                pen.drawImage(material.img, 0, 0, null);
                pen.setTransform(temp);
            }
        }

    }
    //start all scripts on the object
    public void Start(){
        if(active){
            for(int i = 0; i<scripts.size(); i++){
                scripts.get(i).Start();
            }
        }
    }

    //update all scripts on the object
    public void Update(){
        if (active){
            for(int i = 0; i<scripts.size(); i++){
                scripts.get(i).Update();
            }
        }
    }

    //move the GameObject's transform
    public void Translate(float dX, float dY){
        transform.translate(dX, dY);
    }

    //scale the GameObject's transform around the CENTER of its shape
    public void Scale(float sX, float sY){
        double old_width = shape.getBounds2D().getWidth();
        double old_height = shape.getBounds2D().getHeight();

        transform.scale(sX, sY);

        double new_width = shape.getBounds2D().getWidth();
        double new_height = shape.getBounds().getHeight();

        double scale = (-1)*(new_width - old_width) / 2;
        double scaleH = (-1)*(new_height - old_height) /2;

        transform.translate(scale, scaleH);
    }

    public boolean CollidesWith(GameObject other){
        Shape s1 = transform.createTransformedShape(shape);
        Area a1 = new Area(s1);
        Shape s2 = other.transform.createTransformedShape(other.shape);
        Area a2 = new Area(s2);
        a1.intersect(a2);
        if(a1.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean Contains(Point2D point){
        Shape s1 = transform.createTransformedShape(shape);
        if(s1.contains(point)){
            return true;
        }
        return false;
    }

}
