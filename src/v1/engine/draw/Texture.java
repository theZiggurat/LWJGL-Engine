package v1.engine.draw;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private final int id;
    private int width, height;

    // initialize empty texture
    public Texture(int id) {
        this.id = id;
        width = 0;
        height = 0;
    }

    public Texture(int id, int _width, int _height) {
        this.id = id;
        this.width = _width;
        this.height = _height;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void cleanup(){
        glDeleteTextures(id);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getId(){return id;}
}