package v1.engine.entity;

import v1.engine.draw.Texture;
import v1.engine.draw.mesh.HeightMapMesh;
import v1.engine.util.ShaderUtil;
import v1.engine.util.loaders.ImageLoader;
import v1.engine.util.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Terrain {

    private Entity[] entities;
    private int blocksPerRow;
    Texture grass, stone;
    private HeightMapMesh og;

    float scale;

    public Terrain(){

    }

    public void init(int blocksPerRow, float scale, float minY, float maxY,
           String heightMapFile, String grassTexture, String stoneTexture, int textInc) throws Exception {

        this.blocksPerRow = blocksPerRow;
        entities = new Entity[blocksPerRow*blocksPerRow];

        this.scale = scale;

        og = new HeightMapMesh(minY, maxY, heightMapFile, textInc);

        grass = ImageLoader.loadImage(grassTexture);
        stone = ImageLoader.loadImage(stoneTexture);

        for(int row = 0; row < blocksPerRow; row++){
            for(int col = 0; col < blocksPerRow; col++){
                float xDisplacement = (col - ((float) blocksPerRow - 1)/ (float)2)*scale*HeightMapMesh.getXLength();
                float zDisplacement = (row - ((float) blocksPerRow - 1)/ (float)2)*scale*HeightMapMesh.getZLength();

                Entity terrainBlock = new StaticEntity(og.getMesh());
                terrainBlock.setScale(scale);
                terrainBlock.setPos(xDisplacement, 0, zDisplacement);
                entities[row*blocksPerRow+col] = terrainBlock;
            }
        }
    }

    public void render(ShaderUtil terrainShader, Transformation transformation, Matrix4f viewMatrix){

        for(int row = 0; row < blocksPerRow; row++){
            for(int col = 0; col < blocksPerRow; col++) {
                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(entities[row*blocksPerRow+col], viewMatrix);
                terrainShader.setUniform("modelViewMatrix", modelViewMatrix);
                entities[row*blocksPerRow+col].getMesh().render(this);
            }
        }
    }

    public float getHeight(Vector3f pos, float playerHeight){
        return (og.getHeight(pos)*scale)+playerHeight;
    }

    public Entity[] getEntities(){
        return entities;
    }

    public Texture getGrass() {
        return grass;
    }

    public void setGrass(Texture grass) {
        this.grass = grass;
    }

    public Texture getStone() {
        return stone;
    }

    public void setStone(Texture stone) {
        this.stone = stone;
    }
}