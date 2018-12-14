package com.leikr.core.Graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AssetManagerFx {
    private AssetManager manager;

    public AssetManagerFx(AssetManager assetManager) {
        manager = assetManager;
    }

    /**
     * Creates a Texture based animation.
     * @param image Path of the image.
     * @param verticalFrames The number of frames to split the image into (vertically). Uses full width of image.
     * @param playMode THe playmode to use for the animation.
     * @return
     */
    public Animation<TextureRegion> createFullWidthSpriteAnimation(String image, int verticalFrames, Animation.PlayMode playMode) {
        Texture animTexture = getTextureFromPath(image);

        TextureRegion[][] tmpFrames = TextureRegion.split(animTexture, animTexture.getWidth(), animTexture.getHeight() / verticalFrames);

        Array animationFrames = new Array<>();
        for (int j = 0; j < verticalFrames; j++) {
            animationFrames.add(tmpFrames[j][0]);
        }
        float tmp = 1f / verticalFrames;
        Animation<TextureRegion> animation = new Animation<>(tmp, animationFrames);
        animation.setPlayMode(playMode);

        return animation;
    }

    /**
     * Disposes the asset.
     * @param imagePath
     */
    public void disposeAsset(String imagePath) {
        if (manager.isLoaded(imagePath)) {
            manager.unload(imagePath);
        }

    }


    public Texture getTextureFromPath(String imagePath) {
        return (Texture) getFromPath(imagePath, Texture.class);
    }

    public Pixmap getPixmapFromPath(String imagePath) {
        return (Pixmap) getFromPath(imagePath, Pixmap.class);
    }

    /**
     * If path is already in the manager, get it.
     *
     * If not...
     *   load the texture into the manager by path and class Texture.
     *   finishes the loading process to memory
     *   get texture
     *
     * @param assetPath Path of the image to load as a texture.
     * @return Texture
     */
    private Object getFromPath(String assetPath, Class className)
    {
        // Makes a file handle from the image path and gets the string path
        String path = new FileHandle(assetPath).path();

        if (!manager.isLoaded(assetPath)) {
            manager.load(path, className);
            manager.finishLoading();
        }

        return manager.get(assetPath, className);
    }
}
